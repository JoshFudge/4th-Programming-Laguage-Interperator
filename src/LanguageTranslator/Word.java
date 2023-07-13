package LanguageTranslator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Class to represent each word in the text file that will be executed

public class Word {

    // Regex used to define if a "Word" is a number
    private static String numbersRegex = "\\d+";
    // Regex used to define if a "Word" is a quote or will be used in a quote
    private static String quotesRegex = "[’']+";
    // Regex used to define if a "Word" will be used in  a definition
    private static String definitionRegex = "[:]";
    // Regex used to define if a "Word" is a call to do a specific stack operation
    private static String stackOperationRegex = "dup|swap|pop|[+*-]";
    // Regex used to define if a "Word" is call to run either a in or out operation on the stack
    private static String IORegex = "in|out";

    // Enumeration used to give each word a classification.
    // Classifications include : Numbers, Quotes, Definition, Stack Operations,
    // Input and output, and a string currently being used in a quote
    public enum wordType {NUMBERS,QUOTES,DEFINITION,STACKOPERATION,IO,QUOTESTRING}

    // Each "Word" has a property word and a property type.
    // text -> the text that will be a word (ex.. hello, 5, out...)
    // type -> The classification that is givent to each word (ex... 5 = Number, out = IO ....)
    private String text;
    private wordType type;


    /**
     * Constructor for the Word class
     * @param word the text that will be a word (ex.. hello, 5, out...)
     * @param type The classification that is givent to each word (ex... 5 = Number, out = IO ....)
     */
    public Word(String word, wordType type){
        this.text = word;
        this.type = type;
    }

    /**
     * Method to return the Words type
     * @return WordType
     */
    public wordType getType() {
        return type;
    }

    /**
     * Method to return the words text
     * @return Text
     */
    public String getText() {
        return text;
    }

    /**
     * Method to determine a words WordType
     * @param word Word to be assigned a type
     * @return The Passed in words WordType Classification
     */
    public static wordType determineWordType(String word){
        // Initialize a pattern and matcher used to check if a "Word" is a Number
        Pattern num = Pattern.compile(numbersRegex);
        Matcher numMatcher = num.matcher(word);

        // Initialize a pattern and matcher used to check if a "Word" is a Quote
        Pattern quotes = Pattern.compile(quotesRegex);
        Matcher quoteMatcher = quotes.matcher(word);

        // Initialize a pattern and matcher used to check if a "Word" is a Definition starter
        Pattern def = Pattern.compile(definitionRegex);
        Matcher defMatcher = def.matcher(word);

        // Initialize a pattern and matcher used to check if a "Word" is a Stack Operation
        Pattern stack = Pattern.compile(stackOperationRegex);
        Matcher stackMatcher = stack.matcher(word);

        // Initialize a pattern and matcher used to check if a "Word" is an IO Operation
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

    /**
     * Method used to set the output when a Word is used as output to the console
     * @return the Words text property as a string
     */
    @Override
    public String toString(){
        return this.getText();
    }

}