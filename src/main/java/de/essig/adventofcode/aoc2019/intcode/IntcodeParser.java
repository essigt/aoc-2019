package de.essig.adventofcode.aoc2019.intcode;

import java.util.HashMap;
import java.util.Map;

public class IntcodeParser {

    public static Map<Long, Long> parseProgramm(String input) {

        //System.out.println("Parsing intcode input...");
          Map<Long, Long> programm = new HashMap<>();

        String[] split = input.split(",");

        long pos = 0;

        for (String instruction : split) {
            programm.put(pos, Long.valueOf(instruction));
            pos++;
        }

        //System.out.println("Parsed intcode input of length " + pos);


        return programm;
    }
}
