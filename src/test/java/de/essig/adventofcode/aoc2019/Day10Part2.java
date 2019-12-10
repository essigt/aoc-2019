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


/**
 * @author  Tim Essig - essig@synyx.de
 */
public class Day10Part2 {

    private double lastAngel = 90 - 0.000001;

    @Test
    void parserShouldReturnCorrectAmountOfAsteroids() {

        Set<Asteroid> asteroids = MapParser.parseMap(Day10Data.TEST_DATA_ONE);
        assertThat(asteroids, hasSize(10));
    }


    @Test
    void runComp() {


        Asteroid asteroidStation = new Asteroid(11, 11);
        Set<Asteroid> asteroids = MapParser.parseMap(Day10Data.COMPETITION_DATA);

        int counter = 1;
        Asteroid vaporizedAsteroid = null;

        do {
            vaporizedAsteroid = findNextSmallestAngelVisibleAsteroid(asteroidStation, asteroids).orElse(null);

            if (vaporizedAsteroid == null) {
                vaporizedAsteroid = findNextSmallestAngelVisibleAsteroid(asteroidStation, asteroids).orElse(null);
            }

            if (counter == 200)
                System.out.println(counter + " Vapor: " + vaporizedAsteroid);

            counter++;
        } while (vaporizedAsteroid != null);

        // Answer=806
    }


    @Test
    void testSample() {

        Asteroid asteroidStation = new Asteroid(11, 13);
        Set<Asteroid> asteroids = MapParser.parseMap(Day10Data.TEST_DATA_FIVE);

        List<Asteroid> vaporizedAsteroids = new ArrayList<>();
        Asteroid vaporizedAsteroid = null;

        int counter = 1;

        do {
            double preLastAngel = lastAngel;
            vaporizedAsteroid = findNextSmallestAngelVisibleAsteroid(asteroidStation, asteroids).orElse(null);

            if (vaporizedAsteroid == null) {
                printMap(asteroids, 20, 20, 11, 13);
                vaporizedAsteroid = findNextSmallestAngelVisibleAsteroid(asteroidStation, asteroids).orElse(null);
            }

            if (vaporizedAsteroid != null) {
                System.out.println(counter + " Vapor: " + vaporizedAsteroid + " \tat " + lastAngel);
                vaporizedAsteroids.add(vaporizedAsteroid);
            }

            counter++;
        } while (vaporizedAsteroid != null);

        // Test first vaporized asteroids
        assertThat(vaporizedAsteroids.get(0), is(new Asteroid(11, 12)));

        assertThat(vaporizedAsteroids.get(1), is(new Asteroid(12, 1)));

        assertThat(vaporizedAsteroids.get(2), is(new Asteroid(12, 2)));

        assertThat(vaporizedAsteroids.get(19), is(new Asteroid(16, 0)));

        assertThat(vaporizedAsteroids.get(49), is(new Asteroid(16, 9)));

        assertThat(vaporizedAsteroids.get(99), is(new Asteroid(10, 16)));

        assertThat(vaporizedAsteroids.get(198), is(new Asteroid(9, 6)));

        assertThat(vaporizedAsteroids.get(199), is(new Asteroid(8, 2)));

        assertThat(vaporizedAsteroids.get(200), is(new Asteroid(10, 9)));

        assertThat(vaporizedAsteroids.get(298), is(new Asteroid(11, 1)));
    }

    private void printMap(Set<Asteroid> asteroids, int witdh, int height, int stationX, int stationY) {
        System.out.println("------ MAP ------");
        for(int y = 0; y  < height; y++) {
            for(int x = 0; x  < witdh; x++) {
                if(x == stationX && y == stationY)
                    System.out.print("O");
                else if(asteroids.contains(new Asteroid(x,y)))
                    System.out.print("X");
                else
                    System.out.print(".");
            }
            System.out.println("");
        }


    }


    private Optional<Asteroid> findNextSmallestAngelVisibleAsteroid(Asteroid source, Set<Asteroid> asteroids) {

        Set<Asteroid> reachableAsteroids = new HashSet<>();

        // Get all reachable asteroids
        for (Asteroid destination : asteroids) {
            if (destination == source) {
                continue;
            }

            boolean isReachableByLaser = !hasAstroidsInLineOfSight(source, destination, asteroids);

            if (isReachableByLaser) {
                reachableAsteroids.add(destination);
            }
        }

        Asteroid smalestAngelAsteroid = null;
        double smalestAngel = Double.MAX_VALUE;

        for (Asteroid destination : reachableAsteroids) {
            int deltaX = destination.getX() - source.getX();
            int deltaY = destination.getY() - source.getY();
            double angel = (Math.atan2(deltaY, deltaX)) * 180.0 / Math.PI + 180;

            if (angel < smalestAngel && angel > lastAngel) {
                smalestAngel = angel;
                smalestAngelAsteroid = destination;
            }
        }

        if (smalestAngelAsteroid == null && !asteroids.isEmpty()) {
            System.out.println("Next Round, rest lastAngel from" + lastAngel);

            if (lastAngel == 360.0)
                lastAngel = 0;
            else
                lastAngel = 0 - 0.001;
        } else {
            lastAngel = smalestAngel;
        }

        // System.out.println("");
        // Remove from set
        asteroids.remove(smalestAngelAsteroid);

        return Optional.ofNullable(smalestAngelAsteroid);
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
