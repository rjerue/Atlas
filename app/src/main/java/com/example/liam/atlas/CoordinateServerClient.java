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
    private static final String SendCoordinateMessage = "request";


    public static boolean SendMessage(Coordinate coordinate) throws IOException {

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            echoSocket = new Socket(serverHostname, 10008);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverHostname);
            return false;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to: " + serverHostname);
            return false;
        }

        out.println(SendCoordinateMessage);
        String serverMessage=in.readLine();
        System.out.println(serverMessage);
        out.close();
        in.close();
        echoSocket.close();
        return true;
    }

    public static ArrayList<Coordinate> RecieveData() throws IOException {


        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            echoSocket = new Socket(serverHostname, 10008);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
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

        out.println(SendCoordinateMessage);
        String serverMessage=in.readLine();
        out.close();
        in.close();
        echoSocket.close();

        if (serverMessage.equals("empty"))
            return null;

        ArrayList<Coordinate> Coordinates = new ArrayList<Coordinate>();
        for (String coordinateString :
                serverMessage.split("~")) {
            String[] coordinateComponents = coordinateString.split(",");
            Coordinate coordinate = new Coordinate(Double.parseDouble(coordinateComponents[0]),Double.parseDouble(coordinateComponents[1]),Utilities.StringToBitMap(coordinateComponents[2]));
            Coordinates.add(coordinate);

        }

        return Coordinates;
    }
}
