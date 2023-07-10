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


    public static String[] readWordsToArray(BufferedReader reader){
        ArrayList<String> linesRead = new ArrayList<>();
        try {
            String line = "";
            while ((line = reader.readLine()) != null){

                linesRead.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ArrayList<String> noWhiteSpace = new ArrayList<>() ;

        for (int i = 0; i < linesRead.size(); i++){
            String[] individualWords = linesRead.get(i).split("\\s");
            System.out.println(Arrays.toString(individualWords));

            for (String word: individualWords) {
                noWhiteSpace.add(word);

            }
        }

        String[] linesReadArray = noWhiteSpace.toArray(new String[linesRead.size()]);
        return linesReadArray;
    }

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
