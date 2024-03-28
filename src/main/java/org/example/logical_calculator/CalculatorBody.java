package org.example.logical_calculator;

import java.util.ArrayList;

public class CalculatorBody {
    private ArrayList<Character> equationOnLabel = new ArrayList<>();

    public CalculatorBody() {
    }

    public void addToEquation(char c){
        this.equationOnLabel.add(c);
    }

    public ArrayList<Character> getEquationOnLabel() {
        return equationOnLabel;
    }

    public void setEquationOnLabel(ArrayList<Character> equationOnLabel) {
        this.equationOnLabel = equationOnLabel;
    }

    public void backspace(){
        this.equationOnLabel.remove(equationOnLabel.size()-1);
    }

    public void clear(){
        this.equationOnLabel = new ArrayList<>();
    }
}
