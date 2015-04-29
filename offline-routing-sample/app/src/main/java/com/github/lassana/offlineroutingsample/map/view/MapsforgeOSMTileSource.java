package com.github.lassana.offlineroutingsample.map.view;

import java.io.File;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;
import com.github.lassana.offlineroutingsample.util.MapsConfig;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.DatabaseRenderer;
import org.mapsforge.map.layer.renderer.RendererJob;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.reader.MapDatabase;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.ExpirableBitmapDrawable;
import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.tilesource.BitmapTileSourceBase.LowMemoryException;
import org.osmdroid.tileprovider.tilesource.ITileSource;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * @author Nikolai Doronin {@literal <lassana.nd@gmail.com>}
 * @since 4/28/2015.
 */
public class MapsforgeOSMTileSource implements ITileSource {
    private static final float DEFAULT_TEXT_SCALE = 1;
    private static final boolean IS_TRANSPARENT = false;
    private static final String TAG = "MapsforgeOSMTileSource";

    private final Context context;

    private final String name;
    private final String mapFile;

    private final DatabaseRenderer mapGenerator;
    private final XmlRenderTheme xmlRenderTheme;
    private final DisplayModel displayModel;
    //private final TileCache tileCache;
    //private final WeakHashMap<Job, Drawable> tileWeakCache;

    private final BoundingBox mapBounds;

    private int zoomBounds;
    private int westTileBounds;
    private int eastTileBounds;
    private int southTileBounds;
    private int northTileBounds;

    public MapsforgeOSMTileSource(final Context context, final String name, final String mapFile, final TileCache cache) {
        this.context = context;
        this.name = name;
        this.mapFile = mapFile;
        //this.tileCache = cache;
        //this.tileWeakCache = new WeakHashMap<>();

        MapDatabase mapDatabase = new MapDatabase();
        mapGenerator = new DatabaseRenderer(mapDatabase, AndroidGraphicFactory.INSTANCE);
        xmlRenderTheme = new RenderTheme();
        displayModel = new Model().displayModel;

        mapDatabase.closeFile();
        mapDatabase.openFile(new File(mapFile));
        mapBounds = mapDatabase.getMapFileInfo().boundingBox;
        zoomBounds = -1;
    }

    @Override
    public String localizedName(ResourceProxy proxy) {
        return name();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int ordinal() {
        return name.hashCode();
    }

    @Override
    public int getTileSizePixels() {
        return MapsConfig.TILE_SIZE;
    }

    @Override
    public int getMaximumZoomLevel() {
        return mapGenerator.getZoomLevelMax();
    }

    @Override
    public int getMinimumZoomLevel() {
        return 6;
    }

    public synchronized Drawable getDrawable(int tileX, int tileY, int zoom) throws LowMemoryException {
        if (tileOutOfBounds(tileX, tileY, zoom)) return null;
        Tile tile = new Tile(tileX, tileY, (byte) zoom);

        try {
            RendererJob job = new RendererJob(tile, new File(mapFile), xmlRenderTheme, displayModel, DEFAULT_TEXT_SCALE, IS_TRANSPARENT);
            //Drawable rvalue = tileWeakCache.get(job);
            Drawable rvalue = null;

            if ( rvalue == null ) {
                try {
                    Bitmap tileBitmap = AndroidGraphicFactory.getBitmap(mapGenerator.executeJob(job));
                    rvalue = new ExpirableBitmapDrawable(tileBitmap);
                    //tileWeakCache.put(job, rvalue);
                } catch (OutOfMemoryError e) {
                    Log.e(TAG, "OutOfMemory error during tile creating", e);
                    System.gc();
                }
            }
            return rvalue;
        } catch (Exception e) {
            Log.e(TAG, "Cannot create tile! tileX: " + tileX + "; tileY: " + tileY + "; zoom: " + zoom, e);
            //tileBitmap = Bitmap.createBitmap(MapsConfig.TILE_SIZE, MapsConfig.TILE_SIZE, Bitmap.Config.RGB_565);
            //tileBitmap.eraseColor(Color.YELLOW);
            return null;
        }
        //return new BitmapDrawable(context.getResources(), tileBitmap);
        //return new ExpirableBitmapDrawable(tileBitmap);
    }

    private boolean tileOutOfBounds(int tileX, int tileY, int zoom) {
        if (zoom != zoomBounds) recalculateTileBounds(zoom);

        return (tileX < westTileBounds) || (tileX > eastTileBounds) ||
                (tileY < northTileBounds) || (tileY > southTileBounds);
    }

    /* convert lon/lat to tile x,y from http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames */
    private void recalculateTileBounds(final int zoom) {
        zoomBounds = zoom;
        westTileBounds = lon2XTile(mapBounds.minLongitude, zoomBounds);
        eastTileBounds = lon2XTile(mapBounds.maxLongitude, zoomBounds);
        southTileBounds = lat2YTile(mapBounds.minLatitude, zoomBounds);
        northTileBounds = lat2YTile(mapBounds.maxLatitude, zoomBounds);
    }

    @Override
    public Drawable getDrawable(String arg0) throws LowMemoryException {
        return null;
    }

    @Override
    public Drawable getDrawable(InputStream arg0) throws LowMemoryException {
        return null;
    }

    @Override
    public String getTileRelativeFilenameString(final MapTile tile) {
        return null;
    }

    static private int lon2XTile(final double lon, final int zoom) {
        return (int) Math.floor((lon + 180) / 360 * (1 << zoom));
    }

    static private int lat2YTile(final double lat, final int zoom) {
        return (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1 << zoom));
    }
}