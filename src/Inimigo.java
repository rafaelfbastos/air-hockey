import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Inimigo {

    public double posX;
    public double posY;
    public double raio;
    public double velX;
    public double velY;
    public double centroX;
    public double centroY;
    public double velBase;
    public AffineTransform af;
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
    private long acumuladorTempoVertical;
    private long acumuladorTempoHorizontal;
    private long tempoAtual;
    private long tempoAnterior;

    public Inimigo(){
        af = new AffineTransform();
        raio = 50;
        posX = ((Principal.LARGURA_TELA*(1.0/8.0))-raio);
        posY = (Principal.ALTURA_TELA/2.0 )-raio;
        velBase = 4;
        velY = 0;
        centroX = posX+raio;
        centroY = posY+raio;
        acumuladorTempoVertical=0;
        acumuladorTempoHorizontal=0;
        tempoAnterior=System.currentTimeMillis();

        try{
            sprite = ImageIO.read(getClass().getResource("img/sprite_inimigo.png"));
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

    public void handlerEvent(Bolinha bolinha){
        testeColisoes();
        tempoAtual=System.currentTimeMillis();
        acumuladorTempoVertical = acumuladorTempoVertical+(tempoAtual-tempoAnterior);
        acumuladorTempoHorizontal = acumuladorTempoHorizontal+(tempoAtual-tempoAnterior);

        if(acumuladorTempoVertical>=Recursos.getInstance().gerarAleatorio(120,80)){
            acumuladorTempoVertical=0;
            movimentoVertical(bolinha);
        }
        if(acumuladorTempoHorizontal>=Recursos.getInstance().gerarAleatorio(600,400)){
            acumuladorTempoHorizontal = 0;
            movimentoHorizontal(bolinha);
        }
        tempoAnterior=tempoAtual;
    }

    private void movimentoVertical(Bolinha bolinha){
        velY=0;
        double diferencaY = centroY - bolinha.centroY;
        double limite = raio*(Recursos.getInstance().gerarAleatorio(8,4)/10.0);
        if (diferencaY<-limite) {
            velY = velBase;
        } else if (diferencaY>limite) {
            velY = -velBase;
        }

    }


    private void movimentoHorizontal(Bolinha bolinha){
        velY=0;
        double distanciaBolinhaX = Math.abs(centroX-bolinha.centroX);
        double distanciaBolinhaY = Math.abs(centroY-bolinha.centroY);
        double aceleracao = distanciaBolinhaY/120;
        double limite = raio*(Recursos.getInstance().gerarAleatorio(8,4)/10.0);
        if (distanciaBolinhaX>211) {
            velX = velBase*aceleracao;
        } else{
            velX = -velBase*aceleracao;
        }
    }

    private void testeColisoes(){
        if(colideBaixo()){
            velY=0;
            posY = Principal.ALTURA_TELA -raio*2;
        }
        if (colideCima()){
            velY=0;
            posY = 0;
        }
        if(colideDireita()){
            velX=0;
            posX=Principal.LIMITE_ESQUERDO-raio*2;
        }
        if(colideEsquerda()){
            velX=0;
            posX=0;
        }
    }
    private boolean colideCima(){
        return posY<=0;
    }
    private boolean colideBaixo(){
        return posY+raio*2 >=Principal.ALTURA_TELA;
    }
    private boolean colideDireita(){
        return posX + raio*2 >=Principal.LIMITE_ESQUERDO;
    }
    private boolean colideEsquerda(){
        return posX<=0;
    }

}


