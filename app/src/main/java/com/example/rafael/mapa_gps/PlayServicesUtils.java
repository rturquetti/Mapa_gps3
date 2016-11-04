package com.example.rafael.mapa_gps;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class PlayServicesUtils {
    //limite de tempo de espera
    public final static int REQUEST_CODE_ERRO_PLAY_SERVICES = 9000;

    //método para verificação se o serviço está disponível
    public static boolean googlePlayServicesDisponivel(FragmentActivity activity) {
        //checagem do serviço e atribuindo à variável
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);

        //validando se o serviço está diponívle
        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        } else {
            //avisando que o serviço não está disponível
            exibirMensagemDeErro(activity, resultCode);
            return false;
        }
    }


    //contruindo uma mensagem de exibição
    public static void exibirMensagemDeErro(FragmentActivity activity, int codigoDoErro){
        //verificando se o serviço não responder no tempo limite
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(codigoDoErro, activity, REQUEST_CODE_ERRO_PLAY_SERVICES);

        //caso o erro for positivo irá gerar a mensagem
        if (errorDialog != null) {
            MessageDialogFragment errorFragment = new MessageDialogFragment();
            errorFragment.setDialog(errorDialog);
            errorFragment.show(activity.getSupportFragmentManager(),"DIALOG_ERRO_PLAY_SERVICES");
        }
    }

}