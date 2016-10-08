package com.example.liam.atlas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Sahil on 10/8/2016.
 */
public class CoordinateServerClient {
    private static final String serverHostname = new String ("127.0.0.1");


    public static boolean SendMessage(Coordinate coordinate) {

        return false;
    }

    public static ArrayList<Coordinate> RecieveData() throws IOException {

        System.out.println ("Attemping to connect to host " +
                serverHostname + " on port 10008.");

        Socket echoSocket = null;
        BufferedReader in = null;

        try {
            echoSocket = new Socket(serverHostname, 10008);
            in = new BufferedReader(new InputStreamReader(
                    echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverHostname);
            return null;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to: " + serverHostname);
            return null;
        }

        String serverMessage = in.readLine();
        in.close();
        echoSocket.close();

        if (serverMessage.equals(null))
            return null;

        ArrayList<Coordinate> Coordinates = new ArrayList<Coordinate>();
        for (String coordinateString :
             serverMessage.split("~")) {
            String[] coordinateComponents = coordinateString.split(",");
            Coordinate coordinate = new Coordinate(Double.parseDouble(coordinateComponents[0]),Double.parseDouble(coordinateComponents[1]),null);

            Coordinates.add(coordinate);

        }

        return Coordinates;
    }
}
