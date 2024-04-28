package org.example.logical_calculator;

public class DataItem {
    private String pValue;
    private String qValue;
    private String resultValue;

    public DataItem(String pValue, String qValue, String resultValue) {
        this.pValue = pValue;
        this.qValue = qValue;
        this.resultValue = resultValue;
    }

    public String getPValue() {
        return pValue;
    }

    public void setPValue(String pValue) {
        this.pValue = pValue;
    }

    public String getQValue() {
        return qValue;
    }

    public void setQValue(String qValue) {
        this.qValue = qValue;
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }
}

