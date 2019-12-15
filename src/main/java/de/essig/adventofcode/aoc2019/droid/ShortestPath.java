package de.essig.adventofcode.aoc2019.droid;

import scala.concurrent.impl.FutureConvertersImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShortestPath {


    public int shortestPath(int startX, int startY, int endX, int endY, Map<Field, Long> map) {

        Path path = deepenPath(new Path(new Field(startX, startY)), endX, endY, map);

        System.out.println("Path: " + path.getFields());
        return path.getFields().size();
    }


    private Path deepenPath(Path path, int endX, int endY, Map<Field, Long> map) {
        //System.out.println("Deepen Path at " + path.getLastField());
        Field lastField = path.getLastField();
        List<Field> neigbours = findNeigbours(lastField, map);

        neigbours = neigbours.stream().filter(f -> !path.contains(f)).collect(Collectors.toList());

        if(neigbours.size() == 0) {
            return null;
        }

        List<Path> goals = new ArrayList<>();

        for(Field neigbour : neigbours) {
            Path newPath = new Path(path, neigbour);
            if(neigbour.getX() == endX && neigbour.getY() == endY) {
                System.out.println("Found Goal");
                goals.add(path);
            } else {
                Path path1 = deepenPath(newPath, endX, endY, map);
                if(path1 != null) {
                    return path1;
                }
            }
        }


        Path shortest= null;
        int length = Integer.MAX_VALUE;

        for(Path goal : goals) {
            if(goal.getFields().size() < length) {
                shortest = goal;
                length = goal.getFields().size();
            }
        }

        return shortest;
    }




    private List<Field> findNeigbours(Field field, Map<Field, Long> map) {
        List<Field> neigbours = new ArrayList<>();
        int startX = field.getX();
        int startY = field.getY();

        for( int y = startY-1; y <= startY+1; y++) {
            for( int x = startX-1; x <= startX+1; x++) {

                if(x == startX && y == startY) { // Skip the field itself
                    continue;
                }

                Field currentField = new Field(x,y);
                long fieldValue = map.getOrDefault(currentField, -1L);

                if(fieldValue == 1 || fieldValue == 2) {
                    neigbours.add(currentField);
                }
            }
        }

        return neigbours;
    }

}
