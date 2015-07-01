package com.github.lassana.offlineroutingsample.map.marker;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

/**
 * @author Nikolai Doronin {@literal <lassana.nd@gmail.com>}
 * @since 5/19/2015.
 */
public class CustomMarkerModel implements Parcelable {

    private final String title;
    private final int drawableRes;
    private final Location location;

    public CustomMarkerModel(@NonNull String title,
                             @DrawableRes int drawableRes,
                             double lati,
                             double longi) {
        this.title = title;
        this.drawableRes = drawableRes;
        this.location = new Location("");
        location.setLatitude(lati);
        location.setLongitude(longi);
    }

    public CustomMarkerModel(@NonNull String title,
                             @DrawableRes int drawableRes,
                             @NonNull Location location) {
        this.title = title;
        this.drawableRes = drawableRes;
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(drawableRes);
        dest.writeParcelable(location, 0);
    }

    public static final Parcelable.Creator<CustomMarkerModel> CREATOR = new Parcelable.Creator<CustomMarkerModel>() {
        public CustomMarkerModel createFromParcel(Parcel in) {
            return new CustomMarkerModel(in);
        }

        public CustomMarkerModel[] newArray(int size) {
            return new CustomMarkerModel[size];
        }
    };

    private CustomMarkerModel(Parcel in) {
        title = in.readString();
        drawableRes = in.readInt();
        location = in.readParcelable(null);
    }

    public String getTitle() {
        return title;
    }

    public int getDrawableRes() {
        return drawableRes;
    }

    public Location getLocation() {
        return location;
    }

    public Drawable getDrawable(@NonNull Resources resources) {
        return resources.getDrawable(drawableRes);
    }
}
