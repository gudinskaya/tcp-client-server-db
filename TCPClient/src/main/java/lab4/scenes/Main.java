package lab4.scenes;

import javafx.application.Application;
import javafx.stage.Stage;
import lab4.scenes.Connection;

public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception{
        Connection.renderIn(stage);
    }


    public static void main(String[] args) {
        launch(args);
        
    }
}