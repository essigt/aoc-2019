package de.essig.adventofcode.aoc2019;

import de.essig.adventofcode.aoc2019.orbit.Orbit;
import de.essig.adventofcode.aoc2019.orbit.OrbitGraphParser;
import de.essig.adventofcode.aoc2019.orbit.OrbitTransfer;

import org.hamcrest.Matchers;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * @author  Tim Essig - essig@synyx.de
 */
public class Day06Part2 {

    private OrbitGraphParser orbitGraphParser = new OrbitGraphParser();
    private OrbitTransfer orbitTransfer = new OrbitTransfer();

    @Test
    void test() {

        Orbit COM = orbitGraphParser.parseGraph(Puzzle11Data.TRANSFERE_TEST_INPUT);

        assertThat(orbitTransfer.calculateOrbitTransfers(COM, "YOU", "SAN"), Matchers.is(4));
    }


    @Test
    void runCompetition() {

        Orbit COM = orbitGraphParser.parseGraph(Puzzle11Data.INPUT);

        assertThat(orbitTransfer.calculateOrbitTransfers(COM, "YOU", "SAN"), Matchers.is(352));
    }
}
