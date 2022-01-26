package com.dana.wordsearch.model;


import androidx.annotation.NonNull;

import com.dana.wordsearch.commons.Direction;

import java.util.ArrayList;
import java.util.List;

public class UsedWord extends Word {

    private AnswerLine mAnswerLine;
    private Path path;
    private boolean mAnswered;
    private boolean mIsMystery;
    private int mRevealCount;
    private Direction direction;

    public UsedWord() {
        path=new Path();
        mAnswerLine = null;
        direction=null;
        mAnswered = false;
        mIsMystery = false;
        mRevealCount = 0;
    }


    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }



    public AnswerLine getAnswerLine() {
        return mAnswerLine;
    }

    public void setAnswerLine(AnswerLine answerLine) {
        mAnswerLine = answerLine;
    }

    public boolean isAnswered() {
        return mAnswered;
    }

    public void setAnswered(boolean answered) {
        mAnswered = answered;
    }

    public boolean isMystery() {
        return mIsMystery;
    }

    public void setIsMystery(boolean isMystery) {
        mIsMystery = isMystery;
    }

    public int getRevealCount() {
        return mRevealCount;
    }

    public void setRevealCount(int revealCount) {
        mRevealCount = revealCount;
    }

    public static final class AnswerLine {
        public int startRow;
        public int startCol;
        public int endRow;
        public int endCol;
        public int color;

        public AnswerLine() {
            this(0, 0, 0, 0, 0);
        }

        public AnswerLine(int startRow, int startCol, int endRow, int endCol, int color) {
            this.startRow = startRow;
            this.startCol = startCol;
            this.endRow = endRow;
            this.endCol = endCol;
            this.color = color;
        }

        @Override
        public String toString() {
            return startRow + "," + startCol + ":" + endRow + "," + endCol;
        }

        public void fromString(String string) {
            /*
                Expected format string = 1,1:6,6
             */
            if (string == null) return;

            String[] split = string.split(":", 2);
            if (split.length >= 2) {
                String[] start = split[0].split(",", 2);
                String[] end = split[1].split(",", 2);

                if (start.length >= 2 && end.length >= 2) {
                    startRow = Integer.parseInt(start[0]);
                    startCol = Integer.parseInt(start[1]);
                    endRow = Integer.parseInt(end[0]);
                    endCol = Integer.parseInt(end[1]);
                }
            }
        }

    }


    public static final class Path {

        List<PathItem> pathItems=new ArrayList<>();


        public List<PathItem> getPathItems() {
            return pathItems;
        }

        public void setPathItems(List<PathItem> pathItems) {
            this.pathItems = pathItems;
        }


        @Override
        public String toString() {

            StringBuilder stringBuilder=new StringBuilder();

            for (PathItem pathItem:getPathItems())
            {
                stringBuilder.append(pathItem.row);
                stringBuilder.append(",");
                stringBuilder.append(pathItem.column);
                stringBuilder.append(":");
            }

            return stringBuilder.substring(0, stringBuilder.length() - 1);
        }

        public void fromString(String string) {
            /*
                Expected format string = 1,1:6,6
             */
            if (string == null) return;

            String[] split = string.split(":");

            for (String s:split)
            {
                String[] path = s.split(",", 2);
                PathItem pathItem=new PathItem();
                pathItem.row=Integer.parseInt(path[0]);
                pathItem.column=Integer.parseInt(path[1]);
                getPathItems().add(pathItem);
            }

        }

    }


    public static final class PathItem {

        int row,column;

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getColumn() {
            return column;
        }

        public void setColumn(int column) {
            this.column = column;
        }

        @Override
        public String toString() {
            return "Path{" +
                    "row=" + row +
                    ", column=" + column +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UsedWord{" +
                "mAnswerLine=" + mAnswerLine +
                ", path=" + path +
                ", mAnswered=" + mAnswered +
                ", mIsMystery=" + mIsMystery +
                ", mRevealCount=" + mRevealCount +
                ", direction=" + direction +
                '}';
    }
}
