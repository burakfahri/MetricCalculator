package com.servicehouse.metricCalculator.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;

@Data
@ToString(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public final class ProfileFraction extends Profile{
    Double fractionPercentage;
    public ProfileFraction(String profile,Month month,Double fractionPercentage){
        super(profile,month);
        this.fractionPercentage = fractionPercentage;
    }

    public Profile getProfile()
    {
        return new Profile(name,month);
    }
}
