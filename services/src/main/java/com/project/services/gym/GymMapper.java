package com.project.services.gym;

import com.project.domain.booking.Booking;
import com.project.domain.gym.BusinessHours;
import com.project.domain.gym.Pass;
import com.project.domain.gym.UserPass;
import com.project.services.gym.model.BusinessHoursModel;
import com.project.services.gym.model.PassModel;
import com.project.services.gym.model.BookingModel;
import com.project.services.gym.model.UserPassModel;

import java.util.List;

public class GymMapper {

    public static BusinessHours toPassDomain(BusinessHoursModel model) {
        return new BusinessHours(model.getDay(), model.getStartTime(), model.getEndTime());
    }

    public static BusinessHoursModel toModel(BusinessHours domain) {
        return new BusinessHoursModel(domain.getDay(), domain.getStartTime(), domain.getEndTime());
    }

    public static Pass toPassDomain(PassModel model) {
        return new Pass(model.getName(), model.getPrice(), model.getMaxUses(), model.getValidDays());
    }

    public static PassModel toModel(Pass domain) {
        return new PassModel(domain.getName(), domain.getPrice(), domain.getMaxUses(), domain.getValidDays());
    }

    public static UserPassModel toModel(UserPass domain) {
        return new UserPassModel(
                domain.getPass().getId(), domain.getPass().getName(),
                domain.getValidFrom(), domain.getValidUntil(), domain.getRemainingUses());
    }

    public static BookingModel toModel(Booking domain) {
        return new BookingModel(domain.getId(), domain.getUserId(), domain.getBookedDateTime(), domain.getPassId());
    }

    public static List<BusinessHours> toBusinessHoursDomain(List<BusinessHoursModel> models) {
        return models.stream().map(GymMapper::toPassDomain).toList();
    }

    public static List<BusinessHoursModel> toBusinessHoursModels(List<BusinessHours> domains) {
        return domains.stream().map(GymMapper::toModel).toList();
    }

    public static List<Pass> toPassDomain(List<PassModel> models) {
        return models.stream().map(GymMapper::toPassDomain).toList();
    }

    public static List<PassModel> toPassModels(List<Pass> domains) {
        return domains.stream().map(GymMapper::toModel).toList();
    }

    public static List<UserPassModel> toUserPassModels(List<UserPass> domains) {
        return domains.stream().map(GymMapper::toModel).toList();
    }

    public static List<BookingModel> toBookingModels(List<Booking> domains) {
        return domains.stream().map(GymMapper::toModel).toList();
    }
}