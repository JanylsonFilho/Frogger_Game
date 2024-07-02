package br.com.mvbos.lgj.base;


import java.awt.Color;
import java.awt.Graphics2D;

public class Veiculo extends Elemento {
    public Veiculo(int px, int py, int largura, int altura) {
        super(px, py, largura, altura);
        setCor(Color.RED);
        setVel(5);
        setAtivo(true); // Certificar-se de que os veículos estão ativos
    }

    @Override
    public void atualiza() {
        incPx(getVel());
        if (getPx() > 800) {
            setPx(-getLargura());
        }
    }

    @Override
    public void desenha(Graphics2D g) {
        super.desenha(g);
    }
}