package com.dana.wordsearch.data.entity;


import com.dana.wordsearch.commons.Mapper;
import com.dana.wordsearch.commons.generator.StringGridGenerator;
import com.dana.wordsearch.model.GameData;
import com.dana.wordsearch.model.Grid;

public class GameDataMapper extends Mapper<GameDataEntity, GameData> {
    @Override
    public GameData map(GameDataEntity obj) {
        if (obj == null) return null;

        Grid grid = new Grid(obj.getGridRowCount(), obj.getGridColCount());
        GameData gameData = new GameData();
        gameData.setId(obj.getId());
        gameData.setName(obj.getName());
        gameData.setDuration(obj.getDuration());
        gameData.setGrid(grid);

        if (obj.getGridData() != null && obj.getGridData().length() > 0) {
            new StringGridGenerator().setGrid(obj.getGridData(), grid.getArray());
        }

        gameData.addUsedWords(obj.getUsedWords());

        return gameData;
    }

    @Override
    public GameDataEntity revMap(GameData obj) {
        if (obj == null) return null;

        GameDataEntity ent = new GameDataEntity();
        ent.setId(obj.getId());
        ent.setName(obj.getName());
        ent.setDuration(obj.getDuration());

        if (obj.getGrid() != null) {
            ent.setGridRowCount(obj.getGrid().getRowCount());
            ent.setGridColCount(obj.getGrid().getColCount());
            ent.setGridData(obj.getGrid().toString());
        }

        ent.setUsedWords(obj.getUsedWords());

        return ent;
    }
}
