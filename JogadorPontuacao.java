package br.com.mvbos.lgj.base;


public class JogadorPontuacao implements Comparable<JogadorPontuacao> {
    private String nome;
    private int pontos;

    public JogadorPontuacao(String nome, int pontos) {
        this.nome = nome;
        this.pontos = pontos;
    }

    public String getNome() {
        return nome;
    }

    public int getPontos() {
        return pontos;
    }

    @Override
    public int compareTo(JogadorPontuacao o) {
        // Primeiro, comparamos as pontuações
        int comparacaoPontuacao = Integer.compare(o.pontos, this.pontos);

        // Se as pontuações forem iguais, desempatamos pelo nome
        if (comparacaoPontuacao == 0) {
            return this.nome.compareTo(o.nome);
        }

        // Se as pontuações forem diferentes, retornamos a comparação das pontuações
        return comparacaoPontuacao;
    }

    @Override
    public String toString() {
        return nome + " - " + pontos + " pontos";
    }
}