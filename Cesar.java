import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Cesar {

    public static Scanner scanner = new Scanner(System.in);
    private static StringBuilder originalText;
    private static String choise;
    private static final String ALPHABET_STR = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя.,! ?\":;АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    private static final char[] ALPHABET_CHAR = ALPHABET_STR.toCharArray();
    private static int key;

    public static void setKey(int key) {
        Exception exception = new Exception();
        if (key >= 0 && (key % 75) != 0) {
            Cesar.key = key;
        } else try {
            throw exception;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static StringBuilder getOriginalText() {
        return originalText;
    }

    public static void setOriginalText(StringBuilder originalText) {
        Cesar.originalText = originalText;
    }

    public static String getChoise() {
        return choise;
    }

    public static void setChoise(String choise) {
        Cesar.choise = choise;
    }

    public static void main(String[] args) {

        boolean flaqForSwitch = true;

        while (flaqForSwitch) {
            switch (greeting()) {

                case "1":
                    System.out.println("Please enter the path to file that need to be encrypted");
                    flaqForSwitch = false;
                    break;
                case "2":
                    System.out.println("Please enter the path to file that need to be decrypted");
                    flaqForSwitch = false;
                    break;
                case "3":
                    System.out.println("Please enter the path to file that need to be decrypted by brute force");
                    flaqForSwitch = false;
                    break;
                case "4":
                    System.out.println("Please enter the path to file that need to be decrypted by using the statistical analysis");
                    flaqForSwitch = false;
                    break;
                case "5":
                    System.out.println("Good bye!");
                    flaqForSwitch = false;
                    return;
                default:
                    System.out.println("you entered the wrong expression. Please try again.");

            }
        }
        String filePath = scanner.nextLine();
        setOriginalText(originContent(filePath));
        switch (choise) {
            case "1":
                encrypter(filePath);
                break;
            case "2":
                decrypter(filePath);
                break;
            case "3":
                brutforcer(filePath);
                break;
        }


    }

    public static String greeting() {
        System.out.println("Hello!\n" +
                "Please choose and enter the number of action:\n" +
                "1. Encrypt the text\n" +
                "2. Decrypt the text\n" +
                "3. Decrypt the text by brute force\n" +
                "4. Decrypt the text by using the statistical analysis\n" +
                "5. Exit");

        String answer = scanner.nextLine();
        setChoise(answer);
        return answer;
    }

    public static StringBuilder originContent(String filePath) {
        StringBuilder stb = new StringBuilder("");

        try (FileReader reader = new FileReader(filePath);
             BufferedReader bfreader = new BufferedReader(reader)) {
            while (bfreader.ready()) {
                stb.append(bfreader.readLine());
                stb.append("\n");
            }

        } catch (FileNotFoundException e) {
            System.out.println("This fil can't be reached.\n" +
                    "Please make sure that path is correct or enter another one");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stb;

    }

    public static void encrypter(String filePath) {
        System.out.println("enter any key except multiple of 75");
        setKey(scanner.nextInt());
        Path pathParent = Path.of(filePath).getParent();
        String newPath = pathParent.toString() + "\\encrypted.txt";
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < originalText.length(); i++) {

            int index = ALPHABET_STR.indexOf(originalText.charAt(i));
            int newIndex = (index + key) % ALPHABET_STR.length();
            char newChar = ALPHABET_STR.charAt(newIndex);
            stringBuilder.append(newChar);
        }
        writer(newPath, stringBuilder);
    }

    public static void decrypter(String filePath) {
        System.out.println("enter any key except multiple of 75");
        setKey(scanner.nextInt());
        Path pathParent = Path.of(filePath).getParent();
        String newPath = pathParent.toString() + "\\decrypted.txt";
        StringBuilder stringBuilder = new StringBuilder(originalText);
        decrypter(stringBuilder);
        writer(newPath, stringBuilder);
    }

    public static void decrypter(StringBuilder stb) {
        for (int i = 0; i < originalText.length(); i++) {
            int index = ALPHABET_STR.indexOf(originalText.charAt(i));
            int length = ALPHABET_STR.length();
            int newIndex = Math.abs(index - key + length);
            int newIndex2 = newIndex % length;
            char newChar = ALPHABET_STR.charAt(newIndex2);
            stb.setCharAt(i, newChar);
        }
    }

    public static void decrypter(StringBuilder stb, int key) {
        for (int i = 0; i < originalText.length(); i++) {
            int index = ALPHABET_STR.indexOf(originalText.charAt(i));
            int length = ALPHABET_STR.length();
            int newIndex = Math.abs(index - key + length);
            int newIndex2 = newIndex % length;
            char newChar = ALPHABET_STR.charAt(newIndex2);
            stb.setCharAt(i, newChar);
        }
    }

    public static boolean isValid(StringBuilder stringBuilder) {

        String[] separated = stringBuilder.toString().split(" ");
        for (int i = 0; i < separated.length; i++) {
            if (separated[i].length() > 26) {
                return false;
            }
        }

        return true;

    }

    public static void brutforcer(String filePath) {
        Path pathParent = Path.of(filePath).getParent();
        String newPath = pathParent.toString() + "\\brutforced.txt";
        StringBuilder stringBuilder = new StringBuilder(originalText);

        for (int i = 0; i < ALPHABET_STR.length(); i++) {
            decrypter(stringBuilder, i);
            if (isValid(stringBuilder)) {
                break;
            }
        }
        writer(newPath, stringBuilder);
    }

    public static void writer (String filePath, StringBuilder newContent){
        try {
            Files.writeString(Path.of(filePath), newContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
