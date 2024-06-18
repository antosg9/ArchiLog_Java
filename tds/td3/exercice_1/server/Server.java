package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) throws IOException{
		int numPort = 1234;
		ServerSocket serverSocket = new ServerSocket(numPort);
		
		while(true)
		{
			Socket clientSocket = serverSocket.accept(); //Attraper l'adresse du client
            		System.out.println("New client connected : " + clientSocket.getInetAddress()); //Informer
			new clientHandler(clientSocket); //Rediriger le client dans un thread
		}
        
        	//Fermeture du serveur (que l'on utilise pas dans ce cas car le serveur tourne 24/24
		//serverSocket.close();
		//System.out.println("Server closed");
	}

}
