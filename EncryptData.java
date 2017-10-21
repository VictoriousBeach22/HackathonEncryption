import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
        //alex suks eggs
        //fuk u Jme
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

    /**
     * Rotate forward, the sequel. The same thing as above, but with subtraction
     * (backwards).
     */
    private static char rotateBackward(char c, int amount) {
        int val = c - 32; //adjust bounds
        val -= amount; //encrypt
        if (val < 0) { //rebound
            val += 94;
        }
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

        //fore each element in the message
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
    private static ArrayList<Integer> generateKey(ArrayList<Integer> numbers,
            ArrayList<String> message) {
        ArrayList<Integer> key = new ArrayList<Integer>();
        for (int i = 0; i < message.size(); i++) {
            for (int j = 0; j < message.get(i).length(); j++) {
                key.add(numbers.remove(numbers.size() - 1));
            }
        }
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
        int input = Integer.parseInt(
                (String) JOptionPane.showInputDialog(null, "Total Keys:",
                        "Select number of keys:", JOptionPane.QUESTION_MESSAGE,
                        new ImageIcon("programData/client.png"), // Use
                        // default
                        // icon
                        choices, // Array of choices
                        choices[0])); // Initial choice
        return input;
    }

    public static void main(String[] args) throws FileNotFoundException {

        //test gui
        int numKeys = askForKeyNum();

        //input message and random numbers
        String fileName = JOptionPane.showInputDialog("File to encrypt:");

        Scanner inputFile = new Scanner(new File("File/" + fileName));
        Scanner inputNumbers = new Scanner(new File("Random/numbers.txt"));

        ArrayList<String> message = new ArrayList<String>();
        ArrayList<Integer> numbers = new ArrayList<Integer>();

        message = stringInput(inputFile);
        numbers = integerInput(inputNumbers);

        //generate keys
        ArrayList<Integer> key1 = generateKey(numbers, message);
        ArrayList<Integer> key2 = new ArrayList<Integer>();
        ArrayList<Integer> key3 = new ArrayList<Integer>();
        if (numKeys > 1) {
            key2 = generateKey(numbers, message);
            if (numKeys > 2) {
                key3 = generateKey(numbers, message);
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
        inputNumbers.close();

    }
}
