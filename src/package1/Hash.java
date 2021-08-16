package package1;

import java.util.*;

public class Hash {

    public static HashMap<Integer, Position> zobristMap = new HashMap<>(); // first index = hash value, second index = Position Object
    private Random r = new Random(); // Random Number Generator -> May change in future for something more accurate
    private int[][] table = new int[64][12]; // 64 squares with 12 spaces each

    public Hash(){
        // Initialize Random Matrix
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                table[i][j] = r.nextInt(Integer.MAX_VALUE);
            }
        }
    }

    // 1. check if in hash table with getBoardEvaluationHASH(pos) -> if returns not -1 -> this is board evaluation already found.
    //                                                              If is -1 -> addPositionToHashTable(pos)
    // 2. We want to add Position to table if moves to current length is equal to the max depth that will be searched -> it is a good position to skip on...!



    // assumes it's already in the table
    public Position getPositionFromHashedPosition(Position pos){
        try{
            return zobristMap.get(getHashValue(pos)); // return that board evaluation already found
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Tried to access hash value that doesn't exist");
            return null;
        }
    }

    public boolean isInTable(Position pos){
        if(zobristMap.containsKey(getHashValue(pos))){
            return true;
        } return false;
    }

    public void addPositionToTable(Position pos){
        pos.setWasHashed(true);
        zobristMap.put(getHashValue(pos), pos);
    }

    // doesn't take into account castling or en passant -> shouldn't matter for now though...
    private int getHashValue(Position pos){
        ArrayList<Integer> allValues = new ArrayList<>();
        // iterate through the current board values (12 of them of course)
        for (int i = 0; i < pos.getCurrentBoard().length; i++) {
            // get the squares populated with that type's indices.
            ArrayList<Integer> populatedIndices = Runner.controlAndSeparation.getPopulatedIndices(pos.getCurrentBoard()[i]);
            // iterate through each of the pieces in one long
            for (int j = 0; j < populatedIndices.size(); j++) {
                // get the random number from that space (j) for that piece (i) and add it to all values to be xOr-ed later
                allValues.add(table[populatedIndices.get(j)][i]);
            }
        }
        int finalHash = 0;
        for(int value : allValues){
            finalHash^=value;
        }

        return finalHash;
    }

}
