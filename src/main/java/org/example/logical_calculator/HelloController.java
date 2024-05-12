package org.example.logical_calculator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.ResourceBundle;

public class HelloController implements Initializable { // button"
    @FXML
    private Label equationLabel, errorLabel;

    @FXML
    private Button negasiButton, andButton, orButton, implicationButton, bimplicationButton, PButton, QButton, fButton, tButton, backspaceButton, clearButton, openBracketButton, closingBracketButton, enterButton;

    @FXML
    private TableView myTable;

    @FXML
    private Circle tautologiLightStatus, kontradiksiLightStatus, kontingensiLightStatus;

    //buat tampilin hasil postfix, prefix, infix
    @FXML
    private Label firstFormNameLabel, secondFormNameLabel;
    @FXML
    private Label firstFormLabel, secondFormLabel;
    @FXML
    private ChoiceBox<String> myChoiceBox;
    private String[] formChoice = {"Infix", "Prefix", "Postfix"};

    Color wrongLightStatus = Color.rgb(255, 31, 31);
    Color rightLightStatus = Color.rgb(38, 255, 31);

    private CalculatorBody calculatorBody = new CalculatorBody();
    private LogicCalcu logicCalcu = new LogicCalcu();

    @FXML
    protected void onKeyboardClicked(ActionEvent e) {

        Button clickedButton = (Button) e.getSource();  //get source biar dpt idnya button

        if(clickedButton == negasiButton){
            calculatorBody.addToEquation('~');
            reset();
        }else if(clickedButton == andButton){
            calculatorBody.addToEquation('&');
            reset();
        }else if(clickedButton == orButton){
            calculatorBody.addToEquation('|');
            reset();
        }else if(clickedButton == implicationButton){
            calculatorBody.addToEquation('>');
            reset();
        }else if(clickedButton == bimplicationButton){
            calculatorBody.addToEquation('<');
            reset();
        }else if(clickedButton == PButton){
            calculatorBody.addToEquation('P');
            reset();
        }else if(clickedButton == QButton){
            calculatorBody.addToEquation('Q');
            reset();
        }else if(clickedButton == fButton){
            calculatorBody.addToEquation('F');
            reset();
        }else if(clickedButton == tButton){
            calculatorBody.addToEquation('T');
            reset();
        }else if(clickedButton == backspaceButton){
            calculatorBody.backspace();
            reset();
        }else if(clickedButton == clearButton){
            calculatorBody.clear();
            reset();
        }else if(clickedButton == openBracketButton){
            calculatorBody.addToEquation('(');
            reset();
        }else if(clickedButton == closingBracketButton){
            calculatorBody.addToEquation(')');
            reset();
        }else if(clickedButton == enterButton){
            enterLabel();
        }else{
            System.out.println("Button is not recognized.");
        }

        printLabel(calculatorBody.getEquationOnLabel(), equationLabel);

    }


    @FXML
    protected void printLabel(ArrayList<Character> equationList, Label chosenLabel){
        //Use Stringbuilder
        StringBuilder stringBuilder = new StringBuilder();

        for(char c : equationList) {

            if(c == '&'){
                stringBuilder.append('∧');
            }else if(c == '|'){
                stringBuilder.append('∨');
            }else if(c == '~'){
                stringBuilder.append('¬');
            }else if(c == '&'){
                stringBuilder.append('^');
            }else if(c == '|'){
                stringBuilder.append('v');
            }else if(c == '>'){ //implication
                stringBuilder.append('→');
            }else if(c == '<'){ //biimplication
                stringBuilder.append('↔');
            }else {
                stringBuilder.append(c);
            }
        }

        chosenLabel.setText(stringBuilder.toString());
    }

    @FXML
    protected void enterLabel() {
        // Clear Table
        myTable.getColumns().clear();
        // Set variables found status to false
        LogicCalcu.PFound = false;
        LogicCalcu.QFound = false;
        // Count
        if(calculatorBody.getEquationOnLabel().size() > 10){
            errorLabel.setText("Karakter yang diinput lebih dari 10. Tidak boleh.");
            errorLabel.setTextFill(Color.rgb(255, 0, 0));
            return;
        }

        //turn equation into string
        StringBuilder str = new StringBuilder();
        for(char c : calculatorBody.getEquationOnLabel()){
            str.append(c);
        }

        String inputFormType = myChoiceBox.getValue();
        LogicCalcu.startCalculating(str.toString(), inputFormType);
        System.out.println("p " + LogicCalcu.PFound);
        System.out.println("q " + LogicCalcu.QFound);



        // Two conditions: if error is met (results will be empty) and if error is not met (result will show)

        if (LogicCalcu.isErrorFound()) { // Nanti ini diisi kalok ada eror
            reset();
            errorLabel.setText(LogicCalcu.errorString);
            errorLabel.setTextFill(Color.rgb(255, 0, 0));
            System.out.println(LogicCalcu.errorString);
        } else {
            // Change the light (penanda hukum)
            if(LogicCalcu.isTautologiStatus()){
                tautologiLightStatus.setFill(rightLightStatus);
                kontradiksiLightStatus.setFill(wrongLightStatus);
                kontingensiLightStatus.setFill(wrongLightStatus);
            }else if(LogicCalcu.isKontradiksiStatus()){
                tautologiLightStatus.setFill(wrongLightStatus);
                kontradiksiLightStatus.setFill(rightLightStatus);
                kontingensiLightStatus.setFill(wrongLightStatus);
            }else if(LogicCalcu.isKontingensiStatus()){
                tautologiLightStatus.setFill(wrongLightStatus);
                kontradiksiLightStatus.setFill(wrongLightStatus);
                kontingensiLightStatus.setFill(rightLightStatus);
            }else{
                tautologiLightStatus.setFill(wrongLightStatus);
                kontradiksiLightStatus.setFill(wrongLightStatus);
                kontingensiLightStatus.setFill(wrongLightStatus);
            }

            //set error message text
            errorLabel.setText(LogicCalcu.errorString);
            errorLabel.setTextFill(Color.rgb(0, 0, 0));

            // Create an ObservableList to store the data items
            ObservableList<DataItem> dataItems = FXCollections.observableArrayList();

            // Add data items to the list based on the logic
            if (LogicCalcu.PFound && LogicCalcu.QFound && (LogicCalcu.getValueArr().size() == 4)) { // Ada 2 variabel
                ArrayList<Character> currAnswers = LogicCalcu.getValueArr();

                dataItems.add(new DataItem("0", "0", currAnswers.get(0).toString()));
                dataItems.add(new DataItem("0", "1", currAnswers.get(1).toString()));
                dataItems.add(new DataItem("1", "0", currAnswers.get(2).toString()));
                dataItems.add(new DataItem("1", "1", currAnswers.get(3).toString()));
            } else if (LogicCalcu.getValueArr().size() == 2) { // Cuma ada 1 variabel (P or Q)
                if (LogicCalcu.PFound) {
                    ArrayList<Character> currAnswers = LogicCalcu.getValueArr();

                    dataItems.add(new DataItem("0", null, currAnswers.get(0).toString()));
                    dataItems.add(new DataItem("1", null, currAnswers.get(1).toString()));
                } else if (LogicCalcu.QFound) {
                    ArrayList<Character> currAnswers = LogicCalcu.getValueArr();

                    dataItems.add(new DataItem(null, "0", currAnswers.get(0).toString()));
                    dataItems.add(new DataItem(null, "1", currAnswers.get(1).toString()));
                } else {
                    System.out.println("Something went wrong. (HelloController.java -> enterLabel() [1])");
                }
            } else if (LogicCalcu.getValueArr().size() == 1) { // Cuma ada variabel true dan/atau false
                dataItems.add(new DataItem(null, null, LogicCalcu.getValueArr().get(0).toString()));
            } else {
                System.out.println("Something went wrong. (HelloController.java -> enterLabel() [2])");
            }

            // Create TableColumn objects only for the desired columns and add them to the table
            System.out.println(LogicCalcu.getValueArr().size());
            if (LogicCalcu.PFound && LogicCalcu.QFound && (LogicCalcu.getValueArr().size() == 4)) { // Ada 2 variabel
                TableColumn<DataItem, String> PCol = new TableColumn<>("PCol");
                PCol.setCellValueFactory(new PropertyValueFactory<>("pValue"));

                TableColumn<DataItem, String> QCol = new TableColumn<>("QCol");
                QCol.setCellValueFactory(new PropertyValueFactory<>("qValue"));

                TableColumn<DataItem, String> resultCol = new TableColumn<>("resultCol");
                resultCol.setCellValueFactory(new PropertyValueFactory<>("resultValue"));

                myTable.getColumns().addAll(PCol, QCol, resultCol);

                PCol.setPrefWidth(114);
                QCol.setPrefWidth(114);
                resultCol.setPrefWidth(114);

                PCol.setResizable(false);
                QCol.setResizable(false);
                resultCol.setResizable(false);

            } else if (LogicCalcu.getValueArr().size() == 2) { // Cuma ada 1 variabel (P or Q)
                if (LogicCalcu.PFound) {
                    TableColumn<DataItem, String> PCol = new TableColumn<>("PCol");
                    PCol.setCellValueFactory(new PropertyValueFactory<>("pValue"));

                    TableColumn<DataItem, String> resultCol = new TableColumn<>("resultCol");
                    resultCol.setCellValueFactory(new PropertyValueFactory<>("resultValue"));

                    myTable.getColumns().addAll(PCol, resultCol);

                    PCol.setPrefWidth(171);
                    resultCol.setPrefWidth(171);

                    PCol.setResizable(false);
                    resultCol.setResizable(false);
                } else if (LogicCalcu.QFound) {
                    TableColumn<DataItem, String> QCol = new TableColumn<>("QCol");
                    QCol.setCellValueFactory(new PropertyValueFactory<>("qValue"));

                    TableColumn<DataItem, String> resultCol = new TableColumn<>("resultCol");
                    resultCol.setCellValueFactory(new PropertyValueFactory<>("resultValue"));

                    myTable.getColumns().addAll(QCol, resultCol);

                    QCol.setPrefWidth(171);
                    resultCol.setPrefWidth(171);

                    QCol.setResizable(false);
                    resultCol.setResizable(false);
                } else {
                    System.out.println("Something went wrong. (HelloController.java -> enterLabel() [1])");
                }
            } else if (LogicCalcu.getValueArr().size() == 1) { // Cuma ada variabel true dan/atau false
                TableColumn<DataItem, String> answer = new TableColumn<>("Single Answer");
                answer.setCellValueFactory(new PropertyValueFactory<>("resultValue"));
                myTable.getColumns().add(answer);


                answer.setPrefWidth(342);

                answer.setResizable(false);
            } else {
                System.out.println("Something went wrong. (HelloController.java -> enterLabel() [2])");
            }

            // Set the ObservableList as the items list for the TableView
            myTable.setItems(dataItems);
            myTable.setStyle("-fx-font-size: 18;");


            //set infix, prefix, postfix labels
            if(inputFormType.equalsIgnoreCase("infix")){
                firstFormNameLabel.setText("Prefix");
                firstFormLabel.setText(LogicCalcu.getPrefix());
                secondFormNameLabel.setText("Postfix");
                secondFormLabel.setText(LogicCalcu.getPostfix());
            }else if(inputFormType.equalsIgnoreCase("prefix")){
                firstFormNameLabel.setText("Infix");
                firstFormLabel.setText(LogicCalcu.getInfix());
                secondFormNameLabel.setText("Postfix");
                secondFormLabel.setText(LogicCalcu.getPostfix());
            }else if(inputFormType.equalsIgnoreCase("postfix")){
                firstFormNameLabel.setText("Infix");
                firstFormLabel.setText(LogicCalcu.getInfix());
                secondFormNameLabel.setText("Prefix");
                secondFormLabel.setText(LogicCalcu.getPrefix());
            }else{
                System.out.println("something went wrong. HelloController.JAVA -> enterLabel() [3]");
            }
        }

    }


    @FXML
    public void reset(){
        // Clear Table
        myTable.getColumns().clear();

        //reset all law lights
        tautologiLightStatus.setFill(wrongLightStatus);
        kontradiksiLightStatus.setFill(wrongLightStatus);
        kontingensiLightStatus.setFill(wrongLightStatus);

        errorLabel.setText("You're Good :)");
        errorLabel.setTextFill(Color.rgb(0, 0, 0));

        //reset answer label titles
        firstFormNameLabel.setText("[EMPTY] Please enter valid equation");
        secondFormNameLabel.setText("[EMPTY] Please enter valid equation");

        //clear answer labels
        firstFormLabel.setText("");
        secondFormLabel.setText("");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myChoiceBox.getItems().addAll(formChoice);
        myChoiceBox.setValue(formChoice[0]);
    }
}