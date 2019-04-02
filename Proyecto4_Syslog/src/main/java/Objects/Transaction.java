package Objects;

import java.sql.Date;
import java.sql.ResultSet;

import com.mysql.jdbc.ResultSetMetaData;

public class Transaction {
	private String userTransaction;
	private TransactionType tType;
	private String queryExecuted;
	private Date executionDate;
	private ResultSet resultQuery;
	private ResultSetMetaData metadataQuery;
}
