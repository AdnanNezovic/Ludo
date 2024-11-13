package de.uniks.pmws2324.ludo;

import de.uniks.pmws2324.ludo.controller.Controller;
import de.uniks.pmws2324.ludo.controller.GameOverController;
import de.uniks.pmws2324.ludo.controller.IngameController;
import de.uniks.pmws2324.ludo.controller.SetupController;
import de.uniks.pmws2324.ludo.model.Color;
import de.uniks.pmws2324.ludo.service.GameService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.LinkedHashMap;

public class App extends Application {

    private Stage stage;
    private Controller controller;
    private GameService gameService;

    private final boolean testRun;

    public App() {
        this.testRun = false;
    }

    public App(boolean testRun) {
        this.testRun = testRun;
    }

    public GameService getGameService() {
        return this.gameService;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        final Scene scene = new Scene(new Label("Loading..."));

        stage.setScene(scene);
        showSetupView();
        stage.show();
    }

    private void showSetupView() {
        Controller setUpController = new SetupController(this, gameService);
        show(setUpController);
    }

    public void showIngameView(LinkedHashMap<Color, String> playerNames) {
        gameService = new GameService(playerNames, testRun);
        Controller ingameViewController = new IngameController(this, gameService);
        stage.setWidth(1100);
        stage.setHeight(740);
        show(ingameViewController);
        stage.centerOnScreen();
    }

    public void showGameOverView() {
        Controller gameOverController = new GameOverController(this, gameService);
        stage.setWidth(798);
        stage.setHeight(610);
        show(gameOverController);
        stage.centerOnScreen();
    }

    private void show(Controller controller) {
        cleanup();
        this.controller = controller;
        initAndRender(controller);
    }

    private void initAndRender(Controller controller) {
        controller.init();
        stage.getScene().setRoot(controller.render());
        stage.setTitle(controller.getTitle());
    }

    private void cleanup() {
        if (controller != null) {
            controller.destroy();
            controller = null;
        }
    }

    public void exit() {
        System.exit(1);
    }
}
