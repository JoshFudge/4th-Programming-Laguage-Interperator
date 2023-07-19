package LanguageTranslator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Translator {
    //TODO FIGURE OUT HOW TO regex and add a string if on of them is a number outside of quotes


    // A list of every word from the text file that will be used to calculations and operations with
    static ArrayList<Word> currentOperation = new ArrayList<>();
    // A Boolean flag used to signal when a quote string is currently being created
    static Boolean quoteFlag = false;
    // Boolean flag used to signal when a definition is currently being created
    static Boolean definitionFlag = false;

    // Map used to contain all created definitions and their value
    static Map<String, Word> currentDefinitions = new HashMap<>();

    /**
     * Method that contains all logic and operation calls
     * @param originalStack the original list of words created from the text file
     * @throws RuntimeException if there's any syntax issues in the programs, an error will be thrown
     */
    public static void translatePrograms(ArrayList<Word> originalStack) throws RuntimeException {
        // A string that will hold the quote text while it is being created
         String quoteString = "";
         // A string that will hold a definition name while it is being created
         String currentDefinition = "";

         // Add every word in the text file to the operations list
        currentOperation.addAll(originalStack);

        // Loop through every word in the text file.......
        for (Word CurrentWord: originalStack) {
            // If the word is a number and there is not a quote currently being constructed...
            if (CurrentWord.getType() == Word.wordType.NUMBERS && !quoteFlag){
            // Do nothing / continue onto the next word
            }

            // If the word is a Stack Operation key, check to see which one it is
            else if (CurrentWord.getType() == Word.wordType.STACKOPERATION) {
                // If the word is a "+"...
                if (Objects.equals(CurrentWord.getText(), "+")) {
                    // Execute the plus method
                    ExecutePlus(CurrentWord);
                    // If the word is a "-"...
                } else if (Objects.equals(CurrentWord.getText(),"-")) {
                    // Execute the Negation method
                   ExecuteNeg(CurrentWord);
                    // If the word is a "*"...
                } else if (Objects.equals(CurrentWord.getText(), "*")) {
                    // call the multiply/String Regex method
                     ExecuteMultiply(CurrentWord);
                     // If the word is the "pop" keyword...
                } else if (Objects.equals(CurrentWord.getText(), "pop")) {
                    // call the pop method
                    ExecutePop(CurrentWord);
                    // If the word is the "swap" keyword...
                }else if (Objects.equals(CurrentWord.getText(), "swap")) {
                    // call the swap method
                    ExecuteSwap(CurrentWord);
                    // If the word is the "dup" keyword...
                }else if (Objects.equals(CurrentWord.getText(), "dup")) {
                    // call the dup method
                    ExecuteDup(CurrentWord);
                }
                // check and see if the word is a " ' " or if there's currently a quote string being created
            } else if (CurrentWord.getType() == Word.wordType.QUOTES || quoteFlag) {
                // If so try the following...
                try {
                    // If it's the first " ' "...
                    if (CurrentWord.getType() == Word.wordType.QUOTES && quoteString.equals("")) {
                        // Remove the " ' ",
                        currentOperation.remove(CurrentWord);
                        // Turn the flag on to signal that a quote is currently being constructed
                        quoteFlag = true;
                    }
                    // If the word isn't a " ' ",
                    else if (!Objects.equals(CurrentWord.getText(), "'")){
                        // Add the word to the quoteString
                        quoteString +=  CurrentWord.getText() + " ";
                        // Remove the word from the stack
                        currentOperation.remove(CurrentWord);
                    }
                    // If it's the second " ' ",
                    else if (CurrentWord.getType() == Word.wordType.QUOTES && !quoteString.equals("")) {
                        // Turn the flag off
                     quoteFlag = false;
                     // Remove the " ' " from the stack
                     currentOperation.remove(CurrentWord);
                     // Add the quote output string as a new word to the stack
                     currentOperation.add(0, new Word(quoteString, Word.wordType.LetterString));
                     // Reset the string placeholder as the quote has ended
                     quoteString = "";
                    }
                    // If anything else occurs... Throw a new error as the program syntax is incorrect
                    else {
                        throw new RuntimeException("An error occurred while translating your code! Please ensure your are using the quote operators correctly");
                    }
                    // If any errors are caught, display an error message
                }catch (RuntimeException e){
                    throw new java.lang.RuntimeException(e);
                }

                // If the word is a " : " or a definition is currently being defined,
            }  else if (CurrentWord.getType() == Word.wordType.DEFINITION || definitionFlag){
                // Try the following...
                try {
                    // If its the first ":"..
                    if (CurrentWord.getType() == Word.wordType.DEFINITION && currentDefinition.equals("")){
                        // Turn the flag on signaling that definition creation is currently underway
                        definitionFlag = true;
                        // Remove the ":" from the stack
                        currentOperation.remove(CurrentWord);
                    }
                    // If the word is a letter string and the presumed definition key..
                    else if (CurrentWord.getType() == Word.wordType.LetterString){
                        // get the definition identifier
                        currentDefinition = CurrentWord.getText();
                        // remove the word from the stack
                        currentOperation.remove(CurrentWord);

                        // If it's the final ":"
                    }  else if (CurrentWord.getType() == Word.wordType.DEFINITION && !currentDefinition.equals("")) {
                        // Create a new word with the top item in the stack to be used as the definition value
                        Word definitionValue = new Word(currentOperation.get(0).toString(),currentOperation.get(0).getType());

                        // Store the definition in the map with the definition string being the key, and the value being the top item on the stack
                        currentDefinitions.put(currentDefinition,definitionValue);
                        // Set the flag to false as the definition has been created
                        definitionFlag = false;
                        // Reset the Definition placeholder string
                        currentDefinition = "";
                        // Remove the ":" from the stack
                        currentOperation.remove(CurrentWord);
                        // Remove the definition value from the stack
                        currentOperation.remove(0);
                    // If the word is anything else,
                    } else {
                        // Throw an error as there is an issue with the program syntax
                        throw new RuntimeException("There is a problem with your Definition Code! Please ensure you have two ':' surrounding your definition");
                    }
                    // If any exceptions are caught..
                } catch (RuntimeException e){
                    // Display an error message
                    throw new java.lang.RuntimeException(e);
                }
            }
            // If the word has the IO Word-type..
            else if (CurrentWord.getType() == Word.wordType.IO){
                // If the word is the "in" keyword...
                if (Objects.equals(CurrentWord.getText(), "in")){
                    // call the in method
                    Word input = ExecuteIn();
                    // Set the user input to the index of the in command
                    currentOperation.set(currentOperation.indexOf(CurrentWord), input);
                    // If the word is the "out" keyword...
                } else if (Objects.equals(CurrentWord.getText(), "out")) {
                    // call the out method
                    ExecuteOut(CurrentWord);
                }
                // If the word is a definition stored in the Definitions Map..
            } else if (currentDefinitions.containsKey(CurrentWord.getText())) {
                // Add the definition value to the stack
                currentOperation.add(0, currentDefinitions.get(CurrentWord.toString()));
                // Remove the definition key
                currentOperation.remove(CurrentWord);

                // If the word is a character string and hasn't been processed by a prior check...
            } else if (CurrentWord.getType() == Word.wordType.LetterString) {
                // Throw error as it is not a valid word or is it being used in a definition or quote
                throw new RuntimeException("Syntax Error! Word " + CurrentWord.getText() + " is not a valid Word, Operation, Or Definition");
            }
        }
    }

    /**
     * Method used to complete the operations that the "+" keyword will call
     * @param currentWord the current word being processed "+"
     */
    public static void ExecutePlus(Word currentWord){
        // Try the following...
        try{
            // Get the top words on the stack
            Word firstWord = currentOperation.get(currentOperation.indexOf(currentWord) - 1);
            Word secondWord = currentOperation.get(currentOperation.indexOf(currentWord) - 2);

            // If both words are numbers...
            if (firstWord.getType() == Word.wordType.NUMBERS && secondWord.getType() == Word.wordType.NUMBERS){
                // Add both words together and store it as an int
                int total = Integer.parseInt(firstWord.getText()) + Integer.parseInt(secondWord.getText());
                // Remove the "+" from the stack
                currentOperation.remove(currentWord);
                // Remove the 2 numbers added together from the stack
                currentOperation.remove(firstWord);
                currentOperation.remove(secondWord);

                // Convert the answer to a string
                String answerAsString = Integer.toString(total);
                // Add the answer to the stack as a new word
                currentOperation.add(0,new Word(answerAsString,Word.determineWordType(answerAsString)));

                // If either word is a string...
            } else if (secondWord.getType() == Word.wordType.LetterString || firstWord.getType() == Word.wordType.LetterString) {
                // Initialize a string to store the concatenated words
                String output = "";
                // Concatenate the 2 two words
                output += firstWord.getText() + " " + secondWord.getText();
                // Remove the "+" from the stack
                currentOperation.remove(currentWord);
                // Add the concatenated word to the stack
                currentOperation.add(currentOperation.indexOf(firstWord),new Word(output,Word.determineWordType(output)));
                // Remove both words that were concatenated from the stack
                currentOperation.remove(firstWord);
                currentOperation.remove(secondWord);
            // If a words does not pass any of the checks, throw an error as there's an issue wth the program syntax
            }else {
                throw new RuntimeException("An error occurred while translating your code! Please ensure your are using the '+' Operator Correctly");
            }

            // Catch any error and display an error message
        } catch (IndexOutOfBoundsException IOE){
            throw new RuntimeException("There is a Syntax Error in your code! Please ensure it is coded correctly and try again!");
        }catch (RuntimeException e) {
            throw new java.lang.RuntimeException(e);
        }

    }

    /**
     * Method to conduct all the operation that the '-' command will do to the stack
     * @param currentWord the current word being processed "-"
     */
    public static void ExecuteNeg(Word currentWord)  {
        // Try the following:
        try{
            // Get the word on the top of the stack
            Word firstWord = currentOperation.get(currentOperation.indexOf(currentWord) - 1);
            // If the word is a number
            if (firstWord.getType() == Word.wordType.NUMBERS)  {
                // Multiply it by -1 to negate it
                int neg = Integer.parseInt(firstWord.getText()) * (-1);
                // Remove the "-" operation
                currentOperation.remove(currentWord);
                // Convert the negated number to a string
                String negString = Integer.toString(neg);
                // Set the index of the operated number to a new word of the negated number
                currentOperation.set(currentOperation.indexOf(firstWord), new Word(negString, Word.wordType.NUMBERS));

                // If the word on the top of the stack is a valid word that consists of characters..
            } else if (firstWord.getType() == Word.wordType.LetterString){
                // initialize a string as the reversed word
                String reverse = new StringBuilder(firstWord.getText()).reverse().toString();
                // Remove any whitespace
                reverse = reverse.replaceFirst("\\s+","");
                // Remove the "-" from the list
                currentOperation.remove(currentWord);
                // put the new word on the stack
                currentOperation.set(0,new Word(reverse, Word.wordType.LetterString));

                // else, throw a new error as there is an issue with the program syntax
            }else {
                throw new RuntimeException("An error occurred while translating your code! Please ensure your are using the '-' Operator Correctly");
            }
        // If any errors are caught, display a proper error message
        }catch (RuntimeException se){
            throw new java.lang.RuntimeException(se);
        }
    }

    /**
     * Method to conduct all the operation that the '*' command will do to the stack
     * @param currentWord the current word being processed "*"
     */
    public static void ExecuteMultiply(Word currentWord){
        // Try the following...
        try {
            // get the two words on the stack that will be operated on
            Word firstWord = currentOperation.get(currentOperation.indexOf(currentWord) - 1);
            Word secondWord = currentOperation.get(currentOperation.indexOf(currentWord) - 2);

            // If both words are numbers
            if (firstWord.getType() == Word.wordType.NUMBERS && secondWord.getType() == Word.wordType.NUMBERS) {
                // Add them together and store the total
                int total = Integer.parseInt(firstWord.getText()) * Integer.parseInt(secondWord.getText());
                // Remove the "+" and the two numbers from the stack
                currentOperation.remove(currentWord);
                currentOperation.remove(firstWord);
                currentOperation.remove(secondWord);

                // Convert the answer to a string
                String answer = Integer.toString(total);
                // Add the answer to the stack as a new word
                currentOperation.add(0,new Word(answer,Word.determineWordType(answer)));

                // If either word is a string...
            } else if ((secondWord.getType() == Word.wordType.LetterString|| firstWord.getType() == Word.wordType.LetterString
                    || // OR
                    // if one word is a string and the other is a number...
                    ((secondWord.getType() == Word.wordType.LetterString|| firstWord.getType() == Word.wordType.LetterString) && (secondWord.getType() == Word.wordType.NUMBERS|| firstWord.getType() == Word.wordType.NUMBERS))))
            {

                // get the two words that will be operated on
                String firstWordString = firstWord.getText();
                String secondWordString = secondWord.getText().replaceAll("\\s+","");

                // Make the second word a regex pattern
                Pattern letterToFind = Pattern.compile(secondWordString);
                // Create a Matcher for the first string to search for the second string
                Matcher stringToSearch = letterToFind.matcher(firstWordString);

                // If the second string is found...
                if (stringToSearch.find()){
                    // Get the index of the first instance of the second string
                    int firstInstance = stringToSearch.start();
                    // Remove the "*" and the two operated words
                    currentOperation.remove(currentWord);
                    currentOperation.remove(firstWord);
                    currentOperation.remove(secondWord);

                    // Store a string that contains the substring of the first word that starts at the index of the second string
                    String result = firstWordString.substring(firstInstance);

                    // Add the new string as a new word to the stack
                    currentOperation.add(0,new Word(result,Word.determineWordType(result)));
                    // If the second string is not found, throw and error saying that there was no substring created
                } else {
                    throw new RuntimeException("Error! Could not find " + secondWordString + " in " + firstWordString);
                }
            }
            // If a word does not pass any of the checks, throw an error as there's an error with the program syntax
            else {
                throw new RuntimeException("An error occurred while translating your code! Please ensure your are using the '*' Operator correctly");
            }
            // Catch any error any display an error message
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
            currentOperation.set(0,firstWord);
            // Set the second word of the stack as the word that was initially first
            currentOperation.set(1,secondWord);
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
            // caused by no item on the stack prior to the dup command...
        }catch (IndexOutOfBoundsException e ){
            // Throw and error and display an error message
            throw new RuntimeException("An error occurred while translating your code! Please ensure there is an Word available to duplicate on the top of the stack.");
        }
    }
}
