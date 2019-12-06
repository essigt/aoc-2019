package de.essig.adventofcode.aoc2019.orbit;

/**
 * @author  Tim Essig - essig@synyx.de
 */
public class OrbitCheckSum {

    public static Distance calculateChecksum(Orbit com) {

        Distance dist = new Distance();

        for (Orbit orbit : com.getChildren()) {
            dist.add(calculateChecksum(orbit, 0));
        }

        return dist;
    }


    private static Distance calculateChecksum(Orbit parent, int previousDistance) {

        Distance dist = new Distance();

        if (parent.hasChildren()) {
            for (Orbit orbit : parent.getChildren()) {
                dist.add(calculateChecksum(orbit, previousDistance + 1));
            }
        }

        dist.add(new Distance(previousDistance, 1));

        return dist;
    }

    public static class Distance {

        int indirectOrbits;
        int directOrbits;

        public Distance() {
        }


        public Distance(int indirectOrbits, int directOrbits) {

            this.indirectOrbits = indirectOrbits;
            this.directOrbits = directOrbits;
        }

        public void add(Distance distance) {

            this.indirectOrbits += distance.indirectOrbits;
            this.directOrbits += distance.directOrbits;
        }


        public int getSum() {

            return indirectOrbits + directOrbits;
        }


        public int getIndirectOrbits() {

            return indirectOrbits;
        }


        public int getDirectOrbits() {

            return directOrbits;
        }
    }
}
