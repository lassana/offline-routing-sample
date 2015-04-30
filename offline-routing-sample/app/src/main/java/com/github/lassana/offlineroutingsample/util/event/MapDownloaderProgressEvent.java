package com.github.lassana.offlineroutingsample.util.event;

/**
 * @author Nikolai Doronin {@literal <lassana.nd@gmail.com>}
 * @since 4/30/2015.
 */
public class MapDownloaderProgressEvent {
    public final int progress;

    public MapDownloaderProgressEvent(int progress) {
        this.progress = progress;
    }
}
