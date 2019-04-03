package Objects;

import java.util.ArrayList;

public interface QueryHandler {

	 void queryLanzada(Transaction transaction);
	 ArrayList<String> reportSolicitado(String database, TransactionType tipo);
	 ArrayList<String> reportSolicitado(String database, String user, TransactionType tipo);
	 ArrayList<String> reportSolicitado(String database, String user);
}
