package com.djam2.game.tile;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.djam2.game.assets.Assets;
import com.djam2.game.tile.impl.BasicTile;

public enum TileType {
    Air, Ground, Flowers, Crate(true), Start(), End(),

    Road0, Road1, Road2, Road3, Road4, Road5, Road6, Road7, Road8, Road9,
    Road10, Road11, Road12, Road13, Road14, Road15, Road16, Road17, Road18,
    Road19, Road20, Road21, Road22, Road23, Road24, Road25, Road26, Road27,
    Road28, Road29, Road30, Road31, Road32, Road33, Road34, Road35, Road36,
    Road37, Road38, Road39, Road40, Road41, Road42, Road43, Road44, Road45,
    Road46, Road47, Road48, Road49, Road50, Road51, Road52,

    Wall0(true), Wall1(true), Wall2(true), Wall3(true), Wall4(true),
    Wall5(true), Wall6(true),

    StoneWall0(true), StoneWall1(true), StoneWall2(true), StoneWall3(true),
    StoneWall4(true), StoneWall5(true), StoneWall6(true), StoneWall7(true),
    StoneWall8(true),

    Grave0(true), Grave1(true), Gas0(true), Gas1(true), Gas2(true), Fire(true),

    PathBlock,

    Blue0(true), Blue1(true), Blue2(true), Blue3(true), Blue4(true), Blue5(true),
    Blue6(true), Blue7(true), Blue8(true), Blue9(true), Blue10(true),

    Empty0(true), Empty1(true), Empty2(true), Empty3(true), Empty4(true), Empty5(true),
    Empty6(true), Empty7(true), Empty8(true), Empty9(true),

    Fence0(true), Fence1(true), Fence2(true), Fence3(true), Fence4(true), Fence5(true),
    Fence6(true), Fence7(true),

    Tire(true), Soda(true), Sign(true), Barrel(true), Skulls0(true), Skulls1(true),
    Box(true), Car0(true), Car1(true), Car2(true), Car3(true), Shrub, Barrel1(true),
    Govern0(true), Govern1(true), Govern2(true), Govern3(true),

    Green0(true), Green1(true), Green2(true), Green3(true), Green4(true), Green5(true),
    Green6(true), Green7(true), Green8(true), Green9(true), Green10(true), Green11(true),
    Green12(true), Green13(true), Green14(true)
    ;

    TileType(String spritePath, boolean solid) {
        this.SPRITE = new Sprite(Assets.getInstance().getTexture("tile/" + spritePath));
        this.SOLID = solid;
        this.TILE = new BasicTile(this);
    }

    TileType() {
        this.SPRITE = new Sprite(Assets.getInstance().getTexture("tile/" + this.name().toLowerCase() + ".png"));
        this.SOLID = false;
        this.TILE = new BasicTile(this);
    }

    TileType(boolean solid) {
        this.SPRITE = new Sprite(Assets.getInstance().getTexture("tile/" + this.name().toLowerCase() + ".png"));
        this.SOLID = solid;
        this.TILE = new BasicTile(this);
    }

    public final Sprite SPRITE;
    public final boolean SOLID;
    public final Tile TILE;

}