package com.servicehouse.metricCalculator.api.util;

import java.util.Arrays;
import java.util.Objects;

public class CommonUtil {
    private CommonUtil(){}

    public static boolean checkParameterIsNull(final Object... objects){
        return Arrays.stream(objects).anyMatch(Objects::isNull);
    }
}

