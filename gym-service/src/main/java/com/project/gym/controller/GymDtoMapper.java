package com.project.gym.controller;

import com.project.gym.dto.*;

import java.util.List;

public class GymDtoMapper {

    public static BusinessHoursModel toModel(BusinessHours dto) {
        return new BusinessHoursModel(dto.getDay(), dto.getStartTime(), dto.getEndTime());
    }

    public static PassModel toModel(Pass dto) {
        return new PassModel(dto.getName(), dto.getPrice(), dto.getMaxUses(), dto.getValidDays());
    }

    public static List<BusinessHoursModel> toBusinessHoursModels(List<BusinessHours> list) {
        return list.stream().map(GymDtoMapper::toModel).toList();
    }

    public static List<PassModel> toPassModels(List<Pass> list) {
        return list.stream().map(GymDtoMapper::toModel).toList();
    }

    public static BusinessHours toDto(BusinessHoursModel model) {
        return new BusinessHours(model.getDay(), model.getStartTime(), model.getEndTime());
    }

    public static Pass toDto(PassModel model) {
        return new Pass(model.getName(), model.getPrice(), model.getMaxUses(), model.getValidDays());
    }

    public static List<BusinessHours> toBusinessHoursDtos(List<BusinessHoursModel> list) {
        return list.stream().map(GymDtoMapper::toDto).toList();
    }

    public static List<Pass> toPassDtos(List<PassModel> list) {
        return list.stream().map(GymDtoMapper::toDto).toList();
    }

    public static Gym toDto(GymPreviewModel model) {
        List<BusinessHours> businessHours = model.businessHours().stream().map(GymDtoMapper::toDto).toList();
        return new Gym(model.name(), businessHours, model.currentCrowdLevel(), model.address());
    }

    public static GetGymDetailRes toDto(GymDetailModel model) {
        List<BusinessHours> businessHours = model.businessHours().stream().map(GymDtoMapper::toDto).toList();
        List<Pass> passes = model.passes().stream().map(GymDtoMapper::toDto).toList();
        return new GetGymDetailRes(model.name(), businessHours, model.isBusy(), model.address(), passes, model.contact());
    }

    public static Booking toDto(BookingModel model) {
        return new Booking(model.id(), model.userId(), model.dateTime(), model.passId());
    }

    public static List<Booking> toBookingDtos(List<BookingModel> list) {
        return list.stream().map(GymDtoMapper::toDto).toList();
    }

    public static PayWithPassRes toDto(BookedWithPassModel model) {
        return new PayWithPassRes(model.bookingId(), model.remainingUses(), model.qrToken());
    }
}