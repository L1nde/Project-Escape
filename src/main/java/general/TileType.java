package general;

public enum TileType {
    EMPTY, WALL, FOOD;
    public static TileType create(String type){
        switch (type){
            case "W":
                return WALL;
            case "F":
                return FOOD;
            default:
                return EMPTY;
        }
    }
}
