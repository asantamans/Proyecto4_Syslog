package beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import Objects.Transaction;

public class QueryEvento implements PropertyChangeListener {

	ArrayList<Transaction> historico;
	
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		historico.add((Transaction) evt.getNewValue());
	}

}
