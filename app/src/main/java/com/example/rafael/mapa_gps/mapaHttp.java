package com.example.rafael.mapa_gps;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by rafael on 12/10/2016.
 */

public class mapaHttp {
    private static String BASE_URL = "http://www.codifique.net/wsCM/service.php";
    private Context mContext;


    protected void inserir(Cliente cliente) {
        try {
            enviarCliente("POST", cliente);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Cliente> atualizar(Cliente cliente) {
        List<Cliente> listaCli= new ArrayList<Cliente>();
            Log.d("Achei "+cliente.nome,"Cliente");

        try {

           /* cliente.nome = "rafael";
            cliente.email = "rafael@jose.com";
            cliente.telefone = "988110811";
            cliente.podeLocalizar = 1;
            cliente.vertical = 22;
            cliente.horizontal = 654;
            cliente.podeConversar = 0;
            cliente.idioma1 = "turco";
            cliente.idioma2 = "ingles";
            cliente.idioma3 = "frances";*/


            enviarCliente("POST", cliente);
            listaCli.add(cliente);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaCli;
    }

    protected void excluir(Cliente cliente) {
        boolean podeExcluir = true;
        try {
            podeExcluir = enviarCliente("DELETE", cliente);
        } catch (Exception e) {
            podeExcluir = false;
            e.printStackTrace();
        }
    }

    public static boolean enviarCliente(String metodoHttp, Cliente cliente) throws Exception {
        boolean sucesso = false;
        /*
        boolean doOutput = !"DELETE".equals(metodoHttp);
        String url = BASE_URL;
        if (!doOutput) {
            url += "/"+ cliente.id;
        }
        HttpURLConnection conexao = abrirConexao(url, metodoHttp, doOutput);
        Log.d("Achei 2 : "+ cliente.nome+" / "+metodoHttp+" / "+doOutput,"Cliente");
*/
        try{
            URL url = new URL(BASE_URL);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod(metodoHttp);
            conexao.setDoOutput(true);
            OutputStream os = conexao.getOutputStream();
            os.write(("metodoHttp=POST?nome=Julia&email=julia@julia.com&telefone=988888888&podeLocalizar=0&vertical=444&horizontal=333&podeConversar=0&idioma1=italiano&idioma2=Portugues&idioma3=frances\"").getBytes());
            os.flush();
            os.close();
            conexao.connect();
            int responseCode = conexao.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                //setEnviadoProServidor(true);
            } else {
                throw  new RuntimeException("Erro ao registrar o dispositivo no servidor");
            }

        }catch (IOException e)
        {
            e.printStackTrace();
        }




        /*


        if (doOutput) {
            OutputStream os = conexao.getOutputStream();
            try {
                os.write(clienteToJsonBytes(cliente,metodoHttp));
                Log.d("Grav "+os.toString(),"x");
            }
            catch (IOException ex){
                ex.printStackTrace();
            }

            os.flush();
            os.close();
        }
        int responseCode = conexao.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream is = conexao.getInputStream();
            String s = streamToString(is);
            is.close();
                Log.d("Ret"+is,"-+");

            JSONObject json = new JSONObject(s);
            int id = json.getInt("id");
            cliente.id = id;
            sucesso = true;

        } else {
            throw new RuntimeException("Erro ao realizar operação");
        }
        conexao.disconnect();

        */
        return sucesso;
    }

    public static HttpURLConnection abrirConexao(String url,
                                                  String metodo, boolean doOutput) throws Exception{
        URL urlCon = new URL(url);
        HttpURLConnection conexao = (HttpURLConnection) urlCon.openConnection();
        conexao.setReadTimeout(15000);
        conexao.setConnectTimeout(15000);
        conexao.setRequestMethod(metodo);
        conexao.setDoInput(true);
        conexao.setDoOutput(doOutput);
        if (doOutput) {
            conexao.addRequestProperty("Content-Type", "application/json");
        }
        conexao.connect();
        return conexao;
    }

    public static List<Cliente> getCliente() throws Exception {
        HttpURLConnection conexao = abrirConexao(BASE_URL, "GET", false);

        List<Cliente> list = new ArrayList<Cliente>();
        if (conexao.getResponseCode() == HttpURLConnection.HTTP_OK) {
            String jsonString = streamToString(conexao.getInputStream());
            JSONArray json = new JSONArray(jsonString);

            for (int i = 0; i < json.length(); i++) {
                JSONObject clienteJSON = json.getJSONObject(i);

                Cliente cli = new Cliente(
                        clienteJSON.getLong("id"),
                        clienteJSON.getString("nome"),
                        clienteJSON.getString("email"),
                        clienteJSON.getString("telefone"),
                        clienteJSON.getInt("podeLocalizar"),
                        clienteJSON.getInt("vertical"),
                        clienteJSON.getInt("horizontal"),
                        clienteJSON.getInt("podeConversar"),
                        clienteJSON.getString("idioma1"),
                        clienteJSON.getString("idioma2"),
                        clienteJSON.getString("idioma3"));
                list.add(cli);
            }
        }
        return list;
    }

    public static String streamToString(InputStream is) throws IOException {
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int lidos;
        while ((lidos = is.read(bytes)) > 0) {
            baos.write(bytes, 0, lidos);
        }
        return new String(baos.toByteArray());
    }

    public static byte[] clienteToJsonBytes(Cliente cliente, String metodoHttp) {

        Log.d("Achei 3 : "+ cliente.nome,"Cliente");
        try {
            JSONObject jsonCliente = new JSONObject();
            jsonCliente.put("metodoHttp",metodoHttp);
            //jsonCliente.put("id", cliente.id);
            jsonCliente.put("nome", cliente.nome);
            jsonCliente.put("email", cliente.email);
            jsonCliente.put("telefone",cliente.telefone);
            jsonCliente.put("podeLocalizar",cliente.podeLocalizar);
            jsonCliente.put("vertical",cliente.vertical);
            jsonCliente.put("horizontal",cliente.horizontal);
            jsonCliente.put("podeConversar",cliente.podeConversar);
            jsonCliente.put("idioma1",cliente.idioma1);
            jsonCliente.put("idioma2",cliente.idioma2);
            jsonCliente.put("idioma3",cliente.idioma3);

            String json = jsonCliente.toString();

            Log.d("Achei 4 : "+ jsonCliente.toString(),"-");

            return json.getBytes();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
