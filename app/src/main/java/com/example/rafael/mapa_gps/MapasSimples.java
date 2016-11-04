package com.example.rafael.mapa_gps;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapasSimples extends ActionBarActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMapLongClickListener,
        LocationClient.OnAddGeofencesResultListener{

    GoogleMap mGoogleMap;
    LocationClient mLocationCliente;
    LatLng mOrigem;
    LatLng mDestino;
    Marker mMarkerLocalAtual;
    TextView mTextView;
    MapaTask mTask;
    Cliente cliente = new Cliente();
    GeofenceInfo mGeofenceInfo;
    GeofenceDB mGeofenceDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas_simples);

        if (conectado()){
            //instanciando o fragmento do xml
            SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            //obtendo a instancia do GoogleMap
            mGoogleMap = fragment.getMap();
            //mudando a forma como o mapa é exibido HYBRID/TERRAIN/NORMAL/NONE
            //mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            //Instanciando a classe passando context,ConnectionCallbacks,OnConnectionFailedListner
            mLocationCliente = new LocationClient(this, this, this);
            mGoogleMap.setOnMapLongClickListener(this);
            mGeofenceDB = new GeofenceDB(this);
        }
        else{
            Toast.makeText(getApplicationContext(), "Sem internet", Toast.LENGTH_LONG).show();
        }
    }

    private  void iniciarDeteccaoDeLocal(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5 * 1000);
        locationRequest.getFastestInterval();
        mLocationCliente.requestLocationUpdates(locationRequest, this);

    }

        //tentando objeter conexão
    protected void onStart(){
        super.onStart();
        mLocationCliente.connect();
    }
    //desconectando
    protected void onStop(){
        mLocationCliente.disconnect();
        super.onStop();
    }

    //voltando e veirificando se o problema foi solucionado
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        //se o problema foi resolvido tenta conectar novamente
        if (requestCode == PlayServicesUtils.REQUEST_CODE_ERRO_PLAY_SERVICES && requestCode == RESULT_OK){
            mLocationCliente.connect();

        }
    }

    //caso a conexão form bem sucedida
    public void onConnected(Bundle dataBundle){
        Log.d("Informação","onConnected");
        //obtendo a última localização do celular retornando um objeto da classe Location
        Location location = mLocationCliente.getLastLocation();
        Log.d("Location "+location,"    >>>>>>>>>>>>>>>>>>>>>>");
        if(location != null){
            //atribuindo a última localização ao objeto
            mOrigem = new LatLng(location.getLatitude(),location.getLongitude());

            double vertical = mOrigem.latitude;
            double horizontal = mOrigem.longitude;
            Log.d("latitude "+vertical, "longetude "+ horizontal);
            //adicionando o marcador no mapa
            atualizarMapa();

            cliente.nome = "f";
            cliente.email = "j";
            cliente.telefone = "1";
            cliente.podeLocalizar = 1;
            cliente.vertical = 2;
            cliente.horizontal = 6;
            cliente.podeConversar = 0;
            cliente.idioma1 = "t";
            cliente.idioma2 = "i";
            cliente.idioma3 = "f";

            mandandoInformacoes();
        }
    }

    public void onDisconnected(){
        Log.d("Informação","OnDisconnected");
    }

    //verificar se há uma solução para a falta de conexão
    public void onConnectionFailed(ConnectionResult connectionResult){
        if (connectionResult.hasResolution()) {
            try {
                //solucionando o problema de conexão, normalmente invocará o Google Play para resolver
                connectionResult.startResolutionForResult(this, PlayServicesUtils.REQUEST_CODE_ERRO_PLAY_SERVICES);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
        else{
            //caso não haja solução para a falta de conexão
            PlayServicesUtils.exibirMensagemDeErro(this,connectionResult.getErrorCode());
        }
    }

    private void atualizarMapa(){
        //posicionando o mapa e configurando o zoom(vaira 2 a 21)
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mOrigem, 17.0f));
        //incluindo marcação no mapa
        mGoogleMap.addMarker(new MarkerOptions()
                .position(mOrigem) // localização
                .title("Local Atual")); // mostrando informações

        mMarkerLocalAtual = mGoogleMap.addMarker(new MarkerOptions()
                            .position(mDestino)
                            .title("Destino")
                            .position(mOrigem));
        iniciarDeteccaoDeLocal();

        mGeofenceInfo = mGeofenceDB.getGeofence("1");
        if (mGeofenceInfo != null){
            LatLng posicao = new LatLng(mGeofenceInfo.mLatitude,mGeofenceInfo.mLongetude);
            mGoogleMap.addCircle(new CircleOptions()
                .strokeWidth(2)
                .fillColor(0x990000FF)
                .center(posicao)
                .radius(mGeofenceInfo.mRadius)
            );
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mMarkerLocalAtual.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));
    }


    //identificando se há algum tipo de conexção com a internet retorna true caso haja
    public boolean conectado(){
        boolean conect = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnected()){
            conect = true;
        }
        else{
            conect = false;
        }
        return conect;
    }

    public void mandandoInformacoes(){
        if(mTask == null || mTask.getStatus() != AsyncTask.Status.RUNNING){
            mTask = new MapaTask();
            mTask.execute();
        }
    }

    //invocado quando a tela é precionada por uma longo tempo
    public void onMapLongClick(LatLng latLng){
        //se o mLocationCliente está conecetado
        if (mLocationCliente.isConnected()){
            //criando objeto (qudo o cliente entrar ou sair da área demarcada)
            PendingIntent pit = PendingIntent.getBroadcast(
                    this,
                    0,
                    new Intent(this,GeofenceReceiver.class),
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            //criando objeto
            mGeofenceInfo = new GeofenceInfo(
                    "1",//identificador
                    latLng.latitude,latLng.longitude,//coordenada
                    200,//metros
                    Geofence.NEVER_EXPIRE,//tempo de validade
                    Geofence.GEOFENCE_TRANSITION_ENTER | //transições
                            Geofence.GEOFENCE_TRANSITION_EXIT
            );
            List<Geofence> geofences = new ArrayList<Geofence>();
            //adicionando Geofence
            geofences.add(mGeofenceInfo.getGeofence());
            mLocationCliente.addGeofences(geofences,pit,this);
        }
    }

    //veificando se a geofence foi adicionada corretamente
    public void onAddGeofencesResult(int i, String[] strings){
        //caso obtenha sucesso
        if (LocationStatusCodes.SUCCESS == 1){
            //sanvando informação
            mGeofenceDB.salvarGeofence("1",mGeofenceInfo);
            mGeofenceInfo = null;
            atualizarMapa();
        }
    }

    class MapaTask extends AsyncTask<Void, Void, List<Cliente>> {
        @Override
        protected  void onPreExecute ()
        {

        }

        @Override
        protected List<Cliente> doInBackground(Void... params){

            return mapaHttp.atualizar(cliente);
        }

        @Override
        protected void onPostExecute(List<Cliente> alunos)
        {

        }
    }

}


