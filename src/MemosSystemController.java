import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static java.lang.Integer.parseInt;

public class MemosSystemController {
    private final int MAX_YEAR = parseInt(new Date().toString().split(" ")[5]);
    private final int  MIN_YEAR = 2000;
    private final int MAX_MONTH = 12;
    private final int MAX_DAY = 31;
    @FXML
    private Button fileBtn;
    @FXML
    private TextField fileNameField;
    @FXML
    private ComboBox<String> daysBox;
    @FXML
    private ComboBox<String> monthsBox;
    @FXML
    private ComboBox<String> yearsBox;

    private final ComboBox[] dateBoxes = new ComboBox[3];

    @FXML
    private Button getMemoBtn;

    @FXML
    private TextArea memoField;

    @FXML
    private Button saveBtn;

    @FXML
    private Label errorMsgLabel;

public void initialize(){
    ArrayList<String> days = new ArrayList<String>();
    ArrayList<String> months = new ArrayList<String>();
    ArrayList<String> years = new ArrayList<String>();

    setDateBox(months, MAX_MONTH);
    setDateBox(days, MAX_DAY);
    for (int i = 0; i <=MAX_YEAR-MIN_YEAR; i++){
        years.add(String.valueOf(MIN_YEAR +i));
    }
    setComboBox(days,daysBox);
    setComboBox(months, monthsBox);
    setComboBox(years, yearsBox);
    dateBoxes[0] = daysBox;
    dateBoxes[1] = monthsBox;
    dateBoxes[2] = yearsBox;
}

    private void setDateBox(ArrayList<String> vals, int maxVal) {
        for (int i = 1; i<= maxVal; i++){
            vals.add(i-1, "0" + i);
            if (i >= 10){
                vals.remove(i-1);
                vals.add(i-1,String.valueOf(i));
            }
        }
    }

    private void setComboBox(ArrayList<String> dateVals, ComboBox<String> box ) {
        ObservableList<String> data = FXCollections.observableArrayList(dateVals);
        box.setItems(data);
        box.getSelectionModel().selectFirst();
    }

    @FXML
    void fileBtnHandler(ActionEvent event) throws IOException {
    try {
        Memos memos = new Memos(fileNameField.getText());
    }
    catch (Exception e){
        errorMsgLabel.setText("Error: Invalid File Name");
        e.printStackTrace();
        }
    }
    @FXML
    void getMemoBtnHandler(ActionEvent event) {
    String day = (String) dateBoxes[0].getValue();
    String month = (String) dateBoxes[1].getValue();
    String year = (String) dateBoxes[2].getValue();
    CleanDate selectedDate = new CleanDate(day,month,year);
        System.out.println(selectedDate);
    }

    @FXML
    void saveBtnHandler(ActionEvent event) {

    }

}
