package com.djam2.game.io;

import com.badlogic.gdx.files.FileHandle;
import com.djam2.game.map.Map;
import com.djam2.game.map.MapDefinition;
import com.djam2.game.map.MapLayer;
import com.djam2.game.tile.TileType;

import java.util.ArrayList;
import java.util.List;

public class MapImporter {

    private static final MapImporter INSTANCE = new MapImporter();

    public Map getMapFromFile(FileHandle file) {
        MapDefinition mapDefinition = new MapDefinition(3, 10, 10, 16, 16);

        Map map = new Map(mapDefinition, this.getTilesFromFile(file, mapDefinition));

        return map;
    }

    public List<MapLayer> getTilesFromFile(FileHandle file, MapDefinition mapDefinition) {
        List<MapLayer> mapLayers = new ArrayList<MapLayer>();

        String fileData = file.readString();

        String[] fileTileLayers = fileData.split("\n");

        for(String fileTileLayer : fileTileLayers) {
            String[] fileLayerTiles = fileTileLayer.split(",");

            List<TileType> layerTiles = new ArrayList<TileType>();

            for(String fileLayerTile : fileLayerTiles) {
                layerTiles.add(TileType.values()[Integer.parseInt(fileLayerTile)]);
            }

            mapLayers.add(new MapLayer(mapDefinition, layerTiles));
        }

        return mapLayers;
    }

    public static MapImporter getInstance() {
        return INSTANCE;
    }
}