package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    /**
     * TODO: ЗАДАЧА1: Переписать программу самостоятельно.
     * ВЫПОЛНЕНО.
     */

    private static final int WIN_COUNT = 4;
    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = 'O';
    private static final char DOT_EMPTY = ' ';
    private static final String HUMAN_WON = "ВЫ ПОБЕДИЛИ!!!";
    private static final String AI_WON = "ПОБЕДИЛ КОМПЬЮТЕР!!!";
    private static final String DRAW = "НИЧЬЯ!!!";
    private static Scanner scanner;
    private static char[][] field;
    private static int fieldSizeX;
    private static int fieldSizeY;
    private static final Random random = new Random();
    private static String header;
    private static String separator;

    private static Scanner getScanner() {
        if (scanner == null)
            scanner = new Scanner(System.in);
        return scanner;
    }

    public static void main(String[] args) {
        do {
            initialize();
            // считаем максимальное количество ходов на поле для дальнейшего определения ничьи
            int maxCount = fieldSizeX * fieldSizeY;
            int count = 0;

            while (true) {
                printField();
                humanTurn();
                printField();
                if (checkWin(DOT_HUMAN)) {
                    System.out.println(HUMAN_WON);
                    break;
                }
                count += 1;
                if (count == maxCount) {
                    System.out.println(DRAW);
                    break;
                }
                aiTurn();
                printField();
                if (checkWin(DOT_AI)) {
                    System.out.println(AI_WON);
                    break;
                }
                count += 1;
                if (count == maxCount) {
                    System.out.println(DRAW);
                    break;
                }
            }
            System.out.println("Желаете сыграть еще раз ? ('Y' - да)");
        } while (getScanner().next().equalsIgnoreCase("Y"));
    }

    /**
     * TODO: Возможно, поправить отрисовку игрового поля
     * ВЫПОЛНЕНО. ИЗМЕНЁН ВНЕШНИЙ ВИД ПОЛЯ, А ТАКЖЕ ТЕПЕРЬ МОЖНО ВЫБИРАТЬ НЕ ТОЛЬКО КВАДРАТНОЕ ПОЛЕ, НО И ПОЛЕ С РАЗНЫМ
     * РАЗМЕРОМ ПО ОСЯМ Х И У.
     * <p>
     * Инициализация игрового поля
     */
    private static void initialize() {
        askForFieldSize('X');
        askForFieldSize('Y');

        // создаём заголовок с номерами столбцов и разделительную линию из +---+
        header = " |";
        for (int i = 1; i <= fieldSizeX; i++) {
            header += " " + i + " |";
        }
        separator = " " + "+---".repeat(fieldSizeX) + "+";


        // создаём массив необходимой размерностью и содержащий только chars с пустыми пропусками
        field = new char[fieldSizeY][fieldSizeX];
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                field[y][x] = DOT_EMPTY;
            }
        }
    }


    /**
     * Функция ввода размера поля пользоваетелем
     *
     * @param x - ось, для которой вводится размер
     */
    private static void askForFieldSize(char x) {
        String error = "Необходимо указать целое положительное число!!!";
        while (true) {
            System.out.println("Введите размер игрового поля по оси " + x + " :");
            try {
                int size = getScanner().nextInt();
                if (size <= 0) {
                    System.out.println(error);
                    continue;
                }
                if (x == 'X') fieldSizeX = size;
                else fieldSizeY = size;
                break;
            } catch (Exception e) {
                System.out.println(error);
                scanner.next();
            }
        }
    }


    /**
     * Отрисовка игрового поля
     */
    private static void printField() {
        System.out.print("\n" + header + "\n" + separator);

        for (int y = 0; y < fieldSizeY; y++) {
            System.out.print("\n" + (y + 1) + "|");
            for (int x = 0; x < fieldSizeX; x++) {
                System.out.print(" " + field[y][x] + " |");
            }
            System.out.print("\n" + separator);
        }
        System.out.println("\n");
    }


    /**
     * Проверка победы
     * TODO: 2 ЗАДАЧА: Переработать метод проверки победы в домашнем задании, необходимо использовать
     *  вспомогательыне методы и циклы (например for)
     *
     * @param c - фишка игрока
     * @return результат проверки
     */
    static boolean checkWin(char c) {
        for (int x = 0; x < fieldSizeY; x++) {
            for (int y = 0; y < fieldSizeX; y++) {

                // проверяем если значение этой ячейки равно значению фишки ходившего игрока
                if (field[x][y] == c) {
                    // если по горизонтали справа от данной клетки еще может уместиться победное количество фишек...
                    if (fieldSizeY - y >= WIN_COUNT) {
                        //...то проверяем горизонталь:
                        if (checkHorizontal(c, x, y)) return true;
                    }
                    // если по вертикали вниз от данной клетки ещё может уместиться победное количество фишек...
                    if (fieldSizeX - x >= WIN_COUNT) {
                        //... то проверяем вертикаль
                        if (checkVertical(c, x, y)) return true;
                    }
                    // проверяем победу по диагонали в правый верхний угол от фишки, а потом в правый нижний угол
                    if (checkDiagonalUP(c, x, y)) return true;
                    if (checkDiagonalDown(c, x, y)) return true;
                }
            }
        }
        return false;
    }


    /**
     * Функция проверки победы по горизонтали вправо от фишки
     *
     * @param c - фишка ходящего игрока
     * @param x - координата фишки по горизонтали
     * @param y - координата фишки по вертикали
     */
    static boolean checkHorizontal(char c, int x, int y) {
        for (int i = 1; i < WIN_COUNT; i++) {
            if (field[x][y + i] != c) {
                return false;
            }
        }
        return true;
    }

    /**
     * Функция проверки победы по вертикали вниз от фишки
     */
    static boolean checkVertical(char c, int x, int y) {
        for (int i = 1; i < WIN_COUNT; i++) {
            if (field[x + i][y] != c) {
                return false;
            }
        }
        return true;
    }

    /**
     * Функция проверки победы по диагонали в верхний правый угол от фишки
     */
    static boolean checkDiagonalUP(char c, int x, int y) {
        for (int i = 1; i < WIN_COUNT; i++) {
            if (!isCellValid(x - i, y + i) || field[x - i][y + i] != c) {
                return false;
            }
        }
        return true;
    }

    /**
     * Функция проверки победы по диагонали в нижний правый угол от фишки игрока
     */
    static boolean checkDiagonalDown(char c, int x, int y) {
        for (int i = 1; i < WIN_COUNT; i++) {
            if (!isCellValid(x + i, y + i) || field[x + i][y + i] != c) {
                return false;
            }
        }
        return true;
    }


    /**
     * Обработка хода игрока
     */
    private static void humanTurn() {
        int x, y;
        do {
            System.out.println("Введите координаты хода X и Y (от 1 до 3) через пробел >>>");
            y = getScanner().nextInt() - 1;
            x = getScanner().nextInt() - 1;
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
    }

    /**
     * TODO: Задача 3: Компьютер должен помешать игроку победить
     * ВЫПОЛНЕНО. ТЕПЕРЬ КОМПЬТЕР ПРЕДУГАДЫВАЕТ ПОБЕДУ ПРОТИВНИКА ЗА 2 ХОДА, НО ПРИ ЭТОМ ВСЁ ЕЩЁ ОЧЕНЬ ГЛУП
     * Ход компьютера
     */
    private static void aiTurn() {
        List<Integer> list = checkForHumanWin();
        if (!list.isEmpty()) {
            field[list.get(0)][list.get(1)] = DOT_AI;
            return;
        }
        int x, y;
        do {
            y = random.nextInt(fieldSizeX);
            x = random.nextInt(fieldSizeY);
        }
        while (!isCellEmpty(x, y));
        field[x][y] = DOT_AI;
    }

    /**
     * Проверка, является ли ячейка пустой
     *
     * @param x координата
     * @param y координата
     * @return результат проверки
     */
    private static boolean isCellEmpty(int x, int y) {
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Проверка корректности ввода
     * (координаты хода не должны превышать размерность массива игрового поля)
     *
     * @param x координата
     * @param y координата
     * @return результат проверки
     */
    private static boolean isCellValid(int x, int y) {
        return x >= 0 && x < fieldSizeY && y >= 0 && y < fieldSizeX;
    }

    /**
     * Функция прогнозирования победы человека. Подставляет две фишки человека и, в случае определения победы, ставит
     * на место одной из них фишку компа.
     *
     * @return - возвращает координаты, куда нужно поставить фишку компа
     */
    private static List<Integer> checkForHumanWin() {
        List<Integer> list = new ArrayList<>(2);
        for (int x = 0; x < fieldSizeY; x++) {
            for (int y = 0; y < fieldSizeX; y++) {
                if (isCellEmpty(x, y)) {
                    field[x][y] = DOT_HUMAN;
                    for (int a = 0; a < fieldSizeY; a++) {
                        for (int z = 0; z < fieldSizeX; z++) {
                            if (isCellEmpty(a, z)) {
                                field[a][z] = DOT_HUMAN;
                                if (checkWin(DOT_HUMAN)) {
                                    field[x][y] = DOT_EMPTY;
                                    field[a][z] = DOT_EMPTY;
                                    list.add(a);
                                    list.add(z);
                                    return list;
                                } else field[a][z] = DOT_EMPTY;
                            }
                        }
                    }
                    field[x][y] = DOT_EMPTY;
                }
            }
        }
        return list;
    }
}