package beans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Objects.QueryHandler;
import Objects.Transaction;
import Objects.TransactionType;

public class LogController implements QueryHandler{
	private ArrayList<Transaction> historico;

	enum logFormat {
		USER_LIST, QUERY_TYPE, SIMPLE
	};

	LogController() {
		historico = new ArrayList<Transaction>();
	}

	private void addTransaction(Transaction a) {
		historico.add(a);
	}

	private ArrayList<String> getTransactionsUsers() {
		ArrayList<String> userList = new ArrayList<String>();
		for (Transaction tmp : historico) {
			String userName = tmp.getUserTransaction();
			if (!userList.contains(userName)) {
				userList.add(userName);
			}
		}
		return userList;
	}

	private ArrayList<String> getExecutedQueriesList(logFormat format,ArrayList<Transaction> solicitadas) {
		ArrayList<String> queryList = new ArrayList<String>();
		switch (format) {
		case QUERY_TYPE:
			for (Transaction tmp : solicitadas) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date(tmp.getExecutionDate());
				String intermedio = "[" + tmp.getQueryExecuted() + ";"
						+ dateFormat.format(date) + ";" + tmp.gettType().toString() + "]";
				queryList.add(intermedio);
			}
			break;
		case USER_LIST:
			for (Transaction tmp : solicitadas) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date(tmp.getExecutionDate());
				String intermedio = "["+ tmp.getQueryExecuted() + ";"
						+ dateFormat.format(date) + "]";
				queryList.add(intermedio);
			}
			break;
		case SIMPLE: 
			for (Transaction tmp : solicitadas) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date(tmp.getExecutionDate());
				String intermedio = "["+ tmp.getQueryExecuted() + ";"
						+ dateFormat.format(date)+";" +tmp.getUserTransaction()+ "]";
				queryList.add(intermedio);
			}
			break;
		}

		return queryList;
	}
	private ArrayList<String> getReport(String database,String user) {
		ArrayList<Transaction> solicitadas = new ArrayList<Transaction>();
		for (Transaction tmp : historico) {
			if (tmp.getDatabaseUsed().equalsIgnoreCase(database) && tmp.getUserTransaction().equalsIgnoreCase(user)) {
				solicitadas.add(tmp);
			}
		}
		if (solicitadas.size() > 0) {
			return getExecutedQueriesList(logFormat.QUERY_TYPE,solicitadas);
		}
		return null;
		
	}
	private ArrayList<String> getReport(String database,String user,TransactionType tipo) {
		ArrayList<Transaction> solicitadas = new ArrayList<Transaction>();
		for (Transaction tmp : historico) {
			if (tmp.getDatabaseUsed().equalsIgnoreCase(database) && tmp.getUserTransaction().equalsIgnoreCase(user) && tmp.gettType() == tipo) {
				solicitadas.add(tmp);
			}
		}
		if (solicitadas.size() > 0) {
			return getExecutedQueriesList(logFormat.USER_LIST,solicitadas);
		}
		return null;
		
	}
	private ArrayList<String> getReport(String database,TransactionType tipo) {
		ArrayList<Transaction> solicitadas = new ArrayList<Transaction>();
		for (Transaction tmp : historico) {
			if (tmp.getDatabaseUsed().equalsIgnoreCase(database) && tmp.gettType() == tipo) {
				solicitadas.add(tmp);
			}
		}
		if (solicitadas.size() > 0) {
			return getExecutedQueriesList(logFormat.SIMPLE,solicitadas);
		}
		return null;
		
	}

	

	public void queryLanzada(Transaction transaction) {
		// TODO Auto-generated method stub
		historico.add(transaction);
		
	}
	public ArrayList<String> reportSolicitado(String database, TransactionType tipo) {
		// TODO Auto-generated method stub
		return reportSolicitado(database,tipo);
	}

	public ArrayList<String> reportSolicitado(String database, String user, TransactionType tipo) {
		// TODO Auto-generated method stub
		return getReport( database,  tipo);
	}

	public ArrayList<String> reportSolicitado(String database, String user) {
		// TODO Auto-generated method stub
		return getReport( database,  user);
	}
}
