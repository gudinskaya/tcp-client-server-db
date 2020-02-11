package lab4.scenes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.simple.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import lab4.cqrs.State;
import lab4.cqrs.datatypes.MilitaryClient;
import lab4.cqrs.datatypes.MilitaryOrder;
import lab4.utils.Constants;
import lab4.utils.ErrorUtil;

public class TablesController {

    private ObservableList<MilitaryClient> militaryClients = FXCollections.observableArrayList();
    private ObservableList<MilitaryOrder> militaryOrders = FXCollections.observableArrayList();
    private Socket sock = State.INSTANCE.GetCurrentSocket();

    @FXML
    private TableView<MilitaryClient> clientsTable;

    @FXML
    private TableColumn<MilitaryClient, String> ClientId;

    @FXML
    private TableColumn<MilitaryClient, String> TIN;

    @FXML
    private TableColumn<MilitaryClient, String> Country;

    @FXML
    private TableView<MilitaryOrder> ordersTable;

    @FXML
    private TableColumn<MilitaryOrder, String> orderId;

    @FXML
    private TableColumn<MilitaryOrder, String> type;

    @FXML
    private TableColumn<MilitaryOrder, String> subtype;

    @FXML
    private TableColumn<MilitaryOrder, String> marking;

    @FXML
    private TableColumn<MilitaryOrder, String> client_id;

    @FXML
    private TextField tinField;

    @FXML
    private TextField countryField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField subtypeField;

    @FXML
    private TextField markingField;

    @FXML
    private TextField clientIdField;

    @FXML
    public void onClientDelete(ActionEvent event) {
        deleteClient();
    }

    public void deleteClient() {

        MilitaryClient selectedItem = clientsTable.getSelectionModel().getSelectedItem();
        String id = selectedItem.getId();
        System.out.println(">>>>>>>>>" + id);
        System.out.println(">>>>>>>>>" + selectedItem);
        clientsTable.getItems().remove(selectedItem);
        MilitaryOrder selectedItem2 = ordersTable.getSelectionModel().getSelectedItem();
        String id2 = selectedItem.getId();
        System.out.println(">>>>>>>>>" + id2);
        System.out.println(">>>>>>>>>" + selectedItem2);
        ordersTable.getItems().remove(selectedItem2);
        clientsTable.refresh();
        HashMap<String, String> updateData = new HashMap<String, String>();
        updateData.put("type", Constants.CLIENTS_DELETE);
        updateData.put("id", id);
        byte[] resp = JSONObject.toJSONString(updateData).getBytes();
        sendRequest(resp, sock);
        getResponse(sock);
    
        try {
            State.INSTANCE.SetConnectionAddress(Connection.connAddr);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        sock = State.INSTANCE.GetCurrentSocket();
    }
    @FXML
    public void deleteOrder() {

        MilitaryOrder selectedItem = ordersTable.getSelectionModel().getSelectedItem();
        String id = selectedItem.getId();
        System.out.println(">>>>>>>>>" + id);
        System.out.println(">>>>>>>>>" + selectedItem);
        ordersTable.getItems().remove(selectedItem);
        ordersTable.refresh();
        HashMap<String, String> updateData = new HashMap<String, String>();
        updateData.put("type", Constants.ORDERS_DELETE);
        updateData.put("id", id);
        byte[] resp = JSONObject.toJSONString(updateData).getBytes();
        sendRequest(resp, sock);
        getResponse(sock);
        try {
            State.INSTANCE.SetConnectionAddress(Connection.connAddr);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        sock = State.INSTANCE.GetCurrentSocket();
    }
    @FXML
    public void goBack(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Connection.renderIn(stage);
    }

    public void initialize() {
        init();
        clientsTable.setEditable(true);
        ClientId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TIN.setCellValueFactory(new PropertyValueFactory<>("TIN"));
        TIN.setCellFactory(TextFieldTableCell.forTableColumn());
        TIN.setOnEditCommit(event -> {
            String TIN = event.getNewValue();
            event.getRowValue().setTIN(TIN);
            System.out.println(TIN);
            try {

                updateClient(event.getRowValue().getId(), "TIN", TIN);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        Country.setCellValueFactory(new PropertyValueFactory<>("Country"));
        Country.setCellFactory(TextFieldTableCell.forTableColumn());
        Country.setOnEditCommit(event -> {
            String Country = event.getNewValue();
            event.getRowValue().setTIN(Country);
            System.out.println(Country);
            try {
                updateClient(event.getRowValue().getId(), "Country", Country);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        ordersTable.setEditable(true);
        orderId.setCellValueFactory(new PropertyValueFactory<>("id"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        type.setCellFactory(TextFieldTableCell.forTableColumn());
        type.setOnEditCommit(event -> {
            String type = event.getNewValue();
            event.getRowValue().setType(type);
            System.out.println(type);
            try {

                updateOrder(event.getRowValue().getId(), "type", type);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        subtype.setCellValueFactory(new PropertyValueFactory<>("subtype"));
        subtype.setCellFactory(TextFieldTableCell.forTableColumn());
        subtype.setOnEditCommit(event -> {
            String subtype = event.getNewValue();
            event.getRowValue().setSubtype(subtype);
            System.out.println(subtype);
            try {

                updateOrder(event.getRowValue().getId(), "subtype", subtype);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        marking.setCellValueFactory(new PropertyValueFactory<>("marking"));
        marking.setCellFactory(TextFieldTableCell.forTableColumn());
        marking.setOnEditCommit(event -> {
            String marking = event.getNewValue();
            event.getRowValue().setMarking(marking);
            System.out.println(marking);
            try {

                updateOrder(event.getRowValue().getId(), "marking", marking);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        client_id.setCellValueFactory(new PropertyValueFactory<>("client_id"));
        client_id.setCellFactory(TextFieldTableCell.forTableColumn());
        client_id.setOnEditCommit(event -> {
            String client_id = event.getNewValue();
            event.getRowValue().setClient_id(client_id);
            System.out.println(client_id);
            try {

                updateOrder(event.getRowValue().getId(), "client_id", client_id);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    @FXML
    public void addOrder(ActionEvent event) throws UnsupportedEncodingException { // button

        String type = typeField.getText();
        String subtype = subtypeField.getText();
        String marking = markingField.getText();
        String client_id = clientIdField.getText();

        // System.out.println("TIN=" + TIN + "Country" + country);
        HashMap<String, String> updateData = new HashMap<String, String>();
        updateData.put("type", Constants.ORDERS_ADD);
        updateData.put("myType", type);
        updateData.put("subtype", subtype);
        updateData.put("marking", marking);
        updateData.put("client_id", client_id);

        byte[] resp = JSONObject.toJSONString(updateData).getBytes();
        sendRequest(resp, sock);
        getResponse(sock);
        
        // try {
        //     State.INSTANCE.SetConnectionAddress(Connection.connAddr);
        // } catch (IOException | URISyntaxException e) {
        //     e.printStackTrace();
        // }
        // sock = State.INSTANCE.GetCurrentSocket();
    }

    @FXML
    public void addClient(ActionEvent event) throws UnsupportedEncodingException { // button
        String TIN = tinField.getText();

        String country = countryField.getText();
        System.out.println("TIN=" + TIN + "Country" + country);
        HashMap<String, String> updateData = new HashMap<String, String>();
        updateData.put("type", Constants.CLIENTS_ADD);
        updateData.put("TIN", TIN);
        updateData.put("country", country);

        byte[] resp = JSONObject.toJSONString(updateData).getBytes();
        sendRequest(resp, sock);
        getResponse(sock);
    
        // try {
        //     State.INSTANCE.SetConnectionAddress(Connection.connAddr);
        // } catch (IOException | URISyntaxException e) {
        //     e.printStackTrace();
        // }
        // sock = State.INSTANCE.GetCurrentSocket();
    }

    public void init() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, List<HashMap<String, String>>> data = mapper.readValue(getResponse(sock), Map.class);
            List<HashMap<String, String>> clients = data.get("clients");
            for (int i = 0; i < clients.size(); i++) {
                HashMap<String, String> client = clients.get(i);
                militaryClients.add(new MilitaryClient(client.get("id"), client.get("TIN"), client.get("country")));
            }

            List<HashMap<String, String>> orders = data.get("orders");
            for (int i = 0; i < orders.size(); i++) {
                HashMap<String, String> order = orders.get(i);
                militaryOrders.add(new MilitaryOrder(order.get("id"), order.get("type"), order.get("subtype"),
                        order.get("marking"), order.get("client_id")));
            }
            System.out.println(data.toString());

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientsTable.setItems(militaryClients);
        ordersTable.setItems(militaryOrders);
    }

    private void updateClient(String id, String column, String value) throws IOException {
        HashMap<String, String> updateData = new HashMap<String, String>();
        updateData.put("id", id);
        updateData.put("table", "clients");
        updateData.put("column", column);
        updateData.put("value", value);
        updateData.put("type", Constants.CHANGE);
        byte[] resp = JSONObject.toJSONString(updateData).getBytes();
        sendRequest(resp, sock);
        getResponse(sock);
        try {
            State.INSTANCE.SetConnectionAddress(Connection.connAddr);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        sock = State.INSTANCE.GetCurrentSocket();
    }

    private void updateOrder(String id, String column, String value) throws IOException {
        HashMap<String, String> updateData = new HashMap<String, String>();
        updateData.put("id", id);
        updateData.put("table", "orders");
        updateData.put("column", column);
        updateData.put("value", value);
        updateData.put("type", Constants.CHANGE);
        byte[] resp = JSONObject.toJSONString(updateData).getBytes();
        sendRequest(resp, sock);
        getResponse(sock);
        try {
            State.INSTANCE.SetConnectionAddress(Connection.connAddr);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        sock = State.INSTANCE.GetCurrentSocket();
    }

    public static void renderIn(Stage stage) {
        Parent root;
        try {
            root = FXMLLoader.load(Connection.class.getResource("/fxml/Tables.fxml"));
        } catch (Throwable e) {
            ErrorUtil.catchAndShow(e);
            return;
        }

        Scene scene = new Scene(root);

        stage.setTitle("Lab 4 :: " + State.INSTANCE.FormatCurrentConnAddr());
        stage.setScene(scene);

        try {
            stage.show();
        } catch (IllegalStateException e) {
            ErrorUtil.catchAndShow(e);
        }
    }

    private InputStream in;
    private OutputStream out;

    private void sendRequest(byte[] request, Socket sock) {

        try {
            out = sock.getOutputStream();
        } catch (IOException e) {

            ErrorUtil.show(e);
            if (sock != null && !sock.isClosed()) {
                try {
                    sock.close();
                } catch (Throwable z) {
                    // things got terribly wrong, just go back.
                }
            }
            System.out.println("sock is null or closed, when it tried to create InputStream and OutputStream");
            // goBack(event);
            return;
        }
        try {
            // sock.send(request);
            out.write(request);
        } catch (IOException e) {
            ErrorUtil.show(e);
            return;
        }
    }

    private byte[] getResponse(Socket sock) {
        try {
            in = sock.getInputStream();
        } catch (IOException e) {

            ErrorUtil.show(e);
            if (sock != null && !sock.isClosed()) {
                try {
                    sock.close();
                } catch (Throwable z) {
                    // things got terribly wrong, just go back.
                }
            }
            System.out.println("sock is null or closed, when it tried to create InputStream and OutputStream");
            // goBack(event);
        }
        int sz = 1024;
        try {
            sz = sock.getReceiveBufferSize();
        } catch (IOException e) {
            ErrorUtil.show(e);
            if (sock != null && !sock.isClosed()) {
                try {
                    sock.close();
                } catch (Throwable z) {
                    // things got terribly wrong, just go back.
                }
            }
            // goBack(event);
            System.out.println("sock is null or closed, while getting \"receive buffer size\"");
            // return;
        }

        byte[] rcvBuff = new byte[sz];

        try {
            in.read(rcvBuff);
        } catch (IOException e) {
            ErrorUtil.show(e);

            if (!sock.isClosed()) {
                try {
                    sock.close();
                } catch (Throwable z) {
                    // things got terribly wrong, just go back.
                }
            }
            // goBack(event);
            System.out.println("sock is null or closed, while receiving the response");
            // return;
        }
        return rcvBuff;
    }

}
