import javafx.scene.control.TextField;
import javafx.application.Application;
import javafx.stage.Window;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import java.util.List;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import javafx.scene.text.Font;

/**
 * This class provides frontend for an application that provides the shortest path between any two buildings on the UW-Madison campus
 *
 * @author Parin Gouraram
 */
public class Frontend extends Application implements FrontendInterface {

  private static BackendInterface backend; // maintain a reference to the backend for backend methods

  private boolean findPath; // boolean to know whether or not we are currently finding a path

  private boolean showTimes; // boolean to know whether or not we need to display the times between each location in path

  private boolean showThirdLocation; // boolean to know whether or not we need to put in the via location within the path

  private List<String> path = new ArrayList<>(); // list of locations in the path

  private List<Double> timesPath = new ArrayList<>(); // list of times between locations in the path

  private GridPane pathControls; // gridPane which organizes the controls on the screen

  private String viaLocation; // location used to add into path when needed

  private VBox pathResult; // VBox to organize path results

  private boolean showAbout; // boolean used to know when the about section should be showed or not

  /**
   * This method sets the backend reference to the backend the user entered
   *
   * @param backend - backend to set the frontend's refrence to
   */
  public static void setBackend(BackendInterface backend) {
    Frontend.backend = backend;
    try {
      backend.loadGraphData("campus.dot");
    } catch (Exception e) {
      System.out.println("Error loading graph data");
    }
  }

  /**
   * Creates all controls in the GUI.
   *
   * @param parent the parent pane that contains all controls
   */
  @Override
  public void createAllControls(Pane parent) {

    createShortestPathControls(parent);
    createPathListDisplay(parent);
    createAboutAndQuitControls(parent);
    createAdditionalFeatureControls(parent);
  }

  /**
   * This method sets up all the controls on the screen and displays it
   *
   * @param stage -  window for javafx application
   */
  public void start(Stage stage) {

    // create borderpane for all labels, buttons, etc to go into
    BorderPane root = new BorderPane();
    root.setPadding(new Insets(20));

    // create gridpane to organize controls
    pathControls = new GridPane();
    pathControls.setAlignment(Pos.TOP_LEFT);
    pathControls.setHgap(5);
    pathControls.setVgap(10);
    root.setCenter(pathControls);

    // create vbox to display path results
    pathResult = new VBox();
    GridPane.setConstraints(pathResult, 0, 3, 3, 1);
    pathResult.setId("pathResult");
    pathControls.getChildren().add(pathResult);

    // create all controls for the frontend
    createAllControls(root);

    // display the frontend
    Scene scene = new Scene(root, 800, 600);
    stage.setScene(scene);
    stage.setTitle("P2");
    stage.show();
  }


  /**
   * Creates the controls for the shortest path search.
   *
   * @param parent the parent pane that contains all controls
   */
  @Override
  public void createShortestPathControls(Pane parent) {

    // create start label
    Label start = new Label("Start At:");
    GridPane.setConstraints(start, 0, 0);
    start.setId("start");
    pathControls.getChildren().add(start);

    // create start text field
    TextField startEnter = new TextField();
    GridPane.setConstraints(startEnter, 1, 0);
    startEnter.setId("start_text");
    pathControls.getChildren().add(startEnter);

    // create end label
    Label end = new Label("End At:");
    GridPane.setConstraints(end, 0, 1);
    end.setId("end");
    pathControls.getChildren().add(end);

    // create end text field
    TextField endEnter = new TextField();
    GridPane.setConstraints(endEnter, 1, 1);
    endEnter.setId("end_text");
    pathControls.getChildren().add(endEnter);

    // create button to find path
    Button find = new Button("Find Path");
    GridPane.setConstraints(find, 0, 2);
    find.setId("find");
    find.setOnAction(e -> {
      // find paths depending on if via location and times are selected
      if (!showThirdLocation) {
        this.path = backend.findShortestPath(startEnter.getText(), endEnter.getText());
        if (showTimes) this.timesPath = backend.getTravelTimesOnPath(startEnter.getText(), endEnter.getText());
      } else {
        this.path = backend.findShortestPathVia(startEnter.getText(), this.viaLocation, endEnter.getText());
        if (showTimes)
          this.timesPath = backend.getTravelTimesOnPathVia(startEnter.getText(), this.viaLocation, endEnter.getText());
      }

      // set findPath to true since we are currently find the shorest path
      this.findPath = true;

      // display path results to screen
      createPathListDisplay(parent);
    });

    pathControls.getChildren().add(find);
  }

  /**
   * Creates the controls for displaying the shortest path returned by the search.
   *
   * @param the parent pane that contains all controls
   */
  @Override
  public void createPathListDisplay(Pane parent) {

    // clear old results and create new title of results depending on if displaying times
    pathResult.getChildren().clear();
    if (showTimes) {
      Label title = new Label("Path Result (With Times):");
      pathResult.getChildren().add(title);
    } else {
      Label title = new Label("Path Result:");
      pathResult.getChildren().add(title);
    }

    // handle case where no path was found between start and end
    if (this.path.size() == 0 && this.findPath) {
      Label noPath = new Label("No Path Found");
      this.pathResult.getChildren().add(noPath);
    }

    // display results from path and times array to screen
    else {
      // case where times is not displayed
      if (!showTimes) {
        for (int i = 0; i < this.path.size(); i++) {
          Label nextLocation = new Label(this.path.get(i));
          pathResult.getChildren().add(nextLocation);
        }
      }
      // case where times is displayed
      else {
        for (int i = 0; i < this.path.size(); i++) {
          Label nextLocation = new Label(this.path.get(i));
          if (i < this.path.size() - 1)
            nextLocation.setText(nextLocation.getText() + " -> " + this.timesPath.get(i) + " secs");
          pathResult.getChildren().add(nextLocation);
        }
        // display total times of path at end
        Label totalTime = new Label();
        double time = 0;
        for (Double sec : this.timesPath) time += sec;
        totalTime.setText("Total Time: " + time + " secs");
        pathResult.getChildren().add(totalTime);
      }
    }

    // set findPath to false since we are no longer displaying the path
    this.findPath = false;
  }

  /**
   * Creates controls for the two features in addition to the shortest path search.
   *
   * @param parent parent pane that contains all controls
   */
  @Override
  public void createAdditionalFeatureControls(Pane parent) {

    createTravelTimesBox(parent);
    createOptionalLocationControls(parent);
  }

  /**
   * Creates the check box to add travel times in the result display.
   *
   * @param parent parent pane that contains all controls
   */
  @Override
  public void createTravelTimesBox(Pane parent) {

    // set checkbox to show times from each location on path
    CheckBox travelTimesBox = new CheckBox("Show Walking Times");
    GridPane.setConstraints(travelTimesBox, 1, 2);
    travelTimesBox.setId("travelTimesBox");
    travelTimesBox.setOnAction(e -> showTimes = !showTimes);
    pathControls.getChildren().add(travelTimesBox);


  }

  /**
   * Creates controls to allow users to add a third location for the path to go through.
   *
   * @param parent parent pane that contains all controls
   */
  @Override
  public void createOptionalLocationControls(Pane parent) {

    // set up label to direct user to enter a via location into path
    Label locationSelector = new Label("Via Location (optional):");
    GridPane.setConstraints(locationSelector, 2, 0);
    locationSelector.setId("viaLocation");
    pathControls.getChildren().add(locationSelector);

    // set up textfield where user can enter a via location into path
    TextField viaLocation = new TextField();
    GridPane.setConstraints(viaLocation, 3, 0);
    viaLocation.setId("viaLocation_text");
    pathControls.getChildren().add(viaLocation);


    // set up a checkbox to allow user to use their via location in path
    CheckBox useVia = new CheckBox("Use Above Location in Path");
    GridPane.setConstraints(useVia, 2, 1);
    useVia.setId("useVia");
    useVia.setOnAction(e -> {
      showThirdLocation = !showThirdLocation;
      // make sure the via location is correct depending on what is in the via location textfield and if the checkbox is selected
      if (showThirdLocation) this.viaLocation = viaLocation.getText();
      else this.viaLocation = "";
    });
    pathControls.getChildren().add(useVia);
  }

  /**
   * Creates an about and quit button.
   *
   * @param parent parent pane that contains all controls
   */
  @Override
  public void createAboutAndQuitControls(Pane parent) {

    // cast parent to borderpane to use borderpane's library
    BorderPane betterParent = (BorderPane) parent;

    // create Hbox to organize the about and quit button
    HBox appControls = new HBox();
    betterParent.setAlignment(appControls, Pos.BOTTOM_LEFT);
    betterParent.setBottom(appControls);
    appControls.setSpacing(5);

    // create label for the about information
    Label aboutSection = new Label();
    String aboutText = "This app allows you to find the shortest path between any two buildings on the UW-Madison campus.\nClicking on the 'show times' button will allow you to show the time to walk between the locations in the path and the total time it takes to walk the entire path.\nLastly, you can add a location that the start and end  have to go through using the 'via location' area. Hit about again to make this disappear.";
    aboutSection.setId("aboutSection");

    // create quit button
    Button quit = new Button("Quit");
    quit.setOnAction(e -> Platform.exit());
    quit.setId("quitButton");
    appControls.getChildren().add(quit);

    // create about button
    Button about = new Button("About");
    about.setId("aboutButton");
    about.setOnAction(e -> {
      // only display about information if showAbout is true and not display it when aboutSection is false
      showAbout = !showAbout;
      if (showAbout) {
        aboutSection.setText(aboutText);
      } else aboutSection.setText("");
    });
    appControls.getChildren().add(about);
    // change fontsize of the about information and add it the screen
    aboutSection.setFont(new Font("Arial", 8));
    appControls.getChildren().add(aboutSection);
  }
    
}
