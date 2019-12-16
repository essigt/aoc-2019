package de.essig.adventofcode.aoc2019.droid;

import java.util.*;
import java.util.stream.Collectors;

public class ShortestPath {

    private Set<Field> queue = new HashSet<>();
    private Set<Field> visited = new HashSet<>();
    private Map<Field, Integer> costs = new HashMap<>();
    private Map<Field, Long> map;
    private Map<Field, Field> precursors = new HashMap<>();

    public int shortestPath(Field start, Field end, Map<Field, Long> map) {
        //Init
        this.map = map;
        for(Field field : map.keySet()) {
            costs.put(field, Integer.MAX_VALUE);
        }
        costs.put(start, 0);
        queue.add(start);

        // Iterate
        while(!queue.isEmpty()) {
            Field cheapestField = getCheapestField();
            queue.add(cheapestField);
            int cost = costs.get(cheapestField);

            List<Field> neigbours = findNeigbours(cheapestField, map);
            neigbours.removeAll(visited); // Remove all visited
            for(Field neigbour : neigbours) {
                int currentCosts = cost + 1;
                if(currentCosts < costs.get(neigbour)) {
                    costs.put(neigbour, currentCosts);
                    precursors.put(neigbour, cheapestField);
                }

            }

            queue.addAll(neigbours);
            queue.remove(cheapestField);
            visited.add(cheapestField);
        }


        // Find Patch
        System.out.println("Cost: " + costs.get(end));
        System.out.println("Most Expensive:" + costs.get(getMostExpensiveField()));

        Field precursor = precursors.get(end);
        while(precursor != start && precursor != null) {
            //System.out.println("Precursor: " + precursor);
            precursor = precursors.get(precursor);
        }



        return costs.get(end);
    }

    private Field getCheapestField() {
        return costs.entrySet().stream().filter(e -> !visited.contains(e.getKey())).min(Comparator.comparing(Map.Entry::getValue)).map(Map.Entry::getKey).get();
    }

    private Field getMostExpensiveField() {
        return costs.entrySet().stream().filter(e -> e.getValue() != 0).filter(e -> !visited.contains(e.getKey())).max(Comparator.comparing(Map.Entry::getValue)).map(Map.Entry::getKey).get();
    }


    private List<Field> findNeigbours(Field field, Map<Field, Long> map) {
        List<Field> neigbours = new ArrayList<>();
        int startX = field.getX();
        int startY = field.getY();


        Field currentField = new Field(startX,startY+1);
        long fieldValue = map.getOrDefault(currentField, -1L);

        if(fieldValue == 1 || fieldValue == 2) {
            neigbours.add(currentField);
        }

        currentField = new Field(startX,startY-1);
        fieldValue = map.getOrDefault(currentField, -1L);

        if(fieldValue == 1 || fieldValue == 2) {
            neigbours.add(currentField);
        }

        currentField = new Field(startX+1,startY);
        fieldValue = map.getOrDefault(currentField, -1L);

        if(fieldValue == 1 || fieldValue == 2) {
            neigbours.add(currentField);
        }

        currentField = new Field(startX-1,startY);
        fieldValue = map.getOrDefault(currentField, -1L);

        if(fieldValue == 1 || fieldValue == 2) {
            neigbours.add(currentField);
        }


        return neigbours;
    }

}
