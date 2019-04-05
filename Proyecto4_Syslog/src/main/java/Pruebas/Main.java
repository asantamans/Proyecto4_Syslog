package Pruebas;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import beans.ConectorController;
import beans.QueryEvento;
import objects.TransactionType;

public class Main {
	public static void main(String[] args) throws SQLException {
		ConectorController a = new ConectorController();
		QueryEvento evt = new QueryEvento();
		a.addPropertyChangeListener(evt);
		a.setDatabase("forhonor");
		a.setHostSettings("localhost", "3306");
		a.setCredentials("root","");
		boolean end = false;
		while (!end) {
			printOptions();
			try {
				Scanner numeroLector = new Scanner(System.in);
				int num =numeroLector.nextInt();
				switch (num) {
				case 1: {
					String username, password;
					System.out.print("Nombre de usuario:");
					username = leerString();
					System.out.print("Contrasenya:");
					password = leerString();
					a.setCredentials(username, password);
					break;
				}
				case 2: {
					String database;
					System.out.print("Base de datos:");
					database = leerString();
					a.setDatabase(database);
					break;
				}
				case 3: {
					String hostname, port;
					System.out.print("Introduce Host:");
					hostname = leerString();
					System.out.print("Puerto:");
					port = leerString();
					a.setHostSettings(hostname, port);
					break;
				}
				case 4: {
					String query;
					System.out.print("Introduce su sentencia SQL:");
					
					query = leerString();
					a.ejecutarQuery(query);
					break;
				}
				case 5: {
					printLogOptions();
					int reportNum = Integer.parseInt(leerString());
					String database, user, type;
					ArrayList<String> reporte = new ArrayList<String>();
					switch (reportNum) {

					case 1: {
						System.out.print("Introduce base de datos:");
						database =leerString();
						System.out.print("Introduce usuario:");
						user = leerString();
						reporte = a.getReport(database, user);
						System.out.println(reporte.size());
						break;
					}
					case 2: {
						System.out.print("Introduce base de datos:");
						database = leerString();
						System.out.print("Introduce usuario:");
						user = leerString();
						System.out.print("Introduce tipo de query");
						type = leerString();
						if (type.equalsIgnoreCase("INSERT")) {
							reporte = a.getReport(database, user, TransactionType.INSERT);
						} else if (type.equalsIgnoreCase("DELETE")) {
							reporte = a.getReport(database, user, TransactionType.DELETE);
						} else if (type.equalsIgnoreCase("UPDATE")) {
							reporte = a.getReport(database, user, TransactionType.UPDATE);
						} else if (type.equalsIgnoreCase("SELECT")) {
							reporte = a.getReport(database, user, TransactionType.SELECT);
						} else {
							System.out.println("No se reconoce el tipo de query insertado");
						}

						break;
					}
					case 3: {
						System.out.print("Introduce base de datos:");
						database = leerString();
						System.out.print("Introduce tipo de query");
						type = leerString();
						if (type.equalsIgnoreCase("INSERT")) {
							reporte = a.getReport(database, TransactionType.INSERT);
						} else if (type.equalsIgnoreCase("DELETE")) {
							reporte = a.getReport(database, TransactionType.DELETE);
						} else if (type.equalsIgnoreCase("UPDATE")) {
							reporte = a.getReport(database, TransactionType.UPDATE);
						} else if (type.equalsIgnoreCase("SELECT")) {
							reporte = a.getReport(database, TransactionType.SELECT);
						} else {
							System.out.println("No se reconoce el tipo de query insertado");
						}
					
					}
					}
					System.out.println(reporte.size());
					if (reporte.size()> 0) {
						System.out.println("Mostrando reporte solicitado");
						for (String tmp : reporte) {
							System.out.println(tmp);
						}
					}else {
						System.out.println("No se han encontrado registros de los datos solicitados");
					}
					break;
				}
				}
			} catch (Exception e) {
				
			}
		}
	}

	private static void printOptions() {
		System.out.println("################Panel de control###################");
		System.out.println("1:Insertar credenciales");
		System.out.println("2:Insertar BBDD");
		System.out.println("3:Insertar opciones de Host");
		System.out.println("4:Insertar query");
		System.out.println("5:Obtener registros");
		System.out.print("Introduce una opcion:");
	}

	private static void printLogOptions() {
		System.out.println("###########Opciones de reporte####################");
		System.out.println("Introduce la opcion por la que desea filtrar:");
		System.out.println("1:BBDD y usuario");
		System.out.println("2:BBDD,usuario y tipo query");
		System.out.println("3:BBDD y tipo query");
		System.out.print("Introduce una opcion:");
	}
	private static String leerString() {
		Scanner cin = new Scanner(System.in);
		
		return cin.nextLine();
	}
	private static int leerInt() {
		Scanner cin = new Scanner(System.in);
		int retorno = cin.nextInt();
		cin.close();
		return retorno;
	}

}
