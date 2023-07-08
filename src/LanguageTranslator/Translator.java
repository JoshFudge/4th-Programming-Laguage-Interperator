package LanguageTranslator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Translator {
    static ArrayList<Word> currentOperation = new ArrayList<>();

    public static void translatePrograms(ArrayList<Word> originalStack){


        currentOperation.addAll(originalStack);
        for (Word w: originalStack) {


            if (w.getType() == Word.wordType.STACKOPERATION) {
                if (Objects.equals(w.getWord(), "+")) {
                    String answer = ExecuteIntegerPlus();
                    currentOperation.set(0,new Word(answer,Word.determineWordType(answer)));
                } else if (Objects.equals(w.getWord(),"-")) {
                    String answer = ExecuteIntegerNeg();
                    currentOperation.set(0,new Word(answer,Word.determineWordType(answer)));
                }
            }





            else if (w.getType() == Word.wordType.IO){
                if (Objects.equals(w.getWord(), "in")){
                    Word input = Translator.ExecuteIn();
                    currentOperation.set(currentOperation.indexOf(w), input);
                } else if (Objects.equals(w.getWord(), "out")) {

                    ExecuteOut();
                    currentOperation.remove(0);
                    if (!currentOperation.isEmpty() && Objects.equals(currentOperation.get(0).getWord(), "out")){
                        currentOperation.remove(0);
                    }
                }
            }
            

        }


    }


    public static String ExecuteIntegerPlus(){

        int total = Integer.parseInt(currentOperation.get(0).getWord()) + Integer.parseInt(currentOperation.get(1).getWord());
        currentOperation.remove(1);
        return Integer.toString(total);

    }

    public static String ExecuteIntegerNeg(){

            int neg = Integer.parseInt(currentOperation.get(0).getWord()) * (-1);
            currentOperation.remove(1);
            return Integer.toString(neg);

    }

    public static String ExecuteStringPlus(ArrayList<String> stringWords){
        String output = "";
        for (String word: stringWords) {
            output += (word + " ");
        }
        return output;
    }

    public static String ExecuteStringNeg(String wordToBeReversed){

        return wordToBeReversed;
    }


    public static void ExecuteWord(Word word){

    }

    public void ExecuteQuote(ArrayList<String>StringWords){

    }

    public static Word ExecuteIn(){
        String userInput = "";
        try {
            System.out.println("Enter a value: ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            userInput = br.readLine();
        }catch (IOException e){
            System.out.println("ERROR!!!!! :" + e);
        }
        return new Word(userInput,Word.determineWordType(userInput));
    }

    public static void ExecuteOut(){

        currentOperation.remove(1);
        System.out.println(currentOperation.get(0));

    }

}
