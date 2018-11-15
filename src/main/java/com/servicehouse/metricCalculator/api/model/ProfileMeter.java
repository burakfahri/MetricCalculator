package com.servicehouse.metricCalculator.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;

@Data
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public final class ProfileMeter extends Profile {
    private String meterId;
    private Double meterReading;

    public ProfileMeter(String meterId, String profile, Month month, Double meterReading) {
        super(profile, month);
        this.meterId = meterId;
        this.meterReading = meterReading;
    }

    public Profile getProfile(){
        return new Profile(name,month);
    }
}
