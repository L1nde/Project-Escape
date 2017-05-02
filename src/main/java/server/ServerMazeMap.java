package server;

import general.ghosts.Tile;
import general.MapUpdate;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Meelis Perli on 4/2/2017.
 */
public class ServerMazeMap implements Serializable {
    private final int width;
    private final int height;
    private String[][] map;
    private MapUpdate mapUpdate;
    private Tile[][] tiles;

    public ServerMazeMap(int width, int height) throws IOException {
        this.width = width/20;
        this.height = height/20;
        this.map = readMap("map.txt");
    }

    public void setMapUpdate(MapUpdate mapUpdate) {
        this.mapUpdate = mapUpdate;
    }

    public MapUpdate getMapUpdate() {
        return mapUpdate;
    }

    private String[][] readMap(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src", "main", "resources", "maps", fileName));
        map = new String[lines.get(0).length()][lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(0).length(); j++) {
                map[j][i] = lines.get(i).substring(j,j+1);
            }

    }
        return map;
    }

    public String[][] getMap() {
        return this.map;
    }
}
