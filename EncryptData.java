import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author SAVJ
 *
 */
public final class EncryptData {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private EncryptData() {
    }

    /**
     * "Rotates" the current char a specified amount between 32 and 126
     * (inclusive), so that it wraps around (ex: 124 + 4 = 33).
     */
    private static char rotateForward(char c, int amount) {
        int val = c - 32; //adjust bounds
        val += amount; //encrypt
        val %= 94; //rebound
        val += 32; //return to original bounds
        return (char) val; //return char
    }

    /*
     * ENCRYPT a String via an array of random integers.
     */
    private static String encryptString(String toEncrypt, int[] randoms) {
        String encrypted = "";
        for (int i = 0; i < toEncrypt.length(); i++) {
            encrypted += rotateForward(toEncrypt.charAt(i), randoms[i]);
        }
        return encrypted;
    }

    /*
     * ENCRYPT whole data set
     */
    private static ArrayList<String> encryptData(ArrayList<String> message,
            Queue<Integer> randoms) {

        //declare variables
        ArrayList<String> encryptedMessage = new ArrayList<String>();

        //for each element in the message
        for (int i = 0; i < message.size(); i++) {

            //call encryptString
            int[] subRandoms = new int[message.get(i).length()];
            for (int q = 0; q < message.get(i).length(); q++) {
                subRandoms[q] = randoms.remove();
            }
            encryptedMessage.add(encryptString(message.get(i), subRandoms));
        }

        //return
        return encryptedMessage;
    }

    /*
     * Returns an integer array of input length
     */
    private static ArrayList<Integer> generateKey(ArrayList<String> message)
            throws IOException {
        ArrayList<Integer> key = new ArrayList<Integer>();
        //ArrayList<Integer> subKey = new ArrayList<Integer>();

        //calculate size
        int messageLength = 0;
        for (int i = 0; i < message.size(); i++) {
            messageLength += message.get(i).length();
        }

        //fetch random numbers equivalent to message length
        key = getNums(messageLength);

        //        //check how many times to fetch the max
        //        int timesToFetchMax = 0;
        //        while (messageLength > 9990) {
        //            messageLength -= 9990;
        //            timesToFetchMax++;
        //        }
        //
        //        //fetch the max that amount of times
        //        for (int i = 0; i < timesToFetchMax; i++) {
        //            subKey = getNums(9990);
        //            for (int j = 0; j < subKey.size(); j++) {
        //                key.add(subKey.get(j));
        //            }
        //        }
        //
        //        //fetch the remaining numbers
        //        subKey = getNums(messageLength);
        //        for (int j = 0; j < subKey.size(); j++) {
        //            key.add(subKey.get(j));
        //        }

        return key;
    }

    /**
     * Read input data into an array of strings, where each element is one line
     * of the input text
     *
     * @return array list
     * @param input
     */
    private static ArrayList<Integer> integerInput(Scanner input) {
        ArrayList<Integer> numberKey = new ArrayList<Integer>();
        int number = 0;
        String line = input.nextLine();
        while (input.hasNextLine()) {
            if (line != null) {
                number = Integer.parseInt(line);
                numberKey.add(number);
            }
            line = input.nextLine();
        }
        number = Integer.parseInt(line);
        numberKey.add(number);
        return numberKey;
    }

    /**
     * Read input data into an array of strings, where each element is one line
     * of the input text
     *
     * @return array list
     * @param input
     */
    private static ArrayList<String> stringInput(Scanner input) {
        ArrayList<String> toEncrypt = new ArrayList<String>();
        String line = input.nextLine();
        while (input.hasNextLine()) {
            if (line != null) {
                toEncrypt.add(line);
            }
            line = input.nextLine();
        }
        toEncrypt.add(line);
        return toEncrypt;
    }

    public static <T> void writeFile(ArrayList<T> data, String folder,
            String name) throws IOException {
        BufferedWriter writer = new BufferedWriter(
                new FileWriter(folder + "/" + name + ".txt"));

        for (int i = 0; i < data.size(); i++) {
            writer.write(data.get(i).toString());
            writer.write('\n');
        }

        writer.close();
    }

    public static int askForKeyNum() {
        String[] choices = { "1", "2", "3" };
        int input = Integer.parseInt((String) JOptionPane.showInputDialog(null,
                "Total keys:", "ENCRYPT", JOptionPane.QUESTION_MESSAGE,
                new ImageIcon("programData/client.png"), // Use
                // default
                // icon
                choices, // Array of choices
                choices[0])); // Initial choice
        return input;
    }

    /**
     * Pull the given amount of random numbers from the random number website in
     * html and saves it to a text file that will be used for the random numbers
     */
    private static ArrayList<String> pullRandNumbershtml(int n)
            throws IOException {
        //INCREMENT THE AMOUNT OF RAND NUMBERS BY 1 TO MAKE IT EASIER
        int total = n + 1;

        //CONNECTS TO THE RANDOM NUMBER WEBSITE AND GENERATES THE GIVEN NUMBER OF VALUES
        URL url = new URL("https://www.random.org/integers/?num=" + total
                + "&min=0&max=93&col=1&base=10&format=html&rnd=new");
        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();
        Scanner numbers = new Scanner(new InputStreamReader(is));
        ArrayList<String> numberhtml = new ArrayList<>();
        String number = numbers.nextLine();

        //CREATE AN ARRAY LIST OF THE WEBSITE HTML LINE BY LINE
        while (numbers.hasNextLine()) {
            if (number != null) {
                numberhtml.add(number);
            }
            number = numbers.nextLine();
        }

        //CLOSE THE SCANNER
        numbers.close();

        //RETURN THE STRING ARRAYLIST OF THE WEBSITE HTML
        return numberhtml;
    }

    /**
     * Parse only the numbers from the array list created of the random number
     * website's html
     */
    private static ArrayList<Integer> randNumberList(
            ArrayList<String> numberhtml) {

        //CREATE AN EMPTY INTEGER ARRAYLIST
        ArrayList<Integer> randomNumber = new ArrayList<>();

        //INITIALIZE VALUES
        int number = 0;
        int count = 142;
        boolean numbersExist = true;

        //WHILE THE STRING VALUE IS AN INTEGER CONVERT IT AND ADD IT TO AN INTEGER ARRAY LIST
        while (numbersExist) {
            number = Integer.parseInt(numberhtml.get(count));
            randomNumber.add(number);
            count++;

            //IF IT REACHES INTO THE END ATTRIBUTE STOP
            if (numberhtml.get(count).equals("</pre>")) {
                numbersExist = false;
            }
        }

        //RETURN THE INTEGER ARRAY OF THE RANDOM NUMBERS GENERATED
        return randomNumber;
    }

    public static ArrayList<Integer> getNums(int amount) throws IOException {
        ArrayList<String> numbershtml = new ArrayList<>();
        numbershtml = pullRandNumbershtml(amount + 1);

        ArrayList<Integer> randomNumber = new ArrayList<>();
        randomNumber = randNumberList(numbershtml);

        return randomNumber;
    }

    public static void main(String[] args) throws IOException {

        //test gui
        int numKeys = askForKeyNum();

        //input message and random numbers
        String fileName = JOptionPane.showInputDialog("File to encrypt:");

        Scanner inputFile = new Scanner(new File("File/" + fileName));
        //Scanner inputNumbers = new Scanner(new File("Random/numbers.txt"));

        ArrayList<String> message = new ArrayList<String>();
        ArrayList<Integer> numbers = new ArrayList<Integer>();

        message = stringInput(inputFile);
        //numbers = integerInput(inputNumbers);

        //generate keys
        ArrayList<Integer> key1 = generateKey(message);
        ArrayList<Integer> key2 = new ArrayList<Integer>();
        ArrayList<Integer> key3 = new ArrayList<Integer>();
        if (numKeys > 1) {
            key2 = generateKey(message);
            if (numKeys > 2) {
                key3 = generateKey(message);
            }
        }

        try {
            writeFile(key1, "Keys", "key1");
            if (numKeys > 1) {
                writeFile(key2, "Keys", "key2");
                if (numKeys > 2) {
                    writeFile(key3, "Keys", "key3");
                }
            }

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        //populate queue
        Queue<Integer> key1q = new LinkedList<Integer>();
        Queue<Integer> key2q = new LinkedList<Integer>();
        Queue<Integer> key3q = new LinkedList<Integer>();

        for (int i = 0; i < key1.size(); i++) {
            key1q.add(key1.get(i));
            if (numKeys > 1) {
                key2q.add(key2.get(i));
                if (numKeys > 2) {
                    key3q.add(key3.get(i));
                }
            }
        }

        //encrypt
        ArrayList<String> encrypted = encryptData(message, key1q);
        if (numKeys > 1) {
            encrypted = encryptData(encrypted, key2q);
            if (numKeys > 2) {
                encrypted = encryptData(encrypted, key3q);
            }
        }

        try {
            writeFile(encrypted, "File", "encryptedMessage");
            JFrame parent = new JFrame();
            JOptionPane.showMessageDialog(parent, "Encryption complete.");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        inputFile.close();
    }
}
