import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Arquivo {
	
		final int SIZE_CHAR = 2;
		final static int N_CAMPOS = 13;
	    ArrayList<Integer> offset;
	    RandomAccessFile arquivo;
	    RandomAccessFile dados;
	    
	    
	    public Arquivo() throws FileNotFoundException, IOException{
	         offset = new ArrayList<>();
	         arquivo = new RandomAccessFile("./consulta_cand_2020_RS.csv", "rw");
	         dados = new RandomAccessFile("./consulta_cand_2020_RS.dat", "rw");
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
	    
	    
	    public static void main(String[] args) throws FileNotFoundException, IOException {
			Arquivo a = new Arquivo();
			
			
			a.le_arquivo();
			
			System.out.println("Terminou");
		}
}
