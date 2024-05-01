import javafx.application.Application;

public class App {
  public static void main(String[] args) {

    // TODO fill out the partner form!

    System.out.println("v0.1");
    Frontend.setBackend(new Backend(new DijkstraGraph<>()));
    Application.launch(Frontend.class, args);
  }
}
