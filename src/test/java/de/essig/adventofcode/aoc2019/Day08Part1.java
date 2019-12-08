package de.essig.adventofcode.aoc2019;

import de.essig.adventofcode.aoc2019.spaceimageformat.SpaceImage;
import de.essig.adventofcode.aoc2019.spaceimageformat.SpaceImageParser;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class Day08Part1 {

    @Test
    void runCompetition() {
        SpaceImage spaceImage = SpaceImageParser.parseSpaceImage(6, 25, Day08Data.SPACE_IMAGE_ONE);

        int minZeros = Integer.MAX_VALUE;
        SpaceImage.Layer minLayer = null;

        for(SpaceImage.Layer layer : spaceImage.getLayers()) {

            int numberOfZeros = layer.getNumberOfValues(0);
            if(numberOfZeros < minZeros) {
                minZeros = numberOfZeros;
                minLayer = layer;
            }
        }

        int sum = minLayer.getNumberOfValues(1) * minLayer.getNumberOfValues(2);

        assertThat(sum, Matchers.is(1703));
    }
}
