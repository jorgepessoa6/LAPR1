/*
 * Projeto de lapr1
 */
package Project;

import java.util.Formatter;
import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.decomposition.EigenDecompositor;

/**
 *
 * @author João Vitorino
 */
public class Project {

    /**
     * Decomposes a matrix of doubles into eigen values and vectors Matrix
     * objects
     *
     * @param mat - matrix with the information
     * @return an array of Matrix objects corresponding to the eigen values and
     * vectors
     */
    public static Matrix[] decomposeMatrix(double[][] mat) {
        // Creates object of Matrix type
        Matrix object = new Basic2DMatrix(mat);
        //Obtains eigen values and vectors matrices via "Eigen Decomposition"
        EigenDecompositor eigenD = new EigenDecompositor(object);
        Matrix[] decomposed = eigenD.decompose();
        return decomposed;
    }

    /**
     * Obtains the eigen values of a decomposed matrix of doubles
     *
     * @param decomposed - Matrix object with the information
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
     * @param decomposed - Matrix object with the information
     * @return a matrix with the eigen vectors of the initial matrix
     */
    public static double[][] obtainEigenVectors(Matrix[] decomposed) {
        // Converts Matrix object corresponding to the eigen vectors to a java matrix      
        double[][] vector = decomposed[0].toDenseMatrix().toArray();
        return vector;
    }

    /**
     * Obtains the centralities of the eigen vectors of a decomposed matrix of
     * doubles
     *
     * @param decomposed - Matrix object with the information
     * @return an array with the centralities of the eigen vectors
     */
    public static double[] obtainEigenVectorsCentralities(Matrix[] decomposed) {
        double[] matValues = obtainEigenValues(decomposed);
        double[][] matVectors = obtainEigenVectors(decomposed);
        int column = Utilities.obtainFirstPositionMaxValueArray(matValues);
        double[] centralities = new double[matVectors.length];
        for (int i = 0; i < matVectors.length; i++) {
            centralities[i] = matVectors[i][column];
        }
        return centralities;
    }

    /**
     * Outputs the eigen vectors centralities of a matrix of doubles to a
     * predetermined output
     *
     * @param outF - Formatter to wich output
     * @param mat - matrix with the information
     */
    public static void outputEigenVectorsCentralities(Formatter outF, double[][] mat) {
        Matrix[] decomposed = decomposeMatrix(mat);
        double[] centralities = obtainEigenVectorsCentralities(decomposed);
        outF.format("As centralidades dos vetores próprios são:%n");
        for (int i = 0; i < centralities.length; i++) {
            outF.format(" %d - %.3f%n", i + 1, centralities[i]);
        }
    }

    /**
     * Outputs the eigen values and vectors of a matrix of doubles to a
     * predetermined output
     *
     * @param outF - Formatter to wich output
     * @param mat - matrix with the information
     */
    public static void outputEigenValuesAndVectors(Formatter outF, double[][] mat) {
        Matrix[] decomposed = decomposeMatrix(mat);
        double[] values = obtainEigenValues(decomposed);
        double[][] vectors = obtainEigenVectors(decomposed);
        outF.format("Os valores próprios são: ");
        Utilities.displayVectorDoubleWithNoRepeatedElems(outF, values);
        outF.format("%n%nOs vetores próprios são:%n");
        for (int i = 0; i < vectors.length; i++) {
            outF.format(" %d - ", i + 1);
            Utilities.displayVectorDoubleMatrix(outF, vectors, i);
            outF.format("%n");
        }
    }
}
