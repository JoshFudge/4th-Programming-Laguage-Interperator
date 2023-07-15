package LanguageTranslator;

import java.io.*;
import java.util.*;

public class Main {
    //TODO set up the commandline argument stuff, try sqashing any other bugs found

    // Main mthod that will take a filepath as an argument to read from
    public static void main(String[] args) throws RuntimeException {
        // If there is no arguments or too many, throw an error and display an error message..
        if (args.length != 1){
            throw new IllegalArgumentException("Program expects a file Path to read from ");
        }

        // Initialize an arraylist of word objects that wll be used to hold all the words
        ArrayList<Word> programs = new ArrayList<>();

        // Create a new file using the filepath argument entered when the program was started
        File FourthProgramingLanguageScript = new File(args[0]);
//        File FourthProgramingLanguageScript = new File("src/LanguageTranslator/languageScript.txt");

        // Initialize a reader that calls the getFIleReader method and uses the created file
        BufferedReader reader = getFileReader(FourthProgramingLanguageScript);

        // make a String list from the return value of the readWordsToArray method
        String[] script = readWordsToArray(reader);

        // For each string in the script list, create a word object from the string and add it to the Word list
        for (String w: script) {
           Word currentWord = new Word(w,Word.determineWordType(w));
            programs.add(currentWord);
        }

        // Run the translatePrograms method that will do the calculations/operations
        Translator.translatePrograms(programs);

    }

    //Method to return a string array of each "word" in the script
    public static String[] readWordsToArray(BufferedReader reader){
        //initialize a list that will contain each line in the text file
        ArrayList<String> linesRead = new ArrayList<>();
        // Try adding each line to the arraylist and catch any exceptions
        try {
            String line = "";
            while ((line = reader.readLine()) != null){

                linesRead.add(line);
            }
        } catch (IOException e) {
            throw new java.lang.RuntimeException(e);
        }

        // Initialize an arraylist for each word with the whitespace removed
        ArrayList<String> noWhiteSpace = new ArrayList<>() ;

        // For each line in the array..
        for (int i = 0; i < linesRead.size(); i++){
            // Split the list by the whitespace in each line using Regex and put it into a list
            String[] individualWords = linesRead.get(i).split("\\s+");
            System.out.println(Arrays.toString(individualWords));
            // For each word in the list add it to the whitespaceRemoved list
            for (String word: individualWords) {
                noWhiteSpace.add(word);

            }
        }
        // Convert the noWhiteSpace list into an arrayList
        String[] linesReadArray = noWhiteSpace.toArray(new String[linesRead.size()]);
        //Return the converted list
        return linesReadArray;
    }

    // Method to create a reader for the text file that will contain the programs
    public static BufferedReader getFileReader(File file){
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
        }catch (FileNotFoundException e) {
            throw new java.lang.RuntimeException("File Not Found",e);
        }
        return br ;
    }
}
