package com.servicehouse.metricCalculator.web;

import com.servicehouse.metricCalculator.api.exception.FractionValidationException;
import com.servicehouse.metricCalculator.api.exception.NullParameterException;
import com.servicehouse.metricCalculator.api.service.CSVParserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class CSVServiceWebResourceTest {
    @Mock
    CSVParserService csvParserService;

    @InjectMocks
    CSVServiceWebResource csvServiceWebResource;

    private final String mockPath = "MOCK_PATH";


    @Test
    public void addFractionsWithSuccessTest() {
        ResponseEntity responseEntity = csvServiceWebResource.addFractions(mockPath);
        Assert.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void addFractionsWithBadRequestTest() throws NullParameterException, FractionValidationException, IOException {
        Mockito.doThrow(FractionValidationException.class).when(csvParserService).importFraction(ArgumentMatchers.anyString());
        ResponseEntity responseEntity = csvServiceWebResource.addFractions(mockPath);
        Assert.assertTrue(responseEntity.getStatusCode().is4xxClientError());
    }

    @Test
    public void addMeters() {
        ResponseEntity responseEntity = csvServiceWebResource.addMeters(mockPath);
        Assert.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void addMetersWithBadRequestTest() throws NullParameterException,IOException {
        Mockito.doThrow(NullParameterException.class).when(csvParserService).importMeter(ArgumentMatchers.anyString());
        ResponseEntity responseEntity = csvServiceWebResource.addMeters(mockPath);
        Assert.assertTrue(responseEntity.getStatusCode().is4xxClientError());
    }
}
