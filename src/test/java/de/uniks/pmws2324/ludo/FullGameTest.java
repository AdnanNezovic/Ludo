package de.uniks.pmws2324.ludo;

import de.uniks.pmws2324.ludo.model.Piece;
import de.uniks.pmws2324.ludo.service.GameService;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobotInterface;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Objects;

import static de.uniks.pmws2324.ludo.Constants.FIELD_DIM;
import static org.junit.jupiter.api.Assertions.*;

class FullGameTest extends ApplicationTest {

    // the duration of FullGameTest (cursor moving speed normal) is 3 minute and 40 seconds
    // the duration of Full (cursor moving speed high) is 2 minute and 14 seconds

    private Stage stage;
    private App app;
    private Canvas canvas;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        this.app = new App(true);
        app.start(this.stage);
    }

    @Test
    public void ludoTest() {
        assertEquals("Setup - Screen", stage.getTitle());
        clickOn("#yellowNameTextfield").write("Adnan");
        clickOn("#greenNameTextfield").write("Dina");

        clickOn("#startGameButton");

        assertEquals("Ingame - Screen", stage.getTitle());
        Label firstRoundLabel = lookup("#firstRoundLabel").query();
        assertEquals("Decide who starts the game!", firstRoundLabel.getText());

        GameService gameService = app.getGameService();

        canvas = lookup("#gameFieldCanvas").query();
        VBox diceBox = lookup("#diceVBox").query();
        assertEquals(1, diceBox.getChildren().size());
        Label actionLabel;

        VBox playersBox = lookup("#playersBox").query();
        Label yellowPlayerName = (Label) playersBox.getChildren().get(0).lookup("#playerName");
        Label greenPlayerName = (Label) playersBox.getChildren().get(1).lookup("#playerName");
        assertEquals("Adnan", yellowPlayerName.getText());
        assertEquals("Dina", greenPlayerName.getText());

        do {
            actionLabel = lookup("#actionLabel").query();
            clickOn("#rollDiceButton");
            if (actionLabel.getText().equals("Move a piece")) {
                Piece piece = gameService.getCurrentPlayer().getPieces().stream()
                        .filter(f -> Objects.nonNull(gameService.getPiecePath(f)))
                        .findFirst()
                        .orElse(null);
                moveTo(canvas);
                moveToPiece(piece).clickOn(MouseButton.PRIMARY);
                Label diceNumber = lookup("#numberOfDiceLabel").query();
                if (diceNumber.getText().equals("6")) {
                    sleep(1500);
                } else {
                    sleep(400);
                }
            }
        } while (Objects.equals(stage.getTitle(), "Ingame - Screen"));


        assertEquals("GameOver - Screen", stage.getTitle());

        sleep(1020);

        VBox rankingBox = lookup("#rankingPlayerBox").query();

        Label firstPlaceName = (Label) rankingBox.getChildren().get(0).lookup("#namePlayerRankLabel");
        Label firstPlaceRankNumber = (Label) rankingBox.getChildren().get(0).lookup("#rankNumberLabel");
        Label firstPlaceNumberOfPieces = (Label) rankingBox.getChildren().get(0).lookup("#numberOfPieces");
        assertEquals("Dina", firstPlaceName.getText());
        assertEquals("1.", firstPlaceRankNumber.getText());
        assertEquals("4 pieces at home", firstPlaceNumberOfPieces.getText());

        Label secondPlaceName = (Label) rankingBox.getChildren().get(1).lookup("#namePlayerRankLabel");
        Label secondPlaceRankNumber = (Label) rankingBox.getChildren().get(1).lookup("#rankNumberLabel");
        Label secondPlaceNumberOfPieces = (Label) rankingBox.getChildren().get(1).lookup("#numberOfPieces");
        assertEquals("Adnan", secondPlaceName.getText());
        assertEquals("2.", secondPlaceRankNumber.getText());
        assertEquals("1 pieces at home", secondPlaceNumberOfPieces.getText());
    }

    private FxRobotInterface moveToPiece(Piece piece) {
        double xCenter = canvas.getWidth() / 2;
        double yCenter = canvas.getHeight() / 2;
        double xPiece = piece.getX() + FIELD_DIM / 2;
        double yPiece = piece.getY() + FIELD_DIM / 2;

        return moveBy(xPiece - xCenter, yPiece - yCenter);
    }
}
