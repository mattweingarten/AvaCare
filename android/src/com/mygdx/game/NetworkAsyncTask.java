package com.mygdx.game;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


class NetworkAsyncTask extends AsyncTask<URL, Void, String> {

    Messagecallbackhandler parentClass;

    String data;
    String response;
    String url;

    public NetworkAsyncTask(String rest, Messagecallbackhandler msg, String url) {
        this.data = rest;
        this.parentClass = msg;
        this.url = url;
    }

    protected String integrateData(String rest) {
        StringBuilder sb = new StringBuilder();
        sb.append("?param=1");
        sb.append(rest);
        return sb.toString();
    }

    @Override
    protected String doInBackground(URL... urls) {
        String response = "";
        /*int limit = 0;

        while (response.equals("") && limit < 20) {
            response += sendAndReceive();
            limit++;
        }
       */
        URL url;
        HttpsURLConnection connection;
        StringBuilder result = new StringBuilder();
        try {
            url = new URL(this.url);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("POST");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(integrateData(this.data));
            writer.flush();

            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            reader.close();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.response = result.toString();
        return this.response;
    }
//
//    public String sendAndReceive(){
//        byte [] mess = jason.toString().getBytes();
//
//        byte [] response = new byte [1024];
//        String text = "";
//        DatagramSocket socket;
//        try{
//            InetAddress adr = InetAddress.getByName(new URL(this.url).getHost());
//            socket = new DatagramSocket(80);
//            DatagramPacket p = new DatagramPacket(mess,mess.length,adr,80);
//            socket.setSoTimeout(2000);
//            socket.send(p);
//            DatagramPacket p2 = new DatagramPacket(response,response.length);
//            socket.receive(p2);
//
//            String temp = new String(p2.getData());
//            text += temp.substring(0,p2.getLength());
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return text;
//    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        parentClass.handleMessageResponse(this.response);
    }
}


