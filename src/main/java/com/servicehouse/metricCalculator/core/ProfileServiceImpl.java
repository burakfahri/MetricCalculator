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

    /**
     * load all the parameters from the db and starts the Profile Service
     * @param profileMeterRepository which provides meter connection btw db and service
     * @param profileFractionRepository which fraction meter connection btw db and service
     */
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

    /**
     * chects the profile fractions by its profile and calculate every sum of the profile is equal to 1D
     * @param profileGroupedFractionMap which has groupped profile fraction
     * @return the true if it is valid or not
     *
     */
    public boolean checkProfileFraction(Map<String, List<ProfileFraction>> profileGroupedFractionMap) {
        for (Map.Entry<String, List<ProfileFraction>> stringListEntry : profileGroupedFractionMap.entrySet()) {
            if (stringListEntry.getValue().stream().mapToDouble(ProfileFraction::getFractionPercentage).sum() != 1D)
                return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void addProfileFractions(List<ProfileFraction> profileFractionList) throws FractionValidationException, NullParameterException {
        if (CommonUtil.checkParameterIsNull(profileFractionList))
            throw new NullParameterException("Profile fraction list is null or empty");

        //delete all the fractions from db
        profileFractionRepository.deleteAll();
        //clear the cache
        profileFractionMap.clear();

        //group it as it's name
        Map<String, List<ProfileFraction>> profileGroupedFractionMap = profileFractionList.stream()
                .collect(Collectors.groupingBy(ProfileFraction::getName));

        //validate it
        if(!checkProfileFraction(profileGroupedFractionMap))
            throw new FractionValidationException("Sum of the fraction is not equals to 1.00");

        //put all the fraction to the db and cache
        profileFractionList.forEach(profileFraction -> {
            profileFractionMap.put(new Profile(profileFraction.getName(), profileFraction.getMonth()), profileFraction);
            profileFractionRepository.save(profileFraction);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void addProfileMeters(List<ProfileMeter> profileMeterList) throws NullParameterException {
        if (CommonUtil.checkParameterIsNull(profileMeterList) || profileMeterList.isEmpty())
            throw new NullParameterException("Profile meter list is null or empty");

        // group the profile meter according to its profile
        Map<String, List<ProfileMeter>> profileGroupedMeterMap = profileMeterList.stream()
                .collect(Collectors.groupingBy(ProfileMeter::getMeterId));

        //clear cache and db
        profileMeterRepository.deleteAll();
        profileMeterMap.clear();

        //for every profile meter group
        profileGroupedMeterMap.values().forEach(profileMeterList1 -> {
            //validate the integrity like checking if it is incremental
            if (DataIntegrityValidatorUtil.validateProfileFractionIntegrity(profileMeterList1)) {

                //calculating the sum off the consumption
                Double sum = profileMeterList1.get(profileMeterList1.size() - 1).getMeterReading();
                //convert list to consumption list
                DataIntegrityValidatorUtil.convertConsumptionList(profileMeterList);
                //validate consumption according to it's fraction
                if (validateListMetersDataConsistency(profileMeterList, sum))
                    //add lists to cache and db
                    profileMeterList1.forEach(profileMeter -> {
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

    /**
     * {@inheritDoc}
     */
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
