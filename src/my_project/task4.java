package my_project;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class task4 {

    public static int i, j;
    public static int SIZE = 5;
    public static int DOTS_TO_WIN = 4;
    public static final char DOT_EMPTY = '•';
    public static final char DOT_X = 'X';
    public static final char DOT_O = 'O';
    public static char[][] map;
    public static int size;  // Размер поля
    public static int block; // Сколько должно быть одинаковых значение подряд для победы
    public static Scanner sc = new Scanner(System.in);
    public static Random rand = new Random();


    public static void main(String[] args) {
        size = 5;
        block = 4;
        //new task4().customizeGame();
        initMap();                              //
        printMap();                             //
        while (true) {
            humanTurn();
            printMap();
            if (checkWin(DOT_X)) {
                System.out.println("Победил человек");
                break;
            }
            if (isMapFull()) {
                System.out.println("Ничья");
                break;
            }
            aiTurn();
            printMap();
            if (checkWin(DOT_O)) {
                System.out.println("Победил Искуственный Интеллект");
                break;
            }
            if (isMapFull()) {
                System.out.println("Ничья");
                break;
            }
        }
        System.out.println("Игра закончена");


    }//_____________________________________________________________________________


    /**
     * Логика победы изменина для работы с любым размером поля.
     */
    static boolean checkWin(char symb) {
        for (int col = 0; col < size - block + 1; col++) {
            for (int row = 0; row < size - block + 1; row++) {
                if (checkDiagonal(symb, col, row) || checkLanes(symb, col, row)) return true;
            }
        }
        return false;
    }

    /**
     * Проверяем диагонали
     */
    static boolean checkDiagonal(char symb, int offsetX, int offsetY) {
        boolean toright, toleft;
        toright = true;
        toleft = true;
        for (int i = 0; i < block; i++) {
            toright &= (map[i + offsetX][i + offsetY] == symb);
            toleft &= (map[block - i - 1 + offsetX][i + offsetY] == symb);
        }

        if (toright || toleft) return true;

        return false;
    }

    /**
     * Проверяем горизонтальные и вертикальные линии
     */
    static boolean checkLanes(char symb, int offsetX, int offsetY) {
        boolean cols, rows;
        for (int col = offsetX; col < block + offsetX; col++) {
            cols = true;
            rows = true;
            for (int row = offsetY; row < block + offsetY; row++) {
                cols &= (map[col][row] == symb);
                rows &= (map[row][col] == symb);
            }

            if (cols || rows) return true;
        }

        return false;
    }//________________________________________________________________

    public static boolean isMapFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (map[i][j] == DOT_EMPTY) return false;
            }
        }
        return true;
    }

//    public static void aiTurn() {
//        int x = 1;
//        int y = 1;
//        boolean ai_win = false;
//        boolean user_win = false;

        // Блокировка хода пользователя, если он побеждает на следующем ходу

//        if (!ai_win) {
//            for (int i = 0; i < SIZE; i++) {
//                for (int j = 0; j < SIZE; j++) {
//                    if (!isCellValid(i, j)) {
//                        map[i][j] = DOT_X;
//                        if (checkWin(DOT_X)) {
//                            x = i;
//                            y = j;
//                            user_win = true;
//                        }
//                        map[i][j] = DOT_O;
//                    }
//                }
//            }
//        }
//
//        if (ai_win && user_win) {
//            do {
//                x = rand.nextInt(SIZE);
//                y = rand.nextInt(SIZE);
//            } while (!isCellValid(x, y));
//            System.out.println("Компьютер походил в точку " + (x + 1) + " " + (y + 1));
//            map[y][x] = DOT_O;
//        } }


    public static void aiTurn() {
        int x, y;
        int[] blockingXY = getBlockingXY();
        System.out.println(Arrays.toString(blockingXY));
        if (blockingXY.length == 2) {
            y = blockingXY[0];
            x = blockingXY[1];
        } else {
            do {
                y = rand.nextInt(SIZE);
                x = rand.nextInt(SIZE);
            } while (!isCellValid(x, y));
        }

        System.out.println("Компьютер походил в точку " + (x + 1) + " " + (y + 1));
        map[y][x] = DOT_O;
    }

    public static int[] getBlockingXY() {
        char symb = DOT_X;

        int mainDiagonal = 0;
        int sideDiagonal = 0;
        int lastIndex = map.length - 1;
        for (int i = 0; i < map.length; i++) {

            int rowCount = 0;
            int columnCount = 0;

            for (int j = 0; j < map.length; j++) {

                if (map[i][j] == symb && (++rowCount + 1) == DOTS_TO_WIN) {

                    for (int l = 0; l < map.length; l++) {
                        if (isCellValid(l, i)) {
                            return new int[]{i, l};
                        }
                    }

                }

                if (map[j][i] == symb && (++columnCount + 1) == DOTS_TO_WIN) {

                    for (int l = 0; l < map.length; l++) {
                        if (isCellValid(i, l)) {
                            return new int[]{l, i};
                        }
                    }

                }
            }

            if (map[i][i] == symb && (++mainDiagonal + 1) == DOTS_TO_WIN) {
                for (int l = 0; l < map.length; l++) {
                    if (isCellValid(l, l)) {
                        return new int[]{l, l};
                    }
                }
            }

            if (map[i][lastIndex - i] == symb && (++sideDiagonal + 1) == DOTS_TO_WIN) {
                for (int l = 0; l < map.length; l++) {
                    if (isCellValid(lastIndex - l, l)) {
                        return new int[]{l, lastIndex - l};
                    }
                }
            }
        }

        return new int[0];
    }

public static void humanTurn() {

        int x, y;
        do {
            System.out.println("Введите координаты в формате X Y");
            x = sc.nextInt() - 1;
            y = sc.nextInt() - 1;
        } while (!isCellValid(x, y)); // while(isCellValid(x, y) == false)
        map[y][x] = DOT_X;
    }

    public static boolean isCellValid(int x, int y) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) return false;
        if (map[y][x] == DOT_EMPTY) return true;
        return false;
    }

    public static void initMap() {
        map = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                map[i][j] = DOT_EMPTY;
            }
        }
    }

    public static void printMap() {
        for (int i = 0; i <= SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}