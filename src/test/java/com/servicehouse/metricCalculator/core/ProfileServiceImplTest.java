package com.servicehouse.metricCalculator.core;

import com.servicehouse.metricCalculator.api.exception.FractionValidationException;
import com.servicehouse.metricCalculator.api.exception.MeterNotFoundException;
import com.servicehouse.metricCalculator.api.exception.NullParameterException;
import com.servicehouse.metricCalculator.api.model.Month;
import com.servicehouse.metricCalculator.api.model.ProfileFraction;
import com.servicehouse.metricCalculator.api.model.ProfileMeter;
import com.servicehouse.metricCalculator.api.repository.ProfileFractionRepository;
import com.servicehouse.metricCalculator.api.repository.ProfileMeterRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class ProfileServiceImplTest {

    @Mock
    ProfileFractionRepository profileFractionRepository;
    @Mock
    ProfileMeterRepository profileMeterRepository;
    @InjectMocks
    ProfileServiceImpl profileService;
    private List<ProfileFraction> profileFractionList = new ArrayList<>();
    private List<ProfileMeter> profileMeterList = new ArrayList<>();

    @Before
    public void before(){
        profileFractionList.add(new ProfileFraction("A", Month.JAN,0.1D));
        profileFractionList.add(new ProfileFraction("A", Month.FEB,0.1D));
        profileFractionList.add(new ProfileFraction("A", Month.MAR,0.1D));
        profileFractionList.add(new ProfileFraction("A", Month.APR,0.1D));
        profileFractionList.add(new ProfileFraction("A", Month.MAY,0.1D));
        profileFractionList.add(new ProfileFraction("A", Month.JUNE,0.1D));
        profileFractionList.add(new ProfileFraction("A", Month.JULY,0.1D));
        profileFractionList.add(new ProfileFraction("A", Month.AUG,0.1D));
        profileFractionList.add(new ProfileFraction("A", Month.SEPT,0.05D));
        profileFractionList.add(new ProfileFraction("A", Month.OCT,0.05D));
        profileFractionList.add(new ProfileFraction("A", Month.NOV,0.05D));
        profileFractionList.add(new ProfileFraction("A", Month.DEC,0.05D));

        profileMeterList.add(new ProfileMeter("0001","A", Month.JAN,10D));
        profileMeterList.add(new ProfileMeter("0001","A", Month.FEB,20D));
        profileMeterList.add(new ProfileMeter("0001","A", Month.MAR,30D));
        profileMeterList.add(new ProfileMeter("0001","A", Month.APR,40D));
        profileMeterList.add(new ProfileMeter("0001","A", Month.MAY,50D));
        profileMeterList.add(new ProfileMeter("0001","A", Month.JUNE,60D));
        profileMeterList.add(new ProfileMeter("0001","A", Month.JULY,70D));
        profileMeterList.add(new ProfileMeter("0001","A", Month.AUG,80D));
        profileMeterList.add(new ProfileMeter("0001","A", Month.SEPT,85D));
        profileMeterList.add(new ProfileMeter("0001","A", Month.OCT,90D));
        profileMeterList.add(new ProfileMeter("0001","A", Month.NOV,95D));
        profileMeterList.add(new ProfileMeter("0001","A", Month.DEC,100D));
    }

    @Test
    public void profileFractionAddTest() throws NullParameterException, FractionValidationException {
        profileService.addProfileFractions(profileFractionList);
        Assert.assertEquals(profileService.profileFractionMap.size(),12);
    }

    @Test(expected = FractionValidationException.class)
    public void profileFractionAddWithFractionExceptionTest() throws FractionValidationException, NullParameterException {
        profileFractionList.add(new ProfileFraction("A", Month.DEC,0.5D));
        profileService.addProfileFractions(profileFractionList);
    }

    @Test(expected = NullParameterException.class)
    public void profileFractionAddWithNullParameterExceptionTest() throws FractionValidationException, NullParameterException {
        profileService.addProfileFractions(null);
    }

    @Test
    public void profileMeterAddTest() throws NullParameterException, FractionValidationException {
        profileService.addProfileFractions(profileFractionList);
        profileService.addProfileMeters(profileMeterList);
        Assert.assertEquals(profileService.profileMeterMap.get("0001").size(),12);
    }

    @Test
    public void profileMeterAddWithPercentageToleranceBiggerThan25() throws NullParameterException, FractionValidationException {
        profileMeterList.add(new ProfileMeter("0001","A", Month.DEC,200D));

        profileService.addProfileFractions(profileFractionList);
        profileService.addProfileMeters(profileMeterList);
        Assert.assertNull(profileService.profileMeterMap.get("0001"));
    }


    @Test(expected = MeterNotFoundException.class)
    public void profileMeterAddWithSmallerWrongDataFromLastMonth() throws NullParameterException, FractionValidationException, MeterNotFoundException {
        profileMeterList.add(new ProfileMeter("0001","A", Month.MAR,0D));

        profileService.addProfileFractions(profileFractionList);
        profileService.addProfileMeters(profileMeterList);
        Assert.assertNull(profileService.getProfile("0001",Month.APR));
    }

    @Test(expected = NullParameterException.class)
    public void profileMeterAddWithNullParameterException() throws NullParameterException, FractionValidationException {
        profileService.addProfileFractions(profileFractionList);
        profileService.addProfileMeters(null);
    }


    @Test
    public void profileGetMeterTest() throws NullParameterException, FractionValidationException, MeterNotFoundException {
        profileService.addProfileFractions(profileFractionList);
        profileService.addProfileMeters(profileMeterList);
        Assert.assertEquals(profileService.getProfile("0001",Month.APR),10D,0);
    }

    @Test(expected = NullParameterException.class)
    public void profileGetMeterWithNullParameterExceptionTest() throws NullParameterException, MeterNotFoundException {
        Assert.assertEquals(profileService.getProfile(null,null),10D,0);
    }
}
