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

public class FrontendDeveloperTests extends ApplicationTest{

	@BeforeEach
    	public void setup() throws Exception {
	ApplicationTest.launch(App.class);
    	}


	/**
	 * Tests that results for the shortest path appear when two valids points are inputed when the submit button is pressed.
	 *
	 */
	@Test
	public void test1(){
		ClickOn("#start").write("Memorial Union");
		ClickOn("#end").write("Computer Science");

		Label path = lookup("#path").query();
		Assertions.assertEquals("Result List:", path.getText());
		ClickOn("#find");
		Assertions.assertEquals("Results List: \n\tMemorial Union\n\tSciene Hall\n\tPyschology\n\tComputer Science", path.getText());

	

	}


	/**
	 * Tests that no results appear when an invalid point is inputted when the submit button is pressed
	 *
	 */
	@Test
        public void test2(){

                Label path = lookup("#path").query();
                Assertions.assertEquals("Result List:", path.getText());
                ClickOn("#find");
                Assertions.assertEquals("Results List: \n\tNo Path Found", path.getText());


        }

	/**
	 * Tests that results for the shortest path appear with times when two valid points are inputed  when the travel time box is checked and when the submit button is pressed
	 *
	 */
	@Test
        public void test3(){
		ClickOn("#start").write("Memorial Union");
                ClickOn("#end").write("Computer Science");
	

                Label path = lookup("#path").query();
                Assertions.assertEquals("Result List:", path.getText());
		ClickOn("#travelTimesBox"); 
                ClickOn("#find");
                Assertions.assertEquals("Results List: \n\tMemorial Union\n\tSciene Hall\n\tPyschology\n\tComputer Science\n\nResults List (with travel times):\n\tMemorial Union\n\t-(30sec)->Science Hall\n\t-(170sec)->Psychology\n\t-(45sec)->Computer Science\n\tTotal time: 4.08min", path.getText());



        }

	/**
	 * Tests that results for the shortest path appear when a two valid points are inputed when the optional third mid location is checked and the submit button is pressed
	 *
	 */
	@Test
        public void test4(){
		ClickOn("#start").write("Memorial Union");
                ClickOn("#end").write("Computer Science");
		ClickOn("#locationSelector").write("Engineering Hall");
                Label path = lookup("#path").query();
                Assertions.assertEquals("Result List:", path.getText());
                ClickOn("#useVia"); 
                ClickOn("#find");
                Assertions.assertEquals("Results List with optional lcoation: \n\tMemorial Union\n\tSciene Hall\n\tEngineering Hall\n\tPyschology\n\tComputer Science", path.getText());

	}

}
