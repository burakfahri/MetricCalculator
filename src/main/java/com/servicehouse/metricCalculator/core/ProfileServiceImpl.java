package com.servicehouse.metricCalculator.core;

import com.servicehouse.metricCalculator.api.exception.FractionValidationException;
import com.servicehouse.metricCalculator.api.exception.MeterNotFoundException;
import com.servicehouse.metricCalculator.api.exception.NullParameterException;
import com.servicehouse.metricCalculator.api.model.Month;
import com.servicehouse.metricCalculator.api.model.Profile;
import com.servicehouse.metricCalculator.api.model.ProfileFraction;
import com.servicehouse.metricCalculator.api.model.ProfileMeter;
import com.servicehouse.metricCalculator.api.repository.ProfileFractionRepository;
import com.servicehouse.metricCalculator.api.repository.ProfileMeterRepository;
import com.servicehouse.metricCalculator.api.service.ProfileService;
import com.servicehouse.metricCalculator.api.util.CommonUtil;
import com.servicehouse.metricCalculator.api.util.DataIntegrityValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl implements ProfileService {

    protected Map<Profile, ProfileFraction> profileFractionMap;
    protected Map<String, List<ProfileMeter>> profileMeterMap;
    protected ProfileMeterRepository profileMeterRepository;
    protected ProfileFractionRepository profileFractionRepository;

    @Autowired
    public ProfileServiceImpl(ProfileMeterRepository profileMeterRepository, ProfileFractionRepository profileFractionRepository) {
        profileFractionMap = new ConcurrentHashMap<>();
        profileMeterMap = new ConcurrentHashMap<>();
        this.profileMeterRepository = profileMeterRepository;
        this.profileFractionRepository = profileFractionRepository;


        profileMeterRepository.findAll().forEach(profileMeter -> profileMeterMap.computeIfAbsent(profileMeter.getMeterId(),
                (s -> new ArrayList<>())).add(profileMeter));

        profileFractionRepository.findAll().forEach(profileFraction -> profileFractionMap.put(profileFraction.getProfile()
                , profileFraction));
    }


    public boolean checkProfileFraction(Map<String, List<ProfileFraction>> profileGroupedFractionMap) throws FractionValidationException {
        for (Map.Entry<String, List<ProfileFraction>> stringListEntry : profileGroupedFractionMap.entrySet()) {
            if (stringListEntry.getValue().stream().mapToDouble(ProfileFraction::getFractionPercentage).sum() != 1D)
                return false;
        }
        return true;
    }

    @Override
    public synchronized void addProfileFractions(List<ProfileFraction> profileFractionList) throws FractionValidationException, NullParameterException {
        if (CommonUtil.checkParameterIsNull(profileFractionList))
            throw new NullParameterException("Profile fraction list is null or empty");
        profileFractionRepository.deleteAll();

        Map<String, List<ProfileFraction>> profileGroupedFractionMap = profileFractionList.stream()
                .collect(Collectors.groupingBy(ProfileFraction::getName));

        if(!checkProfileFraction(profileGroupedFractionMap))
            throw new FractionValidationException("Sum of the fraction is not equals to 1.00");


        profileFractionList.forEach(profileFraction -> {
            profileFractionMap.put(new Profile(profileFraction.getName(), profileFraction.getMonth()), profileFraction);
            profileFractionRepository.save(profileFraction);
        });
    }

    @Override
    public synchronized void addProfileMeters(List<ProfileMeter> profileMeterList) throws NullParameterException {
        if (CommonUtil.checkParameterIsNull(profileMeterList) || profileMeterList.isEmpty())
            throw new NullParameterException("Profile meter list is null or empty");

        Map<String, List<ProfileMeter>> profileGroupedMeterMap = profileMeterList.stream()
                .collect(Collectors.groupingBy(ProfileMeter::getMeterId));

        profileMeterRepository.deleteAll();
        profileMeterList.clear();

        profileGroupedMeterMap.values().forEach(profileMeterList1 -> {
            if (DataIntegrityValidatorUtil.validateProfileFractionIntegrity(profileMeterList1)) {

                Double sum = profileMeterList1.get(profileMeterList1.size() - 1).getMeterReading();

                DataIntegrityValidatorUtil.convertConsumptionList(profileMeterList1);

                if (validateListMetersDataConsistency(profileMeterList, sum))

                    profileMeterList.forEach(profileMeter -> {
                        profileMeterMap.computeIfAbsent(profileMeter.getMeterId(),
                                (s -> new ArrayList<>())).add(profileMeter);
                        profileMeterRepository.save(profileMeter);
                    });
            }
        });
    }

    private boolean validateListMetersDataConsistency(List<ProfileMeter> profileMeterList, Double sum) {
        return profileMeterList.stream().allMatch(profileMeter ->
                profileFractionMap.get(profileMeter.getProfile()) != null &&
                        DataIntegrityValidatorUtil.validateProfileMeterDataRangeAccordingToItsFraction(
                                profileMeter.getMeterReading(),
                                profileFractionMap.get(profileMeter.getProfile()).getFractionPercentage(), sum));
    }

    @Override
    public Double getProfile(String meterId, Month month) throws NullParameterException, MeterNotFoundException {
        if (CommonUtil.checkParameterIsNull(meterId, month))
            throw new NullParameterException("Meter id " + meterId + " or month " + month + " is null");
        if (profileMeterMap.get(meterId) == null)
            throw new MeterNotFoundException("Meter has not found with meter id" + meterId);
        return profileMeterMap.get(meterId).stream().filter(profileMeter -> profileMeter.getMonth().equals(month))
                .mapToDouble(ProfileMeter::getMeterReading).sum();
    }


}
