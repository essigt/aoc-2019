package de.essig.adventofcode.aoc2019.monitoringstation;

import java.util.HashSet;
import java.util.Set;


/**
 * @author  Tim Essig - essig@synyx.de
 */
public class MapParser {

    public static Set<Asteroid> parseMap(String map) {

        Set<Asteroid> asteroids = new HashSet<>();

        String[] lines = map.split("\\n");
        int y = 0;

        for (String line : lines) {
            int x = 0;

            for (char ch : line.toCharArray()) {
                if (ch == '#') {
                    asteroids.add(new Asteroid(x, y));
                }

                x++;
            }

            y++;
        }

        return asteroids;
    }
}
