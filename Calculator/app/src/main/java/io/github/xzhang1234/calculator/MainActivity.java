package io.github.xzhang1234.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText input;
    private EditText output;

    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button button_dot;
    private Button button_plus;
    private Button button_minus;
    private Button button_multiply;
    private Button button_divide;
    private Button button_equal;

    private Button button_clear;

    private double num1;
    private double num2;
    private String operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.input);
        output = (EditText) findViewById(R.id.output);

        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        button0 = (Button) findViewById(R.id.button0);
        button_dot = (Button) findViewById(R.id.button_dot);

        button_plus = (Button) findViewById(R.id.button_plus);
        button_minus = (Button) findViewById(R.id.button_minus);
        button_multiply = (Button) findViewById(R.id.button_multiply);
        button_divide = (Button) findViewById(R.id.button_divide);
        button_equal = (Button) findViewById(R.id.button_equal);

        button_clear = (Button) findViewById(R.id.button_clear);

        View.OnClickListener clearListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.setText("");
                output.setText("");
            }
        };
        button_clear.setOnClickListener(clearListener);



        View.OnClickListener numListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.append(((Button) v).getText().toString());
            }
        };

        button0.setOnClickListener(numListener);
        button1.setOnClickListener(numListener);
        button2.setOnClickListener(numListener);
        button3.setOnClickListener(numListener);
        button4.setOnClickListener(numListener);
        button5.setOnClickListener(numListener);
        button6.setOnClickListener(numListener);
        button7.setOnClickListener(numListener);
        button8.setOnClickListener(numListener);
        button9.setOnClickListener(numListener);
        button_dot.setOnClickListener(numListener);



        View.OnClickListener operatorListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cache the operator
                operator = ((Button) v).getText().toString();
                input.append(operator);
            }
        };

        button_plus.setOnClickListener(operatorListener);
        button_minus.setOnClickListener(operatorListener);
        button_multiply.setOnClickListener(operatorListener);
        button_divide.setOnClickListener(operatorListener);


        View.OnClickListener equalListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the number and operator
                String[] parsedInput = input.getText().toString().split("\\+|-|\\*|/");
                num1 = Double.valueOf(parsedInput[0]);
                num2 = Double.valueOf(parsedInput[1]);
                //evaluate
                Double result = evaluate(num1, num2, operator);
                //display the output
                output.append(result.toString());
            }
        };

        button_equal.setOnClickListener(equalListener);
    }



    private double evaluate (double num1, double num2, String operator) {
        switch(operator) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                return num1 / num2;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
