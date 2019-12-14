package de.essig.adventofcode.aoc2019.nanofactory;

import de.essig.adventofcode.aoc2019.orbit.Orbit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceGrapParser {

    public List<ResourceDefinition> parseGraph(String grap) {
        List<ResourceDefinition> resourceDefinitions = new ArrayList<>();

        String[] lines = grap.split("\\n");

        for (String relation : lines)
        {

            String secondPart = getSecondPart(relation);

            int amount = getAmount(secondPart);
            String name = getName(secondPart);

            String firstPart = getFirstPart(relation);

            //System.out.println("Amount: " + amount + " Name: " + name);

            Map<String, Integer> needs = new HashMap<>();
            for(String need : firstPart.split(",", -1)) {
                need = need.trim();

                int needAmount = getAmount(need);
                String needName = getName(need);
                needs.put(needName, needAmount);
            }
            //System.out.println("\tNeeds: " + needs);

            resourceDefinitions.add( new ResourceDefinition(name, amount, needs));
        }

        return resourceDefinitions;
    }



    private int getAmount(String relation) {

        return Integer.valueOf(relation.split(" ")[0]);
    }

    private String getName(String relation) {

        return relation.split(" ")[1];
    }

    private String getFirstPart(String relation) {

        return relation.split(" => ")[0];
    }


    private String getSecondPart(String relation) {

        return relation.split(" => ")[1];
    }
}
