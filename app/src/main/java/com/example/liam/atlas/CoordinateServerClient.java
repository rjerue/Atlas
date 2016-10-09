package com.example.liam.atlas;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by Sahil on 10/8/2016.
 */
public class CoordinateServerClient {
    public static final String serverHostname = new String ("52.43.17.188");//TODO ADD REAL SERVER ADRESS
    public static final int serverPort = 10008;
    public static final String SendCoordinateMessage = "request";


    public static boolean SendMessage(Coordinate coordinate) throws IOException, ExecutionException, InterruptedException {
        return new MessageSender().execute(coordinate).get();
    }

    public static ArrayList<Coordinate> ReceiveData() throws IOException, ExecutionException, InterruptedException {
        return new MessageReceiver().execute().get();
    }
}

class MessageReceiver extends AsyncTask<Void,Void,ArrayList<Coordinate>> {
    @Override
    protected ArrayList<Coordinate> doInBackground(Void... params) {

        ArrayList<Coordinate> Coordinates = new ArrayList<Coordinate>();
        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        String serverMessage;

        try {
            echoSocket = new Socket();
            echoSocket.connect(new InetSocketAddress(CoordinateServerClient.serverHostname, CoordinateServerClient.serverPort), 5000);

            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    echoSocket.getInputStream()));
            out.println(CoordinateServerClient.SendCoordinateMessage);
            serverMessage=in.readLine();
            out.close();
            in.close();
            echoSocket.close();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + CoordinateServerClient.serverHostname);
            return null;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to: " + CoordinateServerClient.serverHostname);
            return null;
        }


        if (serverMessage.equals("empty")) {
            System.out.println("This is empty yo");
            return Coordinates;
        }

        for (String coordinateString :
                serverMessage.split("~")) {
            String[] coordinateComponents = coordinateString.split(",");
            Coordinate coordinate = new Coordinate(Double.parseDouble(coordinateComponents[0]),Double.parseDouble(coordinateComponents[1]),Utilities.StringToBitMap(coordinateComponents[2].replace("\\n","\n")));
            Coordinates.add(coordinate);

        }
        return Coordinates;
    }
}

class MessageSender extends AsyncTask<Coordinate,Void,Boolean>{
    @Override
    protected Boolean doInBackground(Coordinate... params) {
        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        System.out.println(1);
        try {
            System.out.println("Tebow");
            echoSocket = new Socket();
            echoSocket.connect(new InetSocketAddress(CoordinateServerClient.serverHostname, CoordinateServerClient.serverPort), 5000);
            System.out.println(2);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            System.out.println(3);
            in = new BufferedReader(new InputStreamReader(
                    echoSocket.getInputStream()));
            System.out.println(4);
            out.println(params[0].getX() + "," + params[0].getY() + "," + Utilities.BitMapToString(params[0].getImage()).replace("\n","\\n"));
            System.out.println(Utilities.BitMapToString(params[0].getImage()));
            String serverMessage=in.readLine();
            System.out.println(serverMessage);
            out.close();
            in.close();
            echoSocket.close();
            return true;
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + CoordinateServerClient.serverHostname);
            return false;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to: " + CoordinateServerClient.serverHostname);
            return false;
        }
    }
}
