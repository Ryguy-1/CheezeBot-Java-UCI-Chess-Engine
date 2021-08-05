package package1;

import java.util.Scanner;

public class UCI {
    Thread t1;
    public static final String engineName = "CheezeBot Beta";
    public static final String creditName = "Ryland";
    public static final String version = "0.11";
    public static boolean engineRunning;

    public static boolean isReadyOk = true;

    public static final int lookAhead = 1;

    UCI() {
        initiateCommunication();
        engineRunning = true;
    }

    private void initiateCommunication() {
//        t1 = new Thread(() -> {
//            Scanner scanner = new Scanner(System.in);
//
//            //draw initial game board
//            Runner.mainBoard.drawGameBoard(Runner.mainBoard.mainPosition.getCurrentBoard());
//
//            while(engineRunning){
//                if(Runner.search.capitalIsInCheckmate(Runner.mainBoard.mainPosition)){
//                    engineRunning = false;
//                    System.out.println("Capital Is In Checkmate!! Good Game");
//                    break;
//                } else if (Runner.search.lowerCaseIsInCheckmate(Runner.mainBoard.mainPosition)) {
//                    engineRunning = false;
//                    System.out.println("Lower Case Is In Checkmate!! Good Game");
//                    break;
//                }
//                    //if there are no checkmates
//                    //my turn as capital (have to make valid moves because don't need to check if my move as a user is valid or not for uci)
//                    System.out.println();
//                    System.out.print("Move FromTo (Algebraic Notation): ");
//                    String fromTo = scanner.nextLine();
//                    Runner.mainBoard.mainPosition.fromToMove(fromTo);
//                    //print board after my move
//                    Runner.mainBoard.drawGameBoard(Runner.mainBoard.mainPosition.getCurrentBoard());
//                    System.out.println();
//                if(Runner.search.capitalIsInCheckmate(Runner.mainBoard.mainPosition)){
//                    engineRunning = false;
//                    System.out.println("Capital Is In Checkmate!! Good Game");
//                    break;
//                } else if (Runner.search.lowerCaseIsInCheckmate(Runner.mainBoard.mainPosition)) {
//                    engineRunning = false;
//                    System.out.println("Lower Case Is In Checkmate!! Good Game");
//                    break;
//                }
//                    //computer turn as lower case
//                    Position newPos = Runner.minimax.minimax(Runner.mainBoard.mainPosition, lookAhead, false, Minimax.MIN, Minimax.MAX);
//                    Runner.mainBoard.mainPosition.fromToMove(newPos.getMovesToCurrent().get(0));
//                    //print logic train
//                    System.out.println(newPos.getMovesToCurrent());
//                    //Print board for computer move
//                    Runner.mainBoard.drawGameBoard(Runner.mainBoard.mainPosition.getCurrentBoard());
//
//            }
//
//        });
//        t1.start();

        t1 = new Thread(() -> {
            while (engineRunning) {
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                if (input.equals("uci")) { //first command sent after bootup to the engine
                    inputUCI();
                }
//                else if (input.startsWith("setoption")) { //not using yet
//                    inputSetOption(input);
//                }
                else if (input.equals("isready")) { //used to make sure engine is still alive, to ping it, and just to make sure the engine and gui are synchronized.
                    inputIsReady();
                } else if (input.equals("ucinewgame")) {
                    inputUCINewGame();
                } else if (input.startsWith("position")) {
                    inputPosition(input);
                } else if (input.equals("go")) {
                    inputGo();
                }else if(input.equals("quit")){
                    quit();
                } else if (input.equals("drawBoard")) {
                    drawMainBoard();
                }


            }
        });
        t1.start();
        // TEMP FOR TESTING!
        inputGo();

    }

    private void inputUCI() {
        //tells the engine the id of the engine
        System.out.println("id name " + engineName);
        System.out.println("id author " + creditName);
        //options go here
        //prints uciok to acknowledge the uci mode. If this is not sent within a certain time period, the engine will be killed by the GUI.
        System.out.println("uciok");
    }

//    private void inputSetOption(String input){
//        //apparently arena can tell you what parameters to modify if you say you want it set up a certain way and it kicks back information. not doing right now.
//    }

    private void inputIsReady() {
        //if engine needs the GUI to wait for it, it should do the task, then return readyok afterwards.
        while(!isReadyOk){
            if(isReadyOk){
                System.out.println("readyok");
                return;
            }
        }System.out.println("readyok");
    }

    private void inputUCINewGame() {
        //what you call when you want to start a new game. Clear hash table, etc.
        System.out.println("Starting new game with " + engineName + ". Version: " + version);
        Runner.mainBoard.initializeNewBoard();
    }

    private void inputPosition(String input){
        if(input.contains("startpos")){
            //the last four terms of startpos is the last move that was made
            String playerMove = input.substring(input.length()-5);
            if(playerMove.charAt(0)==' '){
                playerMove = playerMove.substring(1);
            }
            Runner.mainBoard.mainPosition.fromToMove(playerMove);
            //just for now am calling input go because it is not telling me to go for whatever reason. Testing purposes...
            inputGo();
        }else if(input.contains("fen")){
            //initialize from a certain location/tell the engine to start from a specific place. Will implement later on.
        }
    }

    private void inputGo(){
        //search for the best move. May put this into a new thread? not sure yet.
        //computer turn as lower case
        Position returnedPosition = Runner.findMove.findBestPosition(Runner.mainBoard.mainPosition, lookAhead, false, Minimax.MIN, Minimax.MAX);
        String bestMoveFound = returnedPosition.getMovesToCurrent().get(0);
        Runner.mainBoard.mainPosition.fromToMove(bestMoveFound);
        System.out.println("bestmove " + bestMoveFound);
        drawMainBoard();
    }

    private void quit(){
        engineRunning = false; //stop the engine from receiving commands
        System.exit(0); //exit the program
    }

    private void drawMainBoard(){
        Runner.mainBoard.drawGameBoard(Runner.mainBoard.mainPosition.getCurrentBoard());
    }



}

