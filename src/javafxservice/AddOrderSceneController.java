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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author Mathew
 */
public class AddOrderSceneController implements Initializable {

    @FXML
    private TextField device_fx;
    @FXML
    private TextArea description_fx;

    @FXML
    private void handlePlaceOrder(ActionEvent event) throws IOException {
        String device = device_fx.getText();
        String description = description_fx.getText();
        if (device.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter device name!");
            alert.showAndWait();
        } else {
            if (description.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter description!");
                alert.showAndWait();
            } else {
                if (JavaFXService.serv.addOrder(device, JavaFXService.serv.curr_user.id, 0, description)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Order added!");
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
