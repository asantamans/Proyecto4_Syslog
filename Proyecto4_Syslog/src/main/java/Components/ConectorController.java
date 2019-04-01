package Components;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class ConectorController {
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

	private LogController logController;
	private String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false";// Ruta de la
																									// conexion
	private String driver = "com.mysql.jdbc.Driver";// Driver

	public ConectorController() {
		this.database = "";
		this.hostname = "";
		this.port = "";
		this.username = "root";// Por defecto
		this.password = "";
		this.sSQL = "";
		this.url = "jdbc:mysql://" + hostname + ":" + port + "/" + database;
		logController = new LogController();
	}

	public ConectorController(String hostname, String port, String database) {
		this.database = database;
		this.hostname = hostname;
		this.port = port;
		this.username = "root";// Por defecto
		this.password = "";
		this.sSQL = "";
		this.url = "jdbc:mysql://" + hostname + ":" + port + "/" + database;
		logController = new LogController();
	}

	public void setCredentials(String username, String password) {
		this.username = username;
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

	public void ejecutarQuery(String query) {
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

		}
	}

	private void delete(String query) {
		// TODO Auto-generated method stub
		System.out.println("Ejecutado Delete");
	}

	private void insertar(String query) {
		System.out.println("Ejecutado insert");
	}

	private void update(String query) {
		System.out.println("Ejecutado update");
	}

	private void select(String query) throws SQLException {
		Statement stmt = conn.createStatement();

		ResultSet rs = stmt.executeQuery(query);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		while (rs.next()) {
			for (int i = 1; i <= columnsNumber; i++) {
				if (i > 1) {
				String columnValue = rs.getString(i);
				System.out.print(columnValue+ " ");
				}
				else {
					String columnValue = rs.getString(i);
					System.out.print(columnValue+ " ");
				}
			}
			System.out.println("");
		}
		System.out.println("\n\n");
	}

	private String eliminarVaciosInicio(String query) {
		while (query.charAt(0) == ' ') {
			query = query.substring(1, query.length());
		}
		return query;
	}

}
