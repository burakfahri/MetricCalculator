package com.servicehouse.metricCalculator.api.model;

import org.junit.Test;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class ProfileMeterTest {
    @Test
    public void pojoTest(){
            assertPojoMethodsFor(ProfileMeter.class).areWellImplemented();
    }
}
