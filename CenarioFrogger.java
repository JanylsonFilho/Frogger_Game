package br.com.mvbos.lgj.base;

import java.awt.Color;
import java.awt.Graphics2D;

public class CenarioFrogger extends CenarioPadrao {

    // Construtor do cenário Frogger, inicializa as propriedades
    public CenarioFrogger(int largura, int altura) {
        super(largura, altura);
    }

    @Override
    public void carregar() {
        // Carregar recursos se necessário
    }

    @Override
    public void descarregar() {
        // Descarregar recursos se necessário
    }

    @Override
    public void atualizar() {
        // Atualizar lógica do cenário se necessário
    }

    @Override
    public void desenhar(Graphics2D g) {
        // Desenha a parte da rua
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, largura, altura / 2);

        // Desenha a base do meio
        g.setColor(Color.GRAY);
        g.fillRect(0, altura / 2 - 50, largura, 50);

        // Desenha a parte do lago
        g.setColor(Color.BLACK);
        g.fillRect(0, altura / 2, largura, altura / 2 - 50);

        // Desenha a linha de início
        g.setColor(Color.CYAN);
        g.fillRect(0, altura - 50, largura, 50);

        // Desenha a linha de chegada
        g.setColor(Color.PINK);
        g.fillRect(0, altura - 598, largura, 50);
    }
}
