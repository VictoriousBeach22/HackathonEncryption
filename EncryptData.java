/**
 * Put a short phrase describing the program here.
 *
 * @author Put your name here
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
    * "Rotates" the current char a specified amount between 32 and 126 (inclusive),
    * so that it wraps around (ex: 124 + 4 = 33).
    */
    private static char rotateForward(char c, int amount){
        //TODO
            int val = c - 32; //adjust bounds
            val += amount; //encrypt
            val %= 94; //rebound
            val += 32; //return to original bounds
            return (char) val; //return char    
    }

    /**
    * Rotate forward, the sequel. 
    * The same thing as above, but with subtraction (backwards).
    */
    private static char rotateBackward(char c, int amount){
            int val = c - 32; //adjust bounds
            val -= amount; //encrypt
            if (val < 0) { //rebound
                val += 94;
            }
            val += 32; //return to original bounds
            return (char) val; //return char
    }
    
    /*
     * Encrypt a String via an array of random integers.
     */
    private static String encryptString(String toEncrypt, int[] randoms) {
        String encrypted = "";
        for (int i = 0; i < toEncrypt.length(); i++) {
            encrypted += rotateForward(toEncrypt.charAt(i), randoms[i]);
        }
        return encrypted;
    }

    /*
     * Encrypt whole data set
     */
    private static String[] encryptData(String[] message,
            Queue<Integer> randoms) {

        //declare variables
        String[] encryptedMessage = new String[message.length];

        //for each element in the message
        for (int i = 0; i < message.length; i++) {

            //call encryptString
            int[] subRandoms = new int[message[i].length()];
            for (int q = 0; q < message[i].length(); q++) {
                subRandoms[q] = randoms.remove();
            }
            encryptedMessage[i] = encryptString(message[i], subRandoms);
        }

        //return
        return encryptedMessage;
    }

public static void main(String[] args) {

}
}
