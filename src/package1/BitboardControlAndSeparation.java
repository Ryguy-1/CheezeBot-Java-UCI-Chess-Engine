package package1;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BitboardControlAndSeparation {

    Long[] referenceArray;

    String[][] algebraicNotation = {
            {"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8"},
            {"a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7"},
            {"a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6"},
            {"a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5"},
            {"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4"},
            {"a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3"},
            {"a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2"},
            {"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"}
    };

    String[] bitboardOrderedAlgebraicNotation = {
            "h1", "g1", "f1", "e1", "d1", "c1", "b1", "a1",
            "h2", "g2", "f2", "e2", "d2", "c2", "b2", "a2",
            "h3", "g3", "f3", "e3", "d3", "c3", "b3", "a3",
            "h4", "g4", "f4", "e4", "d4", "c4", "b4", "a4",
            "h5", "g5", "f5", "e5", "d5", "c5", "b5", "a5",
            "h6", "g6", "f6", "e6", "d6", "c6", "b6", "a6",
            "h7", "g7", "f7", "e7", "d7", "c7", "b7", "a7",
            "h8", "g8", "f8", "e8", "d8", "c8", "b8", "a8",
    };

    BitboardControlAndSeparation(){
        referenceArray = new Long[64];
        referenceArray = populateReferenceArray(referenceArray);
    }

    //Method to Separate the individual pieces in a bitboard into multiple of their own bitboards
    public Long[] splitBitboard(long bitboard){
        ArrayList<Long> bitboards = new ArrayList<>();
        for (int i = 0; i < referenceArray.length; i++) {
            if((referenceArray[i]&bitboard)!=0l){
                bitboards.add(referenceArray[i]);
            }
        }

        Long[] bitboardsArray = new Long[bitboards.size()];
        for (int i = 0; i < bitboards.size(); i++) {
            bitboardsArray[i] = bitboards.get(i);
        }

        return bitboardsArray;
    }

    private Long[] populateReferenceArray(Long[] referenceArray){
        long temp = 1l;
        for (int i = 0; i < referenceArray.length; i++) {
            referenceArray[i] = temp << i;
        }
        return referenceArray;
    }

    public Long[][] splitBitboardArray(Long[] currentBoard){

        //each small array has a different size
        Long[][] splitBoard = {splitBitboard(currentBoard[0]),
                splitBitboard(currentBoard[1]),
                splitBitboard(currentBoard[2]),
                splitBitboard(currentBoard[3]),
                splitBitboard(currentBoard[4]),
                splitBitboard(currentBoard[5]),
                splitBitboard(currentBoard[6]),
                splitBitboard(currentBoard[7]),
                splitBitboard(currentBoard[8]),
                splitBitboard(currentBoard[9]),
                splitBitboard(currentBoard[10]),
                splitBitboard(currentBoard[11])};

        return splitBoard;

    }

    public Long[] condense2dBoard(Long[][] currentBoard){

        Long[] condensedBoard = new Long[12];
        //initialize condensedBoard contents
        for (int i = 0; i < condensedBoard.length; i++) {
            condensedBoard[i] = 0l;
        }


        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard[i].length; j++) {
                condensedBoard[i] |= currentBoard[i][j];
            }
        }

        return condensedBoard;
    }

    public long condenseBoard(Long[] currentBoard){
        long combined = 0l;
        for (int i = 0; i < currentBoard.length; i++) {
            combined |= currentBoard[i];
        }
        return combined;
    }

    public Long[] changeBitboardArrayIndex(Long[] currentBoard, int index, long bitboard){
        Long[] copyBoard = new Long[currentBoard.length];
        for (int i = 0; i < currentBoard.length; i++) {
            if(i==index){
                copyBoard[i] = bitboard;
            }else{
                copyBoard[i] = currentBoard[i];
            }
        }
        return copyBoard;
    }

    public Long[] getCapitalPieces(Long[] currentBoard){
        Long[] halfPieces = new Long[currentBoard.length/2];
        byte counter = 0;
        for (int i = currentBoard.length/2; i < currentBoard.length; i++) {
            halfPieces[counter] = currentBoard[i];
            counter++;
        }
        return halfPieces;
    }

    public Long[] getLowerCasePieces(Long[] currentBoard){
        Long[] halfPieces = new Long[currentBoard.length/2];
        for (int i = 0; i < currentBoard.length/2; i++) {
            halfPieces[i] = currentBoard[i];
        }
        return halfPieces;
    }

    //
    public String singleBitBitboardToString(long singleBitBitboard){
        String letters = "";
        for (int i = 0; i < referenceArray.length; i++) {
            if((referenceArray[i] & singleBitBitboard)!=0){
                letters = bitboardOrderedAlgebraicNotation[i];
            }
        }
        return letters;
    }





}
