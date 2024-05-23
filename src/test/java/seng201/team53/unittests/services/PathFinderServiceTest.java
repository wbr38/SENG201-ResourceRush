package seng201.team53.unittests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng201.team53.game.map.Tile;
import seng201.team53.service.PathFinderService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PathFinderServiceTest {
    private PathFinderService pathFinderService;

    @BeforeEach
    void beforeEachTest() {
        pathFinderService = new PathFinderService();
    }

    @Test
    void testNoPath() {
        int[][] matrix = new int[][] {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}};
        Tile[][] tiles = buildTileMapFromMatrix(matrix);
        assertThrows(RuntimeException.class, () -> pathFinderService.findPath(tiles, 0,0, 7, 7));
    }

    @Test
    void testDisconnectedPath() {
        int[][] matrix = new int[][] {
                {1, 1, 1, 1, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 1}};
        Tile[][] tiles = buildTileMapFromMatrix(matrix);
        assertThrows(RuntimeException.class, () -> pathFinderService.findPath(tiles, 0,0, 7, 7));
    }

    @Test
    void testValidShortPaths() {
        int[][] matrix = new int[][] {
                {1, 1, 1, 1, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 1}};
        Tile[][] tiles = buildTileMapFromMatrix(matrix);
        assertDoesNotThrow(() -> pathFinderService.findPath(tiles, 0,0, 7, 7));
    }
    @Test
    void testValidLongPaths() {
        int[][] matrix = new int[][] {
                {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
        };
        Tile[][] tiles = buildTileMapFromMatrix(matrix);
        assertDoesNotThrow(() -> pathFinderService.findPath(tiles, 0,0, 14, 14));
    }

    private Tile[][] buildTileMapFromMatrix(int[][] matrix) {
        Tile[][] tiles = new Tile[matrix.length][matrix[0].length];
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                tiles[y][x] = new Tile(false, matrix[y][x] == 1, x, y);
            }
        }
        return tiles;
    }
}
