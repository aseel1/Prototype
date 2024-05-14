package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private SimpleClient client;

    // @Override
    // public void start(Stage stage) throws IOException {
    // EventBus.getDefault().register(this);

    // //!here
    // client = SimpleClient.getClient();
    // client.openConnection();

    // if (client.isConnected()) {
    // System.out.println("Client is connected to the server.");
    // } else {
    // System.out.println("Client failed to connect to the server.");
    // }

    // scene = new Scene(loadFXML("Login"), 788, 603);
    // stage.setScene(scene);
    // stage.show();
    // }

    @Override
    public void start(Stage stage) throws IOException {
        EventBus.getDefault().register(this);

        scene = new Scene(loadFXML("Connect"), 788, 603);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
    public void stop() throws Exception {
        // TODO Auto-generated method stub
        EventBus.getDefault().unregister(this);
        super.stop();
    }

    @Subscribe
    public void onWarningEvent(WarningEvent event) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.WARNING,
                    String.format("Message: %s\nTimestamp: %s\n",
                            event.getWarning().getMessage(),
                            event.getWarning().getTime().toString()));
            alert.show();
        });

    }

    public static void main(String[] args) {
        launch();
    }

}
