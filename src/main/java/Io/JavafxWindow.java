package Io;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class JavafxWindow extends Application{

    public void runWindow(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Label queryPrompt = new Label("Enter wikipedia article name...");
        Label resultPrompt = new Label("Here are the results!");
        Button searchButton = new Button("Search");
        Button searchAgainButton = new Button("Search Again?");;


        TextField inputBox = new TextField();

        VBox queryVbox = new VBox(15, queryPrompt, inputBox, searchButton);
        queryVbox.setAlignment(Pos.CENTER);
        queryVbox.setPadding(new Insets(20));

        VBox resultsVbox = new VBox(15, resultPrompt, searchAgainButton);
        resultsVbox.setAlignment(Pos.CENTER);
        resultsVbox.setPadding(new Insets(20));

        Scene queryScene = new Scene(queryVbox, 400, 150);
        Scene resultsScene = new Scene(resultsVbox, 600, 800);

        searchButton.setOnAction(event ->{
            String input = inputBox.getText();
            stage.setScene(resultsScene);
        });

        searchAgainButton.setOnAction(event ->{
            stage.setScene(queryScene);
        });


        stage.setTitle("Wiki Editor History Search");
        stage.setScene(queryScene);
        stage.show();
    }
}

