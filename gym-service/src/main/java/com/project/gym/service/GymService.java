package com.project.gym.service;

import com.project.common.jwt.BusinessRegistrationTicket;
import com.project.gym.dto.*;
import com.project.gym.entity.Booking;
import com.project.gym.entity.BusinessHours;
import com.project.gym.entity.CrowdednessLevel;
import com.project.gym.entity.Gym;
import com.project.gym.entity.Pass;
import com.project.gym.entity.UserGym;
import com.project.gym.entity.UserPass;
import com.project.gym.repository.*;
import com.project.gym.port.BusinessVerifier;
import com.project.user.entity.User;
import com.project.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GymService {

    private final GymRepository gymRepository;
    private final UserService userService;
    private final UserGymRepository userGymRepository;
    private final BookingRepository bookingRepository;
    private final UserPassRepository userPassRepository;
    private final BusinessVerifier businessVerifier;
    private final String jwtSecret;
    private final Clock clock;

    public GymService(
            @Value("${jwt.secret}") String jwtSecret,
            GymRepository gymRepository, UserService userService,
            UserGymRepository userGymRepository, BookingRepository bookingRepository,
            UserPassRepository userPassRepository, BusinessVerifier businessVerifier, Clock clock) {
        this.jwtSecret = jwtSecret;
        this.gymRepository = gymRepository;
        this.userService = userService;
        this.userGymRepository = userGymRepository;
        this.bookingRepository = bookingRepository;
        this.userPassRepository = userPassRepository;
        this.businessVerifier = businessVerifier;
        this.clock = clock;
    }

    public String registerGym(String name, String address, String contact,
                              List<BusinessHoursModel> businessHours,
                              List<PassModel> passes, String userId, int maxCapacity,
                              int cancellationNoticeDays) {
        List<BusinessHours> domainBusinessHours = GymMapper.toBusinessHoursDomain(businessHours);
        List<Pass> domainPasses = GymMapper.toPassDomain(passes);

        BusinessHours.validate(domainBusinessHours);
        Gym gym = new Gym(
                name, address, contact, domainBusinessHours, domainPasses, userId,
                maxCapacity, cancellationNoticeDays);
        gymRepository.save(gym);
        userService.setManager(userId);

        return gym.getId();
    }

    public GymDetailModel getGymDetail(String gymId) {
        Gym gym = gymRepository.getById(gymId);
        List<BusinessHoursModel> businessHoursModels = GymMapper.toBusinessHoursModels(gym.getBusinessHours());
        List<PassModel> passModels = GymMapper.toPassModels(gym.getPasses());
        String currentCrowdedness = getCurrentCrowdednessLevel(gymId);

        return new GymDetailModel(
                gym.getName(), businessHoursModels, currentCrowdedness,
                gym.getAddress(), passModels, gym.getContact());
    }

    public UserGymModel getUserInfoFromGym(String gymId, String userId) {
        UserGym userGym = userGymRepository.findByIds(gymId, userId);

        List<UserPassModel> passModels = GymMapper.toUserPassModels(userGym.getPasses());
        List<BookingModel> bookingModels = GymMapper.toBookingModels(userGym.getBookings());

        return new UserGymModel(passModels, bookingModels);
    }

    public List<GymPreviewModel> getGyms(int page, int size, String keyword) {
        List<Gym> searchedGyms = gymRepository.searchGyms(page, size, keyword);

        List<GymPreviewModel> gyms = new ArrayList<>();
        searchedGyms.forEach(gym -> {
            List<BusinessHoursModel> businessHoursModels = GymMapper.toBusinessHoursModels(gym.getBusinessHours());
            gyms.add(new GymPreviewModel(
                    gym.getName(), businessHoursModels, getCurrentCrowdednessLevel(gym.getId()), gym.getAddress()));
        });

        return gyms;
    }

    public BookedWithPassModel bookGymWithPass(
            String userId, String gymId, String userPassId, LocalDateTime startDateTime) {
        UserPass userPass = userPassRepository.getById(userPassId);
        userPass.validate(clock);

        Gym gym = gymRepository.getById(gymId);
        Booking booking = new Booking(userId, gym, userPassId, startDateTime, userPass, clock);
        bookingRepository.save(booking);

        return new BookedWithPassModel(booking.getId(), userPass.getRemainingUses(), booking.getQrToken());
    }

    public List<BookingModel> getGymBookings(String userId, String gymId) {
        Gym gym = gymRepository.getById(gymId);
        if (!gym.getOwnerId().equals(userId)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        List<Booking> gymBookings = bookingRepository.getAllBy(gymId);

        return GymMapper.toBookingModels(gymBookings);
    }

    public String getCrowdedness(String gymId, LocalDateTime dateTime) {
        Gym gym = gymRepository.getById(gymId);
        int bookingCount = bookingRepository.getGymBookingCount(gymId, dateTime);
        CrowdednessLevel level = gym.getCrowdedness(bookingCount);
        return level.toString();
    }

    public String verifyBusiness(
            String businessRegistrationNumber, String representativeName, LocalDate businessStartDate) {
        businessVerifier.verifyBusiness(businessRegistrationNumber, representativeName, businessStartDate);
        BusinessRegistrationTicket ticket = BusinessRegistrationTicket.issue(representativeName, jwtSecret);

        return ticket.getToken();
    }

    public void verifyBusinessRepresentative(
            String ticket, String accountHolderName, String bankName, String accountNumber) {
        BusinessRegistrationTicket parsedTicket = BusinessRegistrationTicket.parse(ticket, jwtSecret);

        businessVerifier.verifyBusinessAccount(
                parsedTicket.getRepresentativeName(), accountHolderName, bankName, accountNumber);
    }

    private String getCurrentCrowdednessLevel(String gymId) {
        int count = bookingRepository.getGymBookingCount(gymId, LocalDateTime.now(clock));
        Gym gym = gymRepository.getById(gymId);
        CrowdednessLevel crowdedness = gym.getCrowdedness(count);

        return crowdedness.toString();
    }
}
