package de.essig.adventofcode.aoc2019.network;

public class Package {


    private long destination;
    private long x;
    private long y;

    public Package(long destination, long x, long y) {
        this.destination = destination;
        this.x = x;
        this.y = y;
    }

    public long getDestination() {
        return destination;
    }

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Package{" +
                "destination=" + destination +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
