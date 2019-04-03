package Components;

import java.util.ArrayList;

import Objects.Transaction;

public class LogController {
	private ArrayList<Transaction> historico;
	
	LogController() {
		historico = new ArrayList<Transaction>();
	}
	
	public void addTransaction(Transaction a) {
		historico.add(a);
	}
}
