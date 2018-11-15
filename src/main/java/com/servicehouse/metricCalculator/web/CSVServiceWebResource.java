package com.servicehouse.metricCalculator.web;


import com.servicehouse.metricCalculator.api.exception.FractionValidationException;
import com.servicehouse.metricCalculator.api.exception.MeterNotFoundException;
import com.servicehouse.metricCalculator.api.exception.NullParameterException;
import com.servicehouse.metricCalculator.api.service.CSVParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/profile")
@Controller
public class CSVServiceWebResource {
    private CSVParserService csvParserService;

    @Autowired
    public CSVServiceWebResource(CSVParserService csvParserService){
        this.csvParserService = csvParserService;
    }

    @PostMapping("/fraction/{path}")
    public ResponseEntity addFractions(@PathVariable String path)
    {
        try {
            csvParserService.importFraction(path);
        }catch (Exception e) {
            return exceptionHandler(e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/meter/{path}")
    public ResponseEntity addMeters(@PathVariable String path)
    {
        try {
            csvParserService.importMeter(path);
        } catch (Exception e) {
            return exceptionHandler(e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private ResponseEntity exceptionHandler(Exception e) {
        if (e instanceof NullParameterException || e instanceof FractionValidationException || e instanceof IOException)
            return ResponseEntity.badRequest().body(e.getMessage());
        else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
