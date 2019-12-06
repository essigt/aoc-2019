package de.essig.adventofcode.aoc2019.orbit;

import java.util.ArrayList;
import java.util.List;


/**
 * @author  Tim Essig - essig@synyx.de
 */
public class OrbitTransfer {

    public int calculateOrbitTransfers(Orbit COM, String from, String to) {

        Orbit fromOrbit = findOrbit(from, COM).getParent();
        Orbit toOrbit = findOrbit(to, COM).getParent();

        // Calculate Paths from Orbit to COM
        List<Orbit> fromOrbitPath = getPathToCOM(fromOrbit);
        List<Orbit> toOrbitPath = getPathToCOM(toOrbit);

        // Find first matching orbit
        int distance = 0;

        for (Orbit orbitInFromPath : fromOrbitPath) {
            if (toOrbitPath.contains(orbitInFromPath)) {
                distance = fromOrbitPath.indexOf(orbitInFromPath) + toOrbitPath.indexOf(orbitInFromPath);

                break;
            }
        }

        return distance;
    }


    private Orbit findOrbit(String name, Orbit orbit) {

        if (orbit.getCenter().equals(name)) {
            return orbit;
        } else {
            for (Orbit child : orbit.getChildren()) {
                Orbit result = findOrbit(name, child);

                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }


    List<Orbit> getPathToCOM(Orbit start) {

        List<Orbit> orbits = new ArrayList<>();

        while (start.getParent() != null) {
            orbits.add(start);
            start = start.getParent();
        }

        return orbits;
    }
}
