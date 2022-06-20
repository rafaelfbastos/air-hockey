import javax.swing.*;
import java.awt.*;

public class Principal extends JFrame {

    public static final int LARGURA_TELA = 640;
    public static final int ALTURA_TELA = 480;
    public static final int LIMITE_DIREITO = 420;
    public static final int LIMITE_ESQUERDO = 215;

    public Principal(){
        super();
        Game game = new Game();
        game.setPreferredSize(new Dimension(LARGURA_TELA,ALTURA_TELA));
        this.getContentPane().add(game);
        this.setTitle("Jogo 2D");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(100,100);
        this.setVisible(true);
        this.pack();
    }



}
