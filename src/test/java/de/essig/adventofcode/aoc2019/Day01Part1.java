package de.essig.adventofcode.aoc2019;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Day01Part1 {

    private final String testInput = "109506\n" +
            "140405\n" +
            "139135\n" +
            "110950\n" +
            "84296\n" +
            "123991\n" +
            "59438\n" +
            "85647\n" +
            "81214\n" +
            "100517\n" +
            "100910\n" +
            "57704\n" +
            "83368\n" +
            "50777\n" +
            "85523\n" +
            "95788\n" +
            "127699\n" +
            "138908\n" +
            "95502\n" +
            "81703\n" +
            "67317\n" +
            "108468\n" +
            "58394\n" +
            "72202\n" +
            "121580\n" +
            "86908\n" +
            "72705\n" +
            "86578\n" +
            "83714\n" +
            "114900\n" +
            "142915\n" +
            "51332\n" +
            "69054\n" +
            "97039\n" +
            "143539\n" +
            "61143\n" +
            "113534\n" +
            "98335\n" +
            "58533\n" +
            "83893\n" +
            "127138\n" +
            "50844\n" +
            "88397\n" +
            "133591\n" +
            "83563\n" +
            "52435\n" +
            "96342\n" +
            "109491\n" +
            "81148\n" +
            "127397\n" +
            "86200\n" +
            "92418\n" +
            "144842\n" +
            "120142\n" +
            "97531\n" +
            "54449\n" +
            "91004\n" +
            "129115\n" +
            "142487\n" +
            "68513\n" +
            "140405\n" +
            "80111\n" +
            "139359\n" +
            "57486\n" +
            "116973\n" +
            "135102\n" +
            "59737\n" +
            "144040\n" +
            "95483\n" +
            "134470\n" +
            "60473\n" +
            "113142\n" +
            "78189\n" +
            "53845\n" +
            "124139\n" +
            "78055\n" +
            "63791\n" +
            "99879\n" +
            "58630\n" +
            "111233\n" +
            "80544\n" +
            "76932\n" +
            "79644\n" +
            "116247\n" +
            "54646\n" +
            "85217\n" +
            "110795\n" +
            "142095\n" +
            "74492\n" +
            "93318\n" +
            "122300\n" +
            "82755\n" +
            "147407\n" +
            "98697\n" +
            "98105\n" +
            "132055\n" +
            "67856\n" +
            "109731\n" +
            "75747\n" +
            "135700\n";

    @Test
    void calculateFuel() {
        String[] split = testInput.split("\\n");
        int sum = 0;
        for(String mass : split) {
            int massInteger = Integer.valueOf(mass);
            System.out.println("Fuel for mass " + massInteger + "=");
            sum += moduleFuel(massInteger);
        }

        System.out.println("Sum: " + sum); // Sum: 3.216.744
    }

    @Test
    void testCalculateFuel() {
        assertTrue( moduleFuel(12) == 2);
        assertTrue( moduleFuel(14) == 2);
        assertTrue( moduleFuel(1969) == 654);
        assertTrue( moduleFuel(100756) == 33583);
    }


    private int moduleFuel(int mass) {
        return (mass/3) -2 ;
    }
}
