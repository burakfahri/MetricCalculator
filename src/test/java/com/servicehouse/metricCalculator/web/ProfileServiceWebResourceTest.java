package com.servicehouse.metricCalculator.web;

import com.servicehouse.metricCalculator.api.exception.FractionValidationException;
import com.servicehouse.metricCalculator.api.exception.MeterNotFoundException;
import com.servicehouse.metricCalculator.api.exception.NullParameterException;
import com.servicehouse.metricCalculator.api.model.*;
import com.servicehouse.metricCalculator.api.service.CSVParserService;
import com.servicehouse.metricCalculator.api.service.ProfileService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ProfileServiceWebResourceTest {
    @Mock
    ProfileService profileService;

    @InjectMocks
    ProfileServiceWebResource profileServiceWebResource;

    private ProfileFractionListDTO profileFractionListDTO = new ProfileFractionListDTO();
    private ProfileMeterListDTO profileMeterListDTO = new ProfileMeterListDTO();
    private List<ProfileFraction> profileFractionList = new ArrayList<>();
    private List<ProfileMeter> profileMeterList = new ArrayList<>();
    private static int NOT_FOUND=404;
    @Before
    public void setup(){
        profileFractionListDTO.setProfileFractionList(profileFractionList);
        profileMeterListDTO.setProfileMeterList(profileMeterList);
    }

    @Test
    public void addFractionsWithSuccessTest() {
        ResponseEntity responseEntity = profileServiceWebResource.addFractions(profileFractionListDTO);
        Assert.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void addFractionsWithBadRequestTest() throws NullParameterException, FractionValidationException {
        Mockito.doThrow(FractionValidationException.class).when(profileService).addProfileFractions(profileFractionList);
        ResponseEntity responseEntity = profileServiceWebResource.addFractions(profileFractionListDTO);
        Assert.assertTrue(responseEntity.getStatusCode().is4xxClientError());
    }

    @Test
    public void addMeters() {
        ResponseEntity responseEntity = profileServiceWebResource.addMeters(profileMeterListDTO);
        Assert.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void addMetersWithBadRequestTest() throws NullParameterException {
        Mockito.doThrow(NullParameterException.class).when(profileService).addProfileMeters(profileMeterList);
        ResponseEntity responseEntity = profileServiceWebResource.addMeters(profileMeterListDTO);
        Assert.assertTrue(responseEntity.getStatusCode().is4xxClientError());
    }

    @Test
    public void getMeter() throws NullParameterException, MeterNotFoundException {
        ProfileMeter profileMeter = new ProfileMeter("0001","A", Month.APR,10D);
        Mockito.when(profileService.getProfile(ArgumentMatchers.anyString(),ArgumentMatchers.anyObject())).thenReturn(profileMeter.getMeterReading());
        ResponseEntity responseEntity = profileServiceWebResource.getMeter(profileMeter.getMeterId(),profileMeter.getMonth());
        Assert.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assert.assertEquals(responseEntity.getBody(), profileMeter.getMeterReading());
    }

    @Test
    public void getMeterWithBadRequestTest() throws NullParameterException, MeterNotFoundException {

        Mockito.doThrow(NullParameterException.class).when(profileService).getProfile(ArgumentMatchers.anyString(),ArgumentMatchers.anyObject());
        ResponseEntity responseEntity = profileServiceWebResource.getMeter("0001",Month.APR);
        Assert.assertTrue(responseEntity.getStatusCode().is4xxClientError());
    }

    @Test
    public void getMeterWithNotFoundTest() throws NullParameterException, MeterNotFoundException {

        Mockito.doThrow(MeterNotFoundException.class).when(profileService).getProfile(ArgumentMatchers.anyString(),ArgumentMatchers.anyObject());
        ResponseEntity responseEntity = profileServiceWebResource.getMeter("0001",Month.APR);
        Assert.assertEquals(responseEntity.getStatusCodeValue(),NOT_FOUND);
    }

    @Test
    public void getMeterWithNotFoundTestWith0Consumption() throws NullParameterException, MeterNotFoundException {

        Mockito.when(profileService.getProfile(ArgumentMatchers.anyString(),ArgumentMatchers.anyObject())).thenReturn(0D);
        ResponseEntity responseEntity = profileServiceWebResource.getMeter("0001",Month.APR);
        Assert.assertEquals(responseEntity.getStatusCodeValue(), NOT_FOUND);
    }

    @Test
    public void getMeterWithInternalServerError() throws NullParameterException, MeterNotFoundException {

        Mockito.doThrow(NullPointerException.class).when(profileService).getProfile(ArgumentMatchers.anyString(),ArgumentMatchers.anyObject());
        ResponseEntity responseEntity = profileServiceWebResource.getMeter("0001",Month.APR);
        Assert.assertTrue(responseEntity.getStatusCode().is5xxServerError());
    }


}
