import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JPanel implements ActionListener {

    private JPanel resultPanel, buttonPanel;
    String imr="";
    String oper="";
    private String string = "7,8,9,+,4,5,6,-,1,2,3,*,CLR,0,=,/";
    private String[] vetorStrings;
    private static JLabel resultTxt;
    private int numeroDeTeclas = 16; // numero multiplo de 3 e 4

    public MainPanel(int w, int h) {
        configure(w, h);

        vetorStrings = string.split(",");
        if (vetorStrings.length != 16)
            System.exit(1);

        resultPanel = new JPanel(null);
        buttonPanel = new JPanel(null);

        configureBorder(resultPanel, "");
        configureButtonPanelElements();

        add(resultPanel);
        add(buttonPanel);
    }

    private void configure(int w, int h) {
        setSize(w, h);
        //setLocation(10, 10);
        setLayout(null);
    }

    private void configureBorder(JPanel panel, String title) {
        Border borda = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

        TitledBorder tit;
        tit = BorderFactory.createTitledBorder(borda, title);
        tit.setTitleColor(Color.GRAY);
        tit.setTitleFont(new Font("Arial", Font.PLAIN, 12));
        panel.setBorder(tit);
    }

    private static int i = 0;
    private static JButton[] buttons;

    private void configureButtonPanelElements() {
        resultPanel.setBounds(10, 0, getWidth()-10, getHeight() / 4);
        resultPanel.setBackground(new Color(241, 255, 253));

        resultTxt = new JLabel();
        resultTxt.setText("0");
        resultTxt.setFont(new Font("Arial", 3, 36));
        resultTxt.setHorizontalAlignment(SwingConstants.RIGHT);
        resultTxt.setVerticalAlignment(SwingConstants.BOTTOM);
        resultTxt.setBounds(-25, -25, resultPanel.getWidth(), resultPanel.getHeight());
        resultPanel.add(resultTxt);

        //==============================================================================================================
        buttonPanel.setBounds(10, resultPanel.getHeight(), getWidth()-10, getHeight());
        buttonPanel.setBackground(new Color(255, 248, 249));

        buttons = new JButton[numeroDeTeclas];
        int buttonsPerRow = 4, panelsPerColumn = 4;

        int xPos = 0, panelYPos = 0, panelWidth = buttonPanel.getWidth(), panelHeight = buttonPanel.getHeight() / 6;

        for (int y = 0; y < panelsPerColumn; y ++) {

            JPanel panel = new JPanel(null);
            panel.setBounds(0, panelYPos, panelWidth, panelHeight);

            for (int x = 0; x < buttonsPerRow; x ++) {

                buttons[i] = new JButton();
                buttons[i].setText(vetorStrings[i]);
                buttons[i].setFont(new Font("Arial", 3, 18));
                buttons[i].setBounds(xPos, 0, panel.getWidth() / buttonsPerRow, panel.getHeight());
                buttons[i].addActionListener(this);

                if (x == buttonsPerRow -1) {
                    buttons[i].setBackground(new Color(115, 115, 115));
                    buttons[i].setForeground(new Color(255, 255, 255));
                } else {
                    buttons[i].setBackground(new Color(77, 77, 77));
                    buttons[i].setForeground(new Color(255, 255, 255));
                }
                panel.add(buttons[i]);
                xPos += panelWidth / buttonsPerRow;
                System.out.print(" " +i);
                i ++;
            }

            buttonPanel.add(panel);
            panelYPos += panelHeight;
            xPos = 0;
        }
    }

    public static void exibirResultadoNaTela(String result) {
        //System.out.println(result);
        //Main.janela.setVisible(false);
        resultTxt.setText(result);
        //Main.janela.repaint();
        //Main.janela.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        for (int i = 0; i < buttons.length; i++) {
            if (e.getSource().equals(buttons[i])) {

                if(buttons[i].getText().equals("+")){
                    oper = "soma";
                }
                if(buttons[i].getText().equals("-")){
                    oper = "subt";
                }
                if(buttons[i].getText().equals("*")){
                    oper = "mult";
                }
                if(buttons[i].getText().equals("/")){
                    oper = "div";
                }
                if(buttons[i].getText().equals("=") && !oper.equals("") && !imr.equals("")){
                    try {
                        enviar(imr,oper);
                        break;
                    } catch (JMSException e1) {
                        e1.printStackTrace();
                    }

                }
                if(buttons[i].getText().equals("CLR")){
                    imr="";
                }
                else{
                    imr += buttons[i].getText();
                }

                exibirResultadoNaTela(imr);
            }
        }

    }

    private void enviar(String p, String oper) throws JMSException {
        Sender sender = new Sender();
        sender.Sender(oper,p);
        Receiver("result"+oper);
    }

    public void Receiver(String subject) throws JMSException {
        // Getting JMS connection from the server
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://10.180.21.102:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        // Creating session for seding messages
        Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);

        // Getting the queue 'JCG_QUEUE'
        Destination destination = session.createQueue(subject);

        // MessageConsumer is used for receiving (consuming) messages
        MessageConsumer consumer = session.createConsumer(destination);

        // Here we receive the message.
        Message message = consumer.receive();

        // We will be using TestMessage in our example. MessageProducer sent us a TextMessage
        // so we must cast to it to get access to its .getText() method.
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            //exibirResultadoNaTela(textMessage.getText());
            System.out.println("Received message '" + textMessage.getText() + "'");
            String text = textMessage.getText();
            exibirResultadoNaTela(text);
            imr = text;

        }

        connection.close();
    }

}
