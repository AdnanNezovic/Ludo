package de.uniks.pmws2324.ludo.service;

import de.uniks.pmws2324.ludo.model.Color;
import de.uniks.pmws2324.ludo.model.Field;
import de.uniks.pmws2324.ludo.model.Piece;
import de.uniks.pmws2324.ludo.model.Player;
import org.junit.jupiter.api.Test;


import java.util.LinkedHashMap;
import java.util.List;

import static de.uniks.pmws2324.ludo.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

public class NextPlayerTest {

    private GameService gameService;
    private LinkedHashMap<Color, String> playerNames;

    @Test
    public void nextPlayerNotSixThrownTest() {
        // Test if the next player is set correctly, when current player has rolled dice with number not equal to 6

        playerNames = new LinkedHashMap<>();
        playerNames.put(YELLOW, "Adnan");
        playerNames.put(GREEN, "Dina");

        gameService = new GameService(playerNames,true).initGame();

        gameService.getGame().setFirstRound(false);

        Player adnanPlayer = gameService.getCurrentPlayer();
        assertEquals("Adnan", adnanPlayer.getName());
        adnanPlayer.setHasRolled(true);
        adnanPlayer.getDice().setNumber(5);
        adnanPlayer.setCanMoveAPiece(gameService.movePieceIsPossible());

        gameService.switchToNextPlayer();

        assertEquals(adnanPlayer.getNextPlayer(), gameService.getCurrentPlayer());
        assertEquals("Dina", gameService.getCurrentPlayer().getName());
        assertNotEquals(adnanPlayer, gameService.getCurrentPlayer());
        assertNotEquals("Adnan", gameService.getCurrentPlayer().getName());
    }

    @Test
    public void nextPlayerSixThrownTest() {
        // Test if the next player is set correctly, when currentPlayer has rolled the dice with number 6 and game has started

        playerNames = new LinkedHashMap<>();
        playerNames.put(YELLOW, "Adnan");
        playerNames.put(GREEN, "Dina");

        gameService = new GameService(playerNames,true).initGame();

        gameService.getGame().setFirstRound(false);

        Player adnanPlayer = gameService.getCurrentPlayer();
        assertEquals("Adnan", adnanPlayer.getName());

        adnanPlayer.setHasRolled(true);
        adnanPlayer.getDice().setNumber(6);
        adnanPlayer.setCanMoveAPiece(gameService.movePieceIsPossible());

        gameService.switchToNextPlayer();

        assertEquals(adnanPlayer, gameService.getCurrentPlayer());
        assertEquals("Adnan", gameService.getCurrentPlayer().getName());
        assertNotEquals(adnanPlayer.getNextPlayer(), gameService.getCurrentPlayer());
        assertNotEquals("Dina", gameService.getCurrentPlayer().getName());
    }

    @Test
    public void sixThrownButNoMovePossible() {
        // Test if the next player is set correctly, when current player has rolled the dice with number 6 but moving is not possible

        playerNames = new LinkedHashMap<>();
        playerNames.put(YELLOW, "Adnan");
        playerNames.put(GREEN, "Dina");

        gameService = new GameService(playerNames,true).initGame();

        gameService.getGame().setFirstRound(false);

        Player adnanPlayer = gameService.getCurrentPlayer();
        assertEquals("Adnan", adnanPlayer.getName());

        adnanPlayer.setHasRolled(true);
        adnanPlayer.getDice().setNumber(6);

        List<Field> lastPartGameFields = gameService.getAllPartGameAndHomeFields().get(3);

        // three pieces of the currentPlayer are on the homeFields
        for (int i = 1; i <= 3; i++) {
            Piece piece = adnanPlayer.getPieces().get(i);
            piece.setPieceField(lastPartGameFields.get(SPECIAL_FIELD_INDEX + i + 1));
        }

        // one piece of the currentPlayer is one step away from the homeFields
        Piece pieceOnSpecialField = adnanPlayer.getPieces().get(0);
        pieceOnSpecialField.setPieceField(lastPartGameFields.get(SPECIAL_FIELD_INDEX));

        adnanPlayer.setCanMoveAPiece(gameService.movePieceIsPossible());

        gameService.switchToNextPlayer();

        assertEquals("Dina", gameService.getCurrentPlayer().getName());
    }
}
