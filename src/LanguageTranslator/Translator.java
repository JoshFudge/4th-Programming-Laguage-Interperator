package LanguageTranslator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class Translator {
    static ArrayList<Word> currentOperation = new ArrayList<>();
    static Boolean quoteFlag = false;
    static Boolean definitionFlag = false;

    static Map<String, Word> currentDefinitions = new HashMap<>();


    public static void translatePrograms(ArrayList<Word> originalStack) throws RuntimeException {
         String quoteString = "";
         String currentDefinition = "";

        currentOperation.addAll(originalStack);
        for (Word CurrentWord: originalStack) {
            if (CurrentWord.getType() == Word.wordType.NUMBERS){}

            else if (CurrentWord.getType() == Word.wordType.STACKOPERATION) {
                if (Objects.equals(CurrentWord.getText(), "+")) {
                    String answer = ExecutePlus(CurrentWord);
                    currentOperation.add(0,new Word(answer,Word.determineWordType(answer)));
                } else if (Objects.equals(CurrentWord.getText(),"-")) {
                    String answer = ExecuteNeg(CurrentWord);
                    currentOperation.add(0,new Word(answer,Word.determineWordType(answer)));
                } else if (Objects.equals(CurrentWord.getText(), "*")) {
                    String answer = ExecuteMultiply(CurrentWord);
                    currentOperation.add(0,new Word(answer,Word.determineWordType(answer)));
                } else if (Objects.equals(CurrentWord.getText(), "pop")) {
                    ExecutePop(CurrentWord);
                }else if (Objects.equals(CurrentWord.getText(), "swap")) {
                    ExecuteSwap(CurrentWord);
                }else if (Objects.equals(CurrentWord.getText(), "dup")) {
                    ExecuteDup(CurrentWord);
                }
            } else if (CurrentWord.getType() == Word.wordType.QUOTES || quoteFlag) {
                try {
                    if (CurrentWord.getType() == Word.wordType.QUOTES && quoteString.equals("")) {
                        currentOperation.remove(CurrentWord);
                        quoteFlag = true;
                    }
                    else if (!Objects.equals(CurrentWord.getText(), "'")){
                        quoteString +=  CurrentWord.getText() + " ";
                        currentOperation.remove(CurrentWord);
                    }
                    else if (CurrentWord.getType() == Word.wordType.QUOTES && !quoteString.equals("")) {
                     quoteFlag = false;
                     currentOperation.remove(CurrentWord);
                     currentOperation.add(0, new Word(quoteString, Word.wordType.QUOTESTRING));
                     quoteString = "";
                    } else {
                        throw new RuntimeException("An error occurred while translating your code! Please ensure your are using the quote operators correctly");
                    }
                }catch (RuntimeException e){
                    throw new java.lang.RuntimeException(e);
                }


            }  else if (CurrentWord.getType() == Word.wordType.DEFINITION || definitionFlag){
                try {
                    if (CurrentWord.getType() == Word.wordType.DEFINITION && currentDefinition.equals("")){

                        definitionFlag = true;
                        currentOperation.remove(CurrentWord);
                    }

                    else if (CurrentWord.getType() != Word.wordType.DEFINITION && CurrentWord.getType() == Word.wordType.PotentialDefinition){
                        // get the definition identifier
                        currentDefinition += CurrentWord.getText();
                        currentOperation.remove(CurrentWord);

                    }  else if (CurrentWord.getType() == Word.wordType.DEFINITION && !currentDefinition.equals("")) {

                        Word definitionValue = new Word(currentOperation.get(0).toString(),currentOperation.get(0).getType());

                        currentDefinitions.put(currentDefinition,definitionValue);

                        definitionFlag = false;
                        currentDefinition = "";
                        currentOperation.remove(CurrentWord);
                        currentOperation.remove(0);

                    } else {
                        throw new RuntimeException("There is a problem with your Definition Code! Please ensure you have two ':' surrounding your definition");
                    }
                } catch (RuntimeException e){
                    throw new java.lang.RuntimeException(e);
                }


            }
            else if (CurrentWord.getType() == Word.wordType.IO){
                if (Objects.equals(CurrentWord.getText(), "in")){
                    Word input = Translator.ExecuteIn();
                    currentOperation.set(currentOperation.indexOf(CurrentWord), input);
                } else if (Objects.equals(CurrentWord.getText(), "out")) {
                    ExecuteOut(CurrentWord);
                    if (!currentOperation.isEmpty() && Objects.equals(currentOperation.get(0).getText(), "out")){
                        currentOperation.remove(0);
                    }
                }
            } else if (currentDefinitions.containsKey(CurrentWord.getText())) {

                currentOperation.add(0, currentDefinitions.get(CurrentWord.toString()));
                currentOperation.remove(CurrentWord);
            } else if (CurrentWord.getType() == Word.wordType.PotentialDefinition) {
                throw new RuntimeException("Syntax Error! Word " + CurrentWord.getText() + " is not a valid Word, Operation, Or Definition");
            }

        }

    }



    public static String ExecutePlus(Word currentWord){
        try{
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
            }else {
                throw new RuntimeException("An error occurred while translating your code! Please ensure your are using the '+' Operator Correctly");
            }


        } catch (IndexOutOfBoundsException IOE){
            throw new RuntimeException("There is a Syntax Error in your code! Please ensure it is coded correctly and try again!");
        }catch (RuntimeException e) {
            throw new java.lang.RuntimeException(e);
        }

    }

    public static String ExecuteNeg(Word currentWord)  {
        try{
            Word firstWord = currentOperation.get(currentOperation.indexOf(currentWord) - 1);

            if (firstWord.getType() == Word.wordType.NUMBERS)  {
                int neg = Integer.parseInt(firstWord.getText()) * (-1);

                currentOperation.remove(currentWord);
                currentOperation.remove(0);

                return Integer.toString(neg);


            } else if (firstWord.getType() == Word.wordType.QUOTESTRING || firstWord.getType() == Word.wordType.PotentialDefinition ){
                String reverse = new StringBuilder(firstWord.getText()).reverse().toString();
                currentOperation.remove(currentWord);
                return reverse;
            }else {
                throw new RuntimeException("An error occurred while translating your code! Please ensure your are using the '-' Operator Correctly");
            }

        }catch (RuntimeException se){
            throw new java.lang.RuntimeException(se);
        }
    }

    public static String ExecuteMultiply(Word currentWord){
        try {
            Word firstWord = currentOperation.get(currentOperation.indexOf(currentWord) - 1);
            Word secondWord = currentOperation.get(currentOperation.indexOf(currentWord) - 2);

            if (firstWord.getType() == Word.wordType.NUMBERS && secondWord.getType() == Word.wordType.NUMBERS) {
                int total = Integer.parseInt(firstWord.getText()) * Integer.parseInt(secondWord.getText());
                currentOperation.remove(currentWord);
                currentOperation.remove(firstWord);
                currentOperation.remove(secondWord);
                return Integer.toString(total);
            } else if ((secondWord.getType() == Word.wordType.QUOTESTRING || firstWord.getType() == Word.wordType.QUOTESTRING)) {
                String firstWordString = firstWord.getText().replaceAll("\\s+","");
                String secondWordString = secondWord.getText().replaceAll("\\s+","");

                String[] result = firstWord.getText().split(secondWord.getText().replaceAll("\\s+",""));
//                System.out.println(Arrays.toString(result));
                currentOperation.remove(currentWord);
                currentOperation.remove(firstWord);
                currentOperation.remove(secondWord);
//                if (result.length == 2){
//                    return secondWord.getText().replaceAll("\\s+","") + result[result.length - 1];
//                }else {
//                System.out.println(secondWordString);
//                System.out.println(firstWordString.replaceFirst(secondWordString,"") + "HERE!!!");
//                System.out.println(firstWordString + "    ----- FIRST");
//                System.out.println(secondWordString + "      ------- SECOND");
//                String regex = ".*" + secondWordString + "(.*)";
//                System.out.println(regex + "     REGEX ------");
                //TODO FIGURE OUT REGEX STUFF
                    return secondWordString + firstWord.getText().replaceAll(".*" + secondWordString + "(.*)","$1");
//                }

            }else {
                throw new RuntimeException("An error occurred while translating your code! Please ensure your are using the '*' Operator correctly");
            }
        } catch (IndexOutOfBoundsException IOE){
            throw new RuntimeException("There is a Syntax Error in your code! Please ensure it is coded correctly and try again!");
        }catch (RuntimeException e ){
            throw new java.lang.RuntimeException(e);
        }
    }


    /**
     * Method when called will conduct the specific operations on the stack
     * to replicate the "In" keyword command
     * @return User Input as a Word Object
     */
    public static Word ExecuteIn(){
        // Initialize a string that will eventually contain the user input
        String userInput = "";
        try {
            // Get input from the user
            System.out.println("Enter a value: ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            userInput = br.readLine();

            // Catch any exceptions
        }catch (IOException e){
            System.out.println("Error Occurred! Please ensure you entered valid data" + e);
        }
        // Return the user input created into a word object
        return new Word(userInput,Word.determineWordType(userInput));
    }

    /**
     * Method when called will conduct the specific operations on the stack
     * to replicate the "Out" keyword command
     * @param currentWord The current word on the stack being executed 'out'
     */
    public static void ExecuteOut(Word currentWord){
        try {
            // Print out the top Word of the stack
            System.out.println(currentOperation.get(0));
            // Remove the top item of the stack as it has been output
            currentOperation.remove(0);
            // remove the 'out' word as it has been executed
            currentOperation.remove(currentWord);

            // If a IndexOutOfBounds Exception is caught
            // caused by no words available to display
        }catch (IndexOutOfBoundsException e){
            // Throw and error and display an error message
            throw new RuntimeException("An error occurred while translating your code! Please ensure there is something to output on the stack.");
        }
    }

    /**
     * Method when called will conduct the specific operations on the stack
     * to replicate the "Pop" keyword command
     * @param currentWord The current word on the stack being executed 'pop'
     */
    public static void ExecutePop(Word currentWord){
        try{
            // Attempt to get the Word that is on top of the stack
            Word firstWord = currentOperation.get(currentOperation.indexOf(currentWord) - 1);
            // Remove that word from the stack
            currentOperation.remove(firstWord);
            // Remove the 'pop' command as it has been executed
            currentOperation.remove(currentWord);

            // If a IndexOutOfBounds Exception is caught
            // caused by no words available to pop
        }catch (IndexOutOfBoundsException e){
            // Throw and error and display an error message
            throw new RuntimeException("An error occurred while translating your code! Please ensure your are using the 'pop' command correctly");
        }
    }

    /**
     * Method when called will conduct the specific operations on the stack
     * to replicate the "Swap" keyword command
     * @param currentWord The current word on the stack being executed 'swap'
     */
    public static void ExecuteSwap(Word currentWord) {
        try {
            // Attempt to get the two words from the stack that will be swapped
            Word firstWord = currentOperation.get(currentOperation.indexOf(currentWord) - 1);
            Word secondWord = currentOperation.get(currentOperation.indexOf(currentWord) - 2);

            // Set the top of the stack as the word that was initially second
            currentOperation.set(0,secondWord);
            // Set the second word of the stack as the word that was initially first
            currentOperation.set(1,firstWord);
            // Remove the 'swap' word from the stack
            currentOperation.remove(currentWord);

            // If a IndexOutOfBounds Exception is caught
            // caused by not enough items on the stack to swap...
        } catch (IndexOutOfBoundsException e){
            // Throw and error and display an error message
            throw new RuntimeException("An error occurred while translating your code! Please ensure that there are words available on the stack to swap.");
        }
    }

    /**
     * Method when called will conduct the specific operations on the stack
     * to replicate the "Dup" keyword command
     * @param currentWord The current word being executed "dup"
     */
    public static void ExecuteDup(Word currentWord) {
        try {
            // Get the top word on the stack
            Word firstWord = currentOperation.get(currentOperation.indexOf(currentWord) - 1);
            // Add another version of the first item on the stack to the stack. Duplicating it
            currentOperation.add(0,firstWord);
            // Remove the 'Dup' command from the stack
            currentOperation.remove(currentWord);

            // If a IndexOutOfBounds Exception is caught
            // caused by no item on the stack prior to the dup command..
        }catch (IndexOutOfBoundsException e ){
            // Throw and error and display an error message
            throw new RuntimeException("An error occurred while translating your code! Please ensure there is an Word available to duplicate on the top of the stack.");
        }
    }
}
