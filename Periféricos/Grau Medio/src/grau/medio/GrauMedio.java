package grau.medio;

public class GrauMedio {

    public static double averageDegree(int nNodes, double sum, int[][] matriz) {
        double averageDegree = sumDegree(nNodes, matriz) / nNodes;

        return averageDegree;
    }

    public static double sumDegree(int nNodes, int[][] matriz) {
        double sum = 0;
        for (int j = 0; j < matriz.length; j++) {
            sum += calcNodeDegree(j, matriz);
        }
        return sum;
    }

    public static int calcNodeDegree(int j, int[][] matriz) {
        int nodeDegree = 0;
        for (int i = 0; i < matriz.length; i++) {
            nodeDegree += matriz[j][i];
        }
        return nodeDegree;
    }
}
