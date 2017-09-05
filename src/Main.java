import javax.swing.*;

public class Main {

    public static Window janela;

    private static void initialize() {

        janela = new Window(380, 620);
        MainPanel painel = new MainPanel(janela.getWidth() - 10, janela.getHeight() - 60);

        janela.add(painel);
        janela.setTitle("Calculadora");
        janela.setResizable(false);
        janela.setVisible(true);
    }

    public static void main(String[] args) {

        initialize();

    }
}


