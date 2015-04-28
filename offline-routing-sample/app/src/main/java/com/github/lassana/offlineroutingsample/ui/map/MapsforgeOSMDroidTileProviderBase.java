package com.github.lassana.offlineroutingsample.ui.map;

import android.graphics.drawable.Drawable;
import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.MapTileRequestState;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.ITileSource;

/**
 * Mapsforge backend for osmdroid library.
 * @see <a href="https://github.com/jezhiggins/android/tree/master/src/org/mapsforge/android/maps">jezhiggins/android</a>
 *
 * @author Nikolai Doronin {@literal <lassana.nd@gmail.com>}
 * @since 4/28/2015.
 */
public class MapsforgeOSMDroidTileProviderBase extends MapTileModuleProviderBase {
    private MapsforgeOSMTileSource tileSource_;
    //private final OnlineTileSourceBase fallbackTileSource_;
    //private final NetworkAvailabliltyCheck networkCheck_;

    public MapsforgeOSMDroidTileProviderBase(//final ITileSource fallbackSource,
                                             //final NetworkAvailabliltyCheck networkCheck
    ) {
        super(NUMBER_OF_TILE_DOWNLOAD_THREADS, TILE_DOWNLOAD_MAXIMUM_QUEUE_SIZE);
        tileSource_ = null;
        //fallbackTileSource_ = fallbackSource instanceof OnlineTileSourceBase ? (OnlineTileSourceBase) fallbackSource : null;
        //networkCheck_ = networkCheck;
    }

    @Override
    protected String getName() {
        return "Mapsforge";
    }

    @Override
    protected String getThreadGroupName() {
        return "mapsforge";
    }

    @Override
    public boolean getUsesDataConnection() {
        return false;
    }

    @Override
    public int getMaximumZoomLevel() {
        return (tileSource_ != null ? tileSource_.getMaximumZoomLevel() : MAXIMUM_ZOOMLEVEL);
    }

    @Override
    public int getMinimumZoomLevel() {
        return (tileSource_ != null ? tileSource_.getMinimumZoomLevel() : MINIMUM_ZOOMLEVEL);
    }

    @Override
    protected Runnable getTileLoader() {
        return new TileLoader();
    }

    @Override
    public void setTileSource(final ITileSource tileSource) {
        tileSource_ = (tileSource instanceof MapsforgeOSMTileSource) ?
                (MapsforgeOSMTileSource) tileSource :
                null;
    }

    private class TileLoader extends MapTileModuleProviderBase.TileLoader {
        @Override
        public Drawable loadTile(final MapTileRequestState aState) throws CantContinueException {
            Drawable tile = drawMapsforgeTile(aState);
            //if (tile == null) tile = downloadTile(aState);
            return tile;
        }

        private Drawable drawMapsforgeTile(final MapTileRequestState aState) {
            if (tileSource_ == null) return null;
            try {
                final MapTile tile = aState.getMapTile();
                return tileSource_.getDrawable(tile.getX(), tile.getY(), tile.getZoomLevel());
            } catch (Exception e) {
                return null;
            }
        }

/*
        private Drawable downloadTile(final MapTileRequestState aState) throws CantContinueException {
            if (fallbackTileSource_ == null)
                return null;

            final MapTile tile = aState.getMapTile();

            try {
                if (!isNetworkAvailable())
                    return null;

                final String tileUrl = fallbackTileSource_.getTileURLString(tile);

                final InputStream in = fetchTileFromUrl(tileUrl);
                if (in == null)
                    return null;

                final byte[] data = loadTileByteArray(in);

                final Drawable result = fallbackTileSource_.getDrawable(new ByteArrayInputStream(data));
                return result;
            } catch (final UnknownHostException e) {
                throw new CantContinueException(e);
            } catch (final BitmapTileSourceBase.LowMemoryException e) {
                throw new CantContinueException(e);
            } catch (final Exception e) {
                return null;
            }
        } // downloadTile

        private boolean isNetworkAvailable() {
            return (networkCheck_ == null || networkCheck_.getNetworkAvailable());
        } // networkAvailable

        private InputStream fetchTileFromUrl(final String tileURLString) throws ClientProtocolException, IOException {
            if (TextUtils.isEmpty(tileURLString))
                return null;

            final HttpClient client = new DefaultHttpClient();
            final HttpUriRequest head = new HttpGet(tileURLString);
            final HttpResponse response = client.execute(head);

            final org.apache.http.StatusLine line = response.getStatusLine();
            if (line.getStatusCode() != 200)
                return null;

            final HttpEntity entity = response.getEntity();
            return (entity != null) ? entity.getContent() : null;
        } // fetchTileFromUrl

        private byte[] loadTileByteArray(final InputStream in) throws IOException {
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            final OutputStream out = new BufferedOutputStream(dataStream, StreamUtils.IO_BUFFER_SIZE);
            try {
                StreamUtils.copy(in, out);
                out.flush();
                return dataStream.toByteArray();
            } finally {
                StreamUtils.closeStream(in);
                StreamUtils.closeStream(out);
            }
        }
*/

    }
}