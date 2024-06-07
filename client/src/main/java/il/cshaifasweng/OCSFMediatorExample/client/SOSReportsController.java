package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.SOS;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javafx.scene.chart.XYChart;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleClient.getCurrentUser;

public class SOSReportsController {
    private static SOSReportsController currentInstance;
    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox<String> communityComboBox;

    @FXML
    private BarChart<String, Number> sosHistogram;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Button LoadDataButton;

    private boolean isButtonClicked = false;

    public static void onServerResponse(Message message) {
    }

    @FXML
    public void handleloadDataButton(ActionEvent actionEvent) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        if(startDatePicker.getValue()==null || endDatePicker.getValue()==null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please select a date range.");
            alert.showAndWait();
            return;
        }
        LocalDate startDate = LocalDate.parse(startDatePicker.getValue().format(formatter));
        LocalDate endDate = LocalDate.parse(endDatePicker.getValue().format(formatter));
        if (startDate.isAfter(endDate) || startDate.isAfter(today) || endDate.isAfter(today)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Illegal input, please try again.");

            alert.showAndWait();
            startDatePicker.setValue(null);
            endDatePicker.setValue(null);

        }
        else {
            isButtonClicked=true;
            // Assuming "My Community" selection requires fetching the community of the current user
            String community = communityComboBox.getValue();
            if (community.equals("My Community")) {
                community = SimpleClient.getCurrentUser().getCommunityManager(); // Implement this method according to your application's structure
            } else if (community.equals("All Communities")) {
                community = "all"; // Use a special identifier for all communities
            }

            Message message = new Message("#showSOS", startDate + " " + endDate + " " + community);
            try {
                SimpleClient.getClient().sendToServer(message);
                System.out.println("(SOS) Sending message to server with dates: " + startDate + " to " + endDate + " and community: " + community);
            } catch (IOException e) {
                System.out.println("Failed to connect to the server.");
                e.printStackTrace();
            }
        }
    }
    public void initialize() {
        //register to eventBus:
        EventBusManager.getEventBus().register(this);
        currentInstance = this; // Update the current instance reference
        communityComboBox.getItems().addAll("All Communities", "My Community");
        communityComboBox.getSelectionModel().select("All Communities");
    }

    public static void updateHistogramFromMessage(Message message) {
        if (currentInstance != null && message != null) {
            // This runs the update on the JavaFX Application Thread
            Platform.runLater(() -> {
                // Assuming message.getObject() returns a List<SOS>, requires casting
                List<SOS> sosRecords = (List<SOS>) message.getObject();
                currentInstance.updateHistogram(sosRecords); // Call instance method to update the histogram
            });
        }
    }

    // Define methods to handle events posted on the EventBus
    @Subscribe
    public void onNewSosReport(NewSosReportEvent event) {
        System.out.println("(SosReportController) event received by "+getCurrentUser().getUserName());
       //check if manager clicked on the button
        if(isButtonClicked) {
            //as if I clicked again
            ActionEvent event1 = new ActionEvent();
            Platform.runLater(() -> {
                handleloadDataButton(event1);
            });
        }
    }

    // This method can be called with the SOS data to update the histogram
    public void updateHistogram(List<SOS> sosRecords) {
        // Clear existing data
        sosHistogram.getData().clear();

        // Process records to count SOS calls per day
        Map<String, Integer> sosCountByDay = new HashMap<>();
        for (SOS sos : sosRecords) {
            String day = sos.getDate(); // This should match the actual method name
            sosCountByDay.put(day, sosCountByDay.getOrDefault(day, 0) + 1);
        }

        // Create a series for the histogram
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : sosCountByDay.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Add series to histogram
        sosHistogram.getData().add(series);
    }

    @FXML
    private void switchToPrimary() throws IOException {
        //unsubscribe from eventBus:
        EventBusManager.getEventBus().unregister(this);
        App.setRoot("primary");
    }


    public void handlePressingSOS(javafx.event.ActionEvent actionEvent) {
        SimpleClient.pressingSOS("SOSReports");
    }
}