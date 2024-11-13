package de.uniks.pmws2324.ludo.controller;

import de.uniks.pmws2324.ludo.App;
import de.uniks.pmws2324.ludo.Main;
import de.uniks.pmws2324.ludo.model.Dice;
import de.uniks.pmws2324.ludo.model.Player;
import de.uniks.pmws2324.ludo.service.GameService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeListener;
import java.io.IOException;

public class PlayerSubController extends Controller {
    private Parent parent;
    private final GameService gameService;
    private final Player player;

    PropertyChangeListener callHandleNumberOfDiceChanged;

    @FXML
    Label playerName;
    @FXML
    AnchorPane namePlayerBox;
    @FXML
    Label lastNumberThrown;
    @FXML
    VBox colorPlayerBox;


    PlayerSubController(App app, GameService gameService, Player player) {
        super(app, gameService);
        this.gameService = gameService;
        this.player = player;
    }

    @Override
    public void init() {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Player.fxml"));
        loader.setControllerFactory(c -> this);
        try {
            this.parent = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Parent render() {
        if (player == gameService.getCurrentPlayer()) {
            namePlayerBox.setStyle("-fx-border-color: " + this.player.getColor() + "; " +
                    "-fx-border-width: 5px; " +
                    "-fx-border-radius: 5px;");
        }

        colorPlayerBox.setStyle("-fx-background-color: " + player.getColor().getName() + "; " +
                "-fx-border-color: black; " +
                "-fx-border-width: 3px; " +
                "-fx-border-radius: 5px; " +
                "-fx-background-radius: 5px;");

        playerName.setText(this.player.getName());

        // number of dice
        lastNumberThrown.setText("" + player.getDice().getNumber());
        callHandleNumberOfDiceChanged = evt -> lastNumberThrown.setText("" + player.getDice().getNumber());
        player.getDice().listeners().addPropertyChangeListener(Dice.PROPERTY_NUMBER, callHandleNumberOfDiceChanged);

        return this.parent;
    }

    @Override
    public void destroy() {
        player.getDice().listeners().removePropertyChangeListener(Dice.PROPERTY_NUMBER, callHandleNumberOfDiceChanged);
        super.destroy();
    }
}
