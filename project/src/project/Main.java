package project;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Main {

	private static final int kOPC_MOSTRAR_DADOS = 1;
	private static final int kOPC_PESQUISA_BINARIA = 2;

	private static final int kINVALID_VALUE = -1;

	private static final int kREGISTER_LINE_SIZE = 800;

	private static final int kQTD_REGISTERS = 10000;

	private static final String kREGISTERS_FILE_NAME = "../Data/consulta_cand_2020_RS.csv";

	public static void main(String[] args) throws IOException {

		int opc = kINVALID_VALUE;

		RandomAccessFile randomAccessFile = null;

		Scanner sc = new Scanner(System.in);

		try {
			randomAccessFile = new RandomAccessFile(kREGISTERS_FILE_NAME, "rw");	

		} catch (Exception e) {

			System.out.println("Nao foi possivel abrir o arquivo de registros");
		}
		

		while(true){

			showMenu();

			opc = sc.nextInt();

			sc.close();

			switch(opc){
			
				case kOPC_MOSTRAR_DADOS:

					System.out.println();

					showAllRegisters(randomAccessFile);

				break;

				case kOPC_PESQUISA_BINARIA:
					
				break;
			}
		}		
	}
	
	public static void showMenu() {
		System.out.println("\n\nOpcoes:");
		System.out.println("1- Mostrar Dados");
		System.out.println("2- Pesquisa Binaria");
		System.out.println("3- Sair");
		System.out.print("Opcao escolhida: ");
	}

	public static void showAllRegisters(RandomAccessFile randomAccessFile) throws IOException {

		char[] charReadLine = new char[kREGISTER_LINE_SIZE];

		int lineCount = 0, seekPos = 0, i = 0;

		String currentLine = null;

		randomAccessFile.seek(seekPos);

		while(lineCount < kQTD_REGISTERS)
		{
			for(i=0 ; i<kREGISTER_LINE_SIZE ; i++) {

				charReadLine[i] = (char)randomAccessFile.readByte();
	        	
			}
	        
	        currentLine = new String(charReadLine);
	        
	        System.out.print(currentLine);

	        seekPos += kREGISTER_LINE_SIZE;

	        randomAccessFile.seek(seekPos);

	        lineCount++;
		}		
	}
}