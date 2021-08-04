package calculator;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ProblemFormat {
    private String operationType;
    private int firstNumber;
    private int secondNumber;
    private int operationResult;

    public ProblemFormat(){
        // Jackson deserialization
    }

    public ProblemFormat(String operationType, int firstNumber, int secondNumber, int operationResult) {
        this.operationType = operationType;
        this.firstNumber = firstNumber;
        this.secondNumber = secondNumber;
        this.operationResult = operationResult;
    }

    public String getOperationType() {
        return operationType;
    }

    public int getFirstNumber() {
        return  firstNumber;
    }

    public int getSecondNumber() {
        return secondNumber;
    }

    public int getOperationResult() {
        return operationResult;
    }

}
