package de.essig.adventofcode.aoc2019.nanofactory;

import de.essig.adventofcode.aoc2019.orbit.Orbit;

import java.util.*;

public class ResourceDefinition {

    private String name;
    private int amount;
    private Map<String, Integer> needs = new HashMap<>();

    public ResourceDefinition(String name, int amount, Map<String, Integer> needs) {
        this.name = name;
        this.amount = amount;
        this.needs = needs;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public Map<String, Integer> getNeeds() {
        return needs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceDefinition that = (ResourceDefinition) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "ResourceDefinition{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                ", needs=" + needs +
                '}';
    }
}
