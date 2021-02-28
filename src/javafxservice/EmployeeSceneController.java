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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import static javafxservice.ClientSceneController.exit_flag;

/**
 *
 * @author Mathew
 */
public class EmployeeSceneController implements Initializable {

    @FXML
    private TableView waiting_orders_table_fx;
    @FXML
    private TableView taken_orders_table_fx;
    @FXML
    private TableColumn<Map, String> waiting_orders_id_fx;
    @FXML
    private TableColumn<Map, String> waiting_orders_device_fx;
    @FXML
    private TableColumn<Map, String> waiting_orders_placed_fx;
    @FXML
    private TableColumn<Map, String> waiting_orders_state_fx;
    @FXML
    private TableColumn<Map, String> waiting_orders_description_fx;
    @FXML
    private TableColumn<Map, String> taken_orders_id_fx;
    @FXML
    private TableColumn<Map, String> taken_orders_device_fx;
    @FXML
    private TableColumn<Map, String> taken_orders_placed_fx;
    @FXML
    private TableColumn<Map, String> taken_orders_description_fx;
    @FXML
    private SplitPane split_pane_fx;

    ObservableList<Map<String, Object>> waiting_orders_items = FXCollections.<Map<String, Object>>observableArrayList();
    ObservableList<Map<String, Object>> taken_orders_items = FXCollections.<Map<String, Object>>observableArrayList();

    @FXML
    private void handleTakeOrder(ActionEvent event) throws IOException {
        ObservableList<Map<String, Object>> selectedItems = waiting_orders_table_fx.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select order to take!");
            alert.showAndWait();
        } else {
            Map<String, Object> selectedItem = selectedItems.get(0);
            int order_id = (Integer) selectedItem.get("id");

            if (JavaFXService.serv.assignOrder(JavaFXService.serv.curr_user.id, order_id)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Order taken!");
                alert.showAndWait();
                waiting_orders_table_fx.getSelectionModel().clearSelection();
                taken_orders_table_fx.getSelectionModel().clearSelection();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("SQL Error");
                alert.setHeaderText(null);
                alert.setContentText("Querry Error!");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void handleMarkAsFinished(ActionEvent event) throws IOException {
        ObservableList<Map<String, Object>> selectedItems = taken_orders_table_fx.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select order to mark as finished!");
            alert.showAndWait();
        } else {
            Map<String, Object> selectedItem = selectedItems.get(0);
            int order_id = (Integer) selectedItem.get("id");

            if (JavaFXService.serv.markAsFinished(order_id)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Order marked as finished!");
                alert.showAndWait();
                taken_orders_table_fx.getSelectionModel().clearSelection();
                waiting_orders_table_fx.getSelectionModel().clearSelection();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("SQL Error");
                alert.setHeaderText(null);
                alert.setContentText("Querry Error!");
                alert.showAndWait();
            }
        }
    }
    static public int exit_flag = 0;
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
                    ObservableList<Map<String, Object>> selectedItemsW = waiting_orders_table_fx.getSelectionModel().getSelectedItems();
                    if (selectedItemsW.isEmpty()) {
                        ArrayList<ArrayList<String>> query_waiting_orders_result = JavaFXService.serv.showOrders();
                        waiting_orders_items.clear();
                        for (ArrayList<String> u : query_waiting_orders_result) {
                            Map<String, Object> item = new HashMap<>();
                            item.put("id", Integer.parseInt(u.get(0)));
                            item.put("device", u.get(1));
                            item.put("placed", u.get(2));
                            item.put("state", u.get(3));
                            item.put("description", u.get(4));
                            waiting_orders_items.add(item);
                        }
                        waiting_orders_table_fx.setItems(waiting_orders_items);
                    }
                    ObservableList<Map<String, Object>> selectedItemsT = taken_orders_table_fx.getSelectionModel().getSelectedItems();
                    if (selectedItemsT.isEmpty()) {
                        ArrayList<ArrayList<String>> query_taken_orders_result = JavaFXService.serv.takenOrders();
                        taken_orders_items.clear();
                        for (ArrayList<String> u : query_taken_orders_result) {
                            Map<String, Object> item = new HashMap<>();
                            item.put("id", Integer.parseInt(u.get(0)));
                            item.put("device", u.get(1));
                            item.put("placed", u.get(2));
                            item.put("description", u.get(3));
                            taken_orders_items.add(item);
                        }
                        taken_orders_table_fx.setItems(taken_orders_items);
                    }
                });

            }

            return null;
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        VBox.setVgrow(waiting_orders_table_fx, Priority.ALWAYS);
        VBox.setVgrow(taken_orders_table_fx, Priority.ALWAYS);
        VBox.setVgrow(split_pane_fx, Priority.ALWAYS);
        waiting_orders_table_fx.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        taken_orders_table_fx.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        waiting_orders_id_fx.setCellValueFactory(new MapValueFactory<>("id"));
        waiting_orders_device_fx.setCellValueFactory(new MapValueFactory<>("device"));
        waiting_orders_placed_fx.setCellValueFactory(new MapValueFactory<>("placed"));
        waiting_orders_state_fx.setCellValueFactory(new MapValueFactory<>("state"));
        waiting_orders_description_fx.setCellValueFactory(new MapValueFactory<>("description"));

        taken_orders_id_fx.setCellValueFactory(new MapValueFactory<>("id"));
        taken_orders_device_fx.setCellValueFactory(new MapValueFactory<>("device"));
        taken_orders_placed_fx.setCellValueFactory(new MapValueFactory<>("placed"));
        taken_orders_description_fx.setCellValueFactory(new MapValueFactory<>("description"));

        ArrayList<ArrayList<String>> query_waiting_orders_result = JavaFXService.serv.showOrders();
        ArrayList<ArrayList<String>> query_taken_orders_result = JavaFXService.serv.takenOrders();

        for (ArrayList<String> u : query_waiting_orders_result) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", Integer.parseInt(u.get(0)));
            item.put("device", u.get(1));
            item.put("placed", u.get(2));
            item.put("state", u.get(3));
            item.put("description", u.get(4));
            waiting_orders_items.add(item);
        }

        for (ArrayList<String> u : query_taken_orders_result) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", Integer.parseInt(u.get(0)));
            item.put("device", u.get(1));
            item.put("placed", u.get(2));
            item.put("description", u.get(3));
            taken_orders_items.add(item);
        }

        waiting_orders_table_fx.setItems(waiting_orders_items);
        taken_orders_table_fx.setItems(taken_orders_items);
        
        new Thread(background_task).start();
        waiting_orders_table_fx.sceneProperty().addListener((obs, oldScene, newScene) -> {
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
