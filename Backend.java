import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Backend for the Campus Map project.
 */
public class Backend implements BackendInterface {

  private GraphADT<String, Double> graph;
  private List<String> locations;

  /**
   * Constructor for the Backend class.
   *
   * @param graph object to store the backend's graph data
   */
  public Backend(GraphADT<String, Double> graph) { // graph can be empty or filled
    this.graph = graph;
    // list of locations will be filled when reading in graph data
    locations = new ArrayList<>();
    System.out.println("graph and locations initialized");
  }

  /**
   * Loads graph data from a dot file.
   *
   * @param filename the path to a dot file to read graph data from
   * @throws IOException if there was a problem reading in the specified file
   */
  public void loadGraphData(String filename) throws IOException {
    // try to read the file
    FileReader fr = new FileReader(filename);
    BufferedReader br = new BufferedReader(fr);
    // regular expression to match a string, then an arrow, then another string, then a number
    // assuming the file is formatted correctly
    Pattern pattern = Pattern.compile("\"([^\"]+)\"\\s*->\\s*\"([^\"]+)\"\\s*\\[seconds=([0-9.]+)\\];");
    String line = br.readLine();
    while (line != null) {
      Matcher matcher = pattern.matcher(line);
      if (matcher.find()) { // assuming one match per line
        String from = matcher.group(1);
        String to = matcher.group(2);
        double duration = Double.parseDouble(matcher.group(3));

        graph.insertNode(from);
        graph.insertNode(to);
        graph.insertEdge(from, to, duration);
        // also store the from and to locations in the list of locations
        if (!locations.contains(from)) locations.add(from);
        if (!locations.contains(to)) locations.add(to);
      }
      line = br.readLine();  // read the next line at the end of the loop
    }
    System.out.println("finished reading file " + filename + " with " + locations.size() + " locations");
    br.close();
  }

  /**
   * Gets a list of all locations in the graph.
   *
   * @return a list of all locations in the graph
   */
  public List<String> getListOfAllLocations() {
    return locations;
  }

  /**
   * Finds the shortest path between two locations.
   *
   * @param startLocation the start location of the path
   * @param endLocation   the end location of the path
   * @return a list of locations representing the shortest path between the start and end locations
   */
  public List<String> findShortestPath(String startLocation, String endLocation) {
    // if either location is not in the list of locations, return an empty list
    if (!locations.contains(startLocation) || !locations.contains(endLocation)) {
      return Arrays.asList();
    }
    return graph.shortestPathData(startLocation, endLocation);
  }

  /**
   * Gets the travel times between locations on a path.
   *
   * @param startLocation the start location of the path
   * @param endLocation   the end location of the path
   * @return a list of travel times between locations on the path
   */
  public List<Double> getTravelTimesOnPath(String startLocation, String endLocation) {
    if (!locations.contains(startLocation) || !locations.contains(endLocation)) {
      return Arrays.asList();
    }
    List<String> path = findShortestPath(startLocation, endLocation);
    List<Double> travelTimes = new ArrayList<>();
    // for each edge in the path, get the weight of the edge
    for (int i = 0; i < path.size() - 1; i++) {
      try {
        travelTimes.add(graph.getEdge(path.get(i), path.get(i + 1)));
      } catch (Exception e) {
        System.out.println("Error getting edge weight:\n" + e);
      }
    }
    return travelTimes;
  }

  /**
   * Finds the shortest path between two locations that passes through a third location.
   *
   * @param startLocation the start location of the path
   * @param via           a location that the path show lead through
   * @param endLocation   the end location of the path
   * @return a list of locations representing the shortest path between the start and end locations
   */
  public List<String> findShortestPathVia(String startLocation, String via, String endLocation) {
    if (!locations.contains(startLocation) || !locations.contains(via) || !locations.contains(endLocation)) {
      return Arrays.asList();
    }
    List<String> path1 = graph.shortestPathData(startLocation, via);
    List<String> path2 = graph.shortestPathData(via, endLocation);
    System.out.println("path1: " + path1);
    System.out.println("path2: " + path2);
    // put them together into one path
    // make sure to NOT add first location in path2 because that would be a duplicate
    path1.addAll(path2.subList(1, path2.size()));
    return path1;
  }

  /**
   * Gets the travel times between locations on a path that passes through a third location.
   *
   * @param startLocation the start location of the path
   * @param via           a location that the path show lead through
   * @param endLocation   the end location of the path
   * @return a list of travel times between locations on the path
   */
  public List<Double> getTravelTimesOnPathVia(String startLocation, String via, String endLocation) {
    if (!locations.contains(startLocation) || !locations.contains(via) || !locations.contains(endLocation)) {
      return Arrays.asList();
    }
    List<String> path = findShortestPathVia(startLocation, via, endLocation);
    List<Double> travelTimes = new ArrayList<>();
    for (int i = 0; i < path.size() - 1; i++) {
      try {
        travelTimes.add(graph.getEdge(path.get(i), path.get(i + 1)));
      } catch (Exception e) {
        System.out.println("Error getting edge weight:\n" + e);
      }
    }
    return travelTimes;
  }

}
