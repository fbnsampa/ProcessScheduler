package main;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class Escalonador {
	private BCP executando;
	private LinkedList <BCP> prontos;
	private LinkedList <BCP> bloqueados;
	double mediaTrocas, mediaInstrucoes; //para fins estatisticos
	private List<String> listaLog = new ArrayList<String>();
	
	public Escalonador(){
		this.prontos = new LinkedList<BCP>();
		this.bloqueados = new LinkedList<BCP>();
		mediaInstrucoes = 0;
		mediaTrocas = 0;
	}
	
	public List<String> getListaLog() { return listaLog; }
	
	public void setListaLog(String listaLog) { this.listaLog.add(listaLog); }

	public void addProcesso(BCP processo){
		this.prontos.add(processo);
	}
	
	public void executar(){
		//Enquanto houver processos na tabela
		while (Sistema.haProcessos()){
			
			while (prontos.isEmpty()){
				//Decrementa a espera de todos processos bloqueados
				//Aqueles que atingirem espera igual a zero são alocados na lista de prontos
				for (BCP processo : bloqueados)
					if (processo.estaPronto()) prontos.add(processo);
				
				//Os processos que passaram para o estado "Pronto" são removidos da lista de bloqueados
				for (BCP processo : prontos) bloqueados.remove(processo);
			}
			
			int numInstrucoes = 0;
			
			//O primeiro processo da fila recebe o estado "Executando"
			executando = prontos.poll();
			executando.executar();
			//Add linha no buffer
			this.setListaLog("Executando " + executando.getNome());
			
			//Carrega o contexto
			Sistema.r1 = executando.getS1();
			Sistema.r2 = executando.getS2();
			
			for (int clock = Sistema.quantum; clock > 0; clock--){
				numInstrucoes++;
				
				//Busca a proxima instrucao do processo e incrementa o pc
				String instrucao = executando.proxInstrucao();
				
				switch (instrucao.charAt(0)){
					case 'C': //COM
						break;
					case 'E': //E/S
						executando.bloquear();
						bloqueados.add(executando);

						//Add linha no buffer
						this.setListaLog("E/S iniciada em " + executando.getNome());
						
						clock = 0; //interrompendo o laço
						break;
					case 'S': //SAIDA
						Sistema.removeProcesso(executando); //remove o processo da tabela
						executando.setEstado('B'); //apenas para evitar que volte para a fila de prontos
						mediaTrocas += executando.getTrocas();
						mediaInstrucoes += executando.calculaMediaInstrucoes();

						//Add linha buffer
						this.setListaLog(executando.getNome() + " terminado. X=" + Sistema.r1 + ". Y=" + Sistema.r2);
						
						clock = 0; //interrompendo o laço
						break;
					case 'X': //X= 
						String [] x = instrucao.split("=");
						int valorX = Integer.parseInt(x[1]);
						Sistema.r1 = valorX;
						break;
					case 'Y': //Y=  
						String [] y = instrucao.split("=");
						int valorY = Integer.parseInt(y[1]);
						Sistema.r2 = valorY;
						break;
				}
			}
			
			//Add linha no buffer
			if (numInstrucoes == 1){
				this.setListaLog("Interrompendo " + executando.getNome() + " após 1 instrução");
			} else {
				this.setListaLog("Interrompendo " + executando.getNome() + " após " + numInstrucoes + " instruções");
			}
			
			//Armazena o contexto
			executando.setS1(Sistema.r1);
			executando.setS2(Sistema.r2);
			//Se o processo nao estiver bloqueado ou finalizado, eh definido como pronto e alocado no final da respectiva fila.
			if (executando.getEstado() == 'E'){ 
				executando.setEstado('P');
				prontos.add(executando);
			}
			executando = null;
		} //FIM WHILE
		
		mediaTrocas /= 10;
		mediaInstrucoes /= 10;
		
		//Add linhan no buffer
		this.setListaLog("MEDIA DE TROCAS: " + mediaTrocas);
		this.setListaLog("MEDIA DE INSTRUCOES: " + mediaInstrucoes);
		this.setListaLog("QUANTUM: " + Sistema.quantum);
	}
	
	public static void main (String [] args){
		//A principio o metodo main ficava na classe Sistema
		//Mas para atender as exigencias do enunciado, foi colocado na classe Escalonador
		Sistema.iniciar();
	}
	
}
