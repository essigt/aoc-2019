package de.essig.adventofcode.aoc2019.orbit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Orbit {

    private String center;
    private Orbit parent;
    private List<Orbit> children = new ArrayList<>();

    public Orbit(String center, Orbit parent) {

        this.center = center;
        this.parent = parent;
    }

    public String getCenter() {

        return center;
    }


    public Orbit getParent() {

        return parent;
    }


    public boolean hasChildren() {

        return !children.isEmpty();
    }


    public void addChildren(List<Orbit> orbit) {

        children.addAll(orbit);
    }


    public List<Orbit> getChildren() {

        return children;
    }


    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Orbit orbit = (Orbit) o;

        return center.equals(orbit.center);
    }


    @Override
    public int hashCode() {

        return Objects.hash(center);
    }


    @Override
    public String toString() {

        return "Orbit{"
            + "center='" + center + '\''
            + ", children=" + children + '}';
    }


    public void prettyPrint() {

        prettyPrint(0);
    }


    private void prettyPrint(int dept) {

        for (int i = 0; i < dept; i++) {
            System.out.print(" |");
        }

        System.out.println("- " + center + ":");
        children.forEach(c -> c.prettyPrint(dept + 1));
    }
}
