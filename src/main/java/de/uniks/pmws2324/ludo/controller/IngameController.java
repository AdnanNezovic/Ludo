package de.uniks.pmws2324.ludo.controller;

import de.uniks.pmws2324.ludo.App;
import de.uniks.pmws2324.ludo.Main;
import de.uniks.pmws2324.ludo.model.*;
import de.uniks.pmws2324.ludo.service.GameService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.util.Duration;

import static de.uniks.pmws2324.ludo.Constants.*;

public class IngameController extends Controller {

    private Parent parent;
    private GraphicsContext context;
    private final GameService gameService;
    private Controller diceController;
    private final List<Controller> playerSubControllerList = new ArrayList<>();
    private long lastTimeClicked;
    private final Game game;

    PropertyChangeListener callRerenderPlayers;
    PropertyChangeListener callRerenderDice;
    PropertyChangeListener callHandleFirstRound;


    private final AnimationTimer animationTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            drawMap();
        }
    };

    @FXML
    Canvas gameFieldCanvas;
    @FXML
    VBox diceVBox;
    @FXML
    VBox playersBox;
    @FXML
    Label firstRoundLabel;
    @FXML
    VBox canvasBox;

    public IngameController(App app, GameService gameService) {
        super(app, gameService);
        this.gameService = gameService;
        this.gameService.initGame();
        this.game = gameService.getGame();
        animationTimer.start();
    }

    @Override
    public void init() {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Ingame.fxml"));
        loader.setControllerFactory(c -> this);
        try {
            this.parent = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        title = "Ingame - Screen";
    }

    @Override
    public Parent render() {
        rerenderPlayers(null);
        rerenderDice(null);
        callHandleFirstRound(null);

        callRerenderPlayers = this::rerenderPlayers;
        game.listeners().addPropertyChangeListener(Game.PROPERTY_CURRENT_PLAYER, callRerenderPlayers);

        callRerenderDice = this::rerenderDice;
        game.listeners().addPropertyChangeListener(Game.PROPERTY_CURRENT_PLAYER, callRerenderDice);

        callHandleFirstRound = this::callHandleFirstRound;
        game.listeners().addPropertyChangeListener(Game.PROPERTY_FIRST_ROUND, callHandleFirstRound);

        this.gameFieldCanvas.setOnMouseClicked(event -> handlePieceClicked(event.getX(), event.getY()));

        return this.parent;
    }

    /*=================================== draw Methods ======================================*/

    private void drawMap() {
        cleanCanvas();
        this.context = gameFieldCanvas.getGraphicsContext2D();
        drawGameAndHomeFields();
        drawBaseFields();
        drawPieces();
    }

    private void cleanCanvas() {
        GraphicsContext context = gameFieldCanvas.getGraphicsContext2D();
        context.setFill(Color.WHITE);
        context.fillRect(0, 0, gameFieldCanvas.getWidth(), gameFieldCanvas.getHeight());
    }

    private void drawGameAndHomeFields() {
        for (List<Field> fieldList:gameService.getAllPartGameAndHomeFields()) {
            for (Field field:fieldList) {
                this.context.setFill(Color.WHITE);
                if (field.getColor() != null) {
                    this.context.setFill(Color.web(field.getColor().getName()));
                }
                this.context.fillOval(
                        field.getX(),
                        field.getY(),
                        FIELD_DIM,
                        FIELD_DIM
                );
                this.context.setStroke(Color.BLACK);
                this.context.setLineWidth(2);
                this.context.strokeOval(
                        field.getX(),
                        field.getY(),
                        FIELD_DIM,
                        FIELD_DIM
                );
            }
        }
    }

    private void drawBaseFields() {
        for (List<Field> fieldList:gameService.getAllPartBaseFields()) {
            for (Field field:fieldList) {
                this.context.setFill(Color.web(field.getColor().getName()));
                this.context.fillOval(
                        field.getX(),
                        field.getY(),
                        FIELD_DIM,
                        FIELD_DIM
                );
                this.context.setStroke(Color.BLACK);
                this.context.setLineWidth(2);
                this.context.strokeOval(
                        field.getX(),
                        field.getY(),
                        FIELD_DIM,
                        FIELD_DIM
                );
            }
        }
    }

    private void drawPieces() {
        drawPiecesOfNotCurrentPlayer();
        drawPiecesOfCurrentPlayer();
    }

    private void drawPiecesOfNotCurrentPlayer() {
        for (Player player:gameService.getPlayers()) {
            if (player == gameService.getCurrentPlayer()) {
                continue;
            }
            for (Piece piece: player.getPieces()) {
                drawOnePiece(piece);
            }
        }
    }

    private void drawPiecesOfCurrentPlayer() {
        for (Piece piece: gameService.getCurrentPlayer().getPieces()) {
            drawOnePiece(piece);
        }
    }

    private void drawOnePiece(Piece piece) {
        this.context.setFill(Color.web(piece.getColor().getName()));
        this.context.fillRect(
                piece.getX() + FIELD_DIM / 4,
                piece.getY() + FIELD_DIM / 4,
                PIECE_DIM,
                PIECE_DIM
        );
        this.context.setStroke(Color.BLACK);
        this.context.setLineWidth(3);
        this.context.strokeRect(
                piece.getX() + FIELD_DIM / 4,
                piece.getY() + FIELD_DIM / 4,
                PIECE_DIM,
                PIECE_DIM
        );
    }
    /*================================================================*/

    /*===================== PropertyChangeEvent ======================*/

    public void rerenderPlayers(PropertyChangeEvent propertyChangeEvent) {
        playersBox.getChildren().clear();

        for (Controller controller: playerSubControllerList) {
            controller.destroy();
        }
        playerSubControllerList.clear();

        for (Player player:gameService.getPlayers()) {
            Controller playerSubController = new PlayerSubController(app, gameService, player);
            playerSubController.init();
            playerSubControllerList.add(playerSubController);
            playersBox.getChildren().add(playerSubController.render());
        }
    }

    private void rerenderDice(PropertyChangeEvent propertyChangeEvent) {
        diceVBox.getChildren().clear();
        if (diceController != null) {
            diceController.destroy();
        }
        diceController = new DiceSubController(app, gameService, gameService.getCurrentPlayer(), gameService.getCurrentPlayer().getDice());
        diceController.init();
        this.diceVBox.getChildren().add(diceController.render());
    }

    private void callHandleFirstRound(PropertyChangeEvent changeEvent) {
        if (gameService.getGame().isFirstRound()) {
            canvasBox.setEffect(new BoxBlur(10, 10, 6));
            firstRoundLabel.setText("Decide who starts the game!");
        } else {
            canvasBox.setEffect(null);
            firstRoundLabel.setText("Game has started!");
        }
    }

    private void handlePieceClicked(double mouseX, double mouseY) {
        // prevent double click at the same time
        if (gameService.getGame().isActive() || lastTimeClicked == System.currentTimeMillis()) {
            lastTimeClicked = System.currentTimeMillis();
            return;
        }

        // prevent click while moving
        gameService.setIsActive(true);
        lastTimeClicked = System.currentTimeMillis();

        // check if piece is clicked
        for (Piece piece: gameService.getCurrentPlayer().getPieces()) {
            boolean pieceIsClicked = piece.getPieceField().getX() <= mouseX && mouseX <= piece.getPieceField().getX() + FIELD_DIM && piece.getPieceField().getY() <= mouseY && mouseY <= piece.getPieceField().getY() + FIELD_DIM;
            List<Field> path = gameService.getPiecePath(piece);
            if (pieceIsClicked && path != null) {
                nextStep(path, piece, path.get(path.size() - 1));
                return;
            }
        }

        // piece had no path
        gameService.setIsActive(false);
    }

    private void nextStep(List<Field> fields, Piece piece, Field lastField) {
        // piece is moving
        if (fields.size() > 0) {
            Location nextLocation = fields.get(0);
            gameService.setPieceNewPosition(piece, nextLocation);
            fields.remove(0);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(TRAVEL_TIME/2), event -> nextStep(fields, piece, lastField)));
            timeline.play();
        } else {
            gameService.kickPiece(lastField.getPiece());
            gameService.setNewNextFieldForPiece(piece, lastField);
            gameService.setIsActive(false);
            if (!gameService.checkWinner()) {
                gameService.switchToNextPlayer();
            } else {
                app.showGameOverView();
            }
        }
    }
    /*================================================================*/

    @Override
    public void destroy() {
        animationTimer.stop();
        game.listeners().removePropertyChangeListener(Game.PROPERTY_CURRENT_PLAYER,callRerenderDice);
        game.listeners().removePropertyChangeListener(Game.PROPERTY_FIRST_ROUND, callRerenderPlayers);
        game.listeners().removePropertyChangeListener(Game.PROPERTY_FIRST_ROUND, callHandleFirstRound);

        for (Controller controller: playerSubControllerList) {
            controller.destroy();
        }
        super.destroy();
    }
}
