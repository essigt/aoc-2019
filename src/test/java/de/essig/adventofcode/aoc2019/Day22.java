package de.essig.adventofcode.aoc2019;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static de.essig.adventofcode.aoc2019.Day22.ACTION.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

public class Day22 {

    public enum ACTION {CUT, INCREMENT, NEW_STACK};


    @Test
    void runPartOne() {
        List<Integer> stack = new ArrayList<>();
        for(int i=0; i < 10_007; i++) {
            stack.add(i);
        }

        // Parse
        String[] split = INPUT.split("\\n");
        List<Action> actions = new ArrayList<>();
        for(String a : split) {
            actions.add(new Action(a));
        }


        for(Action action : actions) {
            if(action.getAction() == NEW_STACK) {
                System.out.println("New Stack");
                Collections.reverse(stack);
            } else if(action.getAction() == CUT) {
                System.out.println("Cut ");
                if(action.getNumber() > 0) {
                    ArrayList<Integer> newStack = new ArrayList<>();

                    newStack.addAll(stack.subList(action.getNumber(), stack.size()));
                    newStack.addAll(stack.subList(0, action.getNumber()));
                    stack = newStack;
                } else if(action.getNumber() < 0) {
                    ArrayList<Integer> newStack = new ArrayList<>();
                    int abs = Math.abs(action.getNumber());

                    newStack.addAll(stack.subList(stack.size()-abs, stack.size()));
                    newStack.addAll(stack.subList(0, stack.size()-abs));

                    stack = newStack;
                }

            } else if(action.getAction() == INCREMENT) {
                System.out.println("Increment");
                ArrayList<Integer> newStack = new ArrayList<>();
                newStack.addAll(stack);

                int pos = 0;
                for(int i=0; i < stack.size(); i++) {
                    newStack.remove(pos);
                    newStack.add(pos, stack.get(i));
                    pos += action.getNumber();
                    pos = pos % stack.size();
                }

                stack = newStack;
            }
        }


        System.out.println("Output: " + stack);
        System.out.println("Index of Card 2019: " + stack.indexOf(2019));

    }


    @Test
    void runPartTwo() {
        List<Long> stack = new ArrayList<>();
        for(long i=0; i < 119315717514047L; i++) {
            stack.add(i);
        }

        // Parse
        String[] split = INPUT.split("\\n");
        List<Action> actions = new ArrayList<>();
        for(String a : split) {
            actions.add(new Action(a));
        }


        for(Action action : actions) {
            if(action.getAction() == NEW_STACK) {
                System.out.println("New Stack");
                Collections.reverse(stack);
            } else if(action.getAction() == CUT) {
                System.out.println("Cut ");
                if(action.getNumber() > 0) {
                    ArrayList<Long> newStack = new ArrayList<>();

                    newStack.addAll(stack.subList(action.getNumber(), stack.size()));
                    newStack.addAll(stack.subList(0, action.getNumber()));
                    stack = newStack;
                } else if(action.getNumber() < 0) {
                    ArrayList<Long> newStack = new ArrayList<>();
                    int abs = Math.abs(action.getNumber());

                    newStack.addAll(stack.subList(stack.size()-abs, stack.size()));
                    newStack.addAll(stack.subList(0, stack.size()-abs));

                    stack = newStack;
                }

            } else if(action.getAction() == INCREMENT) {
                System.out.println("Increment");
                ArrayList<Long> newStack = new ArrayList<>();
                newStack.addAll(stack);

                int pos = 0;
                for(int i=0; i < stack.size(); i++) {
                    newStack.remove(pos);
                    newStack.add(pos, stack.get(i));
                    pos += action.getNumber();
                    pos = pos % stack.size();
                }

                stack = newStack;
            }
        }


        System.out.println("Output: " + stack);
        System.out.println("Index of Card 2020: " + stack.indexOf(2020));

    }

    @Test
    void testParser() {

        Action cut = new Action("cut -1468");
        assertThat(cut.getNumber(), is(-1468));
        assertThat(cut.getAction(), is(CUT));

        Action increment = new Action("deal with increment 19");
        assertThat(increment.getNumber(), is(19));
        assertThat(increment.getAction(), is(INCREMENT));

        Action newStack = new Action("deal into new stack");
        assertThat(newStack.getNumber(), is(0));
        assertThat(newStack.getAction(), is(NEW_STACK));
    }

    public static class Action {
        private int number;
        private ACTION action;

        public Action(String action) {

            if(action.startsWith("cut")) {
                this.action = CUT;
                this.number = Integer.valueOf(action.replace("cut ", "").trim());
            } else if(action.startsWith("deal with increment")) {
                this.action = INCREMENT;
                this.number = Integer.valueOf(action.replace("deal with increment", "").trim());
            } else if(action.startsWith("deal into new stack")) {
                this.action = NEW_STACK;
                this.number = 0;
            }
        }

        public int getNumber() {
            return number;
        }

        public ACTION getAction() {
            return action;
        }
    }


    private final static String INPUT2 = "deal with increment 3\n";

    private final static String INPUT = "cut -1468\n" +
            "deal with increment 19\n" +
            "cut -7127\n" +
            "deal with increment 8\n" +
            "cut -8697\n" +
            "deal with increment 58\n" +
            "cut 4769\n" +
            "deal into new stack\n" +
            "cut 4921\n" +
            "deal with increment 16\n" +
            "cut -1538\n" +
            "deal with increment 55\n" +
            "cut 3387\n" +
            "deal with increment 41\n" +
            "cut 4127\n" +
            "deal with increment 26\n" +
            "cut 5512\n" +
            "deal with increment 21\n" +
            "deal into new stack\n" +
            "deal with increment 44\n" +
            "cut -7989\n" +
            "deal with increment 28\n" +
            "cut 569\n" +
            "deal into new stack\n" +
            "cut -9795\n" +
            "deal into new stack\n" +
            "cut -6877\n" +
            "deal with increment 60\n" +
            "cut -6500\n" +
            "deal with increment 37\n" +
            "cut -9849\n" +
            "deal with increment 66\n" +
            "cut -4821\n" +
            "deal with increment 50\n" +
            "deal into new stack\n" +
            "cut 9645\n" +
            "deal with increment 22\n" +
            "cut -6430\n" +
            "deal with increment 17\n" +
            "cut 658\n" +
            "deal with increment 67\n" +
            "cut -9951\n" +
            "deal into new stack\n" +
            "deal with increment 31\n" +
            "cut -2423\n" +
            "deal with increment 39\n" +
            "cut -5126\n" +
            "deal with increment 7\n" +
            "cut 432\n" +
            "deal with increment 8\n" +
            "cut 682\n" +
            "deal with increment 45\n" +
            "deal into new stack\n" +
            "deal with increment 41\n" +
            "cut -130\n" +
            "deal with increment 74\n" +
            "deal into new stack\n" +
            "cut -9207\n" +
            "deal into new stack\n" +
            "cut 7434\n" +
            "deal with increment 31\n" +
            "cut -5165\n" +
            "deal into new stack\n" +
            "cut 6209\n" +
            "deal with increment 25\n" +
            "cut 2734\n" +
            "deal with increment 53\n" +
            "deal into new stack\n" +
            "cut -1528\n" +
            "deal with increment 25\n" +
            "deal into new stack\n" +
            "deal with increment 68\n" +
            "cut 6458\n" +
            "deal into new stack\n" +
            "cut 1895\n" +
            "deal with increment 16\n" +
            "cut -6137\n" +
            "deal with increment 53\n" +
            "cut 2761\n" +
            "deal with increment 73\n" +
            "deal into new stack\n" +
            "cut 1217\n" +
            "deal with increment 69\n" +
            "deal into new stack\n" +
            "deal with increment 54\n" +
            "cut -6639\n" +
            "deal into new stack\n" +
            "cut -2891\n" +
            "deal with increment 10\n" +
            "cut -6297\n" +
            "deal with increment 31\n" +
            "cut 4591\n" +
            "deal with increment 35\n" +
            "cut -4035\n" +
            "deal with increment 65\n" +
            "cut -7504\n" +
            "deal into new stack\n" +
            "deal with increment 54\n" +
            "deal into new stack\n" +
            "cut 1313\n";

}
