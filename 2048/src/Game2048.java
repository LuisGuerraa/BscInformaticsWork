import isel.leic.pg.Console;

import java.awt.event.KeyEvent;
import java.util.Random;

public class Game2048 {
    static final int LINES = 4, COLS = 4, JOIN = 4;
    private static final int EMPTY_TILE =0 ;
    private static int[][] tiles = new int[LINES][COLS];
    private static int [] [] tilesmask = new int [LINES][COLS];

    static final int MIN_VALUE = 2, MAX_VALUE = 2048;

    private static TopScores top = new TopScores();//referencia para o objeto da classe TopScores

    private static int moves = 0;
    private static boolean exit = false;
    private static int key;
    private static int resultado;


    public static void main(String[] args) {
        Panel.open();
        init();
        for (; ; ) {
            key = Console.waitKeyPressed(0);
            if (!processKey(key)) break;
            while (Console.isKeyPressed(key)) ;
        }
        Panel.close();
        top.saveToFile();
    }

    private static void RandomTile() {

            int l = (int) (Math.random() * LINES); //gerar numa linha random
            int c = (int) (Math.random() * COLS);//gerar numa coluna random
            if (tiles[l][c] != 0) {//se nessa posição ja estiver alguma
                if (checkEnd2048()) {//se ja houver endgame em que o jogador ja chegasse ao 2048
                    Name();
                    newGame();
                } else
                    RandomTile();// se nessa posição ja estivesse ocupada geraria noutra ate encontrar uma vazia
            } else {
                int k = (int) (Math.random() * 10);
                if (k == 1) {
                    tiles[l][c] = 4;
                } else {
                    tiles[l][c] = 2;
                }
                Panel.showTile(l, c, tiles[l][c]);//mostrar a peça
            }
        }


    private static void drawTiles() {
        for (int i = 0; i < LINES; i++) {
            for (int j = 0; j < COLS; j++) {
                if (tiles[i][j] != 0) {
                    Panel.showTile(i, j, tiles[i][j]);
                }
            }
        }
    }

    private static void init() {
        Panel.showMessage("Use cursor keys to play");
        RandomTile();
        RandomTile();
        Panel.updateMoves(moves);
        Panel.updateScore(0);
        for (int i = 0; i < top.getNumOfRows(); i++) {// tabela de scores
            Panel.updateBestRow(i+1, top.getRow(i).getName(), top.getRow(i).getPoints());
        }
    }


    private static boolean processKey(int key) {
        switch (key) {
            case KeyEvent.VK_UP:
                compactUp();
                move();
                break;
            case KeyEvent.VK_DOWN:
                compactDown();
                move();
                break;
            case KeyEvent.VK_LEFT:
                compactLeft();
                move();
                break;
            case KeyEvent.VK_RIGHT:
                compactRight();
                move();
                break;
            case KeyEvent.VK_ESCAPE:
                quitGame();
            case 'Q':
                quitGame();
                break;
            case 'N':
                newGame();
                break;
            case KeyEvent.VK_BACK_SPACE:
                for (int i = 0; i < LINES; i++) {
                    for (int j = 0; j < COLS; j++) {
                        tiles[i][j] = tilesmask[i][j];
                        Panel.showTile(i, j, tiles[i][j]);
                        if (tiles[i][j] == 0)
                            Panel.hideTile(i, j);
                        break;

        }

    }

            }
        return !exit;
        }



    private static boolean gameover() { // verificar se há a condição de gameover(tudo cheio sem moves para realizar)
        for (int i = 0; i < LINES; i++) {
            for (int j = 0; j < COLS; j++) {
                if (tiles[i][j] == EMPTY_TILE) return false;              //Quando há um espaço sem peça, o jogo não acaba
            }
        }

        for (int l = 0; l < LINES-1; l++) {
            for (int c = 0; c < COLS; c++) {
                if(tiles[l][c]==tiles[l+l][c]) return false;     //Quando há uma peça com o mesmo número na próxima linha, o jogo não acaba
            }
        }

        for (int l = 0; l < LINES; l++) {
            for (int c = 0; c < COLS-1; c++) {
                if(tiles[l][c]==tiles[l][c+l]) return false;     //Quando há uma peça com o mesmo número na próxima coluna, o jogo não acaba
            }
        }

        return true;
    }

    private static void quitGame() {//em caso de "Q"
        int resp = Panel.questionChar("Exit game (Y/N)");
        if (resp == 'Y'){
            if(resultado > 0)//se houver pontuação ja realizada pelo jogador irá pedir o seu nome e ver se puderá constar na tabela
               Name();
            exit = true;//fechar o jogo
        }
        if (resp == 'N') { // nao muda nada
            for (int i = 0; i < LINES; i++) {
                for (int j = 0; j < COLS; j++) {
                    if (tiles[i][j] != 0) {
                        exit = false;
                    }
                }
            }
        }

        key = 0;
    }

    private static void newGame() { // em caso de "N"
        int ng = Panel.questionChar("New game? (Y/N)");
        if (ng == 'Y') {
            if(resultado>0)Name();
            for (int i = 0; i < LINES; i++) {
                for (int j = 0; j < COLS; j++) {
                    tiles[i][j] = 0;
                    Panel.hideTile(i, j);//esconder todas as tiles
                }
            }

            moves = 0;//reset aos moves
            resultado = 0;// reset ao resultado
            init(); // processo de iniciação de novo jogo

        }
        if (ng == 'N') {
            for (int i = 0; i < LINES; i++) {
                for (int j = 0; j < COLS; j++) {
                    if (tiles[i][j] != 0) {
                        exit = false;
                    }
                }
            }
            key = 0;
            if(checkEnd2048()) exit=true; // quando o utilizador chega ao 2048 e fizer a pergunta de new game e disser que nao automaticamente fecha o jogo
        }
    }

    private static void move() {
        Panel.updateMoves(++moves);
        Panel.updateScore(resultado);
        if(gameover()){ // em caso de gameover
            Panel.showTempMessage("You lose. Score = "+resultado, 2000);
            Name();
            newGame();

        }
        RandomTile();//geração de uma tile numa posição random
        drawTiles();//desenho dessa peça

    }


    private static void moveUp() {//movimento ascendente
        int temporario = 0;
        int indextemporario = 0;
        for (int i = 0; i < JOIN; i++) { //percorrer 4 vezes

            for (int coluna = 0; coluna < COLS; coluna++) {// por coluna
                temporario = tiles[0][coluna];
                indextemporario = 0;

                for (int linha = 1; linha < LINES; linha++) {//por linha
                    if (temporario == 0 && tiles[linha][coluna] != 0) {//se a posição estiver vazia
                        tiles[indextemporario][coluna] = tiles[linha][coluna];//troca a posição da tile
                        tiles[linha][coluna] = 0;//poe vazia a posiçao em baixo
                    }
                    temporario = tiles[linha][coluna];//passa ao proximo
                    indextemporario = linha;
                }
            }
        }
    }

    private static void moveDown() {//movimento descendente
        int temporario = 0;
        int indextemporario = 0;
        for (int i = 0; i < JOIN; i++) {//percorrer 4 vezes
            // por cada coluna
            for (int coluna = COLS-1; coluna >= 0; coluna--) {// por coluna
                temporario = tiles[3][coluna];
                indextemporario = 3;
                // por cada linha
                for (int linha = LINES-2; linha >= 0; linha--) {//por linha
                    if (temporario == 0 && tiles[linha][coluna] != 0) {//se a posição estiver vazia
                        tiles[indextemporario][coluna] = tiles[linha][coluna];//troca a posição da tile
                        tiles[linha][coluna] = 0;//poe vazia a posiçao em cima
                    }
                    temporario = tiles[linha][coluna];//passa ao proximo
                    indextemporario = linha;
                }
            }
        }
    }

    private static void moveRight() {//movimento para a direita
        int temporario = 0;
        int indextemporario = 0;
        for (int i = 0; i < JOIN; i++) {//percorrer 4 vezes
            // por cada linha
            for (int linha = LINES-1; linha >= 0; linha--) {// por linha
                temporario = tiles[linha][3];
                indextemporario = 3;
                // por cada coluna
                for (int coluna = COLS-2; coluna >= 0; coluna--) {//por coluna
                    if (temporario == 0 && tiles[linha][coluna] != 0) {//se a posição estiver vazia
                        tiles[linha][indextemporario] = tiles[linha][coluna];//troca a posição da tile
                        tiles[linha][coluna] = 0;//poe vazia a posiçao á esquerda
                    }
                    temporario = tiles[linha][coluna];//passa ao proximo
                    indextemporario = coluna;
                }
            }
        }
    }

    private static void moveLeft() {//movimento para a esquerda
        int temporario = 0;
        int indextemporario = 0;
        for (int i = 0; i < JOIN; i++) {//percorrer 4 vezes
            // por cada linha
            for (int linha = 0; linha < LINES; linha++) {//por linha
                temporario = tiles[linha][0];
                indextemporario = 0;
                // por cada coluna
                for (int coluna = 1; coluna < COLS; coluna++) {//por coluna
                    if (temporario == 0 && tiles[linha][coluna] != 0) {//se a posição estiver vazia
                        tiles[linha][indextemporario] = tiles[linha][coluna];//troca a posição da tile
                        tiles[linha][coluna] = 0;//poe vazia a posiçao á direita
                    }
                    temporario = tiles[linha][coluna];//passa ao proximo
                    indextemporario = coluna;
                }
            }
        }
    }

    private static void joinUp(){//ajuntamento de peças para cima
        int temporario = 0;
        int indextemporario = 0;
        // por cada coluna
        for (int coluna = 0; coluna < COLS; coluna++) {//por coluna
            temporario = tiles[0][coluna];
            indextemporario = 0;
            // por cada linha
            for (int linha = 1; linha < LINES; linha++) {//por linha
                if (temporario == tiles[linha][coluna]) {//se encontrar iguais
                    tiles[indextemporario][coluna] = temporario * 2;//multiplica a tile para onde vai por 2
                    resultado += tiles[indextemporario][coluna];//incrementa ao resultado
                    tiles[linha][coluna] = 0;//poe vazia a posição em baixo
                }
                temporario = tiles[linha][coluna];//passa ao proximo
                indextemporario = linha;
            }
        }
    }

    private static void joinDown(){//ajuntamento de peças para baixo
        int temporario = 0;
        int indextemporario = 0;
        // por cada coluna
        for (int coluna = COLS-1; coluna >= 0; coluna--) {//por coluna
            temporario = tiles[3][coluna];
            indextemporario = 3;
            // por cada linha
            for (int linha = LINES-2; linha >= 0; linha--) {//por linha
                if (temporario == tiles[linha][coluna]) {//se encontrar iguais
                    tiles[indextemporario][coluna] = temporario * 2;//multiplica a tile para onde vai por 2
                    resultado += tiles[indextemporario][coluna];//incrementa ao resultado
                    tiles[linha][coluna] = 0;//poe vazia a posição em cima
                }
                temporario = tiles[linha][coluna];//passa ao proximo
                indextemporario = linha;
            }
        }
    }

    private static void joinRight() {//ajuntamento de peças para a direita
        int temporario = 0;
        int indextemporario = 0;
        // por cada linha
        for (int linha = LINES-1; linha >= 0; linha--) {//por linha
            temporario = tiles[linha][3];
            indextemporario = 3;
            // por cada coluna
            for (int coluna = COLS-2; coluna >= 0; coluna--) {//por coluna
                if (temporario == tiles[linha][coluna]) {//se encontrar iguais
                    tiles[linha][indextemporario] = temporario * 2;//multiplica a tile para onde vai por 2
                    resultado += tiles[linha][indextemporario];//incrementa ao resultado
                    tiles[linha][coluna] = 0;//poe vazia a posição á esquerda
                }
                temporario = tiles[linha][coluna];//passa ao proximo
                indextemporario = coluna;
            }
        }
    }

    private static void joinLeft() {//ajuntamento de peças para a esquerda
        int temporario = 0;
        int indextemporario = 0;
        // por cada linha
        for (int linha = 0; linha < LINES; linha++) {//por linha
            temporario = tiles[linha][0];
            indextemporario = 0;
            // por cada coluna
            for (int coluna = 1; coluna < COLS; coluna++) {//por coluna
                if (temporario == tiles[linha][coluna]) {//se encontrar iguais
                    tiles[linha][indextemporario] = temporario * 2;//multiplica a tile para onde vai por 2
                    resultado += tiles[linha][indextemporario];//incrementa ao resultado
                    tiles[linha][coluna] = 0;//poe vazia a posição á direita
                }
                temporario = tiles[linha][coluna];//passa ao proximo
                indextemporario = coluna;
            }
        }
    }

    private static void compactUp() { //processo completo de pressionar para cima
        newMatrix();
        moveUp();
        joinUp();
        moveUp();

        fixTile();
    }

    private static void compactDown() {//processo completo de pressionar para baixo
        newMatrix();
        moveDown();
        joinDown();
        moveDown();

        fixTile();
    }

    private static void compactRight() {//processo completo de pressionar para a direita
        newMatrix();
        moveRight();
        joinRight();
        moveRight();

        fixTile();
    }

    private static void compactLeft() {//processo completo de pressionar para a esquerda
        newMatrix();
        moveLeft();
        joinLeft();
        moveLeft();

        fixTile();
    }

    private static void fixTile(){ // metodo para nao por as posiçoes que ficam sem tile nos compacts a branco
        for (int linha = 0; linha < LINES; linha++) {//voltar a por a 0 (bug de ficar branco)
            // por cada linha
            for (int coluna = 0; coluna < COLS; coluna++) {
                int tile = tiles[linha][coluna];
                if (tile == 0) {
                    Panel.hideTile(linha, coluna);
                } else {
                    Panel.showTile(linha, coluna, tile);
                }
            }
        }
    }

    private static void Name(){
        String name = Panel.questionName("Your Name");//utilização de um metodo da classe panel para perguntar o nome e retorna lo em string
        top.addRow(name,resultado);//adicionar á tabela de resultados
        top.saveToFile();//guardar o resultado no ficheiro para ficar sempre que jogamos
}

    private static boolean checkEnd2048 () {//verificar se ha alguma peça com o numero de 2048, chegou ao fim
        boolean end = false;
        for (int i = 0; i < LINES; i++) {
            for (int j = 0; j < COLS; j++) {
                if (tiles[i][j] == MAX_VALUE) {
                    end = true;
                    }
            }
        }
        return end;
    }

    private static void newMatrix(){
        for (int l = 0; l < LINES; l++) {
            for (int c = 0; c <COLS ; c++) {
                tilesmask[l][c]=tiles[l][c];

            }
        }
    }



}