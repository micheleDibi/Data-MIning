//Keyboard.java       Author: Lewis and Loftus
//
//Facilitates keyboard input by abstracting details about input
//parsing, conversions, and exception handling.
//********************************************************************

package utility;

import java.io.*;
import java.util.*;

/**
 * classe per l'input della porta di ascolto del server
 */

public class Keyboard {
	// ************* Error Handling Section **************************

	private static boolean printErrors = true;

	private static int errorCount = 0;

	/**
	 * restituisce il numero di errori
	 * @return intero che conta il numero di errori correnti
	 */
	public static int getErrorCount() {
		return errorCount;
	}

	/**
	 * resetta il numero corrente di errori a zero
	 * @param count contatore da resettare
	 */

	public static void resetErrorCount(int count) {
		errorCount = 0;
	}

	/**
	 * Restituisce un valore booleano che indica se gli errori di input sono attualmente stampati sullo standard output.
	 * @return vero se ci sono errori stampati
	 */
	public static boolean getPrintErrors() {
		return printErrors;
	}

	/**
	 * Imposta un valore booleano che indica se gli errori di input devono essere stampati sullo standard output.
	 * @param flag booleano da impostare vero se si vogliono gli errori stampati
	 */
	public static void setPrintErrors(boolean flag) {
		printErrors = flag;
	}

	/**
	 * incrementa il numero degli errori e stampa un messaggio di errore se opportuno
	 * @param str messaggio di errore da stampare
	 */
	private static void error(String str) {
		errorCount++;
		if (printErrors)
			System.out.println(str);
	}

	// ************* Tokenized Input Stream Section ******************

	private static String current_token = null;

	private static StringTokenizer reader;

	private static BufferedReader in = new BufferedReader(
			new InputStreamReader(System.in));

	// -----------------------------------------------------------------
	// Gets the next input token assuming it may be on subsequent
	// input lines.
	// -----------------------------------------------------------------
	private static String getNextToken() {
		return getNextToken(true);
	}

	// -----------------------------------------------------------------
	// Gets the next input token, which may already have been read.
	// -----------------------------------------------------------------
	private static String getNextToken(boolean skip) {
		String token;

		if (current_token == null)
			token = getNextInputToken(skip);
		else {
			token = current_token;
			current_token = null;
		}

		return token;
	}

	// -----------------------------------------------------------------
	// Gets the next token from the input, which may come from the
	// current input line or a subsequent one. The parameter
	// determines if subsequent lines are used.
	// -----------------------------------------------------------------
	private static String getNextInputToken(boolean skip) {
		final String delimiters = " \t\n\r\f";
		String token = null;

		try {
			if (reader == null)
				reader = new StringTokenizer(in.readLine(), delimiters, true);

			while (token == null || ((delimiters.indexOf(token) >= 0) && skip)) {
				while (!reader.hasMoreTokens())
					reader = new StringTokenizer(in.readLine(), delimiters,
							true);

				token = reader.nextToken();
			}
		} catch (Exception exception) {
			token = null;
		}

		return token;
	}

	/**
	 * restituisce vero se non ci sono più token da leggere sulla linea di input corrente, falso altrimenti
	 * @return vero se non ci sono più token da leggere sulla linea di input corrente, falso altrimenti
	 */
	public static boolean endOfLine() {
		return !reader.hasMoreTokens();
	}

	// ************* Reading Section *********************************

	/**
	 * restituisce la stringa letta in input
	 * @return stringa letta in input
	 */
	public static String readString() {
		StringBuilder str;

		try {
			str = new StringBuilder(getNextToken(false));
			while (!endOfLine()) {
				str.append(getNextToken(false));
			}
		} catch (Exception exception) {
			error("Errore nella lettura della stringa");
			str = null;
		}
		return str.toString();
	}

	/**
	 * restituisce una parola (stringa delimitata da spazi) letta in input
	 * @return parola letta in input
	 */

	public static String readWord() {
		String token;
		try {
			token = getNextToken();
		} catch (Exception exception) {
			error("Errore nella lettura della stringa");
			token = null;
		}
		return token;
	}


	/**
	 * restitusce un booleano letto in input
	 * @return booleano letto in input
	 */
	public static boolean readBoolean() {
		String token = getNextToken();
		boolean bool;
		try {
			if (token.toLowerCase().equals("true"))
				bool = true;
			else if (token.toLowerCase().equals("false"))
				bool = false;
			else {
				error("Errore nella lettura del valore booleano");
				bool = false;
			}
		} catch (Exception exception) {
			error("Errore nella lettura del valore boolenao");
			bool = false;
		}
		return bool;
	}

	/**
	 * restituisce un carattere letto in input
	 * @return carattere letto in input
	 */
	public static char readChar() {
		String token = getNextToken(false);
		char value;
		try {
			if (token.length() > 1) {
				current_token = token.substring(1, token.length());
			} else
				current_token = null;
			value = token.charAt(0);
		} catch (Exception exception) {
			error("Errore nella lettura del carattere");
			value = Character.MIN_VALUE;
		}

		return value;
	}

	/**
	 * restituisce un intero letto in input
	 * @return intero letto in input
	 */
	public static int readInt() {
		String token = getNextToken();
		int value;
		try {
			value = Integer.parseInt(token);
		} catch (Exception exception) {
			error("Errore nella lettura del valore intero");
			value = Integer.MIN_VALUE;
		}
		return value;
	}

	/**
	 * restitusce un intero long letto in input
	 * @return intero long letto in input
	 */
	public static long readLong() {
		String token = getNextToken();
		long value;
		try {
			value = Long.parseLong(token);
		} catch (Exception exception) {
			error("Errore nella lettura del valore long");
			value = Long.MIN_VALUE;
		}
		return value;
	}


	/**
	 * restitusce un valore float letto in input
	 * @return valore float letto in input
	 */
	public static float readFloat() {
		String token = getNextToken();
		float value;
		try {
			value = Float.parseFloat(token);
		} catch (Exception exception) {
			error("Errore nella lettura del valore float");
			value = Float.NaN;
		}
		return value;
	}

	/**
	 * restitusce un valore double letto in input
	 * @return valore double letto in input
	 */
	public static double readDouble() {
		String token = getNextToken();
		double value;
		try {
			value = Double.parseDouble(token);
		} catch (Exception exception) {
			error("Errore nella lettura del valore double");
			value = Double.NaN;
		}
		return value;
	}
}
