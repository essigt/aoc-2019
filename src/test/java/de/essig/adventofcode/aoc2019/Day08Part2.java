package de.essig.adventofcode.aoc2019;

import de.essig.adventofcode.aoc2019.spaceimageformat.SpaceImage;
import de.essig.adventofcode.aoc2019.spaceimageformat.SpaceImageParser;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;

public class Day08Part2 {

    @Test
    void runCompetition() {
        SpaceImage spaceImage = SpaceImageParser.parseSpaceImage(6, 25, Day08Data.SPACE_IMAGE_ONE);

        ArrayList<SpaceImage.Layer> layers = new ArrayList<>(spaceImage.getLayers());
        Collections.reverse(layers);
        SpaceImage.Layer finalImage = layers.remove(0);

        for(SpaceImage.Layer layer : layers) {
            SpaceImage.Row rowOver = layer.getRows().get(0);
            SpaceImage.Row rowUnder = finalImage.getRows().get(0);

            for(int i=0; i < rowUnder.getPixels().size(); i++) {
                int upperPixel = rowOver.getPixels().get(i);
                int lowerPixel = rowUnder.getPixels().get(i);

                if(upperPixel == 2) { //Transparent pixel
                    // Do not change pixel
                } else {
                    rowUnder.setPixel(i, upperPixel);
                }
            }
        }


        System.out.println("Final Image: " + finalImage.getRows().get(0).getPixels().stream().map(String::valueOf).collect(Collectors.joining("")));
        //Answer: HCGFE

    }
}
