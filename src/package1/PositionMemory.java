package package1;

import java.util.ArrayList;

public class PositionMemory {
    public static ArrayList<Position> searchedPositions = new ArrayList<Position>();

    public static boolean isHashed(Position pos){
        return searchedPositions.contains(pos);
    }

    public static void addPositionToHash(Position pos){
        searchedPositions.add(pos);
    }

    public static void removeFromHash(Position pos){
        searchedPositions.remove(pos);
    }

}