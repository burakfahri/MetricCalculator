package com.servicehouse.metricCalculator.api.service;


import com.servicehouse.metricCalculator.api.exception.FractionValidationException;
import com.servicehouse.metricCalculator.api.exception.NullParameterException;


import java.io.IOException;

public interface CSVParserService {

    /**
     * imports the fractions as old version
     * @param filePath of the file
     * @return the status if it is added or not
     * @throws IOException if file has not been found
     * @throws NullParameterException if the parameter is null
     * @throws FractionValidationException if the Fraction is not valid
     */
    boolean importFraction(String filePath) throws IOException, NullParameterException, FractionValidationException;

    /**
     * imports the meters as old version
     * @param filePath
     * @return the status if it is added or not
     * @throws IOException if file has not been found
     * @throws NullParameterException if the parameter is null
     */
    boolean importMeter(String filePath) throws IOException, NullParameterException;
}
