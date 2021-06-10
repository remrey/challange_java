import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;


/*
An ordinal number is a word representing rank or sequential order. The naming convention for royal names is to follow a given name with an ordinal number using a Roman numeral to indicate the birth order of two people of the same name.

The Roman numerals from 1 to 50 are defined as follows: The numbers 1 through 10 are written I, II, III, IV, V, VI, VII, VIII, IX, and X. The Roman numerals corresponding to the numbers 20, 30, 40, and 50 are XX, XXX, XL, and L. For any other two-digit number < 50, its Roman numeral representation is constructed by concatenating the numeral(s) for its multiples of ten with the numeral(s) for its values < 10. For example, 47 is 40 + 7 = "XL" + "VII" = "XLVII".

In this challenge, you will be given a list of royal name strings consisting of a given name followed by an ordinal number. You must sort the list first alphabetically by name, then by ordinal increasing within any given name.

For example, if you are given the royal names [George VI, William II, Elizabeth I, William I] the result of the sort is [Elizabeth I, George VI, William I, William II].

Function Description Create a function called getSortedList. The function must return the array sorted first by given name, then by ordinal.

getSortedList has the following parameter:

names[names[0],...names[n-1]]:  an array of royal name strings "given ordinal"
Constraints

1 ≤ n ≤ 50
Each names[i] is a single string composed of 2 space-separated values: firstName and ordinal.
ordinal is a valid Roman numeral representing a number between 1 and 50, inclusive.
1 ≤ |firstName| ≤ 20
Each firstName starts with an uppercase letter ascii[A-Z] and its remaining characters are lowercase letters ascii[a-z].
The elements in names are pairwise distinct.
Sample Input

Louis IX
Louis VIII
David II
Sample Output

David II
Louis VIII
Louis IX

*/
public class Solution {

    //This function converts Roman numbers to Arabic ones and returns its value.
    static int romToNum(String str) {
        int result = 0;
        //Lexicon will be used as a guidance to calculate the numbers.
        HashMap<Character, Integer> lexicon = new HashMap<>();

        lexicon.put('I', 1);
        lexicon.put('V', 5);
        lexicon.put('X', 10);
        lexicon.put('L', 50);

        //According to the input, at each iteration the result will be added depending --
        // on the number on the lexicon.
        for(int i=0; i<str.length();++i) {
            if(i > 0 && lexicon.get(str.charAt(i)) > lexicon.get(str.charAt(i-1))) {
                result += lexicon.get(str.charAt(i)) - (2 * lexicon.get(str.charAt(i-1)));
            } else {
                result += lexicon.get(str.charAt(i));
            }
        }

        return result;
    }

    static List<String> getSortedList(List<String> names) {
        //A treemap to store the inputs in ordered fashion.
        //However, it will ignore (miscalculate) the ordinal number part.
        Map<String, String> hashNames = new TreeMap<String, String>();

        for(int i=0;i<names.size();++i) {
            //Splitting names from the whitespace in middle
            String[] temp = names.get(i).split(" ");
            //ronToNum function converts Roman ordinal numbers to Arabic.
            //Then the Arabic numbers added to the end of the name. This way, treemap --
            //-- sorts the input accurately.
            hashNames.put(temp[0]+romToNum(temp[1]), names.get(i));

        }

        //converts the treemap to ArrayList of Strings.
        List<String> result = new ArrayList<>(hashNames.values());

        return result;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int namesCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<String> names = new ArrayList<>();

        for (int i = 0; i < namesCount; i++) {
            String namesItem = bufferedReader.readLine();
            names.add(namesItem);
        }

        List<String> res = getSortedList(names);

        for (int i = 0; i < res.size(); i++) {
            bufferedWriter.write(res.get(i));

            if (i != res.size() - 1) {
                bufferedWriter.write("\n");
            }
        }

        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
