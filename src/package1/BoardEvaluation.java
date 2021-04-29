package package1;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class BoardEvaluation {

    //takes a position object and evaluates how good for each side that board is.
    //We could have a boardEvaluation object linked to each position object or simply make it a static object encapsulated by the Runner class.

    //Capital = maximize
    //Lower Case = minimize


    //Spend time later figuring out mobility advantages
    private static final int materialMultiplier = 20;
    private static final int mobilityMultiplier = 1;
    private static final int checkAdder = 30;

    private static final int acceptStalemateDifference = 200;

    private static final int checkmateWeight = (Integer.MAX_VALUE/2); //HAS TO BE -1 BECAUSE MIN AND MAX VALUES IN MINIMAX ARE INTEGER.MAX_VALUE. THROWS ERROR OTHERWISE.
    private static final int stalemateWeight = (Integer.MAX_VALUE/4);

    private static final int numActiveFactors = 1;

    //piece values for material advantage
    private static final int pawnValue = 1;
    private static final int knightValue = 4;
    private static final int bishopValue = 4;
    private static final int rookValue = 6;
    private static final int queenValue = 12;
    private static final int kingValue = 100_000;


    public int getBoardRanking(Position pos){
        int totalAdv = 0;
        //checks for checkmates before all else
        if(Runner.search.capitalIsInCheckmate(pos)){
            return -checkmateWeight;
        }else if(Runner.search.lowerCaseIsInCheckmate(pos)){
            return checkmateWeight;
        }
        totalAdv+=getPieceAdvantage(pos)*materialMultiplier;
        totalAdv+=getMobilityAdvantage(pos)*mobilityMultiplier;
        totalAdv+=checkAdder;
        //then checks for stalemates.
        if(Runner.search.capitalIsInStalemate(pos) && totalAdv<(-acceptStalemateDifference)){ //if capital is in stalemate and losing by more than acceptStalemateDifference, it tries to stalemate.
            return stalemateWeight; //stalemate does not compare to checkmate
        }else if(Runner.search.capitalIsInStalemate(pos) && totalAdv>acceptStalemateDifference){
            return -stalemateWeight;
        }else if(Runner.search.lowerCaseIsInStalemate(pos) && totalAdv>acceptStalemateDifference){//if lower case is in stalemate and losing by more than acceptStalemateDifference, it tries to stalemate.
            return -stalemateWeight;
        }else if(Runner.search.lowerCaseIsInStalemate(pos) && totalAdv<(-acceptStalemateDifference)){
            return stalemateWeight;
        }
        return totalAdv;
    }

    //order in array: r, n, b, q, k, p, R, N, B, Q, K, P
    private int getPieceAdvantage(Position pos){

        int totalCount = 0;

        for (int i = 0; i < pos.getCurrentBoard().length; i++) {
            switch(i){
                case 0:
                    //LC Rook
                    totalCount+=Runner.controlAndSeparation.splitBitboard(pos.getCurrentBoard()[i]).length * (-rookValue);
                    break;
                case 1:
                    //LC Knight
                    totalCount+=Runner.controlAndSeparation.splitBitboard(pos.getCurrentBoard()[i]).length * (-knightValue);
                    break;
                case 2:
                    //LC Bishop
                    totalCount+= Runner.controlAndSeparation.splitBitboard(pos.getCurrentBoard()[i]).length * (-bishopValue);
                    break;
                case 3:
                    //LC Queen
                    totalCount+=Runner.controlAndSeparation.splitBitboard(pos.getCurrentBoard()[i]).length * (-queenValue);
                    break;
                case 4:
                    //LC King
                    totalCount+=Runner.controlAndSeparation.splitBitboard(pos.getCurrentBoard()[i]).length * (-kingValue);
                    break;
                case 5:
                    //LC Pawn
                    totalCount+=Runner.controlAndSeparation.splitBitboard(pos.getCurrentBoard()[i]).length * (-pawnValue);
                    break;
                case 6:
                    //Capital Rook
                    totalCount+=Runner.controlAndSeparation.splitBitboard(pos.getCurrentBoard()[i]).length * (rookValue);
                    break;
                case 7:
                    //Capital Knight
                    totalCount+=Runner.controlAndSeparation.splitBitboard(pos.getCurrentBoard()[i]).length * (knightValue);
                    break;
                case 8:
                    //Capital Bishop
                    totalCount+= Runner.controlAndSeparation.splitBitboard(pos.getCurrentBoard()[i]).length * (bishopValue);
                    break;
                case 9:
                    //Capital Queen
                    totalCount+=Runner.controlAndSeparation.splitBitboard(pos.getCurrentBoard()[i]).length * (queenValue);
                    break;
                case 10:
                    //Capital King
                    totalCount+=Runner.controlAndSeparation.splitBitboard(pos.getCurrentBoard()[i]).length * (kingValue);
                    break;
                case 11:
                    //Capital Pawn
                    totalCount+=Runner.controlAndSeparation.splitBitboard(pos.getCurrentBoard()[i]).length * (pawnValue);
                    break;
            }
        }
        return totalCount;
    }

    //to make faster, but slightly less accurate, only uses pseudo-legal moves, not fully legal moves necessarily. (just methods in CheckValidConditions)
    //just counts the total number of squares that are included by pseudo-legal moves.
    private int getMobilityAdvantage(Position pos){
        return Runner.controlAndSeparation.splitBitboard(Runner.checkValidConditions.getPseudoLegalMoves(pos, 'c')).length
                - Runner.controlAndSeparation.splitBitboard(Runner.checkValidConditions.getPseudoLegalMoves(pos, 'l')).length;
    }

    //see chessProgrammingWiki for interesting study on Pawn Advantage with a Formula. Implement LATER.
    private double getPawnAdvantage(){
        return 0;
    }

    private double roundDoubleProperly(double num){
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.HALF_EVEN);
        System.out.println(df.format(num));
        return Double.parseDouble(df.format(num));
    }



}
