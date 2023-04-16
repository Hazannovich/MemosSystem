import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import static java.lang.Integer.parseInt;

public class MemosSystemController {
    private final int MAX_YEAR = parseInt(new Date().toString().split(" ")[5]);
    private final int MIN_YEAR = 2000;
    private final int MAX_MONTH = 12;
    private final int MAX_DAY = 31;
    Memos memos = null;
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
    @FXML
    private Button searchMemoBtn;
    @FXML
    private TextArea memoField;
    @FXML
    private Button saveBtn;
    @FXML
    private Label errorMsgLabel;

    public void initialize() {
        ArrayList<String> days = new ArrayList<String>();
        ArrayList<String> months = new ArrayList<String>();
        ArrayList<String> years = new ArrayList<String>();

        setDateBox(months, MAX_MONTH);
        setDateBox(days, MAX_DAY);
        for (int i = 0; i <= MAX_YEAR - MIN_YEAR; i++) {
            years.add(String.valueOf(MIN_YEAR + i));
        }
        setComboBox(days, daysBox);
        setComboBox(months, monthsBox);
        setComboBox(years, yearsBox);
    }

    private void setDateBox(ArrayList<String> vals, int maxVal) {
        for (int i = 1; i <= maxVal; i++) {
            vals.add(i - 1, "0" + i);
            if (i >= 10) {
                vals.remove(i - 1);
                vals.add(i - 1, String.valueOf(i));
            }
        }
    }

    private void setComboBox(ArrayList<String> dateValues, ComboBox<String> box) {
        ObservableList<String> data = FXCollections.observableArrayList(dateValues);
        box.setItems(data);
        box.getSelectionModel().selectFirst();
    }

    @FXML
    void fileBtnHandler() {
        try {
            memos = new Memos(fileNameField.getText());
            searchMemoBtn.setDisable(false);
            searchMemoBtnHandler();
        } catch (Exception e) {
            errorMsgLabel.setText(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void searchMemoBtnHandler() {
        CleanDate selectedDate = getCleanDate();
        memoField.setText(memos.getMemo(selectedDate));
    }

    @FXML
    void saveBtnHandler() {
        CleanDate selectedDate = getCleanDate();
        try {
            memos.addMemo(selectedDate, memoField.getText());
            errorMsgLabel.setText("");
        }
        catch (Exception e){
            e.printStackTrace();
            errorMsgLabel.setText(e.getMessage());
        }
    }

    private CleanDate getCleanDate() {
        String day = daysBox.getValue();
        String month = monthsBox.getValue();
        String year = yearsBox.getValue();
        return new CleanDate(day, month, year);
    }
}
