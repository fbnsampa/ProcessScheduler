package main;
import java.util.LinkedList;

class Escalonador {
	private BCP executando;
	private LinkedList <BCP> prontos;
	private LinkedList <BCP> bloqueados;
	
	public Escalonador(){
		this.prontos = new LinkedList<BCP>();
		this.bloqueados = new LinkedList<BCP>();
	}

	public void addProcesso(BCP processo){
		this.prontos.add(processo);
	}
	
	public static void main(String [] args){
		for (int i = 1; i <= 10; i++){
			String s = String.format("%02d", i) + ".txt";
			System.out.println(s);
		}
		
	}
}
