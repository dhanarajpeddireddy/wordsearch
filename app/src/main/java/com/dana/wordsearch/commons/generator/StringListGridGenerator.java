package com.dana.wordsearch.commons.generator;

import android.util.Log;

import com.dana.wordsearch.commons.Direction;
import com.dana.wordsearch.commons.Util;
import com.dana.wordsearch.model.UsedWord;

import java.util.ArrayList;
import java.util.List;


public class StringListGridGenerator extends GridGenerator<List<String>, List<UsedWord>> {

    private static final int MIN_GRID_ITERATION_ATTEMPT = 1;

    @Override
    public List<UsedWord> setGrid(List<String> dataInput, char[][] grid) {

        List<UsedWord> usedStrings = new ArrayList<>();
        int usedCount;
        for (int i = 0; i < MIN_GRID_ITERATION_ATTEMPT; i++) {

            usedCount = 0;
            resetGrid(grid);
            for (String word : dataInput) {
                UsedWord usedWord=new UsedWord();
                usedWord.setString(word);
                if (tryPlaceWord(usedWord, grid)) {
                    usedCount++;
                    usedStrings.add(usedWord);
                }
            }

            if (usedCount >= dataInput.size())
                break;
        }

        Util.fillNullCharWidthRandom(grid);

        return usedStrings;

    }

    private Direction getRandomDirection() {
        Direction dir;
        do {
            dir = Direction.values()[ Util.getRandomInt() % Direction.values().length];
        } while (dir == Direction.NONE);
        return dir;
    }

    private boolean tryPlaceWord(UsedWord word, char[][] gridArr) {

        Log.e("UsedWord"," : "+word.getString());

        Direction startDir = getRandomDirection();
        Log.e("startDir"," : "+startDir.toString());
        Direction currDir = startDir;

        int row, col,startRow,startCol;

        do {

            startRow = Util.getRandomInt() % gridArr.length;
            Log.e("startRow"," : "+startRow);
            row = startRow;
            do {

                startCol = Util.getRandomInt() % gridArr[0].length;
                col = startCol;
                do {
                    Log.e("startCol"," : "+startCol);
                    if (isValidPlacement(row, col, currDir, gridArr, word.getString())) {
                        placeWordAt(row, col, currDir, gridArr, word);
                        return true;
                    }

                    col = (++col) % gridArr[0].length;
                } while (col != startCol);

                row = (++row) % gridArr.length;
            } while (row != startRow);

            currDir = currDir.nextDirection();
        } while (currDir != startDir);

        return false;
    }


    private boolean isValidPlacement(int row, int col, Direction dir, char[][] gridArr, String word) {
        int wLen = word.length();
        if (dir == Direction.EAST && (col + wLen) >= gridArr[0].length) return false;
        if (dir == Direction.WEST && (col - wLen) < 0) return false;

        if (dir == Direction.NORTH && (row - wLen) < 0) return false;
        if (dir == Direction.SOUTH && (row + wLen) >= gridArr.length) return false;

        if (dir == Direction.SOUTH_EAST && ((col + wLen) >= gridArr[0].length || (row + wLen) >= gridArr.length)) return false;
        if (dir == Direction.NORTH_WEST && ((col - wLen) < 0 || (row - wLen) < 0)) return false;

        if (dir == Direction.SOUTH_WEST && ((col - wLen) < 0 || (row + wLen) >= gridArr.length)) return false;
        if (dir == Direction.NORTH_EAST && ((col + wLen) >= gridArr[0].length || (row - wLen) < 0)) return false;

        for (int i = 0; i < wLen; i++) {
            if (gridArr[row][col] != Util.NULL_CHAR && gridArr[row][col] != word.charAt(i))
                return false;

            Log.e("isValidPlacementb",col+" : "+row);
            col += dir.xOff;
            row += dir.yOff;

            Log.e("isValidPlacement",col+" : "+row);
        }

        return true;
    }


    private void placeWordAt(int row, int col, Direction dir, char[][] gridArr, UsedWord word) {
        Log.e("placeWordAt",col+" : "+row);
        for (int i = 0; i < word.getString().length(); i++) {
            gridArr[row][col] = word.getString().charAt(i);

            UsedWord.PathItem pathItem=new UsedWord.PathItem();

            pathItem.setColumn(col);
            pathItem.setRow(row);

            word.getPath().getPathItems().add(pathItem);

            col += dir.xOff;
            row += dir.yOff;
        }

        Log.e("usedWordv",word.toString());
    }
}
