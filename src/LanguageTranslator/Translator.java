package LanguageTranslator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Translator {
    static ArrayList<Word> currentOperation = new ArrayList<>();

    public static void translatePrograms(ArrayList<Word> originalStack){

        // TODO fix Math so that it goes in th proper order  (2 3 4 * +) goes 4 * 3 -> 12 + 2

        currentOperation.addAll(originalStack);
        for (Word w: originalStack) {

            //TODO instead of setting the answer to math to index 0, add it at 0 so you can remove the top 2 thing after u do the calculation in the add/- method

            if (w.getType() == Word.wordType.STACKOPERATION) {
                if (Objects.equals(w.getWord(), "+")) {
                    String answer = ExecutePlus(w);
                    currentOperation.set(0,new Word(answer,Word.determineWordType(answer)));
                } else if (Objects.equals(w.getWord(),"-")) {
                    String answer = ExecuteNeg();
                    currentOperation.set(0,new Word(answer,Word.determineWordType(answer)));
                } else if (Objects.equals(w.getWord(), "*")) {
                    String answer = ExecuteMultiply();
                    currentOperation.set(0,new Word(answer,Word.determineWordType(answer)));
                } else if (Objects.equals(w.getWord(), "pop")) {
                    ExecutePop();
                    currentOperation.remove(w);
                }else if (Objects.equals(w.getWord(), "swap")) {
                    ExecuteSwap();
                    currentOperation.remove(w);
                }else if (Objects.equals(w.getWord(), "dup")) {
                    ExecuteDup();
                    currentOperation.remove(w);
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



    public static String ExecutePlus(Word currentWord){
        if (currentOperation.get(0).getType() == Word.wordType.NUMBERS && currentOperation.get(1).getType() == Word.wordType.NUMBERS){
            int total = Integer.parseInt(currentOperation.get(currentOperation.indexOf(currentWord) - 1).getWord()) + Integer.parseInt(currentOperation.get(currentOperation.indexOf(currentWord) - 2).getWord());
            currentOperation.remove(currentWord);
            return Integer.toString(total);
        } else if (currentOperation.get(1).getType() == Word.wordType.TEXTWORD && currentOperation.get(0).getType() == Word.wordType.TEXTWORD) {
            String output = "";
            output += currentOperation.get(1).getWord() + " " + currentOperation.get(0).getWord();
            currentOperation.remove(1);
            return output;
        }


        return null;
    }

    public static String ExecuteNeg(){
        if (currentOperation.get(0).getType() == Word.wordType.NUMBERS && currentOperation.get(1).getType() != Word.wordType.NUMBERS)  {
            int neg = Integer.parseInt(currentOperation.get(0).getWord()) * (-1);
            currentOperation.remove(1);
            return Integer.toString(neg);
        } else if (currentOperation.get(0).getType() == Word.wordType.NUMBERS && currentOperation.get(1).getType() == Word.wordType.NUMBERS) {
            int total = Integer.parseInt(currentOperation.get(1).getWord()) - Integer.parseInt(currentOperation.get(0).getWord());
            currentOperation.remove(1);
            return Integer.toString(total);
        }else if (currentOperation.get(0).getType() == Word.wordType.TEXTWORD){
            //TODO reverse a string
            return null;
        }
        return null;
    }

    public static String ExecuteMultiply(){
        if (currentOperation.get(0).getType() == Word.wordType.NUMBERS && currentOperation.get(1).getType() == Word.wordType.NUMBERS) {
            int total = Integer.parseInt(currentOperation.get(1).getWord()) * Integer.parseInt(currentOperation.get(0).getWord());
            currentOperation.remove(1);
            return Integer.toString(total);
        }
        return null;
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

    public static void ExecutePop(){
        currentOperation.remove(0);
    }

    public static void ExecuteSwap() {
        Word top = currentOperation.get(0);
        Word second = currentOperation.get(1);
        currentOperation.set(0,second);
        currentOperation.set(1,top);
    }

    public static void ExecuteDup() {
        Word top = currentOperation.get(0);
        currentOperation.add(0,top);
    }

}
