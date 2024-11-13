package de.uniks.pmws2324.ludo.service;

import de.uniks.pmws2324.ludo.model.*;

import java.util.*;

import static de.uniks.pmws2324.ludo.Constants.*;

public class GameService {

    private final List<Player> players = new ArrayList<>();
    private Player currentPlayer;
    private final LinkedHashMap<Color,String> playerNames;
    private final Random rnGenerator;
    private final List<List<Field>> allPartGameAndHomeFields = new ArrayList<>();
    private final List<List<Field>> allPartBaseFields = new ArrayList<>();
    private Game game;
    private Map<Player, Integer> playerRank = new LinkedHashMap<>(); // sorted by number of pieces at home
    private final boolean testRun;

    public GameService(LinkedHashMap<Color, String> namesOfPlayer, boolean testRun) {
        this.playerNames = namesOfPlayer;
        this.testRun = testRun;
        if (testRun) {
            this.rnGenerator = new Random(3927445);
        } else {
            this.rnGenerator = new Random();
        }
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public List<List<Field>> getAllPartGameAndHomeFields() {
        return this.allPartGameAndHomeFields;
    }

    public List<List<Field>> getAllPartBaseFields() {return this.allPartBaseFields;}

    public HashMap<Player, Integer> getRank() {
        return (HashMap<Player, Integer>) playerRank;
    }

    public Game getGame() {
        return this.game;
    }

    public GameService initGame() {
        initAllGameAndHomeFields();
        initAllBaseFields();
        initPlayersAndPieces();
        setReferencesOfPlayers();
        setCoordinatesOfPieces();
        connectBaseFieldsToStartFields();

        this.currentPlayer = players.get(rnGenerator.nextInt(players.size()));
        game = new Game().setCurrentPlayer(this.currentPlayer).setFirstRound(true);

        return this;
    }

    /*=================== init Methods and rotate Method =====================*/

    private void initAllGameAndHomeFields() {
        List<Field> lastPartGameFields = initFirstPartGameAndHomeFields();
        for (int i = 1; i < 4; ++i) {
            Color startFieldColor = COLOR_LIST.get(i);
            Color homeFieldsColor;
            if (i + 1 > 3) {
                homeFieldsColor = COLOR_LIST.get(0);
            } else {
                homeFieldsColor = COLOR_LIST.get(i+1);
            }
            lastPartGameFields = initRestPartGameAndHomeFields(lastPartGameFields, startFieldColor, homeFieldsColor);
        }

        Field lastSpecialField = allPartGameAndHomeFields.get(3).get(9);
        Field firstStartField = allPartGameAndHomeFields.get(0).get(0);
        lastSpecialField.setNextField(firstStartField);
    }

    private List<Field> initFirstPartGameAndHomeFields() {
        List<Field> firstPartGameField = new ArrayList<>();
        // Set gameFields
        Field currentField = new Field().setType(GAMEFIELD).setColor(YELLOW);
        for (int i = 0; i < SPECIAL_FIELD_INDEX; ++i) {
            if (i < 5) {
                currentField.setX(STARTPOINT_YELLOW_X + i * CANVAS_STEP);
                currentField.setY(STARTPOINT_YELLOW_Y);
            } else {
                currentField.setX(STARTPOINT_YELLOW_X + 4*CANVAS_STEP);
                currentField.setY(STARTPOINT_YELLOW_Y - (i+1)%5 * CANVAS_STEP);
            }
            Field nextField = new Field().setType(GAMEFIELD);
            currentField.setNextField(nextField);
            firstPartGameField.add(currentField);
            currentField = nextField;
        }

        Field lastField = firstPartGameField.get(8);
        Field currentField1 = new Field().setType(GAMEFIELD);
        lastField.setNextField(currentField1);

        // Set homeFields
        for (int i = 0; i < 5; ++i) {
            firstPartGameField.add(currentField1);
            currentField1.setX(lastField.getX() + CANVAS_STEP);
            currentField1.setY(lastField.getY() + i*CANVAS_STEP);
            Field nextField = new Field().setType(HOMEFIELD).setColor(GREEN);
            if (i != 5-1) {
                currentField1.setNextHomeField(nextField);
                currentField1 = currentField1.getNextHomeField();
            }
        }
        allPartGameAndHomeFields.add(firstPartGameField);
        return firstPartGameField;
    }

    private List<Field> initRestPartGameAndHomeFields(List<Field> lastPartGameFields, Color startFieldColor, Color homeFieldsColor) {
        List<Field> newPartGameFields = new ArrayList<>();
        for (Field field:lastPartGameFields) {
            Field newField = rotateNinetyDegree(field.getX(), field.getY());
            if (field.getColor() != null) {
                if (Objects.equals(field.getType(), GAMEFIELD)) { // startField
                    newField.setColor(startFieldColor).setType(GAMEFIELD);
                } else { // homeField
                    newField.setColor(homeFieldsColor).setType(HOMEFIELD);
                }
            } else { // gameField without color
                newField.setType(GAMEFIELD);
            }
            newPartGameFields.add(newField);
        }

        // set references for gameFields
        for (int i = 0; i < SPECIAL_FIELD_INDEX; ++i) {
            Field currentField = newPartGameFields.get(i);
            Field nextField = newPartGameFields.get(i+1);
            currentField.setNextField(nextField);
        }

        // set references for homeFields
        for (int i = SPECIAL_FIELD_INDEX; i < newPartGameFields.size() - 1; ++i) {
            Field currentField = newPartGameFields.get(i);
            Field nextField = newPartGameFields.get(i+1);
            if (i != newPartGameFields.size() - 1) {
                currentField.setNextHomeField(nextField);
            }
        }

        // set reference for specialField
        lastPartGameFields.get(SPECIAL_FIELD_INDEX).setNextField(newPartGameFields.get(START_FIELD_INDEX));

        allPartGameAndHomeFields.add(newPartGameFields);
        return newPartGameFields;
    }

    private void initAllBaseFields() {
        List<Field> lastPartBaseFields = initFirstPartBaseFields();
        for (int i = 1; i < NUMBER_OF_BASES; ++i) {
            Color currentColor = COLOR_LIST.get(i);
            lastPartBaseFields = initRestPartBaseFields(lastPartBaseFields, currentColor);
        }
    }

    private List<Field> initFirstPartBaseFields() {
        // init yellow baseFields
        Field firstPartFieldsFirstField = allPartGameAndHomeFields.get(0).get(0);
        List<Field> firstPartBaseFields = new ArrayList<>();
        for (int i = 1; i <= 2; ++i) {
            for (int j = 1; j <= 2; ++j) {
                Field baseField = (Field) new Field().setX(i*CANVAS_STEP).setY(j*CANVAS_STEP);
                baseField.setType(BASEFIELD)
                        .setNextField(firstPartFieldsFirstField)
                        .setColor(YELLOW);
                firstPartBaseFields.add(baseField);
            }
        }
        allPartBaseFields.add(firstPartBaseFields);
        return firstPartBaseFields;
    }

    private List<Field> initRestPartBaseFields(List<Field> lastPartBaseFields, Color color) {
        List<Field> newPartBaseFields = new ArrayList<>();
        Field startField = allPartGameAndHomeFields.get(COLOR_LIST.indexOf(color)).get(0);

        for (Field field:lastPartBaseFields) {
            Field newField = rotateNinetyDegree(field.getX(), field.getY());
            newField.setNextField(startField).setType(BASEFIELD).setColor(color);
            newPartBaseFields.add(newField);
        }

        allPartBaseFields.add(newPartBaseFields);
        return newPartBaseFields;
    }

    private Field rotateNinetyDegree(double x, double y) {
        double translatedX = x - CENTER_X;
        double translatedY = y - CENTER_Y;

        double rotatedX = -translatedY;
        double rotatedY = translatedX;

        x = rotatedX + CENTER_X;
        y = rotatedY + CENTER_Y;

        return (Field) new Field().setX(x).setY(y);
    }

    private void initPlayersAndPieces() {
        for (Map.Entry<Color, String> entry : playerNames.entrySet()) {
            Color color = entry.getKey();
            String playerName = entry.getValue();
            Player player = new Player().setDice(new Dice())
                    .setName(playerName)
                    .setColor(color);
            Field startField = allPartGameAndHomeFields.get(COLOR_LIST.indexOf(color)).get(0);
            player.setStartField(startField);
            initPiecesOfPlayer(player, COLOR_LIST.indexOf(color));
            this.players.add(player);
        }
    }

    private void initPiecesOfPlayer(Player player, int indexOfPartBaseFields) {
        for (int i = 0; i < 4; ++i) {
            Piece piece = new Piece().setColor(player.getColor());
            player.withPieces(piece);
            player.withBaseFields(allPartBaseFields.get(indexOfPartBaseFields));
            piece.setPieceField(player.getBaseFields().get(i));
        }
    }

    private void setCoordinatesOfPieces() {
        for (Player player:this.players) {
            for (int i = 0; i < 4; ++i) {
                Piece piece = player.getPieces().get(i);
                Field baseField = player.getBaseFields().get(i);
                piece.setX(baseField.getX()).setY(baseField.getY());
            }
        }
    }

    private void setReferencesOfPlayers() {
        for (int i = 0; i < players.size() - 1; ++i) {
            Player player = players.get(i);
            Player nextPlayer = players.get(i + 1);
            player.setNextPlayer(nextPlayer);
        }
        Player lastPlayer = players.get(players.size() - 1);
        Player firstPlayer = players.get(0);
        lastPlayer.setNextPlayer(firstPlayer);
    }

    private void connectBaseFieldsToStartFields() {
        for (int i = 0; i < 4; ++i) {
            Field startField = allPartGameAndHomeFields.get(i).get(0);
            for (int j = 0; j < 4; ++j) {
                Field baseField = allPartBaseFields.get(i).get(j);
                baseField.setNextField(startField);
            }
        }
    }
    /*=================================================================*/

    /*========================= game logic methods ====================*/
    public void throwDice() {
        if (currentPlayer.isHasRolled()) {
            return;
        }

        currentPlayer.setHasRolled(true);
        int randomNumber;
        if (testRun) {
            randomNumber = rnGenerator.nextInt(2) == 0 ? 1 : 6;
        } else {
            randomNumber = rnGenerator.nextInt(1, 7);
        }
        currentPlayer.getDice().setNumber(randomNumber);

        if (game.isFirstRound()) { // first round
            handleFirstRound();
        } else if (!movePieceIsPossible()) { // no move possible
            currentPlayer.setCanMoveAPiece(false);
            switchToNextPlayer();
        } else {
            currentPlayer.setCanMoveAPiece(true);
        }
    }

    private void handleFirstRound() {
        currentPlayer.setPriority(currentPlayer.getDice().getNumber());
        if (game.getFirstRoundInt() == players.size() - 1) {
            this.currentPlayer.setHasRolled(false);
            this.game.setFirstRound(false);
            decideFirstPlayerAndFinallyStartGame();
        } else {
            switchToNextPlayer();
        }
        game.setFirstRoundInt(game.getFirstRoundInt() + 1);
    }

    public void switchToNextPlayer() {
        currentPlayer.setHasRolled(false);
        if (currentPlayer.getDice().getNumber() == 6 && !game.isFirstRound() && currentPlayer.isCanMoveAPiece()) {
            currentPlayer.setCanMoveAPiece(false);
            return;
        }
        this.currentPlayer = currentPlayer.getNextPlayer();
        game.setCurrentPlayer(currentPlayer);
    }

    private void decideFirstPlayerAndFinallyStartGame() {
        game.setFirstRoundInt(players.size()+1);
        game.setFirstRound(false);
        Player decideFirstPlayer = currentPlayer;
        if (decideFirstPlayer.getPriority() != 6) {
            for (Player player : players) {
                if (player.getPriority() > decideFirstPlayer.getPriority()) {
                    decideFirstPlayer = player;
                }
            }
        }
        this.currentPlayer = decideFirstPlayer;
        this.currentPlayer.setHasRolled(false);
        this.game.setCurrentPlayer(currentPlayer);
    }

    public List<Field> getPiecePath(Piece piece) {
        // player has not thrown
        if (!piece.getPieceOwner().isHasRolled()) { // is always currentPlayer
            return null;
        }
        return getPiecePathHelp(piece);
    }

    private List<Field> getPiecePathHelp(Piece piece) {
        List<Field> path = new ArrayList<>();

        // 6 thrown move piece from baseField to startField
        if (piece.getPieceOwner().getDice().getNumber() == 6 && Objects.equals(piece.getPieceField().getType(), BASEFIELD)) {
            Field startField = piece.getPieceField().getNextField();
            if (startField.getPiece() != null && startField.getPiece().getColor() == piece.getColor()) {
                return null;
            }
            path.add(startField);
            return path;
        }

        // 6 not thrown and piece on baseField
        if (Objects.equals(piece.getPieceField().getType(), BASEFIELD)) {
            return null;
        }

        // current player has moveable pieces on baseField and has thrown 6
        if (!Objects.equals(piece.getPieceField().getType(), BASEFIELD) && piece.getPieceOwner().getDice().getNumber() == 6 && playerHasMoveablePiecesOnBaseField()) {
            return null;
        }

        Player currentPlayer = piece.getPieceOwner();
        Field startField = currentPlayer.getStartField();

        // current player has another moveable piece on startField
        if (piece.getPieceField() != startField && startField.getPiece() != null && startField.getPiece().getColor() == currentPlayer.getColor()) {
            if (getPiecePathHelp(startField.getPiece()) != null) {
                return null;
            }
        }

        // 1, 2, 3, 4, 5, 6 thrown and piece lay on game or homeField
        Field iterField = piece.getPieceField();
        for (int i = 0; i < piece.getPieceOwner().getDice().getNumber(); ++i) {
            // piece is going out of bounds
            if (iterField.getNextField() == null && iterField.getNextHomeField() == null) {
                return null;
            }

            // piece is going to homeField
            if (iterField.getNextField() == null || (iterField.getNextField().getColor() == piece.getColor() && !Objects.equals(piece.getPieceField().getType(), BASEFIELD))) { // testen !!!!
                iterField = iterField.getNextHomeField();
                path.add(iterField);

            } else { // piece going to gameField
                iterField = iterField.getNextField();
                path.add(iterField);
            }
        }

        // piece is going to a field with the same color piece not allowed
        if (iterField.getPiece() != null && iterField.getPiece().getColor() == piece.getColor()) {
            return null;
        }

        // piece has a path. piece can move and eventually kick a piece
        return path;
    }

    private boolean playerHasMoveablePiecesOnBaseField() {
        for (Piece piece:this.currentPlayer.getPieces()) {
            if (Objects.equals(piece.getPieceField().getType(), BASEFIELD)) {
                if (getPiecePathHelp(piece) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean movePieceIsPossible() {
        for (Piece piece:currentPlayer.getPieces()) {
            if (getPiecePath(piece) != null) {
                return true;
            }
        }
        return false;
    }

    public void kickPiece(Piece kickedPiece) {
        // no piece to kick
        if (kickedPiece == null) {
            return;
        }

        // kick piece to baseField
        List<Field> baseFields = kickedPiece.getPieceOwner().getBaseFields();
        for (Field basefield : baseFields) {
            if (basefield.getPiece() == null) {
                basefield.setPiece(kickedPiece);
                kickedPiece.setX(kickedPiece.getPieceField().getX()).setY(kickedPiece.getPieceField().getY());
                return;
            }
        }
    }

    public void setNewNextFieldForPiece(Piece piece, Field field) {
        piece.setX(field.getX()).setY(field.getY());
        piece.setPieceField(field);
    }

    public void setPieceNewPosition(Piece piece, Location nextLocation) {
        piece.setX(nextLocation.getX()).setY(nextLocation.getY());
    }

    public boolean checkWinner() {
        for (Piece piece:currentPlayer.getPieces()) {
            if (!Objects.equals(piece.getPieceField().getType(), HOMEFIELD)) {
                return false;
            }
        }
        currentPlayer.setWinner(true);
        this.createRanking();
        return true;
    }

    public void createRanking() {
        for (Player player:players) {
            int piecesOnHomefields = 0;
            for (Piece piece:player.getPieces()) {
                if (Objects.equals(piece.getPieceField().getType(), HOMEFIELD)) {
                    piecesOnHomefields++;
                }
            }
            playerRank.put(player, piecesOnHomefields);
        }
        playerRank = sortByValue(playerRank);
    }

    private Map<Player, Integer> sortByValue(Map<Player, Integer> ranking) {
        // value is the number of pieces at home
        List<Map.Entry<Player, Integer>> list = new LinkedList<>(ranking.entrySet());
        list.sort((o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));

        Map<Player, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Player, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public void setIsActive(boolean active) {
        this.game.setActive(active);
    }
    /*=====================================================================*/
}
