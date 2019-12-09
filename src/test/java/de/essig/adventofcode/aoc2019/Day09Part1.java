package de.essig.adventofcode.aoc2019;

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

import static org.hamcrest.Matchers.containsInAnyOrder;


public class Day09Part1 {

    @Test
    void runCompetition() {

        assertThat(runProgramm(PROGRAM, Collections.emptyList()).getOutput(),
            containsInAnyOrder(34915192L * 34915192L));
    }


    @Test
    void testSamples() {

        assertThat(runProgramm(TEST_DATA_ONE, Collections.emptyList()).getOutput(),
            containsInAnyOrder(34915192L * 34915192L));

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
        long relativeBase = 0;

        while (!stop) {
            int instruction = getOpCode(programmAsInt.get(instructionPointer));

            if (instruction == 99) {
                stop = true;
            } else if (instruction == 1) { // ADD

                long value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                long value2 = getParamAccordingToMode(programmAsInt, instructionPointer, 1);
                long destination = programmAsInt.get(instructionPointer + 3);

                long sum = value1 + value2;

                programmAsInt.put(destination, sum);
                instructionPointer += 4;
            } else if (instruction == 2) { // MUL

                long value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                long value2 = getParamAccordingToMode(programmAsInt, instructionPointer, 1);
                long destination = programmAsInt.get(instructionPointer + 3);

                long sum = value1 * value2;

                programmAsInt.put(destination, sum);
                instructionPointer += 4;
            } else if (instruction == 3) { // INPUT

                long destination = programmAsInt.get(instructionPointer + 1);
                long input = inputs.remove(0);

                programmAsInt.put(destination, input);
                instructionPointer += 2;
            } else if (instruction == 4) { // OUTPUT

                long output = getParamAccordingToMode(programmAsInt, instructionPointer, 0);

                context.getOutput().add(output);

                instructionPointer += 2;
            } else if (instruction == 5) { // jump-if-true

                long value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                long jumpGoal = getParamAccordingToMode(programmAsInt, instructionPointer, 1);

                if (value1 != 0) {
                    instructionPointer = jumpGoal;
                } else {
                    instructionPointer += 3;
                }
            } else if (instruction == 6) { // jump-if-false

                long value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                long jumpGoal = getParamAccordingToMode(programmAsInt, instructionPointer, 1);

                if (value1 == 0) {
                    instructionPointer = jumpGoal;
                } else {
                    instructionPointer += 3;
                }
            } else if (instruction == 7) { // less-then

                long value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                long value2 = getParamAccordingToMode(programmAsInt, instructionPointer, 1);
                long resultGoal = programmAsInt.get(instructionPointer + 3); // getParamAccordingToMode(programmAsInt, instructionPointer, 2);

                if (value1 < value2) {
                    programmAsInt.put(resultGoal, 1L);
                } else {
                    programmAsInt.put(resultGoal, 0L);
                }

                instructionPointer += 4;
            } else if (instruction == 8) { // equals

                long value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                long value2 = getParamAccordingToMode(programmAsInt, instructionPointer, 1);
                long resultGoal = programmAsInt.get(instructionPointer + 3); // getParamAccordingToMode(programmAsInt, instructionPointer, 2);

                if (value1 == value2) {
                    programmAsInt.put(resultGoal, 1L);
                } else {
                    programmAsInt.put(resultGoal, 0L);
                }

                instructionPointer += 4;
            } else if (instruction == 9) { // set relative base

                relativeBase = programmAsInt.get(instructionPointer + 1);
                instructionPointer += 2;
            } else {
                System.err.println("FATAL: Unknown Instruction " + instruction + "@" + instructionPointer);

                break;
            }
        }

        context.setProgrammAsIntEnd(programmAsInt);

        return context;
    }


    long getParamAccordingToMode(Map<Long, Long> programmAsInt, long instructionPointer, int param) {

        if (param == 0) {
            if (getFirstParamMode(programmAsInt.get(instructionPointer)) == 0) { // POSITION MODE
                return programmAsInt.get(programmAsInt.get(instructionPointer + 1));
            } else if (getFirstParamMode(programmAsInt.get(instructionPointer)) == 1) { // Relative MODE
                throw new IllegalArgumentException("Relative mode not implemented");
            } else {
                return programmAsInt.get(instructionPointer + 1);
            }
        } else if (param == 1) {
            if (getSecondParamMode(programmAsInt.get(instructionPointer)) == 0) { // POSITION MODE
                return programmAsInt.get(programmAsInt.get(instructionPointer + 2));
            } else if (getFirstParamMode(programmAsInt.get(instructionPointer)) == 1) { // Relative MODE
                throw new IllegalArgumentException("Relative mode not implemented");
            } else {
                return programmAsInt.get(instructionPointer + 2);
            }
        } else {
            throw new IllegalArgumentException("Not implemented");
        }
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


    int getThirdParamMode(int instruction) {

        return (instruction / 10000) % 10;
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
