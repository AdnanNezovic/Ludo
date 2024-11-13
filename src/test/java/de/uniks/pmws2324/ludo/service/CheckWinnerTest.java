package de.uniks.pmws2324.ludo.service;

import de.uniks.pmws2324.ludo.model.Color;
import de.uniks.pmws2324.ludo.model.Field;
import de.uniks.pmws2324.ludo.model.Piece;
import de.uniks.pmws2324.ludo.model.Player;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static de.uniks.pmws2324.ludo.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

public class CheckWinnerTest {

    private LinkedHashMap<Color, String> playerNames;
    private GameService gameService;

    @Test
    public void isNotWinnerTest() {
        // Test if currentPlayer is not the winner, because it is the beginning of the game

        playerNames = new LinkedHashMap<>();
        playerNames.put(YELLOW, "Adnan");
        playerNames.put(GREEN, "Dina");

        gameService = new GameService(playerNames,true).initGame();
        Player currentPlayer = gameService.getCurrentPlayer();
        assertEquals("Adnan", currentPlayer.getName());
        boolean hasWon = gameService.checkWinner();

        assertFalse(hasWon);
    }

    @Test
    public void isWinnerTest() {
        // Test if currentPlayer is the winner, because all pieces of the currentPlayer are on homeFields

        playerNames = new LinkedHashMap<>();
        playerNames.put(YELLOW, "Adnan");
        playerNames.put(GREEN, "Dina");

        gameService = new GameService(playerNames,true).initGame();
        gameService.getGame().setFirstRound(false);

        Player currentPlayer = gameService.getCurrentPlayer();
        assertEquals("Adnan", currentPlayer.getName());
        currentPlayer.setHasRolled(true);

        // all pieces of the currentPlayer are on homeFields
        List<Field> allGameFields = gameService.getAllPartGameAndHomeFields().get(3);
        for (int i = 0; i < currentPlayer.getPieces().size(); i++) {
            Piece piece = currentPlayer.getPieces().get(i);
            piece.setPieceField(allGameFields.get(SPECIAL_FIELD_INDEX + i + 1));
        }

        boolean hasWon = gameService.checkWinner();

        assertTrue(hasWon);
        assertTrue(currentPlayer.isWinner());
    }

    @Test
    public void rankingTest() {
        // Test if the ranking is correct after the game is finished

        playerNames = new LinkedHashMap<>();
        playerNames.put(YELLOW, "Adnan");
        playerNames.put(GREEN, "Dina");

        gameService = new GameService(playerNames,true).initGame();
        gameService.getGame().setFirstRound(false);

        Player currentPlayer = gameService.getCurrentPlayer();
        assertEquals("Adnan", currentPlayer.getName());

        // all pieces of the currentPlayer are on homeFields
        List<Field> allGameFields = gameService.getAllPartGameAndHomeFields().get(3);
        for (int i = 0; i < currentPlayer.getPieces().size(); i++) {
            Piece piece = currentPlayer.getPieces().get(i);
            piece.setPieceField(allGameFields.get(SPECIAL_FIELD_INDEX + i + 1));
        }

        gameService.checkWinner();

        Map<Player, Integer> ranking = gameService.getRank();

        assertEquals(2, ranking.size());
        assertEquals(4, ranking.get(currentPlayer));
        assertEquals(0, ranking.get(currentPlayer.getNextPlayer()));
    }
}
