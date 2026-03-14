package com.project.domain.gym;

import com.project.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.List;
import java.util.UUID;

@Entity
public class Gym {
    @Id
    private final String id;
    private final String name;
    private final String address;
    private final String contact;
    private final List<BusinessHours> businessHours;
    private final List<Pass> passes;
    private int maxCapacity;
    @ManyToOne
    private final User owner;

    public Gym(String name, String address, String contact,
              List<BusinessHours> businessHours, List<Pass> passes, User owner, int maxCapacity) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.businessHours = businessHours;
        this.passes = passes;
        this.owner = owner;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getContact() { return contact; }
    public List<BusinessHours> getBusinessHours() { return businessHours; }
    public List<Pass> getPasses() { return passes; }
    public User getOwner() { return owner; }
    public int getMaxCapacity() { return maxCapacity; }

    public static Crowdedness getCrowdedness(int maxCapacity, int bookingCount) {
        int chunk = maxCapacity / 6;

        if (bookingCount > chunk * 4) {
            return Crowdedness.BUSY;
        } else if (bookingCount > chunk * 2) {
            return Crowdedness.MODERATE;
        } else {
            return Crowdedness.QUIET;
        }
    }
}