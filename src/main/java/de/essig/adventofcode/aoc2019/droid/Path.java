package de.essig.adventofcode.aoc2019.droid;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class Path {

    private List<Field> fields;

    public Path(Field field) {
        this.fields = new ArrayList<>(asList(field));
    }

    public Path(Path path, Field field) {
        this.fields = new ArrayList<>(path.getFields());
        this.fields.add(field);
    }

    public boolean contains(Field field) {
        return fields.contains(field);
    }

    public List<Field> getFields() {
        return fields;
    }

    public Field getLastField() {
        return fields.get(fields.size()-1);
    }

}
