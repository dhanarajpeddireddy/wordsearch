package com.dana.wordsearch.commons;

import android.graphics.Color;

import com.dana.wordsearch.Constants;

import java.util.List;
import java.util.Random;



public class Util {
    public static final char NULL_CHAR = '\0';

    private static final Random sRand = new Random();

    public static int getRandomColorWithAlpha(int alpha) {
        int r = getRandomInt() % 256;
        int g = getRandomInt() % 256;
        int b = getRandomInt() % 256;
        return Color.argb(alpha, r, g, b);
    }

    public static char getRandomChar() {

        return (char) getRandomIntRange(65, 90);
    }


    public static int getRandomIntRange(int min, int max) {
        return min + (getRandomInt() % ((max - min) + 1));
    }

    public static int getRandomInt() {
        return Math.abs(sRand.nextInt());
    }

    public static int getIndexLength(GridIndex start, GridIndex end) {
        int x = Math.abs(start.col - end.col);
        int y = Math.abs(start.row - end.row);
        return Math.max(x, y) + 1;
    }

    public static <T> void randomizeList(List<T> list) {
        int count = list.size();
        for (int i = 0; i < count; i++) {
            int randIdx = getRandomIntRange(Math.min(i + 1, count - 1), count - 1);
            T temp = list.get(randIdx);
            list.set(randIdx, list.get(i));
            list.set(i, temp);
        }
    }

    public static String getReverseString(String str) {
        StringBuilder out = new StringBuilder();
        for (int i = str.length() - 1; i >= 0; i--)
            out.append(str.charAt(i));

        return out.toString();
    }


    public static void fillNullCharWidthRandom(char[][] gridArr) {
        for (int i = 0; i < gridArr.length; i++) {
            for (int j = 0; j < gridArr[i].length; j++) {
                if (gridArr[i][j] == NULL_CHAR)
                    gridArr[i][j] = getRandomChar();
            }
        }
    }


    public static void sortByLength(List<String> strings) {
        for (int i = 0; i < strings.size(); i++) {
            for (int j = i + 1; j < strings.size(); j++) {
                if (strings.get(j).length() > strings.get(i).length()) {
                    String temp = strings.get(j);
                    strings.set(j, strings.get(i));
                    strings.set(i, temp);
                }
            }
        }
    }


    public static int getgameBoardSize(int gameType)
    {
        if (gameType== Constants.IGAMETYPE.EASY)
        return  Constants.IGAMEGRIDSIZE.EASY;

       else if (gameType== Constants.IGAMETYPE.MEDIUM)
            return  Constants.IGAMEGRIDSIZE.MEDIUM;

        else if (gameType== Constants.IGAMETYPE.HARD)
            return  Constants.IGAMEGRIDSIZE.HARD;

        else return Constants.IGAMEGRIDSIZE.EASY;
    }

    public static int getEarnedCoins(int usedWordCount, int gameType) {

        if (gameType== Constants.IGAMETYPE.EASY)
            return  Constants.IGAMECOINS.EASY*usedWordCount;

        else if (gameType== Constants.IGAMETYPE.MEDIUM)
            return  Constants.IGAMECOINS.MEDIUM*usedWordCount;

        else if (gameType== Constants.IGAMETYPE.HARD)
            return  Constants.IGAMECOINS.HARD*usedWordCount;

        else return Constants.IGAMECOINS.EASY*usedWordCount;
    }
}
