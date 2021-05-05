package package1;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class BoardEvaluation {

    //takes a position object and evaluates how good for each side that board is.
    //We could have a boardEvaluation object linked to each position object or simply make it a static object encapsulated by the Runner class.

    //Capital = maximize
    //Lower Case = minimize


    //Spend time later figuring out mobility advantages
    private static final int materialMultiplier = 100; //don't want anything to interfere with this...
    private static final int centerControlMultiplier = 10;
    private static final int pawnStructureMultiplier = 20;
    private static final int attackingPiecesMultiplier = 2;
    private static final int mobilityMultiplier = 3;

    private static final int checkAdder = 20;
    private static final int castleAdder = 30;

    private static final int acceptStalemateDifference = 300;

    private static final int checkmateWeight = (Integer.MAX_VALUE/2); //HAS TO BE -1 BECAUSE MIN AND MAX VALUES IN MINIMAX ARE INTEGER.MAX_VALUE. THROWS ERROR OTHERWISE.
    private static final int stalemateWeight = (Integer.MAX_VALUE/4);

    private static final int numActiveFactors = 4; //including checkAdder.

    //piece values for material advantage
    private static final int pawnValue = 1;
    private static final int knightValue = 3; //changed to 3 because a knight really should't trade for a bishop
    private static final int bishopValue = 4;
    private static final int rookValue = 7;
    private static final int queenValue = 14;
    private static final int kingValue = 100_000;



    public int getBoardRanking(Position pos){
        int totalAdv = 0;
        //CHECKMATES BEFORE EVERYTHING ELSE
        if(Runner.search.capitalIsInCheckmate(pos)){
            return -checkmateWeight;
        }else if(Runner.search.lowerCaseIsInCheckmate(pos)){
            return checkmateWeight;
        }
        //OTHER VARIABLES
        totalAdv+=getPieceAdvantage(pos)*materialMultiplier;
        totalAdv+=getMobilityAdvantage(pos)*mobilityMultiplier;
        totalAdv+=getAttackingPiecesAdvantage(pos)*attackingPiecesMultiplier;
        totalAdv+=getCenterControlAdvantage(pos)*centerControlMultiplier;
        totalAdv+=getPawnProtectAdvantage(pos)*pawnStructureMultiplier;
        //CASTLING
        if(pos.getCapitalHasCastled()){ //if you have moved a rook or king and haven't castled, this is not ideal
            totalAdv+=castleAdder;
        }
        if(pos.getLowerCaseHasCastled()){
            totalAdv-=castleAdder;
        }
        //CHECKING
        if(Runner.search.capitalIsInCheck(pos)){
            totalAdv-=checkAdder;
        }else if(Runner.search.lowerCaseIsInCheck(pos)){
            totalAdv+=checkAdder;
        }
        //STALEMATES
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

        int capitalPieceCount = Runner.controlAndSeparation.splitBitboard(Runner.controlAndSeparation.condenseBoard(Runner.controlAndSeparation.getCapitalPieces(pos))).length;
        int lowerCasePieceCount = Runner.controlAndSeparation.splitBitboard(Runner.controlAndSeparation.condenseBoard(Runner.controlAndSeparation.getLowerCasePieces(pos))).length;

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

    //gets value for how many of the opponent's pieces you are attacking
    private int getAttackingPiecesAdvantage(Position pos){
        int totalValue = 0;
        long capitalAttackingSquares = Runner.checkValidConditions.getAttackingSquaresByCasing(pos, 'c');
        long lowerCaseAttackingSquares = Runner.checkValidConditions.getAttackingSquaresByCasing(pos, 'l');
        long capitalPieceSquares = Runner.controlAndSeparation.condenseBoard(Runner.controlAndSeparation.getCapitalPieces(pos));
        long lowerCasePieceSquares = Runner.controlAndSeparation.condenseBoard(Runner.controlAndSeparation.getLowerCasePieces(pos));

        long capitalPiecesAttacked = lowerCaseAttackingSquares | capitalPieceSquares;
        long lowerCasePiecesAttacked = capitalAttackingSquares | lowerCasePieceSquares;

        totalValue -= Runner.controlAndSeparation.splitBitboard(capitalPiecesAttacked).length + Runner.controlAndSeparation.splitBitboard(lowerCasePiecesAttacked).length;

        return totalValue;
    }

    //favors being in the center -> demotes a move which is on the outside (a, b, g, or h files)
    private int getCenterControlAdvantage(Position pos){
        int totalAdvantage = 0;

        //moves in the center are fine. If you move to one of the two outside columns though, it will say this is slightly worse.

        //iterates through lowercase pieces first
        for (int i = 0; i < pos.getCurrentBoard().length/2; i++) {
            Long[] piecesOfType = Runner.controlAndSeparation.splitBitboard(pos.getCurrentBoard()[i]);
            for (int j = 0; j < piecesOfType.length; j++) {
                //iterating through each piece
                if(((Runner.checkValidConditions.aFile & piecesOfType[j]) != 0) || ((Runner.checkValidConditions.hFile & piecesOfType[j]) != 0)){
                    totalAdvantage+=2; // really don't want to be on the outside
                }else if(((Runner.checkValidConditions.bFile & piecesOfType[j]) != 0) || ((Runner.checkValidConditions.gFile & piecesOfType[j]) != 0)){
                    totalAdvantage+=1; // don't want to be on the outside
                }
            }
        }
        //then iterates through the capital pieces
        for (int i = pos.getCurrentBoard().length/2; i < pos.getCurrentBoard().length; i++) {
            Long[] piecesOfType = Runner.controlAndSeparation.splitBitboard(pos.getCurrentBoard()[i]);
            for (int j = 0; j < piecesOfType.length; j++) {
                //iterating through each piece
                if(((Runner.checkValidConditions.aFile & piecesOfType[j]) != 0) || ((Runner.checkValidConditions.hFile & piecesOfType[j]) != 0)){
                    totalAdvantage-=2; // really don't want to be on the outside
                }else if(((Runner.checkValidConditions.bFile & piecesOfType[j]) != 0) || ((Runner.checkValidConditions.gFile & piecesOfType[j]) != 0)){
                    totalAdvantage-=1; // don't want to be on the outside
                }
            }
        }

        return totalAdvantage;
    }

    private int getPawnProtectAdvantage(Position pos){
        int totalAdvantage = 0;

        totalAdvantage += Runner.controlAndSeparation.splitBitboard(Runner.checkValidConditions.getCapitalPawnThreatenedSpaces(pos) & pos.getCurrentBoard()[11]).length; //gets the number of spaces the pawns are threatening their own pawns -> A good thing
        totalAdvantage -= Runner.controlAndSeparation.splitBitboard(Runner.checkValidConditions.getLowerCasePawnThreatenedSpaces(pos) & pos.getCurrentBoard()[5]).length;



        return totalAdvantage;
    }

    private double roundDoubleProperly(double num){
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.HALF_EVEN);
        System.out.println(df.format(num));
        return Double.parseDouble(df.format(num));
    }



}
