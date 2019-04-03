package beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class QueryEvento implements PropertyChangeListener {

	ArrayList<Transaction> historico;
	
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		historico.add((Transaction) evt.getNewValue());
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
