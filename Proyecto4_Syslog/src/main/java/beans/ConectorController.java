package beans;

import java.beans.PropertyChangeSupport;
import java.io.Serializable;
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

import Objects.Transaction;
import Objects.TransactionType;
import Objects.logFormat;

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

	
	private Transaction registro;
	private ArrayList<Transaction> historico = new ArrayList<Transaction>();
	PropertyChangeSupport evento;

	public ConectorController() {
		this.database = "";
		this.hostname = "";
		this.port = "";
		this.username = "root";// Por defecto
		this.password = "";
		this.sSQL = "";
		this.url = "jdbc:mysql://" + hostname + ":" + port + "/" + database;
		evento = new PropertyChangeSupport(this);
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
				}
			}
		} catch (Exception e) {

		} finally {
			cerrarConexion();
		}
	}

	private void delete(String query) throws SQLException {
		// TODO Auto-generated method stub
		evento.firePropertyChange("DELETE", null, new Transaction());
		Statement stmt = conn.createStatement();
		int registros = stmt.executeUpdate(query);
		Date date = new Date();
		 registro = new Transaction();
		 registro.setUserTransaction(username);
		 registro.setDatabaseUsed(database);
		 registro.settType(TransactionType.DELETE);
		 registro.setQueryExecuted(query);
		 registro.setRegistros(registros);
		 registro.setExecutionDate(date.getTime());

	}

	private void insertar(String query) throws SQLException {
		evento.firePropertyChange("INSERT", null, new Transaction());
		Statement stmt = conn.createStatement();
		int registros = stmt.executeUpdate(query);
		Date date = new Date();
		 registro = new Transaction();
		 registro.setUserTransaction(username);
		 registro.setDatabaseUsed(database);
		 registro.settType(TransactionType.INSERT);
		 registro.setQueryExecuted(query);
		 registro.setRegistros(registros);
		 registro.setExecutionDate(date.getTime());

	}

	private void update(String query) throws SQLException {
		evento.firePropertyChange("UPDATE", null, new Transaction());
		Statement stmt = conn.createStatement();
		int registros = stmt.executeUpdate(query);
		Date date = new Date();
		 registro = new Transaction();
		 registro.setUserTransaction(username);
		 registro.setDatabaseUsed(database);
		 registro.settType(TransactionType.UPDATE);
		 registro.setQueryExecuted(query);
		 registro.setRegistros(registros);
		 registro.setExecutionDate(date.getTime());

	}

	private void select(String query) throws SQLException {
		evento.firePropertyChange("SELECT", null, new Transaction());
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
		 registro = new Transaction();
		 registro.setUserTransaction(username);
		 registro.setDatabaseUsed(database);
		 registro.settType(TransactionType.SELECT);
		 registro.setQueryExecuted(query);
		 registro.setRegistros(registros);
		 registro.setExecutionDate(date.getTime());
		
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
		ArrayList<Transaction> solicitadas = new ArrayList<Transaction>();
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
		ArrayList<Transaction> solicitadas = new ArrayList<Transaction>();
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
		ArrayList<Transaction> solicitadas = new ArrayList<Transaction>();
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

}
