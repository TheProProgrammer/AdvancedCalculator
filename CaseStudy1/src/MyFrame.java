import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Locale;

public class MyFrame extends JFrame implements KeyListener
{
    private JTextField label;
    private JPanel grid;

    private final Font font1 = new Font(Font.DIALOG,Font.PLAIN, 20);
    private final Font font2 = new Font(Font.DIALOG,Font.PLAIN, 15);

    private final byte DECIMAL=10, BINARY=2, HEXA=16, OCTAL=8;
    private byte currentSystem = DECIMAL;

    private final String[] decimalNumbers = {"0","1","2","3","4","5","6","7","8","9"};
    private final String[] octalNumbers = {"0","1","2","3","4","5","6","7"};
    private final String[] binaryNumbers = {"0","1"};
    private final String[] hexaNumbers = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};

    private final String[] modes = {"Decimal", "Binary", "Octal", "HexaDecimal"};
    private final String[] types = {"Byte", "Short", "Integer", "Long", "Float", "Double"};

    private enum Type {BYTE, SHORT, INT, LONG, FLOAT, DOUBLE}
    private Type currentType = Type.INT;

    private enum AngleType {DEGREES,RADIANS}
    private AngleType currentAngleType = AngleType.DEGREES;

    private final JMenuBar mb = new JMenuBar();
    private final JMenu mode = new JMenu("Mode");
    private final JMenu type = new JMenu("Type");
    private final JMenu angleType = new JMenu("Angle Type");

    private final ButtonGroup modeGroup = new ButtonGroup();
    private final ButtonGroup typeGroup = new ButtonGroup();
    private final ButtonGroup angleTypeGroup = new ButtonGroup();

    private Object num1,num2, resultant;
    private char operation;
    private Clip clip;

    public static void main(String[] args)
    {
        //create and display the fram
        MyFrame myFrame = new MyFrame();
        myFrame.setVisible(true);
        myFrame.pack();
    }
    public MyFrame()
    {
        setWindow();//set window properties
        setLabel();//set label
        setMenus();//set menubar
        setKeypad();//set keypad
        setAudioService();//set audio services
    }
    private void setAudioService()
    {
        //set up the audio service before it can be used
        String errorClipPath = ".//res/error.wav";
        File file = new File(errorClipPath);
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void setMenus()
    {
        //set Type menu
        for(String iType : types)
        {
            JCheckBoxMenuItem jMenuItem = new JCheckBoxMenuItem(iType);
            jMenuItem.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    changeType(e);
                }
            });
            if(iType.equals("Integer"))
                jMenuItem.setSelected(true);
            typeGroup.add(jMenuItem);
            type.add(jMenuItem);
        }
        //set mode menu
        for(String iMode : modes)
        {
            JCheckBoxMenuItem jMenuItem2 = new JCheckBoxMenuItem(iMode);
            jMenuItem2.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    changeMode(e);
                }
            });
            if(iMode.equals("Decimal"))
                jMenuItem2.setSelected(true);
            modeGroup.add(jMenuItem2);
            mode.add(jMenuItem2);
        }
        //set angle type menus
        String[] angletypes = {"Degrees", "Radians"};
        for(String iAngleType : angletypes)
        {
            JCheckBoxMenuItem jMenuItem3 = new JCheckBoxMenuItem(iAngleType);
            jMenuItem3.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    changeAngleType(e);
                }
            });
            if(iAngleType.equals("Degrees"))
                jMenuItem3.setSelected(true);
            angleTypeGroup.add(jMenuItem3);
            angleType.add(jMenuItem3);
        }
        //add all those menus
        mb.add(type);
        mb.add(mode);
        mb.add(angleType);

        setJMenuBar(mb);
    }
    private void changeMode(ItemEvent e)
    {
        //change mode according to the button pressed
        JCheckBoxMenuItem abstractButton = (JCheckBoxMenuItem) e.getSource();
        switch (abstractButton.getText())
        {
            case "Decimal":
                currentSystem = DECIMAL;
                break;
            case "HexaDecimal":
                currentSystem = HEXA;
                break;
            case "Octal":
                currentSystem = OCTAL;
                break;
            case "Binary":
                currentSystem = BINARY;
                break;
        }
        clearEverything();
    }
    private void changeAngleType(ItemEvent e)
    {
        //change angle type according to the button pressed
        JCheckBoxMenuItem abstractButton = (JCheckBoxMenuItem) e.getSource();
        switch (abstractButton.getText())
        {
            case "Degrees":
                currentAngleType = AngleType.DEGREES;
                break;
            case "Radians":
                currentAngleType = AngleType.RADIANS;
                break;
        }
        clearEverything();
    }
    private void clearEverything()
    {
        //clear everything in the calculator
        num1 = 0;
        num2 = 0;
        resultant = 0;
        label.setText("0");
    }
    private void changeType(ItemEvent e)
    {
        //change data type according tot the button pressed
        JCheckBoxMenuItem abstractButton = (JCheckBoxMenuItem) e.getSource();
        switch (abstractButton.getText())
        {
            case "Long":
                currentType = Type.LONG;
                mode.setEnabled(false);
                currentSystem = DECIMAL;
                break;
            case "Integer":
                currentType = Type.INT;
                mode.setEnabled(true);
                break;
            case "Short":
                currentType = Type.SHORT;
                mode.setEnabled(false);
                currentSystem = DECIMAL;
                break;
            case "Byte":
                currentType = Type.BYTE;
                mode.setEnabled(false);
                currentSystem = DECIMAL;
                break;
            case "Float":
                currentType = Type.FLOAT;
                mode.setEnabled(false);
                currentSystem = DECIMAL;
                break;
            case "Double":
                currentType = Type.DOUBLE;
                mode.setEnabled(false);
                currentSystem = DECIMAL;
                break;
        }
        clearEverything();
    }
    private void setKeypad()
    {
        //create a keypad grid
        grid = new JPanel(new GridLayout(9,5));
        {
            //keypad string array
            String[] keypad = {"D_R","cosec","cot","sec","bckspc",
                                            "R_D","sin-1","cos-1","tan-1","CLR",
                                            "|x|","sin", "cos", "tan", "ln",
                                            "","sinh", "cosh", "tanh", "sqrt",
                                            "a", "!", "^", "%", "/",
                                            "b", "7", "8", "9", "*",
                                            "c", "4", "5", "6", "-",
                                            "d", "1", "2", "3", "+",
                                            "e", "f", "0", ".", "="};
            for (String key : keypad) {
                addButton(key);
            }
        }
        add(grid);
    }
    private void setLabel()
    {
        //properties of the main label
        Box box = Box.createVerticalBox();
        label = new JTextField();
        label.setEditable(false);
        label.addKeyListener(this);
        label.setBackground(null);
        label.setBorder(null);
        label.setText("0");
        label.setPreferredSize(new Dimension(300,30));
        label.setForeground(new Color(255, 255, 255));
        label.setFont(font1);
        label.setAlignmentX(Component.RIGHT_ALIGNMENT);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        box.add(label);
        add(box);
    }
    private void setWindow()
    {
        //set window properties
        getContentPane().setBackground(new Color(20, 20, 20));
        setTitle("Calculator");
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(356,500));
        setResizable(false);
        UIManager.put("activeCaption", new javax.swing.plaf.ColorUIResource(
                Color.gray));
        setDefaultLookAndFeelDecorated(true);
        ImageIcon img = new ImageIcon(".//res/icon.png");
        setIconImage(img.getImage());
    }
    private void addButton(String text)
    {
        //add button to the keypad
        JButton button = new JButton(text);
        button.setBackground(Color.black);
        button.setForeground(Color.WHITE);
        button.setFont(font2);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 40),1));
        button.setPreferredSize(new Dimension((340/5),40));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonWasPressed(text);
            }
        });
        //change color of ioerator buttons
        if (text.equals("+") || text.equals("-") || text.equals("*")
                || text.equals("/") ||  text.equals("bckspc") ||  text.equals("%")
                || text.equals("^") || text.equals("!") || text.equals("CLR") || text.equals("sqrt")
                || text.equals("ln") || text.equals("sin") || text.equals("cos") || text.equals("tan")
                || text.equals("cosec") || text.equals("sec") || text.equals("cot")
                || text.equals("sinh") || text.equals("cosh") || text.equals("tanh")
                || text.equals("sin-1") || text.equals("cos-1") || text.equals("tan-1"))
            button.setBackground(new Color(20, 20, 20));
        if (text.equals("="))
            button.setBackground(new Color(0, 87, 255));
        grid.add(button);
    }
    private boolean contains(String[] array, char key)
    {
        for (String element : array)
        {
            if (element.equals(String.valueOf(key)))
            {
                return true;
            }
        }
        return false;
    }
    private void buttonWasPressed(String ke)
    {
        //button pressed, what to do
        switch (ke)
        {
            case "":
            case "bckspc":
                if (label.getText().length() == 1)
                    label.setText("0");
                else
                    label.setText(label.getText().substring(0, label.getText().length() - 1));
                break;
            case "+":
            case "-":
            case "*":
            case "/":
            case "%":
                num1 = parseAccToType(label.getText());
                operation = ke.charAt(0);
                label.setText("0");
                break;
            case "=":
                num2 = parseAccToType(label.getText());
                switch (operation)
                {
                    case '+':
                        resultant = parseAccToType(String.valueOf(my_add(num1,num2)));
                        break;
                    case '-':
                        resultant = parseAccToType(String.valueOf(my_diff(num1,num2)));
                        break;
                    case '*':
                        resultant = parseAccToType(String.valueOf(my_prod(num1,num2)));
                        break;
                    case '/':
                        resultant = parseAccToType(String.valueOf(my_div(num1,num2)));
                        break;
                    case '%':
                        resultant = parseAccToType(String.valueOf(my_rem(num1,num2)));
                        break;
                    case '^':
                        resultant = parseAccToType(String.valueOf(my_pow(num1,num2)));
                        break;
                }
                operation = 0;
                label.setText(String.valueOf(resultant));
                break;
            case ".":
                if(!label.getText().contains("."))
                {
                    //only add decimal point on floating point data type
                    if (currentType == Type.FLOAT || currentType == Type.DOUBLE)
                    {label.setText(label.getText() + '.');}
                    else errorSound();
                }
                break;
            case "^":
                if (currentSystem == DECIMAL)
                {
                    num1 = parseAccToType(label.getText());
                    operation = ke.charAt(0);
                    label.setText("0");
                }
                else errorSound();
                break;
            case "!":
                if (currentSystem == DECIMAL)
                {
                    label.setText(String.valueOf(factorial(Integer.parseInt(label.getText()))));
                }
                else errorSound();
                break;
            case "sin":
            case "cos":
            case "tan":
            case "cosec":
            case "sec":
            case "cot":
            case "sinh":
            case "cosh":
            case "tanh":
                if (currentSystem == DECIMAL && currentType == Type.DOUBLE)
                    trigFunction(ke);
                else
                    errorSound();
                break;
            case "sin-1":
            case "cos-1":
            case "tan-1":
                if (currentSystem == DECIMAL)
                    InvTrigFunction(ke);
                else
                    errorSound();
                break;
            case "D_R":
                Object num = Double.parseDouble(label.getText());
                label.setText(String.valueOf(parseAccToType(String.valueOf((double) num *  Math.PI/180))));
                break;
            case "R_D":
                Object num_r = Double.parseDouble(label.getText());
                label.setText(String.valueOf(parseAccToType(String.valueOf((double) num_r *  180.0 / Math.PI))));
                break;
            case "|x|":
                if (Integer.parseInt(label.getText()) < 0)
                    label.setText(String.valueOf(Integer.parseInt(label.getText())*-1));
            case "ln":
                label.setText(String.valueOf(Math.log(Double.parseDouble(label.getText()))));
                break;
            case "sqrt":
                label.setText(String.valueOf(parseAccToType(String.valueOf(Math.sqrt(Double.parseDouble(label.getText()))))));
                break;
            case "CLR":
                label.setText("0");
                break;
            default:
                if(label.getText().equals("0"))
                {
                    if (currentSystem == BINARY && contains(binaryNumbers, ke.charAt(0)))
                        label.setText(ke);
                    else if (currentSystem == DECIMAL && contains(decimalNumbers,ke.charAt(0)))
                        label.setText(ke);
                    else if (currentSystem == HEXA && contains(hexaNumbers, ke.charAt(0)))
                        label.setText(ke);
                    else if (currentSystem == OCTAL && contains(octalNumbers, ke.charAt(0)))
                        label.setText(ke);
                    else
                        errorSound();
                }
                else
                {
                    if (currentSystem == BINARY && contains(binaryNumbers, ke.charAt(0)))
                        label.setText(label.getText() + ke);
                    else if (currentSystem == DECIMAL && contains(decimalNumbers, ke.charAt(0)))
                        label.setText(label.getText() + ke);
                    else if (currentSystem == HEXA && contains(hexaNumbers, ke.charAt(0)))
                        label.setText(label.getText()+ ke);
                    else if (currentSystem == OCTAL && contains(octalNumbers, ke.charAt(0)))
                        label.setText(label.getText() + ke);
                    else
                        errorSound();
                }
                break;
        }
    }
    private int factorial(int number)
    {
        int i,fact=1;
        for(i=1;i<=number;i++)
            fact=fact*i;
        return fact;
    }
    private Object my_add(Object num1, Object num2)//returns sum according to the data type provided
    {
        switch (currentType)
        {
            case DOUBLE:
                return (double) num1 + (double) num2;
            case SHORT:
                return (short) num1 + (short) num2;
            case FLOAT:
                return (float) num1 + (float) num2;
            case LONG:
                return (long) num1 + (long) num2;
            case BYTE:
                return (byte) num1 + (byte) num2;
            case INT:
                switch (currentSystem)
                {
                    case DECIMAL:
                        return (int) num1 + (int) num2;
                    case BINARY:
                        return Integer.toBinaryString(Integer.parseInt(num1.toString(),2)+Integer.parseInt(num2.toString(),2));
                    case OCTAL:
                        return Integer.toOctalString(Integer.parseInt(num1.toString(),8)+Integer.parseInt(num2.toString(),8));
                    case HEXA:
                        return Integer.toHexString(Integer.parseInt(num1.toString(),16)+Integer.parseInt(num2.toString(),16));
                }
        }
        return 0;
    }
    private Object my_diff(Object num1, Object num2)//returns difference according to the data type provided
    {
        switch (currentType)
        {
            case DOUBLE:
                return (double) num1 - (double) num2;
            case SHORT:
                return (short) num1 - (short) num2;
            case FLOAT:
                return (float) num1 - (float) num2;
            case LONG:
                return (long) num1 - (long) num2;
            case BYTE:
                return (byte) num1 - (byte) num2;
            case INT:
                switch (currentSystem)
                {
                    case DECIMAL:
                        return (int) num1 - (int) num2;
                    case BINARY:
                        return Integer.toBinaryString(Integer.parseInt(num1.toString(),2)-Integer.parseInt(num2.toString(),2));
                    case OCTAL:
                        return Integer.toOctalString(Integer.parseInt(num1.toString(),8)-Integer.parseInt(num2.toString(),8));
                    case HEXA:
                        return Integer.toHexString(Integer.parseInt(num1.toString(),16)-Integer.parseInt(num2.toString(),16));
                }
        }
        return 0;
    }
    private Object my_prod(Object num1, Object num2)//returns product according to the data type provided
    {
        switch (currentType)
        {
            case DOUBLE:
                return (double) num1 * (double) num2;
            case SHORT:
                return (short) num1 * (short) num2;
            case FLOAT:
                return (float) num1 * (float) num2;
            case LONG:
                return (long) num1 * (long) num2;
            case BYTE:
                return (byte) num1 * (byte) num2;
            case INT:
                switch (currentSystem)
                {
                    case DECIMAL:
                        return (int) num1 * (int) num2;
                    case BINARY:
                        return Integer.toBinaryString(Integer.parseInt(num1.toString(),2)*Integer.parseInt(num2.toString(),2));
                    case OCTAL:
                        return Integer.toOctalString(Integer.parseInt(num1.toString(),8)*Integer.parseInt(num2.toString(),8));
                    case HEXA:
                        return Integer.toHexString(Integer.parseInt(num1.toString(),16)*Integer.parseInt(num2.toString(),16));
                }
        }
        return 0;
    }
    private Object my_div(Object num1, Object num2)//returns division according to the data type provided
    {
        switch (currentType)
        {
            case DOUBLE:
                return (double) num1 / (double) num2;
            case SHORT:
                return (short) num1 / (short) num2;
            case FLOAT:
                return (float) num1 / (float) num2;
            case LONG:
                return (long) num1 / (long) num2;
            case BYTE:
                return (byte) num1 / (byte) num2;
            case INT:
                switch (currentSystem)
                {
                    case DECIMAL:
                        return (int) num1 / (int) num2;
                    case BINARY:
                        return Integer.toBinaryString(Integer.parseInt(num1.toString(),2)/Integer.parseInt(num2.toString(),2));
                    case OCTAL:
                        return Integer.toOctalString(Integer.parseInt(num1.toString(),8)/Integer.parseInt(num2.toString(),8));
                    case HEXA:
                        return Integer.toHexString(Integer.parseInt(num1.toString(),16)/Integer.parseInt(num2.toString(),16));
                }
        }
        return 0;
    }
    private Object my_rem(Object num1, Object num2)//returns remainder according to the data type provided
    {
        switch (currentType)
        {
            case DOUBLE:
                return (double) num1 % (double) num2;
            case SHORT:
                return (short) num1 % (short) num2;
            case FLOAT:
                return (float) num1 % (float) num2;
            case LONG:
                return (long) num1 % (long) num2;
            case BYTE:
                return (byte) num1 % (byte) num2;
            case INT:
                switch (currentSystem)
                {
                    case DECIMAL:
                        return (int) num1 % (int) num2;
                    case BINARY:
                        return Integer.toBinaryString(Integer.parseInt(num1.toString(),2)%Integer.parseInt(num2.toString(),2));
                    case OCTAL:
                        return Integer.toOctalString(Integer.parseInt(num1.toString(),8)%Integer.parseInt(num2.toString(),8));
                    case HEXA:
                        return Integer.toHexString(Integer.parseInt(num1.toString(),16)%Integer.parseInt(num2.toString(),16));
                }
        }
        return 0;
    }
    private Object my_pow(Object num1, Object num2)//returns power according to the data type provided
    {
        switch (currentType)
        {
            case DOUBLE:
                return Math.pow((double) num1, (double) num2);
            case SHORT:
                return (short)Math.pow((short) num1, (short) num2);
            case FLOAT:
                return (float)Math.pow((float) num1, (float) num2);
            case LONG:
                return (long)Math.pow((long) num1, (long) num2);
            case BYTE:
                return (byte)Math.pow((byte) num1, (byte) num2);
            case INT:
                return (int)Math.pow((int) num1, (int) num2);
        }
        return 0;
    }
    private void errorSound()//play error audio
    {
        clip.setFramePosition(0);
        clip.start();
    }
    private Object parseAccToType(String number)//parse the number according to the current data type
    {
        if (currentSystem == HEXA || currentSystem == BINARY || currentSystem == OCTAL)
            
            return number;
        else {
            try {
                switch (currentType) {
                    case INT:
                        return Integer.parseInt(number);
                    case BYTE:
                        return Byte.parseByte(number);
                    case LONG:
                        return Long.parseLong(number);
                    case FLOAT:
                        return Float.parseFloat(number);
                    case SHORT:
                        return Short.parseShort(number);
                    case DOUBLE:
                        return Double.parseDouble(number);
                }
            } catch (NumberFormatException e) {
                errorSound();
                JOptionPane.showMessageDialog(new JFrame(), "NUMBER IS OUT OF RANGE", "Error Message", JOptionPane.ERROR_MESSAGE);
            }
        }
        return 0;
    }
    private void trigFunction(String which)
    {
        double value = Double.parseDouble(label.getText());
        double answer = 0.0;
        if((which.equals( "tan") && value == 90.0) || (which.equals( "cot") && value == 0.0))
        {
            JOptionPane.showMessageDialog(new JFrame(), "INFINITY", "Error Message", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (currentAngleType == AngleType.DEGREES)
            value *= Math.PI/180.0;
        switch (which)
        {
            case "sin":
                answer = (double)parseAccToType(String.valueOf(Math.sin(value)));
                break;
            case "cos":
                answer = (double)parseAccToType(String.valueOf(Math.cos(value)));
                break;
            case "tan":
                answer = (double)parseAccToType(String.valueOf(Math.tan(value)));
                break;
            case "cosec":
                answer = (double)parseAccToType(String.valueOf(1/Math.sin(value)));
                break;
            case "sec":
                answer = (double)parseAccToType(String.valueOf(1/Math.cos(value)));
                break;
            case "cot":
                answer = (double)parseAccToType(String.valueOf(1/Math.tan(value)));
                break;
            case "sinh":
                answer = (double)parseAccToType(String.valueOf(Math.sinh(value)));
                break;
            case "cosh":
                answer = (double)parseAccToType(String.valueOf(Math.cosh(value)));
                break;
            case "tanh":
                answer = (double)parseAccToType(String.valueOf(Math.tanh(value)));
                break;
        }
        answer  = Math.round(answer*10000)/10000.0;
        label.setText(String.valueOf(answer));
    }
    private void InvTrigFunction(String which)
    {
        double value = Double.parseDouble(label.getText());
        double answer = 0.0;
        switch(which)
        {
            case "sin-1":
                answer = Math.asin(value);
                break;
            case "cos-1":
                answer = Math.acos(value);
                break;
            case "tan-1":
                answer = Math.atan(value);;
                break;
        }
        if (currentAngleType == AngleType.DEGREES)
            answer *= 180.0/Math.PI;
        answer = Math.round(answer*100)/100.0;
        label.setText(String.valueOf(parseAccToType(String.valueOf(answer))));
    }
    @Override
    public void keyTyped(KeyEvent ke)
    {
        buttonWasPressed(String.valueOf(ke.getKeyChar()));
    }
    @Override
    public void keyPressed(KeyEvent e) {

    }
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
