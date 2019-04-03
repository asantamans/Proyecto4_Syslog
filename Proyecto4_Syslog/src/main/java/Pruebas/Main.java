package Pruebas;

import Components.ConectorController;

public class Main {
	public static void main(String[] args) {
		ConectorController a = new ConectorController();
		a.setDatabase("forhonor");
		a.setHostSettings("localhost","3306");
	
		
	}
}
