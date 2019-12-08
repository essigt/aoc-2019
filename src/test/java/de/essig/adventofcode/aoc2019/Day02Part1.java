package de.essig.adventofcode.aoc2019;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import static org.hamcrest.MatcherAssert.assertThat;

public class Day02Part1 {

    private String inputOriginal = "1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,1,10,19,1,6,19,23,1,10,23,27,2,27,13,31,1,31,6,35,2,6,35,39,1,39,5,43,1,6,43,47,2,6,47,51,1,51,5,55,2,55,9,59,1,6,59,63,1,9,63,67,1,67,10,71,2,9,71,75,1,6,75,79,1,5,79,83,2,83,10,87,1,87,5,91,1,91,9,95,1,6,95,99,2,99,10,103,1,103,5,107,2,107,6,111,1,111,5,115,1,9,115,119,2,119,10,123,1,6,123,127,2,13,127,131,1,131,6,135,1,135,10,139,1,13,139,143,1,143,13,147,1,5,147,151,1,151,2,155,1,155,5,0,99,2,0,14,0";
    private String input = "1,12,2,3,1,1,2,3,1,3,4,3,1,5,0,3,2,1,10,19,1,6,19,23,1,10,23,27,2,27,13,31,1,31,6,35,2,6,35,39,1,39,5,43,1,6,43,47,2,6,47,51,1,51,5,55,2,55,9,59,1,6,59,63,1,9,63,67,1,67,10,71,2,9,71,75,1,6,75,79,1,5,79,83,2,83,10,87,1,87,5,91,1,91,9,95,1,6,95,99,2,99,10,103,1,103,5,107,2,107,6,111,1,111,5,115,1,9,115,119,2,119,10,123,1,6,123,127,2,13,127,131,1,131,6,135,1,135,10,139,1,13,139,143,1,143,13,147,1,5,147,151,1,151,2,155,1,155,5,0,99,2,0,14,0";


    @Test
    void puzzle() {

        System.out.println(prettyPrint(runProgramm(input))); // Answer: 6327510
    }
    @Test
    void testSamples() {

        assertThat(prettyPrint(runProgramm("1,0,0,0,99")), Matchers.is("2,0,0,0,99"));
        assertThat(prettyPrint(runProgramm("2,3,0,3,99")), Matchers.is("2,3,0,6,99"));
        assertThat(prettyPrint(runProgramm("2,4,4,5,99,0")), Matchers.is("2,4,4,5,99,9801"));
        assertThat(prettyPrint(runProgramm("1,1,1,4,99,5,6,0,99")), Matchers.is("30,1,1,4,2,5,6,0,99"));
    }

    private String prettyPrint(List<Integer> programm) {
        return programm.stream().map(String::valueOf).collect(Collectors.joining(","));

    }

    List<Integer> runProgramm(String input) {
        List<Integer> programmAsInt = parseProgramm(input);
        boolean stop = false;
        int instructionPointer = 0;

        while(!stop) {
            int instruction = programmAsInt.get(instructionPointer);

            if(instruction == 99) {
                stop = true;
                System.out.println("Pos " + instructionPointer + ": HALT");
            } else if(instruction == 1) {
                System.out.println("Pos " + instructionPointer + ": ADD");
                int value1 = programmAsInt.get(programmAsInt.get(instructionPointer+1));
                int value2 = programmAsInt.get(programmAsInt.get(instructionPointer+2));
                int destination = programmAsInt.get(instructionPointer+3);

                int sum = value1 + value2;
                System.out.println("\t" + value1 + "+" + value2 + "=" + sum + " store at " + destination);

                programmAsInt.remove(destination);
                programmAsInt.add(destination, sum);
                instructionPointer +=4;
            } else if(instruction == 2) {
                System.out.println("Pos " + instructionPointer + ": MUL");
                int value1 = programmAsInt.get(programmAsInt.get(instructionPointer+1));
                int value2 = programmAsInt.get(programmAsInt.get(instructionPointer+2));
                int destination = programmAsInt.get(instructionPointer+3);

                int sum = value1 * value2;
                System.out.println("\t" + value1 + "*" + value2 + "=" + sum + " store at " + destination);

                programmAsInt.remove(destination);
                programmAsInt.add(destination, sum);
                instructionPointer +=4;
            } else {
                System.err.println("FATAL: Unknown Instruction " + instruction + "@"+instructionPointer);
                break;
            }
        }

        return programmAsInt;
    }

    private List<Integer> parseProgramm(String input) {
        List<Integer> programm = new ArrayList<>();

        String[] split = input.split(",");
        for(String instruction : split) {
            programm.add(Integer.valueOf(instruction));
        }

        return programm;
    }


}
