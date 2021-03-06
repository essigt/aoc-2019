package de.essig.adventofcode.aoc2019;

import de.essig.adventofcode.aoc2019.nanofactory.ResourceDefinition;
import de.essig.adventofcode.aoc2019.nanofactory.ResourceGrapParser;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;

public class Day14Part2 {

    private Map<String, Integer> resourceCache = new HashMap<>();
    private long oreRemaining = 1000000000000L;

    @Test
    void sampleThree() {
        List<ResourceDefinition> resourceDefinitions = new ResourceGrapParser().parseGraph(Day14Data.SAMPLE_THREE);

        int fuelProduced = 0;
        while(true) {
            try {
                int fuel = calculateOre("FUEL", 1, resourceDefinitions).getCost();
            } catch (Exception e) {
                break;
            }
            fuelProduced++;
        }
        assertThat(fuelProduced, Matchers.is(82892753));
    }

    @Test
    void sampleFour() {
        List<ResourceDefinition> resourceDefinitions = new ResourceGrapParser().parseGraph(Day14Data.SAMPLE_FOUR);

        int fuelProduced = 0;
        while(true) {
            try {
                int fuel = calculateOre("FUEL", 1, resourceDefinitions).getCost();
            } catch (Exception e) {
                break;
            }
            fuelProduced++;
        }
        assertThat(fuelProduced, Matchers.is(5586022));
    }

    @Test
    void sampleFive() {
        List<ResourceDefinition> resourceDefinitions = new ResourceGrapParser().parseGraph(Day14Data.SAMPLE_FIVE);

        int fuelProduced = 0;
        while(true) {
            try {
                int fuel = calculateOre("FUEL", 1, resourceDefinitions).getCost();
            } catch (Exception e) {
                break;
            }
            fuelProduced++;
        }
        assertThat(fuelProduced, Matchers.is(460664));
    }

    @Test
    void runPartTwo() {
        List<ResourceDefinition> resourceDefinitions = new ResourceGrapParser().parseGraph(Day14Data.PROGRAM);

        int fuelProduced = 0;
        while(true) {
            try {
                int fuel = calculateOre("FUEL", 1, resourceDefinitions).getCost();
            } catch (Exception e) {
                break;
            }
            fuelProduced++;
        }
        assertThat(fuelProduced, Matchers.is(4436981)); // actual=4436981 // 33m12s191ms
    }


    private AmountAndCost calculateOre(String resourceName, int amountWanted, List<ResourceDefinition> resourceDefinitions) {
        //System.out.println("Require " + amountWanted + " of " +resourceName);

        if(resourceName.equals("ORE")) {
            if(oreRemaining > amountWanted) {
                oreRemaining -= amountWanted;
                return new AmountAndCost(amountWanted, amountWanted);
            } else {
                throw new IllegalArgumentException("Not enought ore left: " + oreRemaining);
            }
        }

        ResourceDefinition resourceDefinition = findResourceDefinitionByName(resourceName, resourceDefinitions);

        if(resourceCache.containsKey(resourceName)) {
            Integer amountInCache = resourceCache.get(resourceName);
            //System.out.println("-- Found in Cache: " + amountInCache + " " + resourceName);

            if(amountInCache >= amountWanted) {
                removeFromCache(resourceName, amountWanted);
                return new AmountAndCost(amountWanted,0);
            } else {
                removeFromCache(resourceName, amountInCache);
                amountWanted = amountWanted - amountInCache;
            }
        }


        int amountToGenerate = calculateAmountToGenerate(resourceDefinition, amountWanted);


        int sumOre = 0;

        for(String needName : resourceDefinition.getNeeds().keySet()) {
            int amount = resourceDefinition.getNeeds().get(needName) * amountToGenerate;
            //System.out.println("Need " + needName + " Amount: " + amount);

            AmountAndCost amountAndCost = calculateOre(needName, amount, resourceDefinitions);
            sumOre += amountAndCost.getCost();
        }

        if( amountToGenerate * resourceDefinition.getAmount() > amountWanted) {
            addToCache(resourceName, amountToGenerate* resourceDefinition.getAmount() - amountWanted);
        }

        return new AmountAndCost(amountWanted,sumOre);
    }

    private int calculateAmountToGenerate(ResourceDefinition resourceDefinitionByName, int amountWanted) {
        int amountsToGenerate = 0;
        int amountsEarned = 0;
        while(amountsEarned < amountWanted) {
            amountsToGenerate++;
            amountsEarned += resourceDefinitionByName.getAmount();
        }

        return amountsToGenerate;
    }

    private void removeFromCache(String needName, int amount) {
        //System.out.println("-- Remove from cache " + amount + " " +needName);
        Integer amountInCache = resourceCache.get(needName);
        resourceCache.put(needName, amountInCache - amount);
    }

    private void addToCache(String needName, int amount) {
        //System.out.println("-- Add to cache " + amount + " " +needName);
        Integer amountInCache = resourceCache.getOrDefault(needName, 0);
        resourceCache.put(needName, amountInCache + amount);
    }

    private ResourceDefinition findResourceDefinitionByName(String name, List<ResourceDefinition> resourceDefinitions) {
        List<ResourceDefinition> collect = resourceDefinitions.stream().filter(def -> def.getName().equals(name)).collect(Collectors.toList());

        if(collect.size() > 1) {
            throw new IllegalArgumentException("FATAL: Multiple definitions found for " + name);
        } else if(collect.size() == 0) {
            throw new IllegalArgumentException("FATAL: No definition found for " + name);
        }else {
            return collect.get(0);
        }
    }

    private static class AmountAndCost {
        private int amount;
        private int cost;

        public AmountAndCost(int amount, int cost) {
            this.amount = amount;
            this.cost = cost;
        }

        public int getAmount() {
            return amount;
        }

        public int getCost() {
            return cost;
        }
    }
}
