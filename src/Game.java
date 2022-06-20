import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;



public class Game extends JPanel {
    private Jogador jogador;
    private Inimigo inimigo;
    private Bolinha bolinha;
    private boolean k_cima = false;
    private boolean k_baixo = false;
    private boolean k_esquerda = false;
    private boolean k_direita = false;
    private boolean k_w = false;
    private boolean k_s = false;
    private boolean k_a = false;
    private boolean k_d = false;
    private long tempoAtual;
    private long tempoAnterior;
    private double deltaT;
    private double FPS_limit = 60;
    private BufferedImage bg;
    private BufferedImage airLogo;
    private char ESTADO;
    private char PLAYERS;


    public Game() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

                if(ESTADO=='E'){
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            k_cima = true;
                            break;
                        case KeyEvent.VK_DOWN:
                            k_baixo = true;
                            break;
                        case KeyEvent.VK_LEFT:
                            k_esquerda = true;
                            break;
                        case KeyEvent.VK_RIGHT:
                            k_direita = true;
                            break;
                        case KeyEvent.VK_ESCAPE:
                            ESTADO='P';
                            Recursos.getInstance().tocarSomMenu();
                            break;
                    }
                    if(PLAYERS=='M'){
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_W:
                                k_w = true;
                                break;
                            case KeyEvent.VK_S:
                                k_s = true;
                                break;
                            case KeyEvent.VK_A:
                                k_a = true;
                                break;
                            case KeyEvent.VK_D:
                                k_d = true;
                                break;
                            case KeyEvent.VK_ESCAPE:
                                ESTADO='P';
                                break;
                        }
                    }
                } else if (ESTADO=='P') {
                    switch (e.getKeyCode()){
                        case KeyEvent.VK_ESCAPE:
                            Recursos.getInstance().tocarSomMenu();
                            ESTADO='E';
                            break;
                        case KeyEvent.VK_UP:
                            Recursos.getInstance().pauseOpt=0;
                            Recursos.getInstance().tocarSomMenu();
                            break;
                        case KeyEvent.VK_DOWN:
                            Recursos.getInstance().pauseOpt=1;
                            Recursos.getInstance().tocarSomMenu();
                            break;
                        case KeyEvent.VK_ENTER:
                            if(Recursos.getInstance().pauseOpt==0) ESTADO='E';
                            else System.exit(0);
                            break;
                    }
                } else if (ESTADO=='S') {
                    switch (e.getKeyCode()){

                        case KeyEvent.VK_UP:
                            Recursos.getInstance().pauseOpt=0;
                            Recursos.getInstance().tocarSomMenu();
                            break;
                        case KeyEvent.VK_DOWN:
                            Recursos.getInstance().pauseOpt=1;
                            Recursos.getInstance().tocarSomMenu();
                                                       break;
                        case KeyEvent.VK_ENTER:
                            if(Recursos.getInstance().pauseOpt==0) PLAYERS='S';
                            else PLAYERS='M';
                            ESTADO='E';
                            break;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(ESTADO=='E'){
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            k_cima = false;
                            break;
                        case KeyEvent.VK_DOWN:
                            k_baixo = false;
                            break;
                        case KeyEvent.VK_LEFT:
                            k_esquerda = false;
                            break;
                        case KeyEvent.VK_RIGHT:
                            k_direita = false;
                            break;
                    }
                    if(PLAYERS=='M'){
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_W:
                                k_w = false;
                                break;
                            case KeyEvent.VK_S:
                                k_s = false;
                                break;
                            case KeyEvent.VK_A:
                                k_a = false;
                                break;
                            case KeyEvent.VK_D:
                                k_d = false;
                                break;
                        }
                    }
                }

            }
        });

        setFocusable(true);
        setLayout(null);
        jogador = new Jogador();
        inimigo = new Inimigo();
        bolinha = new Bolinha();
        ESTADO = 'S';
        PLAYERS = 'M';

        try {
            bg = ImageIO.read(getClass().getResource("img/bg.png"));
            airLogo = ImageIO.read(getClass().getResource("img/logo.png"));
        } catch (Exception e) {
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                gameLoop();
            }
        }).start();
    }

    //GAME-LOOP
    public void gameLoop() {
        tempoAnterior = System.nanoTime();
        double tempoMinimo = (1e9) / FPS_limit;

        while (true) {
            tempoAtual = System.nanoTime();
            deltaT = (tempoAtual - tempoAnterior) * (6e-8);
            handlerEvent();
            update(deltaT);
            render();
            tempoAnterior = tempoAtual;
            try {
                int tempoEspera = (int) ((tempoMinimo - deltaT) * (1e-6));
                Thread.sleep(tempoEspera);
            } catch (Exception e) {
            }

        }
    }

    public void handlerEvent() {
        jogador.handlerEvent(k_cima, k_baixo, k_direita, k_esquerda);
        if(PLAYERS=='M') inimigo.handlerEvent(k_w, k_s, k_d, k_a);
        else inimigo.handlerEvent(bolinha);
    }

    public void update(double deltaT) {
        if(ESTADO=='E'){
            jogador.update(deltaT);
            inimigo.update(deltaT);
            bolinha.update(deltaT);
            testeColisoes(deltaT);
            testeFimJogo();
        } else if (ESTADO=='G') {
            ESTADO='R';
            reiniciar();
            agendarTransicao(3000,'S');
        }

    }

    public void render() {
        repaint();
    }

    public void testeColisoes(double deltaT) {
        //condicionais para limitar movimento do jogador
        if (jogador.posX + jogador.raio * 2 >= Principal.LARGURA_TELA || jogador.posX <= 0) jogador.desmoverX(deltaT);
        if (jogador.posY + jogador.raio * 2 >= Principal.ALTURA_TELA || jogador.posY <= 0) jogador.desmoverY(deltaT);
        if (jogador.posX <= Principal.LIMITE_DIREITO) jogador.desmoverX(deltaT);

        //condicionais para teste de colisão da bolinha com as extremidades da tela
        if (bolinha.posX + bolinha.raio * 2 >= Principal.LARGURA_TELA) {
            bolinha.velX = 3;
            bolinha.velY= 0 ;
            bolinha.posX = (Principal.LARGURA_TELA/2.0)- bolinha.raio;
            bolinha.posY = (Principal.ALTURA_TELA/2.0)- bolinha.raio;
            Recursos.getInstance().pontosInimigo+=1;
            Recursos.getInstance().tocarSomGol();
        }
        if (bolinha.posX<=0){
            bolinha.velX = -3;
            bolinha.velY= 0;
            bolinha.posX = (Principal.LARGURA_TELA/2.0)- bolinha.raio;
            bolinha.posY = (Principal.ALTURA_TELA/2.0)- bolinha.raio;
            Recursos.getInstance().pontosJogador+=1;
            Recursos.getInstance().tocarSomGol();
        }

        if (bolinha.posY<=0){
            bolinha.velY*=-1;
            bolinha.posY = 0;
            Recursos.getInstance().tocarSomBolinha();
        }
        if (bolinha.posY + bolinha.raio * 2 >= Principal.ALTURA_TELA) {
            bolinha.velY *= -1;
            bolinha.posY = Principal.ALTURA_TELA - bolinha.raio * 2;
            Recursos.getInstance().tocarSomBolinha();
        }
        //colisão bolinha com o jogador
        double catAdj = jogador.centroX-bolinha.centroX;
        double catOps = jogador.centroY-bolinha.centroY;
        double hip = Recursos.calcularDistancia(catAdj,catOps);

        if(hip<=jogador.raio+bolinha.raio){
            jogador.desmoverX(deltaT);
            jogador.desmoverY(deltaT);
            double sen,cos;
            sen=catOps/hip;
            cos = catAdj/hip;
            bolinha.velX = (-bolinha.velBase)*cos;
            bolinha.velY = (-bolinha.velBase)*sen;
            Recursos.getInstance().tocarSomBolinha();
        }
        //colisão bolinha com o inimigo
        catAdj = inimigo.centroX-bolinha.centroX;
        catOps = inimigo.centroY-bolinha.centroY;
        hip = Recursos.calcularDistancia(catAdj,catOps);

        if(hip<=inimigo.raio+bolinha.raio){
            inimigo.desmoverX(deltaT);
            inimigo.desmoverY(deltaT);
            double sen,cos;
            sen=catOps/hip;
            cos = catAdj/hip;
            bolinha.velX = (-bolinha.velBase)*cos;
            bolinha.velY = (-bolinha.velBase)*sen;
            Recursos.getInstance().tocarSomBolinha();
        }
        //Movimentação do inimigo
        if(PLAYERS=='M'){
            //condicionais para limitar movimento do jogador
            if (inimigo.posX + inimigo.raio * 2 >= Principal.LARGURA_TELA || inimigo.posX <= 0) inimigo.desmoverX(deltaT);
            if (inimigo.posY + inimigo.raio * 2 >= Principal.ALTURA_TELA || inimigo.posY <= 0) inimigo.desmoverY(deltaT);
            if (inimigo.posX >= Principal.LIMITE_ESQUERDO- inimigo.raio * 2) inimigo.desmoverX(deltaT);
        }

    }

    public void testeFimJogo(){
        if(PLAYERS=='M'){
            if (Recursos.getInstance().pontosJogador==Recursos.getInstance().maxPontos){
                Recursos.getInstance().msgFim="VERMELHO GANHOU";
                ESTADO='G';
            }else if(Recursos.getInstance().pontosInimigo==Recursos.getInstance().maxPontos) {
                Recursos.getInstance().msgFim="AZUL GANHOU";
                ESTADO='G';
            }
        }else {
            if (Recursos.getInstance().pontosJogador==Recursos.getInstance().maxPontos){
                Recursos.getInstance().msgFim="VOCÊ GANHOU";
                ESTADO='G';
            }else if(Recursos.getInstance().pontosInimigo==Recursos.getInstance().maxPontos) {
                Recursos.getInstance().msgFim="VOCÊ PERDEU";
                ESTADO='G';
            }
        }

    }
    public void reiniciar(){
        jogador.posX = ((Principal.LARGURA_TELA*(7.0/8.0))- jogador.raio);
        jogador.posY = (Principal.ALTURA_TELA/2.0 )- jogador.raio;
        inimigo.posX = ((Principal.LARGURA_TELA*(1.0/8.0))-inimigo.raio);
        inimigo.posY = (Principal.ALTURA_TELA/2.0 )-inimigo.raio;
        Recursos.getInstance().pontosJogador=0;
        Recursos.getInstance().pontosInimigo=0;
        bolinha.velX = (bolinha.velBase/2) * Math.pow(-1,Recursos.getInstance().rdn.nextInt(100)) ;
        bolinha.posX= (Principal.LARGURA_TELA/2.0) - bolinha.raio;
        bolinha.posY = (Principal.ALTURA_TELA/2.0) - bolinha.raio;
        k_esquerda = false;
        k_direita = false;
        k_baixo = false;
        k_cima = false;
        k_d = false;
        k_a = false;
        k_s = false;
        k_w = false;


    }

    public void agendarTransicao(int tempo, char estado){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(tempo);
                }catch (Exception e){}
                ESTADO= estado;
            }
        });
        thread.start();
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        super.paintComponent(g2d);

        if(ESTADO=='S'){
            g2d.drawImage(airLogo,0,0,null);
            g2d.setColor(new Color(0,0,0,128));
            g2d.fillRect(0,0,Principal.LARGURA_TELA,Principal.ALTURA_TELA);
            g2d.setFont(Recursos.getInstance().fontMenu);
            g2d.setColor(Color.white);
            g2d.drawString("1 PLayer",220,200);
            g2d.drawString("2 Players",220,270);
            if (Recursos.getInstance().pauseOpt == 0) {
                g2d.fillRect(180, 170, 30, 30);
            } else {
                g2d.fillRect(180, 240, 30, 30);
            }


        } else if (ESTADO=='R') {

            g2d.setColor(Color.black);
            g2d.fillRect(0,0,Principal.LARGURA_TELA,Principal.ALTURA_TELA);
            g2d.setFont(Recursos.getInstance().fontMenu);
            g2d.setColor(Color.white);
            g2d.drawString(Recursos.getInstance().msgFim, 150,200);

        }else {
            g2d.drawImage(bg, 0, 0, Principal.LARGURA_TELA, Principal.ALTURA_TELA, null);
            g2d.setColor(Color.GRAY);
            g2d.fillRect(Principal.LIMITE_DIREITO, 0, 5, Principal.ALTURA_TELA);
            g2d.fillRect(Principal.LIMITE_ESQUERDO, 0, 5, Principal.ALTURA_TELA);
            g2d.drawImage(jogador.obterImagem(), jogador.af, null);
            g2d.drawImage(inimigo.obterImagem(), inimigo.af, null);
            g2d.drawImage(bolinha.img, bolinha.af, null);

            if(ESTADO=='E'){
                g2d.setFont(Recursos.getInstance().fontPontuacao);
                g2d.setColor(Color.white);
                g2d.drawString(Recursos.getInstance().pontosInimigo+"pts",140,40);
                g2d.drawString(Recursos.getInstance().pontosJogador+"pts",440,40);
            }
            else {
                g2d.setColor(new Color(0,0,0,128));
                g2d.fillRect(0,0,Principal.LARGURA_TELA,Principal.ALTURA_TELA);
                g2d.setFont(Recursos.getInstance().fontMenu);
                g2d.setColor(Color.white);
                g2d.drawString("JOGO PAUSADO",150,80);
                g2d.drawString("Continuar",220,200);
                g2d.drawString("Sair",220,270);
                if (Recursos.getInstance().pauseOpt == 0) {
                    g2d.fillRect(180, 170, 30, 30);
                } else {
                    g2d.fillRect(180, 240, 30, 30);
                }
            }
        }

    }
}
