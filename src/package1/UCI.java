package package1;

import java.util.ArrayList;
import java.util.Scanner;

public class UCI {
    Thread t1; // for general communication
    public static final String engineName = "CheezeBot Beta";
    public static final String creditName = "Ryland";
    public static final String version = "0.40";
    public static boolean engineRunning;

    public static int lookAhead = 4;

    public static boolean isCapitalTurn = true;

    UCI() {
        System.out.println(engineName + " by " + creditName + " version " +version);
        initiateCommunication();
        engineRunning = true;
    }

    private void initiateCommunication() {
        t1 = new Thread(() -> {
            while (engineRunning) {
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();

                if (input.equals("isready")) { //used to make sure engine is still alive, to ping it, and just to make sure the engine and gui are synchronized.
                    inputIsReady();
                }else if (input.equals("ucinewgame")) {
                    inputUCINewGame();
                    inputIsReady(); // testing with lichess
                }else if (input.equals("uci")) { //first command sent after bootup to the engine
                    inputUCI();
//              }if (input.startsWith("setoption") || input2.startsWith("setoption")) { //not using yet
//                    inputSetOption(input);
                }else if (input.startsWith("position")) {
                    inputPosition(input);
                }else if (input.equals("go")) {
                    inputGo();
                }else if(input.equals("quit")){
                    quit();
                }else if (input.equals("drawBoard")) {
                    drawMainBoard();
                }
            }
        });

        t1.start();
    }

    private void inputUCI() {
        //tells the engine the id of the engine
        System.out.println("id name " + engineName);
        System.out.println("id author " + creditName);
        //options go here
        //prints uciok to acknowledge the uci mode. If this is not sent within a certain time period, the engine will be killed by the GUI.
        System.out.println("uciok");
    }

    private void inputIsReady() {
        //if engine needs the GUI to wait for it, it should do the task, then return readyok afterwards.
        System.out.println("readyok");
    }


    private void inputUCINewGame() {
        //what you call when you want to start a new game. Clear hash table, etc.
        lookAhead = 4;
        isCapitalTurn = true;
        Runner.mainBoard.initializeNewBoard();
    }

    private void inputPosition(String input) {
        if(input.contains("startpos")){
            // if is the first move (capital move first
            if(input.equals("position startpos")){
                inputGo();
                return;
            }

            String[] moves = input.substring(24).split(" ");
            inputUCINewGame();
            for (String move: moves){
                Runner.mainBoard.mainPosition.fromToMove(move);
                isCapitalTurn = !isCapitalTurn;
            }

            //just for now am calling input go because it is not telling me to go for whatever reason. Testing purposes...
            inputGo();

        }else if(input.contains("fen")){
            //initialize from a certain location/tell the engine to start from a specific place. Will implement later on.
        }
    }

    private void inputGo(){
        //search for the best move. May put this into a new thread? not sure yet.

        if(Runner.controlAndSeparation.splitBitboard(Runner.controlAndSeparation.condenseBoard(Runner.mainBoard.mainPosition.getCurrentBoard())).length<10){
            lookAhead = 6;
        }

        //computer turn as lower case
        Position returnedPosition = Runner.minimax.minimax(Runner.mainBoard.mainPosition, lookAhead, isCapitalTurn, Minimax.MIN, Minimax.MAX);
        String bestMoveFound = returnedPosition.getMovesToCurrent().get(0);
        Runner.mainBoard.mainPosition.fromToMove(bestMoveFound);
        Runner.mainBoard.mainPosition.addMove(bestMoveFound);
        isCapitalTurn = !isCapitalTurn;
        System.out.println("bestmove " + bestMoveFound);
//        drawMainBoard();
    }

    private void quit(){
        engineRunning = false; //stop the engine from receiving commands
        System.exit(0); //exit the program
    }

    private void drawMainBoard(){
        Runner.mainBoard.drawGameBoard(Runner.mainBoard.mainPosition.getCurrentBoard());
    }



}

