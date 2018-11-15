package com.servicehouse.metricCalculator.core;

import com.servicehouse.metricCalculator.api.exception.FractionValidationException;
import com.servicehouse.metricCalculator.api.exception.NullParameterException;
import com.servicehouse.metricCalculator.api.model.Month;
import com.servicehouse.metricCalculator.api.model.ProfileFraction;
import com.servicehouse.metricCalculator.api.model.ProfileMeter;
import com.servicehouse.metricCalculator.api.service.CSVParserService;
import com.servicehouse.metricCalculator.api.service.ProfileService;
import com.servicehouse.metricCalculator.api.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CSVParserServiceImpl implements CSVParserService {
    private final static String[] FRACTION_HEADERS = {"Month","Profile","ProfileFraction"};
    private final static String[] METER_READING_HEADERS = {"MeterID","Profile","Month","Meter reading "};
    private ProfileService profileService;

    @Autowired
    public CSVParserServiceImpl(ProfileService profileService){
        this.profileService = profileService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public  boolean importFraction( String filePathStr) throws IOException,
            NullParameterException, FractionValidationException {

        if(CommonUtil.checkParameterIsNull(filePathStr))
            throw new NullParameterException("file path is null");

        Path filePath = Paths.get(filePathStr);

        try(Reader reader = Files.newBufferedReader(filePath);
            CSVParser csvParser = new CSVParser(reader , CSVFormat.DEFAULT.withFirstRecordAsHeader().
                    withHeader(FRACTION_HEADERS).withTrim())) {
            profileService.addProfileFractions(parseFractionData(csvParser));
            Files.delete(filePath);
            return true;
        }
        catch (IOException e)
        {
            writeErrorLogToFile(e,filePath);
        }
        return false;
    }

    private List<ProfileFraction>  parseFractionData(CSVParser csvParser)
    {
        List<ProfileFraction> profileFractionList = new ArrayList<>();
        for (CSVRecord record : csvParser) {
            String month = record.get(FRACTION_HEADERS[0]);
            String profile = record.get(FRACTION_HEADERS[1]);
            String fraction = record.get(FRACTION_HEADERS[2]);
            profileFractionList.add(new ProfileFraction(profile, Month.valueOf(month.toUpperCase()),Double.valueOf(fraction)));
        }
        return profileFractionList;
    }

    private List<ProfileMeter>  parseMeterData(CSVParser csvParser)
    {
        List<ProfileMeter> profileMeterList = new ArrayList<>();
        for (CSVRecord record : csvParser) {
            String meterId = record.get(METER_READING_HEADERS[0]);
            String profile = record.get(METER_READING_HEADERS[1]);
            String month = record.get(METER_READING_HEADERS[2]);
            String meterReading = record.get(METER_READING_HEADERS[3]);
            profileMeterList.add(new ProfileMeter(meterId, profile, Month.valueOf(month.toUpperCase()), Double.valueOf(meterReading)));
        }
        return profileMeterList;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean importMeter(String filePathStr) throws IOException, NullParameterException {
        if(CommonUtil.checkParameterIsNull(filePathStr))
            throw new NullParameterException("file path is null");
        Path filePath = Paths.get(filePathStr);

        try(Reader reader = Files.newBufferedReader(filePath);
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().
                    withHeader(METER_READING_HEADERS).withTrim())) {

            List<ProfileMeter> profileMeterList = parseMeterData(csvParser);
            profileService.addProfileMeters(profileMeterList);
            Files.delete(Paths.get(filePathStr));
            return true;
        }
        catch (Exception e)
        {
            writeErrorLogToFile(e,filePath);
        }

        return false;
    }

    private void writeErrorLogToFile(Exception e, Path filePath) throws IOException {
        String fileName = filePath.getFileName().toString();
        Path file = filePath.getParent();
        if(file == null)
            throw new FileNotFoundException("Parent file has not found for "+ filePath);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.toAbsolutePath().toString().concat("/")
                .concat(fileName).concat(".log")))) {
            writer.write(e.getMessage());
            log.info(e.getMessage());
        }

    }
}
