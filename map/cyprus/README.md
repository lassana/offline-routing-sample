Create your offline map:

1. Get a [Graphhopper](https://github.com/graphhopper/graphhopper/). I used on SHA1 ID: `1d8a64ea8d3a50a030ace148e7d0c97897bbcf33`.

1. Get a raw OpenStreetMap data from [Geofabrik server](http://download.geofabrik.de/openstreetmap/) and convert it to a routing data:

        ./graphhopper.sh import <downloaded-osm-file.pbf>
The Graphhoper will create `edges`, `geometry`, `locationIndex`, `names`, `nodes`, `properties` files.

1. Get [a map for Mapsforge](http://download.mapsforge.org/maps/).

Note that you should use the same versions of Graphhopper in your Android application and on desktop.
