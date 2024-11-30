package com.example.calculatorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Calculator extends AppCompatActivity {
    private TextView result;
    private TextView solution;
    private String Expression = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);


        result = findViewById(R.id.input);
        solution = findViewById(R.id.output);

        findViewById(R.id.buttonzero).setOnClickListener(onClickListener);
        findViewById(R.id.button1).setOnClickListener(onClickListener);
        findViewById(R.id.button2).setOnClickListener(onClickListener);
        findViewById(R.id.button3).setOnClickListener(onClickListener);
        findViewById(R.id.button4).setOnClickListener(onClickListener);
        findViewById(R.id.button5).setOnClickListener(onClickListener);
        findViewById(R.id.button6).setOnClickListener(onClickListener);
        findViewById(R.id.button7).setOnClickListener(onClickListener);
        findViewById(R.id.button8).setOnClickListener(onClickListener);
        findViewById(R.id.button9).setOnClickListener(onClickListener);
        findViewById(R.id.buttonpoint).setOnClickListener(onClickListener);
        findViewById(R.id.buttonplus).setOnClickListener(onClickListener);
        findViewById(R.id.buttonminus).setOnClickListener(onClickListener);
        findViewById(R.id.buttonmultiply).setOnClickListener(onClickListener);
        findViewById(R.id.buttondivide).setOnClickListener(onClickListener);


        findViewById(R.id.buttonsave).setOnClickListener(onClickListener);

        findViewById(R.id.btn_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Expression.length() > 0) {
                  Expression =Expression.substring(0, Expression.length() - 1);
                    result.setText(Expression);
                    solution.setText(Expression);
                }
            }
        });
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            String buttonText = button.getText().toString();

            if (buttonText.equals("=")) {
                solve();
                return;
            }

           Expression += buttonText;
            result.setText(Expression);
        }
    };


    private void solve() {
        try {
            double resultValue = evaluateExpression(Expression);
            solution.setText(String.valueOf(resultValue));
        } catch (Exception e) {
            solution.setText("Error");
        }
    }

    private double evaluateExpression(String expression) throws Exception {

        expression = expression.replaceAll("\\s+", "");


        if (expression.isEmpty()) {
            throw new Exception("Empty expression");
        }
        String[] parts = expression.split("(?<=[-+×÷])|(?=[-+×÷])");
        ArrayList<String> tokens = new ArrayList<>(Arrays.asList(parts));


        tokens.removeIf(String::isEmpty);


        Stack<Double> operands = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (String token : tokens) {
            if (isNumeric(token)) {
                operands.push(Double.parseDouble(token));
            } else if (isOperator(token)) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(token)) {
                    performOperation(operands, operators);
                }
                operators.push(token);
            } else {
                throw new Exception("Invalid token: " + token);
            }
        }

        while (!operators.isEmpty()) {
            performOperation(operands, operators);
        }


        return operands.pop();
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }


    private boolean isOperator(String str) {
        return str.equals("+") || str.equals("-") || str.equals("×") || str.equals("÷");
    }

    private int precedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "×":
            case "÷":
                return 2;
            default:
                return 0;
        }
    }
    private void performOperation(Stack<Double> operands, Stack<String> operators) throws Exception {
        if (operands.size() < 2 || operators.isEmpty()) {
            throw new Exception("Invalid expression");
        }

        double operand2 = operands.pop();
        double operand1 = operands.pop();
        String op = operators.pop();

        switch (op) {
            case "+":
                operands.push(operand1 + operand2);
                break;
            case "-":
                operands.push(operand1 - operand2);
                break;
            case "×":
                operands.push(operand1 * operand2);
                break;
            case "÷":
                if (operand2 == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                operands.push(operand1 / operand2);
                break;
            default:
                throw new Exception("Invalid operator: " + op);
        }
    }
}