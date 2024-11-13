package de.uniks.pmws2324.ludo;

import de.uniks.pmws2324.ludo.model.*;

import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static final double TRAVEL_TIME = 0.4;

    // Color Constants
    public static final List<Color> COLOR_LIST = new ArrayList<>();
    public static final String YELLOW_NAME = "#ffff00";
    public static final String GREEN_NAME = "#33cc33";
    public static final String RED_NAME = "#ff3333";
    public static final String BLUE_NAME = "#66ccff";
    public static final Color YELLOW = new Color().setName(YELLOW_NAME);
    public static final Color GREEN = new Color().setName(GREEN_NAME);
    public static final Color RED = new Color().setName(RED_NAME);
    public static final Color BLUE = new Color().setName(BLUE_NAME);

    static {
        COLOR_LIST.add(YELLOW);
        COLOR_LIST.add(GREEN);
        COLOR_LIST.add(RED);
        COLOR_LIST.add(BLUE);
    }

    // fieldtype constants
    public static final String BASEFIELD = "Basefield";
    public static final String HOMEFIELD = "Homefield";
    public static final String GAMEFIELD = "Gamefield";

    // canvas constants
    public static final double CANVAS_STEP = 550/11;

    public static final double CENTER_X = 6*CANVAS_STEP;
    public static final double CENTER_Y = 6*CANVAS_STEP;
    public static final double FIELD_DIM = 35;
    public static final double PIECE_DIM = FIELD_DIM/2;

    // startPoints
    public static final double STARTPOINT_YELLOW_X = CANVAS_STEP;
    public static final double STARTPOINT_YELLOW_Y = 5*CANVAS_STEP;

    public static final int SPECIAL_FIELD_INDEX = 9; // the field which is one step away from the homeFields and a startField
    public static final int START_FIELD_INDEX = 0;
    public static final int NUMBER_OF_BASES = 4;
}
