package com.example.mycalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private final StringBuilder builder = new StringBuilder();
    private TextView edt_input,edt_output;
    private double result = 0;
    private String showText = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt_input = findViewById(R.id.edt_input);
        edt_output = findViewById(R.id.edt_output);
        int[] buttonSeq = {
                R.id.btn_0,
                R.id.btn_1,
                R.id.btn_2,
                R.id.btn_3,
                R.id.btn_4,
                R.id.btn_5,
                R.id.btn_6,
                R.id.btn_7,
                R.id.btn_8,
                R.id.btn_9,
                R.id.btn_pt,
                R.id.btn_eq,
                R.id.btn_add,
                R.id.btn_sub,
                R.id.btn_mul,
                R.id.btn_div,
                R.id.btn_clr,
                R.id.btn_del,
                R.id.btn_sqr,
                R.id.btn_alt
        };
        for(int but : buttonSeq)
            findViewById(but).setOnClickListener(this);
        builder.append(0);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String inputText = "";
        if(id!=R.id.btn_sqr) {
            inputText = ((TextView) view).getText().toString();
            if (inputText.matches("\\d|\\.")) {
                resultCheck();
                if(builder.toString().equals("0") && inputText.matches("\\d")){
                    builder.deleteCharAt(builder.length()-1);
                }
                if(inputText.matches("\\.")){
                    if(builder.toString().equals(""))
                        builder.append(0);
                    switch (builder.charAt(builder.length()-1)){
                        case '+':
                        case '-':
                        case '×':
                        case '÷':
                            builder.append(0);
                            break;
                    }
                }
                builder.append(inputText);
                if(verifyExp(builder.toString())){
                    refreshText(builder.toString());
                }
                else{
                    Toast.makeText(this, "请停止你的行为！", Toast.LENGTH_SHORT).show();
                    builder.deleteCharAt(builder.length()-1);  //表达式不正确删除最后一位字符
                }


            }
            else if (inputText.matches("\\+|-|×|÷")) {
                resultCheck();
                builder.append(inputText);
                if(verifyExp(builder.toString())){
                    refreshText(builder.toString());
                }
                else{
                    builder.deleteCharAt(builder.length() - 1);
                    builder.deleteCharAt(builder.length() - 1);
                    builder.append(inputText);
                    refreshText(builder.toString());
                }

            }
            else {
                switch (inputText) {
                    case "+/-":
                        resultCheck();
                        result = 0 - CalculateFunction.calcualteExp(builder.toString());
                        if(result<0.0)
                            builder.insert(0,'-');
                        else if (result>0.0)
                            builder.deleteCharAt(0);
                        refreshText(result+"");
                        edt_output.setText(result+"");
                        break;
                    case "AC":
                        refreshText("");
                        result=0.0;
                        builder.delete(0, builder.length());
                        builder.append(0);
                        break;
                    case "DEL":
                        resultCheck();
                        if (builder.length() > 0) {
                            builder.deleteCharAt(builder.length() - 1);
                            refreshText(builder.toString());
                        }
                        else {
                            Toast.makeText(this, "已没有数字可删!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "=":
                        resultCheck();
                        if(result==0.0) {
                            result = CalculateFunction.calcualteExp(builder.toString());
                            switch (builder.charAt(builder.length()-1)){
                                case '+' :
                                case '-' :
                                case '×' :
                                case '÷' :
                                    builder.deleteCharAt(builder.length()-1);
                                    break;
                                case '.' :
                                    builder.append(0);
                                    break;
                            }
                            refreshText(builder.toString()+"=");
                            edt_output.setText(result+"");
                        }
                        break;
                    default:
                        Toast.makeText(this, "请停止你的行为！", Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            resultCheck();
            result = Math.sqrt(CalculateFunction.calcualteExp(builder.toString()));
            switch (builder.charAt(builder.length()-1)){
                case '+' :
                case '-' :
                case '×' :
                case '÷' :
                    builder.deleteCharAt(builder.length()-1);
                    break;
                case '.' :
                    builder.append(0);
                    break;
            }
            refreshText("√(" + builder.toString() + ")=\n" + result);
            edt_output.setText(result+"");
        }
    }
    public boolean verifyExp(String exp){
        String lastNum = "";
        String[] sp = exp.split("\\+|-|×|÷");
        char lastChar = exp.charAt(exp.length()-1);
        lastNum = sp[sp.length-1];
        if(String.valueOf(lastChar).matches("\\+|-|×|÷"))
        {
            lastNum="";
            return exp.matches(".*(\\d[+-×÷])|.*(\\.[+-×÷])");
        }else{
            return  lastNum.matches("^[-]?\\d*\\.?\\d*");
        }
    }
    private void refreshText(String text) {
        showText = text;
        edt_input.setText(showText);
    }
    private void clear() {
        builder.delete(0, builder.length());
        showText="";
    }
    public void resultCheck(){
        if(result!=0){
            String res=String.valueOf(result);
            if(res.matches("^[-]?\\d*\\.?\\d*")){
                clear();
                builder.append(result);
                result = 0;
            }
            else{
                clear();
                builder.append("0");
                result=0;
            }
        }
        if(builder.length()==0){
            builder.append(0);
        }
    }
}






























