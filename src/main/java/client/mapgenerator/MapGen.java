package client.mapgenerator;

import java.io.*;

/**
 * Created by Meelis on 10/04/2017.
 */
public class MapGen {
    public static void main(String[] args) throws IOException {
        int rows = 30;
        int columns = 40;
        String[][] map = new String[rows][columns];
        boolean outerWalls = true;
        boolean food = true;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (outerWalls && (row == 0 || column == 0 || row == rows -1 || column == columns -1)) {
                    map[row][column] = "W";
                }
                else if (food) {
                    map[row][column] = "F";
                }
            }
        }

        try(FileOutputStream fos = new FileOutputStream(new File("src\\main\\resources\\map1.txt"))) {
            byte[] mapInBytes = getMapAsString(map).getBytes();
            fos.write(mapInBytes);
            fos.flush();
        }
    }
    static String getMapAsString(String[][] map) {
        String mapAsString = "";
        for (String[] strings : map) {
            String mapRow = "";
            for (String string : strings) {
                mapRow += string;
            }
            mapRow += "\n";
            mapAsString += mapRow;
        }
        return mapAsString;
    }
}
