package main;

class BCP {
	private String nome; //nome do programa.
	private String [] texto; //código do programa
	private char estado; //estado do processo: 'E' = Executando   'P' = Pronto   'B' = Bloqueado	
	private int pc; //Program Counter
	private int espera; //contador de espera para quando o processo for bloqueado
	private int s1; //estado atual do registrador 1
	private int s2; //estado atual do registrador 2
	int trocas; //para fins estatisticos
	
	public BCP(String nome){
		this.nome = nome;
		this.texto = new String [21];
		this.estado = 'P'; //todos processos iniciam como "Prontos"
		this.s1 = 0;
		this.s2 = 0;
		this.pc = 0;
		this.espera = 0;
		this.trocas = 0;
	}
	
	public void executar(){
		trocas++;
		this.estado = 'E';
	}
	
	public void bloquear(){
		this.espera = 2;
		this.estado = 'B';
	}
	
	//metodo usado para verificar processos bloqueados 
	public boolean estaPronto(){
		this.espera--;
		if (espera == 0){
			this.estado = 'P';
			return true;
		}
		return false;
	}
	
	//retorna a proxima instrucao a ser executada
	public String proxInstrucao(){
		this.pc++;
		return texto[pc];
	}
	
	public double calculaMediaInstrucoes(){
		return (pc)/trocas;
	}
	
	public String getNome() {
		return nome;
	}

	public void setInstrucao(int index, String texto) {
		if (index < 21) this.texto[index] = texto;
	}

	public char getEstado() {
		return estado;
	}

	public void setEstado(char estado) {
		if (estado == 'P' || estado == 'B' || estado == 'E') this.estado = estado;
	}

	public int getS1() {
		return s1;
	}

	public void setS1(int s1) {
		this.s1 = s1;
	}

	public int getS2() {
		return s2;
	}

	public void setS2(int s2) {
		this.s2 = s2;
	}
	
	public int getTrocas() {
		return trocas;
	}
}
