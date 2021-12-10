/*
 * Projeto de lapr1
 */
package MainPackage;

import java.util.Formatter;

import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.decomposition.EigenDecompositor;

/**
 * @author StaticVoid
 */
public class EigenOps {

    /**
     * Decomposes a matrix of doubles into eigen values and vectors Matrix
     * objects
     *
     * @param mat matrix with the information
     * @return an array of Matrix objects corresponding to the eigen values and
     * vectors
     */
    public static Matrix[] decomposeMatrix(double[][] mat) {
        // Creates object of Matrix type
        Matrix object = new Basic2DMatrix(mat);
        //Obtains eigen values and vectors matrices via "Eigen Decomposition"
        EigenDecompositor eigenD = new EigenDecompositor(object);
        return eigenD.decompose();
    }

    /**
     * Obtains the eigen values of a decomposed matrix of doubles
     *
     * @param decomposed Matrix object with the information
     * @return an array with the eigen values of the matrix
     */
    public static double[] obtainEigenValues(Matrix[] decomposed) {
        // Converts Matrix object corresponding to the eigen values to a java array
        double[][] matValues = decomposed[1].toDenseMatrix().toArray();
        double[] values = new double[matValues.length];
        for (int i = 0; i < matValues.length; i++) {
            values[i] = matValues[i][i];
        }
        return values;
    }

    /**
     * Obtains the eigen vectors of a decomposed matrix of doubles
     *
     * @param decomposed Matrix object with the information
     * @return a matrix with the eigen vectors of the initial matrix
     */
    public static double[][] obtainEigenVectors(Matrix[] decomposed) {
        // Converts Matrix object corresponding to the eigen vectors to a java matrix
        return decomposed[0].toDenseMatrix().toArray();
    }

    /**
     * Obtains the centralities of the eigen vectors of a decomposed matrix of
     * doubles
     *
     * @param decomposed Matrix object with the information
     * @return an array with the centralities of the eigen vectors
     */
    public static double[] obtainEigenVectorsCentralities(Matrix[] decomposed) {
        double[] matValues = obtainEigenValues(decomposed);
        double[][] matVectors = obtainEigenVectors(decomposed);

        int column = Utilities.obtainFirstIndexOfMaxValue(matValues);
        //the method above returns -1 when it is impossible to calculate
        if (column == -1) {
            System.out.println("Impossível calcular as centralidades");
            return null;
        }

        double[] centralities = new double[matVectors.length];
        for (int i = 0; i < matVectors.length; i++) {
            centralities[i] = Math.abs(matVectors[i][column]);
        }
        return centralities;
    }

    /**
     * Outputs the eigen vectors centralities of a matrix of doubles to a
     * predetermined output
     *
     * @param out Formatter to wich output
     * @param decomposed array of Matrix objects with the information
     * @param nodesInfo array with the names of the nodes
     * @param isRecursive boolean that states if the program is recursive at this point
     */
    public static void outputEigenVectorsCentralities(Formatter out, String[][] nodesInfo, Matrix[] decomposed, boolean isRecursive) {
        double[] centralities = obtainEigenVectorsCentralities(decomposed);
        // Displays the centralities array
        if (centralities != null) {
            out.format("%nAs centralidades dos nós são:%n%n");
            for (int i = 0; i < centralities.length; i++) {
                //out.format("s%3d %30s : %.3f%n", i + 1, "(" + names[i] + ")", centralities[i]);
                out.format("%4s: %.3f - %s%n", "s" + (i + 1), centralities[i], "(" + nodesInfo[i][Configurations.NODES_FILE_COLUMN_NAME] + ")");
            }
        }

        //shows the webpage with better centrality
        if (!isRecursive) {
            NetworkAnalysis.getAndOpenWebPage(centralities, nodesInfo);
        }
    }

    /**
     * Outputs the eigen values and vectors of a matrix of doubles to a
     * predetermined output
     *
     * @param out Formatter to wich output
     * @param decomposed array of Matrix objects with the information
     */
    public static void outputEigenValuesAndVectors(Formatter out, Matrix[] decomposed) {
        out.format("%nValores Próprios : Vetores Próprios%n%n");
        double[] values = obtainEigenValues(decomposed);
        double[][] vectors = obtainEigenVectors(decomposed);

        //counts the number of different values just for printing purpouse
        int counter = 1;
        for (int i = 0; i < vectors.length; i++) {
            //ignores the 0s bc they are not eigenvalues
            if (values[i] != 0) {
                //writes the eigen value
                out.format("  %dº => %.3f : ", counter, values[i]);
                //writes the vectors
                showAllVectorsForAGivenEigenValue(out, values, vectors, i);
                counter++;
                out.format("%n");
            }
        }
    }

    /**
     * Displays all the eigenvectors for a given eigenvalue, changes eigenvalues
     * to 0, warning
     *
     * @param out formatter to where to print to
     * @param values array with all the eigen values
     * @param vectors array with all the eigen vectors
     * @param i index of the eigenvalue to show on the matrix
     */
    public static void showAllVectorsForAGivenEigenValue(Formatter out, double[] values, double[][] vectors, int i) {
        Utilities.displayColumnOfDoubleMatrix(out, vectors, i);

        //checks if the eigenvalues is repeated to show all of their eigenvects at the same
        for (int j = i + 1; j < values.length; j++) {
            if (Utilities.roundDoubleThreeDecimalPlaces(values[j]) == Utilities.roundDoubleThreeDecimalPlaces(values[i])) {
                out.format("; ");
                Utilities.displayColumnOfDoubleMatrix(out, vectors, j);

                //if they are the same, changes the eigenvalues to 0 to ignore after
                values[j] = 0;
            }
        }
    }
}
