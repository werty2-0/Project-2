// === CS400 File Header Information ===
// Name: kevin ren
// Email: kren34@wisc.edu
// Group and Team: N/A
// Group TA: N/A
// Lecturer: gary dahl
// Notes to Grader: <optional extra notes>

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number> extends BaseGraph<NodeType, EdgeType> implements GraphADT<NodeType, EdgeType> {

  /**
   * While searching for the shortest path between two nodes, a SearchNode
   * contains data about one specific path between the start node and another
   * node in the graph. The final node in this path is stored in its node
   * field. The total cost of this path is stored in its cost field. And the
   * predecessor SearchNode within this path is referened by the predecessor
   * field (this field is null within the SearchNode containing the starting
   * node in its node field).
   * <p>
   * SearchNodes are Comparable and are sorted by cost so that the lowest cost
   * SearchNode has the highest priority within a java.util.PriorityQueue.
   */
  protected class SearchNode implements Comparable<SearchNode> {
    public Node node;
    public double cost;
    public SearchNode predecessor;

    public SearchNode(Node node, double cost, SearchNode predecessor) {
      this.node = node;
      this.cost = cost;
      this.predecessor = predecessor;
    }

    public int compareTo(SearchNode other) {
      if (cost > other.cost) return +1;
      if (cost < other.cost) return -1;
      return 0;
    }
  }

  /**
   * Constructor that sets the map that the graph uses.
   */
  public DijkstraGraph() {
    super(new PlaceholderMap<>());
  }

  /**
   * This helper method creates a network of SearchNodes while computing the
   * shortest path between the provided start and end locations. The
   * SearchNode that is returned by this method is represents the end of the
   * shortest path that is found: it's cost is the cost of that shortest path,
   * and the nodes linked together through predecessor references represent
   * all of the nodes along that shortest path (ordered from end to start).
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return SearchNode for the final end node within the shortest path
   * @throws NoSuchElementException when no path from start to end is found
   *                                or when either start or end data do not
   *                                correspond to a graph node
   */
  protected SearchNode computeShortestPath(NodeType start, NodeType end) {
    if (!nodes.containsKey(start) || !nodes.containsKey(end)) {
      throw new NoSuchElementException("Start or end is not in graph");
    }
    System.out.println("starting djiikstra's algorithm...");
    // 1. mark all nodes as unvisited
    // the node data is the key, the SearchNode is the value, so when we want to check
    // if it is visited, we can do containsKey
    MapADT<NodeType, SearchNode> visited = new PlaceholderMap<>();
    PriorityQueue<SearchNode> pq = new PriorityQueue<>();
    // 2. assign to all nodes a tentative cost value
    // set the cost of the start node to 0, and all other nodes to infinity ??

    SearchNode startNode = new SearchNode(nodes.get(start), 0, null);
    pq.add(startNode);

    // 3. for the current node, calculate distance to all unvisited neighbors
    // if new distance is less than that node's shortest distance so far, update it
    // also set the neighbor node's previous node to current node
    while (!pq.isEmpty()) {
      SearchNode current = pq.poll(); // node with the cheapest cost
      if (current.node.data.equals(end)) {
        System.out.println("We are done\n");
        return current;
      } else if (visited.containsKey(current.node.data)) {
        continue;
      }
      for (Edge edge : current.node.edgesLeaving) {
        Node neighbor = edge.successor;
        // the data field of Edge is the cost
        double cost = edge.data.doubleValue() + current.cost; // cost to reach neighbor
        // compare cost to the cost it gets to get to neighbor
        SearchNode neighborNode = new SearchNode(neighbor, cost, current);
        if (!visited.containsKey(neighbor.data) || visited.get(neighbor.data).cost > cost) {
          pq.add(neighborNode);
          // update the cost of the neighbor node, if applicable
          if (visited.containsKey(neighbor.data)) {
            // update the cost inside visited as well
            visited.put(neighbor.data, neighborNode);
          }
        }
      }
      // 4. mark current node as visited
      visited.put(current.node.data, current);
    }
    System.out.println("no path found :(");
    throw new NoSuchElementException("No path found");

    // go back to step 3 and update distances accordingly. make sure to update the
    // cost as (cost to reach neighbor node) + (cost to reach current node) = total cost
    // for the neighbor node
    // and if we have no unvisited neighbors, we are done

    // to get the path from starting node to node N, find N in the table and keep looking at
    // predecessors until predecessor is null

  }

  private String string(PriorityQueue<SearchNode> pq) {
    StringBuilder sb = new StringBuilder();
    for (SearchNode node : pq) {
      sb.append(node.node.data + " ");
    }
    return sb.toString();
  }

  /**
   * Returns the list of data values from nodes along the shortest path
   * from the node with the provided start value through the node with the
   * provided end value. This list of data values starts with the start
   * value, ends with the end value, and contains intermediary values in the
   * order they are encountered while traversing this shorteset path. This
   * method uses Dijkstra's shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return list of data item from node along this shortest path
   */
  public List<NodeType> shortestPathData(NodeType start, NodeType end) {
    // implement in step 5.4
    if (!nodes.containsKey(start) || !nodes.containsKey(end)) {
      throw new NoSuchElementException("Start or end node does not exist");
    }

    List<NodeType> path = new LinkedList<>();

    SearchNode current;
    try {
      current = computeShortestPath(start, end);
    } catch (NoSuchElementException e) {
      throw new NoSuchElementException("No path found");
    }

    while (current != null) {
      path.add(0, current.node.data);
      current = current.predecessor;
    }

    return path;
  }

  /**
   * Returns the cost of the path (sum over edge weights) of the shortest
   * path freom the node containing the start data to the node containing the
   * end data. This method uses Dijkstra's shortest path algorithm to find
   * this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return the cost of the shortest path between these nodes
   */
  public double shortestPathCost(NodeType start, NodeType end) {
    if (!nodes.containsKey(start) || !nodes.containsKey(end)) {
      throw new NoSuchElementException("Start or end node does not exist");
    }

    SearchNode endNode;
    try {
      endNode = computeShortestPath(start, end);
    } catch (NoSuchElementException e) {
      throw new NoSuchElementException("No path found");
    }

    return endNode.cost;
  }

  /**
   * Tests the shortest path method by creating a graph and checking the
   * shortest path between two nodes.
   */
  @Test
  public void testShortestPath() {
    DijkstraGraph<String, Integer> graph = createGraph();

    Assertions.assertEquals("[A, D, B, E]", graph.shortestPathData("A", "E").toString());
    Assertions.assertEquals(7, graph.shortestPathCost("A", "E"));
  }

  /**
   * Tests the cost and sequence of the shortest path method by creating a
   * graph and calling the shortestPathCost and shortestPathData methods.
   */
  @Test
  public void testCostAndSequence() {
    DijkstraGraph<String, Integer> graph = createGraph();
    // make sure the shortest path from C to D is C -> A -> D
    double cost = graph.shortestPathCost("C", "D");
    Assertions.assertEquals(5, cost, "incorrect cost");

    List<String> path = graph.shortestPathData("C", "D");
    String[] expected = {"C", "A", "D"};
    for (int i = 0; i < expected.length; i++) {
      // make sure the path is correct
      Assertions.assertEquals(expected[i], path.get(i));
    }
  }

  /**
   * Tests the case where there is no path between two nodes.
   */
  @Test
  public void testNoPath() {
    DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
    // make the graph directed, so we can test for no path
    graph.insertNode("A");
    graph.insertNode("B");
    graph.insertNode("C");
    graph.insertNode("D");
    graph.insertNode("E");
    graph.insertEdge("A", "C", 1);
    graph.insertEdge("A", "B", 15);
    graph.insertEdge("B", "E", 1);
    graph.insertEdge("C", "E", 10);
    graph.insertEdge("D", "B", 2);
    graph.insertEdge("D", "A", 4);
    graph.insertEdge("D", "E", 10);

    // there is no path from C to D, so computeShortestPath should throw an exception
    try {
      SearchNode node = this.computeShortestPath((NodeType) "C", (NodeType) "D");
      Assertions.fail("no exception thrown");
    } catch (NoSuchElementException e) {
      // make sure the exception is thrown
      Assertions.assertTrue(true);
    }
  }

  private DijkstraGraph<String, Integer> createGraph() {
    DijkstraGraph<String, Integer> graph = new DijkstraGraph<>();
    graph.insertNode("A");
    graph.insertNode("B");
    graph.insertNode("C");
    graph.insertNode("D");
    graph.insertNode("E");
    // now add the edges with the corresponding weights
    // make sure they go in both directions, so the graph is undirected
    graph.insertEdge("A", "C", 1);
    graph.insertEdge("C", "A", 1);

    graph.insertEdge("B", "E", 1);
    graph.insertEdge("E", "B", 1);

    graph.insertEdge("B", "D", 2);
    graph.insertEdge("D", "B", 2);

    graph.insertEdge("A", "D", 4);
    graph.insertEdge("D", "A", 4);

    graph.insertEdge("C", "E", 10);
    graph.insertEdge("E", "C", 10);

    graph.insertEdge("D", "E", 10);
    graph.insertEdge("E", "D", 10);

    graph.insertEdge("A", "B", 15);
    graph.insertEdge("B", "A", 15);

    return graph;
  }

}
