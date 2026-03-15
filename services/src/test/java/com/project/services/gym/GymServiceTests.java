package com.project.services.gym;

import com.project.domain.booking.BookingRepository;
import com.project.domain.exception.InvalidPassException;
import com.project.domain.gym.*;
import com.project.domain.user.User;
import com.project.domain.user.UserRepository;
import com.project.services.gym.model.BookedWithPassModel;
import com.project.services.gym.model.BusinessHoursModel;
import com.project.services.gym.model.GymDetailModel;
import com.project.services.gym.model.PassModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GymServiceTests {
    private static final String SECRET = "test-secret-key-that-is-long-enough-for-hmac";
    private final GymRepository gymRepository = new com.project.infra.gym.GymRepository();
    private final UserRepository userRepository = new com.project.infra.user.UserRepository();
    private final UserGymRepository userGymRepository = new com.project.infra.gym.UserGymRepository();
    private final BookingRepository bookingRepository = new com.project.infra.booking.BookingRepository();
    private final UserPassRepository userPassRepository = new com.project.infra.gym.UserPassRepository();
    private final BusinessVerifier businessVerifier = new com.project.infra.gym.BusinessVerifier();
    private final Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

    private GymService sut;

    @BeforeEach
    void setUp() {
        sut = new GymService(
                SECRET, gymRepository, userRepository, userGymRepository, bookingRepository,
                userPassRepository, businessVerifier, fixedClock
        );
    }

    @Test
    void registerGym() {
        String name = "gymName";
        String address = "address";
        String contact = "contact";
        List<BusinessHoursModel> businessHours = new ArrayList<>();
        List<PassModel> passes = new ArrayList<>();
        int maxCap = 300;
        User user = createUser();
        int cancellationNoticeDays = 1;

        String gymId = sut.registerGym(name, address, contact, businessHours, passes, user.getId(), maxCap, cancellationNoticeDays);

        assertNotNull(gymId);
    }

    @Test
    void getGymDetail() {
        Gym gym = createGym();

        GymDetailModel gymDetailModel = sut.getGymDetail(gym.getId());

        assertNotNull(gymDetailModel);
        assertEquals(gym.getName(), gymDetailModel.name());
        assertEquals(gym.getAddress(), gymDetailModel.address());
        assertEquals(gym.getContact(), gymDetailModel.contact());
    }

    @Test
    void bookGymWithPass() {
        User user = createUser();
        Gym gym = createGym();
        LocalDate validFrom = LocalDate.now(fixedClock);
        LocalDateTime bookedAt = LocalDateTime.now(fixedClock).plusDays(2);
        UserPass userPass = new UserPass(gym.getPasses().get(0), user, validFrom);
        userPassRepository.add(userPass);

        BookedWithPassModel result = sut.bookGymWithPass(user.getId(), gym.getId(), userPass.getId(), bookedAt);

        assertNotNull(result);
        assertEquals(result.remainingUses(), userPass.getRemainingUses());
        assertNotNull(result.bookingId());
        assertNotNull(result.qrToken());
    }

    @Test
    void bookGymWithPass_failWithInvalidPass() {
        User user = createUser();
        Gym gym = createGym();
        LocalDate validFrom = LocalDate.now(fixedClock).minusDays(200);
        LocalDateTime bookedAt = LocalDateTime.now(fixedClock).plusDays(2);
        UserPass userPass = new UserPass(gym.getPasses().get(0), user, validFrom);
        userPassRepository.add(userPass);

        assertThrows(InvalidPassException.class, () -> {
            sut.bookGymWithPass(user.getId(), gym.getId(), userPass.getId(), bookedAt);
        });
    }

    private User createUser() {
        User user = new User("user@gmail.com", "password", fixedClock);
        userRepository.create(user);
        return user;
    }

    private Gym createGym() {
        String name = "gymName";
        String address = "address";
        String contact = "contact";
        BusinessHours monBh = new BusinessHours(DayOfWeek.MONDAY, null, null);
        List<BusinessHours> businessHours = List.of(monBh);
        Pass pass = new Pass("5회권", BigDecimal.valueOf(90000), 5, 180);
        List<Pass> passes = List.of(pass);
        int maxCap = 300;
        int cancellationNoticeDays = 1;
        User owner = createUser();

        Gym gym = new Gym(name, address, contact, businessHours, passes,
                owner, maxCap, cancellationNoticeDays);
        gymRepository.add(gym);
        return gym;
    }
}