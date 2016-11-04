package com.example.rafael.mapa_gps;

/**
 * Created by rafael on 27/10/2016.
 */
import com.google.android.gms.location.Geofence;
public class GeofenceInfo {
    final String mId;
    final double mLatitude;
    final double mLongetude;
    final float mRadius;
    long mExpirationDuration;
    int mTransitionType;

    public GeofenceInfo(String geofenceId, double latitude, double longetude, float radius, long expiration, int transition){
        this.mId = geofenceId;
        this.mLatitude = latitude;
        this.mLongetude = longetude;
        this.mRadius = radius;
        this.mExpirationDuration = expiration;
        this.mTransitionType = transition;
    }

    //cria o objeto a patir do satributos do nosso objeto
    public Geofence getGeofence(){
        return new Geofence.Builder()
                //identificador unico
                .setRequestId(mId)
                //tipo de transição interessa (entrar ou sair)
                .setTransitionTypes(mTransitionType)
                //delimitando a área
                .setCircularRegion(mLatitude,mLongetude,mRadius)
                //duração em milisegundos - NEVER_EXPIRE:nunca expira
                .setExpirationDuration(mExpirationDuration)
                .build();
    }
}
