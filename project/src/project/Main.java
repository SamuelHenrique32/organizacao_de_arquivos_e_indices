package project;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * Line 22888 is the bigger, length = 800
 * */

public class Main {

	public static void main(String[] args) throws IOException {
		
		int maxSize = -1;
		
		BufferedReader myBuffer = new BufferedReader(new InputStreamReader(new FileInputStream("../Data/consulta_cand_2020_RS.csv"), "ISO-8859-1"));

		String linha = myBuffer.readLine();

		while(linha != null) {
			
			if(linha.length() > maxSize) {
				maxSize = linha.length();
			}
			
			if(linha.length() == 800) {
				System.out.println(linha);
			}

			linha = myBuffer.readLine();
		}

		System.out.println(maxSize);

		myBuffer.close();
	}
}