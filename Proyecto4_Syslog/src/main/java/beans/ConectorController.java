package beans;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import objects.Transaction;
import objects.TransactionType;
import objects.logFormat;

public class ConectorController implements Serializable {
	private Connection conn;

	// Nom bbdd
	private String database;
	// Host
	private String hostname;
	// Puerto
	private String port;
	// Nombre de usuario
	private String username;
	// Clave de usuario
	private String password;
	// Sentencia sql
	private String sSQL;
	private String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false";// Ruta de la
	// conexion
	private String driver = "com.mysql.jdbc.Driver";// Driver

	private static QueryEvento evt;
	private PropertyChangeSupport evento;

	public ConectorController() {
		evento = new PropertyChangeSupport(this);
		evt = new QueryEvento();
	}

	public ConectorController(String hostname, String port, String database) {
		this.database = database;
		this.hostname = hostname;
		this.port = port;
		this.username = "root";// Por defecto
		this.password = "";
		this.sSQL = "";
		this.url = "jdbc:mysql://" + hostname + ":" + port + "/" + database;
		evento = new PropertyChangeSupport(this);
	}

	public void setCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public void setHostSettings(String hostname, String port) {
		this.hostname = hostname;
		this.port = port;
	}

	private void iniciarConexion() {
		this.url = "jdbc:mysql://" + hostname + ":" + port + "/" + database;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void cerrarConexion() throws SQLException {
		if (conn != null) {
			conn.close();
		}
	}

	public void ejecutarQuery(String query) throws SQLException {
		iniciarConexion();
		this.sSQL = query;
		String tipo = "";
		query = eliminarVaciosInicio(query);
		String[] partes = query.split(" ");
		try {
			if (partes.length >= 1) {
				tipo = partes[0];
				if (tipo.equalsIgnoreCase("INSERT")) {
					insertar(query);
				} else if (tipo.equalsIgnoreCase("DELETE")) {
					delete(query);
				} else if (tipo.equalsIgnoreCase("UPDATE")) {
					update(query);
				} else if (tipo.equalsIgnoreCase("SELECT")) {
					select(query);
				}else if (tipo.equalsIgnoreCase("CALL")) {
					procedure(query);
				}
			}
		} catch (Exception e) {

		} finally {
			cerrarConexion();
		}
	}

	private void delete(String query) throws SQLException {
		// TODO Auto-generated method stub
		Statement stmt = conn.createStatement();
		int registros = stmt.executeUpdate(query);
		Date date = new Date();
		Transaction transaction = new Transaction();
		transaction.setUserTransaction(username);
		transaction.setDatabaseUsed(database);
		transaction.settType(TransactionType.DELETE);
		transaction.setQueryExecuted(query);
		transaction.setRegistros(registros);
		transaction.setExecutionDate(date.getTime());
		evento.firePropertyChange("SELECT", null, transaction);

	}

	private void insertar(String query) throws SQLException {

		Statement stmt = conn.createStatement();
		int registros = stmt.executeUpdate(query);
		Date date = new Date();
		Transaction transaction = new Transaction();
		transaction.setUserTransaction(username);
		transaction.setDatabaseUsed(database);
		transaction.settType(TransactionType.INSERT);
		transaction.setQueryExecuted(query);
		transaction.setRegistros(registros);
		transaction.setExecutionDate(date.getTime());
		evento.firePropertyChange("SELECT", null, transaction);

	}

	private void update(String query) throws SQLException {

		Statement stmt = conn.createStatement();
		int registros = stmt.executeUpdate(query);
		Date date = new Date();
		Transaction transaction = new Transaction();
		transaction.setUserTransaction(username);
		transaction.setDatabaseUsed(database);
		transaction.settType(TransactionType.UPDATE);
		transaction.setQueryExecuted(query);
		transaction.setRegistros(registros);
		transaction.setExecutionDate(date.getTime());
		evento.firePropertyChange("SELECT", null, transaction);

	}

	private void select(String query) throws SQLException {

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		int registros = 0;
		while (rs.next()) {

			++registros;
			for (int i = 1; i <= columnsNumber; i++) {
				if (i > 1) {
					String columnValue = rs.getString(i);
					System.out.print(columnValue + " ");
				} else {
					String columnValue = rs.getString(i);
					System.out.print(columnValue + " ");
				}
			}
			System.out.println("");
		}
		System.out.println("\n\n");
		Date date = new Date();
		Transaction transaction = new Transaction();
		transaction.setUserTransaction(username);
		transaction.setDatabaseUsed(database);
		transaction.settType(TransactionType.SELECT);
		transaction.setQueryExecuted(query);
		transaction.setRegistros(registros);
		transaction.setExecutionDate(date.getTime());
		evento.firePropertyChange("SELECT", null, transaction);

	}
	private void procedure(String query) throws SQLException {
		
		CallableStatement statement = conn.prepareCall(query);  
		statement.execute(); 
		Date date = new Date();
		Transaction transaction = new Transaction();
		transaction.setUserTransaction(username);
		transaction.setDatabaseUsed(database);
		transaction.settType(TransactionType.PROCEDURE);
		transaction.setQueryExecuted(query);
		transaction.setRegistros(0);
		transaction.setExecutionDate(date.getTime());
		evento.firePropertyChange("PROCEDURE", null, transaction);
		
	}

	private String eliminarVaciosInicio(String query) {
		while (query.charAt(0) == ' ') {
			query = query.substring(1, query.length());
		}
		return query;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getsSQL() {
		return sSQL;
	}

	public void setsSQL(String sSQL) {
		this.sSQL = sSQL;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getDatabase() {
		return database;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	private ArrayList<String> getTransactionsUsers() {
		ArrayList<String> userList = new ArrayList<String>();
		ArrayList<Transaction> historico = evt.getHistorico();
		for (Transaction tmp : historico) {
			String userName = tmp.getUserTransaction();
			if (!userList.contains(userName)) {
				userList.add(userName);
			}
		}
		return userList;
	}

	private ArrayList<String> getExecutedQueriesList(logFormat format, ArrayList<Transaction> solicitadas) {
		ArrayList<String> queryList = new ArrayList<String>();
		switch (format) {
		case QUERY_TYPE:
			for (Transaction tmp : solicitadas) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date(tmp.getExecutionDate());
				String intermedio = "[" + tmp.getQueryExecuted() + ";" + dateFormat.format(date) + ";"
						+ tmp.gettType().toString() + "]";
				queryList.add(intermedio);
			}
			break;
		case USER_LIST:
			for (Transaction tmp : solicitadas) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date(tmp.getExecutionDate());
				String intermedio = "[" + tmp.getQueryExecuted() + ";" + dateFormat.format(date) + "]";
				queryList.add(intermedio);
			}
			break;
		case SIMPLE:
			for (Transaction tmp : solicitadas) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date(tmp.getExecutionDate());
				String intermedio = "[" + tmp.getQueryExecuted() + ";" + dateFormat.format(date) + ";"
						+ tmp.getUserTransaction() + "]";
				queryList.add(intermedio);
			}
			break;
		}

		return queryList;
	}

	public ArrayList<String> getReport(String database, String user) {
		PropertyChangeListener[] temporal = evento.getPropertyChangeListeners();
		QueryEvento transicion = (QueryEvento) temporal[0];
		
		ArrayList<Transaction> solicitadas = new ArrayList<Transaction>();
		ArrayList<Transaction> historico = transicion.getHistorico();
		for (Transaction tmp : historico) {
			if (tmp.getDatabaseUsed().equalsIgnoreCase(database) && tmp.getUserTransaction().equalsIgnoreCase(user)) {
				solicitadas.add(tmp);
			}
		}
		if (solicitadas.size() > 0) {
			return getExecutedQueriesList(logFormat.QUERY_TYPE, solicitadas);
		}
		return null;

	}

	public ArrayList<String> getReport(String database, String user, TransactionType tipo) {
		PropertyChangeListener[] temporal = evento.getPropertyChangeListeners();
		QueryEvento transicion = (QueryEvento) temporal[0];
		
		ArrayList<Transaction> solicitadas = new ArrayList<Transaction>();
		ArrayList<Transaction> historico = transicion.getHistorico();
		for (Transaction tmp : historico) {
			if (tmp.getDatabaseUsed().equalsIgnoreCase(database) && tmp.getUserTransaction().equalsIgnoreCase(user)
					&& tmp.gettType() == tipo) {
				solicitadas.add(tmp);
			}
		}
		if (solicitadas.size() > 0) {
			return getExecutedQueriesList(logFormat.USER_LIST, solicitadas);
		}
		return null;

	}

	public ArrayList<String> getReport(String database, TransactionType tipo) {
		/*Recuperamos la lista de eventos del listener, al tener solo 1, esta en la posicion 0
		 * y asi le sacamos el arraylist de transiciones
		 */
		PropertyChangeListener[] temporal = evento.getPropertyChangeListeners();
		QueryEvento transicion = (QueryEvento) temporal[0];
		
		ArrayList<Transaction> solicitadas = new ArrayList<Transaction>();
		ArrayList<Transaction> historico = transicion.getHistorico();	
		for (Transaction tmp : historico) {
			if (tmp.getDatabaseUsed().equalsIgnoreCase(database) && tmp.gettType() == tipo) {
				solicitadas.add(tmp);
			}
		}

		if (solicitadas.size() > 0) {
			return getExecutedQueriesList(logFormat.SIMPLE, solicitadas);
		}
		return null;

	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		evento.addPropertyChangeListener(listener);

	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		evento.removePropertyChangeListener(listener);
	}

}
