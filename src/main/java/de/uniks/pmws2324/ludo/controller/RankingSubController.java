package de.uniks.pmws2324.ludo.controller;

import de.uniks.pmws2324.ludo.App;
import de.uniks.pmws2324.ludo.Main;
import de.uniks.pmws2324.ludo.model.Player;
import de.uniks.pmws2324.ludo.service.GameService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;

public class RankingSubController extends Controller {

    private Parent parent;
    private final GameService gameService;
    private final Player player;
    private final HashMap<Player, Integer> playerRank;
    private final int rankNumber;

    @FXML
    Label namePlayerRankLabel;
    @FXML
    VBox colorRankingBox;
    @FXML
    VBox namePlayerBox;
    @FXML
    Label rankNumberLabel;
    @FXML
    Label winnerLetterLabel;
    @FXML
    Label numberOfPieces;

    public RankingSubController(App app, GameService gameService, Player playerRank, int rankNumber) {
        super(app, gameService);
        this.player = playerRank;
        this.gameService = gameService;
        this.rankNumber = rankNumber;
        this.playerRank = gameService.getRank();
    }

    @Override
    public void init() {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/RankingPlayer.fxml"));
        loader.setControllerFactory(c -> this);
        try {
            this.parent = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Parent render() {
        rankNumberLabel.setText(rankNumber + ".");
        namePlayerRankLabel.setText(player.getName());

        colorRankingBox.setStyle("-fx-background-color: " + player.getColor().getName() + "; " +
                "-fx-border-color: black; " +
                "-fx-border-width: 3px; " +
                "-fx-border-radius: 5px; " +
                "-fx-background-radius: 5px;");

        if (player.isWinner()) {
            winnerLetterLabel.setText("W");
        }

        numberOfPieces.setText(playerRank.get(player) + " pieces " + "at home");
        return this.parent;
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
