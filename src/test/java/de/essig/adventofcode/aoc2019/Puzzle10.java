package de.essig.adventofcode.aoc2019;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;


/**
 * --- Day 5: Sunny with a Chance of Asteroids ---
 *
 * <p>You're starting to sweat as the ship makes its way toward Mercury. The Elves suggest that you get the air
 * conditioner working by upgrading your ship computer to support the Thermal Environment Supervision Terminal.</p>
 *
 * <p>The Thermal Environment Supervision Terminal (TEST) starts by running a diagnostic program (your puzzle input).
 * The TEST diagnostic program will run on your existing Intcode computer after a few modifications:</p>
 *
 * <p>First, you'll need to add two new instructions:</p>
 *
 * <p>Opcode 3 takes a single integer as input and saves it to the position given by its only parameter. For example,
 * the instruction 3,50 would take an input value and store it at address 50. Opcode 4 outputs the value of its only
 * parameter. For example, the instruction 4,50 would output the value at address 50.</p>
 *
 * <p>Programs that use these instructions will come with documentation that explains what should be connected to the
 * input and output. The program 3,0,4,0,99 outputs whatever it gets as input, then halts.</p>
 *
 * <p>Second, you'll need to add support for parameter modes:</p>
 *
 * <p>Each parameter of an instruction is handled based on its parameter mode. Right now, your ship computer already
 * understands parameter mode 0, position mode, which causes the parameter to be interpreted as a position - if the
 * parameter is 50, its value is the value stored at address 50 in memory. Until now, all parameters have been in
 * position mode.</p>
 *
 * <p>Now, your ship computer will also need to handle parameters in mode 1, immediate mode. In immediate mode, a
 * parameter is interpreted as a value - if the parameter is 50, its value is simply 50.</p>
 *
 * <p>Parameter modes are stored in the same value as the instruction's opcode. The opcode is a two-digit number based
 * only on the ones and tens digit of the value, that is, the opcode is the rightmost two digits of the first value in
 * an instruction. Parameter modes are single digits, one per parameter, read right-to-left from the opcode: the first
 * parameter's mode is in the hundreds digit, the second parameter's mode is in the thousands digit, the third
 * parameter's mode is in the ten-thousands digit, and so on. Any missing modes are 0.</p>
 *
 * <p>For example, consider the program 1002,4,3,4,33.</p>
 *
 * <p>The first instruction, 1002,4,3,4, is a multiply instruction - the rightmost two digits of the first value, 02,
 * indicate opcode 2, multiplication. Then, going right to left, the parameter modes are 0 (hundreds digit), 1
 * (thousands digit), and 0 (ten-thousands digit, not present and therefore zero):</p>
 *
 * <p>ABCDE 1002</p>
 *
 * <p>DE - two-digit opcode, 02 == opcode 2 C - mode of 1st parameter, 0 == position mode B - mode of 2nd parameter, 1
 * == immediate mode A - mode of 3rd parameter, 0 == position mode, omitted due to being a leading zero</p>
 *
 * <p>This instruction multiplies its first two parameters. The first parameter, 4 in position mode, works like it did
 * before - its value is the value stored at address 4 (33). The second parameter, 3 in immediate mode, simply has
 * value 3. The result of this operation, 33 * 3 = 99, is written according to the third parameter, 4 in position mode,
 * which also works like it did before - 99 is written to address 4.</p>
 *
 * <p>Parameters that an instruction writes to will never be in immediate mode.</p>
 *
 * <p>Finally, some notes:</p>
 *
 * <p>It is important to remember that the instruction pointer should increase by the number of values in the
 * instruction after the instruction finishes. Because of the new instructions, this amount is no longer always 4.
 * Integers can be negative: 1101,100,-1,4,0 is a valid program (find 100 + -1, store the result in position 4).</p>
 *
 * <p>The TEST diagnostic program will start by requesting from the user the ID of the system to test by running an
 * input instruction - provide it 1, the ID for the ship's air conditioner unit.</p>
 *
 * <p>It will then perform a series of diagnostic tests confirming that various parts of the Intcode computer, like
 * parameter modes, function correctly. For each test, it will run an output instruction indicating how far the result
 * of the test was from the expected value, where 0 means the test was successful. Non-zero outputs mean that a
 * function is not working correctly; check the instructions that were run before the output instruction to see which
 * one failed.</p>
 *
 * <p>Finally, the program will output a diagnostic code and immediately halt. This final output isn't an error; an
 * output followed immediately by a halt means the program finished. If all outputs were zero except the diagnostic
 * code, the diagnostic program ran successfully.</p>
 *
 * <p>After providing 1 to the only input instruction and passing all the tests, what diagnostic code does the program
 * produce?</p>
 *
 * <p>--- Part Two ---</p>
 *
 * <p>The air conditioner comes online! Its cold air feels good for a while, but then the TEST alarms start to go off.
 * Since the air conditioner can't vent its heat anywhere but back into the spacecraft, it's actually making the air
 * inside the ship warmer.</p>
 *
 * <p>Instead, you'll need to use the TEST to extend the thermal radiators. Fortunately, the diagnostic program (your
 * puzzle input) is already equipped for this. Unfortunately, your Intcode computer is not.</p>
 *
 * <p>Your computer is only missing a few opcodes:</p>
 *
 * <p>Opcode 5 is jump-if-true: if the first parameter is non-zero, it sets the instruction pointer to the value from
 * the second parameter. Otherwise, it does nothing. Opcode 6 is jump-if-false: if the first parameter is zero, it sets
 * the instruction pointer to the value from the second parameter. Otherwise, it does nothing. Opcode 7 is less than:
 * if the first parameter is less than the second parameter, it stores 1 in the position given by the third parameter.
 * Otherwise, it stores 0. Opcode 8 is equals: if the first parameter is equal to the second parameter, it stores 1 in
 * the position given by the third parameter. Otherwise, it stores 0.</p>
 *
 * <p>Like all instructions, these instructions need to support parameter modes as described above.</p>
 *
 * <p>Normally, after an instruction is finished, the instruction pointer increases by the number of values in that
 * instruction. However, if the instruction modifies the instruction pointer, that value is used and the instruction
 * pointer is not automatically increased.</p>
 *
 * <p>For example, here are several programs that take one input, compare it to the value 8, and then produce one
 * output:</p>
 *
 * <p>3,9,8,9,10,9,4,9,99,-1,8 - Using position mode, consider whether the input is equal to 8; output 1 (if it is) or
 * 0 (if it is not). 3,9,7,9,10,9,4,9,99,-1,8 - Using position mode, consider whether the input is less than 8; output
 * 1 (if it is) or 0 (if it is not). 3,3,1108,-1,8,3,4,3,99 - Using immediate mode, consider whether the input is equal
 * to 8; output 1 (if it is) or 0 (if it is not). 3,3,1107,-1,8,3,4,3,99 - Using immediate mode, consider whether the
 * input is less than 8; output 1 (if it is) or 0 (if it is not).</p>
 *
 * <p>Here are some jump tests that take an input, then output 0 if the input was zero or 1 if the input was non-zero:
 * </p>
 *
 * <p>3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9 (using position mode) 3,3,1105,-1,9,1101,0,0,12,4,12,99,1 (using
 * immediate mode)</p>
 *
 * <p>Here's a larger example:</p>
 *
 * <p>3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31, 1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
 * 999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99</p>
 *
 * <p>The above example program uses an input instruction to ask for a single number. The program will then output 999
 * if the input value is below 8, output 1000 if the input value is equal to 8, or output 1001 if the input value is
 * greater than 8.</p>
 *
 * <p>This time, when the TEST diagnostic program runs its input instruction to get the ID of the system to test,
 * provide it 5, the ID for the ship's thermal radiator controller. This diagnostic test suite only outputs one number,
 * the diagnostic code.</p>
 *
 * <p>What is the diagnostic code for system ID 5?</p>
 */
public class Puzzle10 {

    private int inputValue = 5;
    private String programmCode =
        "3,225,1,225,6,6,1100,1,238,225,104,0,1102,59,58,224,1001,224,-3422,224,4,224,102,8,223,223,101,3,224,224,1,224,223,223,1101,59,30,225,1101,53,84,224,101,-137,224,224,4,224,1002,223,8,223,101,3,224,224,1,223,224,223,1102,42,83,225,2,140,88,224,1001,224,-4891,224,4,224,1002,223,8,223,1001,224,5,224,1,223,224,223,1101,61,67,225,101,46,62,224,1001,224,-129,224,4,224,1002,223,8,223,101,5,224,224,1,223,224,223,1102,53,40,225,1001,35,35,224,1001,224,-94,224,4,224,102,8,223,223,101,6,224,224,1,223,224,223,1101,5,73,225,1002,191,52,224,1001,224,-1872,224,4,224,1002,223,8,223,1001,224,5,224,1,223,224,223,102,82,195,224,101,-738,224,224,4,224,1002,223,8,223,1001,224,2,224,1,224,223,223,1101,83,52,225,1101,36,77,225,1101,9,10,225,1,113,187,224,1001,224,-136,224,4,224,1002,223,8,223,101,2,224,224,1,224,223,223,4,223,99,0,0,0,677,0,0,0,0,0,0,0,0,0,0,0,1105,0,99999,1105,227,247,1105,1,99999,1005,227,99999,1005,0,256,1105,1,99999,1106,227,99999,1106,0,265,1105,1,99999,1006,0,99999,1006,227,274,1105,1,99999,1105,1,280,1105,1,99999,1,225,225,225,1101,294,0,0,105,1,0,1105,1,99999,1106,0,300,1105,1,99999,1,225,225,225,1101,314,0,0,106,0,0,1105,1,99999,1007,226,226,224,1002,223,2,223,1006,224,329,1001,223,1,223,1108,226,226,224,102,2,223,223,1006,224,344,101,1,223,223,1007,677,677,224,102,2,223,223,1006,224,359,101,1,223,223,1108,677,226,224,1002,223,2,223,1005,224,374,1001,223,1,223,7,677,226,224,102,2,223,223,1005,224,389,1001,223,1,223,1008,677,677,224,1002,223,2,223,1005,224,404,101,1,223,223,108,226,226,224,1002,223,2,223,1006,224,419,101,1,223,223,1008,226,677,224,1002,223,2,223,1006,224,434,1001,223,1,223,1107,677,226,224,1002,223,2,223,1005,224,449,101,1,223,223,1008,226,226,224,102,2,223,223,1005,224,464,1001,223,1,223,8,226,226,224,1002,223,2,223,1006,224,479,1001,223,1,223,107,226,677,224,102,2,223,223,1005,224,494,1001,223,1,223,7,226,226,224,102,2,223,223,1005,224,509,1001,223,1,223,107,226,226,224,102,2,223,223,1005,224,524,101,1,223,223,107,677,677,224,1002,223,2,223,1006,224,539,101,1,223,223,8,677,226,224,1002,223,2,223,1006,224,554,101,1,223,223,1107,677,677,224,1002,223,2,223,1005,224,569,101,1,223,223,108,226,677,224,1002,223,2,223,1006,224,584,101,1,223,223,7,226,677,224,1002,223,2,223,1005,224,599,1001,223,1,223,8,226,677,224,102,2,223,223,1006,224,614,1001,223,1,223,108,677,677,224,1002,223,2,223,1006,224,629,1001,223,1,223,1007,226,677,224,1002,223,2,223,1006,224,644,101,1,223,223,1108,226,677,224,102,2,223,223,1005,224,659,1001,223,1,223,1107,226,677,224,102,2,223,223,1006,224,674,1001,223,1,223,4,223,99,226";

    @Test
    void puzzle() {

        System.out.println(prettyPrint(runProgramm(programmCode, inputValue))); // Answer: 7.731.427

        // Flasch: 1.891.490, your answer is too low.
    }


    @Test
    void testSamples() {

        assertThat(runProgramm("3,9,8,9,10,9,4,9,99,-1,8", 8).getOutput().get(0), is(1));
        assertThat(runProgramm("3,9,8,9,10,9,4,9,99,-1,8", 7).getOutput().get(0), is(0));
    }


    @Test
    void testOps() {

        assertThat(getOpCode(10002), is(2));
        assertThat(getFirstParamMode(11022), is(0));
        assertThat(getFirstParamMode(10122), is(1));
        assertThat(getSecondParamMode(11022), is(1));
        assertThat(getSecondParamMode(10122), is(0));
        assertThat(getThirdParamMode(11022), is(1));
        assertThat(getThirdParamMode(1122), is(0));

        System.out.println("First param Mode: " + getFirstParamMode(10122));
    }


    Context runProgramm(String programmCode, int input) {

        List<Integer> programmAsInt = parseProgramm(programmCode);
        Context context = new Context(programmAsInt, input);

        boolean stop = false;
        int instructionPointer = 0;

        while (!stop) {
            int instruction = getOpCode(programmAsInt.get(instructionPointer));

            if (instruction == 99) {
                stop = true;
            } else if (instruction == 1) { // ADD

                System.out.println("Pos " + instructionPointer + ": ADD");

                int value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                int value2 = getParamAccordingToMode(programmAsInt, instructionPointer, 1);
                int destination = programmAsInt.get(instructionPointer + 3);

                int sum = value1 + value2;
                System.out.println("\t" + value1 + "(from " + (instructionPointer + 1) + ")" + value2 + "(from "
                    + (instructionPointer + 2) + ") =" + sum
                    + " store at " + destination);

                programmAsInt.remove(destination);
                programmAsInt.add(destination, sum);
                instructionPointer += 4;
            } else if (instruction == 2) { // MUL

                System.out.println("Pos " + instructionPointer + ": MUL");

                int value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                int value2 = getParamAccordingToMode(programmAsInt, instructionPointer, 1);
                int destination = programmAsInt.get(instructionPointer + 3);

                int sum = value1 * value2;
                System.out.println("\t" + value1 + "(from " + (instructionPointer + 1) + ")*" + value2 + "(from "
                    + (instructionPointer + 2) + ")=" + sum + " store at " + destination);

                programmAsInt.remove(destination);
                programmAsInt.add(destination, sum);
                instructionPointer += 4;
            } else if (instruction == 3) { // INPUT

                int destination = programmAsInt.get(instructionPointer + 1);

                System.out.println("Pos " + instructionPointer + ": INPUT");
                System.out.println("\tInput " + input + " stored at " + destination);

                programmAsInt.remove(destination);
                programmAsInt.add(destination, input);
                instructionPointer += 2;
            } else if (instruction == 4) { // OUTPUT

                /*int addr = programmAsInt.get(instructionPointer + 1);
                int output = programmAsInt.get(addr);*/

                int output = getParamAccordingToMode(programmAsInt, instructionPointer, 0);

                System.out.println("Pos " + instructionPointer + ": OUTPUT");
                System.out.println("\tOutput : " + output);

                context.getOutput().add(output);

                instructionPointer += 2;
            } else if (instruction == 5) { // jump-if-true
                System.out.println("Pos " + instructionPointer + ": jump-if-true");

                int value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                int jumpGoal = getParamAccordingToMode(programmAsInt, instructionPointer, 1);

                if (value1 != 0) {
                    instructionPointer = jumpGoal;
                } else {
                    instructionPointer += 3;
                }
            } else if (instruction == 6) { // jump-if-false
                System.out.println("Pos " + instructionPointer + ": jump-if-true");

                int value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                int jumpGoal = getParamAccordingToMode(programmAsInt, instructionPointer, 1);

                if (value1 == 0) {
                    instructionPointer = jumpGoal;
                } else {
                    instructionPointer += 3;
                }
            } else if (instruction == 7) { // less-then
                System.out.println("Pos " + instructionPointer + ": less-then");

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
                System.out.println("Pos " + instructionPointer + ": equals");

                int value1 = getParamAccordingToMode(programmAsInt, instructionPointer, 0);
                int value2 = getParamAccordingToMode(programmAsInt, instructionPointer, 1);
                int resultGoal = programmAsInt.get(instructionPointer + 3); // getParamAccordingToMode(programmAsInt, instructionPointer, 2);

                System.out.println("\t" + value1 + "==" + value2);

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
        int input;
        List<Integer> output;

        public Context(List<Integer> programmAsIntOriginal, int input) {

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


        public int getInput() {

            return input;
        }


        public List<Integer> getOutput() {

            return output;
        }
    }
}
