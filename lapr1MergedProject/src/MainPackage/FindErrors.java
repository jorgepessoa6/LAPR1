package MainPackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FindErrors {

	public static boolean isThereErrors(String[] args) {
		boolean theresError;

		theresError = testErrorsInParams(args) || testErrorsInEdgesFile(args) || testErrorsInNodesFile(args) || !checkIfNetworkIsTheSameForBothFiles(args);

		return theresError;
	}

	public static boolean checkIfNetworkIsTheSameForBothFiles(String[] args) {
		String nodesName = "", edgesName = "";

		//finds the nodes and the edges name
		if (args.length == Configurations.NON_RECURSIVE_N_ARGS) {
			nodesName = args[Configurations.PARAM_NODES_FILE];
			edgesName = args[Configurations.PARAM_EDGES_FILE];
		} else if (args.length == Configurations.RECURSIVE_UNDIRECTED_N_ARGS) {
			nodesName = args[Configurations.PARAM_RECURSIVE_NODES_FILE];
			edgesName = args[Configurations.PARAM_RECURSIVE_EDGES_FILE];
		} else if (args.length == Configurations.RECURSIVE_DIRECTED_N_ARGS) {
			nodesName = args[Configurations.PARAM_DIRECTED_RECURSIVE_NODES_FILE];
			edgesName = args[Configurations.PARAM_DIRECTED_RECURSIVE_EDGES_FILE];
		}

		//gets the social network part of the name in each file
		String netNameInNodes = nodesName.substring(nodesName.indexOf("_") + 1, nodesName.lastIndexOf("_"));
		String netNameInEdges = edgesName.substring(edgesName.indexOf("_") + 1, edgesName.lastIndexOf("_"));

		//returns the equality of the social networks names
		boolean isEqual = true;
		if (!netNameInNodes.equals(netNameInEdges)) {
			System.out.println("A rede selecionada para cada ficheiro é diferente!\n\"" + netNameInNodes + "\" é diferente da \"" + netNameInEdges + "\"");
			isEqual = false;
		}
		return isEqual;
	}

	/**
	 * Tests the errors in the nodes file
	 *
	 * @param args array of the parameters to run the program
	 * @return if theres errors or not
	 */
	public static boolean testErrorsInNodesFile(String[] args) {
		boolean theresError = false;

		Scanner input = getScannerToNodesFile(args);
		if (input == null) {
			theresError = true;
		}

		int lineNum = 1;

		while (!theresError && input.hasNext()) {
			String line = input.nextLine();

			//if we are after the header and theres an error in a line, immedeately return true (theres errors in the file)
			if (lineNum > Configurations.HEADER_NUM_OF_LINES_NODES) {
				//divides each line in a vector to better control it
				String[] row = line.split(Configurations.FILES_COLUMNS_SEPARATOR);
				Utilities.trimAVector(row);

				theresError = verifyNodesErrorsOnOneLine(row, lineNum);
			}

			lineNum++;
		}

		return theresError;
	}

	/**
	 * Verifies if theres errors in a specific line of the row file
	 *
	 * @param row        vector with the line
	 * @param lineNumber line where it is in the file
	 * @return if there is error or not in that specific line
	 */
	public static boolean verifyNodesErrorsOnOneLine(String[] row, int lineNumber) {
		boolean theresError = false;

		//gets the used prefix in the id of the node
		String prefix = row[Configurations.NODES_FILE_COLUMN_ID].substring(0, Configurations.ID_PREFIX.length());

		//if the number of nodes is higher than the maximum permitted it returns error
		if (lineNumber > Configurations.MAXIMUM_PERMITTED_NODES + Configurations.HEADER_NUM_OF_LINES_NODES) {
			System.out.println("O número de nós é maior do que o permitido, o máximo permitido é de " + Configurations.MAXIMUM_PERMITTED_NODES);
			theresError = true;
		} else if (!prefix.equals(Configurations.ID_PREFIX)) { //else if the used prefix is different from the standard, returns error
			System.out.println("O prefixo do nó que está na linha " + lineNumber + " está incorreto. O correto seria " + Configurations.ID_PREFIX);
			theresError = true;
		} else if (row.length != Configurations.NODES_FILE_N_PARAMS) { //else if the number of parameters of one line is different from the standard, returns error
			System.out.println("Na linha " + lineNumber + ", o número de colunas está incorreto, o número correto seria de " + Configurations.NODES_FILE_N_PARAMS);
			theresError = true;
		}
		return theresError;
	}

	/**
	 * Gets the scanner for the nodes´ file
	 *
	 * @param args arguments received in the command to run the program
	 * @return the right scanner for the nodes' file, or null if the file does not exist
	 */
	public static Scanner getScannerToNodesFile(String[] args) {
		Scanner input;

		//if the user chose the recursive or the menu option, the position in the args[] array of the files will be different
		//if the file does not exist, return null and displays a message
		if (args[Configurations.PARAM_OPTION].equals(Configurations.PARAM_OPTION_TO_MENU)) {
			try {
				input = new Scanner(new File(args[Configurations.PARAM_NODES_FILE]));
			} catch (FileNotFoundException e) {
				System.out.println("O ficheiro " + args[Configurations.PARAM_NODES_FILE] + " não existe.");
				input = null;
			}
		} else {
			int nodesFileParam = getParamNodesFile(args);

			try {
				input = new Scanner(new File(args[nodesFileParam]));
			} catch (FileNotFoundException e) {
				System.out.println("O ficheiro " + args[nodesFileParam] + " não existe.");
				input = null;
			}
		}
		return input;
	}

	private static int getParamNodesFile(String[] args) {
		boolean isDirected = (args.length == Configurations.RECURSIVE_DIRECTED_N_ARGS);
		int param;
		if (isDirected) {
			param = Configurations.PARAM_DIRECTED_RECURSIVE_NODES_FILE;
		} else {
			param = Configurations.PARAM_RECURSIVE_NODES_FILE;
		}

		return param;
	}

	/**
	 * Tests if theres errors in the edges file
	 *
	 * @param args arguments received by the user
	 * @return if theres error or not
	 */
	public static boolean testErrorsInEdgesFile(String[] args) {
		boolean theresError = false;

		Scanner input = getScannerEdgesFile(args);
		if (input == null) {
			theresError = true;
		}

		int lineNumber = 1;

		while (!theresError && input.hasNextLine()) {
			String line = input.nextLine();

			if (lineNumber == 1) {
				String[] firstRow = line.split(":");
				Utilities.trimAVector(firstRow);
				theresError = !firstRow[0].equals("networkType") || (!firstRow[1].equals("oriented") && !firstRow[1].equals("nonoriented"));
				if (theresError) {
					System.out.println("Erro ao encontrar o tipo de rede, deve estar do tipo \"networkType:(non)oriented\" na primeira linha do ficheiro de ramos");
				}
			}

			//it can have lines with just spaces so we have to check the length of the array
			if (lineNumber > Configurations.HEADER_NUM_OF_LINES_EDGES) {
				//divides the line of the file in a vector
				String[] row = line.split(Configurations.FILES_COLUMNS_SEPARATOR);
				Utilities.trimAVector(row);

				if (row.length > 1 && verifyEdgesErrorsOnOneLine(row, lineNumber)) {
					theresError = true;
				}
			}
			lineNumber++;
		}

		return theresError;
	}

	/**
	 * Verifies edges errors on one single line
	 *
	 * @param row        array with the line of the edges file
	 * @param lineNumber number of the line to be analyzed
	 * @return if theres errors
	 */
	public static boolean verifyEdgesErrorsOnOneLine(String[] row, int lineNumber) {
		boolean theresError = false;

		//gets the prefix in the file to later compare with the needed one
		String prefixTo = row[Configurations.EDGES_FILE_COLUMN_TO].substring(0, Configurations.ID_PREFIX.length());
		String prefixFrom = row[Configurations.EDGES_FILE_COLUMN_FROM].substring(0, Configurations.ID_PREFIX.length());

		if (row.length != Configurations.EDGES_FILE_N_PARAMS) {
			System.out.println("O número de parametros na linha " + lineNumber + " está incorreto, o correto seria de " + Configurations.EDGES_FILE_N_PARAMS);
			theresError = true;
		} else if (!prefixTo.equals(Configurations.ID_PREFIX) || !prefixFrom.equals(Configurations.ID_PREFIX)) { //if the prefix is different from the standard one
			System.out.println("O prefixo usado na linha " + lineNumber + " está incorreto, o correto seria " + Configurations.ID_PREFIX);
			theresError = true;
		} //sees if the weight is valid
		else if (Integer.parseInt(row[Configurations.EDGES_FILE_COLUMN_WEIGHT]) < 0 || Integer.parseInt(row[Configurations.EDGES_FILE_COLUMN_WEIGHT]) > Configurations.MAXIMUM_PERMITTED_WEIGHT) {
			System.out.println("O peso da relação tem de estar entre 0 e " + Configurations.MAXIMUM_PERMITTED_WEIGHT + " inclusive, verifique a linha " + lineNumber);
			theresError = true;
		} //sees if it is a self loop
		else if (row[Configurations.EDGES_FILE_COLUMN_FROM].equals(row[Configurations.EDGES_FILE_COLUMN_TO])) {
			System.out.println("Um nó não pode estar ligado a si mesmo, verifique a linha " + lineNumber + " do ficheiro de ramos");
			theresError = true;
		}
		return theresError;
	}

	/**
	 * Gets the correct scanner for the edges file depending on option chosen by the user
	 *
	 * @param args arguments received by the user
	 * @return the correct scanner or null if the file does not exist
	 */
	public static Scanner getScannerEdgesFile(String[] args) {
		Scanner input;
		//if it is the interactive way
		if (args[Configurations.PARAM_OPTION].equals(Configurations.PARAM_OPTION_TO_MENU)) {

			//if the file does not exist, return null and a message
			try {
				input = new Scanner(new File(args[Configurations.PARAM_EDGES_FILE]));
			} catch (FileNotFoundException e) {
				System.out.println("O ficheiro " + args[Configurations.PARAM_EDGES_FILE] + " não existe.");
				return null;
			}

			//if it is the recursive way
		} else {

			int edgesParam = getParamEdgesFile(args);

			try {
				input = new Scanner(new File(args[edgesParam]));
			} catch (FileNotFoundException e) {
				System.out.println("O ficheiro " + args[edgesParam] + " não existe.");
				return null;
			}
		}
		return input;
	}

	private static int getParamEdgesFile(String[] args) {
		boolean isDirected = (args.length == Configurations.RECURSIVE_DIRECTED_N_ARGS);
		int param;
		if (isDirected) {
			param = Configurations.PARAM_DIRECTED_RECURSIVE_EDGES_FILE;
		} else {
			param = Configurations.PARAM_RECURSIVE_EDGES_FILE;
		}

		return param;
	}

	/**
	 * Tests errors in the command that runs the program
	 *
	 * @param args arguments received by the user
	 * @return if there is error or not
	 */
	public static boolean testErrorsInParams(String[] args) {
		boolean theresError;

		theresError = verifyErrorNumParams(args);

		if (!theresError) {
			String option = args[Configurations.PARAM_OPTION];
			String nodesName = "";
			String edgesName = "";

			if (optionIsMisplaced(option, args)) {
				theresError = true;

			} //3 to the non recursvie option and else to the recursive option
			else if (args.length == Configurations.NON_RECURSIVE_N_ARGS) {
				nodesName = args[Configurations.PARAM_NODES_FILE];
				edgesName = args[Configurations.PARAM_EDGES_FILE];
			} else if (args.length == Configurations.RECURSIVE_UNDIRECTED_N_ARGS) {
				nodesName = args[Configurations.PARAM_RECURSIVE_NODES_FILE];
				edgesName = args[Configurations.PARAM_RECURSIVE_EDGES_FILE];
				theresError = testIfCallsTheKInorrectly(args);
			} else if (args.length == Configurations.RECURSIVE_DIRECTED_N_ARGS) {
				nodesName = args[Configurations.PARAM_DIRECTED_RECURSIVE_NODES_FILE];
				edgesName = args[Configurations.PARAM_DIRECTED_RECURSIVE_EDGES_FILE];
				theresError = (testIfCallsTheKInorrectly(args) || testIfCallsTheDampIncorrectly(args));
			}

			//To finish, checks if the files' names are spelled wrong, "!theresError" bc if it has already errors, it can "jump" this step to increase he speed
			if (!theresError && checkIfFilesNamesWrong(nodesName, edgesName)) {
				theresError = true;
			}
		}
		return theresError;
	}

	/**
	 * Checks if the files names are wrong
	 *
	 * @param nodesName the name of the dodes file
	 * @param edgesName the name of the edges file
	 * @return true if they are not correct
	 */
	public static boolean checkIfFilesNamesWrong(String nodesName, String edgesName) {
		boolean theresError = false;

		//if the nodes file does not start with the prefix ("rs" in this case) or does not end with "nos"
		if (!nodesName.substring(0, Configurations.PARAM_FILE_PREFIX.length()).equals(Configurations.PARAM_FILE_PREFIX) || !nodesName.substring(nodesName.lastIndexOf("_") + 1).equals("nos.csv")) {
			System.out.println("O ficheiro relativo aos nós está incorreto, um nome correto seria: rs_nome_da_rede_nos.csv\nDe relembrar que primeiro vem o ficheiro de nós e apenas depois o ficheiro dos ramos");
			theresError = true;
		} else if (!edgesName.substring(0, Configurations.PARAM_FILE_PREFIX.length()).equals(Configurations.PARAM_FILE_PREFIX) || !edgesName.substring(edgesName.lastIndexOf("_") + 1).equals("ramos.csv")) {
			System.out.println("O ficheiro relativo aos ramos está incorreto, um nome correto seria: rs_nome_da_rede_ramos.csv\nDe relembrar que primeiro vem o ficheiro de nós e apenas depois o ficheiro dos ramos");
			theresError = true;
		}
		return theresError;
	}

	/**
	 * Checks if the k parameter is called incorrectly
	 *
	 * @param args arguments called by user
	 * @return true if it has been incorrectly
	 */
	public static boolean testIfCallsTheKInorrectly(String[] args) {
		//starts with not having errors and if it finds one, returns true
		boolean theresError = false;

		//if it is different from the standard
		if (!args[1].equalsIgnoreCase(Configurations.PARAM_OPTION_INDICATE_POWER)) {
			System.out.println(Configurations.PARAMETERS_ERROR);
			theresError = true;
		} else if (!Utilities.verifyIfInt(args[Configurations.PARAM_RECURSIVE_POWER]) || Integer.parseInt(args[Configurations.PARAM_RECURSIVE_POWER]) <= 0) { //if the power is not an int
			System.out.println("Após -k, deve introduzir um número natural");
			theresError = true;
		}
		return theresError;
	}

	/**
	 * Checks if the k parameter is called incorrectly
	 *
	 * @param args arguments called by user
	 * @return true if it has been incorrectly
	 */
	public static boolean testIfCallsTheDampIncorrectly(String[] args) {
		//starts with not having errors and if it finds one, returns true
		boolean theresError = false;

		//if it is different from the standard
		if (!args[Configurations.PARAM_DAMPING_INDICATIVE].equalsIgnoreCase(Configurations.PARAM_OPTION_INDICATE_DAMPING)) {
			System.out.println(Configurations.PARAMETERS_ERROR);
			theresError = true;
		} else if (!Utilities.verifyIfFloat(args[Configurations.PARAM_DAMPING_VALUE]) || Float.parseFloat(args[Configurations.PARAM_DAMPING_VALUE]) < 0 || Float.parseFloat(args[Configurations.PARAM_DAMPING_VALUE]) > 1) {
			System.out.println("Após " + Configurations.PARAM_OPTION_INDICATE_DAMPING + ", deve introduzir um número no intervalo [0, 1]");
			theresError = true;
		}
		return theresError;
	}

	/**
	 * Checks if the option argument is misspelled
	 *
	 * @param option parameter correspondent to the option argument
	 * @param args   arguments received by the user
	 * @return true if the option is misspelled
	 */
	public static boolean optionIsMisplaced(String option, String[] args) {
		boolean theresError = false;

		if (!option.equals(Configurations.PARAM_OPTION_TO_MENU) && !option.equals(Configurations.PARAM_OPTION_TO_RECURSIVE)) {
			//if the choosen option is not available
			System.out.println("As únicas opções disponíveis são " + Configurations.PARAM_OPTION_TO_MENU + " e " + Configurations.PARAM_OPTION_TO_RECURSIVE);
			theresError = true;
		} else if (option.equals(Configurations.PARAM_OPTION_TO_RECURSIVE) && (args.length != Configurations.RECURSIVE_UNDIRECTED_N_ARGS
				& args.length != Configurations.RECURSIVE_DIRECTED_N_ARGS)) {
			//if chooses the recursive option, has to have 5 parameters or 7
			System.out.println("Com o " + Configurations.PARAM_OPTION_TO_RECURSIVE + ", o comando tem de ter " + Configurations.RECURSIVE_UNDIRECTED_N_ARGS + " parametros "
					+ "ou " + Configurations.RECURSIVE_DIRECTED_N_ARGS + ", como por exemplo:"
					+ "\n" + Configurations.CORRECT_CMD_CALL);
			theresError = true;
		} else if (option.equals(Configurations.PARAM_OPTION_TO_MENU) && args.length != Configurations.NON_RECURSIVE_N_ARGS) {
			// if it is choosen the non recursive it has to have 3 parameters
			System.out.println("Com o " + Configurations.PARAM_OPTION_TO_MENU + ", o comando tem de ter " + Configurations.NON_RECURSIVE_N_ARGS + " parametros, como por exemplo:\n" + Configurations.CORRECT_CMD_CALL);
			theresError = true;
		}
		return theresError;
	}

	private static boolean isTheProgramRecursive(String arg) {
		boolean isRecursive;

		isRecursive = !arg.equals(Configurations.PARAM_OPTION_TO_MENU);

		return isRecursive;
	}

	/**
	 * Verifies if theres errors in the number of params
	 *
	 * @param args parameters inserted by the user
	 * @return if theres error in the number of them
	 */
	private static boolean verifyErrorNumParams(String[] args) {
		int nArgs = args.length;

		boolean theresError = false;
		if (nArgs != Configurations.NON_RECURSIVE_N_ARGS && nArgs != Configurations.RECURSIVE_DIRECTED_N_ARGS
				&& nArgs != Configurations.RECURSIVE_UNDIRECTED_N_ARGS) {

			theresError = true;

			//warning message
			System.out.println("O número de parametros introduzidos está incorreto, o correto seria "
					+ Configurations.NON_RECURSIVE_N_ARGS + " para quando se é usado a opção interativa, ou "
					+ Configurations.RECURSIVE_UNDIRECTED_N_ARGS + " para a opção recursiva sendo uma rede não orientada, ou "
					+ Configurations.RECURSIVE_DIRECTED_N_ARGS + " para a opção recursiva senda uma rede orientada\n"
					+ "E devem ser colocados na forma " + Configurations.CORRECT_CMD_CALL);
		}
		return theresError;
	}
}
