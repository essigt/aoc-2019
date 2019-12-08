package de.essig.adventofcode.aoc2019.spaceimageformat;

public class SpaceImageParser {


    public static SpaceImage parseSpaceImage(int rows, int cols, String image) {

        SpaceImage spaceImage = new SpaceImage();

        int layerSize = rows * cols;
        int layerCount = image.length() / layerSize;

        System.out.println("Try to parse " + layerCount + " layers");

        for(int i=0; i < layerCount; i++) {
            String layerAsString = image.substring(i * layerSize, (i + 1) * layerSize);
            System.out.println("Layer "+ i + ":" + layerAsString);

            SpaceImage.Layer layer = new SpaceImage.Layer();
            SpaceImage.Row row = new SpaceImage.Row();
            layer.addRow(row);

            //Parse whole image as one row... good enought for the task at hand
            for(char ch : layerAsString.toCharArray()) {
                Integer pixel = Integer.valueOf(String.valueOf(ch));
                row.addPixel(pixel);
            }

            spaceImage.addLayer(layer);
        }

        return spaceImage;

    }

}
