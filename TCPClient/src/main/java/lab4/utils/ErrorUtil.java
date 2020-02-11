package lab4.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ErrorUtil {
    public static String getStack(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    public static void catchAndShow(Throwable throwable) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Initialization failed");
        alert.setHeaderText(null);
        alert.setContentText("Stacktrace:\n\n" + getStack(throwable));

        alert.showAndWait();
    }

    public static void show(Throwable throwable) {
        System.out.println(getStack(throwable));
        show(throwable.getMessage());
    }

    public static void show(String message) {
        Alert alert = new Alert(AlertType.ERROR);

        alert.setTitle("Action failed");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
