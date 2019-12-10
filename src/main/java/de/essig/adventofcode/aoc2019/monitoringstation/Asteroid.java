package de.essig.adventofcode.aoc2019.monitoringstation;

import java.util.Objects;


/**
 * @author  Tim Essig - essig@synyx.de
 */
public class Asteroid {

    private int x;
    private int y;
    private int visibleAstreroids;

    public Asteroid(int x, int y) {

        this.x = x;
        this.y = y;
    }

    public int getX() {

        return x;
    }


    public int getY() {

        return y;
    }


    public int getVisibleAstreroids() {

        return visibleAstreroids;
    }


    public void setVisibleAstreroids(int visibleAstreroids) {

        this.visibleAstreroids = visibleAstreroids;
    }


    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Asteroid asteroid = (Asteroid) o;

        return x == asteroid.x && y == asteroid.y;
    }


    @Override
    public int hashCode() {

        return Objects.hash(x, y);
    }


    @Override
    public String toString() {

        return "Asteroid{"
            + "x=" + x
            + ", y=" + y
            + ", visibleAstreroids=" + visibleAstreroids + '}';
    }
}
