package de.essig.adventofcode.aoc2019;

import de.essig.adventofcode.aoc2019.droid.Field;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Day24Part1 {


    private int size = 5;
    private Map<Field, Long> previousMap
            = new HashMap<>();
    private Map<Field, Long> map = new HashMap<>();


    private Set<Map<Field,Long>> history = new HashSet<>();



    @Test
    void runPartOne() {

        initMapProblem();

        for (int i = 0; i < 100; i++) {
            System.out.println("\n\nAfter " + i + " minutes");
            paintMap();


            if(history.contains(map)) {
                System.out.println("Found doublicate");
                System.out.println("Sum: " + calculateBiodiversity());
                break;
            } else {
                history.add(new HashMap<>(map));
            }

            iterate();

        }


    }


    private void iterate() {
        previousMap = new HashMap<>(map);

        for(int y=0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int neigbours = neigbours(x,y);
                long self = previousMap.getOrDefault(new Field(x,y), 0L);

                if(self == 0 && (neigbours == 1 || neigbours == 2)) {
                    map.put(new Field(x,y), 1L);
                } else if(self == 1) {
                    if(neigbours != 1) {
                        map.put(new Field(x, y), 0L);
                    }
                }
            }
        }
    }

    private int neigbours(int x, int y) {
        Long neigbourTop = previousMap.getOrDefault(new Field(x, y-1), 0L);
        Long neigbourBottom = previousMap.getOrDefault(new Field(x, y+1), 0L);
        Long neigbourLeft = previousMap.getOrDefault(new Field(x-1, y), 0L);
        Long neigbourRight = previousMap.getOrDefault(new Field(x+1, y), 0L);

        return  (int)(neigbourBottom + neigbourTop + neigbourLeft + neigbourRight);
    }

    private long calculateBiodiversity() {

        long sum = 0;
        int counter = 0;
        for(int y=0; y < size; y++) {
            for (int x = 0; x < size; x++) {

                if(map.getOrDefault(new Field(x,y), 0L) == 1L) {
                    long pow = (long)Math.pow(2, counter);
                    //System.out.println("Counter: " + counter + " POW: " + pow);
                    sum += pow;
                }

                counter++;
            }
        }

        return sum;
    }

    private void initMapTest() {
        map.clear();

        map.put(new Field(4,0), 1L);

        map.put(new Field(0,1), 1L);
        map.put(new Field(3,1), 1L);

        map.put(new Field(0,2), 1L);
        map.put(new Field(3,2), 1L);
        map.put(new Field(4,2), 1L);

        map.put(new Field(2,3), 1L);

        map.put(new Field(0,4), 1L);
    }

    private void initMapProblem() {
        map.clear();

        map.put(new Field(2,0), 1L);
        map.put(new Field(4,0), 1L);

        map.put(new Field(0,1), 1L);
        map.put(new Field(1,1), 1L);
        map.put(new Field(2,1), 1L);
        map.put(new Field(3,1), 1L);
        map.put(new Field(4,1), 1L);

        map.put(new Field(1,2), 1L);

        map.put(new Field(3,3), 1L);

        map.put(new Field(0,4), 1L);
        map.put(new Field(1,4), 1L);
    }

    private void paintMap() {
        int xMin = map.keySet().stream().mapToInt(Field::getX).min().orElse(0);
        int xMax = map.keySet().stream().mapToInt(Field::getX).max().orElse(0);

        int yMin = map.keySet().stream().mapToInt(Field::getY).min().orElse(0);
        int yMax = map.keySet().stream().mapToInt(Field::getY).max().orElse(0);

        System.out.println("--- MAP ---");
        for(int y = yMin; y <= yMax; y++) {
            for(int x = xMin; x <= xMax; x++) {
                Long color = map.getOrDefault( new Field(x,y), 0L);


                if(color == 0L) {
                    System.out.print(".");
                } else if(color == 1L) {
                    System.out.print("#");
                }
            }
            System.out.println(" " + y);
        }
    }
}
