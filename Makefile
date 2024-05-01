runTests: Frontend.java FrontendDeveloperTests.java BackendPlaceholder.java
	javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar Frontend.java
	javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar FrontendDeveloperTests.java
	javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar BackendPlaceholder.java
	java --module-path ../javafx/lib --add-modules javafx.controls --add-opens javafx.graphics/com.sun.javafx.application=ALL-UNNAMED -jar ../junit5fx.jar -cp . -c FrontendDeveloperTests

clean:
	rm -rf *.class
runApp: App.java
	javac --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar App.java
	java --module-path ../javafx/lib --add-modules javafx.controls -cp .:../junit5fx.jar App.java

runBDTests: Backend.java BackendDeveloperTests.java BackendInterface.java GraphADT.java
	javac Backend.java
	javac -cp .:../junit5.jar BackendDeveloperTests.java
	java -jar ../junit5.jar -cp . -c BackendDeveloperTests

