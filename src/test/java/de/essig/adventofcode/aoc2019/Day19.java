package de.essig.adventofcode.aoc2019;

import de.essig.adventofcode.aoc2019.droid.Field;
import de.essig.adventofcode.aoc2019.intcode.Context;
import de.essig.adventofcode.aoc2019.intcode.IntcodeInterpreter;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static de.essig.adventofcode.aoc2019.intcode.Context.newContext;
import static de.essig.adventofcode.aoc2019.intcode.IntcodeParser.parseProgramm;

public class Day19 {

    private final static String PROGRAM = "109,424,203,1,21101,11,0,0,1106,0,282,21102,18,1,0,1106,0,259,1201,1,0,221,203,1,21102,31,1,0,1106,0,282,21101,0,38,0,1105,1,259,21002,23,1,2,21202,1,1,3,21102,1,1,1,21102,1,57,0,1105,1,303,2101,0,1,222,21002,221,1,3,21002,221,1,2,21101,0,259,1,21101,0,80,0,1105,1,225,21101,169,0,2,21101,0,91,0,1106,0,303,1202,1,1,223,20101,0,222,4,21101,259,0,3,21102,225,1,2,21102,225,1,1,21101,0,118,0,1106,0,225,20102,1,222,3,21101,94,0,2,21101,0,133,0,1106,0,303,21202,1,-1,1,22001,223,1,1,21102,148,1,0,1105,1,259,2102,1,1,223,21001,221,0,4,21002,222,1,3,21101,0,22,2,1001,132,-2,224,1002,224,2,224,1001,224,3,224,1002,132,-1,132,1,224,132,224,21001,224,1,1,21101,0,195,0,106,0,108,20207,1,223,2,21002,23,1,1,21102,1,-1,3,21102,214,1,0,1105,1,303,22101,1,1,1,204,1,99,0,0,0,0,109,5,1202,-4,1,249,21201,-3,0,1,21202,-2,1,2,22101,0,-1,3,21101,0,250,0,1106,0,225,21202,1,1,-4,109,-5,2106,0,0,109,3,22107,0,-2,-1,21202,-1,2,-1,21201,-1,-1,-1,22202,-1,-2,-2,109,-3,2105,1,0,109,3,21207,-2,0,-1,1206,-1,294,104,0,99,21202,-2,1,-2,109,-3,2105,1,0,109,5,22207,-3,-4,-1,1206,-1,346,22201,-4,-3,-4,21202,-3,-1,-1,22201,-4,-1,2,21202,2,-1,-1,22201,-4,-1,1,22101,0,-2,3,21102,343,1,0,1105,1,303,1106,0,415,22207,-2,-3,-1,1206,-1,387,22201,-3,-2,-3,21202,-2,-1,-1,22201,-3,-1,3,21202,3,-1,-1,22201,-3,-1,2,21201,-4,0,1,21101,0,384,0,1105,1,303,1106,0,415,21202,-4,-1,-4,22201,-4,-3,-4,22202,-3,-2,-2,22202,-2,-4,-4,22202,-3,-2,-3,21202,-4,-1,-2,22201,-3,-2,1,21201,1,0,-4,109,-5,2106,0,0";

    private Map<Field, Long> map = new HashMap<>();

    @Test
    void partOne() {
        IntcodeInterpreter intcodeInterpreter = new IntcodeInterpreter();

        int affected = 0;

        for(int y =0; y < 50; y++) {
            for(int x =0; x < 50; x++) {
                Context context = newContext(parseProgramm(PROGRAM));
                context.addInput((long)x);
                context.addInput((long)y);

                while(!context.isHalted()) {
                    context = intcodeInterpreter.runStep(context);
                }

                if(context.getOutput().get(0) == 1L) {
                    System.out.print("x");
                    affected++;
                } else {
                    System.out.print(".");
                }

            }
            System.out.println("");

        }

        System.out.println("Affected: " + affected);
    }

    @Test
    void partTwoSecondGuess() {
        IntcodeInterpreter intcodeInterpreter = new IntcodeInterpreter();

        int squareSize = 100;


        int xMax = 2000;
        int yMax = 2000;

        int lastStartX = 0;
        int lastStopX = xMax;
        boolean found = false;
        for(int y =0; y < yMax && !found; y++) {

            if(y%100==0)
                System.out.println(y);

            boolean alreadyFound = false;

            //print(lastStartX, '.');
            int x =lastStartX;

            int xMaxTmp = xMax;
            if(y < 10) {
                xMaxTmp = 15;
            }
            for(;x < xMaxTmp;) {

                Context context = newContext(parseProgramm(PROGRAM));
                context.addInput((long)x);
                context.addInput((long)y);

                while(!context.isHalted()) {
                    context = intcodeInterpreter.runStep(context);
                }


                if(context.getOutput().get(0) == 1L) {
                    map.put(new Field(x,y), 1L);

                    if(!alreadyFound) { // Found first x, skip
                        lastStartX = x;

                        if(lastStopX > x && lastStopX < xMax) {
                            paintLine(x,lastStopX, y);
                            x = lastStopX;
                        }
                    }
                    alreadyFound=true;
                } else {
                    if(alreadyFound) {
                        lastStopX = x-2;
                        break;
                    }
                }
                x++;
            }

            int search = search(squareSize, lastStopX, y);
            if(search > 0) {
                //paintMap();
                System.out.println("Search: " + search);
                break;
            }

        }



    }


    private int search(int squareSize, int xMax, int y) {

        int squareX=0;
        int squareY=0;


        for(int x =0; x <= xMax ; x++) {

            if(map.getOrDefault(new Field(x, y), 0L) == 1L) {

                Long topLeft = map.getOrDefault(new Field(x, y - (squareSize-1)), 0L);
                Long topRight = map.getOrDefault(new Field(x + (squareSize-1), y - (squareSize-1)), 0L);

                if(topLeft == 1L && topRight == 1L) {
                    squareX = x;
                    squareY = y-(squareSize-1);
                    System.out.println("Found Square: Start Pos:" + squareX + "/" + squareY);
                    paintSquare(squareX, squareY, squareSize);
                    break;
                }

            }
        }


        //System.out.println("Found Square: Start Pos:" + squareX + "/" + squareY);

        return squareX * 10_000 + squareY;
    }

    private void paintLine(int startX, int lastStopX, int y) {
        for(int x=startX; x <= lastStopX; x++) {
                map.put(new Field(x,y), 1L);

        }
    }


    private void paintSquare(int startX, int startY, int squareSize) {

        for(int x=startX; x < startX + squareSize; x++) {
            for(int y=startY; y < startY + squareSize ; y++) {
                if(map.getOrDefault(new Field(x,y), 0L) == 1L)
                    map.put(new Field(x,y), 2L);
                else
                    map.put(new Field(x,y), 3L);

            }
        }
    }

    private void print(int lastStartX, char ch) {
        for(int i=0; i < lastStartX; i++) {
            System.out.print(ch);
        }
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
                    System.out.print("x");
                } else if(color == 2L) {
                    System.out.print("O");
                } else if(color == 3L) {
                    System.out.print("T");
                }
            }
            System.out.println(" " + y);
        }
    }
}
