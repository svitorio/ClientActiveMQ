import javax.swing.*;

/**
 * Created by ivan on 29/03/17.
 */

public class Window extends JFrame {

    public Window(int w, int h) {
        setLayout(null);
        setSize(w, h);
        setLocationRelativeTo(null);
        //setTitle("Dicionário Distribuído");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

}
