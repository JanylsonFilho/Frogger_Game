package br.com.mvbos.lgj.base;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Jogador extends Elemento implements KeyListener {
    private boolean sobreTronco; // Indica se o jogador está sobre um tronco
    private FroggerGame game; // Referência ao jogo Frogger
    public boolean verifica_mov_jogador; // Indica se o jogador está se movendo

    // Construtor do jogador, inicializa as propriedades
    public Jogador(int px, int py, int largura, int altura) {
        super(px, py, largura, altura);
        setCor(Color.GREEN); // Define a cor do jogador
        setAtivo(true); // Define o jogador como ativo
        sobreTronco = false;
        verifica_mov_jogador = false;
    }

    // Obtém a referência ao jogo Frogger
    public FroggerGame getGame() {
        return game;
    }

    // Define a referência ao jogo Frogger
    public void setGame(FroggerGame game) {
        this.game = game;
    }

    // Reseta a posição do jogador e seus estados
    public void reset() {
        setPx(400);
        setPy(550);
        sobreTronco = false;
        verifica_mov_jogador = false;
    }

    // Define se o jogador está sobre um tronco
    public void setSobreTronco(boolean sobreTronco) {
        this.sobreTronco = sobreTronco;
    }

    @Override
    public void atualiza() {
        // A lógica de atualização será realizada no actionPerformed de FroggerGame
    }

    @Override
    public void desenha(Graphics2D g) {
        super.desenha(g); // Desenha o jogador na tela
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            incPx(-20);
            verifica_mov_jogador = true;
        } else if (key == KeyEvent.VK_RIGHT) {
            incPx(20);
            verifica_mov_jogador = true;
        } else if (key == KeyEvent.VK_UP) {
            incPy(-50);
            verifica_mov_jogador = true;
            if (game != null) {
                game.incrementaPontos(); // Incrementa os pontos a cada movimento para frente
                System.out.println(getPx() + " " + getPy());
            }
        } else if (key == KeyEvent.VK_DOWN) {
            incPy(50); // Move o jogador para baixo
            verifica_mov_jogador = true;
            if (game != null) {
                game.decrementaPontos(); // Decrementa os pontos a cada movimento para trás
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
            verifica_mov_jogador = false; // Para o movimento do jogador ao soltar a tecla
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Não utilizado
    }
}
