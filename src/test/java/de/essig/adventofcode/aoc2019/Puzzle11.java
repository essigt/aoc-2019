package de.essig.adventofcode.aoc2019;

import de.essig.adventofcode.aoc2019.orbit.Orbit;
import de.essig.adventofcode.aoc2019.orbit.OrbitCheckSum;
import de.essig.adventofcode.aoc2019.orbit.OrbitGraphParser;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;


/**
 * @author  Tim Essig - essig@synyx.de
 */
public class Puzzle11 {

    private OrbitGraphParser orbitGraphParser = new OrbitGraphParser();

    @Test
    void test() {

        Orbit COM = orbitGraphParser.parseGraph(Puzzle11Data.TEST_INPUT);

        COM.prettyPrint();

        assertThat(OrbitCheckSum.calculateChecksum(COM).getSum(), is(42));
    }


    @Test
    void runCompetition() {

        Orbit COM = orbitGraphParser.parseGraph(Puzzle11Data.INPUT);

        assertThat(OrbitCheckSum.calculateChecksum(COM).getSum(), is(150150));
    }
}
