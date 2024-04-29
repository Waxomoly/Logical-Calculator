package org.example.logical_calculator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class HelloController {
    @FXML
    private Label equationLabel, errorLabel;

    @FXML
    private Button negasiButton, andButton, orButton, implicationButton, bimplicationButton, PButton, QButton, fButton, tButton, backspaceButton, clearButton, openBracketButton, closingBracketButton, enterButton;

    @FXML
    private TableView myTable;

    @FXML
    private Circle tautologiLightStatus, kontradiksiLightStatus, kontingensiLightStatus;

    Color wrongLightStatus = Color.rgb(255, 31, 31);
    Color rightLightStatus = Color.rgb(38, 255, 31);

    private CalculatorBody calculatorBody = new CalculatorBody();
    private LogicCalcu logicCalcu = new LogicCalcu();

    @FXML
    protected void onKeyboardClicked(ActionEvent e) {

        Button clickedButton = (Button) e.getSource();

        if(clickedButton == negasiButton){
            calculatorBody.addToEquation('~');
            initialize();
        }else if(clickedButton == andButton){
            calculatorBody.addToEquation('&');
            initialize();
        }else if(clickedButton == orButton){
            calculatorBody.addToEquation('|');
            initialize();
        }else if(clickedButton == implicationButton){
            calculatorBody.addToEquation('>');
            initialize();
        }else if(clickedButton == bimplicationButton){
            calculatorBody.addToEquation('<');
            initialize();
        }else if(clickedButton == PButton){
            calculatorBody.addToEquation('P');
            initialize();
        }else if(clickedButton == QButton){
            calculatorBody.addToEquation('Q');
            initialize();
        }else if(clickedButton == fButton){
            calculatorBody.addToEquation('F');
            initialize();
        }else if(clickedButton == tButton){
            calculatorBody.addToEquation('T');
            initialize();
        }else if(clickedButton == backspaceButton){
            calculatorBody.backspace();
            initialize();
        }else if(clickedButton == clearButton){
            calculatorBody.clear();
            initialize();
        }else if(clickedButton == openBracketButton){
            calculatorBody.addToEquation('(');
            initialize();
        }else if(clickedButton == closingBracketButton){
            calculatorBody.addToEquation(')');
            initialize();
        }else if(clickedButton == enterButton){
            enterLabel();
        }else{
            System.out.println("Button is not recognized.");
        }

        printLabel();

    }


    @FXML
    protected void printLabel(){
        //Use Stringbuilder
        StringBuilder stringBuilder = new StringBuilder();

        for(char c : calculatorBody.getEquationOnLabel()) {

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

        equationLabel.setText(stringBuilder.toString());
    }

    @FXML
    protected void enterLabel() {
        // Clear Table
        myTable.getColumns().clear();
        // Set variables found status to false
        LogicCalcu.PFound = false;
        LogicCalcu.QFound = false;
        // Count
        LogicCalcu.generateCombinations(new ArrayList<>(calculatorBody.getEquationOnLabel()));
        System.out.println("p " + LogicCalcu.PFound);
        System.out.println("q " + LogicCalcu.QFound);
        // Two conditions: if error is met (results will be empty) and if error is not met (result will show)

        if (LogicCalcu.isErrorFound()) { // Nanti ini diisi kalok ada eror
            initialize();
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
            } else if (LogicCalcu.getValueArr().size() == 2) { // Cuma ada 1 variabel (P or Q)
                if (LogicCalcu.PFound) {
                    TableColumn<DataItem, String> PCol = new TableColumn<>("PCol");
                    PCol.setCellValueFactory(new PropertyValueFactory<>("pValue"));

                    TableColumn<DataItem, String> resultCol = new TableColumn<>("resultCol");
                    resultCol.setCellValueFactory(new PropertyValueFactory<>("resultValue"));

                    myTable.getColumns().addAll(PCol, resultCol);
                } else if (LogicCalcu.QFound) {
                    TableColumn<DataItem, String> QCol = new TableColumn<>("QCol");
                    QCol.setCellValueFactory(new PropertyValueFactory<>("qValue"));

                    TableColumn<DataItem, String> resultCol = new TableColumn<>("resultCol");
                    resultCol.setCellValueFactory(new PropertyValueFactory<>("resultValue"));

                    myTable.getColumns().addAll(QCol, resultCol);
                } else {
                    System.out.println("Something went wrong. (HelloController.java -> enterLabel() [1])");
                }
            } else if (LogicCalcu.getValueArr().size() == 1) { // Cuma ada variabel true dan/atau false
                TableColumn<DataItem, String> answer = new TableColumn<>("Single Answer");
                answer.setCellValueFactory(new PropertyValueFactory<>("resultValue"));
                myTable.getColumns().add(answer);
            } else {
                System.out.println("Something went wrong. (HelloController.java -> enterLabel() [2])");
            }

            // Set the ObservableList as the items list for the TableView
            myTable.setItems(dataItems);


        }
    }


    @FXML
    public void initialize(){
        // Clear Table
        myTable.getColumns().clear();

        //reset all law lights
        tautologiLightStatus.setFill(wrongLightStatus);
        kontradiksiLightStatus.setFill(wrongLightStatus);
        kontingensiLightStatus.setFill(wrongLightStatus);

        errorLabel.setText("You're Good :)");
        errorLabel.setTextFill(Color.rgb(0, 0, 0));
    }



}