import java.util.Scanner;

public class ClassroomOrganizer {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in); // Take in user input
        int rows = 0;
        int cols = 0;
        boolean validInput;
        do {
            rows = Integer.parseInt(args[0]); // Get command line argument 1 (rows)
            cols = Integer.parseInt(args[1]); // Get command line argument 2 (columns)
            validInput = true; // Assuming input is valid
        } while (!validInput);
        int maxNum = rows * cols; // 2
        int[] currentNum = {0};
        if (rows <= 0 || cols <= 0) { // The classroom size cannot be 0 and cannot be (-). int will always be inputted.
            System.out.println("Invalid classroom size"); // Print invalid statement.
            System.exit(0); // Completely exit the program.
        }
        String[][] outputArray = ClassroomOrganizer.mapClassroom(rows, cols); // Make our initial array using our
        // command line arguments(this is the array of our classroom)
        String expression; // Initialize value that we will be given by user
        System.out.print("> "); // Prompt for user to type
        while ((expression = input.nextLine()) != null) {
            String[] elements = expression.split(" "); // Split up the elements, get rid of spaces.
            // As long as the user enters something, we read it.
            // We use a while loop because we don't want the program to end without the user interaction
            switch (elements[0]) {
                case "print": // If the user commands us to print:
                    ClassroomOrganizer.printFormattedArray(outputArray); // print that specifc array
                    System.out.print("> "); // print prompt
                    break; // Go back into the while loop to prompt for another command
                case "quit": // If the user wants to quit, we leave the entire program.
                    System.exit(0);
                    break;
                case "go": // If the user starts their command with go
                    int arg1 = Integer.parseInt(elements[2]); // Get row number
                    int arg2 = Integer.parseInt(elements[3]); // Get column number
                    // System.out.println(arg1 + "\n" + arg2);
                    String name = elements[4]; // Get name
                    outputArray = ClassroomOrganizer.handleGo(outputArray, arg1, arg2, name, currentNum, maxNum);
                    System.out.print("> ");
                    break;
                case "move":
                    String direction = elements[1]; // Gives us the direction we want to move in
                    String nameMove = elements[2];
                    outputArray = ClassroomOrganizer.handleMove(outputArray, direction, nameMove);
                    break;
                default:
                    break;
            }
        }
    }

    public static String[][] mapClassroom(int row, int col) {
        String[][] outputArray = new String[row][col]; // Create array that we will output
        for (int i = 0; i < outputArray.length; i++) {
            for (int j = 0; j < outputArray[i].length; j++) {
                outputArray[i][j] = " ";
            }
        }
        return outputArray;
    }

    public static void printFormattedArray(String[][] outputArray) {
        int[] maxLengths = new int[outputArray[0].length];
        for (String[] row : outputArray) { // Find the biggest name
            for (int i = 0; i < row.length; i++) {
                maxLengths[i] = Math.max(maxLengths[i], row[i] != null ? row[i].length() : 0);
            }
        }
        for (int i = 0; i < maxLengths.length; i++) { // Regardless make sure the column has width 3
            maxLengths[i] = Math.max(1, maxLengths[i]);
        }
        printBorder(maxLengths); // top
        for (int j = 0; j < outputArray.length; j++) {
            System.out.print("#");
            for (int i = 0; i < outputArray[j].length; i++) {
                printCentered(outputArray[j][i], maxLengths[i]);
                System.out.print("#");
            }
            System.out.println();
            if (j < outputArray.length - 1) { // Separator row
                printBorder(maxLengths);
            }
        }
        printBorder(maxLengths); // bottom
    }

    public static void printBorder(int[] maxLengths) {
        // if max lengths is equal to 3
        for (int length : maxLengths) {
            System.out.print("#" + "#".repeat(length + 2));  // add 2 for spaces on sides
        }
        System.out.println("#");  // Closing border and new line
    }

    public static void printCentered(String str, int length) {
        if (str == null || str.isEmpty()) {
            System.out.print("#");
        } else {
            int padding = length - str.length();
            int padStart = padding / 2 + 1;  // add 1 for space
            int padEnd = padding - padStart + 2;  // add 2 for space and the extra space if odd
            System.out.print(" ".repeat(padStart) + str + " ".repeat(padEnd));
        }
    }

    public static String[][] handleGo(String[][] outputArray, int row, int column,
                                      String name, int[] currentNum, int maxNum) {
        name = name; // This makes it the format we want, but now we need to put it in the column
        // System.out.print("inside handleGo");
        // NEED to check if name is there previously
        int currentX = -1;
        int currentY = -1;
        // boolean notThere = false;
        for (int i = 0; i < outputArray.length; i++) {
            for (int j = 0; j < outputArray[i].length; j++) {
                if (outputArray[i][j].equals(name)) {
                    currentX = i;
                    currentY = j;
                    outputArray[i][j] = " ";
                    currentNum[0] = currentNum[0] - 1;
                    break;
                }
            }
            if (currentX != -1 && currentY != -1) {
                break;
            }
        }

        if (row < 0 || column < 0) {
            System.out.println("Invalid location");
        } else if (row > outputArray.length - 1 || column > outputArray[0].length - 1) {
            System.out.println("Invalid location");
        } else if (outputArray[row][column].equals(" ")) {
            outputArray[row][column] = name;
            currentNum[0] = currentNum[0] + 1;
            //System.out.println(currentNum[0]);
            if (currentNum[0] == maxNum) {
                System.out.println("Classroom is full");
                printFormattedArray(outputArray);
                System.exit(0);
            }
        } else if (outputArray[row][column].equals(name)) {
            System.out.println(name + " is already there.");
        } else {
            System.out.println(outputArray[row][column] + " is there");
        }

        //System.out.print("end of handleGo");
        return outputArray;
    }

    public static String[][] handleMove(String[][] outputArray, String direction, String name) {
        int currentX = -1;
        int currentY = -1;

        for (int i = 0; i < outputArray.length; i++) {
            for (int j = 0; j < outputArray[i].length; j++) {
                if (outputArray[i][j].equals(name)) {
                    currentX = i;
                    currentY = j;
                    break;
                }
            }
            if (currentX != -1 && currentY != -1) {
                break;
            }
        }

        if (currentX != -1 && currentY != -1) {
            switch (direction) {
                case "up":
                    if (currentX - 1 < 0) {
                        System.out.println("Invalid location");
                    } else if (outputArray[currentX - 1][currentY].equals(" ")) {
                        outputArray[currentX - 1][currentY] = name;
                        outputArray[currentX][currentY] = " ";
                        //System.out.print("> ");
                    } else {
                        System.out.println(outputArray[currentX - 1][currentY] + " is there");
                    }
                    break;
                case "down":
                    if (currentX + 1 >= outputArray.length) {
                        System.out.println("Invalid location");
                    } else if (outputArray[currentX + 1][currentY].equals(" ")) {
                        outputArray[currentX + 1][currentY] = name;
                        outputArray[currentX][currentY] = " ";
                    } else {
                        System.out.println(outputArray[currentX + 1][currentY] + " is there");
                        //System.out.print("> ");
                    }
                    break;
                case "right":
                    if (currentY + 1 >= outputArray[0].length) {
                        System.out.println("Invalid location");
                        //System.out.print("> ");
                    } else if (outputArray[currentX][currentY + 1].equals(" ")) {
                        outputArray[currentX][currentY + 1] = name;
                        outputArray[currentX][currentY] = " ";
                        //System.out.print("> ");
                    } else {
                        System.out.println(outputArray[currentX][currentY + 1] + " is there");
                        //System.out.print("> ");
                    }
                    break;
                case "left":
                    if (currentY - 1 < 0) {
                        System.out.println("Invalid location");
                    } else if (outputArray[currentX][currentY - 1].equals(" ")) {
                        outputArray[currentX][currentY - 1] = name;
                        outputArray[currentX][currentY] = " ";
                        //System.out.print("> ");
                    } else {
                        System.out.println(outputArray[currentX][currentY - 1] + " is there");
                    }
                    break;
                default:
                    break;
            }
            System.out.print("> ");
        } else {
            System.out.println(name + " is not in the classroom");
            System.out.print("> ");
        }
        return outputArray;
    }
    // Make method that looks at one column and looks at the longest name and gives it back
    // Do this in loop to see where longest name is
    // Then we align column to match and we can set size of array
    // 1. Find longest name
    // 2. add spaces to all the names
    // 3. Adjust every row in the column with the longest name
    // 4. Place the # on every other row
    // 5. place the # in appropriate column space

}


