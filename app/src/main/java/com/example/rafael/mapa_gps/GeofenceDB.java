package com.example.rafael.mapa_gps;

/**
 * Created by rafael on 27/10/2016.
 */
import android.content.Context;
import android.content.SharedPreferences;

public class GeofenceDB {
    public static final String KEY_LATITUDE = "KEY_LATITUDE";
    public static final String KEY_LOGITUDE = "KEY_LONGETUDE";
    public static final String KEY_RADIUS = "KEY_RADIUS";
    public static final String KEY_EXPIRATION = "kEY_EXPIRATION";
    public static final String KEY_TRANSITION_TYPE = "kEY_TRANSITION_TYPE";
    public static final String KEY_PREFIX = "KEY";

    public static final long INVALID_LONG_VALUE = -999l;
    public static final float INVALID_FLOAT_VALUE = -999.0f;
    public static final int INVALID_INT_VALUE = -999;

    private final SharedPreferences mPrefs;
    private static final String SHARED_PREFERENCES = "SharedPreferences";

    public GeofenceDB(Context context) {
        mPrefs = context.getSharedPreferences(SHARED_PREFERENCES,Context.MODE_PRIVATE);
    }

    //adicionando um geofence
    public GeofenceInfo getGeofence(String id){
        double lat = mPrefs.getFloat(getGeofenceFieldKey(id,KEY_LATITUDE),INVALID_FLOAT_VALUE);
        double lng = mPrefs.getFloat(getGeofenceFieldKey(id,KEY_LOGITUDE),INVALID_FLOAT_VALUE);
        float radius = mPrefs.getFloat(getGeofenceFieldKey(id,KEY_RADIUS),INVALID_FLOAT_VALUE);
        long expirationDuration = mPrefs.getLong(getGeofenceFieldKey(id,KEY_EXPIRATION),INVALID_LONG_VALUE);
        int transitionType = mPrefs.getInt(getGeofenceFieldKey(id,KEY_TRANSITION_TYPE),INVALID_INT_VALUE);

        if (lat != INVALID_FLOAT_VALUE && lng != INVALID_FLOAT_VALUE && radius != INVALID_FLOAT_VALUE && expirationDuration != INVALID_LONG_VALUE && transitionType!= INVALID_INT_VALUE){
            return new GeofenceInfo(id,lat,lng,radius,expirationDuration,transitionType);
        }
        else{
            return null;
        }
    }

    //salvando um geofence
    public void salvarGeofence(String id, GeofenceInfo geofence) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putFloat(getGeofenceFieldKey(id,KEY_LATITUDE),(float) geofence.mLatitude);
        editor.putFloat(getGeofenceFieldKey(id,KEY_LOGITUDE),(float) geofence.mLongetude);
        editor.putFloat(getGeofenceFieldKey(id,KEY_RADIUS), geofence.mRadius);
        editor.putLong(getGeofenceFieldKey(id,KEY_EXPIRATION), geofence.mExpirationDuration);
        editor.putInt(getGeofenceFieldKey(id,KEY_TRANSITION_TYPE), geofence.mTransitionType);
        editor.commit();
    }

    private String getGeofenceFieldKey(String id,String fieldName){
        return KEY_PREFIX + "_"+ id + "_" +fieldName;
    }
}





