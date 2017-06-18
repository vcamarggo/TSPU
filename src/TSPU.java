import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author V.Camargo
 * 
 * @Date 10/02/2016
 */

public class TSPU {
	static int[][] matrizRecebida;
	static int[][] custoSolucao;
	static int[][] caminhoSolucao;
	static int linha;
	static int coluna;

	public static void recebeInput() {
		Scanner scan = new Scanner(System.in);
		linha = scan.nextInt();
		coluna = scan.nextInt();
		matrizRecebida = new int[linha][coluna];
		custoSolucao = new int[linha][coluna];
		caminhoSolucao = new int[linha][coluna];
		for (int i = 0; i < linha; i++)
			for (int j = 0; j < coluna; j++) {
				matrizRecebida[i][j] = scan.nextInt();
				custoSolucao[i][j] = Integer.MAX_VALUE;
			}
		scan.close();
	}

	static int PD(int i, int j) {
		if (j >= coluna)
			return 0;
		if (custoSolucao[i][j] != Integer.MAX_VALUE)
			return custoSolucao[i][j];

		int[] p = { i - 1, i, i + 1 };
		if (i == 0)
			p[0] = linha - 1;
		if (i == linha - 1)
			p[2] = 0;

		for (int k = 0; k < 3; k++) {
			int val = matrizRecebida[i][j] + PD(p[k], j + 1);
			if (custoSolucao[i][j] > val
					|| (custoSolucao[i][j] == val && caminhoSolucao[i][j] > p[k])) {
				custoSolucao[i][j] = val;
				caminhoSolucao[i][j] = p[k];
			}
		}
		return custoSolucao[i][j];
	}

	public static void imprimeCaminhoETotalPD(int r, int bestPD) {
		System.out.println("Solução com PD: ");
		System.out.print("Linhas: ");
		for (int j = 0; j < coluna; j++) {
			if (j != 0) {
				System.out.print(" ");
			}
			System.out.print((r + 1));
			r = caminhoSolucao[r][j];
		}
		System.out.println("");
		System.out.println("Total:" + bestPD);
	}

	public static void imprimePD() {
		int bestPD = Integer.MAX_VALUE;
		int r = 0;
		for (int i = 0; i < linha; i++) {
			PD(i, 0);
			if (custoSolucao[i][0] < bestPD) {
				bestPD = custoSolucao[i][0];
				r = i;
			}
		}
		imprimeCaminhoETotalPD(r, bestPD);
	}

	static int notPD(int i, int j) {
		if (j >= coluna)
			return 0;

		int[] p = { i - 1, i, i + 1 };
		if (i == 0)
			p[0] = linha - 1;
		if (i == linha - 1)
			p[2] = 0;
		for (int k = 0; k < 3; k++) {
			int val = matrizRecebida[i][j] + notPD(p[k], j + 1);
			if (custoSolucao[i][j] > val
					|| (custoSolucao[i][j] == val && caminhoSolucao[i][j] > p[k])) {
				custoSolucao[i][j] = val;
				caminhoSolucao[i][j] = p[k];
			}
		}
		return custoSolucao[i][j];
	}

	public static void imprimeCaminhoETotalNotPD(int r, int bestNotPD) {
		System.out.println("Solução sem PD: ");
		System.out.print("Linhas: ");
		for (int j = 0; j < coluna; j++) {
			if (j != 0) {
				System.out.print(" ");
			}
			System.out.print((r + 1));
			r = caminhoSolucao[r][j];
		}
		System.out.println("");
		System.out.println("Total:" + bestNotPD);
	}

	public static void imprimeNotPD() {
		int bestNotPD = Integer.MAX_VALUE;
		int r = 0;
		for (int i = 0; i < linha; i++) {
			int solucaoAUX = notPD(i, 0);
			if (solucaoAUX < bestNotPD) {
				bestNotPD = solucaoAUX;
				r = i;
			}
		}
		imprimeCaminhoETotalNotPD(r, bestNotPD);
	}

	static int greedy(int i, int j) {
		int[] p = { i - 1, i, i + 1 };
		if (i == 0)
			p[0] = linha - 1;
		if (i == linha - 1)
			p[2] = 0;
		System.out.print((i + 1) + " ");

		if (j >= coluna - 1)
			return matrizRecebida[i][j];

		int k;
		if (matrizRecebida[p[0]][j + 1] <= matrizRecebida[p[1]][j + 1]
				&& matrizRecebida[p[0]][j + 1] <= matrizRecebida[p[2]][j + 1]) {
			k = 0;
		} else if (matrizRecebida[p[1]][j + 1] <= matrizRecebida[p[0]][j + 1]
				&& matrizRecebida[p[1]][j + 1] <= matrizRecebida[p[2]][j + 1]) {
			k = 1;
		} else {
			k = 2;
		}

		int val = matrizRecebida[i][j] + greedy(p[k], j + 1);

		return val;
	}

	public static void imprimeGreedy() {
		System.out.println("Solução com AG: ");
		System.out.print("Linhas: ");
		int i = 0;
		for (int k = 0; k < linha; k++) {
			if (matrizRecebida[k][0] < matrizRecebida[i][0]) {
				i = k;
			}
		}
		int bestAG = greedy(i, 0);
		System.out.println();
		System.out.println("Total: " + bestAG);
	};

	public static void main(String[] args) {

		recebeInput();
		long inicio;
		long fim;

		System.out.println();
		inicio = System.nanoTime();
		imprimeNotPD();
		fim = System.nanoTime();
		System.out.println("Tempo aproximado de execução em segundos: "
				+ TimeUnit.SECONDS
						.convert((fim - inicio), TimeUnit.NANOSECONDS));

		System.out.println();
		inicio = System.nanoTime();
		imprimePD();
		fim = System.nanoTime();
		System.out.println("Tempo aproximado de execução em segundos: "
				+ TimeUnit.SECONDS
						.convert((fim - inicio), TimeUnit.NANOSECONDS));

		System.out.println();
		inicio = System.nanoTime();
		imprimeGreedy();
		fim = System.nanoTime();
		System.out.println("Tempo aproximado de execução em segundos: "
				+ TimeUnit.SECONDS
						.convert((fim - inicio), TimeUnit.NANOSECONDS));
	}
}
