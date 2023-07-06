package com.example.mycalculator;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculateFunction {
    private static final Stack<Double> stack = new Stack<>();
    private static final Queue<Double> queue = new ArrayDeque<>();
    private static final Queue<String> queOp = new ArrayDeque<>();
    private static final Queue<String> newQueOp = new ArrayDeque<>();

    public static double calcualteExp(String exp) {

        //处理运算符在表达式最后
        if (exp.matches(".*[\\+\\-×÷]")) {
            exp = exp.substring(0, exp.length() - 1);
        }

        String[] sp; //存放分割数组

        //运算符在式子最前面
        if (exp.matches("[\\+\\-×÷].*")) {
            String fistElem=exp.substring(0,1); //截取首个字符
            exp=exp.substring(1);       //舍去首个字符
            //分割字符，提取数字
            sp = exp.split("\\+|-|×|÷");
            if(fistElem.equals("-")){       //首个字符为负号
                sp[0]="-"+sp[0];        //添加负号
            }
        }
        else{  //没有符号在前
            sp = exp.split("\\+|-|×|÷");
        }

        for (int i=sp.length-1;i>=0;--i) {
            if(sp[i].equals(".")) {
                stack.push(0.0);       //替换点号
            }
            else {
                stack.push(Double.parseDouble(sp[i]));
            }
        }
        //寻找匹配字符串
        Pattern p = Pattern.compile("\\+|-|×|÷");
        Matcher m = p.matcher(exp);
        while (m.find()) {
            queOp.add(m.group());
        }
        //运算降级序列化
        while (stack.size()>0) {
            String currOp;
            if (queOp.size()>0) {
                currOp = queOp.poll();
            }
            else {
                currOp = "0";
            }
            switch (currOp) {
                case "×":
                    stack.push(stack.pop() * stack.pop());
                    break;
                case "÷":
                    stack.push(stack.pop() / stack.pop());
                    break;
                case "+":
                    queue.add(stack.pop());
                    newQueOp.add("+");
                    break;
                case "-":
                    queue.add(stack.pop());
                    newQueOp.add("-");
                    break;
                default:
                    queue.add(stack.pop());
            }
        }
        //正常运算
        if (queue.size()>0) {
            double res = queue.poll();
            while (queue.size()>0) {
                String op = "";
                if (newQueOp.size()>0) {
                    op = newQueOp.poll();
                }
                else {
                    op = "NaN";
                }
                switch (op) {
                    case "+":
                        res += queue.poll();
                        break;
                    case "-":
                        res -= queue.poll();
                        break;
                    default:
                        System.out.println("NaN");
                }
            }
            return res;
        }
        return 0.0;
    }
}
