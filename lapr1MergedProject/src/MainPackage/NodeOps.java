/*
 * Projeto de lapr1
 */
package MainPackage;

import java.util.Formatter;

/**
 * @author StaticVoid
 */
public class NodeOps {

    public static void outputDegreesIn(Formatter out, double[][] adjMatrix, String[][] nodesMedia, boolean isRecursive) {
        int nodeDegree;
        int biggestDegree = 0;
        int biggestDegreeIndex = 0;

        //header
        out.format("%nOs graus de entrada dos nós são:%n%n");

        for (int i = 0; i < adjMatrix.length; i++) {
            //calculates the nodeDegree;
            nodeDegree = calcNodeDegreeIn(adjMatrix, i);

            //gets the node wich has the biggest degree
            if (nodeDegree > biggestDegree) {
                biggestDegree = nodeDegree;
                biggestDegreeIndex = i;
            }

            out.format("%4s: %d - %s%n", "s" + (i + 1), nodeDegree, "(" + nodesMedia[i][Configurations.NODES_FILE_COLUMN_NAME] + ")");
        }

        //opens the webpage of the node that has the biggest degree
        if (!isRecursive) {
            NetworkAnalysis.openWebPage(nodesMedia[biggestDegreeIndex][Configurations.NODES_FILE_COLUMN_WEB]);
        }
    }

    public static void outputDegreesOut(Formatter out, double[][] adjMatrix, String[][] nodesMedia, boolean isRecursive) {
        int nodeDegree;
        //starts the biggest ones as 0
        int biggestDegree = 0;
        int biggestDegreeIndex = 0;

        //header
        out.format("%nOs graus de saída dos nós são:%n%n");

        for (int i = 0; i < adjMatrix.length; i++) {

            nodeDegree = calcNodeDegreeOut(adjMatrix, i);

            //gets the node wich has the biggest degree
            if (nodeDegree > biggestDegree) {
                biggestDegree = nodeDegree;
                biggestDegreeIndex = i;
            }

            //prints the nodeDegree
            out.format("%4s: %d - %s%n", "s" + (i + 1), nodeDegree, "(" + nodesMedia[i][Configurations.NODES_FILE_COLUMN_NAME] + ")");
        }

        //opens the webpage
        if (!isRecursive) {
            NetworkAnalysis.openWebPage(nodesMedia[biggestDegreeIndex][Configurations.NODES_FILE_COLUMN_WEB]);
        }
    }

    /**
     * Outputs all the node degrees of a non oriented network to a predetermined
     * formatter
     *
     * @param nodesMedia array with the names of the nodes
     * @param out formatter to output
     * @param adjMatrix adjacency array
     * @param isRecursive boolean that states if the program is recursive at 
     * that point in time
     */
    public static void outputDegreesNonOri(Formatter out, double[][] adjMatrix, String[][] nodesMedia, boolean isRecursive) {
        //starts the node who has the biggest node as the first
        int biggestNodeIndex = 0;
        int biggestNode = 0;

        out.format("%nOs graus dos nós são:%n%n");
        for (int i = 0; i < adjMatrix.length; i++) {
            int nodeDegree = calcNodeDegreeOut(adjMatrix, i);

            //sets the biggest node
            if (nodeDegree > biggestNode) {
                biggestNodeIndex = i;
                biggestNode = nodeDegree;
            }

            out.format("%4s: %d - %s%n", "s" + (i + 1), nodeDegree, "(" + nodesMedia[i][Configurations.NODES_FILE_COLUMN_NAME] + ")");
        }

        //opens the webpage of the node with biggest degree
        if (!isRecursive) {
            NetworkAnalysis.openWebPage(nodesMedia[biggestNodeIndex][Configurations.NODES_FILE_COLUMN_WEB]);
        }

    }

    /**
     * Receives a node index and the matrix of adjacencies and proceeds to
     * calculate the entry degree of that node
     *
     * @param adjMatrix adjacency array of the socail network
     * @param nodeIndex index of node
     * @return Entry degree of the node
     */
    public static int calcNodeDegreeOut(double[][] adjMatrix, int nodeIndex) {
        int nodeDegree = 0;
        for (int col = 0; col < adjMatrix[nodeIndex].length; col++) {
            nodeDegree += adjMatrix[nodeIndex][col];
        }
        return nodeDegree;
    }

    /**
     * Receives a node index and the matrix of adjacencies and proceeds to
     * calculate the exit degree of that node
     *
     * @param adjMatrix adjacency array of the socail network
     * @param nodeIndex index of node
     * @return Exit degree of the node
     */
    public static int calcNodeDegreeIn(double[][] adjMatrix, int nodeIndex) {
        int nodeDegree = 0;
        for (double[] row : adjMatrix) {
            nodeDegree += row[nodeIndex];
        }
        return nodeDegree;
    }

    /**
     * Calculates the average from the total sum of degrees
     *
     * @param adjMatrix adjaacecy array
     * @return Average Degree
     */
    public static double calcAverageDegree(double[][] adjMatrix) {

        return sumDegree(adjMatrix) / adjMatrix.length;
    }

    /**
     * Calculates the sum of all degrees of the nodes
     *
     * @param adjMatrix adjacency array of the network
     * @return Total sum of degrees
     */
    public static double sumDegree(double[][] adjMatrix) {
        double sum = 0;
        for (int nodeIndex = 0; nodeIndex < adjMatrix.length; nodeIndex++) {
            sum += NodeOps.calcNodeDegreeOut(adjMatrix, nodeIndex);
        }
        return sum;
    }

    /**
     * Calculates the density o the graph
     *
     * @param adjMatrix - matrice made from the read file
     * @return densidade - density of the graph (a double between 0 and 1)
     */
    public static float calcDensity(double[][] adjMatrix) {
        int numNodes = adjMatrix.length;
        int maxNumEdges = (numNodes * (numNodes - 1)) / 2; // maximum number of nodes
        //beacause the edges can have only 0 or 1 as weight and is indirected, the number of edges == sum of the degrees / 2
        int numEdges = (int) (NodeOps.sumDegree(adjMatrix) / 2);

        //density is the number of edges / maximum possible number of edges
        return ((float) numEdges / (maxNumEdges));
    }
}
