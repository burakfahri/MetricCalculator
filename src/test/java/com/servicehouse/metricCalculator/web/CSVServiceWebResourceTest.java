package com.servicehouse.metricCalculator.web;

import com.servicehouse.metricCalculator.api.exception.FractionValidationException;
import com.servicehouse.metricCalculator.api.exception.NullParameterException;
import com.servicehouse.metricCalculator.api.service.CSVParserService;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(MockitoJUnitRunner.class)
public class CSVServiceWebResourceTest {
    @Mock
    CSVParserService csvParserService;

    @InjectMocks
    CSVServiceWebResource csvServiceWebResource;

    private final String mockPath = "MOCK_PATH";



    @Test
    public void addFractionsWithSuccessTest() throws NullParameterException, FractionValidationException, IOException {
        Mockito.when(csvParserService.importFraction(Mockito.anyString())).thenReturn(true);
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
    public void addMeters() throws IOException, NullParameterException {
        Mockito.when(csvParserService.importMeter(Mockito.anyString())).thenReturn(true);
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
