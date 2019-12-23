package de.essig.adventofcode.aoc2019;

import de.essig.adventofcode.aoc2019.network.NetworkComputer;
import de.essig.adventofcode.aoc2019.network.Package;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

public class Day23 {

    @Test
    void runPartOne() {
        long result = 0;

        List<NetworkComputer> computerList = new ArrayList<>();

        for(int i=0; i < 50; i++) {
            computerList.add( new NetworkComputer(i));
        }


        while (result == 0) {
            for (int i = 0; i < 50; i++) {
                NetworkComputer networkComputer = computerList.get(i);
                Optional<Package> aPackage = networkComputer.runStep();
                if(aPackage.isPresent()) {
                    Package pck = aPackage.get();

                    if(pck.getDestination() == 255) {
                        System.out.println("Received package: " + pck);
                        result = pck.getY();
                        break;
                    } else if(pck.getDestination() < 50) {
                        computerList.get((int)pck.getDestination()).addPackage(pck);
                    }
                }
            }
        }

        assertThat(result, is(18966L));
    }

    Package lastNATPackage;
    private long secondLastNATyDelivered = -1;
    private long lastNATyDelivered = -1;

    @Test
    void runPartTwo() {
        boolean stop = false;

        List<NetworkComputer> computerList = new ArrayList<>();

        for(int i=0; i < 50; i++) {
            computerList.add( new NetworkComputer(i));
        }


        while (!stop) {
            for (int i = 0; i < 50; i++) {
                NetworkComputer networkComputer = computerList.get(i);
                Optional<Package> aPackage = networkComputer.runStep();
                if(aPackage.isPresent()) {
                    Package pck = aPackage.get();

                    if(pck.getDestination() == 255) {
                        System.out.println("NAT: Received NAT package: " + pck);
                        lastNATPackage = pck;
                        break;
                    } else if(pck.getDestination() < 50) {
                        computerList.get((int)pck.getDestination()).addPackage(pck);
                    }
                }
            }

            boolean networkIdle = true;
            int idleComputers = 0;
            for(NetworkComputer computer : computerList) {
                if(!computer.hasIdled()) {
                    networkIdle = false;
                } else {
                    idleComputers++;
                }
            }

            //System.out.println("Idle Computer: " + idleComputers);
            if(networkIdle) {
                System.out.println("NAT: Network idle, send NAT to first computer: " + lastNATPackage);
                computerList.get(0).addPackage(lastNATPackage);
                secondLastNATyDelivered = lastNATyDelivered;
                lastNATyDelivered = lastNATPackage.getY();

                if(secondLastNATyDelivered == lastNATyDelivered) {
                    System.out.println("NAT:  Y=" + lastNATyDelivered);
                    break;

                    // to high: 18966
                    // second: 14370
                }
            }

            //stop = networkIdle;
        }

    }
}
