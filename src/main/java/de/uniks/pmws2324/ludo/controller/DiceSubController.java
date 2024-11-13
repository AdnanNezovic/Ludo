package de.uniks.pmws2324.ludo.controller;

import de.uniks.pmws2324.ludo.App;
import de.uniks.pmws2324.ludo.Main;
import de.uniks.pmws2324.ludo.model.Dice;
import de.uniks.pmws2324.ludo.model.Player;
import de.uniks.pmws2324.ludo.service.GameService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class DiceSubController extends Controller {

    private Parent parent;
    private final GameService gameService;
    private final Player currentPlayer;
    private final Dice dice;

    PropertyChangeListener callHandleNumberOfDiceChanged;
    PropertyChangeListener callHandleActionChanged;

    @FXML
    Label actionLabel;
    @FXML
    Button rollDiceButton;
    @FXML
    Label numberOfDiceLabel;

    @FXML
    Label currentPlayerLabel;
    @FXML
    VBox diceVBox2;

    public DiceSubController(App app, GameService gameService, Player currentPlayer, Dice dice) {
        super(app, gameService);
        this.gameService = gameService;
        this.currentPlayer = currentPlayer;
        this.dice = dice;
    }

    @Override
    public void init() {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Dice.fxml"));
        loader.setControllerFactory(c -> this);
        try {
            this.parent = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Parent render() {
        rollDiceButton.setOnAction(event -> gameService.throwDice());

        // current player
        currentPlayerLabel.setFont(Font.font("Book Antiqua", 25));
        currentPlayerLabel.setText("Turn: " + currentPlayer.getName());

        diceVBox2.setStyle("-fx-background-color: " + currentPlayer.getColor().getName() + ";" +
                "-fx-border-color: #000;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;");

        // action status
        handleActionChanged(null);
        callHandleActionChanged = this::handleActionChanged;
        currentPlayer.listeners().addPropertyChangeListener(Player.PROPERTY_HAS_ROLLED, callHandleActionChanged);

        // number of dice
        numberOfDiceLabel.setText("" + dice.getNumber());
        callHandleNumberOfDiceChanged = evt -> numberOfDiceLabel.setText("" + dice.getNumber());
        dice.listeners().addPropertyChangeListener(Dice.PROPERTY_NUMBER, callHandleNumberOfDiceChanged);

        return this.parent;
    }

    private void handleActionChanged(PropertyChangeEvent propertyChangeEvent) {
        if (gameService.getCurrentPlayer().isHasRolled()) {
            rollDiceButton.setDisable(true);
            actionLabel.setText("Move a piece");
        } else {
            rollDiceButton.setDisable(false);
            actionLabel.setText("Roll the dice");
        }
    }

    @Override
    public void destroy() {
        currentPlayer.listeners().removePropertyChangeListener(Player.PROPERTY_HAS_ROLLED, callHandleActionChanged);
        dice.listeners().removePropertyChangeListener(Dice.PROPERTY_NUMBER, callHandleNumberOfDiceChanged);
        super.destroy();
    }
}
