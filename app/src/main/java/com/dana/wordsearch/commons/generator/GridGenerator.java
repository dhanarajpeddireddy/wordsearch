package com.dana.wordsearch.commons.generator;


import java.util.Arrays;

public abstract class GridGenerator<InputType, OutputValue> {

    public abstract OutputValue setGrid(InputType dataInput, char[][] grid);

    protected void resetGrid(char[][] grid) {
        for (char[] chars : grid) {
            Arrays.fill(chars, '\0');
        }
    }

}
