import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Jogador {

    public double posX;
    public double posY;
    public double raio;
    public double velX;
    public double velY;
    public double velBase;
    public double centroX;
    public double centroY;
    public BufferedImage sprite;
    public BufferedImage cima;
    public BufferedImage baixo;
    public BufferedImage direita;
    public BufferedImage esquerda;
    public BufferedImage parada;
    public BufferedImage esquerda_baixo;
    public BufferedImage esquerda_cima;
    public BufferedImage direita_cima;
    public BufferedImage direita_baixo;
    public AffineTransform af;

    public Jogador(){
        af = new AffineTransform();
        raio = 50;
        velBase = 4;
        velX = 0;
        velY = 0;
        posX = ((Principal.LARGURA_TELA*(7.0/8.0))-raio);
        posY = (Principal.ALTURA_TELA/2.0 )-raio;
        centroX = posX+raio;
        centroY = posY+raio;

        try{
            sprite = ImageIO.read(getClass().getResource("img/sprite_person_bola.png"));
            cima = Recursos.getInstance().cortarImagem(100,0,200,100,sprite);
            baixo = Recursos.getInstance().cortarImagem(0,100,100,200,sprite);
            esquerda = Recursos.getInstance().cortarImagem(200,100,300,200,sprite);
            direita = Recursos.getInstance().cortarImagem(300,0,400,100,sprite);
            parada = Recursos.getInstance().cortarImagem(300,100,400,200,sprite);
            esquerda_baixo = Recursos.getInstance().cortarImagem(100,100,200,200,sprite);
            esquerda_cima = Recursos.getInstance().cortarImagem(0,0,100,100,sprite);
            direita_baixo = Recursos.getInstance().cortarImagem(400,0,500,100,sprite);
            direita_cima = Recursos.getInstance().cortarImagem(200,0,300,100,sprite);

        }catch (Exception e){}
    }

    public BufferedImage obterImagem(){

        if (velX<0){
            if(velY==0) return esquerda;
            else if (velY>0) return esquerda_baixo;
            else return esquerda_cima;
        } else if (velX>0) {
            if(velY==0) return direita;
            else if (velY>0) return direita_baixo;
            else return direita_cima;
        } else{
            if(velY==0) return parada;
            else if (velY>0) return baixo;
            else return cima;
        }
    }
    public void mover(double deltaT){
        posX = posX+(velX*deltaT);
        posY = posY + (velY*deltaT);
        af.setToTranslation(posX,posY);
    }
    public void desmoverX(double deltaT){
        posX = posX-(velX*deltaT);
        af.setToTranslation(posX,posY);
    }
    public void desmoverY(double deltaT){
        posY = posY-(velY*deltaT);
        af.setToTranslation(posX,posY);
    }
    public void update(double deltaT){
        mover(deltaT);
        centroX = posX+raio;
        centroY = posY +raio;
    }

    public void handlerEvent(boolean k_cima, boolean k_baixo, boolean k_direita, boolean k_esquerda){
        velX = 0;
        velY = 0;

        if(k_baixo){
            velY = velBase;
            if(k_direita) velX = velBase;
            else if(k_esquerda) velX = -velBase;

        } else if (k_cima) {
            velY = -velBase;
            if(k_direita) velX = velBase;
            else if(k_esquerda) velX = -velBase;
        }
        else if (k_direita) velX = velBase;

        else if(k_esquerda) velX = -velBase;
    }
}
