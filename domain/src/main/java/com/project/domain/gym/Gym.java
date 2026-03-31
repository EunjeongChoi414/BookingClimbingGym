package com.project.domain.gym;

import com.project.domain.exception.InvalidPassException;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class Gym {
    @Id
    private String id;
    private String name;
    private String address;
    private String contact;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "gym_business_hours", joinColumns = @JoinColumn(name = "gym_id"))
    private List<BusinessHours> businessHours;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "gym_id")
    private List<Pass> passes;

    private int cancellationNoticeDays;
    private int maxCapacity;

    private String ownerId;

    protected Gym() {
    }

    public Gym(String name, String address, String contact,
               List<BusinessHours> businessHours, List<Pass> passes, String ownerId,
               int maxCapacity, int cancellationNoticeDays) {

        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.businessHours = businessHours;
        this.passes = passes;
        this.ownerId = ownerId;
        this.maxCapacity = maxCapacity;
        this.cancellationNoticeDays = cancellationNoticeDays;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }

    public List<BusinessHours> getBusinessHours() {
        return businessHours;
    }

    public List<Pass> getPasses() {
        return passes;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getCancellationNoticeDays() {
        return cancellationNoticeDays;
    }

    public Pass getPassById(String passId) {
        return passes.stream()
                .filter(p -> p.getId().equals(passId))
                .findFirst()
                .orElseThrow(InvalidPassException::new);
    }

    public CrowdednessLevel getCrowdedness(int bookingCount) {
        int chunk = maxCapacity / 6;

        if (bookingCount > chunk * 4) {
            return CrowdednessLevel.BUSY;
        } else if (bookingCount > chunk * 2) {
            return CrowdednessLevel.MODERATE;
        } else {
            return CrowdednessLevel.QUIET;
        }
    }
}