package LanguageTranslator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Translator {
    static ArrayList<Word> currentOperation = new ArrayList<>();
    static Boolean quoteFlag = false;

    static Map<String, Word> currentDefinitions = new HashMap<>();

    //TODO quotes
    public static void translatePrograms(ArrayList<Word> originalStack){
         String quoteString = "";

        currentOperation.addAll(originalStack);
        for (Word w: originalStack) {


            if (w.getType() == Word.wordType.STACKOPERATION) {
                if (Objects.equals(w.getText(), "+")) {
                    String answer = ExecutePlus(w);
                    currentOperation.add(0,new Word(answer,Word.determineWordType(answer)));
                } else if (Objects.equals(w.getText(),"-")) {
                    String answer = ExecuteNeg(w);
                    currentOperation.add(0,new Word(answer,Word.determineWordType(answer)));
                } else if (Objects.equals(w.getText(), "*")) {
                    String answer = ExecuteMultiply(w);
                    currentOperation.add(0,new Word(answer,Word.determineWordType(answer)));
                } else if (Objects.equals(w.getText(), "pop")) {
                    ExecutePop(w);
                    currentOperation.remove(w);
                }else if (Objects.equals(w.getText(), "swap")) {
                    ExecuteSwap(w);
                    currentOperation.remove(w);
                }else if (Objects.equals(w.getText(), "dup")) {
                    ExecuteDup(w);
                    currentOperation.remove(w);
                }
            } else if (w.getType() == Word.wordType.QUOTES || quoteFlag)

             {
                 currentOperation.remove(w);

                 quoteFlag = true;

                if (!Objects.equals(w.getText(), "'")){
                    quoteString += " " + w.getText() ;
                    currentOperation.remove(w);
                }
                if (w.getType() == Word.wordType.QUOTES && quoteString != ""){
                    quoteFlag = false;
                    currentOperation.remove(w);
                    currentOperation.add(0,new Word(quoteString, Word.wordType.QUOTESTRING));
                    quoteString = "";
                }


            }  else if (w.getType() == Word.wordType.DEFINITION){

            }
            else if (w.getType() == Word.wordType.IO){
                if (Objects.equals(w.getText(), "in")){
                    Word input = Translator.ExecuteIn();
                    currentOperation.set(currentOperation.indexOf(w), input);
                } else if (Objects.equals(w.getText(), "out")) {

                    ExecuteOut(w);
                    currentOperation.remove(0);
                    if (!currentOperation.isEmpty() && Objects.equals(currentOperation.get(0).getText(), "out")){
                        currentOperation.remove(0);
                    }
                }
            }

        }

    }



    public static String ExecutePlus(Word currentWord){
        Word firstWord = currentOperation.get(currentOperation.indexOf(currentWord) - 1);
        Word secondWord = currentOperation.get(currentOperation.indexOf(currentWord) - 2);

        if (firstWord.getType() == Word.wordType.NUMBERS && secondWord.getType() == Word.wordType.NUMBERS){
            int total = Integer.parseInt(firstWord.getText()) + Integer.parseInt(secondWord.getText());
            currentOperation.remove(currentWord);
            currentOperation.remove(firstWord);
            currentOperation.remove(secondWord);
            return Integer.toString(total);

        } else if (secondWord.getType() == Word.wordType.QUOTESTRING || firstWord.getType() == Word.wordType.QUOTESTRING) {
            String output = "";
            output += firstWord.getText() + " " + secondWord.getText();
            currentOperation.remove(currentWord);
            currentOperation.remove(firstWord);
            currentOperation.remove(secondWord);
            return output;
        }


        return null;
    }

    public static String ExecuteNeg(Word currentWord){
        Word firstWord = currentOperation.get(currentOperation.indexOf(currentWord) - 1);

        if (firstWord.getType() == Word.wordType.NUMBERS)  {
            int neg = Integer.parseInt(firstWord.getText()) * (-1);

            currentOperation.remove(currentWord);
            currentOperation.remove(0);

            return Integer.toString(neg);


        } else if (firstWord.getType() == Word.wordType.QUOTESTRING ){
            String reverse = new StringBuilder(firstWord.getText()).reverse().toString();
            currentOperation.remove(currentWord);
            return reverse;
        }
        return null;
    }

    public static String ExecuteMultiply(Word currentWord){
        Word firstWord = currentOperation.get(currentOperation.indexOf(currentWord) - 1);
        Word secondWord = currentOperation.get(currentOperation.indexOf(currentWord) - 2);

        if (firstWord.getType() == Word.wordType.NUMBERS && secondWord.getType() == Word.wordType.NUMBERS) {
            int total = Integer.parseInt(firstWord.getText()) * Integer.parseInt(secondWord.getText());
            currentOperation.remove(currentWord);
            currentOperation.remove(firstWord);
            currentOperation.remove(secondWord);
            return Integer.toString(total);
        }
        return null;
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

    public static void ExecuteOut(Word currentWord){

        currentOperation.remove(currentWord);
        System.out.println(currentOperation.get(0));

    }

    public static void ExecutePop(Word currentWord){
        Word firstWord = currentOperation.get(currentOperation.indexOf(currentWord) - 1);
        currentOperation.remove(firstWord);
    }

    public static void ExecuteSwap(Word currentWord) {
        Word firstWord = currentOperation.get(currentOperation.indexOf(currentWord) - 1);
        Word secondWord = currentOperation.get(currentOperation.indexOf(currentWord) - 2);

        currentOperation.set(0,secondWord);
        currentOperation.set(1,firstWord);
    }

    public static void ExecuteDup(Word currentWord) {
        Word firstWord = currentOperation.get(currentOperation.indexOf(currentWord) - 1);
        currentOperation.add(0,firstWord);
    }

}
