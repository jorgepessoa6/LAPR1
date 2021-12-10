package lapr1_staticvoid;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
 * Projeto de Lapr1 - StaticVoid
 *
 *
 * @author Marcio Duarte
 */
public class StaticVoid {

	public static void main(String[] args) throws FileNotFoundException {
		/*TODO:faz as verificações do numero de parametros e se eles sao falsos
		se retornar null é porque houve erro, podes parar o programa
		*/
		convertFilesToAdjacencyArray(args);
	}

	/**
	 * Converts the files that will be passed as params to an adjacency array
	 *
	 * @param args files passed as params
	 * @return the adjacency array, or null if it is impossible to do it
	 * @throws FileNotFoundException
	 */
	public static double[][] convertFilesToAdjacencyArray(String[] args) throws FileNotFoundException {
		int numOfNodes = Utilities.countNumOfLinesFile(args[Configurations.PARAM_NODES_FILE]) - Configurations.HEADER_NUM_OF_LINES;

		if (numOfNodes <= 0) {
			System.err.println("\nThe file " + args[Configurations.PARAM_NODES_FILE] + " is invalid");
			return null;
		}

		double[][] adjArray = new double[numOfNodes][numOfNodes];

		fillAdjArray(args[Configurations.PARAM_EDGES_FILE], adjArray);

		return adjArray;
	}

	/**
	 * Fullfills an array name adjArray (from adjacency array) with the informations provided in a csv file that describes the edges of the social network
	 *
	 * @param adjArray array that is being fullfilled
	 * @param edgesFile txt files with the edges of the social network info
	 * @throws FileNotFoundException
	 */
	public static void fillAdjArray(String edgesFile, double[][] adjArray) throws FileNotFoundException {
		Scanner scan = new Scanner(new File(edgesFile));

		while (scan.hasNextLine()) {

			String line = scan.nextLine();

			//if the line starts with "s" and has more than 1 char, it means it is a parameter and not the header
			if (line.length() > 0 && line.substring(Configurations.EDGES_FILE_COLUMN_FROM, Configurations.EDGES_FILE_COLUMN_FROM + Configurations.ID_PREFIX.length()).equals(Configurations.ID_PREFIX)) {
				fillAdjArrayEdge(line, adjArray);
			}
		}

		scan.close();
	}

	/**
	 * Fullfills a sigle row of the adjacency array with a line of String
	 *
	 * @param line String with a single line of the csv file
	 * @param adjArray Adjacency array being fullfiled
	 * @throws NumberFormatException
	 */
	public static void fillAdjArrayEdge(String line, double[][] adjArray) throws NumberFormatException {
		//divides eache line in a vector
		String[] rowVector = line.split(Configurations.FILES_COLUMNS_SEPARATOR);

		//getting the id of the node in a string to transform it in int to put in the matrix
		String adjArrayFromString = rowVector[Configurations.EDGES_FILE_COLUMN_FROM].trim().substring(Configurations.ID_PREFIX.length()); // <- to ignore the Id prefix
		String adjArrayToString = rowVector[Configurations.EDGES_FILE_COLUMN_TO].trim().substring(Configurations.ID_PREFIX.length());
		String adjArrayWeightString = rowVector[Configurations.EDGES_FILE_COLUMN_WEIGHT].trim();

		int adjArrayRow = Integer.parseInt(adjArrayFromString) - 1; //1 because the array starts at 0, not 1
		int adjArrayColumn = Integer.parseInt(adjArrayToString) - 1;
		int adjArrayWeight = Byte.parseByte(adjArrayWeightString);

		adjArray[adjArrayRow][adjArrayColumn] = adjArrayWeight;
		adjArray[adjArrayColumn][adjArrayRow] = adjArrayWeight;
	}
}
