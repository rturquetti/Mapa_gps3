package com.example.rafael.mapa_gps;

import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rafael on 13/10/2016.
 */

public class Cliente implements Parcelable {
    public long id;
    public String nome;
    public String email;
    public String telefone;
    public int podeLocalizar;
    public int vertical;
    public int horizontal;
    public int podeConversar;
    public String idioma1;
    public String idioma2;
    public String idioma3;

    public Cliente(){

    }


    public Cliente (long id,String nome,String email,String telefone,int podeLocalizar, int vertical,int horizontal,int podeConversar, String idioma1, String idioma2, String idioma3){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.podeLocalizar = podeLocalizar;
        this.vertical = vertical;
        this.horizontal = horizontal;
        this.podeConversar = podeConversar;
        this.idioma1 = idioma1;
        this.idioma2 = idioma2;
        this.idioma3 = idioma3;
    }

    public Cliente(Parcel from){
        id = from.readLong();
        nome = from.readString();
        email = from.readString();
        telefone = from.readString();
        podeLocalizar = from.readInt();
        vertical = from.readInt();
        horizontal = from.readInt();
        podeConversar = from.readInt();
        idioma1 = from.readString();
        idioma2 = from.readString();
        idioma3 = from.readString();
    }

    public static final Parcelable.Creator<Cliente> CREATOR = new Parcelable.Creator<Cliente>(){
        public Cliente createFromParcel(Parcel in){
            return new Cliente(in);
        }
        public Cliente[] newArray(int size){
            return new Cliente[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(nome);
        dest.writeString(email);
        dest.writeString(telefone);
        dest.writeInt(podeLocalizar);
        dest.writeInt(vertical);
        dest.writeInt(horizontal);
        dest.writeInt(podeConversar);
        dest.writeString(idioma1);
        dest.writeString(idioma2);
        dest.writeString(idioma3);
    }
}
