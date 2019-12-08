package de.essig.adventofcode.aoc2019;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import static de.essig.adventofcode.aoc2019.Day07Data.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class Day07Part2 {



    @Test
    void testPermutations() {

        OptionalInt max = Permutations.of(asList(5, 6, 7, 8, 9)).mapToInt(per ->
                testAmplifierConfiguraiton(PROGRAM, per.collect(toList()))
        ).max();

        int maxResult = max.getAsInt();
        System.out.println("MaxResult " + maxResult);


        assertThat(maxResult, is(14897241));


    }

    @Test
    void testSamples() {

        assertThat(testAmplifierConfiguraiton(TEST_DATA_FOUR, asList(9,8,7,6,5)), is(139629729));
        assertThat(testAmplifierConfiguraiton(TEST_DATA_FIVE, asList(9,7,8,5,6)), is(18216));
    }


    public int testAmplifierConfiguraiton(String programm, List<Integer> ampPhases) {


        Context firstAmp = runProgramm(programm, 0, new ArrayList<>(asList(ampPhases.get(0), 0)));

        Context secondAmp = runProgramm(programm, 0,new ArrayList<>(asList(ampPhases.get(1), firstAmp.getOutput().get(0))));

        Context thirdAmp = runProgramm(programm,0, new ArrayList<>(asList(ampPhases.get(2), secondAmp.getOutput().get(0))));

        Context fourthAmp = runProgramm(programm,0, new ArrayList<>(asList(ampPhases.get(3), thirdAmp.getOutput().get(0))));

        Context fifthAmp = runProgramm(programm,0, new ArrayList<>(asList(ampPhases.get(4), fourthAmp.getOutput().get(0))));

        boolean halted = false;
        int lastThrustersOutput = 0;

        while(!halted) {

            firstAmp = runProgramm( prettyPrint(firstAmp), firstAmp.getInstructionPointer(), fifthAmp.getOutput());

            secondAmp = runProgramm(prettyPrint(secondAmp), secondAmp.getInstructionPointer(),  firstAmp.getOutput());

            thirdAmp = runProgramm(prettyPrint(thirdAmp), thirdAmp.getInstructionPointer(), secondAmp.getOutput());

            fourthAmp = runProgramm(prettyPrint(fourthAmp), fourthAmp.getInstructionPointer(),thirdAmp.getOutput());

            fifthAmp = runProgramm(prettyPrint(fifthAmp), fifthAmp.getInstructionPointer(), fourthAmp.getOutput());

            halted = fifthAmp.isHalted();

            if(!halted) {
                lastThrustersOutput = fifthAmp.getOutput().get(0);
            }
        }


        return lastThrustersOutput;
    }

    Context runProgramm(String programmCode, int instructionPointer, List<Integer> inputs) {

        List<Integer> programmAsInt = parseProgramm(programmCode);
        Context context = new Context(programmAsInt, inputs);

        boolean stop = false;

        while (!stop) {
            int instruction = getOpCode(programmAsInt.get(instructionPointer));

            if (instruction == 99) {
                context.setHalted(true);
                stop = true;
            } else if (instruction == 1) { // ADD


                int value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                int value2 = getParamAccordingToMode(programmAsInt, instructionPointer, 1);
                int destination = programmAsInt.get(instructionPointer + 3);

                int sum = value1 + value2;

                programmAsInt.remove(destination);
                programmAsInt.add(destination, sum);
                instructionPointer += 4;
            } else if (instruction == 2) { // MUL

                int value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                int value2 = getParamAccordingToMode(programmAsInt, instructionPointer, 1);
                int destination = programmAsInt.get(instructionPointer + 3);

                int sum = value1 * value2;

                programmAsInt.remove(destination);
                programmAsInt.add(destination, sum);
                instructionPointer += 4;
            } else if (instruction == 3) { // INPUT

                int destination = programmAsInt.get(instructionPointer + 1);
                int input = inputs.remove(0);

                programmAsInt.remove(destination);
                programmAsInt.add(destination, input);
                instructionPointer += 2;
            } else if (instruction == 4) { // OUTPUT

                int output = getParamAccordingToMode(programmAsInt, instructionPointer, 0);

                context.getOutput().add(output);
                stop = true; // Modification for Day07 Part 02 -> end run when output

                instructionPointer += 2;
            } else if (instruction == 5) { // jump-if-true

                int value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                int jumpGoal = getParamAccordingToMode(programmAsInt, instructionPointer, 1);

                if (value1 != 0) {
                    instructionPointer = jumpGoal;
                } else {
                    instructionPointer += 3;
                }
            } else if (instruction == 6) { // jump-if-false

                int value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                int jumpGoal = getParamAccordingToMode(programmAsInt, instructionPointer, 1);

                if (value1 == 0) {
                    instructionPointer = jumpGoal;
                } else {
                    instructionPointer += 3;
                }
            } else if (instruction == 7) { // less-then

                int value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                int value2 = getParamAccordingToMode(programmAsInt, instructionPointer, 1);
                int resultGoal = programmAsInt.get(instructionPointer + 3); // getParamAccordingToMode(programmAsInt, instructionPointer, 2);

                if (value1 < value2) {
                    programmAsInt.remove(resultGoal);
                    programmAsInt.add(resultGoal, 1);
                } else {
                    programmAsInt.remove(resultGoal);
                    programmAsInt.add(resultGoal, 0);
                }

                instructionPointer += 4;
            } else if (instruction == 8) { // equals

                int value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                int value2 = getParamAccordingToMode(programmAsInt, instructionPointer, 1);
                int resultGoal = programmAsInt.get(instructionPointer + 3); // getParamAccordingToMode(programmAsInt, instructionPointer, 2);


                if (value1 == value2) {
                    programmAsInt.remove(resultGoal);
                    programmAsInt.add(resultGoal, 1);
                } else {
                    programmAsInt.remove(resultGoal);
                    programmAsInt.add(resultGoal, 0);
                }

                instructionPointer += 4;
            } else {
                System.err.println("FATAL: Unknown Instruction " + instruction + "@" + instructionPointer);

                break;
            }
        }

        context.setInstructionPointer(instructionPointer);
        context.setProgrammAsIntEnd(programmAsInt);

        return context;
    }


    int getParamAccordingToMode(List<Integer> programmAsInt, int instructionPointer, int param) {

        if (param == 0) {
            if (getFirstParamMode(programmAsInt.get(instructionPointer)) == 0) { // POSITION MODE
                return programmAsInt.get(programmAsInt.get(instructionPointer + 1));
            } else {
                return programmAsInt.get(instructionPointer + 1);
            }
        } else if (param == 1) {
            if (getSecondParamMode(programmAsInt.get(instructionPointer)) == 0) { // POSITION MODE
                return programmAsInt.get(programmAsInt.get(instructionPointer + 2));
            } else {
                return programmAsInt.get(instructionPointer + 2);
            }
        } else {
            throw new IllegalArgumentException("Not implemented");
        }
    }


    int getOpCode(int instruction) {

        return instruction % 100;
    }


    int getFirstParamMode(int instruction) {

        return (instruction / 100) % 10;
    }


    int getSecondParamMode(int instruction) {

        return (instruction / 1000) % 10;
    }


    private List<Integer> parseProgramm(String input) {

        List<Integer> programm = new ArrayList<>();

        String[] split = input.split(",");

        for (String instruction : split) {
            programm.add(Integer.valueOf(instruction));
        }

        return programm;
    }


    private String prettyPrint(Context context) {

        return context.getProgrammAsIntEnd().stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    private static class Context {

        private List<Integer> programmAsIntOriginal;
        private List<Integer> programmAsIntEnd;
        private List<Integer> input;
        private List<Integer> output;
        private boolean halted = false;
        private int instructionPointer;

        public Context(List<Integer> programmAsIntOriginal, List<Integer> input) {

            this.programmAsIntOriginal = new ArrayList<>(programmAsIntOriginal);
            this.input = input;
            this.output = new ArrayList<>();
        }

        public List<Integer> getProgrammAsIntEnd() {

            return programmAsIntEnd;
        }


        public void setProgrammAsIntEnd(List<Integer> programmAsIntEnd) {

            this.programmAsIntEnd = new ArrayList<>(programmAsIntEnd);
        }


        public List<Integer> getProgrammAsIntOriginal() {

            return programmAsIntOriginal;
        }


        public List<Integer> getInputs() {

            return input;
        }


        public List<Integer> getOutput() {

            return output;
        }

        public void setHalted(boolean halted) {
            this.halted = halted;
        }

        public boolean isHalted() {
            return halted;
        }

        public void setInstructionPointer(int instructionPointer) {
            this.instructionPointer = instructionPointer;
        }

        public int getInstructionPointer() {
            return instructionPointer;
        }
    }
}
