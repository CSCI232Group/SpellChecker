/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Batman
 */
public class SpellChecker {  
    
    final static char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray(); //defines an alphabet
 
    //addCHar
    //adds every letter of the alphabet to the string starting before the word 
    //and goes to one letter past the word.
    private static void addChar(String input,ArrayList<String> returnList, SET<String> dictionary){  
        int lengthOfString = input.length() -1;
        for(char a : alphabet){
            String inFront = a + input;
            String inBack = input + a;
            if(dictionary.contains(inFront)){
                returnList.add(inFront);
            }
            if(dictionary.contains(inBack)){
                returnList.add(inFront);
            }
            String test;
            for(int i =0; i < lengthOfString; i++){    
                test = input.substring(0,i) + a + input.substring(i); //remove the ith letter of the string
                if(dictionary.contains(test)){                      //is this letter in the dictionary?
                    addtoList(returnList, test, input);                    //if yes add it to the array list                      
                }
            }
        }
    } 
    private static void addtoList(ArrayList<String> returnList, String test, String input){
        if(returnList.contains(test)== false){
            if(input.length() <= test.length() + 3 )
                returnList.add(test);
        }  
    }
    private  static void removeChars(String input,ArrayList<String> returnList, SET<String> dictionary){
        int lengthOfString = input.length();
        String test;
        for(int i =0; i < lengthOfString-1; i++){
            test = input.substring(0, lengthOfString - i); //remove one letter at a time from the back each time through the loop remove up to 3
            if(dictionary.contains(test)){                          //is this letter in the dictionary?
                addtoList(returnList, test, input);                        //if yes add it to the array list
            }
        }
        for(int i =0; i<lengthOfString-1; i++){
            test = input.substring(i,lengthOfString);               //remove letters from the begining of the word untill the word is 2 letters big
            if(dictionary.contains(test)){                          //is this letter in the dictionary?
                addtoList(returnList, test, input);                        //if yes add it to the array list
            }
            
        }    
    }
    
    private static void removeChar(String input,ArrayList<String> returnList, SET<String> dictionary){
        int lengthOfString = input.length() -1;
        String test = input.substring(1);                       //remove first letter of string
        if(dictionary.contains(test)){                          //is this letter in the dictionary?
            addtoList(returnList, test, input);                        //if yes add it to the array list
        }
        for(int i =0; i < lengthOfString; i++){    
            test = input.substring(0,i) + input.substring(i+1); //remove the ith letter of the string
            if(dictionary.contains(test)){                      //is this letter in the dictionary?
                addtoList(returnList, test, input);                    //if yes add it to the array list                      
            }
        }
           
        test = input.substring(0, lengthOfString);              //remove last letter of the string
        if(dictionary.contains(test)){                          //is the letter in the dictionary?
            addtoList(returnList, test, input);                        //if yes add it to the array list
        }
    }
    
    public static String printListChoice(ArrayList<String> list, String input){
        int userIn = -1;
        boolean validInput = false;
        while(validInput == false){
            StdOut.println(input +": did you mean:");
            int i = 1;
            for(String elem : list){
                StdOut.println(i +". "+ elem + " ");
                i++; }
            StdOut.println("0. Enter A Spelling");
            Scanner reader = new Scanner(System.in);
            
            try{
                userIn = reader.nextInt();
                if(userIn < 0){
                   StdOut.println("Please Enter a Valid Option");
                }else if(userIn > list.size()){
                   StdOut.println("Please Enter a Valid Option");
                }else{
                    validInput = true;}
                }
            catch(InputMismatchException e){
                StdOut.println("Not a valid Choice");}
        }
        if(userIn == 0){
            StdOut.println("Enter the correct Spelling");
            Scanner reader = new Scanner(System.in);
            String userInput = reader.next();
            StdOut.println("Your chose: " + userIn  + " -> " +  userInput);
            return userInput;
        }else if(userIn == -1){
            StdOut.println("Problem");
            
        }else{
            StdOut.println("Your chose: " + userIn + " -> " + list.get(userIn-1));
            return list.get(userIn-1);
        }
            return list.get(userIn-1);
    }
    
    public static void printList(ArrayList<String> list){
        for(String elem : list){
        StdOut.println(elem + " ");
        }
    }
    
    public static void main(String[] args) {
        SET<String> dictionary = new SET<String>();
        //SET<String> dictionary2 = new SET<String>();

        // read in dictionary of words
        In dict = new In(args[0]);
        while (!dict.isEmpty()) {
            String word = dict.readString();
            dictionary.add(word);
        }
        StdOut.println("Done reading dictionary");
        try(FileWriter fw = new FileWriter("mydoc-checked.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
            {
                In document = new In(args[1]);
                while (!document.isEmpty()) {
                    
                    String word2 = document.readString();
                   
                    boolean inDictionary = dictionary.contains(word2);
                    if(inDictionary == true){
                        StdOut.println("The word, " +word2+" is in the dictionary" );
                        out.print(word2 + " ");
                    }else{
                        StdOut.println("The word, " +word2+" is not the dictionary");
                        ArrayList<String> returnList = new ArrayList<>();
                        String correctedSpelling;
                        addChar(word2, returnList, dictionary);  //adds any words to the Possible Missspelling List "a" + "misspelled word"
                        removeChar(word2, returnList, dictionary);  //removes first letter then checks for the new word, then iterates through all the letters
                        removeChars(word2, returnList, dictionary);
                        correctedSpelling = printListChoice(returnList, word2);
                        StdOut.println("Corrected Spelling is: " + correctedSpelling);
                        out.print(correctedSpelling + " ");
                    }
                }     
                out.close();
            }
        catch (IOException ex) {
            Logger.getLogger(SpellChecker.class.getName()).log(Level.SEVERE, null, ex);
        }
        
     
    }
}