package com.dana.wordsearch;

public class Constants {

    public interface IGAMETYPE
    {
        int EASY=0;
        int MEDIUM=1;
        int HARD=2;
    }


    public interface IGAMEGRIDSIZE
    {
        int EASY=5;
        int MEDIUM=7;
        int HARD=11;
    }

    public interface IGAMECOINS
    {
        int EASY=2;
        int MEDIUM=3;
        int HARD=5;
    }


    public static final String EXTRA_GAME_ROUND_ID =
            "Game.ID";
    public static final String EXTRA_ROW_COUNT =
            "Game.ROW";
    public static final String EXTRA_COL_COUNT =
            "Game.COL";

    public static final String EXTRA_GAME_TYPE =
            "Game.TYPE";

    public static final String EXTRA_EARNED_COINS =
            "Game.EARNEDCOINS";

    public static final String AVATHAR_ID =
            "USER.AVATHAR";

    public static int QLUECOST=10;

}
