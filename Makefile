runFDTests: Backend.java BackendDeveloperTests.java
	javac Backend.java
    javac -cp .:../junit5.jar BackendDeveloperTests.java
    java -jar ../junit5.jar -cp . -c BackendDeveloperTests
clean:
    rm *.class