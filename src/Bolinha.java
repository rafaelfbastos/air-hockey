import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bolinha {
    public double posX;
    public double posY;
    public double raio;
    public double velX;
    public double velY;
    public double velBase;
    public double centroX;
    public double centroY;
    public BufferedImage sprite;
    public BufferedImage img;
    public AffineTransform af;

    public Bolinha(){
        try{
            sprite = ImageIO.read(getClass().getResource("img/sprite_person_bola.png"));
            img = Recursos.getInstance().cortarImagem(400,100,430,130,sprite);
        }catch (Exception e){}

        af = new AffineTransform();
        raio=15;
        posX= (Principal.LARGURA_TELA/2.0) - raio;
        posY = (Principal.ALTURA_TELA/2.0) - raio;
        velBase=6;
        velX=3*(Math.pow(-1,Recursos.getInstance().rdn.nextInt(100)));
        velY=0;
        centroX=posX+raio;
        centroY=posY+raio;
    }

    public void update(double deltaT){
        mover(deltaT);
        centroX = posX+raio;
        centroY = posY +raio;
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

}
