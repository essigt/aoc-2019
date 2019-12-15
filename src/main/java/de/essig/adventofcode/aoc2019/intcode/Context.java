package de.essig.adventofcode.aoc2019.intcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Context {

    private Map<Long, Long> originalProgram;
    private Map<Long, Long> programmAsIntEnd;
    private long instructionPointer;
    private List<Long> inputs;
    private List<Long> output;
    private long relativeBase = 0;
    private boolean halted;

    public static Context newContext(Map<Long, Long> programCode) {
        return new Context(programCode, new ArrayList<>());
    }

    Context(Map<Long, Long> programCode, List<Long> input) {

        this.originalProgram = new HashMap<>(programCode);
        this.programmAsIntEnd = new HashMap<>(programCode);
        this.inputs = input;
        this.output = new ArrayList<>();
    }

    public Map<Long, Long> getProgrammAsIntEnd() {

        return programmAsIntEnd;
    }


    public void setProgrammAsIntEnd(Map<Long, Long> programmAsIntEnd) {

        this.programmAsIntEnd = new HashMap<>(programmAsIntEnd);
    }

    public void incrementRelativeBase(long relativeBaseIncrement) {
        this.relativeBase = relativeBase + relativeBaseIncrement;
    }

    public long getRelativeBase() {
        return relativeBase;
    }

    public Map<Long, Long> getOriginalProgram() {

        return originalProgram;
    }


    public List<Long> getInputs() {

        return inputs;
    }

    public void addInput(Long input) {
        this.inputs.add(input);
    }

    public long getInstructionPointer() {
        return instructionPointer;
    }

    public void setInstructionPointer(long instructionPointer) {
        this.instructionPointer = instructionPointer;
    }

    public void resetOutputs() {
        output.clear();
    }

    public List<Long> getOutput() {

        return output;
    }

    public boolean isHalted() {
        return halted;
    }

    public void setHalted(boolean halted) {
        this.halted = halted;
    }
}