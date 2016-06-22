package com.chess.engine.board;

import java.util.HashMap;
import java.util.Map;

/**
 * the BoardUtils class
 * @author Daniel Wakefield
 * @version 1.0
 */
public class BoardUtils {

    // some global constants
    public static final int NUM_ROWS = 8;
    public static final int NUM_COLS = 8;
    public static final int NUM_TILES = NUM_COLS*NUM_ROWS;

    // a cache of empty tiles
    static Map<Integer, Tile> emptyTiles = createEmptyTiles();

    /**
     * is the input coordinate valid
     * @param coordinate the coordinate to check the validity of
     * @return true if valid, false otherwise
     */
    public static boolean isValidCoordinate(int coordinate) {
        return 0 <= coordinate && coordinate < NUM_TILES;
    }

    /**
     * create the empty tile cache
     * @return a Map of empty tiles
     */
    private static Map<Integer, Tile> createEmptyTiles() {
        HashMap<Integer, Tile> tiles = new HashMap<>();
        for (int i = 0; i < NUM_TILES; i++) {
            tiles.put(i, new Tile(i));
        }
        return tiles;
    }
}
