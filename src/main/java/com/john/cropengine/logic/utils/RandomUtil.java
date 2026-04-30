package com.john.cropengine.logic.utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {
    public static int getTickDelay(int min, int max) {
        if (min < 0) min = 0;
        if (max < min) max = min;

        double mean = (min + max) / 2.0;
        double stdDev = (max - min) / 3.0;
        double gaussian = ThreadLocalRandom.current().nextGaussian();

        int value = (int) Math.round(mean + gaussian * stdDev);
        return Math.clamp(value, min, max);
    }
}
