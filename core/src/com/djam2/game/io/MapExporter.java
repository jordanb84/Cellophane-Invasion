package com.djam2.game.io;

import com.badlogic.gdx.files.FileHandle;
import com.djam2.game.map.Map;
import com.djam2.game.map.MapLayer;
import com.djam2.game.tile.TileType;

import java.util.List;

public class MapExporter {

    private static final MapExporter INSTANCE = new MapExporter();

    public void exportToFile(Map map, FileHandle file) {
        String tiles = this.getTilesAsString(map.getMapLayers());

        file.writeString(tiles, false);
    }

    private String getTilesAsString(List<MapLayer> mapLayers) {
        String tiles = ("");

        for(MapLayer mapLayer : mapLayers) {
            for(TileType tileType : mapLayer.getLayerTiles()) {
                tiles += (tileType.ordinal() + ",");
            }

            tiles += ("\n");
        }

        return tiles;
    }

    public static MapExporter getInstance() {
        return INSTANCE;
    }

}
