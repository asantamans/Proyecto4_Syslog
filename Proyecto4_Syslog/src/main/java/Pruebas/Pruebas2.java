package Pruebas;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import Objects.TransactionType;
import beans.ConectorController;
import beans.QueryEvento;

public class Pruebas2 {
	
	public static void main(String[] args) throws SQLException {
		ConectorController a = new ConectorController();
		QueryEvento evt = new QueryEvento();
		a.addPropertyChangeListener(evt);
		
		a.setDatabase("forhonor");
		a.setHostSettings("localhost","3306");
		a.setCredentials("root","");
		
		System.out.println("SELECT * FROM PERSONAJE;");
		a.ejecutarQuery("Select * from personaje");
		System.out.println("SELECT * FROM USERS");
		a.ejecutarQuery("SELECT * from users");
		
		a.setCredentials("arnau","");
		System.out.println("SELECT * FROM FACCION");
		a.ejecutarQuery("Select * from faccion");
		a.setDatabase("bv");
		System.out.println("SELECT * FROM BFPLAYER");
		a.ejecutarQuery("select * from bfplayer");
		
		System.out.println("\n\n\n\nPrintado de reporte de bbdd forhonor con query select");
		a.getReport("forhonor", TransactionType.SELECT);
		
		System.out.println("\n\n\n\nPrintado de reporte de bbdd forhonor con usuario arnau");
		a.getReport("forhonor","arnau");
		
		System.out.println("\n\n\n\nPrintado de reporte de bbdd forhonor con usuario root");
		a.getReport("forhonor","root");
		
		System.out.println("\n\n\n\nPrintado de reporte  de bbdd bv i usuario arnau");
		a.getReport("bv", "arnau");
		
		System.out.println("\n\n\n\nPrintado de reporte  de bbdd bv i usuario root");
		a.getReport("bv", "root");
		
		
	}
	
}
