package de.essig.adventofcode.aoc2019;

import de.essig.adventofcode.aoc2019.monitoringstation.Asteroid;
import de.essig.adventofcode.aoc2019.monitoringstation.MapParser;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import static java.util.Comparator.comparing;


/**
 * @author  Tim Essig - essig@synyx.de
 */
public class Day10Part1 {

    @Test
    void parserShouldReturnCorrectAmountOfAsteroids() {

        Set<Asteroid> asteroids = MapParser.parseMap(Day10Data.TEST_DATA_ONE);
        assertThat(asteroids, hasSize(10));
    }


    @Test
    void testOne() {

        Set<Asteroid> asteroids = MapParser.parseMap(Day10Data.TEST_DATA_ONE);
        Asteroid bestFit = findBestFit(asteroids).get();
        assertThat(bestFit, is(new Asteroid(3, 4)));
        assertThat(bestFit.getVisibleAstreroids(), is(8));
    }


    @Test
    void testTwo() {

        Set<Asteroid> asteroids = MapParser.parseMap(Day10Data.TEST_DATA_TWO);
        Asteroid bestFit = findBestFit(asteroids).get();
        assertThat(bestFit, is(new Asteroid(5, 8)));
        assertThat(bestFit.getVisibleAstreroids(), is(33));
    }


    @Test
    void testThree() {

        Set<Asteroid> asteroids = MapParser.parseMap(Day10Data.TEST_DATA_THREE);
        Asteroid bestFit = findBestFit(asteroids).get();
        assertThat(bestFit, is(new Asteroid(1, 2)));
        assertThat(bestFit.getVisibleAstreroids(), is(35));
    }


    @Test
    void testFour() {

        Set<Asteroid> asteroids = MapParser.parseMap(Day10Data.TEST_DATA_FOUR);
        Asteroid bestFit = findBestFit(asteroids).get();
        assertThat(bestFit, is(new Asteroid(6, 3)));
        assertThat(bestFit.getVisibleAstreroids(), is(41));
    }


    @Test
    void testFive() {

        Set<Asteroid> asteroids = MapParser.parseMap(Day10Data.TEST_DATA_FIVE);
        Asteroid bestFit = findBestFit(asteroids).get();
        assertThat(bestFit, is(new Asteroid(11, 13)));
        assertThat(bestFit.getVisibleAstreroids(), is(210));
    }


    @Test
    void testCompetition() {

        Set<Asteroid> asteroids = MapParser.parseMap(Day10Data.COMPETITION_DATA);
        Asteroid bestFit = findBestFit(asteroids).get();
        assertThat(bestFit, is(new Asteroid(3, 4)));
        assertThat(bestFit.getVisibleAstreroids(), is(1));
    }


    private Optional<Asteroid> findBestFit(Set<Asteroid> asteroids) {

        for (Asteroid source : asteroids) {
            for (Asteroid destination : asteroids) {
                if (destination == source) {
                    continue;
                }

                boolean hasAstroidsInLineOfSight = hasAstroidsInLineOfSight(source, destination, asteroids);

                if (!hasAstroidsInLineOfSight) {
                    source.setVisibleAstreroids(source.getVisibleAstreroids() + 1);
                }
            }
        }

        return asteroids.stream().max(comparing(Asteroid::getVisibleAstreroids));
    }


    private boolean hasAstroidsInLineOfSight(Asteroid source, Asteroid destination, Set<Asteroid> asteroids) {

        asteroids = new HashSet<>(asteroids);
        asteroids.remove(source);
        asteroids.remove(destination);

        int deltaX = destination.getX() - source.getX();
        int deltaY = destination.getY() - source.getY();

        boolean viewIsBlocked = false;

        if (deltaX == 0) { // vertical line

            for (int y : integersInBetween(source.getY(), destination.getY())) {
                int x = source.getX();

                if (asteroids.contains(new Asteroid(x, y))) {
                    return true;
                }
            }
        } else if (deltaY == 0) { // horizontal line

            for (int x : integersInBetween(source.getX(), destination.getX())) {
                int y = source.getY();

                if (asteroids.contains(new Asteroid(x, y))) {
                    return true;
                }
            }
        } else {
            double m = (double) deltaY / (double) deltaX;

            for (int x : integersInBetween(source.getX(), destination.getX())) {
                double xDiff = (double) (x - source.getX());
                double y = source.getY() + (xDiff * m);

                if (y == Math.floor(y)) {
                    if (asteroids.contains(new Asteroid(x, (int) y))) {
                        return true;
                    }
                }
            }
        }

        return viewIsBlocked;
    }


    private List<Integer> integersInBetween(int startInclusive, int endInclusive) {

        if (startInclusive == endInclusive) {
            return Collections.emptyList();
        } else if (startInclusive < endInclusive) {
            return IntStream.rangeClosed(startInclusive, endInclusive).boxed()
                .collect(Collectors.toList());
        } else { // startInclusive > endInclusive

            List<Integer> ints = new ArrayList<>();

            for (int step = endInclusive; step <= startInclusive; step++) {
                ints.add(step);
            }

            return ints;
        }
    }
}
