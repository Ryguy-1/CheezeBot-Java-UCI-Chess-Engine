package package1;

public class NewBoardEvaluation {

    /*Game states:
        1. Early Game: 0
        2. Mid Game: 1
        3. End Game: 2
     */
    byte gameState = 0;


    //bitboard of pyramid structure pawns at the beginning
    long capitalPawnMoveGoalEarlyGame = Runner.mainBoard.parseLong("0000000000000000000000000000000000011000001111000000000000000000", 2);
    long lowerCasePawnMoveGoalEarlyGame = Runner.mainBoard.parseLong("0000000000000000001111000001100000000000000000000000000000000000", 2);
    //bitboard of expanded pyramid structure pawns mid game
    long capitalPawnMoveGoalMidGame = Runner.mainBoard.parseLong("0000000000000000000000000001100000111100011111100000000000000000", 2);
    long lowerCasePawnMoveGoalMidGame = Runner.mainBoard.parseLong  ("0000000000000000011111100011110000011000000000000000000000000000", 2);
    //bitboard pawns want to move to end game (closer to the other side for promotion) -> Rank before promotion because promotions.
    long capitalPawnMoveGoalEndGame1 = Runner.checkValidConditions.rank7;
    long lowerCasePawnMoveGoalEndGame1 = Runner.checkValidConditions.rank2;
    //
    long capitalPawnMoveGoalEndGame2 = Runner.checkValidConditions.rank6;
    long lowerCasePawnMoveGoalEndGame2 = Runner.checkValidConditions.rank3;
    //
    long capitalPawnMoveGoalEndGame3 = Runner.checkValidConditions.rank5;
    long lowerCasePawnMoveGoalEndGame3 = Runner.checkValidConditions.rank4;
    //
    long capitalPawnMoveGoalEndGame4 = Runner.checkValidConditions.rank4;
    long lowerCasePawnMoveGoalEndGame4 = Runner.checkValidConditions.rank5;
    //
    long capitalPawnMoveGoalEndGame5 = Runner.checkValidConditions.rank3;
    long lowerCasePawnMoveGoalEndGame5 = Runner.checkValidConditions.rank6;
    //
    long capitalPawnMoveGoalEndGame6 = Runner.checkValidConditions.rank2;
    long lowerCasePawnMoveGoalEndGame6 = Runner.checkValidConditions.rank7;
    ///////////////////////






    private static final int checkmateWeight = (Integer.MAX_VALUE/2); //HAS TO BE -1 BECAUSE MIN AND MAX VALUES IN MINIMAX ARE INTEGER.MAX_VALUE. THROWS ERROR OTHERWISE.
    private static final int stalemateWeight = (Integer.MAX_VALUE/4);



    public int getBoardEvaluation(Position pos){
        int totalEvaluation = 0;

        //if one side in checkmate, look no further.
//        if(Runner.search.capitalIsInCheckmate(pos)){
//            return -checkmateWeight;
//        }else if(Runner.search.lowerCaseIsInCheckmate(pos)){
//            return checkmateWeight;
//        }
        //if not in checkmate...

        //totalEvaluation+=getPawnAdvantage(pos); //typically maxes out somewhere around 5 ish. Should be rather constant throughout game states
        totalEvaluation+=getRookAdvantage(pos);





        return totalEvaluation;
    }


    /* Pawn Preferences:
       - Set up good Pawn Structure (using matching bitboards above for pawn structure)
       - Protect their own pieces
       - Attack other pieces as long as they are supported - not implemented yet
     */
    private int getPawnAdvantage(Position pos){
        //order in array: r, n, b, q, k, p, R, N, B, Q, K, P

        int totalPawnAdvantage = 0;

        long capitalPawnAttacks = Runner.checkValidConditions.getCapitalPawnThreatenedSpaces(pos);
        long lowerCasePawnAttacks = Runner.checkValidConditions.getLowerCasePawnThreatenedSpaces(pos);

        //Pawns like to protect their own pieces
        totalPawnAdvantage += Runner.controlAndSeparation.splitBitboard(capitalPawnAttacks&Runner.controlAndSeparation.condenseBoard(Runner.controlAndSeparation.getCapitalPieces(pos))).length
                - Runner.controlAndSeparation.splitBitboard(lowerCasePawnAttacks&Runner.controlAndSeparation.condenseBoard(Runner.controlAndSeparation.getLowerCasePieces(pos))).length;

        //Pawns like to attack other color's pieces just as much, as long as they are supported
        /*
            Do later...
         */



        /*Game States:
            1. Set up small protected pyramid structure
            2. Set up large protected pyramid structure
            3. Push Pawns for promotion
         */
        byte capitalPawnCount = 0;
        byte lowerCasePawnCount = 0;
        if(gameState==0){ //early game
            capitalPawnCount+=Runner.controlAndSeparation.splitBitboard(capitalPawnMoveGoalEarlyGame & pos.getCurrentBoard()[11]).length;
            lowerCasePawnCount+=Runner.controlAndSeparation.splitBitboard(lowerCasePawnMoveGoalEarlyGame & pos.getCurrentBoard()[5]).length;
        }else if(gameState==1){ //mid game
            capitalPawnCount+=Runner.controlAndSeparation.splitBitboard(capitalPawnMoveGoalMidGame & pos.getCurrentBoard()[11]).length;
            lowerCasePawnCount+=Runner.controlAndSeparation.splitBitboard(lowerCasePawnMoveGoalMidGame & pos.getCurrentBoard()[5]).length;
        }else if(gameState==2){ //end game
            /* Get closer to other side for promotion*/
            capitalPawnCount+=Runner.controlAndSeparation.splitBitboard(capitalPawnMoveGoalEndGame1 & pos.getCurrentBoard()[11]).length*3;
            capitalPawnCount+=Runner.controlAndSeparation.splitBitboard(capitalPawnMoveGoalEndGame2 & pos.getCurrentBoard()[11]).length*2;
            capitalPawnCount+=Runner.controlAndSeparation.splitBitboard(capitalPawnMoveGoalEndGame3 & pos.getCurrentBoard()[11]).length;
            capitalPawnCount+=Runner.controlAndSeparation.splitBitboard(capitalPawnMoveGoalEndGame4 & pos.getCurrentBoard()[11]).length;
            capitalPawnCount+=Runner.controlAndSeparation.splitBitboard(capitalPawnMoveGoalEndGame5 & pos.getCurrentBoard()[11]).length;
            capitalPawnCount+=Runner.controlAndSeparation.splitBitboard(capitalPawnMoveGoalEndGame6 & pos.getCurrentBoard()[11]).length;

            lowerCasePawnCount+=Runner.controlAndSeparation.splitBitboard(lowerCasePawnMoveGoalEndGame1 & pos.getCurrentBoard()[5]).length*3;
            lowerCasePawnCount+=Runner.controlAndSeparation.splitBitboard(lowerCasePawnMoveGoalEndGame2 & pos.getCurrentBoard()[5]).length*2;
            lowerCasePawnCount+=Runner.controlAndSeparation.splitBitboard(lowerCasePawnMoveGoalEndGame3 & pos.getCurrentBoard()[5]).length;
            lowerCasePawnCount+=Runner.controlAndSeparation.splitBitboard(lowerCasePawnMoveGoalEndGame4 & pos.getCurrentBoard()[5]).length;
            lowerCasePawnCount+=Runner.controlAndSeparation.splitBitboard(lowerCasePawnMoveGoalEndGame5 & pos.getCurrentBoard()[5]).length;
            lowerCasePawnCount+=Runner.controlAndSeparation.splitBitboard(lowerCasePawnMoveGoalEndGame6 & pos.getCurrentBoard()[5]).length;
        }

        //adds the states where the pawns should be to the total advantage
        totalPawnAdvantage += capitalPawnCount-lowerCasePawnCount;

        return totalPawnAdvantage;

    }

    /*Rook Preferences:
       - Take control of as many squares as possible (largest amount of attacking squares available)
       - Link themselves if possible for potential (Stacked or "attacking" each other)
     */
    private int getRookAdvantage(Position pos){
        //order in array: r, n, b, q, k, p, R, N, B, Q, K, P

        int totalRookAdvantage = 0;
        int linkedValue = 6; //has to outweigh most other lines it would want to take

        int numCapitalRookAttackingSquares = Runner.controlAndSeparation.splitBitboard(Runner.checkValidConditions.getCapitalRookMoves(pos)).length;
        int numLowerCaseRookAttackingSquares = Runner.controlAndSeparation.splitBitboard(Runner.checkValidConditions.getLowerCaseRookMoves(pos)).length;

        totalRookAdvantage+=numCapitalRookAttackingSquares-numLowerCaseRookAttackingSquares;


        //stacked rooks - just want to know if they are in the same column




        //capital rook stack capital rook or capital rook attack capital rook
//        if((Runner.checkValidConditions.getCapitalRookMoves(pos) & pos.getCurrentBoard()[6]) != 0){
//            System.out.println("capital linked rooks");
//            totalRookAdvantage+=linkedValue;
//        }
//        if((Runner.checkValidConditions.getLowerCaseRookMoves(pos) & pos.getCurrentBoard()[0]) != 0){
//            System.out.println("lower case linked rooks");
//            totalRookAdvantage-=linkedValue;
//        }

        return totalRookAdvantage;
    }

}
