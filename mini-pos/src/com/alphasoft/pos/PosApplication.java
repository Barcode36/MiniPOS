package com.alphasoft.pos;

import com.alphasoft.pos.contexts.ConnectionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class PosApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/com/alphasoft/pos/views/login_form.fxml"))));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        Connection connection = ConnectionManager.getConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }
}