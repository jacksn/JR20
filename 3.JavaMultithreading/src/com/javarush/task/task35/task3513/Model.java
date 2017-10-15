package com.javarush.task.task35.task3513;

import java.util.*;
import java.util.stream.Collector;

public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles;
    private Stack<Tile[][]> previousStates = new Stack<>();
    private Stack<Integer> previousScores = new Stack<>();
    private boolean isSaveNeeded = true;

    int score;
    int maxTile;

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    public Model() {
        gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        resetGameTiles();
    }

    private void saveState(Tile[][] tiles) {
        Tile[][] stateToSave = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                stateToSave[i][j] = new Tile(tiles[i][j].value);
            }
        }
        previousStates.push(stateToSave);
        previousScores.push(score);
        isSaveNeeded = false;
    }

    public void rollback() {
        if (!previousStates.isEmpty() && !previousScores.isEmpty()) {
            gameTiles = previousStates.pop();
            score = previousScores.pop();
        }
    }

    void resetGameTiles() {
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                gameTiles[i][j] = new Tile();
            }
        }
        addTile();
        addTile();
        score = 0;
        maxTile = 2;
    }

    private void addTile() {
        List<Tile> emptyTiles = getEmptyTiles();
        if (emptyTiles.isEmpty()) {
            return;
        }
        Tile tile = emptyTiles.get((int) (Math.random() * emptyTiles.size()));
        tile.value = Math.random() < 0.9 ? 2 : 4;
        isSaveNeeded = true;
    }

    private List<Tile> getEmptyTiles() {
        List<Tile> emptyTiles = new ArrayList<>();
        for (Tile[] row : gameTiles) {
            for (Tile tile : row) {
                if (tile.isEmpty()) {
                    emptyTiles.add(tile);
                }
            }
        }
        return emptyTiles;
    }

    public boolean canMove() {
        if (!getEmptyTiles().isEmpty()) {
            return true;
        }

        for (int i = 0; i < FIELD_WIDTH - 1; i++) {
            for (int j = 0; j < FIELD_WIDTH - 1; j++) {
                if (gameTiles[i][j].value == gameTiles[i][j + 1].value || gameTiles[i][j].value == gameTiles[i + 1][j].value) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean mergeTiles(Tile[] tiles) {
        boolean tilesChanged = false;
        for (int i = 0; i < FIELD_WIDTH - 1; i++) {
            if (tiles[i].value == tiles[i + 1].value) {
                tiles[i].value = tiles[i].value * 2;
                if (tiles[i].value > maxTile) {
                    tilesChanged = true;
                    maxTile = tiles[i].value;
                }
                score += tiles[i].value;
                for (int j = i + 1; j < FIELD_WIDTH - 1; j++) {
                    tiles[j].value = tiles[j + 1].value;
                }
                tiles[FIELD_WIDTH - 1].value = 0;
            }
        }
        return tilesChanged;
    }

    private boolean compressTiles(Tile[] tiles) {
        boolean tilesChanged = false;
        for (int i = 0; i < FIELD_WIDTH - 1; i++) {
            if (tiles[i].isEmpty()) {
                for (int j = i + 1; j < FIELD_WIDTH; j++) {
                    if (!tiles[j].isEmpty()) {
                        tilesChanged = true;
                        tiles[i].value = tiles[j].value;
                        tiles[j].value = 0;
                        break;
                    }
                }
            }
        }
        return tilesChanged;
    }

    void left() {
        if (isSaveNeeded) {
            saveState(gameTiles);
            isSaveNeeded = false;
        }
        for (Tile[] row : gameTiles) {
            boolean tilesChanged = compressTiles(row) | mergeTiles(row);
            if (tilesChanged) {
                addTile();
            }
        }
    }

    void right() {
        if (isSaveNeeded) {
            saveState(gameTiles);
            isSaveNeeded = false;
        }
        boolean tilesChanged = false;
        for (Tile[] row : gameTiles) {
            Tile[] tempRow = new Tile[row.length];
            for (int i = 0; i < row.length; i++) {
                tempRow[row.length - i - 1] = row[i];
            }
            tilesChanged = compressTiles(tempRow) | mergeTiles(tempRow);
        }
        if (tilesChanged) {
            addTile();
        }
    }

    void up() {
        if (isSaveNeeded) {
            saveState(gameTiles);
            isSaveNeeded = false;
        }
        boolean tilesChanged = false;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            Tile[] tempRow = new Tile[FIELD_WIDTH];
            for (int j = 0; j < FIELD_WIDTH; j++) {
                tempRow[j] = gameTiles[j][i];
            }
            tilesChanged = compressTiles(tempRow) | mergeTiles(tempRow);
        }
        if (tilesChanged) {
            addTile();
        }
    }

    void down() {
        if (isSaveNeeded) {
            saveState(gameTiles);
            isSaveNeeded = false;
        }
        boolean tilesChanged = false;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            Tile[] tempRow = new Tile[FIELD_WIDTH];
            for (int j = 0; j < FIELD_WIDTH; j++) {
                tempRow[j] = gameTiles[FIELD_WIDTH - j - 1][i];
            }
            tilesChanged = compressTiles(tempRow) | mergeTiles(tempRow);
        }
        if (tilesChanged) {
            addTile();
        }
    }

    public void randomMove() {
        int n = ((int) (Math.random() * 100)) % 4;
        switch (n) {
            case 0:
                left();
                break;
            case 1:
                right();
                break;
            case 2:
                up();
                break;
            case 3:
                down();
                break;
        }
    }

    boolean hasBoardChanged() {
        if (!previousStates.isEmpty()) {
            int tilesScore = 0;
            int previousTilesScore = 0;
            Tile[][] previousState = previousStates.peek();

            for (int i = 0; i < FIELD_WIDTH; i++) {
                for (int j = 0; j < FIELD_WIDTH; j++) {
                    tilesScore += gameTiles[i][j].value;
                    previousTilesScore += previousState[i][j].value;
                }
            }
            return tilesScore != previousTilesScore;
        }
        return false;
    }

    MoveEfficiency getMoveEfficiency(Move move) {
        move.move();
        MoveEfficiency moveEfficiency;

        if (hasBoardChanged()) {
            moveEfficiency = new MoveEfficiency(getEmptyTiles().size(), score, move);
        } else {
            moveEfficiency = new MoveEfficiency(-1, 0, move);
        }

        rollback();
        return moveEfficiency;
    }

    void autoMove() {
        PriorityQueue<MoveEfficiency> queue = new PriorityQueue<>(4, Collections.reverseOrder());
        queue.offer(getMoveEfficiency(this::left));
        queue.offer(getMoveEfficiency(this::right));
        queue.offer(getMoveEfficiency(this::up));
        queue.offer(getMoveEfficiency(this::down));
        queue.poll().getMove().move();
    }
}
