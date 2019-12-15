package de.essig.adventofcode.aoc2019;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static de.essig.adventofcode.aoc2019.Day11Data.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class Day11 {

    private long relativeBase = 0;


    private Map<Tile, Long> hull = new HashMap<>();


    private enum Direction {LEFT, UP, RIGHT, DOWN};


    @Test
    void partOne() {

        int posX = 0;
        int posY = 0;
        Direction currentDirection = Direction.UP;

        boolean halt = false;
        long instructionPointer = 0;
        Map<Long, Long> programmAsInt = parseProgramm(PROGRAM);

        while(!halt) {
            ArrayList<Long> input = new ArrayList<>();
            input.add(getColor(posX, posY));

            Context context = runProgramm(programmAsInt, instructionPointer, input);
            System.out.println("Output: " + context.getOutput());
            if(context.isHalted()) {
                break;
            }

            long color = context.getOutput().get(0);
            long direction = context.getOutput().get(1);

            // Paint
            hull.put(new Tile(posX, posY), color);

            // Turne
            if(direction == 0) { //Turn left
                if(currentDirection == Direction.UP) {
                    currentDirection = Direction.LEFT;
                } else if(currentDirection == Direction.LEFT) {
                    currentDirection = Direction.DOWN;
                } else if(currentDirection == Direction.DOWN) {
                    currentDirection = Direction.RIGHT;
                } else {
                    currentDirection = Direction.UP;
                }

            } else { //Turn right
                if(currentDirection == Direction.UP) {
                    currentDirection = Direction.RIGHT;
                } else if(currentDirection == Direction.LEFT) {
                    currentDirection = Direction.UP;
                } else if(currentDirection == Direction.DOWN) {
                    currentDirection = Direction.LEFT;
                } else {
                    currentDirection = Direction.DOWN;
                }
            }


            // Move
            if(currentDirection == Direction.UP) {
                posY++;
            } else if(currentDirection == Direction.LEFT) {
                posX--;
            } else if(currentDirection == Direction.DOWN) {
                posY--;
            } else {
                posX++;
            }

            halt = context.isHalted();
            programmAsInt = context.getProgrammAsIntEnd();
            instructionPointer = context.getInstructionPointer();
            //System.out.println("Painted at leased once: " + hull.size());
        }


        System.out.println("Painted at leased once: " + hull.size());
        assertThat(hull.size(), is(1985));
        paintHull();

    }


    @Test
    void partTwo() {

        int posX = 0;
        int posY = 0;
        Direction currentDirection = Direction.UP;

        boolean halt = false;
        long instructionPointer = 0;
        Map<Long, Long> programmAsInt = parseProgramm(PROGRAM);

        hull.put(new Tile(posX,posY), 1L);

        while(!halt) {
            ArrayList<Long> input = new ArrayList<>();
            input.add(getColor(posX, posY));

            Context context = runProgramm(programmAsInt, instructionPointer, input);

            if(context.isHalted()) {
                System.out.println("Output: " + context.getOutput());
                break;
            }

            if(context.getOutput().size() < 2) {
                System.out.println("Output: " + context.getOutput());
                System.out.println("Unexpected No Output");
                break;
            }

            long color = context.getOutput().get(0);
            long direction = context.getOutput().get(1);

            // Paint
            hull.remove(new Tile(posX,posY));
            hull.put(new Tile(posX, posY), color);

            // Turn
            if(direction == 0) { //Turn left
                if(currentDirection == Direction.UP) {
                    currentDirection = Direction.LEFT;
                } else if(currentDirection == Direction.LEFT) {
                    currentDirection = Direction.DOWN;
                } else if(currentDirection == Direction.DOWN) {
                    currentDirection = Direction.RIGHT;
                } else {
                    currentDirection = Direction.UP;
                }

            } else { //Turn right
                if(currentDirection == Direction.UP) {
                    currentDirection = Direction.RIGHT;
                } else if(currentDirection == Direction.LEFT) {
                    currentDirection = Direction.UP;
                } else if(currentDirection == Direction.DOWN) {
                    currentDirection = Direction.LEFT;
                } else {
                    currentDirection = Direction.DOWN;
                }
            }


            // Move
            if(currentDirection == Direction.UP) {
                posY++;
            } else if(currentDirection == Direction.LEFT) {
                posX--;
            } else if(currentDirection == Direction.DOWN) {
                posY--;
            } else {
                posX++;
            }

            halt = context.isHalted();
            programmAsInt = context.getProgrammAsIntEnd();
            instructionPointer = context.getInstructionPointer();

        }


        paintHull(); // Answer = BLCZCJLZ
    }


    private void paintHull() {
        int xMin = hull.keySet().stream().mapToInt(Tile::getX).min().orElse(0);
        int xMax = hull.keySet().stream().mapToInt(Tile::getX).max().orElse(0);

        int yMin = hull.keySet().stream().mapToInt(Tile::getY).min().orElse(0);
        int yMax = hull.keySet().stream().mapToInt(Tile::getY).max().orElse(0);


        for(int y = yMax; y >= yMin; y--) {
            for(int x = xMin; x <= xMax; x++) {
                System.out.print(getColor(x,y) == 1 ? "X" : " ");
            }
            System.out.println();
        }
    }

    private Long getColor(int x, int y) {
        return hull.getOrDefault(new Tile(x,y), 0L);
    }

    private Context runProgramm(Map<Long, Long> programmAsInt, long instructionPointer, List<Long> inputs) {
        Context context = new Context(programmAsInt, inputs);

        boolean stop = false;


        while (!stop) {
            int instruction = getOpCode(programmAsInt.get(instructionPointer));

            if (instruction == 99) {
                context.setHalted(true);
                stop = true;
            } else if (instruction == 1) { // ADD
                printCommand(programmAsInt, instructionPointer, 4);

                long value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                long value2 = getParamAccordingToMode(programmAsInt, instructionPointer, 1);
                long destination = getJumpGoalAccordingToMode(programmAsInt, instructionPointer, 2);

                long sum = value1 + value2;

                programmAsInt.put(destination, sum);
                instructionPointer += 4;
            } else if (instruction == 2) { // MUL
                printCommand(programmAsInt, instructionPointer, 4);

                long value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                long value2 = getParamAccordingToMode(programmAsInt, instructionPointer, 1);
                long destination = getJumpGoalAccordingToMode(programmAsInt, instructionPointer, 2);

                long sum = value1 * value2;

                programmAsInt.put(destination, sum);
                instructionPointer += 4;
            } else if (instruction == 3) { // INPUT
                printCommand(programmAsInt, instructionPointer, 2);

                long destination = getJumpGoalAccordingToMode(programmAsInt, instructionPointer, 0);
                long input = inputs.remove(0);
                //System.out.println("Input Value " + input + " at " + destination);

                programmAsInt.put(destination, input);
                instructionPointer += 2;
            } else if (instruction == 4) { // OUTPUT
                printCommand(programmAsInt, instructionPointer, 2);

                long output = getParamAccordingToMode(programmAsInt, instructionPointer, 0);

                context.getOutput().add(output);

                // Finish when two outputs are collected
                if(context.getOutput().size() == 2) {
                    stop=true;
                }

                instructionPointer += 2;
            } else if (instruction == 5) { // jump-if-true
                printCommand(programmAsInt, instructionPointer, 3);

                long value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                long jumpGoal = getParamAccordingToMode(programmAsInt, instructionPointer, 1);

                if (value1 != 0) {
                    instructionPointer = jumpGoal;
                } else {
                    instructionPointer += 3;
                }
            } else if (instruction == 6) { // jump-if-false
                printCommand(programmAsInt, instructionPointer, 3);

                long value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                long jumpGoal = getParamAccordingToMode(programmAsInt, instructionPointer, 1);

                if (value1 == 0) {
                    instructionPointer = jumpGoal;
                } else {
                    instructionPointer += 3;
                }
            } else if (instruction == 7) { // less-then
                printCommand(programmAsInt, instructionPointer, 4);

                long value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                long value2 = getParamAccordingToMode(programmAsInt, instructionPointer, 1);
                long resultGoal = getJumpGoalAccordingToMode(programmAsInt, instructionPointer, 2);

                if (value1 < value2) {
                    programmAsInt.put(resultGoal, 1L);
                } else {
                    programmAsInt.put(resultGoal, 0L);
                }

                instructionPointer += 4;
            } else if (instruction == 8) { // equals
                printCommand(programmAsInt, instructionPointer, 4);

                long value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                long value2 = getParamAccordingToMode(programmAsInt, instructionPointer, 1);
                long resultGoal = getJumpGoalAccordingToMode(programmAsInt, instructionPointer, 2);

                if (value1 == value2) {
                    programmAsInt.put(resultGoal, 1L);
                } else {
                    programmAsInt.put(resultGoal, 0L);
                }

                instructionPointer += 4;
            } else if (instruction == 9) { // set relative base
                printCommand(programmAsInt, instructionPointer, 2);
                relativeBase += getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                //System.out.println("Relativ Base=" + relativeBase);

                instructionPointer += 2;
            } else {
                System.err.println("FATAL: Unknown Instruction " + instruction + "@" + instructionPointer);

                break;
            }
        }

        context.setProgrammAsIntEnd(programmAsInt);
        context.setInstructionPointer(instructionPointer);
        return context;
    }

    private void printCommand(Map<Long, Long> programmAsInt, long instructionPointer, int length) {

        for(int i = 0; i< length; i++) {
            System.out.print(programmAsInt.getOrDefault(instructionPointer+i, 0L) + ",");
        }
        System.out.println();

    }

    long getJumpGoalAccordingToMode(Map<Long, Long> programmAsInt, long instructionPointer, int param) {

        if (param == 0) {
            if (getFirstParamMode(programmAsInt.get(instructionPointer)) == 2) { // Relative MODE
                long relativPart = programmAsInt.getOrDefault(instructionPointer + 1, 0L);

                return relativeBase + relativPart;
                //throw new IllegalArgumentException("Relative mode not implemented");
            } else {
                return programmAsInt.get(instructionPointer + 1 );
            }
        } else if (param == 1) {
            if (getSecondParamMode(programmAsInt.get(instructionPointer)) == 2) { // Relative MODE
                long relativPart = programmAsInt.getOrDefault(instructionPointer + 2, 0L);

                return relativeBase + relativPart;
            } else {
                return programmAsInt.get(instructionPointer + 2 );
            }
        } else if (param == 2) {
            if (getThirdParamMode(programmAsInt.get(instructionPointer)) == 2) { // Relative MODE
                long relativPart = programmAsInt.getOrDefault(instructionPointer + 3, 0L);

                return relativeBase + relativPart;
            } else {
                return programmAsInt.get(instructionPointer + 3 );
            }
        } else {
            throw new IllegalArgumentException("Not implemented");
        }
    }

    long getParamAccordingToMode(Map<Long, Long> programmAsInt, long instructionPointer, int param) {

        if (param == 0) {
            if (getFirstParamMode(programmAsInt.get(instructionPointer)) == 0) { // POSITION MODE
                return programmAsInt.getOrDefault(programmAsInt.get(instructionPointer + 1), 0L);
            } else if (getFirstParamMode(programmAsInt.get(instructionPointer)) == 2) { // Relative MODE
                long relativPart = programmAsInt.getOrDefault(instructionPointer + 1,0L);

                return programmAsInt.getOrDefault( relativeBase + relativPart, 0L);
            } else {
                return programmAsInt.getOrDefault(instructionPointer + 1, 0L);
            }
        } else if (param == 1) {
            if (getSecondParamMode(programmAsInt.get(instructionPointer)) == 0) { // POSITION MODE
                return programmAsInt.getOrDefault(programmAsInt.get(instructionPointer + 2),0L );
            } else if (getSecondParamMode(programmAsInt.get(instructionPointer)) == 2) { // Relative MODE

                long relativPart = programmAsInt.getOrDefault(instructionPointer + 2, 0L);
                return programmAsInt.getOrDefault( relativeBase + relativPart, 0L);
            } else {
                return programmAsInt.getOrDefault(instructionPointer + 2, 0L);
            }
        } else if (param == 2) {
            if (getThirdParamMode(programmAsInt.get(instructionPointer)) == 0) { // POSITION MODE

                return programmAsInt.getOrDefault(programmAsInt.get(instructionPointer + 3),0L );
            } else if (getThirdParamMode(programmAsInt.get(instructionPointer)) == 2) { // Relative MODE

                long relativPart = programmAsInt.getOrDefault(instructionPointer + 3, 0L);
                return programmAsInt.getOrDefault( relativeBase + relativPart, 0L);
            } else {
                return programmAsInt.getOrDefault(instructionPointer + 3, 0L);
            }
        } else {
            throw new IllegalArgumentException("Not implemented");
        }
    }

    @Test
    void testOps() {

        assertThat(getOpCode(10002), is(2));
        assertThat(getFirstParamMode(11022), is(0));
        assertThat(getFirstParamMode(20122), is(1));
        assertThat(getSecondParamMode(21022), is(1));
        assertThat(getSecondParamMode(10122), is(0));
        assertThat(getThirdParamMode(11022), is(1));
        assertThat(getThirdParamMode(1122), is(0));
        assertThat(getFirstParamMode(21202), is(2));
        assertThat(getSecondParamMode(2022), is(2));
        assertThat(getThirdParamMode(21122), is(2));

    }

    int getOpCode(long instruction) {

        return (int) instruction % 100;
    }


    int getFirstParamMode(long instruction) {

        return (int) (instruction / 100L) % 10;
    }


    int getSecondParamMode(long instruction) {

        return (int) (instruction / 1000L) % 10;
    }


    int getThirdParamMode(long instruction) {

        return (int)(instruction / 10000L) % 10;
    }


    private Map<Long, Long> parseProgramm(String input) {

        Map<Long, Long> programm = new HashMap<>();

        String[] split = input.split(",");

        long pos = 0;

        for (String instruction : split) {
            programm.put(pos, Long.valueOf(instruction));
            pos++;
        }

        return programm;
    }


    private String prettyPrint(Context context) {

        return context.getProgrammAsIntEnd().values().stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    private class Context {

        Map<Long, Long> programmAsIntOriginal;
        Map<Long, Long> programmAsIntEnd;
        List<Long> input;
        List<Long> output;
        private boolean halted;
        private long instructionPointer;

        public Context(Map<Long, Long> programmAsIntOriginal, List<Long> input) {

            this.programmAsIntOriginal = new HashMap<>(programmAsIntOriginal);
            this.input = input;
            this.output = new ArrayList<>();
        }

        public boolean isHalted() {
            return halted;
        }

        public void setHalted(boolean halted) {
            this.halted = halted;
        }

        public long getInstructionPointer() {
            return instructionPointer;
        }

        public void setInstructionPointer(long instructionPointer) {
            this.instructionPointer = instructionPointer;
        }

        public Map<Long, Long> getProgrammAsIntEnd() {

            return programmAsIntEnd;
        }


        public void setProgrammAsIntEnd(Map<Long, Long> programmAsIntEnd) {

            this.programmAsIntEnd = new HashMap<>(programmAsIntEnd);
        }


        public Map<Long, Long> getProgrammAsIntOriginal() {

            return programmAsIntOriginal;
        }


        public List<Long> getInputs() {

            return input;
        }


        public List<Long> getOutput() {

            return output;
        }
    }

    private static class Tile{
        private int x;
        private int y;

        public Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tile tile = (Tile) o;
            return x == tile.x &&
                    y == tile.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
