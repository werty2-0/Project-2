import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class BackendDeveloperTests {

  /**
   * Test method for getListOfAllLocations method in the BackendInterface.
   */
  @Test
  public void testList() {
    // create a graph and add some locations
    GraphADT<String, Double> graph = new GraphPlaceholder();
    graph.insertNode("A");
    graph.insertNode("B");
    graph.insertNode("C");
    graph.insertNode("D");

    graph.insertEdge("A", "B", 1.0);
    graph.insertEdge("A", "D", 3.0);
    graph.insertEdge("B", "C", 2.0);
    graph.insertEdge("D", "A", 3.0);

    // now create a backend
    BackendInterface backend = new BackendPlaceholder(graph);
    // call the method and ensure output matches expected
    List<String> locations = backend.getListOfAllLocations();
    List<String> expected = Arrays.asList("A", "B", "C", "D");
    // for each location, ensure it is in the expected list
    for (String location : expected) {
      Assertions.assertTrue(locations.contains(location));
    }
  }

  /**
   * Test method for findShortestPath method in the BackendInterface.
   */
  @Test
  public void testShortestPath() {
    // create a graph and add some locations
    GraphADT<String, Double> graph = lectureGraph();

    BackendInterface backend = new BackendPlaceholder(graph);
    // test the correct path is outputted
    Assertions.assertEquals(backend.findShortestPath("A", "E"), Arrays.asList("A", "D", "B", "E"));
    // test that an empty list is outputted for a path that doesn't exist
    Assertions.assertEquals(backend.findShortestPath("A", "F"), Arrays.asList());
  }

  /**
   * Test method for getTravelTimesOnPath method in the BackendInterface.
   */
  @Test
  public void testTravelTimes() {
    GraphADT<String, Double> graph = lectureGraph();

    BackendInterface backend = new BackendPlaceholder(graph);
    // since no path was created in the graph object, the travel times should be empty
    Assertions.assertEquals(backend.getTravelTimesOnPath("A", "E"), Arrays.asList());
    // now create a path and test the travel times
    backend.findShortestPath("A", "E");
    Assertions.assertEquals(backend.getTravelTimesOnPath("A", "E"), Arrays.asList(4.0, 2.0, 1.0));
  }

  @Test
  public void testVia() {
    GraphADT<String, Double> graph1 = lectureGraph();
    // test findShortestPathVia
    BackendInterface backend1 = new BackendPlaceholder(graph1);
    // test the correct path is outputted
    Assertions.assertEquals(backend1.findShortestPathVia("A", "C", "E"), Arrays.asList("A", "C", "E"));
    Assertions.assertEquals(backend1.findShortestPathVia("A", "D", "E"), Arrays.asList("A", "D", "B", "E"));
    // test that an empty list is outputted for a path that doesn't exist
    Assertions.assertEquals(backend1.findShortestPathVia("A", "F", "E"), Arrays.asList());

    // then test getTravelTimesOnPathVia with a new graph
    GraphADT<String, Double> graph2 = lectureGraph();
    BackendInterface backend2 = new BackendPlaceholder(graph2);
    // since no path was created in the graph object, ensure that the travel times are be empty
    Assertions.assertEquals(backend2.getTravelTimesOnPathVia("A", "C", "E"), Arrays.asList());
    // now create a path and check the travel times
    backend2.findShortestPathVia("A", "C", "E");
    Assertions.assertEquals(backend2.getTravelTimesOnPathVia("A", "C", "E"), Arrays.asList(1.0, 10.0));
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