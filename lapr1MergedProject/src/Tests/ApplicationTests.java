/*
 * Projeto de lapr1
 */
package Tests;

import MainPackage.EigenOps;
import MainPackage.NodeOps;
import MainPackage.Utilities;
import org.la4j.Matrix;

/**
 * @author StaticVoid
 */
public class ApplicationTests {

    public static void main(String[] args) {
        double[][] matrix2 = {{0, 1, 1, 1, 0, 1}, {1, 0, 0, 0, 1, 0}, {1, 0, 0, 1, 0, 0}, {1, 0, 1, 0, 0, 0}, {0, 1, 0, 0, 0, 0}, {1, 0, 0, 0, 0, 0}};
        int expectedNodeDegree = 4;
        System.out.println("Teste de calcNodeDegree, 1º teste: " + testCalcNodeDegree(matrix2, 0, expectedNodeDegree));
        expectedNodeDegree = 2;
        System.out.println("Teste de calcNodeDegree, 2º teste: " + testCalcNodeDegree(matrix2, 1, expectedNodeDegree));

        double expectedAverageDegree = 2;
        System.out.println("Teste de averageDegree, 1º teste: " + testCalcAverageDegree(matrix2, expectedAverageDegree));
        double[][] matrix = {{0, 1, 1, 1, 0}, {1, 0, 1, 0, 1}, {1, 1, 0, 1, 0}, {1, 0, 1, 0, 1}, {0, 1, 0, 1, 0}};
        expectedAverageDegree = 2.8;
        System.out.println("Teste de averageDegree, 2º teste: " + testCalcAverageDegree(matrix, expectedAverageDegree));

        float expectedDensity = (float) 0.4;
        System.out.println("Teste de calcDensity, 1º teste: " + testCalcDensity(matrix2, expectedDensity));
        expectedDensity = (float) 0.7;
        System.out.println("Teste de calcDensity, 2º teste: " + testCalcDensity(matrix, expectedDensity));

        double[][] testMatrix = {{0, 1, 0, 1, 0}, {1, 0, 1, 1, 1}, {0, 1, 0, 1, 0}, {1, 1, 1, 0, 0}, {0, 1, 0, 0, 0}};
        double[] expectedCent = {0.4119, 0.5825, 0.4119, 0.5237, 0.2169};
        System.out.println("Teste de calcCentrality; " + testObtainEigenVectorsCentralities(testMatrix, expectedCent));

  
        double[] array = {1000, 2000, 2, 3, 2000};
        int expectedIndex = 1;
        System.out.println("Teste de obtainFirstIndexOfMaxValue: " + testObtainFirstIndexOfMaxValue(array, expectedIndex));

        String toVerify = "a";
        boolean isInt = false;
        System.out.println("Teste de verifyIfInt, 1º teste: " + testVerifyIfIint(toVerify, isInt));
        toVerify = "1";
        isInt = true;
        System.out.println("Teste de verifyIfInt, 2º teste: " + testVerifyIfIint(toVerify, isInt));

        double[][] matrix3 = {{2, 3}, {4, 5}, {6, 7}, {8, 9}, {7, 3}, {6, 1}};
        double[][] expectedMulti = {{24, 22}, {9, 6}, {10, 12}, {8, 10}, {4, 5}, {2, 3}};
        System.out.println("Teste de multiplyMatrices, 1º teste: " + testMultiplyMatrices(matrix2, matrix3, expectedMulti));
        matrix3 = new double[][]{{3, 2, 1}, {5, 4, 3}, {7, 6, 5}, {9, 8, 7}, {3, 5, 7}, {1, 4, 6}};
        expectedMulti = new double[][]{{22, 22, 21}, {6, 7, 8}, {12, 10, 8}, {10, 8, 6}, {5, 4, 3}, {3, 2, 1}};
        System.out.println("Teste de multiplyMatrices, 2º teste: " + testMultiplyMatrices(matrix2, matrix3, expectedMulti));

        matrix = new double[][]{{95.14, 203.5, 48.5, 42.71, 24.0, 18.73}, {20.4, 31.7, 32.2, 32.98, 2.433, 24.76}, {44.8, 32.6, 41.8, 40.7, 8.526, 26.75}};
        matrix2 = new double[][]{{45.238, 68.235, 342.573}, {4583.1, 43.4323, 906.54}};
        int expectedLength = 3;
        System.out.println("Teste de getBiggestLengthInMatrix, 1º teste: " + testGetBiggestLengthInMatrix(matrix, expectedLength));
        expectedLength = 4;
        System.out.println("Teste de getBiggestLengthInMatrix, 2º teste: " + testGetBiggestLengthInMatrix(matrix2, expectedLength));

        expectedLength = 5;
        System.out.println("Teste de getBiggestLengthInMatrixWithDecimals, 1º teste: " + testGetBiggestLengthInMatrixWithDecimals(matrix, expectedLength));
        expectedLength = 7;
        System.out.println("Teste de getBiggestLengthInMatrixWithDecimals, 2º teste: " + testGetBiggestLengthInMatrixWithDecimals(matrix2, expectedLength));
   
        int nodeIndex = 0;
        int expectedDegree = 2;
        System.out.println("Teste de grau de entrada: " + testInDegree(testMatrix, nodeIndex, expectedDegree));
        
        System.out.println("Teste de grau de saída: " + testOutDegree(testMatrix, nodeIndex, expectedDegree));
        
        double[] vectorToNorm = {0, -2, 3, 6};
        double[] expectedNormed = {0, (double) -2/7, (double) 3/7, (double) 6/7};
        System.out.println("Teste de normalização de vetor: " + testNormalizingArray(vectorToNorm, expectedNormed));
    }

    public static boolean testInDegree(double[][] adjMatrix, int nodeIndex, int expected) {
        return (expected == NodeOps.calcNodeDegreeIn(adjMatrix, nodeIndex));
    }

    public static boolean testOutDegree(double[][] adjMatrix, int nodeIndex, int expected) {
        return (expected == NodeOps.calcNodeDegreeOut(adjMatrix, nodeIndex));
    }
    
    public static boolean testNormalizingArray(double[] toNorm, double[] expected){
        double[] arr = Utilities.normalizeVector(toNorm);
        
        boolean res = true;
        for(int elem = 0; elem < expected.length; elem++){
            if(arr[elem] != expected[elem]){
                res = false;
            }
        }
        
        return res;
    }

    public static boolean testCalcNodeDegree(double[][] matriz, int nodeIndex, int expectedNodeDegree) {
        int nodeDegree = NodeOps.calcNodeDegreeOut(matriz, nodeIndex);
        return expectedNodeDegree == nodeDegree;
    }

    public static boolean testCalcAverageDegree(double[][] matrix, double expected) {
        double averageDegree = NodeOps.calcAverageDegree(matrix);
        return averageDegree == expected;
    }

    public static boolean testCalcDensity(double[][] matriz, float densidadeExpected) { // cálculo da matriz teste
        float densidade = NodeOps.calcDensity(matriz);
        return densidade == densidadeExpected;
    }

    public static boolean testObtainEigenVectorsCentralities(double[][] matrix, double[] expected) {
        Matrix[] decomposed = EigenOps.decomposeMatrix(matrix);
        double[] centralities = EigenOps.obtainEigenVectorsCentralities(decomposed);
        boolean res = true;

        /* Como existem diferenças de casas decimais entre o expected e o valor fornecido pela library, multiplicamos
		 * ambas por 1000 (3 casas decimais) e comaparamos a partir daí */
        for (int elem = 0; elem < centralities.length; elem++) {
            if ((int) (centralities[elem] * 1000) != (int) (expected[elem] * 1000)) {
                res = false;
            }
        }
        return res;
    }

    public static boolean testCountNumLinesFile(String file, int expected) {
        return Utilities.countNumLinesFile(file) == expected;
    }

    public static boolean testObtainFirstIndexOfMaxValue(double[] array, int expected) {
        return Utilities.obtainFirstIndexOfMaxValue(array) == expected;
    }

    public static boolean testVerifyIfIint(String toVerify, boolean expected) {
        return Utilities.verifyIfInt(toVerify) == expected;
    }

    public static boolean testMultiplyMatrices(double[][] matA, double[][] matB, double[][] expected) {
        double[][] multiplied = Utilities.multiplyMatrices(matA, matB);
        boolean res = true;
        for (int row = 0; row < multiplied.length; row++) {
            for (int col = 0; col < multiplied[row].length; col++) {
                if (multiplied[row][col] != expected[row][col]) {
                    res = false;
                }
            }
        }
        return res;
    }

    public static boolean testGetBiggestLengthInMatrix(double[][] mat, int expected) {
        return Utilities.getBiggestLengthInMatrix(mat) == expected;
    }

    public static boolean testGetBiggestLengthInMatrixWithDecimals(double[][] mat, int expected) {
        return Utilities.getBiggestLengthInMatrixWithDecimals(mat) == expected;
    }
}
