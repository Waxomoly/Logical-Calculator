package org.example.logical_calculator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;

public class HelloController {
    @FXML
    private Label equationLabel;

    @FXML
    private Button negasiButton, andButton, orButton, implicationButton, bimplicationButton, PButton, QButton, fButton, tButton, backspaceButton, clearButton, openBracketButton, closingBracketButton, enterButton;

    @FXML
    private TableView myTable;

    private CalculatorBody calculatorBody = new CalculatorBody();
    private LogicCalcu logicCalcu = new LogicCalcu();

    @FXML
    protected void onKeyboardClicked(ActionEvent e) {

        Button clickedButton = (Button) e.getSource();

        if(clickedButton == negasiButton){
            calculatorBody.addToEquation('~');
        }else if(clickedButton == andButton){
            calculatorBody.addToEquation('&');
        }else if(clickedButton == orButton){
            calculatorBody.addToEquation('|');
        }else if(clickedButton == implicationButton){
            calculatorBody.addToEquation('>');
        }else if(clickedButton == bimplicationButton){
            calculatorBody.addToEquation('<');
        }else if(clickedButton == PButton){
            calculatorBody.addToEquation('P');
        }else if(clickedButton == QButton){
            calculatorBody.addToEquation('Q');
        }else if(clickedButton == fButton){
            calculatorBody.addToEquation('F');
        }else if(clickedButton == tButton){
            calculatorBody.addToEquation('T');
        }else if(clickedButton == backspaceButton){
            calculatorBody.backspace();
        }else if(clickedButton == clearButton){
            calculatorBody.clear();
        }else if(clickedButton == openBracketButton){
            calculatorBody.addToEquation('(');
        }else if(clickedButton == closingBracketButton){
            calculatorBody.addToEquation(')');
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

        for(char c : calculatorBody.getEquationOnLabel()){
            if(c == '~'){
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
    protected void enterLabel(){
        //Clear Table
        myTable.getColumns().clear();
        //set variables found status to false
        LogicCalcu.PFound = false;
        LogicCalcu.QFound = false;
        //Count
        LogicCalcu.generateCombinations(calculatorBody.getEquationOnLabel());
        System.out.println("p " + LogicCalcu.PFound);
        System.out.println("q " + LogicCalcu.QFound);
        //two conditions : if error is met (results will be empty) and if error is not met (result will show)

        if(false){ //nanti ini diisi kalok ada eror

        }else{
            //change the light (penanda hukum)


            //print out the table
            ArrayList<TableColumn> tableColumns = new ArrayList<>();

            if(LogicCalcu.PFound && LogicCalcu.QFound && (LogicCalcu.getValueArr().size()==4)){ //ada 2 variabel
                TableColumn PCol = new TableColumn("PCol");
                TableColumn QCol = new TableColumn("QCol");
                TableColumn resultCol = new TableColumn("resultCol");
                myTable.getColumns().addAll(PCol, QCol, resultCol);

                ArrayList<Character> currAnswers = LogicCalcu.getValueArr();

                myTable.getItems().add(new DataItem("0", "0", currAnswers.get(0).toString()));
                myTable.getItems().add(new DataItem("1", "0", currAnswers.get(1).toString()));
                myTable.getItems().add(new DataItem("0", "1", currAnswers.get(2).toString()));
                myTable.getItems().add(new DataItem("1", "1", currAnswers.get(3).toString()));
            }else if(LogicCalcu.getValueArr().size()==2){ //cuma ada 1 variabel (P or Q)
                if(LogicCalcu.PFound){
                    TableColumn PCol = new TableColumn("PCol");
                    TableColumn resultCol = new TableColumn("resultCol");
                    myTable.getColumns().addAll(PCol, resultCol);

                    ArrayList<Character> currAnswers = LogicCalcu.getValueArr();

                    myTable.getItems().add(new DataItem("0", null, currAnswers.get(0).toString()));
                    myTable.getItems().add(new DataItem("1", null, currAnswers.get(1).toString()));
                }else if(LogicCalcu.QFound){
                    TableColumn QCol = new TableColumn("QCol");
                    TableColumn resultCol = new TableColumn("resultCol");
                    myTable.getColumns().addAll(QCol, resultCol);

                    ArrayList<Character> currAnswers = LogicCalcu.getValueArr();

                    myTable.getItems().add(new DataItem(null, "0", currAnswers.get(0).toString()));
                    myTable.getItems().add(new DataItem(null, "1", currAnswers.get(1).toString()));
                }else{
                    System.out.println("Something went wrong. (HelloController.java -> enterLabel() [1])");
                }
            }else if(LogicCalcu.getValueArr().size()==1){ //cuma ada variabel true dan/atau false
                TableColumn answer = new TableColumn("Single Answer");
                myTable.getColumns().addAll(answer);
                //add data to table
                myTable.getItems().add(new DataItem(null, null, LogicCalcu.getValueArr().getFirst().toString()));
            }else{
                System.out.println("Something went wrong. (HelloController.java -> enterLabel() [2])");
            }

        }

    }



}