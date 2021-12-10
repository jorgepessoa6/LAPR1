package MainPackage;

import java.util.Formatter;

import org.la4j.*;

public class PageRank {

    /**
     * Calculates the page rank for every node when given a number of times to
     * iterate
     *
     * @param adjMatrix adjacency matrix of the net
     * @param nOfTimes number of times to iterate
     * @param dampingFactor damping factor to calc the pagerank
     * @param out Formatter that states where to output to
     * @return an array with all the page ranks
     */
    public static double[] iterativePageRankShowingAllIteractions(double[][] adjMatrix, int nOfTimes, float dampingFactor, Formatter out) {
        //the number of nodes == length of the adjacency matrix
        int numOfNodes = adjMatrix.length;
        //page rank is initialized has a column matrix to easy the calcs, it will be later translated
        double[][] tmpPageRank = new double[numOfNodes][1];

        //the initial page rank has 1 as all elements
        putOneInAllElements(numOfNodes, tmpPageRank);

        //tmpMatrix is a matrix with the "amount" of rank each node "shares" with another node
        double[][] tmpMatrix = getTmpMatrix(adjMatrix, numOfNodes, dampingFactor);

        //multiplies the current page rank by the temporary matrix and shows the page rank one by one
        for (int i = 1; i <= nOfTimes; i++) {
            out.format("%nIteração num %d%n", i - 1);
            Utilities.showColumnMatrix(tmpPageRank, out);
            tmpPageRank = Utilities.multiplyMatrices(tmpMatrix, tmpPageRank);
        }

        //converts that temporary page rank (column matrix) to a simple array, normalizes it and returns
        double[] pageRanks = Utilities.convertColumnMatrixToArray(tmpPageRank);
        pageRanks = Utilities.normalizeVector(pageRanks);
        return pageRanks;
    }

    /**
     * Calculates the page rank based on eigen values and vectors, like in
     * centrality, calculates the absolute page rank
     *
     * @param adjMatrix adjacency matrix of the net
     * @param dampingFactor damping factor to calc the pagerank
     * @return a vector with all the page ranks
     */
    public static double[] infinitePageRank(double[][] adjMatrix, float dampingFactor) {
        //the number of nodes == length of the adjacency matrix
        int numOfNodes = adjMatrix.length;

        //tmpMatrix is a matrix with the "amount" of rank each node "shares" with another node
        double[][] tmpMatrix = getTmpMatrix(adjMatrix, numOfNodes, dampingFactor);

        //gets the eigenValues
        Matrix[] decomposed = EigenOps.decomposeMatrix(tmpMatrix);

        //returns the centrality wich means the vector correponding to the largest eigen value, doesnt normalize it bc they are already normalized
        return EigenOps.obtainEigenVectorsCentralities(decomposed);
    }

    /**
     * Starts the pagerank vector with 1 as all elements
     *
     * @param numOfNodes number of nodes of the net
     * @param array array to format
     */
    private static void putOneInAllElements(int numOfNodes, double[][] array) {
        for (int elem = 0; elem < numOfNodes; elem++) {
            array[elem][0] = (float) 1 / numOfNodes;
        }
    }

    /**
     * Gets the temporary matrix wich is a matrix with the "amount" of rank each
     * node "shares" with another node
     *
     * @param adjMatrix adjacency matrix (float)
     * @param numOfNodes number of nodes of the social net
     * @return the temporary matrix
     */
    private static double[][] getTmpMatrix(double[][] adjMatrix, int numOfNodes, float dampingFactor) {
        double[][] tmpMatrix = new double[numOfNodes][numOfNodes];

        //goes through every element in the adjacency matrix coincident with any element in the tmpMatrix
        for (int row = 0; row < tmpMatrix[0].length; row++) {
            int outDegree = NodeOps.calcNodeDegreeOut(adjMatrix, row);
            for (int col = 0; col < tmpMatrix.length; col++) {
                fulfillElement(adjMatrix[row][col], numOfNodes, tmpMatrix, col, outDegree, row, dampingFactor);
            }
        }
        return tmpMatrix;
    }

    /**
     * Fullfills an element on the temporary matrix
     *
     * @param elem adjacency matrix (double)
     * @param numOfNodes number of nodes
     * @param tmpMatrix matrix with the "amount" of rank each node "shares" with
     * another node
     * @param col column where the loop is
     * @param outDegree out-degree of the node in question
     * @param row row where the loop is
     */
    private static void fulfillElement(double elem, int numOfNodes, double[][] tmpMatrix, int col, int outDegree, int row, float dampingFactor) {
        float aElement;
        //if there is a link
        if (elem == 1) {
            aElement = ((float) 1 / outDegree);
        } else if (outDegree == 0) { //if it is a node with no edgees
            aElement = ((float) 1 / numOfNodes);
        } else {
            aElement = 0;
        }
        float calc = aElement * dampingFactor + ((1 - dampingFactor) / numOfNodes);
        tmpMatrix[col][row] = calc;
    }

    public static void pageRankDemonstration(double[][] adjMatrix, String[][] nodes) {
        boolean tryAgain;

        do {
            tryAgain = false;

            int ans = Utilities.askAndRetrieveAnswerInt("\nQual o algoritmo de page rank que deseja utilizar?"
                    + "\n1 - Iterativo (onde se define o número de iterações)"
                    + "\n2 - Calculado a partir dos valores e vetores próprios"
                    + "\n\n    Opção: ");

            switch (ans) {
                case 1:
                    outputIterativePageRankMenu(adjMatrix, nodes, false); //always false bc its always non recursive
                    break;
                case 2:
                    outputVectorsPageRankMenu(adjMatrix, nodes, false); //always false bc its always non recursive
                    break;
                default:
                    tryAgain = true;
            }
        } while (tryAgain);
    }

    public static void outputVectorsPageRankMenu(double[][] adjMatrix, String[][] nodes, boolean isRecursive) {
        //gets the damping factor
        float dampingFactor = getDampingFactor();

        Formatter out = new Formatter(System.out);

        outputVectorsPageRank(adjMatrix, dampingFactor, out, nodes, isRecursive);
    }

    public static void outputVectorsPageRank(double[][] adjMatrix, float dampingFactor, Formatter out, String[][] nodes, boolean isRecursive) {
        //gets all the Page ranks
        double[] pageRanks = infinitePageRank(adjMatrix, dampingFactor);

        //writes all page ranks
        out.format("%nUtilizando os vetores e valores próprios");
        writePageRanks(out, pageRanks, nodes, dampingFactor);

        if (!isRecursive) {
            NetworkAnalysis.getAndOpenWebPage(pageRanks, nodes);
        }
    }

    public static void outputIterativePageRankMenu(double[][] adjMatrix, String[][] nodesMedia, boolean isRecursive) {
        //gets the number of iteractions
        int numIteractions = Utilities.askAndRetrieveAnswerInt("\nQual o número de iterações que deseja? ");

        //gets the damping factor
        float dampingFactor = getDampingFactor();

        Formatter out = new Formatter(System.out);

        outputIterativePageRank(adjMatrix, numIteractions, dampingFactor, out, nodesMedia, isRecursive);
    }

    public static void outputIterativePageRank(double[][] adjMatrix, int numIteractions, float dampingFactor, Formatter out, String[][] nodesMedia, boolean isRecursive) {
        //gets all the page ranks
        double[] pageRanks = iterativePageRankShowingAllIteractions(adjMatrix, numIteractions, dampingFactor, out);

        //writes all page ranks
        out.format("%nUtilizando %d iterações", numIteractions);
        writePageRanks(out, pageRanks, nodesMedia, dampingFactor);

        if (!isRecursive) {
            NetworkAnalysis.getAndOpenWebPage(pageRanks, nodesMedia);
        }
    }

    public static void writePageRanks(Formatter out, double[] pageRanks, String[][] nodesMedia, float dampingFactor) {
        out.format("%nOs Page ranks com " + dampingFactor + " como damping factor, são os seguintes:%n%n");
        for (int pageRank = 0; pageRank < pageRanks.length; pageRank++) {
            out.format("%4s: %.3f - %s%n", "s" + (pageRank + 1), pageRanks[pageRank], "(" + nodesMedia[pageRank][Configurations.NODES_FILE_COLUMN_NAME] + ")");
        }
    }

    /**
     * Gets the damping factor
     *
     * @return the damping factor
     */
    public static float getDampingFactor() {
        boolean dampingIsCorrect;
        float dampingFactor;

        do {
            //asks the damping factor and returns the answe
            dampingFactor = Utilities.askAndRetrieveAnswerFloat("\nQual é o damping factor que deseja? ");

            //checks if damping factor is in ]0, 1]
            dampingIsCorrect = checkIfDampingFactorIsInTheBounds(dampingFactor);

        } while (!dampingIsCorrect);

        return dampingFactor;
    }

    /**
     * checks if damping factor is in ]0, 1]
     *
     * @param dampingFactor the damping factor
     * @return if the damping factor is in the bounds
     */
    private static boolean checkIfDampingFactorIsInTheBounds(float dampingFactor) {
        boolean dampingIsCorrect = true;

        if (dampingFactor < 0 || dampingFactor > 1) {
            System.out.println("O damping factor deve estar em [0, 1]");
            dampingIsCorrect = false;
        }
        return dampingIsCorrect;
    }
}
