import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Testers for the Backend class. These tests are designed to test the Backend class and its methods.
 */
public class BackendDeveloperTests extends ApplicationTest {

  /**
   * Runs frontend before each test
   */
  @BeforeEach
  public void setup() throws Exception {
    ApplicationTest.launch(Frontend.class);
  }

  /**
   * Additional test method for the loadGraphData method in the BackendInterface.
   */
  @Test
  public void testLoadGraphData() {
    BackendInterface backend = new Backend(new GraphPlaceholder());
    try {
      backend.loadGraphData("campus.dot");
    } catch (IOException e) {
      Assertions.fail("Exception thrown when loading graph data");
    }
    // now try an invalid file
    try {
      backend.loadGraphData("adjfklasdjfklasdjfkldajsffjkldafdd");
      Assertions.fail("No exception thrown when loading invalid file");
    } catch (IOException e) {
      // expected
    }
  }

  /**
   * Test method for getListOfAllLocations method in the BackendInterface.
   */
  @Test
  public void testList() {
    GraphADT<String, Double> graph = new DijkstraGraph<>();
    // now create a backend, remember to replace it with the actual Backend object and not the placeholder
    BackendInterface backend = new Backend(graph);
    try {
      backend.loadGraphData("graph1.dot");
    } catch (IOException e) {
      Assertions.fail("Error loading graph data, not caused by getListOfAllLocations method");
    }
    // call the method and ensure output matches expected
    List<String> actual = backend.getListOfAllLocations();
    List<String> expected = Arrays.asList("A", "B", "C", "D");
    // for each location, ensure it is in the expected list
    for (String location : expected) {
      Assertions.assertTrue(actual.contains(location), "List of locations does not contain " + location);
    }
  }

  /**
   * Test method for findShortestPath method in the BackendInterface.
   */
  @Test
  public void testShortestPath() {
    // create a graph and add some locations
    GraphADT<String, Double> graph = new DijkstraGraph<>();
    BackendInterface backend = new Backend(graph);
    try {
      backend.loadGraphData("graph2.dot");
    } catch (IOException e) {
      Assertions.fail("Error loading graph data, not caused by findShortestPath method");
    }
    // test the correct path is outputted
    Assertions.assertEquals(Arrays.asList("A", "D", "B", "E"), backend.findShortestPath("A", "E"));
    // test that an empty list is outputted for a path that doesn't exist
    Assertions.assertEquals(Arrays.asList(), backend.findShortestPath("A", "F"));
  }

  /**
   * Test method for getTravelTimesOnPath method in the BackendInterface.
   */
  @Test
  public void testTravelTimes() {
    GraphADT<String, Double> graph = new DijkstraGraph<>();

    BackendInterface backend = new Backend(graph);
    try {
      backend.loadGraphData("graph2.dot");
    } catch (IOException e) {
      Assertions.fail("Error while loading graph data");
    }
    // now create a path and test the travel times
    backend.findShortestPath("A", "E");
    Assertions.assertEquals(Arrays.asList(4.0, 2.0, 1.0), backend.getTravelTimesOnPath("A", "E"));
  }

  /**
   * Test method for findShortestPathVia and getTravelTimesOnPathVia methods in the BackendInterface.
   */
  @Test
  public void testVia() {
    GraphADT<String, Double> graph1 = new DijkstraGraph<>();
    // test findShortestPathVia
    BackendInterface backend1 = new Backend(graph1);
    try {
      backend1.loadGraphData("graph2.dot");
    } catch (IOException e) {
      Assertions.fail("Error while loading graph data");
    }

    // test the correct path is outputted
    Assertions.assertEquals(Arrays.asList("A", "D", "B", "E"), backend1.findShortestPathVia("A", "D", "E"));
    // test that an empty list is outputted for a path that doesn't exist
    Assertions.assertEquals(Arrays.asList(), backend1.findShortestPathVia("A", "F", "E"));

    // then test getTravelTimesOnPathVia with a new graph
    GraphADT<String, Double> graph2 = new DijkstraGraph<>();
    BackendInterface backend2 = new Backend(graph2);
    try {
      backend2.loadGraphData("graph2.dot");
    } catch (IOException e) {
      Assertions.fail("Error while loading graph data");
    }
    // now test the via travel times method
    // create a path and check the travel times
    backend2.findShortestPathVia("A", "C", "E");
    Assertions.assertEquals(Arrays.asList(1.0, 1.0, 4.0, 2.0, 1.0), backend2.getTravelTimesOnPathVia("A", "C", "E"));
    // once again, ensure an empty list is returned for a path that doesn't exist
    Assertions.assertEquals(Arrays.asList(), backend2.getTravelTimesOnPathVia("A", "B", "G"));
    Assertions.assertEquals(Arrays.asList(), backend2.getTravelTimesOnPathVia("A", "P", "E"));
    Assertions.assertEquals(Arrays.asList(), backend2.getTravelTimesOnPathVia("G", "B", "P"));
  }


  /**
   * Test method for the integration of the Backend and Frontend classes. Ensures that
   * the backend and frontend harmoniously and seamlessly integrate, and that the
   * correct path is found when the input is entered and the find button is pressed.
   */
  @Test
  public void testIntegration1() {
    // set backend
    GraphADT<String, Double> graph = new DijkstraGraph<>();
    BackendInterface backend = new Backend(graph);

    Frontend.setBackend(backend);
    try {
      backend.loadGraphData("graph2.dot");
    } catch (IOException e) {
      Assertions.fail("Error loading graph data, not caused by findShortestPath method");
    }

    // put correct locations in start and end fields
    clickOn("#start_text").write("A");
    clickOn("#end_text").write("E");

    // get reference to the results of the shortest path
    VBox pathResult = lookup("#pathResult").query();

    // find the path
    clickOn("#find");

    // make sure result has the correct number of locations, +1 for the title of "Path Result:"
    Assertions.assertEquals(5, pathResult.getChildren().size());

    // make sure every label in the path is correct
    String[] expectedResult = {"Path Result:", "A", "D", "B", "E"};
    for (int i = 1; i < pathResult.getChildren().size(); i++) {
      Assertions.assertEquals(expectedResult[i], ((Label) pathResult.getChildren().get(i)).getText());
    }
  }

  /**
   * Test method for the integration of the Backend and Frontend classes. Ensures that
   * the backend and frontend harmoniously and seamlessly integrate, and that the
   * backend correctly handles invalid point inputs, and the frontend displays the
   * correct output when the submit button is pressed.
   */
  @Test
  public void testIntegration2() {
    GraphADT<String, Double> graph = new DijkstraGraph<>();
    BackendInterface backend = new Backend(graph);

    Frontend.setBackend(backend);

    // put incorrect location in the start field and a correct location in the end field
    clickOn("#start_text").write("Mem");
    clickOn("#end_text").write("Atmospheric, Oceanic and Space Sciences");

    // get reference to the results of the shortest path
    VBox pathResult = lookup("#pathResult").query();

    // find the path
    clickOn("#find");

    // make sure result has the correct number of locations in it(should only have two labels with the title and the label stating that no path was found)
    Assertions.assertEquals(2, pathResult.getChildren().size());

    // make sure every Label in the result path is corerct
    String[] expectedResult = {"Path Result:", "No Path Found"};
    for (int i = 0; i < pathResult.getChildren().size(); i++) {
      Assertions.assertEquals(expectedResult[i], ((Label) pathResult.getChildren().get(i)).getText());
    }
  }

  /**
   * Verifies that no results appear when no input is given and the submit button is clicked.
   */
  @Test
  public void partnerTest1() {
    Frontend.setBackend(new BackendPlaceholder(new DijkstraGraph<>()));

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
   * Tests that results for the shortest path appear when two valid points are inputted with
   * a valid third location, but the use via checkbox is not checked.
   */
  @Test
  public void partnerTest2() {
    Frontend.setBackend(new BackendPlaceholder(new DijkstraGraph<>()));

    // put correct locations in the start, end, and via location fields
    clickOn("#start_text").write("Union South");
    clickOn("#end_text").write("Atmospheric, Oceanic and Space Sciences");
    clickOn("#viaLocation_text").write("Science Hall");

    // get reference to the results of the shortest path
    VBox pathResult = lookup("#pathResult").query();

    clickOn("#find");

    // make sure the shortest path has the correct number of locations in it
    Assertions.assertTrue(4 == pathResult.getChildren().size());

    // make sure each Label in the path is correct
    String[] expectedResult = {"Path Result:", "Union South", "Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences"};
    for (int i = 0; i < pathResult.getChildren().size(); i++) {
      Assertions.assertEquals(expectedResult[i], ((Label) pathResult.getChildren().get(i)).getText());
    }
  }


  private GraphADT<String, Double> lectureGraph() {
    GraphADT<String, Double> graph = new GraphPlaceholder();
    graph.insertNode("A");
    graph.insertNode("B");
    graph.insertNode("C");
    graph.insertNode("D");
    graph.insertNode("E");
    // now add the edges with the corresponding weights
    // make sure they go in both directions, so the graph is undirected
    graph.insertEdge("A", "C", 1.0);
    graph.insertEdge("C", "A", 1.0);

    graph.insertEdge("B", "E", 1.0);
    graph.insertEdge("E", "B", 1.0);

    graph.insertEdge("B", "D", 2.0);
    graph.insertEdge("D", "B", 2.0);

    graph.insertEdge("A", "D", 4.0);
    graph.insertEdge("D", "A", 4.0);

    graph.insertEdge("C", "E", 10.0);
    graph.insertEdge("E", "C", 10.0);

    graph.insertEdge("D", "E", 10.0);
    graph.insertEdge("E", "D", 10.0);

    graph.insertEdge("A", "B", 15.0);
    graph.insertEdge("B", "A", 15.0);

    return graph;
  }


}
