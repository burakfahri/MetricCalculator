package com.servicehouse.metricCalculator.core;

import com.servicehouse.metricCalculator.api.exception.FractionValidationException;
import com.servicehouse.metricCalculator.api.exception.NullParameterException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class CSVParserServiceImplTest {

    @Mock
    ProfileServiceImpl profileService;
    @InjectMocks
    CSVParserServiceImpl csvParser;

    private File fractionFile;
    private File meterFile;
    private final String mockPath = "MOCK_PATH";

    public static final String fractionStr = "Month,Profile,Fraction\n" +
            "JAN,A,0.1\n" +
            "FEB,A,0.2\n" +
            "MAR,A,0.1\n" +
            "APR,A,0.1\n" +
            "MAY,A,0.1\n" +
            "JUNE,A,0.1\n" +
            "JULY,A,0.05\n" +
            "AUG,A,0.05\n" +
            "SEPT,A,0.05\n" +
            "OCT,A,0.05\n" +
            "NOV,A,0.05\n" +
            "DEC,A,0.05";


    public static final String meterStr = "MeterID,Profile,Month,Meter reading\n" +
            "0001,A,JAN,10\n" +
            "0001,A,FEB,30\n" +
            "0001,A,MAR,40\n" +
            "0001,A,APR,50\n" +
            "0001,A,MAY,60\n" +
            "0001,A,JUNE,70\n" +
            "0001,A,JULY,80\n" +
            "0001,A,AUG,90\n" +
            "0001,A,SEPT,95\n" +
            "0001,A,OCT,100\n" +
            "0001,A,NOV,105\n" +
            "0001,A,DEC,110";

    Path resourcePath = null;

    private void writeErrorLogToFile(String fileType) throws IOException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(resourcePath.toAbsolutePath().toString().concat("/")
                .concat(fileType).concat(".csv")))) {
            if(fileType.equals("meter"))
                writer.write(meterStr);
            else
                writer.write(fractionStr);
        }

    }

    @Before
    public void before() throws IOException, URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL fractionFileURL = classLoader.getResource("fraction.csv");
        File resourcesDirectory = new File("src/test/resources");

        resourcePath = Paths.get(resourcesDirectory.getPath());
        if (fractionFileURL != null) {
            fractionFile = new File(fractionFileURL.getFile());
        } else if (resourcePath != null) {
            writeErrorLogToFile("fraction");
        }


        URL meterFileURL = classLoader.getResource("meter.csv");
        if (meterFileURL != null){
            meterFile = new File(meterFileURL.getFile());
        }
        else if(resourcePath != null) {
            writeErrorLogToFile("meter");
        }

    }

    @Test
    public void importFractionTest() throws NullParameterException, FractionValidationException, IOException {
        csvParser.importFraction(fractionFile.getAbsolutePath());
    }

    @Test(expected = NullParameterException.class)
    public void importFractionTestWithNullParameterException() throws NullParameterException, FractionValidationException, IOException {
        csvParser.importFraction(null);
    }

    @Test(expected = NullParameterException.class)
    public void importMeterTestWithNullParameterException() throws NullParameterException, FractionValidationException, IOException {
        csvParser.importMeter(null);
    }

    @Test
    public void importMeterTest() throws NullParameterException, IOException {
        csvParser.importMeter(meterFile.getAbsolutePath());
    }

    @Test(expected = IOException.class)
    public void importFractionWithIOExceptionTest() throws NullParameterException, FractionValidationException, IOException {
        csvParser.importFraction(mockPath);
    }

    @Test(expected = IOException.class)
    public void importMeterWithIOExceptionTest() throws NullParameterException, IOException {
        csvParser.importMeter(mockPath);
    }

    @Test(expected = FractionValidationException.class)
    public void importFractionWithFractionValidationExceptionTest() throws NullParameterException,
            FractionValidationException, IOException {
        Mockito.doThrow(new FractionValidationException("Sum of the fraction is not equals to 1.00"))
                .when(profileService).addProfileFractions(ArgumentMatchers.anyList());
        csvParser.importFraction(fractionFile.getPath());
    }



}
