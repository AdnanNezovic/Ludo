package de.uniks.pmws2324.ludo.controller;

import de.uniks.pmws2324.ludo.App;
import de.uniks.pmws2324.ludo.Main;
import de.uniks.pmws2324.ludo.model.Color;
import de.uniks.pmws2324.ludo.service.GameService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.LinkedHashMap;

import static de.uniks.pmws2324.ludo.Constants.*;

public class SetupController extends Controller {

    Parent parent;
    private LinkedHashMap<Color, String> playerNames;

    @FXML
    TextField yellowNameTextfield;
    @FXML
    TextField greenNameTextfield;
    @FXML
    TextField redNameTextfield;
    @FXML
    TextField blueNameTextfield;
    @FXML
    Button startGameButton;
    @FXML
    Button exitGameButton;

    public SetupController(App app, GameService gameService) {
        super(app, gameService);
    }

    @Override
    public void init() {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Setup.fxml"));
        loader.setControllerFactory(c -> this);
        try {
            this.parent = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        title = "Setup - Screen";
    }

    @Override
    public Parent render() {
        this.playerNames = new LinkedHashMap<>();
        startGameButton.setOnAction(this::handlePlayerNames);
        startGameButton.setDisable(true);

        exitGameButton.setOnAction(evt -> app.exit());

        yellowNameTextfield.textProperty().addListener((obs, oldVal, newVal) -> updateStartButtonStatus());
        greenNameTextfield.textProperty().addListener((obs, oldVal, newVal) -> updateStartButtonStatus());
        redNameTextfield.textProperty().addListener((obs, oldVal, newVal) -> updateStartButtonStatus());
        blueNameTextfield.textProperty().addListener((obs, oldVal, newVal) -> updateStartButtonStatus());

        return this.parent;
    }

    private void updateStartButtonStatus() {
        int validNameCounter = 0;

        if (isValidName(yellowNameTextfield.getText())) validNameCounter++;
        if (isValidName(greenNameTextfield.getText())) validNameCounter++;
        if (isValidName(redNameTextfield.getText())) validNameCounter++;
        if (isValidName(blueNameTextfield.getText())) validNameCounter++;

        // activate start button if at least two names are valid
        startGameButton.setDisable(validNameCounter < 2);
    }
    
    private boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.trim().length() <= 18;
    }

    private void handlePlayerNames(ActionEvent evt) {
        if (!yellowNameTextfield.getText().trim().isEmpty()) {
            playerNames.put(YELLOW, yellowNameTextfield.getText());
        }

        if (!greenNameTextfield.getText().trim().isEmpty()) {
            playerNames.put(GREEN, greenNameTextfield.getText());
        }

        if (!redNameTextfield.getText().trim().isEmpty()) {
            playerNames.put(RED, redNameTextfield.getText());
        }

        if (!blueNameTextfield.getText().trim().isEmpty()) {
            playerNames.put(BLUE, blueNameTextfield.getText());
        }
        
        app.showIngameView(this.playerNames);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
