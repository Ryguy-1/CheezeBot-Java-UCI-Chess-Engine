package package1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Openings {

    public static final String[][] openingsWithNames = {
            {"Slav Defense", "d2d4 d7d5 c2c4 c7c6"},
            {"London System", "d2d4 d7d5 c1f4 g8f6 g1f3"},
            {"Sicilian Defence", "e2e4 c7c5"},
            {"Sicilian Dragon", "e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6"}, //just for testing -> should add in future though because cool
    };
}





























//    public static final String bookFilepath = "openings.csv"; //   src\openings\openings.csv   -> use when testing not with jar file.
//    ArrayList
//
//
//    Openings() {
//    try {
//        BufferedReader br = new BufferedReader(new FileReader(bookFilepath));
//
//        String line;
//        while ((line = br.readLine()) != null) {
//            String[] values = line.split(", ");
//            openingsWithNames
//        }
//
//    }catch(IOException e){
//        System.out.println("Error opening CSV Sheet");
//    }
//
////
//        for (int i = 0; i < openingLines.size(); i++) {
//            System.out.println(openingLines.get(i));
//        }

//}