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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 *
 * @author Mathew
 */
public class AddUserSceneController implements Initializable {

    @FXML
    private TextField username_fx;
    @FXML
    private PasswordField passwd_fx;
    @FXML
    private TextField name_fx;
    @FXML
    private TextField surname_fx;
    @FXML
    private ChoiceBox user_type_fx;

    @FXML
    private void handleAddOrder(ActionEvent event) throws IOException {
        String username = username_fx.getText();
        String passwd = passwd_fx.getText();
        String name = name_fx.getText();
        String surname = surname_fx.getText();
        int user_type = user_type_fx.getSelectionModel().getSelectedIndex();
        if (username.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter username!");
            alert.showAndWait();
        } else {
            if (passwd.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter password!");
                alert.showAndWait();
            } else {
                if (name.equals("")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Please enter name!");
                    alert.showAndWait();
                } else {
                    if (surname.equals("")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Please enter surname!");
                        alert.showAndWait();
                    } else {
                        if (user_type == -1) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Please choose user type!");
                            alert.showAndWait();
                        } else {
                            if (JavaFXService.serv.addUser(username, passwd, user_type, name, surname)) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Success");
                                alert.setHeaderText(null);
                                alert.setContentText("User added!");
                                alert.showAndWait();
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("SQL Error");
                                alert.setHeaderText(null);
                                alert.setContentText("Querry Error!");
                                alert.showAndWait();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        user_type_fx.getItems().add("Admin");
        user_type_fx.getItems().add("Employee");
        user_type_fx.getItems().add("Client");
    }

}
