package Pruebas;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import beans.ConectorController;
import beans.QueryEvento;
import objects.Transaction;
import objects.TransactionType;

public class Pruebas2 {

	public static void main(String[] args) throws SQLException {
		ConectorController a = new ConectorController();
		QueryEvento evt = new QueryEvento();
		a.addPropertyChangeListener(evt);

		a.setDatabase("forhonor");
		a.setHostSettings("localhost", "3306");
		a.setCredentials("root", "");

		System.out.println("SELECT * FROM PERSONAJE;");
		a.ejecutarQuery("Select * from personaje");
		System.out.println("SELECT * FROM USERS");
		a.ejecutarQuery("SELECT * from users");
		
		a.setCredentials("root","");
		System.out.println("CALL test2;");
		a.ejecutarQuery("CALL test2");

		a.setCredentials("arnau", "");
		System.out.println("SELECT * FROM FACCION");
		a.ejecutarQuery("Select * from faccion");
		a.setDatabase("bv");
		System.out.println("SELECT * FROM BFPLAYER");
		a.ejecutarQuery("select * from bfplayer");
		
		System.out.println("Realizamos un insert");
		a.ejecutarQuery("INSERT INTO `bfplayer`(`user_id`, `class_id`, `primary_weapon`, `device1`, `kills`, `Deads`) VALUES ('ARNOLD',2,1,2,9,7)");
		System.out.println("Ejecutamos update");
		a.ejecutarQuery("UPDATE bfplayer SET user_id='ARNOLD_PRO' WHERE user_id='ARNOLD'");
		System.out.println("Ejecutamos remove");
		a.ejecutarQuery("delete from bfplayer where user_id = 'ARNOLD_PRO'");
		
		
		System.out.println("\n\n\n\nPrintado de reporte de bbdd forhonor con query select");
		a.printReport("forhonor", TransactionType.SELECT);

		
		System.out.println("\n\n\n\nPrintado de reporte de bbdd forhonor con usuario arnau");
		a.printReport("forhonor", "arnau");

		System.out.println("\n\n\n\nPrintado de reporte de bbdd forhonor con usuario root");
		a.printReport("forhonor", "root");

		System.out.println("\n\n\n\nPrintado de reporte  de bbdd bv i usuario arnau");
		a.printReport("bv", "arnau");

		System.out.println("\n\n\n\nPrintado de reporte  de bbdd bv i usuario root");
		a.printReport("bv", "root");

		System.out.println("\n\n\n\nPrintado de reporte de bbdd forhonor con query procedure");
		a.printReport("forhonor",TransactionType.PROCEDURE);

	}

	private static void printReport(ArrayList<String> reporte) {
		if (reporte != null) {
			for (String a : reporte) {
				System.out.println(reporte);
			}
		} else {
			System.out.println("No se han encontrado registros");
		}

	}

}
