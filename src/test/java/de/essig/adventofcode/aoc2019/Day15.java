package de.essig.adventofcode.aoc2019;

import de.essig.adventofcode.aoc2019.droid.Field;
import de.essig.adventofcode.aoc2019.droid.ShortestPath;
import de.essig.adventofcode.aoc2019.intcode.Context;
import de.essig.adventofcode.aoc2019.intcode.IntcodeInterpreter;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static de.essig.adventofcode.aoc2019.intcode.Context.newContext;
import static de.essig.adventofcode.aoc2019.intcode.IntcodeParser.parseProgramm;


public class Day15 {

    private Map<Field, Long> map = new HashMap<>();

    IntcodeInterpreter intcodeInterpreter = new IntcodeInterpreter();

    @Test
    void partOne() {
        Random rnd = new Random();
        int posX = 0;
        int posY = 0;

        Context context = newContext(parseProgramm(Day15Data.PROGRAM));

        Field oxygenField = null;

        for(int i=0; i < 500_000; i++) {
            long direction = (long)rnd.nextInt(4) + 1;



            // Check if input is a smart idea
  /*          if(direction == 1 && map.getOrDefault(new Field(posX, posY-1), -1L) == 0) {
                direction++;
            }
            if(direction == 2 && map.getOrDefault(new Field(posX, posY+1), -1L) == 0) {
                direction++;
            }
            if(direction == 3 && map.getOrDefault(new Field(posX-1, posY), -1L) == 0) {
                direction++;
            }
            if(direction == 4 && map.getOrDefault(new Field(posX+1, posY), -1L) == 0) {
                direction = 1;
            }
*/


            /*if(direction == 3 && map.get(new Field(posX, posY)) == 0) {
                direction++;
            }*/


            context.addInput(direction);

            while(context.getOutput().size() == 0 && !context.isHalted()) {
                context = intcodeInterpreter.runStep(context);
            }

            if(context.getOutput().size() == 0) {
                System.out.println("No Output");
                break;
            }

            if(context.getOutput().get(0) == 0) { // BLOCCK
                int nextX = nextX(posX, direction);
                int nextY = nextY(posY, direction);
                map.put(new Field(nextX, nextY), context.getOutput().get(0));
            } else {
                posX = nextX(posX, direction);
                posY = nextY(posY, direction);
                map.put(new Field(posX, posY), context.getOutput().get(0));

                if(context.getOutput().get(0) == 2) {
                    System.out.println("Found Oxygen");
                    oxygenField = new Field(posX, posY);
                    break;
                }
            }

            context.resetOutputs();
        }

        paintHull();

        //Optional<Field> field = map.entrySet().stream().filter(e -> e.getValue().equals(2L)).map(Map.Entry::getKey).findFirst();

        System.out.println("Oxygen: " + oxygenField);

        try {
            FileOutputStream f = new FileOutputStream(new File("myObjects.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);

            o.writeObject(map);

            o.close();
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //System.out.println("Shortest Path: " + new ShortestPath().shortestPath(0,0, field.get().getX(),field.get().getY() ,map));

        // 205 = to low
    }

    private Field findNearestFieldWithOpenNeigbours(int x, int y) {

        map.entrySet().stream().filter(e -> !e.getValue().equals(0L)).filter(e -> hasOpenNeibours(e.getKey())).map(Map.Entry::getKey).collect(Collectors.toList());
        return null;
    }

    private boolean hasOpenNeibours(Field field) {

        int startX = field.getX();
        int startY = field.getY();

        for( int y = startY-1; y <= startY+1; y++) {
            for (int x = startX - 1; x <= startX + 1; x++) {

                if(startX != x ^ startY != y) {
                    if (map.getOrDefault(new Field(x, y), -1L) == -1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int nextY(int y, long dir) {
        if(dir == 1) {
            return y-1;
        } else if(dir == 2)
            return y+1;
        else
            return y;
    }

    private int nextX(int x, long dir) {
        if(dir == 3) {
            return x-1;
        } else if(dir == 4)
            return x+1;
        else
            return x;
    }

    private Long getColor(int x, int y) {
        return map.getOrDefault(new Field(x,y), -1L);
    }

    private void paintHull() {
        int xMin = map.keySet().stream().mapToInt(Field::getX).min().orElse(0);
        int xMax = map.keySet().stream().mapToInt(Field::getX).max().orElse(0);

        int yMin = map.keySet().stream().mapToInt(Field::getY).min().orElse(0);
        int yMax = map.keySet().stream().mapToInt(Field::getY).max().orElse(0);

        System.out.println("--- Field ---");
        for(int y = yMin; y <= yMax; y++) {
            for(int x = xMin; x <= xMax; x++) {
                Long color = getColor(x, y);

                if(x == 0 && y == 0) {
                    System.out.print("D");
                } else if(color == 0) {
                    System.out.print("#");
                } else if(color == 1) {
                    System.out.print(".");
                } else if(color == 2) {
                    System.out.print("O");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }


    private String prettyPrint(Context context) {

        return context.getProgrammAsIntEnd().values().stream().map(String::valueOf).collect(Collectors.joining(","));
    }


}
