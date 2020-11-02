
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void mostraMenu() {
		System.out.println("\n=-----------------------------------------------------------------------------------=");
		System.out.println("1 - Mostrar Todas as Linhas do Arquivo");
		System.out.println("2 - Busca Binária por NOME no arquivo");
		System.out.println("3 - Busca Binária por ID no Indice");
		System.out.println("4 - Listar candidatos da Hashtag 1 - Candidatos sem o ensino fundamental completo");
		System.out.println("5 - Porcentagem de candidatos com ENSINO FUNDAMENTAL INCOMPLETO (Hashtag1)");
		System.out.println("6 - Verificar se candidato está na Hashtag1");
		System.out.println("7 - Porcentagem de candidatos que são trabalhadores rurais (Hashtag2)");
		System.out.println("8 - Cruzar dados de Hashtag1 e Hashtag2 através da pesquisa binária em índice");
		System.out.println("9 - Verificar se candidato está na Hashtag2 em memoria via hashing CHAVE=107");;
		System.out.println("10 - Sair");
		System.out.println("Digite uma opção:");
		System.out.println("=-----------------------------------------------------------------------------------=\n");

	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		Scanner read = new Scanner(System.in);
		Arquivo arquivo = new Arquivo();
		
		int opc = 0;
		
		while(opc != -1) {
			mostraMenu();
			opc = read.nextInt();
			read.nextLine();
			switch(opc) {
			
			case 1:
				arquivo.escreve_arquivo();
				break;
				
			case 2:
				System.out.println("Insira o nome do candidato: \n");
				String nome = read.nextLine();
				arquivo.busca_registro_nome(nome.toUpperCase());
				break;
				
			case 3:
				System.out.println("Insira o ID do candidato: ");
				int id = read.nextInt();
				arquivo.busca_registro_indice(id);
				break;
				
			case 4:
				arquivo.get_allhashtag1();
				break;
				
			case 5:
				int x = arquivo.hashtag1();
				System.out.println(x + " Candidatos tem o status ENSINO FUNDAMENTAL INCOMPLETO ");
				System.out.println("Aproximadamente "+((x*100)/Arquivo.QTD_REG) + "%");
				break;
				
			case 6:
				System.out.println("Insira o ID do candidato: ");
				int id1 = read.nextInt();
				arquivo.get_hashtag1(id1);
				break;
				
			case 7:
				int x1 = arquivo.hashtag2();
				System.out.println(x1 + " Candidatos exercem a profissão de AGRICULTORES ");
				System.out.println("Aproximadamente "+((x1*100)/Arquivo.QTD_REG) + "%");
				break;
				
			case 8:
				arquivo.estatistica();
				break;
				
			case 9:
				ArrayList<String> [] tabela = arquivo.hash();
				System.out.println("Insira o ID do candidato");
				int x2 = read.nextInt();
				int hash = x2 % 106;
				System.out.println("Dado mapeado na posição "+hash);
				for(int i = 0 ; i < tabela[hash].size() ; i++) {
						String value = tabela[hash].get(i).split("#")[0];
						int val = Integer.valueOf(value);
					 	if(val == x2) {
					 		System.out.println("Registro enconstrado, candidato: \n");
					 		System.out.println(arquivo.get_campo(arquivo.get_line(Integer.valueOf(tabela[hash].get(i).split("#")[1])), 5));
					 	}
					 	
					 	if(i == tabela[hash].size() -1) {
					 		System.out.println("Registro nao encontrado!");
					 	}
					 }
				break;
				
			case 10:
				opc = -1;
				break;
				
			
			}
			
		}
	}
}