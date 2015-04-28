package com.github.lassana.offlineroutingsample.ui.map;

import org.mapsforge.map.rendertheme.XmlRenderTheme;

import java.io.InputStream;

/**
 * @author Nikolai Doronin {@literal <lassana.nd@gmail.com>}
 * @since 4/28/2015.
 */
public class RenderTheme  implements XmlRenderTheme {
    //static private final String path = "/org/mapsforge/android/maps/rendertheme/osmarender/osmarender.xml";
    static private final String path = "/osmarender/osmarender.xml";

    @Override
    public String getRelativePathPrefix() {
        return path;
    }

    @Override
    public InputStream getRenderThemeAsStream() {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(path);
        if ( resourceAsStream == null ) {
            resourceAsStream = getClass().getResourceAsStream(path);
        }

        return resourceAsStream;
    }
}