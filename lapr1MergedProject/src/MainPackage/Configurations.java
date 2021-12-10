/*
 * Projeto de lapr1
 */
package MainPackage;

/**
 * @author StaticVoid
 */
public class Configurations {

	/*
	Error messages
	 */
	public static String PARAMETERS_ERROR = "Por favor verifique os parametros, deverá ser do tipo:\n"
			+ "java -jar nome_do_programa.jar -n (-t -k XX -d YY) rs_nome_da_rede_nos.csv rs_nome_da_rede_ramos.csv";
	public static String CORRECT_CMD_CALL = "java -jar nome_do_programa.jar -n (-t -k XX -d YY) rs_nome_da_rede_nos.csv rs_nome_da_rede_ramos.csv";

	/*
	number of necessary arguments
	 */
	public static int RECURSIVE_UNDIRECTED_N_ARGS = 5;
	public static int NON_RECURSIVE_N_ARGS = 3;
	public static int RECURSIVE_DIRECTED_N_ARGS = 7;

	/*
	position of the arguments in the running command
	 */
	public static int PARAM_OPTION = 0;
	public static int PARAM_NODES_FILE = 1;
	public static int PARAM_EDGES_FILE = 2;
	public static int PARAM_RECURSIVE_POWER = 2;
	public static int PARAM_RECURSIVE_NODES_FILE = 3;
	public static int PARAM_RECURSIVE_EDGES_FILE = 4;
	public static int PARAM_DIRECTED_RECURSIVE_EDGES_FILE = 6;
	public static int PARAM_DIRECTED_RECURSIVE_NODES_FILE = 5;
	public static int PARAM_DAMPING_INDICATIVE = 3;
	public static int PARAM_DAMPING_VALUE = 4;

	/*
	parameters' name configurations
	 */
	public static String PARAM_FILE_PREFIX = "rs";
	public static String PARAM_OPTION_TO_RECURSIVE = "-t";
	public static String PARAM_OPTION_TO_MENU = "-n";
	public static String PARAM_OPTION_INDICATE_POWER = "-k";
	public static String PARAM_OPTION_INDICATE_DAMPING = "-d";

	/*
	files' content configurations
	 */
	public static int EDGES_FILE_N_PARAMS = 3;
	public static int NODES_FILE_N_PARAMS = 5;
	public static String FILES_COLUMNS_SEPARATOR = ",";
	public static String ID_PREFIX = "s";
	public static int HEADER_NUM_OF_LINES_NODES = 2;
	public static int HEADER_NUM_OF_LINES_EDGES = 3;
	public static int EDGES_FILE_COLUMN_FROM = 0;
	public static int EDGES_FILE_COLUMN_TO = 1;
	public static int EDGES_FILE_COLUMN_WEIGHT = 2;
	public static int NODES_FILE_COLUMN_ID = 0;
	public static int NODES_FILE_COLUMN_NAME = 1;
	public static int NODES_FILE_COLUMN_WEB = 4;
	public static int PARAM_NODEMATRIX = 2;

	/*
	social network's permissions
	 */
	public static int MAXIMUM_PERMITTED_WEIGHT = 1;
	public static int MAXIMUM_PERMITTED_NODES = 200;
}
