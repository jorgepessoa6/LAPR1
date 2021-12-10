/*
 * Projeto de lapr1
 */
package MainPackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Scanner;

/**
 * @author StaticVoid
 */
public class Utilities {

    /**
     * Counts the number of lines of a file
     *
     * @param file file in question
     * @return number of lines of that file, returns -1 and an error message if
     * it does not find the file
     */
    public static int countNumLinesFile(String file) {
        int count = 0;
        Scanner scan;

        try {
            scan = new Scanner(new File(file));

            while (scan.hasNextLine()) {
                scan.nextLine();
                count++;
            }

        } catch (FileNotFoundException ex) {
            System.out.println("\nO ficheiro " + file + " não existe!");
            count = -1;
        }

        return count;
    }

    /**
     * Outputs a column of a double matrix in the form of a double array
     *
     * @param outF formatter to wich output the information
     * @param matrix matrix with the information
     * @param pos position (column) of the matrix
     */
    public static void displayColumnOfDoubleMatrix(Formatter outF, double[][] matrix, int pos) {
        outF.format("(");
        for (int i = 0; i < matrix.length - 1; i++) {
            outF.format("%.3f; ", matrix[i][pos]);
        }
        outF.format("%.3f)", matrix[matrix.length - 1][pos]);
    }

    /**
     * Obtains the first position of the biggest value of an array
     *
     * @param values matrix with the information
     * @return the first index where the biggest value appears
     */
    public static int obtainFirstIndexOfMaxValue(double[] values) {
        double max = obtainMaxValueArray(values);

        int index = 0;

        //means that there are no eigenvalues or they are impossible
        if (max == 0) {
            return -1;
        }

        while (values[index] != max) {
            index++;
        }

        return index;
    }

    /**
     * Obtains the biggest value of an array
     *
     * @param values - matrix with the information
     * @return the biggest value of the array
     */
    public static double obtainMaxValueArray(double[] values) {
        //sets the biggest value to the first index and then compares starting with it
        double max = values[0];
        // Compares each value with the current max
        for (double elem : values) {
            if (elem > max) {
                max = elem;
            }
        }
        return max;
    }

    /**
     * Asks a question a returns the answer, if theres no question, simply
     * outputs "Answer please"
     *
     * @param question question thats being asked
     * @return the answer answered by the user
     */
    public static String askAndRetrieveAnswerStr(String question) {
        Scanner read = new Scanner(System.in);
        if (question.length() < 1) {
            question = "Responda, por favor";
        }

        System.out.print(question);
        return read.nextLine();
    }

    /**
     * Asks a question a return the answer in form of int
     *
     * @param question question thats being asked
     * @return the answer answered by the user, an int
     */
    public static int askAndRetrieveAnswerInt(String question) {
        Scanner read = new Scanner(System.in);
        if (question.length() < 1) {
            question = "Responda, por favor";
        }

        boolean theresError;
        int answerInt = -1;

        do {
            theresError = false;
            System.out.print(question);
            String ans = read.nextLine();

            //verifies if it is indeed an integer
            try {
                answerInt = Integer.parseInt(ans);
            } catch (Exception e) {
                System.out.println("Introduza um número inteiro por favor!");
                theresError = true;
            }

        } while (theresError);

        return answerInt;
    }

    public static float askAndRetrieveAnswerFloat(String question) {
        Scanner read = new Scanner(System.in);

        //if is not introduced any question
        if (question.length() < 1) {
            question = "Responda, por favor";
        }

        boolean theresError;
        float answerFloat = -1;

        do {
            theresError = false;

            System.out.print(question);
            String ans = read.nextLine();

            ans = ans.replaceAll(",", ".");

            //verifies if it is indeed a float
            try {
                answerFloat = Float.parseFloat(ans);
            } catch (Exception e) {
                System.out.println("Introduza um número por favor!");
                theresError = true;
            }

        } while (theresError);

        return answerFloat;
    }

    /**
     * Trim a vector or a line of a matrix
     *
     * @param row vector trimming
     */
    public static void trimAVector(String[] row) {
        for (int column = 0; column < row.length; column++) {
            row[column] = row[column].trim();
        }
    }

    /**
     * Verifies if the string is an int
     *
     * @param str string that will be verified
     * @return boolean that checks if it is an int
     */
    public static boolean verifyIfInt(String str) {
        boolean isInt = true;
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            isInt = false;
        }
        return isInt;
    }

    /**
     * Verifies if the string is an float
     *
     * @param str string that will be verified
     * @return boolean that checks if it is an int
     */
    public static boolean verifyIfFloat(String str) {
        str = str.replaceAll(",", ".");

        boolean isFloat = true;
        try {
            Float.parseFloat(str);
        } catch (NumberFormatException e) {
            isFloat = false;
        }
        return isFloat;
    }

    /**
     * Calculates a single element of a multiplication of matrices
     *
     * @param row row of that element
     * @param col column of that element
     * @param matrix1 first matrix that is being multiplied
     * @param matrix2 second matrix that is being multiplied
     * @return the element in the row and column chosen of the resultant of the
     * multiplication of the matrices
     */
    public static double calcElemMatrixMultiplying(int row, int col, double[][] matrix1, double[][] matrix2) {
        double res = 0;
        for (int i = 0; i < matrix1.length; i++) {
            res += (matrix1[row][i] * matrix2[i][col]);
        }
        return res;
    }

    /**
     * Multiplies 2 matrices of double, the order matters!! If the operation is
     * impossible displays a message and returns the first matrix
     *
     * @param matrix1 first matrix
     * @param matrix2 second matrix
     * @return a matrix with the multiplication done
     */
    public static double[][] multiplyMatrices(double[][] matrix1, double[][] matrix2) {
        if (matrix1[0].length != matrix2.length) {
            System.out.println("A multiplicação dessas matrizes é impossível");
            return matrix1;
        }

        //e.g. a 3x2  x  2x4 results in a 3x4 matrix
        double[][] res = new double[matrix1.length][matrix2[0].length];

        for (int row = 0; row < res.length; row++) {
            for (int col = 0; col < res[0].length; col++) {
                res[row][col] = calcElemMatrixMultiplying(row, col, matrix1, matrix2);
            }
        }

        return res;
    }

    /**
     * Writes a string a determined number of times, good for "-----" or
     * "__________" or "##########"
     *
     * @param outF formatter that states where to ooutput to
     * @param str the string to repeat
     * @param nTimes the number of times to repeat
     */
    public static void writeStrXTimes(Formatter outF, String str, int nTimes) {
        for (int i = 0; i < nTimes; i++) {
            outF.format("%s", str);
        }
        outF.format("%n");
    }

    /**
     * Gets the biggest length an elem has in a double[][] array, rounding each
     * one wich means it does not count the decimal counterpart
     *
     * @param matrix array in wich we are iterating
     * @return the biggest length
     */
    public static int getBiggestLengthInMatrix(double[][] matrix) {
        int length;
        //starts the biggestLength at 0, because for sure it will be bigger than 0
        int biggestLength = 0;

        //iterates thorugh array and gets the length of each elem
        for (double[] row : matrix) {
            for (double elem : row) {
                //rounds the number
                long roundedElem = Math.round(elem);
                //and gets the number of digits
                length = String.valueOf(roundedElem).length();

                //if the length is bigger than the previous bigger (starting at 0) the new biggest length is set
                if (length > biggestLength) {
                    biggestLength = length;
                }
            }
        }

        return biggestLength;
    }

    /**
     * Gets the biggest length an elem can have in an array of double[][]
     * counting the decimal counterpart and the dot
     *
     * @param matrix array in wich we are iterating
     * @return the biggest length
     */
    public static int getBiggestLengthInMatrixWithDecimals(double[][] matrix) {
        int length;
        int biggestLength = 0;

        //iterates thorugh array and gets the length of each elem
        for (double[] row : matrix) {
            for (double elem : row) {
                length = String.valueOf(elem).length();

                //if the length is bigger than the previous bigger (starting at 0) the new biggest length is set
                if (length > biggestLength) {
                    biggestLength = length;
                }
            }
        }

        return biggestLength;
    }

    /**
     * Finds the current date and concatenates it in a string lika ddmmyyyy
     *
     * @return the date
     */
    public static String getCurrentDatedmy() {
        String day = Integer.toString(Calendar.getInstance().get(Calendar.DATE));
        String month = Integer.toString(Calendar.getInstance().get(Calendar.MONTH) + 1);
        String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));

        //for example, if the day is 3, will pass to 03, the same to the months
        if (day.length() < 2) {
            day = "0" + day;
        }
        if (month.length() < 2) {
            month = "0" + month;
        }

        return (day + month + year);
    }

    /**
     * rounds a number to three decimal places
     *
     * @param number number to round
     * @return rounded number
     */
    public static double roundDoubleThreeDecimalPlaces(double number) {
        return (double) Math.round(number * 1000) / 1000;
    }

    /**
     * Converts a column matrix in an array
     *
     * @param matrix colum matrix
     * @return the array
     */
    public static double[] convertColumnMatrixToArray(double[][] matrix) {
        double[] res = new double[matrix.length];

        for (int col = 0; col < matrix.length; col++) {
            res[col] = matrix[col][0];
        }

        return res;
    }

    /**
     * Shows a colum matrix (a matrix wich has only one column)
     *
     * @param matrix matrix to show
     * @param out formatter that states where to show it
     */
    public static void showColumnMatrix(double[][] matrix, Formatter out) {
        out.format("[ ");
        for (int row = 0; row < matrix.length - 1; row++) {
            out.format("%.3f; ", matrix[row][0]);
        }
        out.format("%.3f ]%n", matrix[matrix.length - 1][0]);
    }

    /**
     * Gets the |X| being X the norm of a vector
     *
     * @param vector vector to find the norm
     * @return |X|
     */
    public static double getAbsVectorNorm(double[] vector) {
        double norm;

        //gets the number inside the square root
        double insideSqrrt = 0;
        for (double elem : vector) {
            insideSqrrt += Math.pow(elem, 2);
        }

        //square roots it and returns
        norm = Math.sqrt(insideSqrrt);
        return norm;
    }

    /**
     * Normalizes a vector
     *
     * @param vector vector to normalize
     * @return returns the normalized vector
     */
    public static double[] normalizeVector(double[] vector) {
        //gets the norm of the vector
        double norm = getAbsVectorNorm(vector);

        double[] res = new double[vector.length];

        //divides each element by the norm of the vector to get the normalised one
        for (int elem = 0; elem < vector.length; elem++) {
            res[elem] = (double) vector[elem] / norm;
        }

        return res;
    }
}
