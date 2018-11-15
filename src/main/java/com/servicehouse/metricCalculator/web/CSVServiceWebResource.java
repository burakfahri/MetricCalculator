package com.servicehouse.metricCalculator.web;


import com.servicehouse.metricCalculator.api.exception.FractionValidationException;
import com.servicehouse.metricCalculator.api.exception.MeterNotFoundException;
import com.servicehouse.metricCalculator.api.exception.NullParameterException;
import com.servicehouse.metricCalculator.api.service.CSVParserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/profile")
@Controller
@Slf4j
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
            log.info("add fractions from file has been requested for {}",path);

            if(!csvParserService.importFraction(path))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }catch (Exception e) {
            return exceptionHandler(e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/meter/{path}")
    public ResponseEntity addMeters(@PathVariable String path)
    {
        try {
            log.info("add meters from file has been requested for {}",path);
            if(!csvParserService.importMeter(path))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return exceptionHandler(e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private ResponseEntity exceptionHandler(Exception e) {
        log.info("exception occurs {}",e.getMessage());

        if (e instanceof NullParameterException || e instanceof FractionValidationException || e instanceof IOException)
            return ResponseEntity.badRequest().body(e.getMessage());
        else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
