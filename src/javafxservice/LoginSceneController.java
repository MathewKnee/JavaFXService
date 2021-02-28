/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxservice;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Mathew
 */
public class LoginSceneController implements Initializable {

    @FXML
    private TextField username_fx;
    @FXML
    private PasswordField passwd_fx;

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = username_fx.getText();
        String passwd = passwd_fx.getText();
        int type = JavaFXService.serv.login(username, passwd);
        Stage stage = new Stage();
        boolean logged_in = false;
        switch (type) {
            case 0: {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminScene.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Serwis");
                stage.show();
                logged_in = true;
                break;
            }
            case 1: {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeeScene.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Serwis");
                stage.show();
                logged_in = true;
                break;
            }
            case 2: {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientScene.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Serwis");
                stage.show();
                logged_in = true;
                break;
            }
            case -1: {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Error");
                alert.setHeaderText(null);
                alert.setContentText("Wrong username or password, try again!");
                alert.showAndWait();
                break;
            }
            case -2: {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("SQL Error");
                alert.setHeaderText(null);
                alert.setContentText("Querry Error!");
                alert.showAndWait();
                break;
            }
        }
        if (logged_in) {
            Stage curr_stage = (Stage) passwd_fx.getScene().getWindow();
            curr_stage.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
