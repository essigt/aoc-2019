package de.essig.adventofcode.aoc2019;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static de.essig.adventofcode.aoc2019.Day09Data.PROGRAM;
import static de.essig.adventofcode.aoc2019.Day09Data.TEST_DATA_ONE;
import static de.essig.adventofcode.aoc2019.Day09Data.TEST_DATA_THREE;
import static de.essig.adventofcode.aoc2019.Day09Data.TEST_DATA_TWO;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class Day09 {

    long relativeBase = 0;

    @Test
    void partOne() {

        ArrayList<Long> input = new ArrayList<>();
        input.add(1L);
        Long output = runProgramm(PROGRAM, input).getOutput().get(0);
        System.out.println("Output: " + output);
        assertThat(output,
           is(3533056970L));

    }

    @Test
    void partTwo() {

        ArrayList<Long> input = new ArrayList<>();
        input.add(2L);
        Long output = runProgramm(PROGRAM, input).getOutput().get(0);
        System.out.println("Output: " + output);
        assertThat(output,
                is(72852L));

    }


    @Test
    void testSamples() {


        List<Long> output = runProgramm(TEST_DATA_ONE, Collections.emptyList()).getOutput();
        System.out.println("Output:" + output);
        assertThat(output,
                contains(109L,1L,204L,-1L,1001L,100L,1L,100L,1008L,100L,16L,101L,1006L,101L,0L,99L));
        assertThat(runProgramm(TEST_DATA_TWO, Collections.emptyList()).getOutput(),
            containsInAnyOrder(34915192L * 34915192L));
        assertThat(runProgramm(TEST_DATA_THREE, Collections.emptyList()).getOutput(),
            containsInAnyOrder(1125899906842624L));
    }


    Context runProgramm(String programmCode, List<Long> inputs) {

        Map<Long, Long> programmAsInt = parseProgramm(programmCode);
        Context context = new Context(programmAsInt, inputs);

        boolean stop = false;
        long instructionPointer = 0;
        relativeBase = 0;

        while (!stop) {
            int instruction = getOpCode(programmAsInt.get(instructionPointer));

            if (instruction == 99) {
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
                System.out.println("Input Value " + input + " at " + destination);

                programmAsInt.put(destination, input);
                instructionPointer += 2;
            } else if (instruction == 4) { // OUTPUT
                printCommand(programmAsInt, instructionPointer, 2);

                long output = getParamAccordingToMode(programmAsInt, instructionPointer, 0);

                context.getOutput().add(output);

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
                System.out.println("Relativ Base=" + relativeBase);

                instructionPointer += 2;
            } else {
                System.err.println("FATAL: Unknown Instruction " + instruction + "@" + instructionPointer);

                break;
            }
        }

        context.setProgrammAsIntEnd(programmAsInt);

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
                //relativeBase += relativPart;
                System.out.println("Relative Read from " + (relativeBase + relativPart));

                return programmAsInt.getOrDefault( relativeBase + relativPart, 0L);
                //throw new IllegalArgumentException("Relative mode not implemented");
            } else {
                return programmAsInt.getOrDefault(instructionPointer + 1, 0L);
            }
        } else if (param == 1) {
            if (getSecondParamMode(programmAsInt.get(instructionPointer)) == 0) { // POSITION MODE
                return programmAsInt.getOrDefault(programmAsInt.get(instructionPointer + 2),0L );
            } else if (getSecondParamMode(programmAsInt.get(instructionPointer)) == 2) { // Relative MODE

                long relativPart = programmAsInt.getOrDefault(instructionPointer + 2, 0L);
                System.out.println("Relative Read from " + (relativeBase + relativPart));
                return programmAsInt.getOrDefault( relativeBase + relativPart, 0L);
            } else {
                return programmAsInt.getOrDefault(instructionPointer + 2, 0L);
            }
        } else if (param == 2) {
            if (getThirdParamMode(programmAsInt.get(instructionPointer)) == 0) { // POSITION MODE

                return programmAsInt.getOrDefault(programmAsInt.get(instructionPointer + 3),0L );
            } else if (getThirdParamMode(programmAsInt.get(instructionPointer)) == 2) { // Relative MODE

                long relativPart = programmAsInt.getOrDefault(instructionPointer + 3, 0L);
                System.out.println("Relative Read from " + (relativeBase + relativPart));
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

        public Context(Map<Long, Long> programmAsIntOriginal, List<Long> input) {

            this.programmAsIntOriginal = new HashMap<>(programmAsIntOriginal);
            this.input = input;
            this.output = new ArrayList<>();
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
}
