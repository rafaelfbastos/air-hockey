import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Recursos {
    private static Recursos singleton = null;
    public int pontosJogador;
    public int pontosInimigo;
    public Font fontPontuacao;
    public int maxPontos;
    public Font fontMenu;
    public String msgFim;
    public Random rdn;
    public int pauseOpt;
    public AudioInputStream menu;
    public AudioInputStream silence;
    public AudioInputStream set;
    public AudioInputStream gol;
    public Clip clipMenu;
    public Clip clipSilence;
    public Clip clipSet;
    public Clip clipGol;

    private Recursos(){
        pontosInimigo = 0;
        pontosJogador = 0;
        fontPontuacao = new Font("Arial narrow", Font.LAYOUT_LEFT_TO_RIGHT,40);
        maxPontos = 5;
        fontMenu = new Font("Arial narrow", Font.CENTER_BASELINE,50);
        msgFim = "";
        rdn = new Random(System.currentTimeMillis());
        pauseOpt=0;
        carregarSons();
    }

    public static Recursos getInstance(){
        if (singleton==null) singleton = new Recursos();

        return singleton;
    }

    public BufferedImage cortarImagem(int x1, int y1, int x2, int y2, BufferedImage img){
        int largura = x2-x1;
        int altura = y2-y1;
        return img.getSubimage(x1,y1,largura,altura);
    }

    public void carregarSons(){

        try{
            menu = AudioSystem.getAudioInputStream(getClass().getResource("audio/menu.wav"));
            silence = AudioSystem.getAudioInputStream(getClass().getResource("audio/silence.wav"));
            set = AudioSystem.getAudioInputStream(getClass().getResource("audio/set.wav"));
            gol = AudioSystem.getAudioInputStream(getClass().getResource("audio/gol.wav"));

            clipGol = AudioSystem.getClip();
            clipSilence = AudioSystem.getClip();
            clipMenu = AudioSystem.getClip();
            clipSet = AudioSystem.getClip();

            clipMenu.open(menu);
            clipSilence.open(silence);
            clipSet.open(set);
            clipGol.open(gol);

            clipSilence.start();

        }catch (Exception e){}

    }

    public void tocarSomMenu(){
        clipMenu.setFramePosition(0);
        clipMenu.start();
    }
    public void tocarSomBolinha(){
        clipSet.setFramePosition(0);
        clipSet.start();
    }
    public void tocarSomGol(){
        clipGol.setFramePosition(0);
        clipGol.start();
    }

    public static double calcularDistancia(double catAdj, double catOps){
        double deltaX = Math.pow(catAdj,2);
        double deltaY = Math.pow(catOps,2);
        return Math.sqrt(deltaX+deltaY);
    }

    public int gerarAleatorio(int max, int min){
        return rdn.nextInt((max+1)-min)+min;
    }



}
