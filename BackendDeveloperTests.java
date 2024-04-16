import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class BackendDeveloperTests {

  /**
   * Additional test method for the loadGraphData method in the BackendInterface.
   */
  @Test
  public void testLoadGraphData() {
    BackendInterface backend = new Backend(new GraphPlaceholder());
    try {
      backend.loadGraphData("src" + File.separator + "campus.dot");
    } catch (IOException e) {
      Assertions.fail("Exception thrown when loading graph data");
    }
    // now try an invalid file
    try {
      backend.loadGraphData("src" + File.separator + "adjfklasdjfklasdjfkldajsffjkldafdd");
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
    GraphADT<String, Double> graph = new GraphPlaceholder();
    // now create a backend, remember to replace it with the actual Backend object and not the placeholder
    BackendInterface backend = new Backend(graph);
    try {
      backend.loadGraphData("src" + File.separator + "graph1.dot");
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
    GraphADT<String, Double> graph = new GraphPlaceholder();
    BackendInterface backend = new Backend(graph); // verify with TA that we replace with Backend ??
    try {
      backend.loadGraphData("src" + File.separator + "graph2.dot");
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
    GraphADT<String, Double> graph = new GraphPlaceholder();

    BackendInterface backend = new Backend(graph);
    try {
      backend.loadGraphData("src" + File.separator + "graph2.dot");
    } catch (IOException e) {
      Assertions.fail("Error while loading graph data");
    }
    // now create a path and test the travel times
    backend.findShortestPath("A", "E");
    Assertions.assertEquals(Arrays.asList(4.0, 2.0, 1.0), backend.getTravelTimesOnPath("A", "E"));
  }

  @Test
  public void testVia() {
    GraphADT<String, Double> graph1 = new GraphPlaceholder();
    // test findShortestPathVia
    BackendInterface backend1 = new Backend(graph1);
    try {
      backend1.loadGraphData("src" + File.separator + "graph2.dot");
    } catch (IOException e) {
      Assertions.fail("Error while loading graph data");
    }

    // test the correct path is outputted
    Assertions.assertEquals(Arrays.asList("A", "D", "B", "E"), backend1.findShortestPathVia("A", "D", "E"));
    // test that an empty list is outputted for a path that doesn't exist
    Assertions.assertEquals(Arrays.asList(), backend1.findShortestPathVia("A", "F", "E"));

    // then test getTravelTimesOnPathVia with a new graph
    GraphADT<String, Double> graph2 = new GraphPlaceholder();
    BackendInterface backend2 = new Backend(graph2);
    try {
      backend2.loadGraphData("src" + File.separator + "graph2.dot");
    } catch (IOException e) {
      Assertions.fail("Error while loading graph data");
    }
    // now test the via travel times method
    // create a path and check the travel times
    backend2.findShortestPathVia("A", "C", "E");
    Assertions.assertEquals(Arrays.asList(1.0, 10.0), backend2.getTravelTimesOnPathVia("A", "C", "E"));
    // once again, ensure an empty list is returned for a path that doesn't exist
    Assertions.assertEquals(Arrays.asList(), backend2.getTravelTimesOnPathVia("A", "B", "G"));
    Assertions.assertEquals(Arrays.asList(), backend2.getTravelTimesOnPathVia("A", "P", "E"));
    Assertions.assertEquals(Arrays.asList(), backend2.getTravelTimesOnPathVia("G", "B", "P"));
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