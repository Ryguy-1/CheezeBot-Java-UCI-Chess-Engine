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
    long capitalPawnMoveGoalEndGame = Runner.checkValidConditions.rank7;
    long lowerCasePawnMoveGoalEndGame = Runner.checkValidConditions.rank2;






    private static final int checkmateWeight = (Integer.MAX_VALUE/2); //HAS TO BE -1 BECAUSE MIN AND MAX VALUES IN MINIMAX ARE INTEGER.MAX_VALUE. THROWS ERROR OTHERWISE.
    private static final int stalemateWeight = (Integer.MAX_VALUE/4);



    public int getBoardEvaluation(Position pos){
        int totalEvaluation = 0;

        //if one side in checkmate, look no further.
        if(Runner.search.capitalIsInCheckmate(pos)){
            return -checkmateWeight;
        }else if(Runner.search.lowerCaseIsInCheckmate(pos)){
            return checkmateWeight;
        }
        //if not in checkmate...

        totalEvaluation+=getPawnAdvantage(pos);






        return totalEvaluation;
    }


    /* Pawn Preferences:
       1. Protect themselves and other pieces
       2. Attack other pieces as long as they are supported
       3. Get to the other side of the board in endgame
     */
    private int getPawnAdvantage(Position pos){
        //order in array: r, n, b, q, k, p, R, N, B, Q, K, P

        int totalPawnAdvantage = 0;

        long capitalPawnAttacks = Runner.checkValidConditions.getCapitalPawnThreatenedSpaces(pos);
        long lowerCasePawnAttacks = Runner.checkValidConditions.getLowerCasePawnThreatenedSpaces(pos);
        long allPieces = Runner.controlAndSeparation.condenseBoard(pos.getCurrentBoard());

        //Pawns like to protect their own pieces
        totalPawnAdvantage = Runner.controlAndSeparation.splitBitboard(capitalPawnAttacks&Runner.controlAndSeparation.condenseBoard(Runner.controlAndSeparation.getCapitalPieces(pos))).length
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
            
        }


        return totalPawnAdvantage;

    }






}
