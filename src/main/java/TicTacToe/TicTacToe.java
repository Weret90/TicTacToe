package TicTacToe;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe {
    private static char[][] field;
    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = 'O';
    private static final char DOT_EMPTY = '.';
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RANDOM = new Random();
    private static final int SIZE = 5;
    private static final int DOT_TO_WIN = 4;

    public static void main(String[] args) {
        while (true) {
            initField();
            printField();

            while (true) {
                humanTurn();
                printField();
                if (checkGame(DOT_HUMAN, "Human wins!")) break;
                aiTurn();
                printField();
                if (checkGame(DOT_AI, "AI win!")) break;
            }
            System.out.println("Play again? Write 'yes' if you want");
            if (!SCANNER.next().equals("yes")) break;
        }
    }

    private static void humanTurn() {
        int x;
        int y;
        do {
            System.out.println("Введите координаты хода X и Y от 1 до 3 через пробел ->");
            x = SCANNER.nextInt() - 1;
            y = SCANNER.nextInt() - 1;
        } while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[y][x] = DOT_HUMAN;
    }

    private static void aiTurn() { //немного усовершенствовал АИ - он старается сразу занять центр и старается блокировать ходы игрока по вертикали или горизонтали если там у игрока 2 или более фишки
        int x;
        int y;
        int blockY = -1;
        int blockX = -1;
        for (int i = 0; i < SIZE; i++) {
            int countX = 0;
            int countY = 0;
            for (int j = 0; j < SIZE; j++) {
                if (field[i][j] == DOT_HUMAN) {
                    countX++;
                    if (countX == DOT_TO_WIN - 2) { //если игрок начинает заполнять горизонталь то запоминаем индекс i этой горизонтали (blockY)
                        blockY = i;
                    }
                }
                if (field[j][i] == DOT_HUMAN) {
                    countY++;
                    if (countY == DOT_TO_WIN - 2) { //если игрок начинает заполнять вертикаль то запоминаем индекс i этой вертикали i (blockX)
                        blockX = i;
                    }
                }
            }

        }
        do {
            if (field[2][2] == DOT_EMPTY) { // если центр поля свободен, комп сразу ее занимает
                x = 2;
                y = 2;
            } else if (blockX != -1) {
                x = blockX;
                y = RANDOM.nextInt(SIZE); // блокируем вертикаль которую заполяет игрок (если таковая есть)
            } else if (blockY != -1) {
                y = blockY;
                x = RANDOM.nextInt(SIZE); //блокируем горизонталь которую заполняет игрок (если таковая есть)
            } else {
                x = RANDOM.nextInt(SIZE);
                y = RANDOM.nextInt(SIZE);
            }
        } while (!isCellEmpty(x, y));
        field[y][x] = DOT_AI;
    }

    private static boolean checkGame(char dot, String s) {
        if (checkWin(dot)) {
            System.out.println(s);
            return true;
        }
        if (checkDraw()) {
            System.out.println("Draw!");
            return true;
        }
        return false;
    }

    private static boolean checkWin(char symb) {
        for (int i = 0; i < SIZE; i++) { // проверка победы по оси Х и Y (по горизонтали или вертикали)
            int countX = 0;
            int countY = 0;
            for (int j = 0; j < SIZE; j++) {
                if (field[i][j] == symb) {
                    countX++;
                }
                if (field[j][i] == symb) {
                    countY++;
                }
            }
            if (countX == DOT_TO_WIN || countY == DOT_TO_WIN) {
                return true;
            }
        }

        int countDiag1 = 0; //проверка победы по диагоналям для поля 5х5 используя циклы
        int countDiag2 = 0;
        int countDiag3 = 0;
        int countDiag4 = 0;
        int countDiag5 = 0;
        int countDiag6 = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (field[i][j] == symb && i + j == field.length - 1) {
                    countDiag1++;
                }
                if (field[i][j] == symb && i == j) {
                    countDiag2++;
                }
                if (field[i][j] == symb && i + j == field.length) {
                    countDiag3++;
                }
                if (field[i][j] == symb && i + j == DOT_TO_WIN - 1) {
                    countDiag4++;
                }
                if (field[i][j] == symb && j - i == field.length - DOT_TO_WIN) {
                    countDiag5++;
                }
                if (field[i][j] == symb && i - j == field.length - DOT_TO_WIN) {
                    countDiag6++;
                }
                if (countDiag1 == DOT_TO_WIN || countDiag2 == DOT_TO_WIN || countDiag3 == DOT_TO_WIN || countDiag4 == DOT_TO_WIN || countDiag5 == DOT_TO_WIN || countDiag6 == DOT_TO_WIN) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkDraw() {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                if (isCellEmpty(x, y)) return false;
            }
        }
        return true;
    }

    private static void initField() {
        field = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                field[i][j] = DOT_EMPTY;
            }
        }
    }

    private static void printField() {
        for (int i = 0; i <= SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static boolean isCellEmpty(int x, int y) {
        return field[y][x] == DOT_EMPTY;
    }

    private static boolean isCellValid(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE;
    }
}

