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
	//private Date executionDate;
	private long executionDate;
	private ResultSet resultQuery;
	private ResultSetMetaData metadataQuery;

	public Transaction(String userTransaction, String databaseUsed, TransactionType tType, String queryExecuted,
			long executionDate, ResultSet resultQuery, ResultSetMetaData metadataQuery) {
		super();
		this.userTransaction = userTransaction;
		this.databaseUsed = databaseUsed;
		this.tType = tType;
		this.queryExecuted = queryExecuted;
		this.executionDate = executionDate;
		this.resultQuery = resultQuery;
		this.metadataQuery = metadataQuery;
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

	public ResultSet getResultQuery() {
		return resultQuery;
	}

	public ResultSetMetaData getMetadataQuery() {
		return metadataQuery;
	}
}
