package main;
import java.io.*;
import java.nio.file.*;

class Sistema {
	static BCP [] tabela; //tabela de processos.
	static Escalonador escalonador;
	static int quantum; 
	static int r1; //registardor 1
	static int r2; //registrador 2	
	static File log;
	
	//carrega os processos da pasta processos
	public static void carregaProcessos(){
		String diretorio = "processos/";
		String nomeArquivo = "quantum.txt";
		Path endereco = Paths.get(diretorio, nomeArquivo);
		
		//Faz a leitura do arquivo "quantum.txt" e atribui o seu valor a variavel quantum
		try (InputStream in = Files.newInputStream(endereco);
		    BufferedReader arquivo = new BufferedReader(new InputStreamReader(in))) {
			quantum = Integer.parseInt(arquivo.readLine());
		} catch (IOException x) {
			System.out.println("Arquivo " + nomeArquivo + " nao encontrado.");
		    System.err.println(x);
		}	
		
		//Faz a leitura dos demais arquivos, cria um BCPs para cada processo,
		//preenche a tabela com eles e os adiciona na fila "prontos" do escalonador
		for (int i = 1; i <= 10; i++){
			nomeArquivo = String.format("%02d", i) + ".txt";
			endereco = Paths.get(diretorio, nomeArquivo);
			
			try (InputStream in = Files.newInputStream(endereco);
			    BufferedReader arquivo = new BufferedReader(new InputStreamReader(in))) {
				String linha = arquivo.readLine();
				BCP processo = new BCP(linha);
			
				//Adiciona linha  no buffer de dados
				escalonador.setListaLog("Carregando " + processo.getNome());
				
				int j = 0;
				while (!linha.equals("SAIDA")){
					linha = arquivo.readLine();
					processo.setInstrucao(j,linha);
					j++;
				}
				tabela[i-1] = processo;
				escalonador.addProcesso(processo);
			} catch (IOException x) {
				System.out.println("Arquivo " + nomeArquivo + " nao encontrado.");
			    System.err.println(x);
			}			
		}
	}
	
	//cria o arquivo do log
	public static void criaArquivo(){
		try{
			log =  new File("processos/log"+ String.format("%02d", quantum) +".txt");
			
			if(!log.exists())
				log.createNewFile();
			
			if (log == null){
				System.out.println("\n escrever arquivo: erro no log \n" + log.toString());
				return;
			} 
			FileWriter fw = new FileWriter (log);
			BufferedWriter bw = new BufferedWriter(fw);
			
			for (String miniLinha : escalonador.getListaLog()) 
			    bw.write(miniLinha + "\n");
						
			bw.close();
			
		} catch (IOException e){
			e.printStackTrace();
		}
		
	}
	
	//remove um processo da tabela
	public static void removeProcesso(BCP processo){
		for (int i = 0; i < tabela.length; i++){
			if (tabela[i] == processo){
				tabela[i] = null;
				break;
			}
		}
	}
	
	//Verifica se ha processos na tabela
	public static boolean haProcessos(){
		for (int i = 0; i < tabela.length; i++){
			if (tabela[i] != null) return true;
		}
		return false;
	}
	
	public static void iniciar() {
		tabela = new BCP[10];
		escalonador = new Escalonador();
		carregaProcessos();
		escalonador.executar();
		criaArquivo();
	}

}
