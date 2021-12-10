/*
 * Projeto de lapr1
 */
package MainPackage;

import java.awt.Desktop;
import org.la4j.Matrix;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Formatter;
import java.util.Scanner;

/**
 * @author StaticVoid
 */
public class NetworkAnalysis {

    public static void main(String[] args) throws FileNotFoundException {
        //checks errors
        boolean theresError = FindErrors.isThereErrors(args);

        //if there's error the program will stop, if not, it continues according to the parameters chosen
        if (!theresError) {
            String opt = args[Configurations.PARAM_OPTION];

            //boolean that saves if the user chose the recursive option or not
            boolean isRecursive = opt.equals(Configurations.PARAM_OPTION_TO_RECURSIVE);

            //gets the name of the edges file
            String edgesFile = getEdgesFileName(args);

            //finds if the network is oriented or not
            boolean isOriented = obtainNetworkType(edgesFile);
            double[][] adjacencyMatrix = createAdjacencyMatrix(args, isRecursive, isOriented, edgesFile);

            if (adjacencyMatrix != null) {
                //after creating the necessary variables and arrays, starts the program itself
                runProgram(adjacencyMatrix, args, isRecursive, isOriented);
            } else {
                System.out.println("There is an error!");
            }
        }
    }

    /**
     * Runs the program itself depending on the option choosen on the parameter
     * and the orientation of the network
     *
     * @param adjacencyMatrix adjacency matrix with the description of the
     * social network
     * @param args arguments received by the user
     * @param isRecursive boolean to state if it is recursive or not
     * @param isOriented boolean to state if it is an oriented network or not
     * @throws java.io.FileNotFoundException if it does not find the fikle
     */
    public static void runProgram(double[][] adjacencyMatrix, String[] args, boolean isRecursive, boolean isOriented) throws FileNotFoundException {
        //creates a matrix with all the info about nodes and an array of matrices to calculate eigenvalues and vectors
        String[][] nodesInfo = makeNodesInfoMatrix(args, isRecursive, isOriented);
        Matrix[] decomposed = null;

        //runs the programs in different ways if the network is oriented or not
        if (!isOriented) {
            if (!isRecursive) {
                executeNotOrientedNotRecursive(args, adjacencyMatrix, nodesInfo, decomposed);
            } else {
                executeNotOrientedRecursive(args, adjacencyMatrix, nodesInfo);
            }
        } else {
            if (!isRecursive) {
                executeOrientedNotRecursive(args, adjacencyMatrix, nodesInfo);
            } else {
                executeOrientedRecursive(args, adjacencyMatrix, nodesInfo);
            }
        }
    }

    public static void executeOrientedRecursive(String[] args, double[][] adjacencyMatrix, String[][] nodesInfo) throws FileNotFoundException, NumberFormatException {
        //finds the name of the file to be written
        String fileName = getOutputFileName(args);

        //gets the power choosen by the user in the recursive option
        int k = Integer.parseInt(args[Configurations.PARAM_RECURSIVE_POWER]);
        float dampingFactor = Float.parseFloat(args[Configurations.PARAM_DAMPING_VALUE]);

        //write everything to a file
        RecursionOps.outputToFileOri(adjacencyMatrix, nodesInfo, fileName, k, args, dampingFactor, true); //true bc its always recursive
        System.out.println("Ficheiro " + fileName + " criado!\n");
    }

    public static void executeOrientedNotRecursive(String[] args, double[][] adjacencyMatrix, String[][] nodesInfo) {
        Formatter write = new Formatter(System.out);
        writeNetworkName(args, write);
        executeProgramByMenuOri(adjacencyMatrix, nodesInfo, args);
    }

    public static void executeNotOrientedRecursive(String[] args, double[][] adjacencyMatrix, String[][] nodesInfo) throws FileNotFoundException, NumberFormatException {
        String fileName = getOutputFileName(args);

        //gets the power choosen by the user in the recursive option
        int power = Integer.parseInt(args[Configurations.PARAM_RECURSIVE_POWER]);

        //write everything to a file
        RecursionOps.outputToFileNonOri(adjacencyMatrix, nodesInfo, fileName, power, args, true); //always true bc its always recursive
        System.out.println("Ficheiro " + fileName + " criado!\n");
    }

    public static void executeNotOrientedNotRecursive(String[] args, double[][] adjacencyMatrix, String[][] nodesInfo, Matrix[] decomposed) {
        Formatter write = new Formatter(System.out);
        writeNetworkName(args, write);
        executeProgramByMenuNonOri(adjacencyMatrix, nodesInfo, decomposed, args);
    }

    /**
     * Gets the file name to where the information will be output
     *
     * @param args arguments received by the user
     * @return the correct file name
     */
    public static String getOutputFileName(String[] args) {
        //the name of the output file should be "out_nameNet_date.txt"
        String date = Utilities.getCurrentDatedmy();
        String socialNetName = getSocialNetworksName(args);
        return "out_" + socialNetName + "_" + date + ".txt";
    }

    /**
     * Writes the name of the network
     *
     * @param args arguments received by the user
     * @param write Formatter that states where to write to
     */
    public static void writeNetworkName(String[] args, Formatter write) {
        String networkName = getSocialNetworksName(args);
        write.format("   »»»»  Análise da rede " + networkName);
    }

    /**
     * Obtains the name of the social network
     *
     * @param args the arguments of the program
     * @return the name of the social network
     */
    public static String getSocialNetworksName(String[] args) {
        String fileEdgesName = getEdgesFileName(args);
        //edges file is like: "rs_social_network_name_edges.csv", so the network name is from the first "_" non inclusive to the last "_" non invlusive
        return fileEdgesName.substring(fileEdgesName.indexOf("_") + 1, fileEdgesName.lastIndexOf("_"));
    }

    /**
     * Obtains the webpage name of the most popular node
     *
     * @param arr matrix with the information
     * @param nodesMedia matrix with the names and webpages of the nodes
     * @return the String with the link of the webpage
     */
    public static String getMostPopularWebPage(double[] arr, String[][] nodesMedia) {
        int biggestValueIndex = Utilities.obtainFirstIndexOfMaxValue(arr);

        return nodesMedia[biggestValueIndex][Configurations.NODES_FILE_COLUMN_WEB];
    }

    /**
     * Shows the menu for non oriented networks and executes the choice chosen
     * by the user
     *
     * @param adjMatrix adjacency matrix
     * @param nodesMedia array with the names of the nodes
     * @param decomposed an array of matrices to calculate eigenvectors and
     * values
     * @param args an array with all the args used by the user
     */
    public static void executeProgramByMenuNonOri(double[][] adjMatrix, String[][] nodesMedia, Matrix[] decomposed, String[] args) {
        String answer;
        do {
            answer = writeMenuRetrieveChoiceNonOri();
            decomposed = executeChoiceOfMenuNonOri(answer, adjMatrix, nodesMedia, decomposed, args);
        } while (!answer.equals("0")); //"0" is the option to leave the menu
    }

    /**
     * Shows the menu for oriented networks and executes the choice choosen by
     * the user
     *
     * @param adjMatrix adjacency matrix
     * @param nodesMedia array with the names of the nodes
     * @param args parameters passed by the user
     */
    public static void executeProgramByMenuOri(double[][] adjMatrix, String[][] nodesMedia, String[] args) {
        String answer;
        do {
            answer = writeMenuRetrieveChoiceOri();
            executeChoiceOfMenuOri(answer, adjMatrix, nodesMedia, args);
        } while (!answer.equals("0")); //"0" is the option to leave the menu
    }

    /**
     * Executes one functionality chosen by the user using a menu
     *
     * @param choice string that refers to the choice made by the user
     * @param adjMatrix adjacency matrix of the social network
     * @param nodesInfo array with the names of all the nodes
     * @param decomposed an array of matrices used to calculate eigen values and
     * vectors
     * @param args parameters used by the user
     * @return object corresponding to the decomposed Matrix
     */
    public static Matrix[] executeChoiceOfMenuNonOri(String choice, double[][] adjMatrix, String[][] nodesInfo, Matrix[] decomposed, String[] args) {
        Formatter write = new Formatter(System.out);

        switch (choice) {
            case "1":
                showNetworkInfo(write, adjMatrix, nodesInfo, args);
                break;
            case "2":
                NodeOps.outputDegreesNonOri(write, adjMatrix, nodesInfo, false); //always false bc its always non recursive
                break;
            case "3":
                if (decomposed == null) {
                    decomposed = EigenOps.decomposeMatrix(adjMatrix);
                }
                EigenOps.outputEigenVectorsCentralities(write, nodesInfo, decomposed, false); //always false bc its always non recursive
                break;
            case "4":
                double averageDegree = NodeOps.calcAverageDegree(adjMatrix);
                System.out.println("\nO grau médio é: " + averageDegree);
                break;
            case "5":
                System.out.printf("\nA densidade da rede é: %.3f%n", NodeOps.calcDensity(adjMatrix));
                break;
            case "6":
                String k = Utilities.askAndRetrieveAnswerStr("\nLevantar o k a: ");
                if (!Utilities.verifyIfInt(k)) {
                    System.out.println(k + " não é um número inteiro!");
                    break;
                }
                int numK = Integer.parseInt(k);
                int numEdges = calculateNumberOfEdgesUndirected(adjMatrix.length);
                if (thestIfPowerIsIncorrect(numK, numEdges)) {
                    break;
                }
                getMatrixPowerShowingAllOperations(numK, adjMatrix, write);
                break;
            case "7":
                if (decomposed == null) {
                    decomposed = EigenOps.decomposeMatrix(adjMatrix);
                }
                EigenOps.outputEigenValuesAndVectors(write, decomposed);
                break;
            case "0":
                System.out.println("\nPrograma terminado");
                break;
            default:
                System.out.println("\nEscolha uma opção válida");
                break;
        }

        return decomposed;
    }

    public static void getAndOpenWebPage(double[] arr, String[][] nodesInfo) {
        //Gets the most popular webpage
        String webPage = NetworkAnalysis.getMostPopularWebPage(arr, nodesInfo);

        webPage = webPage.trim();

        openWebPage(webPage);
    }

    /**
     * Opens a web page
     *
     * @param webPage webpage to open
     */
    public static void openWebPage(String webPage) {
        webPage = webPage.trim();

        //opens it
        try {
            Desktop desktop = java.awt.Desktop.getDesktop();
            URI oURL = new URI(webPage);
            desktop.browse(oURL);
        } catch (Exception e) {
            System.out.println("É impossível abrir " + webPage);
        }
    }

    public static void executeChoiceOfMenuOri(String choice, double[][] adjMatrix, String[][] nodesInfo, String[] args) {
        Formatter write = new Formatter(System.out);
        switch (choice) {
            case "1":
                showNetworkInfo(write, adjMatrix, nodesInfo, args);
                break;
            case "2":
                NodeOps.outputDegreesIn(write, adjMatrix, nodesInfo, false); //always false bc its always non recursive
                break;
            case "3":
                NodeOps.outputDegreesOut(write, adjMatrix, nodesInfo, false); //always false bc its always non recursive
                break;
            case "4":
                PageRank.pageRankDemonstration(adjMatrix, nodesInfo);
                break;
            case "0":
                System.out.println("\nPrograma terminado");
                break;
            default:
                System.out.println("\nEscolha uma opção válida");
                break;
        }
    }

    /**
     * Tests if the power is incorrect
     *
     * @param kInt power
     * @param numOfEdges number of edges of the social network
     * @return if the power is incorrect or not
     */
    public static boolean thestIfPowerIsIncorrect(int kInt, int numOfEdges) {
        boolean theresError = false;
        if (kInt > numOfEdges) {
            System.out.println("O k tem de ser menor do que o número de ramos!");
            theresError = true;
        } else if (kInt < 1) {
            System.out.println("O k tem de ser no mínimo 1!");
            theresError = true;
        }
        return theresError;
    }

    /**
     * Writes the menu of the application for non oriented networks
     *
     * @return the choice choosen
     */
    public static String writeMenuRetrieveChoiceNonOri() {
        String answer = Utilities.askAndRetrieveAnswerStr("\n______________________________________________\n"
                + "\n               // MENU //"
                + "\n 1 - Mostrar informação de nós e ramos"
                + "\n 2 - Graus dos nós"
                + "\n 3 - Centralidades dos nós"
                + "\n 4 - Grau médio"
                + "\n 5 - Densidade"
                + "\n 6 - Potências da Matriz de Adjacência"
                + "\n 7 - Valores e vetores próprios"
                + "\n 0 - Terminar"
                + "\n______________________________________________\n"
                + "\n    Opção: ");

        //if the answer has not one character, changes it to a non usable choice
        if (answer.length() != 1) {
            answer = "-1";
        }
        return answer;
    }

    /**
     * Writes the menu of the application for oriented networks
     *
     * @return the choice choosen
     */
    public static String writeMenuRetrieveChoiceOri() {
        String answer = Utilities.askAndRetrieveAnswerStr("\n______________________________________________\n"
                + "\n               // MENU //"
                + "\n 1 - Mostrar informação de nós e ramos"
                + "\n 2 - Grau de entrada dos nós"
                + "\n 3 - Grau de saída dos nós"
                + "\n 4 - Page Rank"
                + "\n 0 - Terminar"
                + "\n______________________________________________\n"
                + "\n    Opção: ");

        //if the answer has not one character, changes it to a non usable choice
        if (answer.length() != 1) {
            answer = "-1";
        }
        return answer;
    }

    /**
     * Obtains the network type
     *
     * @param file name of the input file
     * @return true if the network is oriented or false if it isn't
     * @throws java.io.FileNotFoundException if does not find a file
     */
    public static boolean obtainNetworkType(String file) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(file));
        String line = scan.nextLine();
        String[] type = line.split(":");
        Utilities.trimAVector(type);
        boolean isOriented = false;
        //obtains the network type
        if (type[1].equals("oriented")) {
            isOriented = true;
        }
        scan.close();
        return isOriented;
    }

    /**
     * Creates the adjacency array
     *
     * @param args files passed as params
     * @param isRecursive boolean that state sif the the program is recursive at that point
     * @param isOriented boolean that states if the network is oriented
     * @param edgesFile name of the edges' file
     * @return the adjacency array, or null if it is impossible to do it
     * @throws java.io.FileNotFoundException if does not find the edge file
     */
    public static double[][] createAdjacencyMatrix(String[] args, boolean isRecursive, boolean isOriented, String edgesFile) throws FileNotFoundException {
        String nodesFile = getNodesFileName(isRecursive, args, isOriented);

        int numOfNodes = getNumOfNodes(nodesFile);

        //returns null if the file has not nodes
        if (numOfNodes <= 0) {
            System.out.println("\nO ficheiro " + nodesFile + " é inválido");
            return null;
        }

        //adjacency array is a squared matrix that has allTheNodes x allTheNodes
        double[][] adjArray = new double[numOfNodes][numOfNodes];
        fillAdjArray(edgesFile, adjArray, isOriented);

        return adjArray;
    }

    /**
     * Gets the name of the edges file
     *
     * @param args arguments passed by the user
     * @return the name of the file of the edges
     */
    public static String getEdgesFileName(String[] args) {
        int edgesFileIndex = args.length - 1;

        return args[edgesFileIndex];
    }

    /**
     * Gets the name of the nodes file
     *
     * @param isRecursive boolean that states if the choice of the user is the
     * recursive or the non recursive one
     * @param args arguments passed by the user
     * @param isOriented boolean that states if the net is oriented
     * @return the name of the file of the nodes
     */
    public static String getNodesFileName(boolean isRecursive, String[] args, boolean isOriented) {
        String nodesFile;
        if (!isRecursive) {
            nodesFile = args[Configurations.PARAM_NODES_FILE];
        } else {
            if (!isOriented) {
                nodesFile = args[Configurations.PARAM_RECURSIVE_NODES_FILE];
            } else {
                nodesFile = args[Configurations.PARAM_DIRECTED_RECURSIVE_NODES_FILE];
            }
        }
        return nodesFile;
    }

    /**
     * Fills an array name adjArray (from adjacency array) with the informations
     * provided in a csv file that describes the edges of the social network
     *
     * @param adjArray array that is being fullfilled
     * @param edgesFile csv files with the edges of the social network info
     * @param isOriented boolean that states if the network is oriented
     *
     * @throws FileNotFoundException if the edges file is not found
     */
    public static void fillAdjArray(String edgesFile, double[][] adjArray, boolean isOriented) throws FileNotFoundException {
        //boolean that states if the user has been warned or not about a possible error
        boolean warned = false;

        Scanner scan = new Scanner(new File(edgesFile));
        while (scan.hasNextLine()) {
            String line = scan.nextLine();

            //if the line starts with the prefix ("s") and has more than 1 char, it means it is a parameter and not the header
            if (line.length() > 0 && line.substring(Configurations.EDGES_FILE_COLUMN_FROM, Configurations.EDGES_FILE_COLUMN_FROM + Configurations.ID_PREFIX.length()).equals(Configurations.ID_PREFIX)) {
                warned = fillAdjArrayEdge(line, adjArray, warned, isOriented);
            }
        }
        scan.close();
    }

    /**
     * Fullfills a sigle row of the adjacency array with a line of String
     *
     * @param line String with a single line of the csv file
     * @param adjArray Adjacency array being fullfiled
     * @param warned boolean that shows if the user is already warned of
     * repeated elements
     * @param isOriented boolean that states if the network is oriented
     * @return if the user has beem already warned about repeated elements
     */
    public static boolean fillAdjArrayEdge(String line, double[][] adjArray, boolean warned, boolean isOriented) {
        //divides eache line in a vector
        String[] rowVector = line.split(Configurations.FILES_COLUMNS_SEPARATOR);

        //gets all the columns in separated string to be easier to translate in ints
        String adjArrayFromString = rowVector[Configurations.EDGES_FILE_COLUMN_FROM].trim().substring(Configurations.ID_PREFIX.length()); // <- to ignore the Id prefix
        String adjArrayToString = rowVector[Configurations.EDGES_FILE_COLUMN_TO].trim().substring(Configurations.ID_PREFIX.length());
        String adjArrayWeightString = rowVector[Configurations.EDGES_FILE_COLUMN_WEIGHT].trim();

        //translates in ints
        int adjArrayRow = Integer.parseInt(adjArrayFromString) - 1; //1 because the array starts at 0, not 1
        int adjArrayColumn = Integer.parseInt(adjArrayToString) - 1;
        int adjArrayWeight = Byte.parseByte(adjArrayWeightString);

        /*
		because of the symmetry of the adjacency array in this case where the social network is undirected
		if the weight is already bigger than 0 in the nodes in question, and the user hasnt been warned yet, hell receive a warning
         */
        if ((adjArray[adjArrayRow][adjArrayColumn] > 0 || adjArray[adjArrayColumn][adjArrayRow] > 0) && !warned) {
            System.out.println("Aviso: Os ficheiros contêm atribuições de ramos repetidas, mas a informação foi carregada corretamente\n\n");
            warned = true;
        }
        //puts the weight in both sides because the network is undirected, so if A is connected to B, B is connected to A
        adjArray[adjArrayRow][adjArrayColumn] = adjArrayWeight;
        if (!isOriented) {
            adjArray[adjArrayColumn][adjArrayRow] = adjArrayWeight;
        }
        return warned;
    }

    /**
     * Completes a matrix with information of the nodes
     *
     * @param nodesFile file with the nodes
     * @param nodesInfo array to be completed
     * @throws FileNotFoundException if the file does not exist
     */
    public static void completeNodesInfoMatrix(String nodesFile, String[][] nodesInfo) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(nodesFile));

        while (scan.hasNextLine()) {
            String line = scan.nextLine();

            //if the line starts with "s" and has more than 1 char, it means it is a parameter and not the header
            if (line.length() > 0 && line.substring(Configurations.NODES_FILE_COLUMN_ID, Configurations.NODES_FILE_COLUMN_ID + Configurations.ID_PREFIX.length()).equals(Configurations.ID_PREFIX)) {
                fillNodesInfoMatrix(line, nodesInfo);
            }
        }
        scan.close();
    }

    /**
     * Puts the information of a node in it respective place in a matrix
     *
     * @param line line of the node in the file
     * @param nodesInfo array where to put all the names
     */
    public static void fillNodesInfoMatrix(String line, String[][] nodesInfo) {
        //splits the line in an array to be easier to work on it
        String[] rowVector = line.split(Configurations.FILES_COLUMNS_SEPARATOR);
        Utilities.trimAVector(rowVector);

        //getting the id of the node in a string to transform it in int to put in the matrix
        String nodeIdStr = rowVector[Configurations.NODES_FILE_COLUMN_ID].trim().substring(Configurations.ID_PREFIX.length()); // <- to ignore the Id prefix

        int nodePlace = Integer.parseInt(nodeIdStr) - 1; //1 because the array starts at 0, not 1

        if (nodesInfo[nodePlace][0] != null) {
            System.out.println("Ficheiro de nós, tem o nó " + rowVector[0] + " repetido, adicionado a última opção do mesmo");
        }
        nodesInfo[nodePlace] = rowVector;
    }

    /**
     * Converts the the nodes file to an array
     *
     * @param args files passed as params
     * @param isRecursive boolean that states if the program will be recursvie
     * or not
     * @param isOriented boolean that states if the string is oriented
     * @return the array with the nodes
     * @throws FileNotFoundException if the file does not exist
     */
    public static String[][] makeNodesInfoMatrix(String[] args, boolean isRecursive, boolean isOriented) throws FileNotFoundException {
        String nodesFile = getNodesFileName(isRecursive, args, isOriented);

        //int numOfNodes = Utilities.countNumLinesFile(nodesFile) - Configurations.HEADER_NUM_OF_LINES_NODES;
        int numOfNodes = getNumOfNodes(nodesFile);

        if (numOfNodes <= 0) {
            return null;
        }

        String[][] nodesInfo = new String[numOfNodes][Configurations.NODES_FILE_N_PARAMS];

        completeNodesInfoMatrix(nodesFile, nodesInfo);
        return nodesInfo;
    }

    public static int getNumOfNodes(String nodesFile) throws FileNotFoundException {
        Scanner read = new Scanner(new File(nodesFile));

        int nLines = 1;
        int nNodes = 0;

        while (read.hasNextLine()) {
            String line = read.nextLine();

            if (nLines > Configurations.HEADER_NUM_OF_LINES_NODES && line.length() > 1) {
                nNodes++;
            }

            nLines++;
        }

        return nNodes;
    }

    /**
     * Shows the adjacency array with the ids of all the nodes
     *
     * @param out formatter to which send the output
     * @param adjArray adjacency matrix
     * @param nodesInfo matrix with the nodes information
     * @param args parameters received by the user
     */
    public static void showNetworkInfo(Formatter out, double[][] adjArray, String[][] nodesInfo, String[] args) {
        out.format("%n");
        writeNetworkName(args, out);
        
        //header
        out.format("%n%n%5s | %-30s | %5s | %-20s | %-50s%n", "ID", "Nome", "Tipo", "Nome-tipo", "URL");
        for (String[] row : nodesInfo) {
            //shows the info of every node
            out.format("%5s | %-30s | %5s | %-20s | %-50s%n", row[0], row[1], row[2], row[3], row[4]);
        }
        
        out.format("%n");
        showAdjacencyArray(out, adjArray);
        out.format("%n");
    }

    /**
     * Shows the matrix of adjacency
     * @param out formatter that states where to output to
     * @param adjArray adjacency array
     */
    public static void showAdjacencyArray(Formatter out, double[][] adjArray) {
        //calculates a safe distance which is the length of the biggest element
        int safeDistance = getBiggestElemLength(adjArray) + 3;
        
        //writes the header
        writeAdjArrayHeader(adjArray, out, safeDistance, safeDistance);

        for (int linha = 0; linha < adjArray.length; linha++) {
            //before every serie of edges, shows the id of the node
            out.format("%" + (safeDistance) + "s", Configurations.ID_PREFIX + (linha + 1));
            for (int coluna = 0; coluna < adjArray[0].length; coluna++) {
                out.format("%" + (safeDistance) + "s", Math.round(adjArray[linha][coluna]));
            }
            out.format("%n");
        }
    }

    public static int getBiggestElemLength(double[][] adjArray) {
        int biggestLengthElem = Utilities.getBiggestLengthInMatrix(adjArray);

        /* String.valueOf(adsArray.length + Configurations.ID_PREFIX.length()) is the number of digits of the biggest index in the array plus the length of the prefix of the id
        * if the biggest length is smaller than the biggest length in the ids, the biggest length of the ids will replace the matrix one
        * for example if the matrix is 200 * 200, and has just numbers from 0 to 9, the biigest length in the array is 1, but counting with the ids, the biggest id is
        * s200, so the biggest length is 4*/
        if (biggestLengthElem < String.valueOf(adjArray.length + Configurations.ID_PREFIX.length()).length()) {
            biggestLengthElem = String.valueOf(adjArray.length + Configurations.ID_PREFIX.length()).length();
        }
        return biggestLengthElem;
    }

    /**
     * Writes the header of the adjacency array
     *
     * @param adjArray adjacency array
     * @param out formatter that states where to write to
     * @param safeDistance distance between any element to show properly
     * @param inicialSpace space behind it
     */
    public static void writeAdjArrayHeader(double[][] adjArray, Formatter out, int safeDistance, int inicialSpace) {
        out.format("%n%" + (inicialSpace) + "s", " ");
        for (int i = 0; i < adjArray.length; i++) {
            out.format("%" + (safeDistance) + "s", Configurations.ID_PREFIX + (i + 1));
        }
        out.format("%n");
    }

    /**
     * Displays a matrix of doubles with all elements rounded
     *
     * @param outF formatter to where to show
     * @param matrix matrix that we are displaying
     */
    public static void showDoubleMatrixRounded(Formatter outF, double[][] matrix) {
        int biggestLength = Utilities.getBiggestLengthInMatrix(matrix);
        int biggestLengthId = String.valueOf(matrix.length).length() + Configurations.ID_PREFIX.length();
        /* String.valueOf(adsArray.length + Configurations.ID_PREFIX.length()) is the number of digits of the biggest index in the array plus the length of the prefix of the id
		 * if the biggest length is smaller than the biggest length in the ids, the biggest length of the ids will replace the matrix one
		 * for example if the matrix is 200 * 200, and has just numbers from 0 to 9, the biigest length in the array is 1, but counting with the ids, the biggest id is
		 * s200, so the biggest length is 4*/
        if (biggestLength < biggestLengthId) {
            biggestLength = biggestLengthId;
        }

        //safe distance addes 2 to the biggest possible length in the matrix so that values are separated by at least 2 cases or spaces
        int safeDistance = biggestLength + 2;

        //+2 bc of the "|"
        writeAdjArrayHeader(matrix, outF, safeDistance, biggestLengthId + 2);
        outF.format("%n");
        for (int row = 0; row < matrix.length; row++) {
            //adds the id and a "|" at the beggining of each row
            outF.format("%" + biggestLengthId + "s %s", "s" + (row + 1), "|");
            for (int col = 0; col < matrix[row].length; col++) {
                outF.format("%" + (safeDistance) + "d", Math.round(matrix[row][col]));
            }
            //adds "|" at the end of each row
            outF.format("%" + (2) + "s%n", "|");
        }
    }

    /**
     * Gets the power of a matrix but outputs matrix by matrix while it is
     * calculating
     *
     * @param pow the power of the matrix to calculate
     * @param matrix matrix calcultating
     * @param out formatter that states where the output will go to
     */
    public static void getMatrixPowerShowingAllOperations(int pow, double[][] matrix, Formatter out) {
        double[][] newMatrix = matrix;
        out.format("%n");

        //starts by showing the original matrix
        out.format("Potência de expoente 1%n");
        showDoubleMatrixRounded(out, matrix);
        out.format("%n%n");

        for (int xTimes = 1; xTimes < pow; xTimes++) {
            newMatrix = Utilities.multiplyMatrices(newMatrix, matrix);
            //shows the matrix before multiplying again
            out.format("Potência de expoente " + (xTimes + 1) + "%n");
            showDoubleMatrixRounded(out, newMatrix);
            out.format("%n%n");
        }
    }

    /**
     * Calcules the number of edges of a undirected social net
     *
     * @param numOfNodes number of nodes of that net
     * @return the number of edges
     */
    public static int calculateNumberOfEdgesUndirected(int numOfNodes) {
        return ((numOfNodes) * (numOfNodes - 1)) / 2;
    }
}
