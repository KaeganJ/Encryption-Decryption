package encryptdecrypt;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import static java.lang.Character.isLetter;


public class Main {
    public static void main(String[] args) throws IOException {

        /* Initialize Variables */
        String mode = "enc";
        int key = 0;
        String data = "";
        String alg = "shift";
        String outToFile = null;


        /* Set program args to variables */
        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("-mode")) {
                mode = args[i + 1];
            } else if (args[i].contains("-key")) {
                key = Integer.parseInt(args[i + 1]);
            } else if (args[i].contains("-in")) {
                data = GetTextFromFile(args[i + 1]);
            } else if (args[i].contains("-data")) {
                data = args[i + 1];
            } else if (args[i].contains("-out")) {
                outToFile = args[i + 1];
            } else if (args[i].contains("-alg")) {
                alg = args[i + 1];
            }
        }

        /* Sets mode variable to shift or unicode */
        if (alg.equals("unicode")) {
            switch (mode) {
                case "enc" -> mode = "unienc";
                case "dec" -> mode = "unidec";
            }
        } else {
            switch (mode) {
                case "enc" -> mode = "shiftenc";
                case "dec" -> mode = "shiftdec";
            }
        }
        /* Calls the correct method based on the defined mode */
        if (outToFile == null) {
            switch (mode) {
                case "unienc" -> System.out.println(uniEncryption(data, key));
                case "unidec" -> System.out.println(uniDecryption(data, key));
                case "shiftenc" -> System.out.println(shiftEncryption(data, key));
                case "shiftdec" -> System.out.println(shiftDecryption(data, key));
            }
        } else {
            switch (mode) {
                case "unienc" -> SendToFile(uniEncryption(data, key), outToFile);
                case "unidec" -> SendToFile(uniDecryption(data, key), outToFile);
                case "shiftenc" -> SendToFile(shiftEncryption(data, key), outToFile);
                case "shiftdec" -> SendToFile(shiftDecryption(data, key), outToFile);
            }
        }

    }

    /* Unicode Encryption Method */
    public static String uniEncryption(String data, int key) {
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            char ch = (char) (data.charAt(i) + key);
            encrypted.append(ch);
        }
        return encrypted.toString();
    }

    /* Unicode Decryption Method */
    public static String uniDecryption(String data, int key) {
        StringBuilder decrypted = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            char ch = (char) (data.charAt(i) - key);
            decrypted.append(ch);
        }
        return decrypted.toString();
    }

    /* Shift Encryption Method */
    public static String shiftEncryption(String data, int key) {
        int originalPosition;
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            if (isLetter(data.charAt(i))) {
                if (data.charAt(i) <= 'z' && data.charAt(i) >= 'a') {
                    originalPosition = data.charAt(i) - 'a';
                    int newPosition = (originalPosition + key) % 26;
                    char ch = (char) ('a' + newPosition);
                    encrypted.append(ch);
                } else {
                    originalPosition = data.charAt(i) - 'A';
                    int newPosition = (originalPosition + key) % 26;
                    char ch = (char) ('A' + newPosition);
                    encrypted.append(ch);
                }
            } else {
                encrypted.append(data.charAt(i));
            }
        }
        return encrypted.toString();
    }

    /* Shift Decryption Method */
    public static String shiftDecryption(String data, int key) {
        StringBuilder decrypted = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            if (isLetter(data.charAt(i))) {
                if (data.charAt(i) <= 'z' && data.charAt(i) >= 'a') {
                    if (data.charAt(i) - key < 'a') {
                        int offset = 26 - key;
                        char ch = (char) (data.charAt(i) + offset);
                        decrypted.append(ch);
                    } else {
                        char ch = (char) (data.charAt(i) - key);
                        decrypted.append(ch);
                    }
                } else if (data.charAt(i) <= 'Z' && data.charAt(i) >= 'A') {
                    if (data.charAt(i) - key < 'A') {
                        int offset = 26 - key;
                        char ch = (char) (data.charAt(i) + offset);
                        decrypted.append(ch);
                    } else {
                        char ch = (char) (data.charAt(i) - key);
                        decrypted.append(ch);
                    }
                }
            } else {
                decrypted.append(data.charAt(i));

            }
        }
        return decrypted.toString();
    }

    /* Pulling text from a file specified in the launch args */
    public static String GetTextFromFile(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    /* Sending text to a file specified in the launch args */
    public static void SendToFile(String encryption, String fileName) throws IOException {
        try (PrintWriter printWriter = new PrintWriter(fileName)) {
            printWriter.print(encryption);
        } catch (IOException e) {
            System.out.println("Error : CANNOT WRITE TO FILE");
        }
    }
}

