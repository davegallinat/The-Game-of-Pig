module MP7_dgallinat {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	requires java.base;
	requires java.sql;
	
	opens pigGame to javafx.graphics, javafx.fxml;
}
