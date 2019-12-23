package de.essig.adventofcode.aoc2019;

import de.essig.adventofcode.aoc2019.droid.Field;
import de.essig.adventofcode.aoc2019.intcode.Context;
import de.essig.adventofcode.aoc2019.intcode.IntcodeInterpreter;
import de.essig.adventofcode.aoc2019.intcode.IntcodeParser;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

public class Day17 {


    private final static String PROGRAM = "1,330,331,332,109,4928,1102,1182,1,16,1101,0,1483,24,101,0,0,570,1006,570,36,102,1,571,0,1001,570,-1,570,1001,24,1,24,1106,0,18,1008,571,0,571,1001,16,1,16,1008,16,1483,570,1006,570,14,21101,58,0,0,1106,0,786,1006,332,62,99,21102,1,333,1,21102,1,73,0,1106,0,579,1101,0,0,572,1102,0,1,573,3,574,101,1,573,573,1007,574,65,570,1005,570,151,107,67,574,570,1005,570,151,1001,574,-64,574,1002,574,-1,574,1001,572,1,572,1007,572,11,570,1006,570,165,101,1182,572,127,1002,574,1,0,3,574,101,1,573,573,1008,574,10,570,1005,570,189,1008,574,44,570,1006,570,158,1105,1,81,21101,340,0,1,1106,0,177,21102,1,477,1,1106,0,177,21101,514,0,1,21101,0,176,0,1105,1,579,99,21101,184,0,0,1105,1,579,4,574,104,10,99,1007,573,22,570,1006,570,165,102,1,572,1182,21102,1,375,1,21101,0,211,0,1106,0,579,21101,1182,11,1,21102,222,1,0,1105,1,979,21102,1,388,1,21102,233,1,0,1105,1,579,21101,1182,22,1,21101,0,244,0,1106,0,979,21102,1,401,1,21102,255,1,0,1105,1,579,21101,1182,33,1,21101,0,266,0,1106,0,979,21101,0,414,1,21101,277,0,0,1105,1,579,3,575,1008,575,89,570,1008,575,121,575,1,575,570,575,3,574,1008,574,10,570,1006,570,291,104,10,21101,0,1182,1,21101,313,0,0,1105,1,622,1005,575,327,1102,1,1,575,21101,327,0,0,1106,0,786,4,438,99,0,1,1,6,77,97,105,110,58,10,33,10,69,120,112,101,99,116,101,100,32,102,117,110,99,116,105,111,110,32,110,97,109,101,32,98,117,116,32,103,111,116,58,32,0,12,70,117,110,99,116,105,111,110,32,65,58,10,12,70,117,110,99,116,105,111,110,32,66,58,10,12,70,117,110,99,116,105,111,110,32,67,58,10,23,67,111,110,116,105,110,117,111,117,115,32,118,105,100,101,111,32,102,101,101,100,63,10,0,37,10,69,120,112,101,99,116,101,100,32,82,44,32,76,44,32,111,114,32,100,105,115,116,97,110,99,101,32,98,117,116,32,103,111,116,58,32,36,10,69,120,112,101,99,116,101,100,32,99,111,109,109,97,32,111,114,32,110,101,119,108,105,110,101,32,98,117,116,32,103,111,116,58,32,43,10,68,101,102,105,110,105,116,105,111,110,115,32,109,97,121,32,98,101,32,97,116,32,109,111,115,116,32,50,48,32,99,104,97,114,97,99,116,101,114,115,33,10,94,62,118,60,0,1,0,-1,-1,0,1,0,0,0,0,0,0,1,18,50,0,109,4,2101,0,-3,587,20102,1,0,-1,22101,1,-3,-3,21102,0,1,-2,2208,-2,-1,570,1005,570,617,2201,-3,-2,609,4,0,21201,-2,1,-2,1105,1,597,109,-4,2106,0,0,109,5,2102,1,-4,630,20102,1,0,-2,22101,1,-4,-4,21101,0,0,-3,2208,-3,-2,570,1005,570,781,2201,-4,-3,652,21001,0,0,-1,1208,-1,-4,570,1005,570,709,1208,-1,-5,570,1005,570,734,1207,-1,0,570,1005,570,759,1206,-1,774,1001,578,562,684,1,0,576,576,1001,578,566,692,1,0,577,577,21102,702,1,0,1106,0,786,21201,-1,-1,-1,1106,0,676,1001,578,1,578,1008,578,4,570,1006,570,724,1001,578,-4,578,21101,731,0,0,1105,1,786,1105,1,774,1001,578,-1,578,1008,578,-1,570,1006,570,749,1001,578,4,578,21101,0,756,0,1105,1,786,1105,1,774,21202,-1,-11,1,22101,1182,1,1,21101,774,0,0,1105,1,622,21201,-3,1,-3,1106,0,640,109,-5,2106,0,0,109,7,1005,575,802,20102,1,576,-6,20101,0,577,-5,1105,1,814,21101,0,0,-1,21101,0,0,-5,21101,0,0,-6,20208,-6,576,-2,208,-5,577,570,22002,570,-2,-2,21202,-5,53,-3,22201,-6,-3,-3,22101,1483,-3,-3,2101,0,-3,843,1005,0,863,21202,-2,42,-4,22101,46,-4,-4,1206,-2,924,21102,1,1,-1,1105,1,924,1205,-2,873,21102,35,1,-4,1106,0,924,1202,-3,1,878,1008,0,1,570,1006,570,916,1001,374,1,374,1201,-3,0,895,1102,2,1,0,2101,0,-3,902,1001,438,0,438,2202,-6,-5,570,1,570,374,570,1,570,438,438,1001,578,558,921,21001,0,0,-4,1006,575,959,204,-4,22101,1,-6,-6,1208,-6,53,570,1006,570,814,104,10,22101,1,-5,-5,1208,-5,65,570,1006,570,810,104,10,1206,-1,974,99,1206,-1,974,1101,0,1,575,21101,973,0,0,1105,1,786,99,109,-7,2105,1,0,109,6,21101,0,0,-4,21102,0,1,-3,203,-2,22101,1,-3,-3,21208,-2,82,-1,1205,-1,1030,21208,-2,76,-1,1205,-1,1037,21207,-2,48,-1,1205,-1,1124,22107,57,-2,-1,1205,-1,1124,21201,-2,-48,-2,1106,0,1041,21102,-4,1,-2,1106,0,1041,21102,1,-5,-2,21201,-4,1,-4,21207,-4,11,-1,1206,-1,1138,2201,-5,-4,1059,1202,-2,1,0,203,-2,22101,1,-3,-3,21207,-2,48,-1,1205,-1,1107,22107,57,-2,-1,1205,-1,1107,21201,-2,-48,-2,2201,-5,-4,1090,20102,10,0,-1,22201,-2,-1,-2,2201,-5,-4,1103,2102,1,-2,0,1106,0,1060,21208,-2,10,-1,1205,-1,1162,21208,-2,44,-1,1206,-1,1131,1106,0,989,21101,0,439,1,1106,0,1150,21101,477,0,1,1106,0,1150,21102,514,1,1,21102,1149,1,0,1105,1,579,99,21101,1157,0,0,1106,0,579,204,-2,104,10,99,21207,-3,22,-1,1206,-1,1138,2102,1,-5,1176,2101,0,-4,0,109,-6,2105,1,0,14,7,46,1,5,1,46,1,5,1,46,1,5,1,46,1,5,1,46,1,5,1,46,1,5,1,46,1,5,1,46,1,5,1,46,1,5,1,46,9,50,1,1,1,50,11,44,1,7,1,44,1,7,1,44,1,7,1,44,1,7,1,44,1,7,1,34,11,7,1,34,1,17,1,34,1,17,1,34,1,17,1,34,1,17,9,26,1,25,1,26,1,25,1,26,1,25,1,24,9,19,1,24,1,1,1,5,1,19,1,14,13,5,1,19,1,14,1,9,1,7,1,19,1,14,1,9,1,7,1,19,1,14,1,9,1,7,1,19,1,14,1,9,1,7,1,19,1,3,12,9,1,7,1,19,1,3,1,9,12,7,1,19,11,3,1,18,1,23,1,5,1,3,1,18,11,13,1,5,1,3,1,28,1,13,1,5,1,3,1,28,1,11,13,28,1,11,1,1,1,5,1,32,1,1,13,5,1,32,1,1,1,9,1,7,1,32,1,1,1,9,1,7,1,32,1,1,1,9,1,7,1,32,1,1,1,9,1,7,1,32,1,1,1,9,1,7,1,32,1,1,1,5,13,32,1,1,1,5,1,3,1,40,13,42,1,5,1,34,13,5,1,52,1,52,1,52,1,52,1,52,1,42,11,42,1,52,1,52,1,52,1,52,1,52,1,52,1,52,1,26";

    IntcodeInterpreter intcodeInterpreter = new IntcodeInterpreter();

    private Map<Field, Long> map = new HashMap<>();

    @Test
    void runPartOne() {

        Context context = Context.newContext(IntcodeParser.parseProgramm(PROGRAM));

        int x = 0;
        int y = 0;

        while (!context.isHalted()) {

            context = intcodeInterpreter.runStep(context);

            if (context.getOutput().size() > 0) {
                long value = context.getOutput().get(0);
                if (value == 35) {
                    System.out.print('#');
                    map.put(new Field(x,y), value);
                    x++;
                } else if (value == 46) {
                    System.out.print('.');
                    map.put(new Field(x,y), value);
                    x++;
                } else if (value == 10) {
                    System.out.println("");
                    x=0;
                    y++;
                } else {
                    map.put(new Field(x,y), value);
                    x++;
                }



            }
            context.resetOutputs();
        }



        int intersections = findIntersections();
        paintMap();

        assertThat(intersections, is(12512));

    }

    private int findIntersections() {
        int xMax = map.keySet().stream().mapToInt(Field::getX).max().orElse(0);
        int yMax = map.keySet().stream().mapToInt(Field::getY).max().orElse(0);

        int sum = 0;

        for(int y = 0; y <= yMax; y++) {
            for (int x = 0; x <= xMax; x++) {
                if(isIntersection(x,y)){
                    System.out.println("Intersection " + x + "/" + y);
                    map.put(new Field(x,y), (long)'O');
                    sum += x*y;
                }
            }
        }

        return sum;
    }

    private boolean isIntersection(int x, int y) {
        Long neigbourSelf = map.getOrDefault(new Field(x, y), 46L);
        Long neigbourTop = map.getOrDefault(new Field(x, y-1), 46L);
        Long neigbourBottom = map.getOrDefault(new Field(x, y+1), 46L);
        Long neigbourLeft = map.getOrDefault(new Field(x-1, y), 46L);
        Long neigbourRight = map.getOrDefault(new Field(x+1, y), 46L);

        return  neigbourSelf == 35L && neigbourTop == 35L && neigbourBottom == 35L && neigbourRight == 35L && neigbourLeft == 35L;


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
                } else if(color == 35L) {
                    System.out.print("#");
                } else if(color == 46L) {
                    System.out.print(".");
                } else {
                    System.out.print( Character.valueOf((char) color.intValue()));
                }
            }
            System.out.println(" " + y);
        }
    }
}
