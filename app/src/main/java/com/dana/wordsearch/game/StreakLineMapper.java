package com.dana.wordsearch.game;


import com.dana.wordsearch.commons.Mapper;
import com.dana.wordsearch.custom.StreakView;
import com.dana.wordsearch.model.UsedWord;

public class StreakLineMapper extends Mapper<UsedWord.AnswerLine, StreakView.StreakLine> {
    @Override
    public StreakView.StreakLine map(UsedWord.AnswerLine obj) {
        StreakView.StreakLine s = new StreakView.StreakLine();
        s.getStartIndex().set(obj.startRow, obj.startCol);
        s.getEndIndex().set(obj.endRow, obj.endCol);
        s.setColor(obj.color);

        return s;
    }

    @Override
    public UsedWord.AnswerLine revMap(StreakView.StreakLine obj) {
        UsedWord.AnswerLine a = new UsedWord.AnswerLine();
        a.startRow = obj.getStartIndex().row;
        a.startCol = obj.getStartIndex().col;
        a.endRow = obj.getEndIndex().row;
        a.endCol = obj.getEndIndex().col;
        a.color = obj.getColor();

        return a;
    }
}
