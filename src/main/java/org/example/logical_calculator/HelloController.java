package org.example.logical_calculator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label equationLabel;

    @FXML
    private Button negasiButton, andButton, orButton, implicationButton, bimplicationButton, PButton, QButton, fButton, tButton, backspaceButton, clearButton, openBracketButton, closingBracketButton, enterButton;

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
            //enterLabel();
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
        //two conditions : if error is met (results will be empty) and if error is not met (result will show)

        if(false){ //nanti ini diisi kalok ada eror

        }else{
            //change the light (penanda hukum)

            //print out the table

        }

    }



}