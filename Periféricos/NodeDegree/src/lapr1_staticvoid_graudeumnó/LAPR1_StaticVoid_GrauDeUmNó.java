package lapr1_staticvoid_graudeumnó;

import java.util.Scanner;

public class LAPR1_StaticVoid_GrauDeUmNó {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean errorOcurred = false;
        int nodeDegree = 0;
        String node = "";
        int nodeID = 0;
        int[][] matriz = {{1, 0, 1}, {1, 1, 1}, {0, 0, 0}};
        nodeDegree = validateNodes(node, errorOcurred, input, nodeID, matriz, nodeDegree);
        System.out.println("\nGrau do nó: " + nodeDegree);
        //TESTE:        
        int expectedNodeDegree = 3;
        if (testCalcNodeDegree(matriz, nodeID, nodeDegree, expectedNodeDegree)) {
            System.out.println("\nTESTE APROVADO");
        } else {
            System.out.println("\nTESTE REPROVADO");
        }
        input.close();
    }    

    public static int validateNodes(String node, boolean errorOcurred, Scanner input, int nodeID, int[][] matriz, int nodeDegree) {
        do {
            System.out.println("\nIntroduza o ID do nó que deseja calcular o seu grau: ");
            node = input.nextLine();
            errorOcurred = false;
            if (!node.substring(0, 1).equals("s")) {
                System.out.println("\nEstrutura de ID incorrecta. A estrutura é sXX; sendo XX um número.");
                errorOcurred = true;
            } else {
                nodeID = Integer.parseInt(node.substring(1));
                if (nodeID > matriz.length) {
                    System.out.println("\nID inexistente.");
                    errorOcurred = true;
                } else {
                    nodeDegree = calcNodeDegree(matriz, nodeID, nodeDegree);
                }

            }
        } while (errorOcurred);
        return nodeDegree;
    }
    
    public static int calcNodeDegree(int[][] matriz, int nodeID, int nodeDegree) {
        int nodeIndex = nodeID - 1;
        for (int i = 0; i < matriz.length; i++) {
            nodeDegree += matriz[nodeIndex][i];
        }
        return nodeDegree;
    }
    
    public static boolean testCalcNodeDegree (int[][] matriz, int nodeID, int nodeDegree, int expectedNodeDegree) {
        nodeDegree = calcNodeDegree(matriz, nodeID, nodeDegree);
        if (expectedNodeDegree == nodeDegree) {
            return true;
        }
        return false;        
    }

}
