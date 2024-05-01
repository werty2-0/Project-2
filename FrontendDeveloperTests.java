
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.scene.control.TextField;
import javafx.application.Application;
import javafx.stage.Window;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.Node;

/**
 * This class uses unit tests to test the implementation in the Frontend class
 *
 * @author Parin Gouraram
 */
public class FrontendDeveloperTests extends ApplicationTest {


  /**
   * Runs frontend before each test
   */
  @BeforeEach
  public void setup() throws Exception {
    ApplicationTest.launch(Frontend.class);
  }


  /**
   * Tests that results for the shortest path appear when two valids points are inputed when the submit button is pressed.
   */
  @Test
  public void test1() {
    // set backend
    Frontend.setBackend(new BackendPlaceholder(new GraphPlaceholder()));

    // put correct locations in start and end fields
    clickOn("#start_text").write("Union South");
    clickOn("#end_text").write("Atmospheric, Oceanic and Space Sciences");

    // get reference to the results of the shortest path
    VBox pathResult = lookup("#pathResult").query();

    // find the path
    clickOn("#find");

    // make sure result has the correct number of locations
    Assertions.assertTrue(4 == pathResult.getChildren().size());

    // make sure every label in the path is correct
    String[] expectedResult = {"Path Result:", "Union South", "Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences"};
    for (int i = 0; i < pathResult.getChildren().size(); i++) {
      Assertions.assertEquals(expectedResult[i], ((Label) pathResult.getChildren().get(i)).getText());
    }
  }


  /**
   * Tests that no results appear when an invalid point is inputted when the submit button is pressed
   */
  @Test
  public void test2() {
    // set backend
    Frontend.setBackend(new BackendPlaceholder(new GraphPlaceholder()));

    // put incorrect location in the start field and a correct location in the end field
    clickOn("#start_text").write("Mem");
    clickOn("#end_text").write("Atmospheric, Oceanic and Space Sciences");

    // get reference to the results of the shortest path
    VBox pathResult = lookup("#pathResult").query();

    // find the path
    clickOn("#find");

    // make sure result has the correct number of locations in it(should only have two labels with the title and the label stating that no path was found)
    Assertions.assertTrue(2 == pathResult.getChildren().size());

    // make sure every Label in the result path is corerct
    String[] expectedResult = {"Path Result:", "No Path Found"};
    for (int i = 0; i < pathResult.getChildren().size(); i++) {
      Assertions.assertEquals(expectedResult[i], ((Label) pathResult.getChildren().get(i)).getText());
    }

  }

  /**
   * Tests that results for the shortest path appear with times when two valid points are inputed  when the travel time box is checked and when the submit button is pressed
   */
  @Test
  public void test3() {
    // set backend
    Frontend.setBackend(new BackendPlaceholder(new GraphPlaceholder()));

    // put correct locations in the start and end location fields
    clickOn("#start_text").write("Union South");
    clickOn("#end_text").write("Atmospheric, Oceanic and Space Sciences");

    // get reference to the results of the shortst path
    VBox pathResult = lookup("#pathResult").query();

    // check the show times box so that the shortest path shows the distance between each location in the path in seconds
    clickOn("#travelTimesBox");

    // find the path
    clickOn("#find");

    // make sure the shortest path has the correct number of locations in it
    Assertions.assertTrue(5 == pathResult.getChildren().size());

    // make sure each label in the path is corret
    String[] expectedResult = {"Path Result (With Times):", "Union South -> 176.0 secs", "Computer Sciences and Statistics -> 80.0 secs", "Atmospheric, Oceanic and Space Sciences", "Total Time: 256.0 secs"};
    for (int i = 0; i < pathResult.getChildren().size(); i++) {
      Assertions.assertEquals(expectedResult[i], ((Label) pathResult.getChildren().get(i)).getText());
    }
  }

  /**
   * Tests that results for the shortest path appear when a two valid points are inputed when the optional third mid location is checked and the submit button is pressed
   */
  @Test
  public void test4() {
    // set backend
    Frontend.setBackend(new BackendPlaceholder(new GraphPlaceholder()));

    // put correct locations in the start, end, and via location fields
    clickOn("#start_text").write("Memorial Union");
    clickOn("#end_text").write("Radio Hall");
    clickOn("#viaLocation_text").write("Science Hall");

    // get reference to the results of the shortest path
    VBox pathResult = lookup("#pathResult").query();

    // check the box to use the via location in the path and find the path
    clickOn("#useVia");
    clickOn("#find");

    // make sure the shortest path has the correct number of locations in it
    Assertions.assertTrue(4 == pathResult.getChildren().size());

    // make sure each Label in the path is correct
    String[] expectedResult = {"Path Result:", "Memorial Union", "Science Hall", "Radio Hall"};
    for (int i = 0; i < pathResult.getChildren().size(); i++) {
      Assertions.assertEquals(expectedResult[i], ((Label) pathResult.getChildren().get(i)).getText());
    }

  }


  /**
   * Tests that the about button on the screen displays information about this application when pressed the first time and removes it off the screen when pressed a second time
   */
  @Test
  public void test5() {
    // set backend
    Frontend.setBackend(new BackendPlaceholder(new GraphPlaceholder()));

    // create a reference to the about information and click on the about button
    Label about = lookup("#aboutSection").query();
    clickOn("#aboutButton");

    // make sure the about button displays the information about this application
    String expectedResult = "This app allows you to find the shortest path between any two buildings on the UW-Madison campus.\nClicking on the 'show times' button will allow you to show the time to walk between the locations in the path and the total time it takes to walk the entire path.\nLastly, you can add a location that the start and end  have to go through using the 'via location' area. Hit about again to make this disappear.";
    Assertions.assertEquals(expectedResult, about.getText());

    // click on the  about button again and make sure the application information disappears
    clickOn("#aboutButton");
    Assertions.assertEquals("", about.getText());

  }

  public static void main(String[] args) {
    try {
      ApplicationTest.launch(Frontend.class, args);
    } catch (Exception e) {
      System.out.println("Exception caught:\n" + e);
    }
  }


}
