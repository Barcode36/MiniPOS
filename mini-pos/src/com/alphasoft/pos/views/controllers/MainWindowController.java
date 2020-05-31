package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.contexts.Logger;
import com.alphasoft.pos.views.customs.ConfirmBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML
    private HBox navigationBar;

    @FXML
    private StackPane contentPane;

    @FXML
    private Label username;

    public static Stage mainStage;


    @FXML
    public void requestView(MouseEvent event){
        var source = event.getSource();
        if(source instanceof VBox){
            VBox vBox = (VBox)source;
            loadView(vBox.getId());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username.setText(Logger.getLogger().getLoggedAccount().getName());
        Platform.runLater(()-> loadView("pos_home"));
    }

    private void loadView(String viewName){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(String.format("/com/alphasoft/pos/views/%s.fxml",viewName)));
            Parent view = fxmlLoader.load();
            loadView(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadView(Parent view){
        contentPane.getChildren().clear();
        contentPane.getChildren().add(view);
        navigationBar.getChildren().stream().filter(b->b.getId().equals(view.getId())).forEach(Node::requestFocus);
        navigationBar.getChildren().stream().filter(b->b instanceof VBox).forEach(b->{
                                                                                        b.getStyleClass().retainAll("navigation-icon-box");
                                                                                        if(b.getId().equals(view.getId())) b.getStyleClass().add("navigation-icon-box-active");
                                                                                    });
    }

    public void exit(){
        ConfirmBox confirmBox = new ConfirmBox(mainStage);
        confirmBox.setTitle("Confirm");
        confirmBox.setContentText("Are you sure to exit?");
        confirmBox.setOnConfirmed(e->mainStage.close());
        confirmBox.showAndWait();
    }

    public static void show(){
        try {
            Parent view = FXMLLoader.load(PosHomeController.class.getResource("/com/alphasoft/pos/views/main_window.fxml"));
            Stage stage = new Stage();
            mainStage = stage;
            stage.setScene(new Scene(view));
            stage.centerOnScreen();
//            stage.setFullScreen(true);
//            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
//            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
