package Objects;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Transaction {
	private String userTransaction;
	private String databaseUsed;
	private TransactionType tType;
	private String queryExecuted;
	private int registros;
	//private Date executionDate;
	private long executionDate;

	public Transaction() {
		
	}

	public Transaction(String userTransaction, String databaseUsed, TransactionType tType, String queryExecuted,
			int registros,long executionDate) {
	
		this.userTransaction = userTransaction;
		this.databaseUsed = databaseUsed;
		this.tType = tType;
		this.queryExecuted = queryExecuted;
		this.registros = registros;
		this.executionDate = executionDate;
	
	}

	public String getUserTransaction() {
		return userTransaction;
	}

	public String getDatabaseUsed() {
		return databaseUsed;
	}

	public TransactionType gettType() {
		return tType;
	}

	public String getQueryExecuted() {
		return queryExecuted;
	}

	public long getExecutionDate() {
		return executionDate;
	}

	public int getRegistros() {
		return registros;
	}

	public void setUserTransaction(String userTransaction) {
		this.userTransaction = userTransaction;
	}

	public void setDatabaseUsed(String databaseUsed) {
		this.databaseUsed = databaseUsed;
	}

	public void settType(TransactionType tType) {
		this.tType = tType;
	}

	public void setQueryExecuted(String queryExecuted) {
		this.queryExecuted = queryExecuted;
	}

	public void setRegistros(int registros) {
		this.registros = registros;
	}

	public void setExecutionDate(long executionDate) {
		this.executionDate = executionDate;
	}



}
