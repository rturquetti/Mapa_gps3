package com.example.rafael.mapa_gps;

/**
 * Created by rafael on 27/10/2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import java.util.List;

public class GeofenceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //verificando a ocorrencia de erros
        if (LocationClient.hasError(intent)) {
            //identificando o erro ocorrido
            int errorCode = LocationClient.getErrorCode(intent);
            Toast.makeText(context, "Erro no serviço de localização: " + errorCode,
                    Toast.LENGTH_LONG).show();

        } else {
            //caso não haja erro
            //identificando o tipo de transação
            int transicao = LocationClient.getGeofenceTransition(intent);
            if (transicao == Geofence.GEOFENCE_TRANSITION_ENTER
                    || transicao == Geofence.GEOFENCE_TRANSITION_EXIT) {
                //veificando quais geofences foram disparados
                List<Geofence> geofences = LocationClient.getTriggeringGeofences(intent);

                Toast.makeText(context,
                        "Geofence! " + transicao +" - "+ geofences.get(0).getRequestId(),
                        Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(context,
                        "Erro no Geofence: " + transicao, Toast.LENGTH_LONG).show();
            }
        }
    }
}