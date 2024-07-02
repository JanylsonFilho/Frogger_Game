package br.com.mvbos.lgj.base;
import java.awt.*;

public class Tronco extends Elemento {
    private boolean daDireitaParaEsquerda;

    // Construtor do Tronco, inicializa as propriedades e define a direção do movimento
    public Tronco(int px, int py, int largura, int altura, boolean daDireitaParaEsquerda) {
        super(px, py, largura, altura);
        this.daDireitaParaEsquerda = daDireitaParaEsquerda;
        setVel(2); // Define a velocidade do tronco
    }

    // Método para verificar se o tronco se move da direita para a esquerda
    public boolean isDaDireitaParaEsquerda() {
        return daDireitaParaEsquerda;
    }

    // Atualiza a posição do tronco a cada frame
    @Override
    public void atualiza() {
        if (daDireitaParaEsquerda) {
            // Move o tronco para a esquerda
            setPx(getPx() - getVel());
            // Se o tronco sair da tela à esquerda, reinicia à direita
            if (getPx() + getLargura() < 0) {
                setPx(800);
            }
        } else {
            // Move o tronco para a direita
            setPx(getPx() + getVel());
            // Se o tronco sair da tela à direita, reinicia à esquerda
            if (getPx() > 800) {
                setPx(-getLargura());
            }
        }
    }

    // Desenha o tronco na tela
    @Override
    public void desenha(Graphics2D g) {
        g.setColor(Color.ORANGE); // Define a cor do tronco
        g.fillRect((int) getPx(), (int) getPy(), getLargura(), getAltura()); // Desenha o retângulo do tronco
    }
}
