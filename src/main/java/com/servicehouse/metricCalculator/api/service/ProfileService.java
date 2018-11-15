package com.servicehouse.metricCalculator.api.service;

import com.servicehouse.metricCalculator.api.exception.FractionValidationException;
import com.servicehouse.metricCalculator.api.exception.MeterNotFoundException;
import com.servicehouse.metricCalculator.api.exception.NullParameterException;
import com.servicehouse.metricCalculator.api.model.Month;
import com.servicehouse.metricCalculator.api.model.ProfileFraction;
import com.servicehouse.metricCalculator.api.model.ProfileMeter;

import java.util.List;

public interface ProfileService {

    /**
     * add the fraction to the profile
     * @param profileFractionList which will evaluate and add to fraction list
     * @throws FractionValidationException if the fraction is not valid
     * @throws NullParameterException if the {@param:profileFractionList} is empty
     */
    void addProfileFractions(List<ProfileFraction> profileFractionList) throws FractionValidationException, NullParameterException;

    /**
     * add the meters to specified profile of the profile meter
     * @param profileMeterList of the profile which will add
     * @throws NullParameterException if the parameter is null
     */
    void addProfileMeters(List<ProfileMeter> profileMeterList) throws NullParameterException;

    /**
     * @param meterId of the wanted consumption
     * @param month of the wanted consumption
     * @return the consumption of the given month of the given meter id
     * @throws NullParameterException if the parameters are null
     * @throws MeterNotFoundException if the meter not found
     */
    Double getProfile(String meterId, Month month) throws NullParameterException, MeterNotFoundException;
}
