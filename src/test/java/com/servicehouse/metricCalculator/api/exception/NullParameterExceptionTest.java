package com.servicehouse.metricCalculator.api.exception;

import org.junit.Assert;
import org.junit.Test;
import pl.pojo.tester.api.assertion.Assertions;
import pl.pojo.tester.api.assertion.Method;

public class NullParameterExceptionTest {
    @Test(expected = NullParameterException.class)
    public void testConstructor() throws NullParameterException {
        NullParameterException exception = new NullParameterException("Dummy");
        Assert.assertNotNull("NullParameterException can not be null", exception);
        throw exception;
    }

    @Test
    public void testConstructorAuto() {
        Assertions.assertPojoMethodsFor(NullParameterException.class).testing(Method.CONSTRUCTOR).areWellImplemented();
    }
}
