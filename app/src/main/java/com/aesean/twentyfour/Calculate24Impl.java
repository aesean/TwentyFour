package com.aesean.twentyfour;

import java.util.HashSet;

/**
 * Calculate24Impl
 *
 * @author xl
 * @version V1.0
 * @since 09/01/2017
 */
@SuppressWarnings("WeakerAccess")
public class Calculate24Impl implements ICalculate24 {
    @Override
    public HashSet<String> calculateResult(int a, int b, int c, int d) {
        return TwentyFour.calculateResult(a, b, c, d);
    }
}
