package com.example.calculator;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MainActivity extends AppCompatActivity {

    private TextView display, result;
    private StringBuilder equation = new StringBuilder();
    private ScriptEngine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);
        result = findViewById(R.id.result);

        // Initialize the JavaScript engine for evaluating expressions
        engine = new ScriptEngineManager().getEngineByName("rhino");

        setNumberButtonClickListener();
        setOperatorButtonClickListener();
    }

    private void setNumberButtonClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                equation.append(button.getText().toString());
                display.setText(equation.toString());
            }
        };

        int[] numberIds = {R.id.button0, R.id.button1, R.id.button2, R.id.button3,
                R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9};

        for (int id : numberIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void setOperatorButtonClickListener() {
        findViewById(R.id.clear).setOnClickListener(v -> {
            equation.setLength(0);
            display.setText("0");
            result.setText("");
        });

        findViewById(R.id.add).setOnClickListener(v -> appendOperator('+'));
        findViewById(R.id.subtract).setOnClickListener(v -> appendOperator('-'));
        findViewById(R.id.multiply).setOnClickListener(v -> appendOperator('*'));
        findViewById(R.id.divide).setOnClickListener(v -> appendOperator('/'));
        findViewById(R.id.modulus).setOnClickListener(v -> appendOperator('%'));
        findViewById(R.id.open_bracket).setOnClickListener(v -> appendOperator('('));
        findViewById(R.id.close_bracket).setOnClickListener(v -> appendOperator(')'));

        findViewById(R.id.equal).setOnClickListener(v -> calculateResult());
    }

    private void appendOperator(char operator) {
        equation.append(operator);
        display.setText(equation.toString());
    }

    private void calculateResult() {
        try {
            // Evaluate the equation with the JavaScript engine
            String expression = equation.toString().replace("×", "*").replace("÷", "/");
            Object resultValue = engine.eval(expression);

            // Display the result
            result.setText("= " + resultValue);
        } catch (ScriptException e) {
            result.setText("Error");
        }
    }
}
