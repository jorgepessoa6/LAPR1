/*
 * Projeto de Lapr1 - StaticVoid
 */
package Tests;

import java.io.FileNotFoundException;
import lapr1_staticvoid.StaticVoid;

/**
 *
 * @author Marcio Duarte
 */
public class testLoadFiles {

	public static void main(String[] args) throws FileNotFoundException {
		String[] csvFilesTest = {"-n", "test1.csv", "test2.csv"};
		int[][] expected = {{0, 1, 1, 1, 0, 1}, {1, 0, 0, 0, 1, 0}, {1, 0, 0, 1, 0, 0}, {1, 0, 1, 0, 0, 0}, {0, 1, 0, 0, 0, 0}, {1, 0, 0, 0, 0, 0}};

		System.out.println(testLoadFiles(csvFilesTest, expected));
	}

	public static boolean testLoadFiles(String[] csvFilesTest, int[][] expected) throws FileNotFoundException {
		double[][] obtainedMatrix = StaticVoid.convertFilesToAdjacencyArray(csvFilesTest);
		boolean res = true;

		for (int row = 0; row < expected.length; row++) {
			for (int column = 0; column < expected[row].length; column++) {
				if (obtainedMatrix[row][column] != expected[row][column]) {
					return false;
				}
			}
		}

		return res;
	}
}
