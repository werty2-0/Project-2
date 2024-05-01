// === CS400 Spring 2024 File Header Information ===
// Name: parin gouraram
// Email: pgouraram@wisc.edu
// Lecturer: gary dahl
// Notes to Grader: n/a

import java.util.PriorityQueue;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph. The final node in this path is stored in its node
     * field. The total cost of this path is stored in its cost field. And the
     * predecessor SearchNode within this path is referened by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     *
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
            if (cost > other.cost)
                return +1;
            if (cost < other.cost)
                return -1;
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

	// check if either the start or end nodes are not in the graph 
	if(!this.containsNode(start) || !this.containsNode(end)) throw new NoSuchElementException();

	// initalize priority queue to find shortest path every iteration
	PriorityQueue<SearchNode> pq = new PriorityQueue<>();
	SearchNode root = new SearchNode(new Node(start), 0, null);
	pq.add(root);

	// initialize visited map to store nodes that we have found the shortest paths to
	MapADT<NodeType, SearchNode> visited = new PlaceholderMap<>();

	// initialize distances which stores temporary distances from start to the specified node. Used to store and update distance information between nodes when we find shorter paths
	MapADT<NodeType, SearchNode> distances = new PlaceholderMap<>();
	distances.put(root.node.data, root);

	//keep going until priority queue is empty or until we have found the shortest path to our end node
	while(!pq.isEmpty() && !visited.containsKey(end)){

	    // remove a node from our priority queue and add it to visited since we know it is the shortest possible path
	    SearchNode prevNode = pq.poll();
	    visited.put(prevNode.node.data, prevNode);


	    // go through all of the visited node's edges and add any new nodes we find to the pq or update the shortest paths to be shorter if we found a shorter path to another node
	    for(Edge edge : this.nodes.get(prevNode.node.data).edgesLeaving){


	        // if we already found the shorter path to the successor of this edge, we can just continue
	        if(visited.containsKey(edge.successor.data)) continue;

                // calculate new cost of path using this edge and create a SearchNode with it
	        double alt = prevNode.cost + edge.data.doubleValue();
	        SearchNode neighbor = new SearchNode(edge.successor, alt, prevNode);

	        // if we found a new node, add it to our distances map and add it to the pq for further exploration
	        if(!distances.containsKey(neighbor.node.data)) {

		    distances.put(neighbor.node.data, neighbor);
		    pq.add(distances.get(neighbor.node.data));
		}

		// if we didnt found a new node but found a better path to a node we already found, update the pq and distances map with the updated cost
		else if(distances.get(neighbor.node.data).cost > neighbor.cost) {

		    pq.remove(distances.get(neighbor.node.data));
		    pq.add(neighbor);
		    distances.remove(neighbor.node.data);
	            distances.put(neighbor.node.data, neighbor);
		}
	    }
	}

	// if we never visit the end node, then our nodes are not linked in any way so we can throw an exception
	if(!visited.containsKey(end)) throw new NoSuchElementException();

	// returns SearchNode for the final end node within the shortest path
        return visited.get(end);
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
        
	// compute shortest path and get searchNode of the end node
	SearchNode path = this.computeShortestPath(start, end);

	// create linked list which will store data of shortest path
        List<NodeType> result = new LinkedList<>();

	// keep adding nodes in path in reverse order to the linked list to record the nodes of the shortest path in the correct order
	while(path != null){
	    result.add(0, path.node.data);
	    path = path.predecessor;
	}
	
	// return shortest path data
	return result;
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
        
	// compute shortest path and get searchNode of the end node
	SearchNode path = this.computeShortestPath(start, end);

	// returns cost of searchnode of end node found above
        return path.cost;
    }

    // TODO: implement 3+ tests in step 4.1


    /**
     * This testor method tests the dijkstra's shortest path implementation with a directed graph that was traced through in lecture.
     * It tests the sequence and cost of the shortest path between nodes 2 and 4. Expected cost of shortest path should be 6 and expected sequence should be '[2, 5, 4]'.
     *
     */
    @Test
    public void test1(){

	// initalize graph and add nodes
	DijkstraGraph<Integer, Integer> map = new DijkstraGraph<>();
	map.insertNode(1);
	map.insertNode(2);
	map.insertNode(3);
	map.insertNode(4);
	map.insertNode(5);

	// create edges between nodes
	map.insertEdge(1, 4, 4);
	map.insertEdge(3, 1, 1);
	map.insertEdge(5, 3, 3);       
	map.insertEdge(5, 4, 2);
	map.insertEdge(4, 2, 5);
	map.insertEdge(2, 1, 4);
	map.insertEdge(2, 3, 3);
	map.insertEdge(2, 5, 4);

	// make sure cost of shortest path is right
	Assertions.assertEquals(6, map.shortestPathCost(2,4));

	// make sure sequence of shortest path is right
	Assertions.assertEquals("[2, 5, 4]", map.shortestPathData(2, 4).toString());
    }
    
    /**
     * This testor rmethod tests the dijkstra's shortest path implementation with a directed graph that was traced through lecture and is the same graph from test1. 
     * It tests the sequence and cost of the shortest path between nodes 5 and 1. Expected cost of shortest path should be 4 and expected sequence should be '[5, 3, 1]'.
     *
     */
    @Test
    public void test2(){

	// initalize graph and add nodes
	DijkstraGraph<Integer, Integer> map = new DijkstraGraph<>();
        map.insertNode(1);
        map.insertNode(2);
        map.insertNode(3);
        map.insertNode(4);
        map.insertNode(5);

	// create edges between nodes
        map.insertEdge(1, 4, 4);
        map.insertEdge(3, 1, 1);
        map.insertEdge(5, 3, 3);
        map.insertEdge(5, 4, 2);
        map.insertEdge(4, 2, 5);
        map.insertEdge(2, 1, 4);
        map.insertEdge(2, 3, 3);
        map.insertEdge(2, 5, 4);

	// make sure cost of shortest path is right
	Assertions.assertEquals(4, map.shortestPathCost(5, 1));

	// make sure sequence of shortest path is right
	Assertions.assertEquals("[5, 3, 1]", map.shortestPathData(5, 1).toString());
    }

    /**
     * This testor method tests the dijkstra's shortest path implementation in its ability to return a no such element exception when asked to find the shortest path between two nodes that are not linked by any edges.
     *
     */
    @Test
    public void test3(){

	// initalize graph and add nodes
	DijkstraGraph<Integer, Integer> map = new DijkstraGraph<>();
        map.insertNode(1);
        map.insertNode(2);
        map.insertNode(3);

	// create edges between nodes
	map.insertEdge(1,2,5);

	// make sure computing a shortest path between two nodes that are not linked by edges throws an exception
	Assertions.assertThrows(NoSuchElementException.class, () -> map.shortestPathCost(1, 3));
	Assertions.assertThrows(NoSuchElementException.class, () -> map.shortestPathData(1, 3));
    }

    /**
     * This testor method tests the Dijkstra's shortest path implementation in its ability to return the shortest path in a undirected graph traced through in lecture from node 1 to node 5. 
     * Expected cost of shortest path should be 7 and the sequence should be '[1, 4, 3, 5]'.
     */
    @Test
    public void test4(){

	// initalize graph and add nodes
        DijkstraGraph<Integer, Integer> map = new DijkstraGraph<>();
        map.insertNode(1);
        map.insertNode(2);
        map.insertNode(3);
        map.insertNode(4);
        map.insertNode(5);

        // create edges between nodes
        map.insertEdge(1, 2, 1);
        map.insertEdge(2, 1, 1);
        map.insertEdge(1, 3, 15);
        map.insertEdge(3, 1, 15);
        map.insertEdge(1, 4, 4);
        map.insertEdge(4, 1, 4);
        map.insertEdge(2, 5, 10);
        map.insertEdge(5, 2, 10);
	map.insertEdge(4, 3, 2);
        map.insertEdge(3, 4, 2);
        map.insertEdge(3, 5, 1);
        map.insertEdge(5, 3, 1);
        map.insertEdge(4, 5, 10);
        map.insertEdge(5, 4, 10);

	// make sure cost of shortest path is right
	Assertions.assertEquals(7, map.shortestPathCost(1, 5));

	// make sure sequence of shortest path is right 
        Assertions.assertEquals("[1, 4, 3, 5]", map.shortestPathData(1, 5).toString());	

    }

    /**
     * This testor method tests the Dijkstra's shortest path implementation in its ability to throw a no such element exception when a path cannot be found between the start node and the end node.
     *
     */
    @Test
    public void test5(){
	
	// initalize graph and add nodes
	DijkstraGraph<Integer, Integer> map = new DijkstraGraph<>();
	map.insertNode(1);
        map.insertNode(2);
        map.insertNode(3);
        map.insertNode(4);

	// create edges between nodes
	map.insertEdge(1, 2, 1);
        map.insertEdge(2, 3, 5);
        map.insertEdge(4, 3, 15);
        map.insertEdge(4, 1, 8);

	// make sure computing a shortest path between two nodes that are not linked by edges throws an exception
	Assertions.assertThrows(NoSuchElementException.class, () -> map.shortestPathCost(1, 4));
	Assertions.assertThrows(NoSuchElementException.class, () -> map.shortestPathData(1, 4));
    }

    /**
     * This testor method tests the Dijkstra's shortest path implementation in its ability to throw a no such element exception when the end node cannot be found within the graph.
     *
     */
    @Test
    public void test6(){
	// initalize graph and add nodes
        DijkstraGraph<Integer, Integer> map = new DijkstraGraph<>();
        map.insertNode(1);
        map.insertNode(2);
        map.insertNode(3);

        // create edges between nodes
        map.insertEdge(1, 2, 1);
        map.insertEdge(2, 3, 5);
        

	// make sure computing a shortest path between two nodes in which the end node is not in the graph throws an exception
        Assertions.assertThrows(NoSuchElementException.class, () -> map.shortestPathCost(1, 4));
        Assertions.assertThrows(NoSuchElementException.class, () -> map.shortestPathData(1, 4));



    }

}
