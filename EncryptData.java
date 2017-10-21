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

public static void main(String[] args) {

}
}
