package com.servicehouse.metricCalculator.web;


import com.servicehouse.metricCalculator.api.exception.FractionValidationException;
import com.servicehouse.metricCalculator.api.exception.MeterNotFoundException;
import com.servicehouse.metricCalculator.api.exception.NullParameterException;
import com.servicehouse.metricCalculator.api.model.Month;
import com.servicehouse.metricCalculator.api.model.ProfileFractionListDTO;
import com.servicehouse.metricCalculator.api.model.ProfileMeterListDTO;
import com.servicehouse.metricCalculator.api.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/profile")
@Controller
public class ProfileServiceWebResource {
    private ProfileService profileService;

    @Autowired
    public ProfileServiceWebResource(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("/fraction")
    public ResponseEntity addFractions(@RequestBody ProfileFractionListDTO profileFractionList) {
        try {
            profileService.addProfileFractions(profileFractionList.getProfileFractionList());
        } catch (Exception e) {
            return exceptionHandler(e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/meter")
    public ResponseEntity addMeters(@RequestBody ProfileMeterListDTO profileMeterListDTO) {
        try {
            profileService.addProfileMeters(profileMeterListDTO.getProfileMeterList());
        } catch (Exception e) {
            return exceptionHandler(e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/{meterId}/{month}")
    public ResponseEntity getMeter(@PathVariable("meterId") String meterId, @PathVariable("month") Month month) {
        Double sum;
        try {
            sum = profileService.getProfile(meterId, month);
            if (sum.equals(0D))
                return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return exceptionHandler(e);
        }
        return ResponseEntity.ok(sum);
    }

    private ResponseEntity exceptionHandler(Exception e) {
        if (e instanceof NullParameterException || e instanceof FractionValidationException)
            return ResponseEntity.badRequest().body(e.getMessage());
        else if (e instanceof MeterNotFoundException)
            return ResponseEntity.notFound().build();
        else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
