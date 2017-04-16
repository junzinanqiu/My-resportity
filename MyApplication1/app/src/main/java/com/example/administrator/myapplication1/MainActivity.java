package com.example.administrator.myapplication1;
import java.math.BigDecimal;
import java.util.regex.Pattern;

import com.example.administrator.myapplication1.R.id;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
public class MainActivity extends AppCompatActivity {

    private EditText print;
    private static String fistNumber = "0";// 第一次输入的值
    private static String secondNumber = "0";// 第二次输入的值
    private static String num = "0";// 显示的结果
    private static String saveNum = "0";// 储存被清空的num
    private static String numText = "0";//测试字符串是否为"x.o"
    //用着用着发现设置过多的全局变量是种非常不好的习惯
    private static int flg = 0;//实现当显示错误后只能按C，CE按钮，按其他键无效
    private static int flg2 = 0;//判断是否为重新输入符号还是累加运算符加减乘除
    private static int flgIfGetFir=0;//运算符累加一次
    private static int flg3 = 0;//控制在字符前面加+，-
    private static int flg4 = 0;//开关，进行ontake或输入加减乘除之后要清空屏幕以便下一次输入数字
    private static int flg5 = 0;//开关，输入加减乘除之后要清空屏幕以便下一次输入数字
    private static int flgIfGetTwo = 0;//开关，在未输入其他运算符前开关关闭不更新SecondNumber
    private static int flgIfGetTwo2=0;//通过输入数字或者点ontake 里面的按钮来打开按等号时获取SecondNumber的通道并通过flgGetTwo来判断是否替换firstNumber的值
    private static int flg10=0;//判断按等号时是否更新FirstNumber的值
    private int flg6 = 0;//控制，显示错误后就不再显示savenum
    private int flg7 = 0;//判断字符串的数值是否等于0的参数
    private int flg8=0;//判断是否输入过ce
    private int length=0;
    private int time=0;
    public Counts take = null;

    //分别用数字，二目运算符，单目运算符数组存储按钮的ID并初始化按钮
    private int[] btidNum = {R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4,
            R.id.button_5, R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9, R.id.button_0,
            R.id.button_point};
    private Button[] buttons = new Button[btidNum.length];
    private int[] btidTake = {R.id.button_divide, R.id.button_min, R.id.button_multiply,
            R.id.button_add};
    private Button[] buttonTake = new Button[btidTake.length];
    private int[] btidOntake = {R.id.button_change, R.id.button_dao, R.id.button_sqrt, id.button_x2};
    private Button[] buttonOntake = new Button[btidOntake.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        print = (EditText) findViewById(R.id.edit_text);
        print.setText("0");
        print.setEnabled(false);
        GetNumber get = new GetNumber();
        for (int i = 0; i < btidNum.length; i++) {
            buttons[i] = (Button) findViewById(btidNum[i]);
            buttons[i].setOnClickListener(get);
        }
        Compute cm = new Compute();
        for (int i = 0; i < btidTake.length; i++) {
            buttonTake[i] = (Button) findViewById(btidTake[i]);
            buttonTake[i].setOnClickListener(cm);
        }
        OnTake ontake = new OnTake();
        for (int i = 0; i < btidOntake.length; i++) {
            buttonOntake[i] = (Button) findViewById(btidOntake[i]);
            buttonOntake[i].setOnClickListener(ontake);
        }



        Button button_delete = (Button) findViewById(id.button_DEL);
        button_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flg!=1)
                if (saveNum.indexOf("E") == -1&&flg4!=1) {
                        if(saveNum.length()==1)
                        {
                            saveNum="0";
                        }
                        //用substring 来截取一段字符串
                        else {saveNum = saveNum.substring(0, saveNum.length() - 1);
                            //num=num.substring(0,num.length()-1);
                        if (saveNum.equals("-0") || saveNum.equals("-"))
                            saveNum = "0";}
                        print.setText(saveNum);
                }
            }
        });
        Button button_ce = (Button) findViewById(R.id.button_CE);
        button_ce.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                num = "0";
                flg8=1;
                //如果屏幕提示错误的时候除了按C和CE键都没反应此时c与CE键功能相同
                if(flg==1){
                    fistNumber = secondNumber =saveNum=numText= num;
                    flg=0;
                    flg2=0;
                    flg3 = 0;
                    flg4=0;
                    flg5=0;
                    flgIfGetFir= 0;
                    flgIfGetTwo=0;
                    flgIfGetTwo2=0;
                    flg6=0;
                    flg7=0;
                    flg8=0;
                    take=null;}
                print.setText(num);
            }
        });
        Button button_c = (Button) findViewById(R.id.button_C);
        button_c.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                num = "0";
                fistNumber = secondNumber = saveNum=numText=num;
                print.setText(num);
                flg=0;
                flg2=0;
                flg3 = 0;
                flg4=0;
                flg5=0;
                flgIfGetFir= 0;
                 flgIfGetTwo=0;
                flgIfGetTwo2=0;
                flg6=0;
                flg7=0;
                flg8=0;
                flg10=0;
                take=null;
            }
        });
        Button eq = (Button) findViewById(R.id.button_eq);

        eq.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flg!=1) {
                    if (take != null) {
                        //在等号之前输入过加减乘除则开关打开，从屏幕获取SecondNum
                        if (flgIfGetTwo == 0)
                        { secondNumber = print.getText().toString();
                            //secondNum不能总是gettext()里的内容
                            if(flg8==1)
                            {
                                secondNumber=secondNumber.substring(length-1);
                                flg8=0;
                            }
                            flg10=1;}
                        //出现a+b=a=这样的输入的时候用的
                        else if(flgIfGetTwo2==1&&flg10==1)
                        {
                            fistNumber=secondNumber;
                            flg10=0;
                            secondNumber = print.getText().toString();
                        }
                        for (int i = 0; i < secondNumber.length(); i++)
                            if (secondNumber.charAt(i) != '.' && secondNumber.charAt(i) != '0')
                                flg7 = 1;
                        if (take == Counts.DIVIDE && flg7!=1) {
                            print.setText("0不能为被除数");
                            flg = 1;
                        } else {
                            num = take.Values(fistNumber, secondNumber);
                            fistNumber = num;
                            print.setText(num);
                            //加减乘除获取fistNum的开关打开
                            flgIfGetFir = 0;
                            //获取secondNum的开关关闭
                            flgIfGetTwo = 1;
                            //获取SecondNum并把secondNum赋值给FistNum的开关关闭
                            flgIfGetTwo2=0;
                            //归零
                            flg7=0;
                        }
                    }
                    //把运算结果赋值给savenum因为在ontake，delete里面是对SaveNum进行操作
                    saveNum=num;
                    //为下一次输入清空屏幕的参数
                    flg4=1;
                }
            }
        });


    }

//完成点击加减乘除后的获取并为take（储存加减运算符的）赋值
    class Compute implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            if (flg != 1) {
                //按了这个按钮之后flg2=1,开关关闭不接受fisNum或secondNum只是完成给Take赋值,从而避免连续输入多个运算符的情况
                if (flg2 == 0) {
                    //如果是第一次输入符号或者是之前按过等号后再输入符号则获取FirstNum
                    if (flgIfGetFir == 0) {
                        fistNumber = print.getText().toString();
                    } //不然则应该是连续的加减此时应先算出值显示出来再获取SecondNum
                    else {
                        secondNumber = print.getText().toString();
                        for (int i = 0; i < secondNumber.length(); i++)
                            if (secondNumber.charAt(i) != '.' && secondNumber.charAt(i) != '0')
                                flg7 = 1;
                        if (take == Counts.DIVIDE && flg7 != 1) {
                            print.setText("0不能为被除数");
                            flg = 1;
                        } else {
                            num = take.Values(fistNumber, secondNumber);
                            fistNumber = num;
                            print.setText(num);
                            flg7=0;
                        }
                    }
                }
                    switch (arg0.getId()) {
                        case R.id.button_add:
                            take = Counts.ADD;
                            break;
                        case R.id.button_min:
                            take = Counts.MINUS;
                            break;
                        case R.id.button_multiply:
                            take = Counts.MULTIPLY;
                            break;
                        case id.button_divide:
                            take = Counts.DIVIDE;
                            break;
                    }
                //为下一次谁数据时清空屏幕做准备,为什么不用之前的flg4呢因为flg4被用作输入过=,ontake符号的标志，有了这个标志delete便不起作用
                flg5=1;
                if(flg2==0) {
                    flgIfGetFir = 1;
                    flgIfGetTwo = 0;
                    flg3 = 0;
                    flg2 = 1;
                }
            }
        }
    }
//一个枚举类，里面包含着运算方法。
//用一个枚举类型来存储运算符以及把给take赋值和计算结果分开虽然增加了代码量但是是代码显得更清晰
    public enum Counts {
        ADD, MINUS, MULTIPLY, DIVIDE;
        public String Values(String num1, String num2) {
            BigDecimal number1 = new BigDecimal(num1);
            BigDecimal number2 = new BigDecimal(num2);
            BigDecimal number = BigDecimal.valueOf(0);
            switch (this) {
                case ADD:
                    number = number1.add(number2);
                    break;
                case MINUS:
                    number = number1.subtract(number2);
                    break;
                case MULTIPLY:
                    number = number1.multiply(number2);
                    break;
                case DIVIDE:
                    number = number1.divide(number2, 20, BigDecimal.ROUND_UP);
                    break;

            }
            return number.toString();

        }

    }

    class OnTake implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (flg != 1) {
                switch (v.getId()) {
                    case R.id.button_change:
                        if (flg3 == 0 && !saveNum.equals("0")) {
                            saveNum="-"+saveNum;
                            flg3 = 1;
                            flg5=0;
                        } else {
                            saveNum =  saveNum.substring( saveNum.indexOf("-") + 1, saveNum.length());
                            flg3 = 0;
                        }
                        break;
                    case R.id.button_dao:
                        for (int i = 0; i <  saveNum.length(); i++)
                            if ( saveNum.charAt(i) != '.' &&  saveNum.charAt(i) != '0')
                                flg7 = 1;
                        if (flg7 == 1) {
                            saveNum = BigDecimal.valueOf(1).divide(new BigDecimal( saveNum), 12, BigDecimal.ROUND_UP).stripTrailingZeros()
                                    .toString();
                            flg7=0;
                        } else {
                            print.setText("除数不能为0");
                            flg6 = 1;
                            flg = 1;
                        }
                        break;
                    case R.id.button_x2:
                        double text = Double.parseDouble( saveNum) * Double.parseDouble( saveNum);
                        saveNum = String.valueOf(text);
                        break;
                    case R.id.button_sqrt:
                        if ( saveNum.indexOf("-") != -1) {
                            print.setText("无效输入");
                            flg6 = 1;
                            flg = 1;
                        } else {
                            Double numss = Math.sqrt(new BigDecimal( saveNum).doubleValue());
                            saveNum=numss.toString();
                        }
                }
                if(v.getId()!=R.id.button_change)
                {flg4 = 1;
                }
                if (flg6 != 1) {
                    if(saveNum.length()>2){
                    numText=saveNum.substring(saveNum.length()-2);
                    if(numText.equals(".0"))
                    {
                        saveNum=saveNum.substring(0,saveNum.length()-2);
                        numText="0";
                    }
                    }
                    print.setText(saveNum);
                }if(flgIfGetTwo==1)
                flgIfGetTwo2=1;
            }
        }
    }

    class GetNumber implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (flg != 1) {
                num=saveNum;
                //有两种情况都要清空输入栏一时已经计算过一次了二是输入加减乘除任意符号后
                if (flg4 == 1||flg5==1) {
                  num = "0";
                    flg4 = 0;
                    flg5=0;
                }
                if (num.equals("0")) {
                    //print.setText("");
                    //最初状态时如果输入的第一个数不是小数点就把num赋值为""
                    //达到两个效果一个是重复输入0时，num一直被清空再赋值，不会显示0000000二是如果有小数点则显示为"0."
                    num = v.getId() == R.id.button_point ? "0" : "";
                }
                //因为点击时传过来的参数是V通过把v强制转换为button,则可以使用Button 的相关方法
                // 通过getText()获得按钮的文本内容
                //因为gettext()不是字符串类型所以通过toSrting()来转换
                String txt = ((Button) v).getText().toString();
                //正则表达式如果符合表达式则把num+text赋值给num
                boolean s = Pattern.matches("-*(\\d+).?(\\d)*", num + txt);
                num = s ? (num + txt) : num;
                print.setText(num);
                flg2=0;
                if(flgIfGetTwo==1)
                flgIfGetTwo2=1;
                saveNum=num;
                if(flg8==1&&time==0)
                {
                    length=saveNum.length();
                    time=1;
                }
            }

        }
    }
}


