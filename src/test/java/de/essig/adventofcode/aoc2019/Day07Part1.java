package de.essig.adventofcode.aoc2019;

import de.essig.adventofcode.aoc2019.amplifier.AmplifierConfigurationFinder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static de.essig.adventofcode.aoc2019.Day07Data.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

public class Day07Part1 {



    @Test
    void testPermutations() {

        int[] elements = new int[]{0,1,2,3,4};
        int n = 5;
        int[] indexes = new int[n];
        for (int i = 0; i < n; i++) {
            indexes[i] = 0;
        }


        List<Integer> maxPermutation = emptyList();
        int maxResult = 0;

        int i = 0;
        while (i < 5) {
            if (indexes[i] < i) {
                swap(elements, i % 2 == 0 ?  0: indexes[i], i);

                List<Integer> phases = Arrays.stream(elements).boxed().collect(Collectors.toList());
                int result = testAmplifierConfiguraiton(PROGRAM, phases);
                if(result > maxResult) {
                    maxResult = result;
                    maxPermutation = phases;
                }

                indexes[i]++;
                i = 0;
            }
            else {
                indexes[i] = 0;
                i++;
            }
        }

        System.out.println("MaxResult " + maxResult);
        System.out.println("Max Permutation: " + maxPermutation.toString());


    }

    private void swap(int[] input, int a, int b) {
        int tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }


    @Test
    void testSamples() {

        assertThat(testAmplifierConfiguraiton(TEST_DATA_ONE, asList(4, 3, 2, 1, 0)), is(43210));
        assertThat(testAmplifierConfiguraiton(TEST_DATA_TWO, asList(0,1,2,3,4)), is(54321));
        assertThat(testAmplifierConfiguraiton(TEST_DATA_THREE, asList(1, 0,4,3,2)), is(65210));
    }


    public int testAmplifierConfiguraiton(String programm, List<Integer> ampPhases) {


        Context firstAmp = runProgramm(programm, new ArrayList<>(asList(ampPhases.get(0), 0)));
        //System.out.println("First Amp Output: " + firstAmp.getOutput());

        Context secondAmp = runProgramm(programm, new ArrayList<>(asList(ampPhases.get(1), firstAmp.getOutput().get(0))));
        //System.out.println("Second Amp Output: " + secondAmp.getOutput());

        Context thirdAmp = runProgramm(programm, new ArrayList<>(asList(ampPhases.get(2), secondAmp.getOutput().get(0))));
        //System.out.println("Third Amp Output: " + thirdAmp.getOutput());

        Context fourthAmp = runProgramm(programm, new ArrayList<>(asList(ampPhases.get(3), thirdAmp.getOutput().get(0))));
        //System.out.println("Fourth Amp Output: " + fourthAmp.getOutput());

        Context fifthAmp = runProgramm(programm, new ArrayList<>(asList(ampPhases.get(4), fourthAmp.getOutput().get(0))));
        //System.out.println("Fourth Amp Output: " + fifthAmp.getOutput());

        return fifthAmp.getOutput().get(0);
    }

    Context runProgramm(String programmCode, List<Integer> inputs) {

        List<Integer> programmAsInt = parseProgramm(programmCode);
        Context context = new Context(programmAsInt, inputs);

        boolean stop = false;
        int instructionPointer = 0;

        while (!stop) {
            int instruction = getOpCode(programmAsInt.get(instructionPointer));

            if (instruction == 99) {
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


    int getThirdParamMode(int instruction) {

        return (instruction / 10000) % 10;
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

    private class Context {

        List<Integer> programmAsIntOriginal;
        List<Integer> programmAsIntEnd;
        List<Integer> input;
        List<Integer> output;

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
    }
}
