package com.servicehouse.metricCalculator.api.model;

import org.junit.Test;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

public class BaseEntityTest {
    @Test
    public void pojoTest(){
            assertPojoMethodsFor(BaseEntity.class).areWellImplemented();
    }
}
