module BADFinalProjectLAB {
	requires javafx.graphics;
	requires javafx.controls;
	requires java.sql;
	requires javafx.base;
	
	
	
	opens BADFinalProjectLAB;
	opens Model;
	opens Controller;
	opens Util;
	opens View;
	exports BADFinalProjectLAB;
}