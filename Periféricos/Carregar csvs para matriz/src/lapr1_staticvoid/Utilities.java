package lapr1_staticvoid;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
 * Projeto de Lapr1 - StaticVoid
 */
/**
 *
 * @author Marcio Duarte
 */
public class Utilities {

	/**
	 * Counts the number of lines of a file
	 *
	 * @param file file in question
	 * @return number of lines of that file
	 */
	public static int countNumOfLinesFile(String file) {
		int count = 0;
		Scanner scan;
		try {
			scan = new Scanner(new File(file));

			while (scan.hasNextLine()) {
				scan.nextLine();
				count++;
			}
			scan.close();

		} catch (FileNotFoundException ex) {
			System.out.println("\nThe file " + file + " does not exist");
		}

		return count;
	}
}
