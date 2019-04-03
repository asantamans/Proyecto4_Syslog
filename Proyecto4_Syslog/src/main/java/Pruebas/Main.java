package Pruebas;

import java.util.ArrayList;
import java.util.Scanner;

import Objects.TransactionType;
import beans.ConectorController;

public class Main {
	public static void main(String[] args) {
		ConectorController a = new ConectorController();
		a.setDatabase("forhonor");
		a.setHostSettings("localhost", "3306");

		boolean end = false;
		while (!end) {
			printOptions();
			Scanner cin = new Scanner(System.in);
			String opcion = cin.next();
			System.out.println("\n\n\n\n");
			try {
				int num = Integer.parseInt(opcion);
				switch (num) {
				case 1: {
					String username, password;
					System.out.print("Nombre de usuario:");
					username = cin.next();
					System.out.print("Contrasenya:");
					password = cin.next();
					a.setCredentials(username, password);
					break;
				}
				case 2: {
					String database;
					System.out.print("Base de datos:");
					database = cin.next();
					a.setDatabase(database);
					break;
				}
				case 3: {
					String hostname, port;
					System.out.print("Introduce Host:");
					hostname = cin.next();
					System.out.print("Puerto:");
					port = cin.next();
					a.setHostSettings(hostname, port);
					break;
				}
				case 4: {
					String query;
					System.out.print("Introduce su sentencia SQL:");
					query = cin.next();
					a.ejecutarQuery(query);
					break;
				}
				case 5: {
					printLogOptions();
					int reportNum = Integer.parseInt(cin.next());
					String database, user, type;
					ArrayList<String> reporte = new ArrayList<String>();
					switch (reportNum) {

					case 1: {
						System.out.print("Introduce base de datos:");
						database = cin.next();
						System.out.print("Introduce usuario:");
						user = cin.next();
						reporte = a.getReport(database, user);
						break;
					}
					case 2: {
						System.out.print("Introduce base de datos:");
						database = cin.next();
						System.out.print("Introduce usuario:");
						user = cin.next();
						System.out.print("Introduce tipo de query");
						type = cin.next();
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
						database = cin.next();
						System.out.print("Introduce tipo de query");
						type = cin.next();
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
						break;
					}
					}
					if (reporte.size()> 0) {
						System.out.println("Mostrando reporte solicitado");
						for (String tmp : reporte) {
							System.out.println(tmp);
						}
					}else {
						System.out.println("No se han encontrado registros de los datos solicitados");
					}
				}
				}
			} catch (Exception e) {
				cin.close();
				cin = new Scanner(System.in);
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

}
