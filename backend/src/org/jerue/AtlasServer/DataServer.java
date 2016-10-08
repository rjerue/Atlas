package org.jerue.AtlasServer;


import java.net.Socket;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;

public class DataServer extends Thread
{ 
	protected Socket clientSocket;
	CoordinateDatabase coords = new CoordinateDatabase();

	public static void main(String[] args) throws IOException 
	{ 
		ServerSocket serverSocket = null; 
		try { 
			serverSocket = new ServerSocket(10008); 
			System.out.println ("Connection Socket Created");
			try { 
				while (true)
				{
					System.out.println ("Waiting for Connection");
					new DataServer (serverSocket.accept()); 
				}
			} 
			catch (IOException e) 
			{ 
				System.err.println("Accept failed."); 
				System.exit(1); 
			} 
		} 
		catch (IOException e) 
		{ 
			System.err.println("Could not listen on port: 10008."); 
			System.exit(1); 
		} 
		finally
		{
			try {
				serverSocket.close(); 
			}
			catch (IOException e)
			{ 
				System.err.println("Could not close port: 10008."); 
				System.exit(1); 
			} 
		}
	}

	private DataServer (Socket clientSoc)
	{
		clientSocket = clientSoc;
		start();
	}

	public void run()
	{
		System.out.println ("New Communication Thread Started");

		try { 
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), 
					true); 
			BufferedReader in = new BufferedReader( 
					new InputStreamReader( clientSocket.getInputStream())); 

			String inputLine; 

			while ((inputLine = in.readLine()) != null) //reads
			{
				if(inputLine.equals("request")){
					System.out.println("Server: Client is only requesting");
					out.println(coords.toString()); //empty message is "empty"
				}
				else if (inputLine.equals("Bye.")) 
					break; 
				else{
					System.out.println ("Client: " + inputLine); 
					coords.add(new Coordinate(inputLine));
					String send = coords.toString();
					System.out.println("Server: " + send);
					out.println(send); //Sends stuff back 
				}
			} 

			out.close(); 
			in.close(); 
			clientSocket.close(); 
		} 
		catch (IOException e) 
		{ 
			System.err.println("Problem with Communication Server");
			System.exit(1); 
		} 
	}
} 