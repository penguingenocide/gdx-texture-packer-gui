package com.crashinvaders.texturepackergui.views.canvas;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/** This is simple TextureAtlas wrapper that holds some extra info for service use */
public class AtlasModel implements Disposable {
    private final FileHandle atlasFile;
    private final TextureAtlas atlas;
    private final Array<Page> pages = new Array<>(true, 8);
    private final String atlasPath;
    private final TextureAtlas.TextureAtlasData atlasData;

    public AtlasModel(FileHandle atlasFile) {
        this.atlasFile = atlasFile;
        this.atlasData = new TextureAtlas.TextureAtlasData(atlasFile, atlasFile.parent(), false);
        this.atlas = new TextureAtlas(atlasData);
        atlasPath = atlasFile.file().getAbsolutePath();

        generatePages(atlas);
    }

    private void generatePages(TextureAtlas atlas) {
        Array<Texture> textures = new Array<>();

        // We could use simple atlas.getTextures(), but it returns them in random order...
        for (TextureRegion region : atlas.getRegions()) {
            if (!textures.contains(region.getTexture(), true)) {
                textures.add(region.getTexture());
            }
        }
        pages.clear();
        for (int i = 0; i < textures.size; i++) {
            Page page = new Page(this, textures.get(i), i);
            pages.add(page);
        }
    }

    @Override
    public void dispose() {
        atlas.dispose();
    }

    public FileHandle getAtlasFile() {
        return atlasFile;
    }

    public String getAtlasPath() {
        return atlasPath;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public TextureAtlas.TextureAtlasData getAtlasData() {
        return atlasData;
    }

    public Array<Page> getPages() {
        return pages;
    }

    public static class Page {
        private final AtlasModel atlasModel;
        private final Texture texture;
        private final int pageIndex;
        private final Array<AtlasRegion> regions = new Array<>(true, 32);

        private Page(AtlasModel atlasModel, Texture pageTexture, int pageIndex) {
            this.atlasModel = atlasModel;
            this.texture = pageTexture;
            this.pageIndex = pageIndex;

            for (AtlasRegion atlasRegion : atlasModel.atlas.getRegions()) {
                if (atlasRegion.getTexture().equals(texture)) {
                    regions.add(atlasRegion);
                }
            }
        }

        public AtlasModel getAtlasModel() {
            return atlasModel;
        }

        public Texture getTexture() {
            return texture;
        }

        public int getPageIndex() {
            return pageIndex;
        }

        public Array<AtlasRegion> getRegions() {
            return regions;
        }
    }
}
