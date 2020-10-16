import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.util.Stack;

public class Calculate extends JFrame implements ActionListener{

    private String expression;//记录表达式

    //各个按键的字符
    private final String[] keys = {"(",")","C","Backspace",
            "7","8","9","+",
            "4","5","6","-",
            "1","2","3","*",
            ".","0","=","/"};
    private JTextField result = new JTextField("0");

    //按键
    private JButton[] button = new JButton[20];

    Calculate(){


        //计算器布局
        JPanel calckeysPanel = new JPanel();
        //布局先分为六行，第一行为文本显示，二到五行为按键
        calckeysPanel.setLayout(new GridLayout(6,1));

        calckeysPanel.add(result);
        result.setFont(result.getFont().deriveFont((float)(30)));
        Container[] containers = new Container[5];
        for (int i = 0;i < 5;++i){
            containers[i] = new Container();
            containers[i].setLayout(new GridLayout(1,4));
            calckeysPanel.add(containers[i]);
        }

        //按钮
        for (int i = 0;i < button.length;++i){
            button[i] = new JButton(keys[i]);
            button[i].setFont(button[i].getFont().deriveFont((float)(30)));
            containers[i/4].add(button[i]);
            button[i].addActionListener(this);
            if (i == 2 || i == 3) button[i].setForeground(Color.red);
        }
        getContentPane().add(calckeysPanel);

        //表达式赋值为空
        expression = "";
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        String action = e.getActionCommand();


        if (action.equals(keys[2])){
            //用户按下C
            handleC();
        }else if (action.equals(keys[3])){
            //用户按下退格
            handleBackspace();
        }else if (action.equals(keys[18])){
            //用户按下=
            handleCalc();
        }else{
            //用户输入表达式
            handleExpression(action);
        }
    }

    //处理按下C的事件
    private void handleC(){
        expression = "";
        result.setText("");
    }

    //处理按下退格的事件
    private void handleBackspace(){
        expression = expression.substring(0,expression.length() - 1);
        result.setText(expression);
    }

    //处理按下等号的事件
    private void handleCalc(){
        result.setText(Calculator.calrp(Calculator.getrp(expression)));
        expression = "";
    }

    //处理用户按下数字或运算符的事件
    private void handleExpression(String action){
        expression += action;
        result.setText(expression);
    }

    public static void main(String[] args){
        Calculate calculator = new Calculate();
        calculator.setSize(600,800);
        calculator.setVisible(true);
    }

    static class Calculator {
        //计算中缀表达式
        static Stack<Character> op = new Stack<>();

        public static Float getv(char op, Float f1, Float f2){
            if(op == '+') return f2 + f1;
            else if(op == '-') return f2 - f1;
            else if(op  == '*') return f2 * f1;
            else if(op == '/') return f2 / f1;
            else return Float.valueOf(-0);
        }

        public static String calrp(String rp){
            Stack<Float> v = new Stack<>();
            char[] arr = rp.toCharArray();
            int len = arr.length;
            for(int i = 0; i < len; i++){
                Character ch = arr[i];

                if(ch >= '0' && ch <= '9') v.push(Float.valueOf(ch - '0'));

                else v.push(getv(ch, v.pop(), v.pop()));
            }
            return v.pop().toString();
        }

        public static String getrp(String s){
            char[] arr = s.toCharArray();
            int len = arr.length;
            String out = "";

            for(int i = 0; i < len; i++){
                char ch = arr[i];
                if(ch == ' ') continue;


                if(ch >= '0' && ch <= '9') {
                    out+=ch;
                    continue;
                }

                if(ch == '(') op.push(ch);

                if(ch == '+' || ch == '-'){
                    while(!op.empty() && (op.peek() != '('))
                        out+=op.pop();
                    op.push(ch);
                    continue;
                }

                if(ch == '*' || ch == '/'){
                    while(!op.empty() && (op.peek() == '*' || op.peek() == '/'))
                        out+=op.pop();
                    op.push(ch);
                    continue;
                }

                if(ch == ')'){
                    while(!op.empty() && op.peek() != '(')
                        out += op.pop();
                    op.pop();
                    continue;
                }
            }
            while(!op.empty()) out += op.pop();
            return out;
        }
    }
}

