package com.servicehouse.metricCalculator.api.exception;

import org.junit.Assert;
import org.junit.Test;
import pl.pojo.tester.api.assertion.Assertions;
import pl.pojo.tester.api.assertion.Method;

public class FractionValidateExceptionTest {
    @Test(expected = FractionValidationException.class)
    public void testConstructor() throws FractionValidationException {
        FractionValidationException exception = new FractionValidationException("Dummy");
        Assert.assertNotNull("NullParameterException can not be null", exception);
        throw exception;
    }

    @Test
    public void testConstructorAuto() {
        Assertions.assertPojoMethodsFor(FractionValidationException.class).testing(Method.CONSTRUCTOR).areWellImplemented();
    }
}
