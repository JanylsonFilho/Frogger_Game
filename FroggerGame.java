package br.com.mvbos.lgj.base;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.TreeSet;

public class FroggerGame extends JPanel implements ActionListener {
    private Timer timer;
    private Jogador jogador;
    private Veiculo[] veiculos;
    private Tronco[] troncos;
    private CenarioPadrao cenario;
    private static final int LARGURA_JANELA = 800;
    private static final int ALTURA_JANELA = 600;
    private long tempoInicial;
    private int pontos;

    private TreeSet<JogadorPontuacao> ranking;
    private static final String RANKING_FILE = "ranking.txt";
    private String nomeJogador;

    public FroggerGame() {
        ranking = new TreeSet<>();
        carregarRanking(); // Carrega o ranking ao iniciar o jogo
        initGame();
    }

    private void initGame() {
        setPreferredSize(new Dimension(LARGURA_JANELA, ALTURA_JANELA));
        setBackground(Color.BLACK);
        jogador = new Jogador(400, 550, 30, 30); // Cria o jogador
        jogador.setGame(this); // Define o jogo para o jogador

        veiculos = new Veiculo[10]; // Cria os veículos
        for (int i = 0; i < veiculos.length; i++) {
            veiculos[i] = new Veiculo((i * 80) % LARGURA_JANELA, 300 + (i % 4) * 50, 50, 30);
        }

        troncos = new Tronco[13]; // Cria os troncos
        for (int i = 0; i < troncos.length; i++) {
            boolean daDireitaParaEsquerda = (i % 2 == 0);
            int x = daDireitaParaEsquerda ? 800 : -100; // Define a posição inicial dependendo da direção
            troncos[i] = new Tronco((x + i * 160) % LARGURA_JANELA, 50 + ((i % 4) * 50), 100, 30, daDireitaParaEsquerda);
        }

        cenario = new CenarioFrogger(LARGURA_JANELA, ALTURA_JANELA); // Cria o cenário
        pontos = 0;
        tempoInicial = System.currentTimeMillis();
        timer = new Timer(16, this); // Configura o timer para atualização
        timer.start();

        setFocusable(true);
        addKeyListener(jogador); // Adiciona o KeyListener do jogador
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        jogador.atualiza();
        boolean sobreTronco = false;

        // Atualiza e verifica colisões com veículos
        for (Veiculo veiculo : veiculos) {
            veiculo.atualiza();
            if (Util.colide(jogador, veiculo) && jogador.getPy() > 295) {
                jogador.reset();
                pontos = 0; // Reseta os pontos se o jogador colidir com um veículo
            }
        }

        // Atualiza e verifica colisões com troncos
        for (Tronco tronco : troncos) {
            tronco.atualiza();
            if (Util.colide_com_tronco(jogador, tronco)) {
                sobreTronco = true;
                if (!jogador.verifica_mov_jogador) {
                    // Se o jogador não estiver se movimentando, ele se move junto com o tronco
                    int ponto_futuro_do_tronco = tronco.getPx() + (tronco.isDaDireitaParaEsquerda() ? -tronco.getVel() : tronco.getVel());
                    int ponto_atual_do_jogador_no_tronco = jogador.getPx() - tronco.getPx();
                    int ponto_futuro_do_jogador_no_tronco = ponto_futuro_do_tronco + ponto_atual_do_jogador_no_tronco;
                    jogador.setPx(ponto_futuro_do_jogador_no_tronco);
                }
                if (jogador.getPx() >= 800) {
                    jogador.reset();
                }
            }
        }

        jogador.setSobreTronco(sobreTronco);

        // Condições de reset no lago
        if (jogador.getPy() <= 200 && !sobreTronco && jogador.getPy() >= 50) {
            jogador.reset();
            pontos = 0;
        }
        if (jogador.getPx() >= 800 || jogador.getPx() <= 0 || jogador.getPy() > 550) {
            jogador.reset();
            pontos = 0;
        }

        // Verifica se o jogador venceu
        if (jogador.getPy() < 50) {
            long tempoFinal = System.currentTimeMillis();
            long tempoDecorrido = (tempoFinal - tempoInicial) / 1000; // Tempo em segundos
            int pontosFinais = pontos * (int) (1000 / tempoDecorrido);
            jogador.reset();
            JOptionPane.showMessageDialog(this, "Você venceu!");

            nomeJogador = JOptionPane.showInputDialog("Digite seu nome:");
            if (nomeJogador == null || nomeJogador.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nome inválido! Encerrando o jogo.");
                System.exit(0); // Encerra o jogo se o nome for inválido
            }
            JOptionPane.showMessageDialog(this, nomeJogador + " - " + pontosFinais);

            atualizarRanking(new JogadorPontuacao(nomeJogador, pontosFinais));
            exibirRanking();
            pontos = 0;
            tempoInicial = System.currentTimeMillis();
            timer.stop();

            salvarRanking();
            jogador.setGame(null);
            initGame();
        }

        repaint(); // Re-desenha o componente
    }

    // Salva o ranking em um arquivo
    private void salvarRanking() {
        File arquivo = new File(RANKING_FILE);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo))) {
            for (JogadorPontuacao jp : ranking) {
                bw.write(jp.getNome() + " - " + jp.getPontos() + " pontos");
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar o ranking: " + e.getMessage());
        }
    }

    // Carrega o ranking de um arquivo
    private void carregarRanking() {
        File arquivo = new File(RANKING_FILE);
        if (!arquivo.exists()) {
            return; // Não faz nada se o arquivo não existir
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(" - ");
                String nome = partes[0];
                int pontos = Integer.parseInt(partes[1].split(" ")[0]);
                ranking.add(new JogadorPontuacao(nome, pontos));
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar o ranking: " + e.getMessage());
        }
    }

    // Atualiza o ranking com a nova pontuação
    private void atualizarRanking(JogadorPontuacao jp) {
        ranking.add(jp);
        if (ranking.size() > 10) {
            ranking.pollLast(); // Remove o último se houver mais de 10 jogadores no ranking
        }
    }

    // Exibe o ranking
    private void exibirRanking() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ranking:\n");
        int posicao = 1;
        for (JogadorPontuacao jp : ranking) {
            sb.append(posicao++).append(". ").append(jp).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        cenario.desenhar(g2d); // Desenha o cenário
        for (Veiculo veiculo : veiculos) {
            veiculo.desenha(g2d); // Desenha os veículos
        }
        for (Tronco tronco : troncos) {
            tronco.desenha(g2d); // Desenha os troncos
        }
        jogador.desenha(g2d); // Desenha o jogador
        g2d.setColor(Color.WHITE);
        g2d.drawString("Pontos: " + pontos, 10, 20); // Exibe a pontuação
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Frogger");
        FroggerGame jogo = new FroggerGame();
        frame.add(jogo);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void incrementaPontos() {
        pontos += 10; // Incrementa a pontuação
    }

    public void decrementaPontos() {
        pontos -= 10; // Decrementa a pontuação
    }
}
