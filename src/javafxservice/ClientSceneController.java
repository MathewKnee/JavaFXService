/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxservice;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Mathew
 */
public class ClientSceneController implements Initializable {

    static public int exit_flag = 0;
    @FXML
    private TableView orders_table_fx;
    @FXML
    private TableColumn<Map, String> orders_device_fx;
    @FXML
    private TableColumn<Map, String> orders_placed_fx;
    @FXML
    private TableColumn<Map, String> orders_state_fx;
    @FXML
    private TableColumn<Map, String> orders_description_fx;
    @FXML
    private TextField device_fx;
    @FXML
    private TextArea description_fx;

    ObservableList<Map<String, Object>> orders_items = FXCollections.<Map<String, Object>>observableArrayList();

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
    Task background_task = new Task<Void>() {
        @Override
        public Void call() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                if (exit_flag == 1) {
                    break;
                }
                Platform.runLater(() -> {
                    orders_items.clear();
                    ArrayList<ArrayList<String>> query_order_result = JavaFXService.serv.showOrders();
                    for (ArrayList<String> u : query_order_result) {
                        Map<String, Object> item = new HashMap<>();
                        item.put("device", u.get(0));
                        item.put("placed", u.get(1));
                        item.put("state", u.get(2));
                        item.put("description", u.get(3));
                        orders_items.add(item);
                    }
                    orders_table_fx.setItems(orders_items);
                });

            }

            return null;
        }
    };


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        VBox.setVgrow(orders_table_fx, Priority.ALWAYS);

        orders_device_fx.setCellValueFactory(new MapValueFactory<>("device"));
        orders_placed_fx.setCellValueFactory(new MapValueFactory<>("placed"));
        orders_state_fx.setCellValueFactory(new MapValueFactory<>("state"));
        orders_description_fx.setCellValueFactory(new MapValueFactory<>("description"));

        ArrayList<ArrayList<String>> query_order_result = JavaFXService.serv.showOrders();

        for (ArrayList<String> u : query_order_result) {
            Map<String, Object> item = new HashMap<>();
            item.put("device", u.get(0));
            item.put("placed", u.get(1));
            item.put("state", u.get(2));
            item.put("description", u.get(3));
            orders_items.add(item);
        }
        orders_table_fx.setItems(orders_items);
        new Thread(background_task).start();
        orders_table_fx.sceneProperty().addListener((obs, oldScene, newScene) -> {
            Platform.runLater(() -> {
                Stage stage = (Stage) newScene.getWindow();
                stage.setOnCloseRequest(e -> {
                    exit_flag = 1;
                    Platform.exit();
                    System.exit(0);
                });
            });
        });
    }

}
