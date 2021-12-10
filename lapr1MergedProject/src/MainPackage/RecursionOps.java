/*
 * Projeto de lapr1
 */
package MainPackage;

import org.la4j.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;

/**
 * @author StaticVoid
 */
public class RecursionOps {

    /**
     * Outputs all the functionalities, related to the direct (oriented)
     * matrices, to one single txt file
     *
     * @param adjMatrix adjacency array of the social network
     * @param nodesInfo array with the names and links of the nodes
     * @param fileName name of the file to export to
     * @param k power of the matrix to power to
     * @param args parametes choosen by the user
     * @param dampingFactor damping factor to calc the pagerank
     * @param isRecursive boolean that states if the program is recursive
     * @throws FileNotFoundException if it is unable to export to the file,
     * throws the excpetion
     */
    public static void outputToFileOri(double[][] adjMatrix, String[][] nodesInfo, String fileName, int k, String[] args, float dampingFactor, boolean isRecursive) throws FileNotFoundException {
        Formatter output = new Formatter(new File(fileName));

        //Mostrar ramos e informação
        NetworkAnalysis.showNetworkInfo(output, adjMatrix, nodesInfo, args);
        Utilities.writeStrXTimes(output, "_", 70);

        //Graus de entrada e saída dos nós
        NodeOps.outputDegreesIn(output, adjMatrix, nodesInfo, isRecursive);
        Utilities.writeStrXTimes(output, "_", 70);

        NodeOps.outputDegreesOut(output, adjMatrix, nodesInfo, isRecursive);
        Utilities.writeStrXTimes(output, "_", 70);

        //Page Ranks
        PageRank.outputIterativePageRank(adjMatrix, k, dampingFactor, output, nodesInfo, isRecursive);
        Utilities.writeStrXTimes(output, "_", 70);

        PageRank.outputVectorsPageRank(adjMatrix, dampingFactor, output, nodesInfo, isRecursive);
        Utilities.writeStrXTimes(output, "_", 70);

        output.close();

    }

    /**
     * Outputs all the functionalities, related to the non direct (non oriented)
     * matrices, to one single txt file
     *
     * @param adjMatrix adjacency array of the social network
     * @param nodesInfo array with the names of the nodes
     * @param fileName name of the file to export to
     * @param k power of the matrix to power to
     * @param args parameters choosen by the user
     * @param isRecursive boolean that states if the program is recursive
     * @throws FileNotFoundException if it is unable to export to the file,
     * throws the excpetion
     */
    public static void outputToFileNonOri(double[][] adjMatrix, String[][] nodesInfo, String fileName, int k, String[] args, boolean isRecursive) throws FileNotFoundException {
        Matrix[] decomposed = EigenOps.decomposeMatrix(adjMatrix);
        Formatter output = new Formatter(new File(fileName));

        //Mostrar ramos e informação
        NetworkAnalysis.showNetworkInfo(output, adjMatrix, nodesInfo, args);
        Utilities.writeStrXTimes(output, "_", 70);

        //Graus dos nós
        NodeOps.outputDegreesNonOri(output, adjMatrix, nodesInfo, isRecursive);
        Utilities.writeStrXTimes(output, "_", 70);

        //Centralidade de um vetor próprio
        EigenOps.outputEigenVectorsCentralities(output, nodesInfo, decomposed, isRecursive);
        Utilities.writeStrXTimes(output, "_", 70);

        //Grau médio
        output.format("%n%s%n", "O grau médio é: " + NodeOps.calcAverageDegree(adjMatrix));
        Utilities.writeStrXTimes(output, "_", 70);

        //Densidade
        output.format("%nDensidade: %s%n", NodeOps.calcDensity(adjMatrix));
        Utilities.writeStrXTimes(output, "_", 70);
        output.format("%n");

        //Potências da matriz de adjacências
        NetworkAnalysis.getMatrixPowerShowingAllOperations(k, adjMatrix, output);
        Utilities.writeStrXTimes(output, "_", 70);

        //Calcular valores e vetores próprios
        EigenOps.outputEigenValuesAndVectors(output, decomposed);

        output.close();

    }

}
