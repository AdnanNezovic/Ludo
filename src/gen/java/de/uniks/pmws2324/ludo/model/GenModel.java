package de.uniks.pmws2324.ludo.model;

import org.fulib.FulibTools;
import org.fulib.builder.ClassModelDecorator;
import org.fulib.builder.ClassModelManager;
import org.fulib.builder.reflect.Link;

import java.util.List;

public class GenModel implements ClassModelDecorator {

    class Player {
        String name = "";
        Player nextPlayer;
        Color color;
        boolean hasRolled;
        boolean winner;
        boolean canMoveAPiece;
        Field startField;
        int priority;

        @Link("pieceOwner")
        List<Piece> pieces;

        @Link("currentPlayer")
        Dice dice;

        @Link("baseFieldsOwner")
        List<Field> baseFields;
    }

    class Piece extends Location {
        Color color;

        @Link("piece")
        Field pieceField;

        @Link("pieces")
        Player pieceOwner;
    }

    class Dice {
        int number;

        @Link("dice")
        Player currentPlayer;
    }

    class Field extends Location {
        String type;
        Field nextField;
        Field nextHomeField;
        Color color;

        @Link("pieceField")
        Piece piece;

        @Link("baseFields")
        Player baseFieldsOwner;
    }

    class Color {
        String name = "";
    }

    class Location {
        double x;
        double y;
    }

    class Game {
        Player currentPlayer;
        boolean active;
        boolean firstRound;
        int firstRoundInt;
    }

    @Override
    public void decorate(ClassModelManager m) {
        m.haveNestedClasses(GenModel.class);
        FulibTools.classDiagrams().dumpSVG(m.getClassModel(), "doc/images/srunclassdiagram.svg");
    }
}
