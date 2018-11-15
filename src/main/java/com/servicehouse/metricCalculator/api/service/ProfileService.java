package com.servicehouse.metricCalculator.api.service;

import com.servicehouse.metricCalculator.api.exception.FractionValidationException;
import com.servicehouse.metricCalculator.api.exception.MeterNotFoundException;
import com.servicehouse.metricCalculator.api.exception.NullParameterException;
import com.servicehouse.metricCalculator.api.model.Month;
import com.servicehouse.metricCalculator.api.model.ProfileFraction;
import com.servicehouse.metricCalculator.api.model.ProfileMeter;

import java.util.List;

public interface ProfileService {
    void addProfileFractions(List<ProfileFraction> profileFractionList) throws FractionValidationException, NullParameterException;
    void addProfileMeters(List<ProfileMeter> profileMeterList) throws NullParameterException;
    Double getProfile(String meterId, Month month) throws NullParameterException, MeterNotFoundException;
}
