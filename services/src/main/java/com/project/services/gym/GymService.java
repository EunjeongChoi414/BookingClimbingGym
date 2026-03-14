package com.project.services.gym;

import com.project.common.BusinessRegistrationTicket;
import com.project.domain.booking.Booking;
import com.project.domain.booking.BookingRepository;
import com.project.domain.gym.*;
import com.project.domain.user.User;
import com.project.domain.user.UserRepository;
import com.project.services.booking.model.BookingDetailModel;
import com.project.services.gym.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GymService {

    private final GymRepository gymRepository;
    private final UserRepository userRepository;
    private final UserGymRepository userGymRepository;
    private final BookingRepository bookingRepository;
    private final UserPassRepository userPassRepository;
    private final BusinessVerifier businessVerifier;
    private final String jwtSecret;

    public GymService(
            @Value("${jwt.secret}") String jwtSecret,
            GymRepository gymRepository, UserRepository userRepository,
            UserGymRepository userGymRepository, BookingRepository bookingRepository,
            UserPassRepository userPassRepository, BusinessVerifier businessVerifier) {
        this.jwtSecret = jwtSecret;
        this.gymRepository = gymRepository;
        this.userRepository = userRepository;
        this.userGymRepository = userGymRepository;
        this.bookingRepository = bookingRepository;
        this.userPassRepository = userPassRepository;
        this.businessVerifier = businessVerifier;
    }

    public String registerGym(String name, String address, String contact,
                              List<BusinessHoursModel> businessHours,
                              List<PassModel> passes, String userId, int maxCapacity) {
        User owner = userRepository.findById(userId);

        List<BusinessHours> domainBusinessHours = businessHours.stream()
                .map(bh -> new BusinessHours(bh.getDay(), bh.getStartTime(), bh.getEndTime()))
                .toList();

        List<Pass> domainPasses = passes.stream()
                .map(p -> new Pass(p.getName(), p.getPrice(), p.getMaxUses(), p.getValidDays()))
                .toList();

        Gym gym = new Gym(
                name, address, contact, domainBusinessHours, domainPasses, owner, maxCapacity);
        gymRepository.save(gym);

        //유저를 관리자로 만든다.
        return gym.getId();
    }

    public GymDetailModel getGymDetail(String gymId) {
        Gym gym = gymRepository.findById(gymId);

        List<BusinessHoursModel> businessHoursModels = new ArrayList<>();
        for (BusinessHours hours : gym.getBusinessHours()) {
            businessHoursModels.add(new BusinessHoursModel(hours.getDay(), hours.getStartTime(), hours.getEndTime()));
        }
        List<PassModel> passModels = new ArrayList<>();
        for (Pass p : gym.getPasses()) {
            passModels.add(new PassModel(p.getName(), p.getPrice(), p.getMaxUses(), p.getValidDays()));
        }

        return new GymDetailModel(gym.getName(), businessHoursModels, "Quiet", gym.getAddress(),
                passModels, gym.getContact());
    }

    public UserGymModel getUserInfoFromGym(String gymId, String userId) {
        UserGym userGym = userGymRepository.findByIds(gymId, userId);

        List<UserPassModel> passModels = new ArrayList<>();
        for (UserPass p : userGym.getPasses()) {
            Pass pass = p.getPass();
            passModels.add(new UserPassModel(
                    pass.getId(), pass.getName(),p.getValidFrom(), p.getValidUntil(), p.getRemainingUses()));
        }

        List<UserBookingModel> bookingModels = new ArrayList<>();
        for (UserBooking b : userGym.getBookings()) {
            bookingModels.add(new UserBookingModel(b.getId(), b.getStartDateTime()));
        }

        return new UserGymModel(passModels, bookingModels);
    }

    public List<GymPreviewModel> getGyms(int page, int size, String keyword) {
        List<Gym> searchedGyms = gymRepository.searchGyms(page, size, keyword);

        List<GymPreviewModel> gyms = new ArrayList<>();
        searchedGyms.forEach(gym -> {
            List<BusinessHoursModel> businessHoursModels = new ArrayList<>();

            for (BusinessHours hours : gym.getBusinessHours()) {
                businessHoursModels.add(new BusinessHoursModel(hours.getDay(), hours.getStartTime(), hours.getEndTime()));
            }
            gyms.add(new GymPreviewModel(gym.getName(), businessHoursModels, "Quiet", gym.getAddress()));
        });

        return gyms;
    }

    public BookedWithPassModel bookGymWithPass(
            String userId, String gymId, String passId, LocalDateTime startDateTime){
        UserPass userPass = userPassRepository.findById(passId);
        if(!userPass.getUser().getId().equals(userId)){
            throw new RuntimeException("유저의 패스가 아닙니다.");
        }
        userPass.isValid();

        Gym gym = gymRepository.findById(gymId);
        Booking booking = new Booking(userId, gym, passId, startDateTime);
        bookingRepository.add(booking);

        userPass.usePass();
        return new BookedWithPassModel(booking.getId(), userPass.getRemainingUses(), booking.getQrToken());
    }

    public List<BookingDetailModel> getGymBookings(String userId, String gymId){
        Gym gym = gymRepository.findById(gymId);
        if (!gym.getOwner().getId().equals(userId)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        List<Booking> gymBookings = bookingRepository.getAllBy(gymId);
        List<BookingDetailModel> models = new ArrayList<>();
        gymBookings.forEach(booking -> {
            models.add(new BookingDetailModel(booking.getId(), booking.getUserId(), booking.getBookedDateTime(), booking.getPassId()));
        });

        return models;
    }

    public String getCrowdedness(String gymId, LocalDateTime dateTime) {
        Gym gym = gymRepository.findById(gymId);
        int bookingCount = bookingRepository.getBookingCount(gymId, dateTime);
        Crowdedness crowdedness  = Gym.getCrowdedness(gym.getMaxCapacity(), bookingCount);
        return crowdedness.toString();
    }

    public String verifyBusiness(
            String businessRegistrationNumber, String representativeName, LocalDate businessStartDate) {
        businessVerifier.verifyBusiness(businessRegistrationNumber, representativeName, businessStartDate);
        com.project.common.BusinessRegistrationTicket ticket = BusinessRegistrationTicket.issue(representativeName, jwtSecret);

        return ticket.getToken();
    }

    public void verifyBusinessRepresentative(
            String ticket, String accountHolderName, String bankName, String accountNumber) {
        // 사업자 정보 인증되었는지 확인하기
        BusinessRegistrationTicket parsedTicket = BusinessRegistrationTicket.parse(ticket, jwtSecret);

        // 이름이 같은지 확인한다.
        if(!parsedTicket.getRepresentativeName().equals(accountHolderName)) {
            throw new RuntimeException("사업자와 계좌 소유주의 이름이 같아야 합니다.");
        }
        // 실제로 존재하는 계좌인지 확인한다
        businessVerifier.verifyBusinessRepresentative(accountHolderName, bankName, accountNumber);
    }
}