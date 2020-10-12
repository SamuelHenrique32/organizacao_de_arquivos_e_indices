package project;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

/*
 * Line 22888 is the bigger, length = 800
 * */

public class Main {

	public static void main(String[] args) throws IOException {
		
		int maxSize = -1;
		
		BufferedReader myBuffer = new BufferedReader(new InputStreamReader(new FileInputStream("../Data/consulta_cand_2020_RS.csv"), "ISO-8859-1"));

		OutputStream outputStream = new FileOutputStream("../Data/saida.txt");

		StringBuilder strBuilder = null;
		
		String linha = myBuffer.readLine();

		while(linha != null) {
			
			if(linha.length() > maxSize) {
				maxSize = linha.length();
			}
			
			if(linha.length() == 800) {
				System.out.println(linha);
			}

			strBuilder = new StringBuilder(linha);
			
			for(int i=linha.length() ; i<799 ; i++) {
				strBuilder.append(" ");
			}
			
			strBuilder.append("\n");
					
			outputStream.write(strBuilder.toString().getBytes());
				
			outputStream.flush();			

			linha = myBuffer.readLine();
		}

		System.out.println(maxSize);

		myBuffer.close();
	}
}