package de.essig.adventofcode.aoc2019;

import de.essig.adventofcode.aoc2019.droid.Field;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Day24Part2 {


    private int size = 5;

    private Map<Integer, Map<Field, Integer>> previousMap
            = new HashMap<>();
    private Map<Integer, Map<Field, Integer>> map = new HashMap<>();


    @Test
    void runPartTwo() {

        initMapProblem();
        //initMapTest();

        for (int i = 0; i <= 200; i++) {
            System.out.println("\n\nAfter " + i + " minutes");
            //paintMap(-1);
            paintMap(0);
            //paintMap(1);
            System.out.println("Count Bugs: " + countBugs());

            iterate();

        }


    }

    private int countBugs() {

        return map.entrySet().stream().mapToInt( e -> (int)e.getValue().values().stream().filter(v -> v == 1L).count()).sum();
    }


    private void iterate() {
        previousMap = copyMap(map);


        for(int level : map.keySet()) {
            Map<Field, Integer> currentLevelMap = map.get(level);

            for(int y=0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if(x==2 && y == 2) {
                        continue;
                    }

                    int neigbours = neigbours(x,y, level);
                    long self = previousMap.get(level).getOrDefault(new Field(x,y), 0);

                    if(self == 0 && (neigbours == 1 || neigbours == 2)) {
                        currentLevelMap.put(new Field(x,y), 1);
                        //System.out.println(level + ": " + x + "/" + y +" comes alive");
                    } else if(self == 1) {
                        if(neigbours != 1) {
                            currentLevelMap.put(new Field(x, y), 0);
                            //System.out.println(level + ": " + x + "/" + y +" dies");
                        }
                    }
                }
            }
        }

    }

    private Map<Integer, Map<Field, Integer>> copyMap(Map<Integer, Map<Field, Integer>> map) {

        Map<Integer, Map<Field, Integer>>  newMap = new HashMap<>();

        for (Map.Entry<Integer, Map<Field, Integer>> entry : map.entrySet()) {
            newMap.put(entry.getKey(), new HashMap<>(entry.getValue()));
        }


        return newMap;
    }


    private int neigbours(int x, int y, int baseLevel) {

        Integer neigbourTop = getFieldValue(x, y-1, x, y, baseLevel);
        Integer neigbourBottom = getFieldValue(x, y+1, x, y, baseLevel);
        Integer neigbourLeft = getFieldValue(x-1, y, x, y, baseLevel);
        Integer neigbourRight = getFieldValue(x+1, y, x, y, baseLevel);;

        return  neigbourBottom + neigbourTop + neigbourLeft + neigbourRight;
    }

    private int getFieldValue(int x, int y, int xOrig, int yOrig, int baseLevel) {
        Map<Field, Integer> lowerLevel = previousMap.getOrDefault(baseLevel-1, new HashMap<>());
        Map<Field, Integer> currentLevel = previousMap.getOrDefault(baseLevel, new HashMap<>());
        Map<Field, Integer> upperLevel = previousMap.getOrDefault(baseLevel+1, new HashMap<>());

        if( x == 2 && y == 2) {
            //lowerLevel
            if( xOrig < 2 && y == yOrig) {
                return  lowerLevel.getOrDefault(new Field(0, 0), 0) +
                        lowerLevel.getOrDefault(new Field(0, 1), 0) +
                        lowerLevel.getOrDefault(new Field(0, 2), 0) +
                        lowerLevel.getOrDefault(new Field(0, 3), 0) +
                        lowerLevel.getOrDefault(new Field(0, 4), 0);
            } else if( xOrig > 2 && y == yOrig) {
                return  lowerLevel.getOrDefault(new Field(4, 0), 0) +
                        lowerLevel.getOrDefault(new Field(4, 1), 0) +
                        lowerLevel.getOrDefault(new Field(4, 2), 0) +
                        lowerLevel.getOrDefault(new Field(4, 3), 0) +
                        lowerLevel.getOrDefault(new Field(4, 4), 0);
            } else if( xOrig == x && yOrig < 2) {
                return  lowerLevel.getOrDefault(new Field(0, 0), 0) +
                        lowerLevel.getOrDefault(new Field(1, 0), 0) +
                        lowerLevel.getOrDefault(new Field(2, 0), 0) +
                        lowerLevel.getOrDefault(new Field(3, 0), 0) +
                        lowerLevel.getOrDefault(new Field(4, 0), 0);
            } else if( xOrig == x && yOrig > 2) {
                return  lowerLevel.getOrDefault(new Field(0, 4), 0) +
                        lowerLevel.getOrDefault(new Field(1, 4), 0) +
                        lowerLevel.getOrDefault(new Field(2, 4), 0) +
                        lowerLevel.getOrDefault(new Field(3, 4), 0) +
                        lowerLevel.getOrDefault(new Field(4, 4), 0);
            }
        }

        if(x >= 0 && x < size && y >= 0 && y < size) {
            return  currentLevel.getOrDefault(new Field(x, y), 0);
        }

        int sum = 0;
        if(x < 0) {
            sum += upperLevel.getOrDefault(new Field(1,2), 0);
        }

        if(x == size) {
            sum += upperLevel.getOrDefault(new Field(3,2), 0);
        }

        if(y < 0) {
            sum += upperLevel.getOrDefault(new Field(2,1), 0);
        }

        if(y == size) {
            sum += upperLevel.getOrDefault(new Field(2,3), 0);
        }

        return sum;
    }

    private void initMapTest() {
        map.clear();

        for(int i=-200; i <= 200; i++) {
            map.put(i, new HashMap<>());
        }


        Map<Field, Integer> level0 = map.get(0);

        level0.put(new Field(4,0), 1);

        level0.put(new Field(0,1), 1);
        level0.put(new Field(3,1), 1);

        level0.put(new Field(0,2), 1);
        level0.put(new Field(3,2), 1);
        level0.put(new Field(4,2), 1);

        level0.put(new Field(2,3), 1);

        level0.put(new Field(0,4), 1);
    }

    private void initMapProblem() {
        map.clear();

        for(int i=-200; i <= 200; i++) {
            map.put(i, new HashMap<>());
        }


        Map<Field, Integer> level0 = map.get(0);

        level0.put(new Field(2,0), 1);
        level0.put(new Field(4,0), 1);

        level0.put(new Field(0,1), 1);
        level0.put(new Field(1,1), 1);
        level0.put(new Field(2,1), 1);
        level0.put(new Field(3,1), 1);
        level0.put(new Field(4,1), 1);

        level0.put(new Field(1,2), 1);

        level0.put(new Field(3,3), 1);

        level0.put(new Field(0,4), 1);
        level0.put(new Field(1,4), 1);
    }

    private void paintMap(int level) {
        Map<Field, Integer> level0 = map.get(level);

        int xMin = level0.keySet().stream().mapToInt(Field::getX).min().orElse(0);
        int xMax = level0.keySet().stream().mapToInt(Field::getX).max().orElse(0);

        int yMin = level0.keySet().stream().mapToInt(Field::getY).min().orElse(0);
        int yMax = level0.keySet().stream().mapToInt(Field::getY).max().orElse(0);

        System.out.println("--- MAP (" + level + ")---");
        for(int y = yMin; y <= yMax; y++) {
            for(int x = xMin; x <= xMax; x++) {
                int color = level0.getOrDefault( new Field(x,y), 0);

                if( x == 2 && y == 2) {
                    System.out.print("?");
                } else if(color == 0) {
                    System.out.print(".");
                } else if(color == 1) {
                    System.out.print("#");
                }
            }
            System.out.println(" " + y);
        }
    }
}
