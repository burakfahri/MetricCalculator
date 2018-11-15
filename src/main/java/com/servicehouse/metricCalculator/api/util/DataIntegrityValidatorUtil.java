package com.servicehouse.metricCalculator.api.util;

import com.servicehouse.metricCalculator.api.model.Profile;
import com.servicehouse.metricCalculator.api.model.ProfileMeter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class DataIntegrityValidatorUtil {

    public static boolean validateProfileFractionIntegrity(List<ProfileMeter> profileMeterList) {
        Collections.sort(profileMeterList, Comparator.comparing(Profile::getMonth));
        Double smallMeterReading = -1D;
        for (ProfileMeter profileMeter : profileMeterList) {
            if (profileMeter.getMeterReading().compareTo(smallMeterReading) < 0)
                return false;
            else smallMeterReading = profileMeter.getMeterReading();
        }
        return true;
    }

    public static boolean validateProfileMeterDataRangeAccordingToItsFraction(Double meterReading, Double fraction,Double sum){
                log.info(" is true {}", meterReading *5/4 >= fraction* sum && meterReading *3/4 < fraction* sum );
                return meterReading *5/4 >= fraction* sum && meterReading *3/4 < fraction* sum;

    }

    public static void convertConsumptionList(List<ProfileMeter> profileMeterList) {
        Double lastConsumption = 0D;

        for (ProfileMeter profileMeter : profileMeterList) {
            profileMeter.setMeterReading(profileMeter.getMeterReading() - lastConsumption);
            lastConsumption += profileMeter.getMeterReading();
        }
    }
}
