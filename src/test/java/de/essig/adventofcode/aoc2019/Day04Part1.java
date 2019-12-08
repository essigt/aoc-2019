package de.essig.adventofcode.aoc2019;

import org.hamcrest.Matchers;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;


public class Day04Part1 {

    private static final int lowerLimit = 158126;
    private static final int upperLimit = 624574;

    @Test
    void calculatePosabiliteies() {

        int numMatchingCirteria = 0;

        for (int i = lowerLimit; i <= upperLimit; i++) {
            if (isValid(i)) {
                numMatchingCirteria++;
            }
        }

        System.out.println("Valid: " + numMatchingCirteria);
    }


    @Test
    void runTests() {

        assertThat(isValid(111111), Matchers.is(true));
        assertThat(isValid(223450), Matchers.is(false));
        assertThat(isValid(123789), Matchers.is(false));
    }


    private boolean isValid(int number) {

        String numberAsString = String.valueOf(number);

        boolean hasDouble = false;

        for (int pos = 1; pos < numberAsString.length(); pos++) {
            int lastDigit = Integer.valueOf(numberAsString.charAt(pos - 1));
            int currentDigit = Integer.valueOf(numberAsString.charAt(pos));

            if (lastDigit > currentDigit) {
                return false;
            }

            if (lastDigit == currentDigit) {
                hasDouble = true;
            }
        }

        return hasDouble;
    }
}
