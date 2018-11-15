package com.servicehouse.metricCalculator.api.service;


import com.servicehouse.metricCalculator.api.exception.FractionValidationException;
import com.servicehouse.metricCalculator.api.exception.NullParameterException;


import java.io.IOException;

public interface CSVParserService {

    boolean importFraction(String filePath) throws IOException, NullParameterException, FractionValidationException;
    boolean importMeter(String filePath) throws IOException, NullParameterException;
}
