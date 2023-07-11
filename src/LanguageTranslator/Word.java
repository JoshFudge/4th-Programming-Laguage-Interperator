package LanguageTranslator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Word {

    private static String numbersRegex = "\\d+";
    private static String quotesRegex = "[’']+";
    private static String definitionRegex = "[:]";
    private static String stackOperationRegex = "dup|swap|pop|[+*-]";
    private static String IORegex = "in|out";

    public enum wordType {NUMBERS,QUOTES,DEFINITION,STACKOPERATION,IO,QUOTESTRING}

    private String word;
    private wordType type;



    public Word(String word, wordType type){
        this.word = word;
        this.type = type;
    }

    public wordType getType() {
        return type;
    }

    public String getWord() {
        return word;
    }

    public static wordType determineWordType(String word){
        Pattern num = Pattern.compile(numbersRegex);
        Matcher numMatcher = num.matcher(word);

        Pattern quotes = Pattern.compile(quotesRegex);
        Matcher quoteMatcher = quotes.matcher(word);

        Pattern def = Pattern.compile(definitionRegex);
        Matcher defMatcher = def.matcher(word);

        Pattern stack = Pattern.compile(stackOperationRegex);
        Matcher stackMatcher = stack.matcher(word);

        Pattern IO = Pattern.compile(IORegex);
        Matcher IOMatcher = IO.matcher(word);


        if (numMatcher.find()){
            return wordType.NUMBERS;
        } else if (quoteMatcher.find()) {
            return wordType.QUOTES;
        }else if (defMatcher.find()){
            return wordType.DEFINITION;
        } else if (stackMatcher.find()) {
            return  wordType.STACKOPERATION;
        } else if (IOMatcher.find()) {
            return wordType.IO;
        }

        //TODO Throw error here, word fails validation
        return null;
    }

    @Override
    public String toString(){
        return this.getWord();
    }

}
