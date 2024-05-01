runApp: Backend.java Frontend.java App.java DijkstraGraph.java GraphADT.java BackendInterface.java FrontendInterface.java
	javac --module-path ../javafx/lib --add-modules javafx.controls Frontend.java
	javac --module-path ../javafx/lib --add-modules javafx.controls Backend.java
	javac --module-path ../javafx/lib --add-modules javafx.controls App.java
	java --module-path ../javafx/lib --add-modules javafx.controls App
runTests: runBDTests runFDTests
runBDTests: Backend.java BackendInterface.java GraphADT.java
	javac Backend.java
	javac -cp .:../junit5.jar BackendDeveloperTests.java
	java -jar ../junit5.jar -cp . -c BackendDeveloperTests
runFDTests: Frontend.java FrontendDeveloperTests.java
	javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar Frontend.java
	javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar FrontendDeveloperTests.java
	javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar BackendPlaceholder.java
	java --module-path ../javafx/lib --add-modules javafx.controls --add-opens javafx.graphics/com.sun.javafx.application=ALL-UNNAMED -jar ../junit5fx.jar -cp . -c FrontendDeveloperTests

clean:
	rm -rf *.class
