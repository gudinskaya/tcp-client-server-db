package lab4.scenes;

import lab4.utils.ErrorUtil;
import lab4.cqrs.State;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Connection implements Initializable {
    @FXML
    public TextField connAddrField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public static String connAddr;

    @FXML
    public void onConnect(ActionEvent event) {
        try {
           State.INSTANCE.SetConnectionAddress(connAddrField.getText());
           connAddr = connAddrField.getText();
        } catch (Throwable e) {
            ErrorUtil.show(e);
            return;
        }

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        TablesController.renderIn(stage);
    }

    public static void renderIn(Stage stage) {
        Parent root;
        try {
            root = FXMLLoader.load(Connection.class.getResource("/fxml/ConnectionScene.fxml"));
        } catch (Throwable e) {
            ErrorUtil.catchAndShow(e);
            return;
        }

        Scene scene = new Scene(root);

        stage.setTitle("Lab 4 :: Connect");
        stage.setScene(scene);

        try {
            stage.show();
        } catch (IllegalStateException e) {
            ErrorUtil.catchAndShow(e);
        }
    }
}
