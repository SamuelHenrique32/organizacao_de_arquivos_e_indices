import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.util.ArrayList;

public class Arquivo {
	
		final int SIZE_CHAR = 2;
		final static int N_CAMPOS = 13;
		final static int [] TAM = {5,
		                           26,
		                           2,
		                           13,
		                           5,
		                           49,
		                           2,
		                           13,
		                           9,
		                           1,
		                           29,
		                           3,
		                           70};
		final static int LINE_SIZE = 229;
		final static int INDICE_SIZE = 13;
		final static int QTD_REG = 10000;
	    RandomAccessFile arquivo;
	    RandomAccessFile dados;
	    RandomAccessFile indice_id;
	    RandomAccessFile hashtag1;
	    RandomAccessFile hashtag2;

	    
	    
	    public Arquivo() throws FileNotFoundException, IOException{
	         arquivo = new RandomAccessFile("./consulta_cand_2020_RS.csv", "rw");
	         dados = new RandomAccessFile("./consulta_cand_2020_RS.dat", "rw");
	         indice_id = new RandomAccessFile("./indice_id.dat", "rw");
	         hashtag1 = new RandomAccessFile("./hashtag1.dat", "rw");
	         hashtag2 = new RandomAccessFile("./hashtag2.dat", "rw");
	         System.out.println(arquivo.length() + " bytes \n");
	    }
	    
	    public long conta_campo(int index) throws FileNotFoundException, IOException{
	    	arquivo.seek(0);
	    	String line = arquivo.readLine();
	    	long max = 0;
	    	
	    	while(line != null) {
	    		String [] linha = line.split(";");
	    		if(linha[index].length() > max) {
	    			
	    			max = linha[index].length();
	    		}
	    		line = arquivo.readLine();
	    	}
	    	
	    	return max;
	    	
	    }
	    
	    public void le_arquivo() throws IOException {
	    	
	    	long tamanhos[] = new long[N_CAMPOS]; 
	    	
	    	for(int i = 0; i < N_CAMPOS ;i++) {
	    		tamanhos[i] = conta_campo(i);
	    	}
	    	
	    	arquivo.seek(0);
	    	dados.seek(0);
	    	
	    	String line = arquivo.readLine();
	    		
	    		while(line != null) {
	    		
	    			String [] linha = line.split(";");
	    			
	    			for(int j = 0; j < N_CAMPOS; j++) {
	    				String att = linha[j];
	    				
	    				if(att.length() < tamanhos [j]) {
	    					for(int k = att.length() ; k < tamanhos[j] ; k++) {
	    						att+= " ";
	    					}
	    				}
	    				
	    				
	    				dados.write(att.getBytes());
	    				
	    				
	    			}
	    			
	    			dados.writeChar(10);
	    			line = arquivo.readLine();
	    		}
			
	    	
	    	arquivo.close();
	    	dados.close();
	    }
	    
	    public void escreve_arquivo() throws IOException {
	    	dados.seek(0);
	    	char [] linha = new char[LINE_SIZE];
	    	for(int i = 0; i < QTD_REG ; i++) {
	    		for(int j = 0; j < LINE_SIZE  ;j++) {
	    			linha[j] = (char) dados.readByte();
	    		}
	    		System.out.println(String.valueOf(linha));
	    	}
	    }
	    
	    public char[] get_line(int offset) throws IOException {
	    	dados.seek(offset);
	    	char [] linha = new char[LINE_SIZE];
	    	for(int j = 0; j < LINE_SIZE ;j++) {
    			linha[j] = (char) dados.readByte();
    		}
	    	
	    	return linha;
	    }
	    
	    public String get_campo(char[] linha,int index) {
	    	String campo = "";
	    	int pos = 0;
	    	for(int i = 0 ; i < index ; i++) {
	    		pos+= TAM[i];
	    	}
	    	
	    	for(int i = pos ; i < pos + TAM[index] ; i++) {
	    		campo+=linha[i];
	    	}
	    	campo = campo.strip();
	    	return campo;
	    }

	    public int busca_binaria(int index, String key) throws FileNotFoundException, IOException{
	        
	        int begin = 0;
	        int end = QTD_REG - 1;
	        int i;
	        int offset;
	        while (begin < end) {  /* Condição de parada */
	        	i = (begin + end) / 2;  /* Calcula o meio do sub-vetor */
	        	offset = i * LINE_SIZE;
	        	char[] linha = get_line(offset);
	        	String campo = get_campo(linha, index);
	        	
	        	if (campo.compareTo(key) == 0) {  /* Item encontrado */
	        		return offset;
	        	}else
	        		if (campo.compareTo(key) < 0) {  /* Item está no sub-vetor à direita */
	        			begin = i + 1;
	        		} else {  /* vector[i] > item. Item está no sub-vetor à esquerda */
	        			end = i;
	        		}
	     }

	     return -1;
	        
	        
	     }
	    
	    public void busca_registro_nome (String key) throws FileNotFoundException, IOException {
	    	int index = busca_binaria(5, key);
	    	
	    	if(index == -1) {
	    		System.out.println("Registro não encontrado");
	    		return;
	    	}
	    	
	    	info(get_line(index));
	    	
	    }
	    
	    public void info (char[] line) {
	    	String [] campos = new String[N_CAMPOS];
	    	for(int i = 0 ; i < N_CAMPOS ; i++) {
	    		campos[i] = get_campo(line, i);
	    	}
	    	
	    	String info = "Candidato: \n" + campos[4] + " -- " +
	    				  "Nome: " + campos[5] + "\n \n" +
	    				  "Candidato a " + campos[3] + "\n" +
	    				  "Municipio: " + campos[1] + "\n" +
	    				  "Partido: " + campos[7];
	    	System.out.println(info + "\n");
	    }
	    
	    public static String completeToLeft(String value, char c, int size) {
			String result = value;

			while (result.length() < size) {
				result = c + result;
			}

			return result;
		}
	    
	    public void cria_indice_id () throws IOException {
	    	indice_id.seek(0);
	    	int cont = 0;
	    	String id = String.valueOf(cont);
	    	id = completeToLeft(id, '0', 4);
	    	for (long i = 0 ; i < dados.length() ; i+=LINE_SIZE) {
	    		indice_id.writeBytes(id);
	    		indice_id.writeBytes("#");
	    		indice_id.writeBytes(completeToLeft(String.valueOf(i), '0', 7));
	    		indice_id.writeByte(10);
	    		cont++;
	    		id = String.valueOf(cont);
	    		id = completeToLeft(id, '0', 4);
	    	}
	    }
	    
	    public String get_indice (long seek, int id, RandomAccessFile file) throws IOException {
	    	file.seek(seek);
	    	
	    	String line = file.readLine();
	    	String [] array = line.split("#");
	    	
	    	return array[id];
	    }
	    
	    public int busca_binaria_indice(int key) throws FileNotFoundException, IOException{
	        
	        int begin = 0;
	        int end = QTD_REG - 1;
	        int i;
	        int offset;
	        while (begin < end) {  /* Condição de parada */
	        	i = (begin + end) / 2;  /* Calcula o meio do sub-vetor */
	        	offset = i * INDICE_SIZE;
	        	String campo = get_indice(offset, 0, indice_id);
	        	int id = Integer.valueOf(campo);
	        	
	        	if (id == key) {  /* Item encontrado */
	        		return Integer.valueOf(get_indice(offset, 1, indice_id));
	        	}else
	        		if (key > id) {  /* Item está no sub-vetor à direita */
	        			begin = i + 1;
	        		} else {  /* vector[i] > item. Item está no sub-vetor à esquerda */
	        			end = i;
	        		}
	     }

	     return -1;
	        
	        
	     }
	    
	    public void busca_registro_indice(int key) throws FileNotFoundException, IOException {
	    	
	    	int response = busca_binaria_indice(key);
	    	
	    	if(response != -1) {
	    		char[] line = get_line(response);
	    		this.info(line);
	    	}else {
	    		System.out.println("Registro nao encontrado");
	    	}
	    }
	    
	    public int hashtag1() throws IOException {
	    	dados.seek(0);
	    	indice_id.seek(0);
	    	hashtag1.seek(0);
	    	boolean flag = hashtag1.length() == 0;
	    	int cont = 0;
	    	for (long i = 0, j = 0 ; i < dados.length() ; i+=LINE_SIZE, j+=INDICE_SIZE) {
	    	String id = get_indice(j, 0, indice_id);
	    	char[] line = get_line((int)i);
	    	String campo = get_campo(line, 9);
	    	if(Integer.valueOf(campo) == 3) {
	    		if(flag) {
	    			hashtag1.writeBytes(id);
		    		hashtag1.writeBytes("#");
		    		hashtag1.writeBytes(completeToLeft(String.valueOf(i), '0', 7));
		    		hashtag1.writeByte(10);
	    		}
	    		
	    		cont++;
	    	}
	    	}
	    	
	    	return cont;
	    }
	    
	    public int hashtag2() throws IOException {
	    	dados.seek(0);
	    	indice_id.seek(0);
	    	hashtag2.seek(0);
	    	boolean flag = hashtag2.length() == 0;
	    	int cont = 0;
	    	for (long i = 0, j = 0 ; i < dados.length() ; i+=LINE_SIZE, j+=INDICE_SIZE) {
	    	String id = get_indice(j, 0, indice_id);
	    	char[] line = get_line((int)i);
	    	String campo = get_campo(line, 11);
	    	if(Integer.valueOf(campo) == 601) {
	    		if(flag) {
	    			hashtag2.writeBytes(id);
		    		hashtag2.writeBytes("#");
		    		hashtag2.writeBytes(completeToLeft(String.valueOf(i), '0', 7));
		    		hashtag2.writeByte(10);
	    		}
	    		
	    		cont++;
	    	}
	    	}
	    	
	    	return cont;
	    }
	    
	    public int busca_binaria_indice_hashtag(int key, int arquivo) throws FileNotFoundException, IOException{
	    	RandomAccessFile file = null ;
	    	if(arquivo==1) {
	        	file = hashtag1;
	        }else {
	        	file = hashtag2;
	        }
	    	
	        int begin = 0;
	        int end = (int) (file.length() / INDICE_SIZE) - 1;
	        int i;
	        int offset;
	        while (begin < end) {  /* Condição de parada */
	        	i = (begin + end) / 2;  /* Calcula o meio do sub-vetor */
	        	offset = i * INDICE_SIZE;
	        	String campo = get_indice(offset, 0, file);
	        	int id = Integer.valueOf(campo);
	        	
	        	if (id == key) {  /* Item encontrado */
	        		return Integer.valueOf(get_indice(offset, 1, file));
	        	}else
	        		if (key > id) {  /* Item está no sub-vetor à direita */
	        			begin = i + 1;
	        		} else {  /* vector[i] > item. Item está no sub-vetor à esquerda */
	        			end = i;
	        		}
	     }

	     return -1;
	        
	        
	     }
	    
	    public void estatistica() throws IOException {
	    	hashtag1.seek(0);
	    	hashtag2.seek(0);
	    	
	    	String campo1;
	    	String campo2;
	    	
	    	int cont = 0;
	    	
	    	long barra = 0;
	    	
	    	
	    	for(long i = 0 ; i < hashtag1.length() ; i+= INDICE_SIZE) {
	    	
	    		campo1 = get_indice(i, 0, hashtag1);
	    		
	    		int index = busca_binaria_indice_hashtag(Integer.valueOf(campo1), 2);
	    		if(index != -1) {
	    			cont++;
	    		}
	    			
	    		barra = 100*i / hashtag1.length();
	    		String ret = barra + 1 + "% \n";
	    		System.out.println("\n \n \n \n \n \n \n \n");
	    		System.out.print(ret);
	    		
	    		}
	    	
			System.out.println("Completo!");

	    	
	    	System.out.println(cont + " dos candidatos são AGRICULTORES, na situação \n "
	    						+ " de ENSINO FUNDAMENTAL INCOMPLETO \n"
	    						+ ((cont*100)/QTD_REG) + "%");
	    	
	    }
	    
	    public void get_hashtag1 (int key) throws FileNotFoundException, IOException {
	    	long offset = (int) busca_binaria_indice_hashtag(key,1);
	    	
	    	if(offset != -1) {
	    		char[] line = get_line((int)offset);
	    		this.info(line);
	    	}else {
	    		System.out.println("Registro nao encontrado");
	    	}
	    }
	    
	    public void get_hashtag2 (int key) throws FileNotFoundException, IOException {
	    	long offset = (int) busca_binaria_indice_hashtag(key,2);
	    	
	    	if(offset != -1) {
	    		char[] line = get_line((int)offset);
	    		this.info(line);
	    	}else {
	    		System.out.println("Registro nao encontrado");
	    	}
	    }
	    
	    public void get_allhashtag1 () throws IOException {
	    	hashtag1.seek(0);
	    	for(int j = 0 ; j < this.hashtag1.length() ; j+=INDICE_SIZE) {
		    	String campo = get_indice(j, 1, hashtag1);
		    	int offset = Integer.valueOf(campo);
		    	char[] line = get_line(offset);
		    	this.info(line);

	    	}
	    }
	    
	    
	    public ArrayList<String>[] hash () throws IOException {
	    	hashtag2.seek(0);
	    	final int chave = 107;
	    	ArrayList<String> [] lista = new ArrayList[chave];
	    	
	    	for(int i = 0 ; i < lista.length ; i++) {
	    		lista[i] = new ArrayList<String>();
	    	}
	    	String val = "";
	    	for(long i = 0 ; i < hashtag2.length() ; i+=INDICE_SIZE) {
	    		val = get_indice(i, 0, hashtag2);
	    		int hash = Integer.valueOf(val) % (chave - 1);
	    		String info = get_indice(i, 0, hashtag2) + "#" + get_indice(i, 1, hashtag2);
	    		lista[hash].add(info);
	    	}
	    	
	    	return lista;
	    	
	    }
	    
	    
	    
	    
	    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
			
	    	Arquivo a = new Arquivo();
			
			/* ---- CONVERTE o CSV no .dat formatado ---- */
			
			/*
			 * System.out.println("Iniciando Conversão do arquivo"); 
			 * a.le_arquivo();
			 * System.out.println("Terminou!");
			 */
			 
			
			/* ---- Exibe as linhas do arquivo gerado ---- */

			//a.escreve_arquivo();
			
			
			/* ---- TESTE DE ACESSO A UM CAMPO DE UMA LINHA EX: LINHA 15 CAMPO 3 ---- */

			//System.out.println(a.get_campo(a.get_line(15 * LINE_SIZE), 3));
			
			
			/* ---- TESTE DA BUSCAN BINARIA POR NOME DIRETO NO ARQUIVO ---- */

			//a.busca_registro_nome("ACILDO RICHTER");
			//a.busca_registro_nome("ADAO PRETTO FILHO");
			
	    	
	    	
			/* ---- CRIA O ARQUIVO DE INDICE E GERA ID SEQUENCIAL ---- */

			//a.cria_indice_id();
			//System.out.println("Terminou");
			
			
			/* ---- TESTE DA BUSCA BINARIA NO INDICE PELO ID E SEEK NO ARQUIVO DE DADOS ---- */
			// teste que retorna o seek para o arquivo de daods
			//System.out.println(a.busca_binaria_indice(2500));
			// teste que faz toString do dados com indice 2500
			//a.busca_registro_indice(2500);
			
			
			
			/* ---- TESTE DO FILTRO POR CANDIDATOS QUE NAO COMPLETARAM O FUNDAMENTAL ---- */
			
			//conta direto no arquivo de dados quantos quandidato se encaixam, e gera um arquivo de indice pra consulta a esses candidatos
			/*
			 * float x = a.hashtag1(); 
			 * System.out.println((int)x +
			 * " Candidatos tem o status ENSINO FUNDAMENTAL INCOMPLETO ");
			 * 
			 * System.out.println(((x*100)/QTD_REG) + "%");
			 */
			 
			/* ---- BUSCA BINARIA NO ARQUIVO DE INDICE DOS CANDIDATOS DO FILTRO 1 ---- */

			//a.get_hashtag1(1);
			
			
			
			//Exibir todos os candidatos do filtro 1
			//a.get_allhashtag1();
			
			
			
			
			//TESTE do filtro 2
			/*
			 * float x = a.hashtag2(); System.out.println((int)x +
			 * " Candidatos são AGRICULTORES ");
			 * 
			 * System.out.println(((x*100)/QTD_REG) + "%");
			 * 
			 * 
			 * ---- BUSCA BINARIA NO ARQUIVO DE INDICE DOS CANDIDATOS DO FILTRO 2 ---- 
			 * a.get_hashtag2(15);
			 */
			 
	    	
			//a.estatistica();
			
			
			
			
			//Faz Hash do Indice do Filtro 2 e retorna um vetor que é a tabela hash
			
			//ArrayList<String> [] tabela = a.hash();
			
			//Teste pra buscar o indice 9994 (linha 9995)
			// 106 é a chave - 1 (chave = 107)
			
			/*
			 * int id = 9994; int 
			 * hash = id % 106;
			 */
			
			
			//Chaves mapeadas no mesmo lugar ficam em um array, após saber onde
			//é mapeada a chave, procura sequencialmente.
			
			/*
			 *for(int i = 0 ; i < tabela[hash].size() ; i++) {
			 *	if(tabela[hash].get(i).split("#")[0].compareTo(String.valueOf(id)) == 0) {
			 *		System.out.println(a.get_campo(a.get_line(Integer.valueOf(tabela[hash].get(i).split("#")[1])), 5));
			 *	}
			 *}
			 */
			
		}
}

	    
	   
	