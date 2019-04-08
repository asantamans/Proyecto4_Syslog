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

	public ArrayList<String> getResultsSelect(String query) throws SQLException {
		iniciarConexion();
		/*
		 * Funcion para transaciones sql de tipo select registros = numero de filas
		 * devueltas; devuelve los registros en cadena de String con formato
		 * registro1;registro2;registro3;.... registro1_2;registro2_2;registro3_2;....
		 * registro1_3;registro2_3;registro3_3;....
		 */
		int registros = 0;
		ArrayList<String> result = new ArrayList<String>();

		try {
			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(query);

			ResultSetMetaData rsmd = rs.getMetaData();

			int columnsNumber = rsmd.getColumnCount();

			while (rs.next()) {
				String intermedio = "";
				++registros;
				
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1 && i < columnsNumber) {
						String columnValue = rs.getString(i);
						intermedio = intermedio + columnValue + ";";
					} else if (i == columnsNumber) {
						String columnValue = rs.getString(i);
						intermedio = intermedio + columnValue;

					} else {
						String columnValue = rs.getString(i);
						intermedio = intermedio + columnValue + ";";

					}
				}
				result.add(intermedio);
			}

		} catch (SQLException e) {

		} finally {
			Date date = new Date();
			Transaction transaction = new Transaction();
			transaction.setUserTransaction(username);
			transaction.setDatabaseUsed(database);
			transaction.settType(TransactionType.SELECT);
			transaction.setQueryExecuted(query);
			transaction.setRegistros(registros);
			transaction.setExecutionDate(date.getTime());
			evento.firePropertyChange("SELECT", null, transaction);
			cerrarConexion();
			return result;
		}

	}

	public void ejecutarQuery(String query) throws SQLException {
		iniciarConexion();
		this.sSQL = query;
		// Limpiamos el formato de la query introducida en caso que contenga espacios no
		// deseados al inicio de esta para asi poder saber que tipo de sentencia se
		// trata i como proceder
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
				} else if (tipo.equalsIgnoreCase("CALL")) {
					procedure(query);
				}
			}
		} catch (SQLException e) {

		} finally {
			cerrarConexion();
		}
	}

	private void delete(String query) throws SQLException {
		// Funcion para transaciones de tipo delete
		int registros = 0;
		try {
			Statement stmt = conn.createStatement();
			registros = stmt.executeUpdate(query);
		} catch (SQLException e) {

		} finally {
			// Una vez lanzada la query, recogemos todos los datos que nos interesa de esta
			// i lanzamos el evento esperando a que el listener lo escuche
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

	}

	private void insertar(String query) {
		/*
		 * Funcion para transaciones sql de tipo insert
		 * 
		 */
		int registros = 0;
		try {
			Statement stmt = conn.createStatement();
			registros = stmt.executeUpdate(query);
		} catch (SQLException e) {

		} finally {
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
	}

	private void update(String query) {
		/*
		 * Funcion para transaciones sql de tipo update
		 * 
		 */
		int registros = 0;
		try {
			Statement stmt = conn.createStatement();
			registros = stmt.executeUpdate(query);
		} catch (SQLException e) {

		} finally {
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
	}

	private void select(String query) {
		/*
		 * Funcion para transaciones sql de tipo select registros = numero de filas
		 * devueltas
		 */
		int registros = 0;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();

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
		} catch (SQLException e) {

		} finally {
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

	}

	private void procedure(String query) throws SQLException {
		/*
		 * Funcion para ejecucion de procedures
		 * 
		 */
		try {
			CallableStatement statement = conn.prepareCall(query);
			statement.execute();

		} finally {
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

	}

	private String eliminarVaciosInicio(String query) {
		/*
		 * Funcion transitoria para eliminar posibles vacios delante del tipo de
		 * sentencia SQL, ej: "   SELECT" => "SELECT"
		 */
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
		// Devuelve un listado de todos los usuarios que han hecho una transacion sqlq
		// de cualquier tipo
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
		/*
		 * Funcion que parsea una lista de queries ejecutadas en formato String para ser
		 * impreso por pantalla o devuelto como valor
		 */
		ArrayList<String> queryList = new ArrayList<String>();
		switch (format) {
		case QUERY_TYPE: {
			for (Transaction tmp : solicitadas) {

				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date(tmp.getExecutionDate());
				String intermedio = "[" + tmp.getQueryExecuted() + ";" + dateFormat.format(date) + ";"
						+ tmp.gettType().toString() + "]";
				queryList.add(intermedio);

			}
			break;
		}
		case USER_LIST: {
			for (Transaction tmp : solicitadas) {

				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date(tmp.getExecutionDate());
				String intermedio = "[" + tmp.getQueryExecuted() + ";" + dateFormat.format(date) + "]";
				queryList.add(intermedio);
			}
			break;
		}
		case SIMPLE: {
			for (Transaction tmp : solicitadas) {

				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date(tmp.getExecutionDate());
				String intermedio = "[" + tmp.getQueryExecuted() + ";" + dateFormat.format(date) + ";"
						+ tmp.getUserTransaction() + "]";
				queryList.add(intermedio);

			}
			break;
		}
		}

		return queryList;
	}

	public ArrayList<String> getReport(String database, String user) {
		// Funcion que devuelve un listado de las sentencias sql ejecutadas en la base
		// de datos i por el usuario pasados como parametros

		// Obtenemos el arraylist del evento que escucha, en nuestro caso es el [0] ya
		// que solo tenemos uno, en caso de tener mas podriamos filtrar por el
		// propertyName del que se lanza el evento
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
		// Funcion que devuelve un listado de las sentencias sql ejecutadas sobre la
		// base de datos pasada como parametro por el usuario del parametro y ffiltrados
		// por el tipo de sentencia
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
		/*
		 * Recuperamos la lista de eventos del listener, al tener solo 1, esta en la
		 * posicion 0 y asi le sacamos el arraylist de transiciones
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

	public void printReport(String database, TransactionType tipo) {
		// Imprime por pantalla el reporte solicitado. ver getReport(String
		// database,TransactionType tipo)
		ArrayList<String> reporte = new ArrayList<String>();
		reporte = getReport(database, tipo);

		if (reporte != null) {
			for (String a : reporte) {
				System.out.println(a);
			}
		} else {
			System.out.println("No se han encontrado registros");
		}
	}

	public void printReport(String database, String user, TransactionType tipo) {
		// Imprime por pantalla el reporte solicitado. ver getReport(String
		// database,String user,TransactionType tipo)
		ArrayList<String> reporte = new ArrayList<String>();
		reporte = getReport(database, user, tipo);
		if (reporte != null) {
			System.out.println();
			for (String a : reporte) {
				System.out.println(a);
			}
		} else {
			System.out.println("No se han encontrado registros");
		}
	}

	public void printReport(String database, String user) {
		// Imprime por pantalla el reporte solicitado. ver getReport(String
		// database,String user)
		ArrayList<String> reporte = new ArrayList<String>();
		reporte = getReport(database, user);
		if (reporte != null) {
			for (String a : reporte) {
				System.out.println(a);
			}
		} else {
			System.out.println("No se han encontrado registros");
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		evento.addPropertyChangeListener(listener);

	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		evento.removePropertyChangeListener(listener);
	}

}
