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
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Mathew
 */
public class AdminSceneController implements Initializable {

    static public int exit_flag = 0;
    @FXML
    private TableView users_table_fx;
    @FXML
    private TableView orders_table_fx;
    @FXML
    private TableColumn<Map, String> users_id_fx;
    @FXML
    private TableColumn<Map, String> users_username_fx;
    @FXML
    private TableColumn<Map, String> users_passwd_fx;
    @FXML
    private TableColumn<Map, String> users_type_fx;
    @FXML
    private TableColumn<Map, String> users_name_fx;
    @FXML
    private TableColumn<Map, String> users_surname_fx;
    @FXML
    private TableColumn<Map, String> orders_id_fx;
    @FXML
    private TableColumn<Map, String> orders_device_fx;
    @FXML
    private TableColumn<Map, String> orders_client_id_fx;
    @FXML
    private TableColumn<Map, String> orders_placed_fx;
    @FXML
    private TableColumn<Map, String> orders_state_fx;
    @FXML
    private TableColumn<Map, String> orders_description_fx;
    @FXML
    private ChoiceBox year_fx;
    @FXML
    private BarChart<String, Number> orders_by_month_fx;
    @FXML
    private BarChart<String, Number> user_distribution_fx;
    @FXML
    private CategoryAxis monthAxis_fx;
    @FXML
    private CategoryAxis userAxis_fx;
    @FXML
    private NumberAxis orderAxis_fx;
    @FXML
    private NumberAxis nr_of_userAxis_fx;
    @FXML
    private Label performance_ratio_fx;
    @FXML
    private TabPane main_tab_pane_fx;
    @FXML
    private TabPane chart_tab_pane_fx;

    ObservableList<Map<String, Object>> users_items = FXCollections.<Map<String, Object>>observableArrayList();
    ObservableList<Map<String, Object>> orders_items = FXCollections.<Map<String, Object>>observableArrayList();

    int order_tab_viemode = 0;

    @FXML
    private void handleAddUser(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddUserScene.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Add User");
        stage.show();
        users_table_fx.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleRemoveUser(ActionEvent event) throws IOException {
        ObservableList<Map<String, Object>> selectedItems = users_table_fx.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select user to remove!");
            alert.showAndWait();
        } else {
            Map<String, Object> selectedItem = selectedItems.get(0);
            int user_id = (Integer) selectedItem.get("id");
            if (user_id == JavaFXService.serv.curr_user.id) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("You can't remove yourself!");
                alert.showAndWait();
            } else {
                if (JavaFXService.serv.removeUser(user_id)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Removed user!");
                    alert.showAndWait();
                    users_table_fx.getSelectionModel().clearSelection();
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

    

    @FXML
    private void handlePlaceOrder(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddOrderScene.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Add Order");
        stage.show();
        orders_table_fx.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleRemoveOrder(ActionEvent event) throws IOException {
        ObservableList<Map<String, Object>> selectedItems = orders_table_fx.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select order to remove!");
            alert.showAndWait();
        } else {
            Map<String, Object> selectedItem = selectedItems.get(0);
            int order_id = (Integer) selectedItem.get("id");

            if (JavaFXService.serv.removeOrder(order_id)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Removed order!");
                alert.showAndWait();
                orders_table_fx.getSelectionModel().clearSelection();
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
    private void handleShowDelayedOrders(ActionEvent event) throws IOException {
        orders_items.clear();
        ArrayList<ArrayList<String>> query_order_result = JavaFXService.serv.getDelayedOrders();
        for (ArrayList<String> u : query_order_result) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", Integer.parseInt(u.get(0)));
            item.put("device", u.get(1));
            item.put("client_id", u.get(2));
            item.put("placed", u.get(3));
            item.put("state", u.get(4));
            item.put("description", u.get(5));
            orders_items.add(item);
        }
        orders_table_fx.setItems(orders_items);
        order_tab_viemode = 1;
    }

    @FXML
    private void handleAssignOrder(ActionEvent event) throws IOException {
        ObservableList<Map<String, Object>> selectedItems = orders_table_fx.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select order to assign!");
            alert.showAndWait();
        } else {
            Map<String, Object> selectedItem = selectedItems.get(0);
            int order_id = (Integer) selectedItem.get("id");
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Order Assignemnt");
            dialog.setHeaderText("Assigning order of id: " + order_id);
            dialog.setContentText("Please enter employee ID:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                int user_id = Integer.parseInt(result.get());
                if (JavaFXService.serv.employeeExists(user_id)) {
                    if (JavaFXService.serv.assignOrder(user_id, order_id)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText(null);
                        alert.setContentText("Order assigned!");
                        alert.showAndWait();
                        orders_table_fx.getSelectionModel().clearSelection();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("SQL Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Querry Error!");
                        alert.showAndWait();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Employee with ID = " + user_id + " does not exist!");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter employee ID!");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void handleShowAllOrders(ActionEvent event) throws IOException {
        orders_items.clear();
        ArrayList<ArrayList<String>> query_order_result = JavaFXService.serv.showOrders();
        for (ArrayList<String> u : query_order_result) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", Integer.parseInt(u.get(0)));
            item.put("device", u.get(1));
            item.put("client_id", u.get(2));
            item.put("placed", u.get(3));
            item.put("state", u.get(4));
            item.put("description", u.get(5));
            orders_items.add(item);
        }
        orders_table_fx.setItems(orders_items);
        order_tab_viemode = 0;
    }

    @FXML
    private void handleYearChange(ActionEvent event) throws IOException {
        orders_by_month_fx.getData().clear();
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        HashMap<String, Integer> entries = JavaFXService.serv.getOrdersByMonth((Integer) year_fx.getValue());
        XYChart.Series series = new XYChart.Series();
        series.setName("Number of orders");
        for (String ele : months) {
            series.getData().add(new XYChart.Data(ele, entries.get(ele)));
        }
        orders_by_month_fx.getData().add(series);
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
                    int tab_index = main_tab_pane_fx.getSelectionModel().getSelectedIndex();
                    if (tab_index == 0) {
                        ObservableList<Map<String, Object>> selectedItems = users_table_fx.getSelectionModel().getSelectedItems();
                        if (selectedItems.isEmpty()) {
                            users_items.clear();
                            ArrayList<ArrayList<String>> query_user_result = JavaFXService.serv.showUsers();
                            for (ArrayList<String> u : query_user_result) {
                                Map<String, Object> item = new HashMap<>();
                                item.put("id", Integer.parseInt(u.get(0)));
                                item.put("username", u.get(1));
                                item.put("passwd", u.get(2));
                                item.put("type", u.get(3));
                                item.put("name", u.get(4));
                                item.put("surname", u.get(5));
                                users_items.add(item);
                            }
                            users_table_fx.setItems(users_items);
                        }
                    } else if (tab_index == 1) {
                        ObservableList<Map<String, Object>> selectedItems = orders_table_fx.getSelectionModel().getSelectedItems();
                        if (order_tab_viemode == 0) {
                            if (selectedItems.isEmpty()) {
                                orders_items.clear();
                                ArrayList<ArrayList<String>> query_order_result = JavaFXService.serv.showOrders();
                                for (ArrayList<String> u : query_order_result) {
                                    Map<String, Object> item = new HashMap<>();
                                    item.put("id", Integer.parseInt(u.get(0)));
                                    item.put("device", u.get(1));
                                    item.put("client_id", u.get(2));
                                    item.put("placed", u.get(3));
                                    item.put("state", u.get(4));
                                    item.put("description", u.get(5));
                                    orders_items.add(item);
                                }
                                orders_table_fx.setItems(orders_items);
                            }
                        } else {
                            if (selectedItems.isEmpty()) {
                                orders_items.clear();
                                ArrayList<ArrayList<String>> query_order_result = JavaFXService.serv.getDelayedOrders();
                                for (ArrayList<String> u : query_order_result) {
                                    Map<String, Object> item = new HashMap<>();
                                    item.put("id", Integer.parseInt(u.get(0)));
                                    item.put("device", u.get(1));
                                    item.put("client_id", u.get(2));
                                    item.put("placed", u.get(3));
                                    item.put("state", u.get(4));
                                    item.put("description", u.get(5));
                                    orders_items.add(item);
                                }
                                orders_table_fx.setItems(orders_items);
                            }
                        }
                    } else if (tab_index == 2) {
                        int chart_tab_index = chart_tab_pane_fx.getSelectionModel().getSelectedIndex();
                        if (chart_tab_index == 0) {
                            orders_by_month_fx.getData().clear();
                            String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                            HashMap<String, Integer> entries = JavaFXService.serv.getOrdersByMonth((Integer) year_fx.getValue());
                            XYChart.Series series = new XYChart.Series();
                            series.setName("Number of orders");
                            for (String ele : months) {
                                series.getData().add(new XYChart.Data(ele, entries.get(ele)));
                            }
                            orders_by_month_fx.getData().add(series);
                        } else if (chart_tab_index == 1) {
                            double performance_ratio = JavaFXService.serv.showPerformanceRate();
                            performance_ratio_fx.setText("Performance ratio (number of orders divided by number of employees): " + performance_ratio);
                            user_distribution_fx.getData().clear();
                            String[] users = {"Admin", "Employee", "Client"};
                            HashMap<String, Integer> users_entries = JavaFXService.serv.getUserDist();
                            XYChart.Series series = new XYChart.Series();
                            series.setName("Number of users");
                            for (String ele : users) {
                                series.getData().add(new XYChart.Data(ele, users_entries.get(ele)));
                            }
                            user_distribution_fx.getData().add(series);
                        }
                    }

                });

            }

            return null;
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        VBox.setVgrow(users_table_fx, Priority.ALWAYS);
        VBox.setVgrow(orders_table_fx, Priority.ALWAYS);
        VBox.setVgrow(orders_by_month_fx, Priority.ALWAYS);
        VBox.setVgrow(user_distribution_fx, Priority.ALWAYS);

        users_table_fx.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        orders_table_fx.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        users_id_fx.setCellValueFactory(new MapValueFactory<>("id"));
        users_username_fx.setCellValueFactory(new MapValueFactory<>("username"));
        users_passwd_fx.setCellValueFactory(new MapValueFactory<>("passwd"));
        users_type_fx.setCellValueFactory(new MapValueFactory<>("type"));
        users_name_fx.setCellValueFactory(new MapValueFactory<>("name"));
        users_surname_fx.setCellValueFactory(new MapValueFactory<>("surname"));

        orders_id_fx.setCellValueFactory(new MapValueFactory<>("id"));
        orders_device_fx.setCellValueFactory(new MapValueFactory<>("device"));
        orders_client_id_fx.setCellValueFactory(new MapValueFactory<>("client_id"));
        orders_placed_fx.setCellValueFactory(new MapValueFactory<>("placed"));
        orders_state_fx.setCellValueFactory(new MapValueFactory<>("state"));
        orders_description_fx.setCellValueFactory(new MapValueFactory<>("description"));

        ArrayList<ArrayList<String>> query_user_result = JavaFXService.serv.showUsers();
        ArrayList<ArrayList<String>> query_order_result = JavaFXService.serv.showOrders();

        for (ArrayList<String> u : query_user_result) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", Integer.parseInt(u.get(0)));
            item.put("username", u.get(1));
            item.put("passwd", u.get(2));
            item.put("type", u.get(3));
            item.put("name", u.get(4));
            item.put("surname", u.get(5));
            users_items.add(item);
        }

        for (ArrayList<String> u : query_order_result) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", Integer.parseInt(u.get(0)));
            item.put("device", u.get(1));
            item.put("client_id", u.get(2));
            item.put("placed", u.get(3));
            item.put("state", u.get(4));
            item.put("description", u.get(5));
            orders_items.add(item);
        }

        users_table_fx.setItems(users_items);
        orders_table_fx.setItems(orders_items);

        double performance_ratio = JavaFXService.serv.showPerformanceRate();
        performance_ratio_fx.setText("Performance ratio (number of orders divided by number of employees): " + performance_ratio);

        ArrayList<Integer> years = JavaFXService.serv.getYears();
        if (!years.isEmpty()) {
            for (Integer ele : years) {
                year_fx.getItems().add(ele);
            }
            year_fx.setValue(years.get(0));
            orders_by_month_fx.getData().clear();
            String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            HashMap<String, Integer> entries = JavaFXService.serv.getOrdersByMonth((Integer) year_fx.getValue());
            XYChart.Series series = new XYChart.Series();
            series.setName("Number of orders");
            for (String ele : months) {
                series.getData().add(new XYChart.Data(ele, entries.get(ele)));
            }
            orders_by_month_fx.getData().add(series);
        }
        String[] users = {"Admin", "Employee", "Client"};
        HashMap<String, Integer> users_entries = JavaFXService.serv.getUserDist();
        XYChart.Series series = new XYChart.Series();
        series.setName("Number of users");
        for (String ele : users) {
            series.getData().add(new XYChart.Data(ele, users_entries.get(ele)));
        }
        user_distribution_fx.getData().add(series);

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
