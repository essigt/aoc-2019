package de.essig.adventofcode.aoc2019.spaceimageformat;

import java.util.ArrayList;
import java.util.List;

public class SpaceImage {


    List<Layer> layers = new ArrayList<>();

    public void addLayer(Layer row) {
        layers.add(row);
    }

    public List<Layer> getLayers() {
        return layers;
    }


    public static class Layer {
        public List<Row> rows = new ArrayList<>();

        public void addRow(Row row) {
            rows.add(row);
        }

        public List<Row> getRows() {
            return rows;
        }

        public int getNumberOfValues(int value) {
            int count = 0;

            for(Row row : rows) {
                count += row.getNumberOfValues(value);
            }

            return count;
        }
    }


    public static class Row {
        public List<Integer> pixels = new ArrayList<>();

        public void addPixel(int pixel) {
            pixels.add(pixel);
        }

        public List<Integer> getPixels() {
            return pixels;
        }

        public int getNumberOfValues(int value) {
            int count = 0;

            for(int pixel : pixels) {
                if(pixel == value) {
                    count++;
                }
            }

            return count;
        }

        public void setPixel(int i, int newValue) {
            pixels.remove(i);
            pixels.add(i, newValue);
        }
    }
}
