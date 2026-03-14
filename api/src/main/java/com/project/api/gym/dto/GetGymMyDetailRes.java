package com.project.api.gym.dto;

import com.project.services.gym.model.UserBookingModel;
import com.project.services.gym.model.UserGymModel;
import com.project.services.gym.model.UserPassModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetGymMyDetailRes {
    private List<UserPass> passes;
    private List<UserBooking> bookings;

    public static GetGymMyDetailRes from(UserGymModel  userGymModel) {
        List<UserPass> passes = new ArrayList<>();
        for (UserPassModel p : userGymModel.pass()) {
            passes.add(new UserPass(
                    p.getPassId(), p.getName(), p.getValidFrom(), p.getValidUntil(), p.getRemainingUses()
            ));
        }

        List<UserBooking> bookings = new ArrayList<>();
        for (UserBookingModel b : userGymModel.bookings()) {
            bookings.add(new UserBooking(b.id(), b.startDateTime()));
        }

        return new GetGymMyDetailRes(passes, bookings);
    }
}
