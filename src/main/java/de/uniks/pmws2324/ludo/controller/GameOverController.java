package de.uniks.pmws2324.ludo.controller;

import de.uniks.pmws2324.ludo.App;
import de.uniks.pmws2324.ludo.Main;
import de.uniks.pmws2324.ludo.model.Player;
import de.uniks.pmws2324.ludo.service.GameService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Map;

public class GameOverController extends Controller {

    private Parent parent;
    private final GameService gameService;
    private final Map<Player, Integer> playerRank;

    @FXML
    VBox rankingPlayerBox;

    public GameOverController(App app, GameService gameService) {
        super(app, gameService);
        this.gameService = gameService;
        this.playerRank = gameService.getRank();
    }

    @Override
    public void init() {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/GameOver.fxml"));
        loader.setControllerFactory(c -> this);
        try {
            this.parent = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        title = "GameOver - Screen";
    }

    @Override
    public Parent render() {
        renderRankingList();
        return this.parent;
    }

    private void renderRankingList() {
        int rankNumber = 1;
        for (Map.Entry<Player, Integer> entry : playerRank.entrySet()) {
            Player player = entry.getKey();
            RankingSubController rankingSubController = new RankingSubController(app, gameService, player, rankNumber);
            rankingSubController.init();
            rankingPlayerBox.getChildren().add(rankingSubController.render());
            ++rankNumber;
        }
    }

    @Override
    public void destroy() {
        rankingPlayerBox.getChildren().clear();
        super.destroy();
    }
}
