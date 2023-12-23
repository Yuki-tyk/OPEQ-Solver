import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OPEQSolver {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // reand the boxes and qpoints files, and convert those String list into Double ArrayList
        ArrayList<ArrayList<Double>> boxes_D = listStringnToListDouble(readFile("boxes.csv"));
        ArrayList<ArrayList<Double>> qpoints_D = listStringnToListDouble(readFile("query-points.csv"));
        
        // declare a 2D ArrayList for the results
        ArrayList<ArrayList<Integer>> results = new ArrayList<>();

        // get the number of dimensions of the boxes and qpoints
        int numOfDimensions = qpoints_D.get(0).size();

        int choice = 0;

        // keep looping until the user choose to exit (choice == 4)
        while (choice != 3 && choice != 4) {
            // display menu
            displayMenu();

            // get user input
            try {
                choice = Integer.parseInt(input.nextLine());

                // process user input
                switch (choice) {
                    case 1:
                        visualize2DInputs(boxes_D, qpoints_D, numOfDimensions);
                        break;
                    
                    case 2:
                        results = OPEQSegmentTree(boxes_D, qpoints_D, numOfDimensions);
                        break;

                    case 3:
                        saveToCSV(results);
                        break;
                    
                    case 4:
                        System.out.println("Goodbye!");
                        break;
                    
                    default:
                        System.out.println("The user input is out of range. Please input an integer between 1 and 4.");
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("The user input has to be an integer.");
            }

            System.out.println();
            
        }
    
        input.close(); // closing the scanner object
    }

    // read the input files
    private static List<String> readFile(String fileName) {
        List<String> temp = new ArrayList<String>();
        try {
            File myFile = new File(fileName);
            Scanner inputFile = new Scanner(myFile);
            while (inputFile.hasNextLine()) {
                String data = inputFile.nextLine();
                temp.add(data);
            }
            inputFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
        return temp;
    }

    // turn a list of string to a list of list of double for boxes and qpoints
    private  static ArrayList<ArrayList<Double>> listStringnToListDouble(List<String> listStrings) {
        ArrayList<ArrayList<Double>> listOfListOfDouble = new ArrayList<>();
        String[] listString;
        
        // loop through all the rows
        for (int i = 0; i < listStrings.size(); i++) {
            ArrayList<Double> listOfDouble = new ArrayList<>(); // Create a new row object for each row
            listString = listStrings.get(i).split(",");

            // loop through all the columns
            for (String s : listString) {
                listOfDouble.add(Double.parseDouble(s));
            }
            listOfListOfDouble.add(listOfDouble); // Add the row to the arrayList
        }
        return listOfListOfDouble;
    }

    // display menu
    private static void displayMenu() {
        System.out.println("Welcome to Orthogonal Point Enclosure Query Problem Solver!");
        System.out.println("1. Visualize 2D Inputs");
        System.out.println("2. Solve OPEQ Problem by Segment Tree");
        System.out.println("3. Save OPEQ results to file");
        System.out.println("4. Exit");
        System.out.println();
        System.out.print("Please select an option: ");
    }

    // choice == 1: Visualize 2D Inputs
    private static void visualize2DInputs(ArrayList<ArrayList<Double>> boxes_D, ArrayList<ArrayList<Double>> qpoints_D, int numOfDi) {
        if (numOfDi != 2) {
            System.out.println("Visualisation only available for 2D inputs.");
            return;
        }
        GUI gui = new GUI(boxes_D, qpoints_D);
        gui.setVisible(true);
    }

    // choice == 2: solve OPEQ problem by segment tree
    private static ArrayList<ArrayList<Integer>> OPEQSegmentTree (ArrayList<ArrayList<Double>> boxes_D, ArrayList<ArrayList<Double>> qpoints_D, int numOfDi) {
        ArrayList<ArrayList<Integer>> results = new ArrayList<>();
        ArrayList<Double> qpoint;
        ArrayList<Double> box;
        int temp;
        boolean toAdd;

        // loop through all the query points
        for (int i = 0; i < qpoints_D.size(); i++) {
            ArrayList<Integer> row = new ArrayList<>(); // Create a new row object for each row
            qpoint = qpoints_D.get(i);

            // loop through all the boxes
            for (int j = 0; j < boxes_D.size(); j++) {
                toAdd = true;
                box = boxes_D.get(j);
                temp = 0;

                // loop through all the dimensions
                for (int k = 0; k < numOfDi; k++) {
                    if ((qpoint.get(k) < box.get(temp)) || (qpoint.get(k) > box.get(temp + 1))) {
                        toAdd = false;
                        break;
                    }
                    temp += 2;
                }

                if (toAdd) {
                    row.add(j+1);
                }
            }
            results.add(row); // Add the row to the arrayList
        }

        return results;
    }

    // choice == 3: Save OPEQ results to file
    private static void saveToCSV(ArrayList<ArrayList<Integer>> results) {
        try {
            FileWriter outputFile = new FileWriter("results.txt");
            for (int i = 0; i < results.size(); i++) {
                outputFile.write("Query Point " + (i + 1) + ":\n");
                for (int num : results.get(i)) {
                    outputFile.write(num + "\n");
                }
            }
            System.out.println("File saved successfully");
            outputFile.close();
        
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}