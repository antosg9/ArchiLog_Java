package serveur;

import java.io.*;
import java.net.*;
import java.util.List;

import cours.Cours;
import cours.PasAssezDePlacesException;

// ce service envoie les questions au client
// on pourrait lors du premier envoi lister les cours o� il reste de la place
// mais il faudrait coder les \n en ##
// (et d�coder du cot� client)

public class ServiceCours implements Runnable {

// **** ressources partag�es : les cours *****************
	private static List<Cours> lesCours;

	public static void setLesCours(List<Cours> lesCours) {
		ServiceCours.lesCours = lesCours;
	}

	private static Cours getCours(int noCours) {
		for (Cours c : lesCours)
			if (c.getNumeroCours() == noCours)
				return c;
		return null;
	}
// ********************************************************
	
	private final Socket client;

	ServiceCours(Socket socket) {
		this.client = socket;
	}

	@Override
	public void run() {
		String reponse = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);

			out.println("Tapez le num�ro de cours ");
			int noCours = Integer.parseInt(in.readLine());
			out.println("Tapez le nombre de places souhait�es ");
			int nbrePlaces = Integer.parseInt(in.readLine());

			System.out.println("Requ�te de " + this.client.getInetAddress() + " Cours n�" + noCours + " Nbre de places "
					+ nbrePlaces);
			Cours cours = getCours(noCours);
			if (cours != null)
				// la ressource concurrente est le cours
				synchronized (cours) {
					try {
						cours.inscription(nbrePlaces);
						reponse = "Inscription(s) r�ussie(s)";
					} catch (PasAssezDePlacesException e) {
						reponse = e.toString();
					}
				}
			else
				reponse = "Aucun cours ne porte ce num�ro";
			System.out.println(reponse);
			out.println(reponse);
		} catch (IOException e) {
			// Fin du service d'inversion
		}

		try {
			client.close();
		} catch (IOException e2) {
		}
	}

	protected void finalize() throws Throwable {
		client.close();
	}
}
