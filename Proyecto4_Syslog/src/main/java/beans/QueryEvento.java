package beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import objects.Transaction;

public class QueryEvento implements PropertyChangeListener {

	private ArrayList<Transaction> historico = new ArrayList<Transaction>();
	
	
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		Transaction a = (Transaction) evt.getNewValue();
		historico.add(a);
		
	}
	public ArrayList<Transaction> getHistorico() {
		return historico;
	}

	public void setLog(ArrayList<Transaction> historico) {
		this.historico = historico;
	}
	
	public Transaction getTransaction(int point) {
		return historico.get(point);
	}

	public void setLog(Transaction transaction,int point) {
		this.historico.set(point,transaction);
	}

}
