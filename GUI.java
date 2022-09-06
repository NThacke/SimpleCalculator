import javax.swing.BorderFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class GUI implements ActionListener {
    
    private JFrame frame;
    private JTextField textfield;
    private JLabel inputLabel;
    private JLabel outputLabel;
    private JPanel panel; 

    public GUI() {
        
        frame = new JFrame();    //the GUI frame

        //textfield of user input
        textfield = new JTextField("enter an expression");
        textfield.addActionListener(this);

        //evluation button
        JButton button = new JButton("Evaluate");
        button.addActionListener(this);

        //input label
        inputLabel = new JLabel("Input");
        
        //output label
        outputLabel = new JLabel("Output");

        //the panel inside the frame
        panel = new JPanel();                           //a panel within the GUI
        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));  //border of the panel
        panel.setLayout(new GridLayout(0,1));   //layout of the panel

        panel.add(textfield);                               //textfield in panel
        panel.add(button);                                  //button in panel
        panel.add(inputLabel);                              //input label in panel
        panel.add(outputLabel);                             //output label in panel


        //setting up the GUI frame
        frame.add(panel, BorderLayout.CENTER);                  //add panel to the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   //set what happens when user closes frame
        frame.setTitle("Calculator");                   //set title of frame
        frame.pack();                                           //matches frame to a certain size
        frame.setVisible(true);                               //sets frame to be visible


    }

    public void actionPerformed(ActionEvent e) {
        
        String input = textfield.getText();
        inputLabel.setText("Input: " + input);

        try {
            Expression expression = new Expression(input);
            outputLabel.setText("Output: " + expression.evaluate());
        }
        //maybe some error will occur here if user inputs odd input
        catch (Exception ef) {
            outputLabel.setText("Output: ERROR. Input must be valid (fully paranthesized)");
        }



    }
    public static void main(String[] args) {

        new GUI();
    }
}