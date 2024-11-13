package de.uniks.pmws2324.ludo.service;

import de.uniks.pmws2324.ludo.model.*;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;

import static de.uniks.pmws2324.ludo.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

public class GetPathTest {

    private GameService gameService;
    private LinkedHashMap<Color, String> playerNames;

    @Test
    public void pieceHavePath() {
        // Test if piece (of currentPlayer) has a path, if currentPlayer has rolled the dice and the piece is on the startField of the currentPlayer

        playerNames = new LinkedHashMap<>();
        playerNames.put(YELLOW, "Adnan");
        playerNames.put(GREEN, "Dina");

        gameService = new GameService(playerNames,true).initGame();

        Player currentPlayer = gameService.getCurrentPlayer().setHasRolled(true);
        assertEquals("Adnan", currentPlayer.getName());
        Piece piece = currentPlayer.getPieces().get(0);
        piece.setPieceField(currentPlayer.getStartField());

        currentPlayer.getDice().setNumber(5);

        List<Field> piecePath = gameService.getPiecePath(piece);
        assertNotNull(piecePath);
        assertEquals(5, piecePath.size());
    }

    @Test
    public void pieceNoPathNotSixAndAllPiecesOnBasefield() {
        // Test if piece (of currentPlayer) has no path, if the currentPlayer has rolled the dice with number not equal to 6 and all pieces are on baseFields

        playerNames = new LinkedHashMap<>();
        playerNames.put(YELLOW, "Adnan");
        playerNames.put(GREEN, "Dina");

        gameService = new GameService(playerNames,true).initGame();
        Player currentPlayer = gameService.getCurrentPlayer();
        assertEquals("Adnan", currentPlayer.getName());
        currentPlayer.setHasRolled(true);
        currentPlayer.getDice().setNumber(5);

        for (Piece piece:currentPlayer.getPieces()) {
            assertNull(gameService.getPiecePath(piece));
        }
    }

    @Test
    public void pieceNoPathBecauseNotThrownDice() {
        // Test if piece (of currentPlayer) has no path, if the currentPlayer has not rolled the dice yet

        playerNames = new LinkedHashMap<>();
        playerNames.put(YELLOW, "Adnan");
        playerNames.put(GREEN, "Dina");

        gameService = new GameService(playerNames,true).initGame();
        Player currentPlayer = gameService.getCurrentPlayer();
        assertEquals("Adnan", currentPlayer.getName());
        Field startField = currentPlayer.getStartField();
        Piece piece = currentPlayer.getPieces().get(0);

        piece.setPieceField(startField);
        currentPlayer.getDice().setNumber(5);

        assertFalse(currentPlayer.isHasRolled());

        List<Field> piecePath = gameService.getPiecePath(piece);
        assertNull(piecePath);
    }

    @Test
    public void pieceHasPathSix() {
        // Test if piece (of currentPlayer) has no path, if currentPlayer has rolled the dice with number 6 and all pieces are on baseFields

        playerNames = new LinkedHashMap<>();
        playerNames.put(YELLOW, "Adnan");
        playerNames.put(GREEN, "Dina");

        gameService = new GameService(playerNames,true).initGame();
        Player currentPlayer = gameService.getCurrentPlayer();
        assertEquals("Adnan", currentPlayer.getName());
        currentPlayer.setHasRolled(true);
        currentPlayer.getDice().setNumber(6);

        for (Piece piece:currentPlayer.getPieces()) {
            assertNotNull(gameService.getPiecePath(piece));
            assertEquals(1, gameService.getPiecePath(piece).size());
        }
    }

    @Test
    public void pieceHasPathSixAndOpponentPieceOnStartField() {
        // Test if piece (of currentPlayer) has no path, if the currentPlayer has rolled the dice with number 6 and all pieces are on baseFields
        // and the piece of the opponent is on the currentPlayers startField

        playerNames = new LinkedHashMap<>();
        playerNames.put(YELLOW, "Adnan");
        playerNames.put(GREEN, "Dina");

        gameService = new GameService(playerNames,true).initGame();
        gameService.getGame().setFirstRound(false);

        Player currentPlayer = gameService.getCurrentPlayer();
        assertEquals("Adnan", currentPlayer.getName());

        Player opponentPlayer = gameService.getPlayers().get(1);
        Piece opponentPiece = opponentPlayer.getPieces().get(0);
        opponentPiece.setPieceField(currentPlayer.getStartField());

        currentPlayer.setHasRolled(true);
        currentPlayer.getDice().setNumber(6);

        for (Piece piece:currentPlayer.getPieces()) {
            assertNotNull(gameService.getPiecePath(piece));
            assertEquals(1, gameService.getPiecePath(piece).size());
        }
    }

    @Test
    public void pieceHasPathSixAndOwnPieceOnStartField() {
        // Test if pieces (of currentPlayer) on baseFields have no path, if the currentPlayer has rolled the dice with number 6
        // and 3 pieces are on baseFields and one piece of the same color is on the startField

        playerNames = new LinkedHashMap<>();
        playerNames.put(YELLOW, "Adnan");
        playerNames.put(GREEN, "Dina");

        gameService = new GameService(playerNames,true).initGame();
        gameService.getGame().setFirstRound(false);

        Player currentPlayer = gameService.getCurrentPlayer();
        assertEquals("Adnan", currentPlayer.getName());

        Piece ownPiece = currentPlayer.getPieces().get(0);
        ownPiece.setPieceField(currentPlayer.getStartField());

        currentPlayer.setHasRolled(true);
        currentPlayer.getDice().setNumber(6);

        for (int i = 1; i < currentPlayer.getPieces().size(); i++) {
            Piece piece = currentPlayer.getPieces().get(i);
            assertNull(gameService.getPiecePath(piece));
        }
    }
}
