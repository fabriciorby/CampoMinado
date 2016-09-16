/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package campominado;

import java.util.Scanner;
import java.util.Random;

/**
 *
 * @author Fabricio
 */
public class CampoMinado {

    static int col = 9;
    static int row = 9;
    static int minas = 10;
    static char[][] campo;
    static Scanner sc = new Scanner(System.in);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Bem vindo ao Campo Minado");
        menu();
    }

    //menu inicial
    public static void menu() {
        System.out.println("1 - Iniciar");
        System.out.println("2 - Configuração");
        System.out.println("3 - Ajuda");
        System.out.println("4 - Sair");
        int opcao = sc.nextInt();
        switch (opcao) {
            case 1:
                jogo();
                break;
            case 2:
                configuracao();
                break;
            case 3:
                ajuda();
                break;
            case 4:
                System.out.println("Até mais, obrigado por jogar Campo Minado.");
                break;
            default:
                System.out.println("Comando inválido.");
                menu();
        }
    }
    
    //configurações de dificuldade
    public static void configuracao() {
        System.out.println("Qual dificuldade?");
        System.out.println("1 - Fácil");
        System.out.println("2 - Médio");
        System.out.println("3 - Difícil");
        int dificuldade = sc.nextInt();
        switch (dificuldade) {
            case 1:
                col = 9;
                row = 9;
                minas = 10;
                break;
            case 2:
                col = 16;
                row = 16;
                minas = 40;
                break;
            case 3:
                col = 30;
                row = 16;
                minas = 99;
                break;
            default:
                System.out.println("Comando inválido.");
                configuracao();
        }
        menu();
    }

    //regras do jogo
    public static void ajuda() {
        System.out.println("Campo minado é um popular jogo de computador para"
                + "um jogador. Foi inventado por Robert Donner em 1989 e tem "
                + "como objectivo revelar um campo de minas sem que alguma seja "
                + "detonada.");
        System.out.println("A área de jogo consiste num campo de quadrados "
                + "retangular. Cada quadrado pode ser revelado clicando sobre "
                + "ele, e se o quadrado clicado contiver uma mina, então o jogo "
                + "acaba. Se, por outro lado, o quadrado não contiver uma mina, "
                + "uma de duas coisas poderá acontecer:");
        System.out.println("Um número aparece, "
                + "indicando a quantidade de quadrados adjacentes que contêm "
                + "minas;");
        System.out.println("Nenhum número aparece. Neste caso, o jogo revela "
                + "automaticamente os quadrados que se encontram adjacentes ao "
                + "quadrado vazio, já que não podem conter minas;");
        System.out.println("O jogo é ganho quando todos os quadrados que não "
                + "têm minas são revelados.");
        System.out.println("Entre com um inteiro para voltar: ");
        sc.nextInt();
        menu();
    }

    //começa o jogo
    public static void jogo() {
        char[][] novoCampo = new char[row][col];
        iniciaCampo(novoCampo);
        desenhaCampo(novoCampo);
        System.out.println("Digite as coordenadas que você gostaria de testar: (x y)");
        int x = sc.nextInt();
        int y = sc.nextInt();
        //verifica se a coordenada é válida
        while (!validaCoordenada(y, x)) {
            System.out.println("Coordenadas incorretas!");
            System.out.println("Digite as coordenadas que você gostaria de testar: (x y)");
            x = sc.nextInt();
            y = sc.nextInt();
        }
        //faz a validação para ter certeza que o jogador não comece o jogo perdendo
        do {
            inicio();
        } while (temMina(y, x) == 1);
        do {
            if (campo[y][x] == '-') {
                temVazio(y, x, novoCampo);
            }
            novoCampo[y][x] = campo[y][x];
            if (voceGanhou(novoCampo)) {
                break;
            }
            desenhaCampo(novoCampo);
            System.out.println("Digite as coordenadas que você gostaria de testar: (x y)");
            x = sc.nextInt();
            y = sc.nextInt();
            //verifica se a coordenada é válida ou se já foi utilizada
            while (!validaCoordenada(y, x) || novoCampo[y][x] != ' ') {
                System.out.println("Coordenadas incorretas!");
                System.out.println("Digite as coordenadas que você gostaria de testar: (x y)");
                x = sc.nextInt();
                y = sc.nextInt();
            }
        } while (temMina(y, x) == 0);
        desenhaCampo(campo);
        if (temMina(y, x) == 1) {
            System.out.println("Você morreu!");
        } else {
            System.out.println("Parabéns, você ganhou!");
        }
        System.out.println("Entre com um inteiro para voltar: ");
        sc.nextInt();
        System.out.print("\n");
        menu();
    }

    public static void inicio() {
        campo = new char[row][col];
        //inicia o campo com espaços em branco
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                campo[y][x] = '-';
            }
        }
        //coloca as bombas
        int contMinas = 0;
        Random random = new Random();
        while (contMinas < minas) {
            int x = random.nextInt(col); // gera um número entre 0 e col - 1
            int y = random.nextInt(row); // gera um número entre 0 e row - 1
            // não se pode colocar 2 minas no mesmo lugar
            if (campo[y][x] != '*') {
                campo[y][x] = '*';
                contMinas++;
            }
        }
        //coloca as dicas/números
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                if (campo[y][x] != '*') {
                    campo[y][x] = quantasMinas(y, x);
                }
            }
        }
    }

    //inicia o campo com espaços em branco
    public static void iniciaCampo(char[][] a) {
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                a[y][x] = ' ';
            }
        }
    }

    //checa quantas minas tem ao redor
    public static char quantasMinas(int y, int x) {
        int contMinas = 0;
        contMinas += temMina(y - 1, x - 1);  // noroeste
        contMinas += temMina(y - 1, x);      // norte
        contMinas += temMina(y - 1, x + 1);  // nordeste
        contMinas += temMina(y, x - 1);      // oeste
        contMinas += temMina(y, x + 1);      // leste
        contMinas += temMina(y + 1, x - 1);  // sudoeste
        contMinas += temMina(y + 1, x);      // sul
        contMinas += temMina(y + 1, x + 1);  // sudeste
        if (contMinas > 0) {
            return (char) (contMinas + 48);
        } else {
            return '-';
        }
    }

    public static void temVazio(int y, int x, char[][] novoCampo) {
        //aqui aplicamos uma variante do algoritmo recursivo de flood fill
        //onde estiver vazio ele irá completar dentro da matriz se estiver próximo
        if (campo[y][x] != '-') {
            novoCampo[y][x] = campo[y][x];
            return;
        } else if (novoCampo[y][x] == '-') {
            return;
        } else {
            novoCampo[y][x] = '-';
        }
        if (y + 1 < row) {
            temVazio(y + 1, x, novoCampo);
        }
        if (y - 1 >= 0) {
            temVazio(y - 1, x, novoCampo);
        }
        if (x + 1 < col) {
            temVazio(y, x + 1, novoCampo);
        }
        if (x - 1 >= 0) {
            temVazio(y, x - 1, novoCampo);
        }
    }

    //checa se tem mina no local
    public static int temMina(int y, int x) {
        if (y >= 0 && y < row && x >= 0 && x < col) {
            if (campo[y][x] == '*') {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    //imprime o campo no console
    public static void desenhaCampo(char[][] a) {
        System.out.printf("    ");
        for (int x = 0; x < col; x++) {
            System.out.printf("%4d", x);
        }
        System.out.print("\n");
        for (int y = 0; y < row; y++) {
            System.out.printf("%4d", y);
            for (int x = 0; x < col; x++) {
                System.out.printf("%4c", a[y][x]);
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    //checa se a coordenada é válida para evitar array out of boundaries
    public static boolean validaCoordenada(int y, int x) {
        return y >= 0 && y < row && x >= 0 && x < col;
    }

    //checa se o número de casas em branco são iguais ao número de minas
    //se for, então você ganhou o jogo
    public static boolean voceGanhou(char[][] novoCampo) {
        int cont = 0;
        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                if (novoCampo[y][x] == ' ') {
                    cont++;
                }
            }
        }
        return cont == minas;
    }
}
