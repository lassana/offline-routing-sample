package com.github.lassana.offlineroutingsample.ui.map;

import android.content.Context;
import android.location.Location;
import com.github.lassana.offlineroutingsample.util.MapsConfig;
import org.mapsforge.map.layer.cache.TileCache;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.IMapTileProviderCallback;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

/**
 * @author Nikolai Doronin {@literal <lassana.nd@gmail.com>}
 * @since 4/28/2015.
 */
public class MapsforgeMapView extends MapView {

    public MapsforgeMapView(Context context, final TileCache tileCache, final String offlineMapFilePath) {
        super(context, MapsConfig.TILE_SIZE, new DefaultResourceProxyImpl(context), new MapsforgeTileProvider(context, tileCache, offlineMapFilePath), null);
    }

    public void setCenter(Location location) {
        setCenter(new GeoPoint(location.getLatitude(), location.getLongitude()));

    }

    public void setCenter(final GeoPoint geoPoint) {
        post(new Runnable() {
            @Override
            public void run() {
                IMapController controller = getController();
                controller.animateTo(geoPoint);
            }
        });
    }

    static private class MapsforgeTileProvider extends MapTileProviderArray implements IMapTileProviderCallback {
        private MapsforgeTileProvider(final Context context, final TileCache tileCache, final String offlineMapFilePath) {
            super(TileSourceFactory.getTileSource("MapquestOSM"), new SimpleRegisterReceiver(context));
            final MapsforgeOSMDroidTileProviderBase mapsforgeProvider = new MapsforgeOSMDroidTileProviderBase();
            final MapsforgeOSMTileSource tileSourceMf = new MapsforgeOSMTileSource(context, "mapsforge", offlineMapFilePath, tileCache);
            mapsforgeProvider.setTileSource(tileSourceMf);
            mTileProviderList.add(mapsforgeProvider);
        }
    }

}
