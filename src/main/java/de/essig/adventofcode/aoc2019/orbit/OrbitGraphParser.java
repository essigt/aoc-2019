package de.essig.adventofcode.aoc2019.orbit;

import java.util.ArrayList;
import java.util.List;


public class OrbitGraphParser {

    public Orbit parseGraph(String grap) {

        Orbit COM = new Orbit("COM", null);
        String[] relations = grap.split("\\n");

        COM.addChildren(parseChildren(COM, relations));

        return COM;
    }


    private List<Orbit> parseChildren(Orbit parent, String[] relations) {

        List<Orbit> orbits = new ArrayList<>();

        for (String relation : relations) {
            String firstPart = getFirstPart(relation);
            String secondPart = getSecondPart(relation);

            if (firstPart.equals(parent.getCenter())) {
                Orbit orbit = new Orbit(secondPart, parent);
                orbit.addChildren(parseChildren(orbit, relations));
                orbits.add(orbit);
            }
        }

        return orbits;
    }


    private String getFirstPart(String relation) {

        return relation.split("\\)")[0];
    }


    private String getSecondPart(String relation) {

        return relation.split("\\)")[1];
    }
}
