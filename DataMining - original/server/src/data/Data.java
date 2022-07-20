package data;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

import database.*;
import example.Example;

/**
 * Gestisce il prelievo del training set e la predizione del obbiettivo secondo
 * l'input dell'utente
 */
public class Data implements Serializable {

    /**
     * List contenente tutti gli esempi prelevati
     */
    private final LinkedList<Example> data;
    /**
     * List contenente tutti i valori target di ciacun esempio prelevato
     */
    private final ArrayList<Double> target;
    /**
     * numero di esempi prelevati
     */
    private final int numberOfExamples;
    /**
     * List contenente gli header delle colonne del training set caratterizzati dal
     * tipo (discreto o continuo)
     */
    private final ArrayList<Attribute> explanatorySet;
    /**
     * Attributo contenente il valore di target
     */
    private ContinuousAttribute classAttribute;

    /**
     * restituisce la stringa per la stampa di tutto ciò che è stato prelevato
     * (indipendetemente da file o da database)
     * 
     * @return restituisce la stringa per la stampa di tutto ciò che è stato
     *         prelevato (indipendetemente da file o da database)
     */
    public String toString() {
        String s = "";

        s = s + "data: " + data.toString() + "\n";
        s = s + "target: " + target.toString() + "\n";
        s = s + "numberOfExamples: " + numberOfExamples + "\n";
        s = s + "explanatorySet: " + explanatorySet;

        return s;
    }

    /**
     * costruttore per la definizione del training set prelevati da un file
     * 
     * @param fileName nome del file da cui prelevare gli esempi
     * @throws TrainingDataException eccezione lanciata nel caso in cui il pattern
     *                               del file inserito non sia corretto o privo di
     *                               dati
     * @throws FileNotFoundException eccezione lanciata in caso di file non trovato
     */
    public Data(String fileName) throws TrainingDataException, FileNotFoundException {

        File inFile = new File(fileName);

        if (!inFile.isFile()) {
            throw new FileNotFoundException("File non trovato o e' una directory");
        }

        if (!fileName.substring(fileName.indexOf(".")).equals(".txt")
                && !fileName.substring(fileName.indexOf(".")).equals(".dat")) {
            throw new FileNotFoundException("Estensione del file non valida (accettati .dat o .txt)");
        }

        Scanner sc = new Scanner(inFile);
        String line = sc.nextLine().trim();

        while ((line.isEmpty() || line.trim().equals("") || line.trim().equals("\n")) && sc.hasNextLine()) {
            line = sc.nextLine().trim();
        }

        if (!sc.hasNextLine()) {
            throw new TrainingDataException("File vuoto");
        }

        String[] s = line.split(" ");

        if (s.length == 2) {
            if (!s[0].contains("@schema")) {
                throw new TrainingDataException("Parametro @schema non dichiarato");
            } else if (!s[1].matches("[0-9]+")) {
                throw new TrainingDataException("Il valore inserito del parametro @schema non e' numerico");
            }

        } else if (s.length > 2) {
            throw new TrainingDataException("Numero di parametri di @schema non valido");
        } else { // s.length < 2
            throw new TrainingDataException("Valore dello @schema non dichiarato");
        }

        // popolare explanatory Set
        explanatorySet = new ArrayList<>(Integer.parseInt(s[1]));
        short iAttribute = 0;
        line = sc.nextLine().trim();

        while ((line.isEmpty() || line.trim().equals("") || line.trim().equals("\n")) && sc.hasNextLine()) {
            line = sc.nextLine().trim();
        }

        if (!sc.hasNextLine()) {
            throw new TrainingDataException("Dati mancanti");
        }

        while (!line.contains("@data") && sc.hasNextLine()) {

            s = line.split(" ");
            if (s[0].equals("@desc")) {

                if (s.length == 3) {
                    if (s[2].equalsIgnoreCase("discrete")) {
                        explanatorySet.add(iAttribute, new DiscreteAttribute(s[1], iAttribute));
                    } else if (s[2].equalsIgnoreCase("continuous")) {
                        explanatorySet.add(iAttribute, new ContinuousAttribute(s[1], iAttribute));
                    } else
                        throw new TrainingDataException("Sintassi parametri @desc errata (discrete o continuous)");
                } else if (s.length > 3) {
                    throw new TrainingDataException("Numero di parametri @desc non valido");
                } else if (s.length < 3) {
                    throw new TrainingDataException("Parametri @desc non dichiarati");
                }

            } else if (s[0].equals("@target")) {

                if (s.length == 2) {
                    classAttribute = new ContinuousAttribute(s[1], iAttribute);
                } else if (s.length > 2) {
                    throw new TrainingDataException("Numero di parametri @target non valido");
                } else if (s.length < 2) { //
                    throw new TrainingDataException("Parametri @target non dichiarati");
                }

            }

            iAttribute++;
            line = sc.nextLine().trim();

            while ((line.isEmpty() || line.trim().equals("") || line.trim().equals("\n")) && sc.hasNextLine()) {
                line = sc.nextLine().trim();
            }

        }

        if (explanatorySet.isEmpty()) {
            throw new TrainingDataException("Nessun parametro @desc dichiarato");
        } else if (classAttribute == null) {
            throw new TrainingDataException("Parametro @target non dichiarato");
        }

        if (!sc.hasNextLine()) {
            throw new TrainingDataException("Parametro @data mancante");
        }

        if (line.split(" ").length == 2) {
            if (!line.split(" ")[1].matches("[0-9]+")) {
                throw new TrainingDataException("Il valore inserito del parametro @data non e' numerico");
            }
        } else if (line.split(" ").length > 2) {
            throw new TrainingDataException("Numero di parametri @data non valido");
        } else if (line.split(" ").length < 2) {
            throw new TrainingDataException("Parametri @data non dichiarati");
        }

        // avvalorare numero di esempi
        numberOfExamples = Integer.parseInt(line.split(" ")[1]);

        // popolare data e target
        data = new LinkedList<>();
        target = new ArrayList<>(numberOfExamples);

        short iRow = 0;
        while (sc.hasNextLine() && iRow < numberOfExamples) {
            boolean checkFlag = false;

            Example e = new Example(getNumberofExplanatoryAttributes());
            line = sc.nextLine().trim();

            while ((line.isEmpty() || line.trim().equals("") || line.trim().equals("\n")) && sc.hasNextLine()) {
                line = sc.nextLine().trim();
                checkFlag = true;
            }

            if (!sc.hasNextLine() && checkFlag) {
                throw new TrainingDataException(
                        "Numero degli esempi presenti nel file diverso da quanto specificato nel paramentro @data. @data: "
                                + numberOfExamples + "; esempi tovati: " + iRow);
            }

            s = line.split("\\s*,\\s*");

            if (getNumberofExplanatoryAttributes() != s.length - 1) {
                throw new TrainingDataException(
                        "Il numero dei parametri di un esempio non e' coerente con quanto dichiarato, riga: "
                                + (iRow + 1));
            }

            for (short jColumn = 0; jColumn < s.length - 1; jColumn++) {

                if (s[jColumn].trim().equals("") || s[jColumn] == null) {
                    throw new TrainingDataException("Parametro non presente alla riga: " + (iRow + 1));
                }

                if (s[jColumn].trim().matches("[0-9]+[\\.]?[0-9]*")) {
                    e.set(Double.parseDouble(s[jColumn]), jColumn);
                } else if (s[jColumn].trim().matches("^[a-zA-Z0-9]*$")) {
                    e.set(s[jColumn], jColumn);
                }
            }

            data.add(iRow, e);

            if (!s[s.length - 1].matches("[0-9]+[\\.]?[0-9]*")) {
                throw new TrainingDataException(
                        "Il valore dell'esempio presente alla riga " + (iRow + 1) + " non e' numerico");
            }

            target.add(iRow, Double.valueOf(s[s.length - 1]));

            int k = 0;
            for (Attribute a : explanatorySet) {

                try {
                    if (a instanceof ContinuousAttribute continuousAttribute) {
                        continuousAttribute.setMax((Double) e.get(k));
                        continuousAttribute.setMin((Double) e.get(k));
                    }
                } catch (ClassCastException cce) {
                    throw new TrainingDataException("Presente una stringa come attributo continuo");
                }

                k++;
            }

            iRow++;
        }

        if (sc.hasNextLine()) {
            line = sc.nextLine().trim();

            while (!(line.isEmpty() || line.trim().equals("") || line.trim().equals("\n")) && sc.hasNextLine()) {
                line = sc.nextLine().trim();
                iRow++;
            }
        }

        if (iRow != numberOfExamples) {
            throw new TrainingDataException(
                    "Numero degli esempi presenti nel file diverso da quanto specificato nel paramentro @data. @data: "
                            + numberOfExamples + "; esempi tovati: " + iRow);
        }

        sc.close();
    }

    /**
     * costruttore per la definizione del training set prelevati dal database
     *
     * @param db    DataBase da cui prelevare il training set
     * @param table tabella presente nel database da cui prelevare il training set
     * @throws InsufficientColumnNumberException eccezione lanciata in caso di
     *                                           numero di colonne minore di 2
     * @throws SQLException                      eccezione lanciata in caso di
     *                                           sintassi query errata
     * @throws NoValueException                  eccezione lanciata in caso di valore null presente nella tabella
     */
    public Data(DbAccess db, String table) throws InsufficientColumnNumberException, SQLException, NoValueException {

        TableSchema tableSchema = new TableSchema(table, db);
        TableData tableData = new TableData(db, tableSchema);

        explanatorySet = new ArrayList<>();

        Iterator<Column> itSchema = tableSchema.iterator();
        int counter = 0;
        while (itSchema.hasNext()) {
            Column col = itSchema.next();

            if (!col.isNumber()) {
                explanatorySet.add(counter, new DiscreteAttribute(col.getColumnName(), counter));
            } else {
                explanatorySet.add(counter, new ContinuousAttribute(col.getColumnName(), counter));
                ((ContinuousAttribute) explanatorySet.get(counter))
                        .setMin(tableData.getAggregateColumnValue(col, QUERY_TYPE.MIN));
                ((ContinuousAttribute) explanatorySet.get(counter))
                        .setMax(tableData.getAggregateColumnValue(col, QUERY_TYPE.MAX));
            }

            counter++;
        }

        numberOfExamples = tableData.getExamples().size();

        data = new LinkedList<>();
        target = new ArrayList<>(numberOfExamples);

        Iterator<Example> itData = tableData.getExamples().iterator();
        Iterator<?> itTarget = tableData.getTargetValues().iterator();
        int i = 0;
        while (itData.hasNext()) {
            data.add(i, itData.next());
            target.add(i, (Double) itTarget.next());
            i++;
        }

    }

    private int getNumberofExplanatoryAttributes() {
        return explanatorySet.size();
    }

    /*
     * Partiziona data rispetto all'elemento x di key e restiutisce il punto di
     * separazione
     */
    private int partition(ArrayList<Double> key, int inf, int sup) {
        int i = inf, j = sup;
        int med = (inf + sup) / 2;

        Double x = key.get(med);

        data.get(inf).swap(data.get(med));

        double temp = target.get(inf);
        target.set(inf, target.get(med));
        target.set(med, temp);

        temp = key.get(inf);
        key.set(inf, key.get(med));
        key.set(med, temp);

        while (true) {

            while (i <= sup && key.get(i) <= x) {
                i++;
            }

            while (key.get(j) > x) {
                j--;
            }

            if (i < j) {
                data.get(i).swap(data.get(j));

                temp = target.get(i);
                target.set(i, target.get(j));
                target.set(j, temp);

                temp = key.get(i);
                key.set(i, key.get(j));
                key.set(j, temp);

            } else {
                break;
            }
        }

        data.get(inf).swap(data.get(j));

        temp = target.get(inf);
        target.set(inf, target.get(j));
        target.set(j, temp);

        temp = key.get(inf);
        key.set(inf, key.get(j));
        key.set(j, temp);

        return j;

    }

    /*
     * Algoritmo quicksort per l'ordinamento di data
     * usando come relazione d'ordine totale "<=" definita su key
     * 
     * @param A
     */
    private void quicksort(ArrayList<Double> key, int inf, int sup) {

        if (sup >= inf) {

            int pos;

            pos = partition(key, inf, sup);

            if ((pos - inf) < (sup - pos + 1)) {
                quicksort(key, inf, pos - 1);
                quicksort(key, pos + 1, sup);
            } else {
                quicksort(key, pos + 1, sup);
                quicksort(key, inf, pos - 1);
            }

        }

    }

    /**
     * Funzione per il calcolo del target dell'esempio inserito mediante l'algoritmo
     * di regressione
     * 
     * @param e esempio inserito dall'utente
     * @param k range dei target degli esempi di cui calcolare la media
     * @return restituisce il target dell'esempio inserito dall'utente della media
     *         dei target dei k esempi più "vicini" presi dai training set
     */
    public double avgClosest(Example e, int k) {
        int diff = 0;
        double somma = 0;
        ArrayList<Double> key = new ArrayList<>(data.size());

        e = scaledExample(e);

        for (Example ecsample : data) {
            key.add(scaledExample(ecsample).distance(e));
        }

        quicksort(key, 0, key.size() - 1);

        Double min = key.get(0);
        int i;
        for (i = 0; i < target.size() && diff < k; i++) {
            if (key.get(i).equals(min)) {
                somma = somma + target.get(i);
            } else {
                diff++;
                min = key.get(i);
                i--;
            }
        }

        return somma / (i);
    }

    /**
     * funzione per la lettura dal client degli attributi dell'esempio dell'utente
     * 
     * @param out ObjectOutputStream per inviare i dati al client
     * @param in  ObjectInputStream per ricevere i dati dal client
     * @return esempio contenente i valori degli attributi inseriti dall'utente
     * @throws IOException            eccezione generata in caso di errata
     *                                comunicazione con il client
     * @throws ClassNotFoundException eccezione generata in caso di errato cast
     */
    public Example readExample(ObjectOutputStream out, ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        Example e = new Example(getNumberofExplanatoryAttributes());

        int i = 0;
        for (Attribute a : explanatorySet) {
            if (a instanceof DiscreteAttribute) {
                out.writeObject("@READSTRING"); // tag
                out.writeObject("Inserisci valore discreto X[" + i + "]:");

                e.set(in.readObject(), i);
            } else {
                double x;
                do {
                    out.writeObject("@READDOUBLE"); // tag
                    out.writeObject("Inserisci valore continuo X[" + i + "]:");

                    x = (Double) in.readObject();
                } while (Double.valueOf(x).equals(Double.NaN));
                e.set(x, i);
            }
            i++;
        }

        out.writeObject("@ENDEXAMPLE");
        return e;
    }

    private Example scaledExample(Example e) {
        int i = 0;
        Example supp = new Example(getNumberofExplanatoryAttributes());

        for (Attribute a : explanatorySet) {
            if (a instanceof ContinuousAttribute) {
                Double scaled = ((ContinuousAttribute) a).scale(Double.parseDouble(e.get(i).toString()));
                supp.set(scaled, i);
            } else if (a instanceof DiscreteAttribute) {
                supp.set(e.get(i), i);
            }

            i++;
        }

        return supp;
    }

}
