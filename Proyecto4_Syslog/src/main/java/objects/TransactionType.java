package objects;

public enum TransactionType {
	INSERT {
		public String toString() {
			return "INSERT";
		}
	},
	DELETE {
		public String toString() {
			return "DELETE";
		}
	},
	UPDATE {
		public String toString() {
			return "UPDATE";
		}
	},
	SELECT {
		public String toString() {
			return "SELECT";
		}
	},
	PROCEDURE {
		public String toString() {
			return "PROCEDURE";
		}
	}
	
}
