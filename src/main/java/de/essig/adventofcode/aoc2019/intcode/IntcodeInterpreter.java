package de.essig.adventofcode.aoc2019.intcode;


import java.util.List;
import java.util.Map;

public class IntcodeInterpreter {


    public Context runStep(Context context) {

        Map<Long, Long> programmAsInt = context.getProgrammAsIntEnd();
        long instructionPointer = context.getInstructionPointer();

        int instruction = getOpCode(programmAsInt.get(instructionPointer));
        long relativeBase = context.getRelativeBase();

        if (instruction == 99) {
            context.setHalted(true);
        } else if (instruction == 1) { // ADD
            printCommand(programmAsInt, instructionPointer, 4);

            long value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0, relativeBase);
            long value2 = getParamAccordingToMode(programmAsInt, instructionPointer, 1, relativeBase);
            long destination = getJumpGoalAccordingToMode(programmAsInt, instructionPointer, 2, relativeBase);

            long sum = value1 + value2;

            programmAsInt.put(destination, sum);
            instructionPointer += 4;
        } else if (instruction == 2) { // MUL
            printCommand(programmAsInt, instructionPointer, 4);

            long value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0, relativeBase);
            long value2 = getParamAccordingToMode(programmAsInt, instructionPointer, 1, relativeBase);
            long destination = getJumpGoalAccordingToMode(programmAsInt, instructionPointer, 2, relativeBase);

            long sum = value1 * value2;

            programmAsInt.put(destination, sum);
            instructionPointer += 4;
        } else if (instruction == 3) { // INPUT
            printCommand(programmAsInt, instructionPointer, 2);

            long destination = getJumpGoalAccordingToMode(programmAsInt, instructionPointer, 0, relativeBase);
            long input = context.getInputs().remove(0);

            programmAsInt.put(destination, input);
            instructionPointer += 2;
        } else if (instruction == 4) { // OUTPUT
            printCommand(programmAsInt, instructionPointer, 2);

            long output = getParamAccordingToMode(programmAsInt, instructionPointer, 0, relativeBase);

            context.getOutput().add(output);
            instructionPointer += 2;
        } else if (instruction == 5) { // jump-if-true
            printCommand(programmAsInt, instructionPointer, 3);

            long value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0, relativeBase);
            long jumpGoal = getParamAccordingToMode(programmAsInt, instructionPointer, 1, relativeBase);

            if (value1 != 0) {
                instructionPointer = jumpGoal;
            } else {
                instructionPointer += 3;
            }
        } else if (instruction == 6) { // jump-if-false
            printCommand(programmAsInt, instructionPointer, 3);

            long value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0, relativeBase);
            long jumpGoal = getParamAccordingToMode(programmAsInt, instructionPointer, 1, relativeBase);

            if (value1 == 0) {
                instructionPointer = jumpGoal;
            } else {
                instructionPointer += 3;
            }
        } else if (instruction == 7) { // less-then
            printCommand(programmAsInt, instructionPointer, 4);


            long value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0, relativeBase);
            long value2 = getParamAccordingToMode(programmAsInt, instructionPointer, 1, relativeBase);
            long resultGoal = getJumpGoalAccordingToMode(programmAsInt, instructionPointer, 2, relativeBase);

            if (value1 < value2) {
                programmAsInt.put(resultGoal, 1L);
            } else {
                programmAsInt.put(resultGoal, 0L);
            }

            instructionPointer += 4;
        } else if (instruction == 8) { // equals
            printCommand(programmAsInt, instructionPointer, 4);

            long value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0, relativeBase);
            long value2 = getParamAccordingToMode(programmAsInt, instructionPointer, 1, relativeBase);
            long resultGoal = getJumpGoalAccordingToMode(programmAsInt, instructionPointer, 2, relativeBase);

            if (value1 == value2) {
                programmAsInt.put(resultGoal, 1L);
            } else {
                programmAsInt.put(resultGoal, 0L);
            }

            instructionPointer += 4;
        } else if (instruction == 9) { // set relative base
            printCommand(programmAsInt, instructionPointer, 2);
            long relativeBaseIncrement =  getParamAccordingToMode(programmAsInt, instructionPointer, 0, relativeBase);
            context.incrementRelativeBase(relativeBaseIncrement);

            instructionPointer += 2;
        } else {
            System.err.println("FATAL: Unknown Instruction " + instruction + "@" + instructionPointer);

            context.setHalted(true);
        }


        context.setProgrammAsIntEnd(programmAsInt);
        context.setInstructionPointer(instructionPointer);

        return context;
    }

    private void printCommand(Map<Long, Long> programmAsInt, long instructionPointer, int length) {

        for(int i = 0; i< length; i++) {
            //System.out.print(programmAsInt.getOrDefault(instructionPointer+i, 0L) + ",");
        }
       // System.out.println();

    }

    long getJumpGoalAccordingToMode(Map<Long, Long> programmAsInt, long instructionPointer, int param, long relativeBase) {

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

    long getParamAccordingToMode(Map<Long, Long> programmAsInt, long instructionPointer, int param, long relativeBase) {

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


    // TODO: Move Intcode Interpreter into separate classes and make extendable
}
