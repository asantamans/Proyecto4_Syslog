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

		a.setCredentials("arnau", "");
		System.out.println("SELECT * FROM FACCION");
		a.ejecutarQuery("Select * from faccion");
		a.setDatabase("bv");
		System.out.println("SELECT * FROM BFPLAYER");
		a.ejecutarQuery("select * from bfplayer");

		ArrayList<String> reporte = new ArrayList<String>();
		System.out.println("\n\n\n\nPrintado de reporte de bbdd forhonor con query select");
		reporte = a.getReport("forhonor", TransactionType.SELECT);
		printReport(reporte);

		System.out.println("\n\n\n\nPrintado de reporte de bbdd forhonor con usuario arnau");
		reporte = a.getReport("forhonor", "arnau");
		printReport(reporte);

		System.out.println("\n\n\n\nPrintado de reporte de bbdd forhonor con usuario root");
		reporte = a.getReport("forhonor", "root");
		printReport(reporte);

		System.out.println("\n\n\n\nPrintado de reporte  de bbdd bv i usuario arnau");
		reporte = a.getReport("bv", "arnau");
		 printReport(reporte);
		
		System.out.println("\n\n\n\nPrintado de reporte  de bbdd bv i usuario root");
		reporte = a.getReport("bv", "root");
		 printReport(reporte);

	}

	private static void printReport(ArrayList<String> reporte) {
		if (reporte != null) {
			for (String a : reporte) {
				System.out.println(reporte);
			}
		}else {
			System.out.println("No se han encontrado registros");
		}

	}

}
