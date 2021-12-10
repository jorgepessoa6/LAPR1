/**
 * Cálculo da densidade de uma rede social.
 */
package lapr1_projeto_staticvoid09;

public class Densidade {

    static int[] matriz = new int[12]; // varável definida conforme o númeor de elementos lidos no ficheiro

    public static void main(String[] args) {
        System.out.printf("Denisade da rede social %.3f%n", CalculoDensidade(matriz));
        float densidadeExpected = CalculoDensidade(matriz);
        if (TestCalculoDensidade(matriz,densidadeExpected)==true) { // comparação das duas matrizes, conferindo se são ou não iguais.
            System.out.println("TESTE APROVADO!!!");
            
        } else {
            System.out.println("TESTE REPROVADO!!!");
        }    
        }

    

    private static float CalculoDensidade(int[] matriz) { // cálculo da densidade da matriz
        int num = matriz.length;
        int nMaxNos = num * (num - 1) / 2; // máximo de nós possíveis
        float densidade = ((float) num / (nMaxNos));

        return densidade;
    }

    private static boolean TestCalculoDensidade(int[] matriz, double densidadeExpected) { // cálculo da matriz teste
        float densidade = CalculoDensidade(matriz);
        if (densidade == densidadeExpected) {
            return true;
        }
        return false;
    }
}
