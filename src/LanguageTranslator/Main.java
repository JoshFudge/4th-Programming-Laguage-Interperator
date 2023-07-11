package LanguageTranslator;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        ArrayList<Word> programs = new ArrayList<>();
        File FourthProgramingLanguageScript = new File("src/LanguageTranslator/languageScript.txt");
        BufferedReader reader = getFileReader(FourthProgramingLanguageScript);
        String[] script = readWordsToArray(reader);


        for (String w: script) {
           Word currentWord = new Word(w,Word.determineWordType(w));
            programs.add(currentWord);
        }


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
            throw new RuntimeException(e);
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
            throw new RuntimeException("File Not Found",e);
        }
        return br ;
    }
}
