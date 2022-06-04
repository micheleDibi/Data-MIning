package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import utility.Keyboard;

public class Client {

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	Client (String address, int port) throws IOException, ClassNotFoundException{
			socket = new Socket(address, port);
			System.out.println(socket);		
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream()); // stream con richieste del client
			talking();
	}
	
	private void talking() throws IOException, ClassNotFoundException {
		
		int decision;
		String menu;
		
		do {	
			do{
				System.out.println("Caricare KNN da file [1]");
				System.out.println("Caricare KNN da file binario [2]");
				System.out.println("Caricare KNN da database [3]");
				decision = Keyboard.readInt();
			} while(decision < 0 || decision > 3);
			
			String risposta;
			do {
				out.writeObject(decision);

				if(decision == 1) {
					System.out.println("Inserire il path di un file contenente un training set valido:");
				} else if(decision == 2) {
					System.out.println("Inserire il path di un file binario");
				} else if(decision == 3) {
					System.out.println("Inserire il nome della tabella presente nel database");
				}

				out.writeObject(Keyboard.readString()); // lettura nome file - tabella ;
				risposta = (String)in.readObject();
			} while(risposta.contains("@ERROR"));
			
			System.out.println("KNN loaded on the server");
			// predict
			String c;
			do {
				out.writeObject(4);

				boolean flag = true; //reading example
				do {
					risposta = (String)(in.readObject());

					if(!risposta.contains("@ENDEXAMPLE")) {
						// sto leggendo l'esempio
						String msg=(String)(in.readObject());

						if(risposta.equals("@READSTRING")) { //leggo una stringa
							System.out.println(msg);
							out.writeObject(Keyboard.readString());
						} else { //leggo un numero
							double x;
							do {
								System.out.println(msg);								
								x=Keyboard.readDouble();
							} while(new Double(x).equals(Double.NaN));
							out.writeObject(x);
						}
					}
					else flag = false;
				} while(flag);
				
				//sto leggendo k
				risposta = (String)(in.readObject());
				int k;
				do {
					System.out.print(risposta);
					k=Keyboard.readInt();
				}while (k<1);
				out.writeObject(k);
				//aspetto la predizione 
				
				System.out.println("Prediction:" + in.readObject());
				System.out.println("Vuoi ripetere predizione? Y/N");
				c = Keyboard.readString();
				
			} while (c.equalsIgnoreCase("y"));

			System.out.println("Vuoi ripetere una nuova esecuzione con un nuovo oggetto KNN? (Y/N)");
			menu = Keyboard.readString();
		}
		while(menu.equalsIgnoreCase("y"));
		
	}

	public static void main(String[] args){

		String addressSupp = "localhost";
		String portSupp = "8080";

		if(args.length == 1) {
			addressSupp = args[0];
		} else if (args.length == 2) {
			addressSupp = args[0];
			portSupp = args[1];
		}

		InetAddress addr;
		try {
			addr = InetAddress.getByName(addressSupp);
		} catch (UnknownHostException e) {
			System.out.println("[UnknownHostException] : " + e.getMessage());
			return;
		}
		
		Client c;
		try {
			c = new Client(addressSupp, new Integer(portSupp));
			
		}  catch (IOException e) {
			System.out.println("[client_main_IOException] : " + e.getMessage());
			return;
		} catch (NumberFormatException e) {
			System.out.println("[client_main_NumberFormatException] : " + e.getMessage());
			return;
		} catch (ClassNotFoundException e) {
			System.out.println("[client_main_ClassNotFoundException] : " + e.getMessage());
			return;
		}
	}
}
