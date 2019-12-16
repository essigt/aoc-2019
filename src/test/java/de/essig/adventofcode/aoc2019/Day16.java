package de.essig.adventofcode.aoc2019;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class Day16 {

    private static final String inputString = "59791911701697178620772166487621926539855976237879300869872931303532122404711706813176657053802481833015214226705058704017099411284046473395211022546662450403964137283487707691563442026697656820695854453826690487611172860358286255850668069507687936410599520475680695180527327076479119764897119494161366645257480353063266653306023935874821274026377407051958316291995144593624792755553923648392169597897222058613725620920233283869036501950753970029182181770358827133737490530431859833065926816798051237510954742209939957376506364926219879150524606056996572743773912030397695613203835011524677640044237824961662635530619875905369208905866913334027160178";

    private static final List<Integer> basePattern = new ArrayList<>(asList(0, 1, 0, -1));

    @Test
    void runTest() {
        List<Integer> parsedInput = parseInput("98765");
        List<Integer> basePattern = new ArrayList<>(asList(1, 2, 3));


        List<Integer> integers = calculateNextPhase(parsedInput, basePattern, 1);
        System.out.println("Output 1: " + integers);
    }

    @Test
    void runTestTwo() {
        List<Integer> parsedInput = parseInput("12345678");

        List<Integer> integers = null;
        System.out.println("Input Signal: " + parsedInput);

        for(int phase =0; phase < 4; phase++) {
            List<Integer> nextPhaseInput = new ArrayList<>();

            for (int i = 1; i <= 8; i++) {
                integers = calculateNextPhase(parsedInput, basePattern, i);
                int result = onesDigit(integers.stream().mapToInt(Integer::intValue).sum());
                //System.out.println("Output " + i + ": " + integers);
                //System.out.println("Output " + i + ": " + result);
                nextPhaseInput.add(result);

            }
            parsedInput = new ArrayList<>(nextPhaseInput);
            System.out.println("After " + (phase +1) + " phase: " + parsedInput);
        }

    }

    @Test
    void runTestThree() {
        List<Integer> parsedInput = parseInput("80871224585914546619083218645595");

        List<Integer> integers = null;
        System.out.println("Input Signal: " + parsedInput);

        for(int phase=0; phase < 100; phase++) {
            List<Integer> nextPhaseInput = new ArrayList<>();

            for (int i = 1; i <= parsedInput.size(); i++) {
                integers = calculateNextPhase(parsedInput, basePattern, i);
                int result = onesDigit(integers.stream().mapToInt(Integer::intValue).sum());
                nextPhaseInput.add(result);

            }
            parsedInput = new ArrayList<>(nextPhaseInput);
            System.out.println("After " + (phase +1) + " phase: " + parsedInput);
        }

    }

    @Test
    void runProgrammPartOne() {
        List<Integer> parsedInput = parseInput(inputString);

        List<Integer> integers = null;
        System.out.println("Input Signal: " + parsedInput);

        for(int phase=0; phase < 100; phase++) {
            List<Integer> nextPhaseInput = new ArrayList<>();

            for (int i = 1; i <= parsedInput.size(); i++) {
                integers = calculateNextPhase(parsedInput, basePattern, i);
                int result = onesDigit(integers.stream().mapToInt(Integer::intValue).sum());
                nextPhaseInput.add(result);
            }
            parsedInput = new ArrayList<>(nextPhaseInput);

            if(phase == 99) {
                System.out.println("After " + (phase + 1) + " phase: " + parsedInput);
            }
        }

        // answer = 45834272

    }

    @Test
    void runProgrammPartOneOptimized() {
        int[] parsedInput = parseInputAsArray(inputString);

        int[] integers = null;
        System.out.println("Input Signal: " + parsedInput);

        for(int phase=0; phase < 100; phase++) {
            int[] nextPhaseInput = new int[parsedInput.length];


            for (int i = 1; i <= parsedInput.length; i++) {
                integers = calculateNextPhaseFromArray(parsedInput, basePattern, i);
                int result = onesDigit(sum(integers));
                nextPhaseInput[i-1] = result;
            }
            parsedInput = nextPhaseInput;

            if(phase == 99) {
                System.out.println("After " + (phase + 1) + " phase: " + parsedInput);
            }
        }

        // answer = 45834272

    }

    private int sum(int[] ints) {
        int sum = 0;
        for(int i=0; i < ints.length;i++) {
            sum += ints[i];
        }
        return sum;
    }


    @Test
    void runProgrammPartTwo() {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < 1000; i++) {
            sb.append(inputString);
        }
        List<Integer> parsedInput = null;//new ArrayList<>(parseInput(sb.toString()));

        List<Integer> integers = null;
        //System.out.println("Input Signal: " + parsedInput);

        for(int phase=0; phase < 100; phase++) {
            List<Integer> nextPhaseInput = new ArrayList<>();

            for (int i = 1; i <= parsedInput.size(); i++) {
                integers = calculateNextPhase(parsedInput, basePattern, i);
                int result = onesDigit(integers.stream().mapToInt(Integer::intValue).sum());
                nextPhaseInput.add(result);

            }
            parsedInput = new ArrayList<>(nextPhaseInput);

            /*if(phase == 99) {
                System.out.println("After " + (phase + 1) + " phase: " + parsedInput);
            } else {*/
                System.out.println("Phase " + (phase +1) + " finished");
            //}
        }

        List<Integer> collect2 = parsedInput.stream().limit(7).collect(Collectors.toList());
        System.out.println("first 8 digits: " + collect2);

        long offset = 0;
        for(int i=1; i <= 7; i++) {

            offset += parsedInput.get(i - 1) * ( Math.pow(10, 7-i));

        }


        System.out.println("Offset: " + offset);

        List<Integer> collect = parsedInput.stream().skip(offset).limit(8).collect(Collectors.toList());
        System.out.println("Result: " + collect);

        // answer = 45834272

    }

    private int onesDigit(int input) {
        return Math.abs(input % 10);
    }

    private List<Integer> calculateNextPhase(List<Integer> input, List<Integer> pattern, int patternStretch) {
        List<Integer> list = new ArrayList<>();
        List<Integer> stretchedPattern = stretchPattern(pattern, patternStretch);
        int patternPos = 1;


        for(int i=0; i < input.size(); i++) {
            int currentInt = input.get(i);
            list.add(currentInt * stretchedPattern.get(patternPos));

            patternPos = (patternPos +1 ) % stretchedPattern.size();
        }

        return list;
    }

    private int[] calculateNextPhaseFromArray(int[] input, List<Integer> pattern, int patternStretch) {
        int[] list = new int[input.length];
        List<Integer> stretchedPattern = stretchPattern(pattern, patternStretch);
        int patternPos = 1;

        for(int i=0; i < input.length; i++) {
            int currentInt = input[i];
            list[i] = (currentInt * stretchedPattern.get(patternPos));

            patternPos = (patternPos +1 ) % stretchedPattern.size();
        }

        return list;
    }

    private List<Integer> stretchPattern(List<Integer> pattern, int strech) {

        ArrayList<Integer> list = new ArrayList<>();

        for(Integer integer : pattern) {
            for(int i=0; i< strech; i++) {
                list.add(integer);
            }
        }

        return list;

    }

    private List<Integer> parseInput(String input) {
        ArrayList<Integer> list = new ArrayList<>();

        for(char c : input.toCharArray()) {
            list.add(Integer.valueOf(String.valueOf(c)));
        }

        return list;
    }

    private int[] parseInputAsArray(String input) {
        ArrayList<Integer> list = new ArrayList<>();

        for(char c : input.toCharArray()) {
            list.add(Integer.valueOf(String.valueOf(c)));
        }

        return list.stream().mapToInt(i->i).toArray();
    }
}
