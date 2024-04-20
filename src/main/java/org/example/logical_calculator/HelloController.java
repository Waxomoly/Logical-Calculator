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

    @FXML
    protected void onKeyboardClicked(ActionEvent e) {

        Button clickedButton = (Button) e.getSource();

        if(clickedButton == negasiButton){
            calculatorBody.addToEquation('¬');
        }else if(clickedButton == andButton){
            calculatorBody.addToEquation('∧');
        }else if(clickedButton == orButton){
            calculatorBody.addToEquation('∨');
        }else if(clickedButton == implicationButton){
            calculatorBody.addToEquation('→');
        }else if(clickedButton == bimplicationButton){
            calculatorBody.addToEquation('↔');
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
            stringBuilder.append(c);
        }

        equationLabel.setText(stringBuilder.toString());
    }

    @FXML
    protected void enterLabel(){
        //Clear Table
        myTable.getColumns().clear();
        //Count
        LogicCalcu.generateCombinations(calculatorBody.getEquationOnLabel());
        System.out.println("p " + LogicCalcu.isPFound());
        System.out.println("q " + LogicCalcu.isQFound());
        //two conditions : if error is met (results will be empty) and if error is not met (result will show)

        if(false){ //nanti ini diisi kalok ada eror

        }else{
            //change the light (penanda hukum)


            //print out the table
            ArrayList<TableColumn> tableColumns = new ArrayList<>();

            if(LogicCalcu.isPFound() && LogicCalcu.isQFound() && (LogicCalcu.getValueArr().size()==2)){
                TableColumn PCol = new TableColumn("PCol");
                TableColumn QCol = new TableColumn("QCol");
                TableColumn resultCol = new TableColumn("resultCol");
                myTable.getColumns().addAll(PCol, QCol, resultCol);
            }else if(LogicCalcu.getValueArr().size()==2){
                if(LogicCalcu.isPFound()){
                    TableColumn PCol = new TableColumn("PCol");
                    TableColumn resultCol = new TableColumn("resultCol");
                    myTable.getColumns().addAll(PCol, resultCol);
                }else if(LogicCalcu.isQFound() && LogicCalcu.getValueArr().size()==2){
                    TableColumn QCol = new TableColumn("QCol");
                    TableColumn resultCol = new TableColumn("resultCol");
                    myTable.getColumns().addAll(QCol, resultCol);
                }else{
                    System.out.println("Something went wrong. (HelloController.java -> enterLabel() [1])");
                }
            }else{
                System.out.println("Something went wrong. (HelloController.java -> enterLabel() [2])");
            }

        }

    }



}