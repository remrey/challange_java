import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;


/**
-------QUESTION------
You are given a binary tree as a sequence of parent-child pairs.
For example, the tree represented by the node pairs below:
(A,B) (A,C) (B,G) (C,H) (E,F) (B,D) (C,E)
may be illustrated in many ways, with two possible representations below:

Below is the recursive definition for the S-expression of a tree:
S-exp(node) = ( node->val
(S-exp(node->first_child))(S-exp(node->second_child))), if node != NULL
                         = "", node == NULL
   where, first_child->val < second_child->val (lexicographically smaller)

This tree can be represented in a S-expression in multiple ways.
The lexicographically smallest way of expressing this is as follows:
(A(B(D)(G))(C(E(F))(H)))

We need to translate the node-pair representation into an S-expression
(lexicographically smallest one), and report any errors that do not conform to the definition of a binary tree.
The list of errors with their codes is as follows:
Error Code          Type of error
E1                 Input Error
E2                 Duplicate Nodes
E3                 More than 2 children
E4                 There is a cycle in the system.
E5                 Multiple Roots

Input Format:
Input must be read from standard input.
Input will consist of on line of parent-child pairs. Each pair consists of two node names separated by a single comma and enclosed in parentheses. A single space separates the pairs.
Output:
The function must write to standard output.
Output the given tree in the S-expression representation described above.
There should be no spaces in the output.
Constraints:
There is no specific sequence in which the input (parent,child) pairs are represented.
The name space is composed of upper case single letter (A-Z) so the maximum size is 26 nodes.
Error cases are to be tagged in the order they appear on the list. For example,
if one input pair raises both error cases 1 and 2, the output must be E1.
Sample Input #00
(B,D) (D,E) (A,B) (C,F) (E,G) (A,C)
Sample Output #00
(A(B(D(E(G))))(C(F)))
Sample Input #01
(A,B) (A,C) (B,D) (D,C)
Sample Output #01
E3

*/


public class Solution {
    static String root =""; //root node, will be calculated after everything is read.
    static HashMap<String, String> tree = new HashMap<String, String>();


    public static String checkE3E4E5(){
        int[] letterArr = new int[26]; //allocating memory for each letter

        //Each letter will be given an index on the array according to their ASCII code.
        //At any index, there can be maximum of 2 visits, aka each parent --
        //-- can have only 2 children.
        //If there is more than 2 visits, it means a Parent has more than 2children. E3 thrown.
         for(String value: tree.values()) {
             //zeroing the ASCII. A is 65, setting it to 0
             int index = (int)value.charAt(0) - 65;
             letterArr[index]++;
             if(letterArr[index] >=3) return "E3";
        }

        //this hashmap will be used to check if there is more than 1 root.
        HashMap<String, Integer> rootMap = new HashMap<String, Integer>();

        //Root nodes dont have parents. So if a Node doesnt have parent in the tree(key) --
        //That means its a root node. We will store it.
        for(String key: tree.keySet()) {
            String temp = tree.get(key); //value of the key
            if(!tree.containsKey(temp)) {
                rootMap.put(temp, 1);
                root = temp;
            }
        }
        //If there is no root element, it means there is a cycle.
        //This was the case we couldnt catch previously.
        if(rootMap.size()==0) return "E4";
        //If there is more than 1 root element, it means there is multiple roots.
        if(rootMap.size()>=2) return "E5";

        return "";
    }

    //This function will be used to convert String,String HashMap into --
    // a Strig,Arraylist<string> hashmap. That way, we can store children of parents on same        //value.
    //Overall, if the architecture designed more smoothly, there wouldnt be any need --
    //for this function.
    public static void printBinaryTree() {

        //Creating String Arraylist from keys(parents).
        Set<String> keySet = tree.keySet();
        ArrayList<String> children = new ArrayList<String>(keySet);

        //Creating String arraylist from values(children).
        Collection<String> values = tree.values();
        ArrayList<String> parents = new ArrayList<>(values);

        //Creating the final HashMap for recursive print.
        HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

        //Filling the HashMap. Parents will be keys, their chilren will be stored --
        // an arraylist.
        for (int i = 0; i < parents.size(); i++) {
            if(map.containsKey(parents.get(i))){
                map.get(parents.get(i)).add(children.get(i));
            } else{
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(children.get(i));
                map.put(parents.get(i),temp);
            }
        }

        //Recursive printing method.
        recPrint(map,root);
    }

    //Recursive printing method.
    public static void recPrint(HashMap<String, ArrayList<String>> map, String current){
        System.out.print("(" + current);
        if(map.get(current)==null){
            System.out.print(")");
            return; //termination condition
        }
        int curNode = map.get(current).size();
        if(curNode == 1){ //if only 1child left, print it and terminate on next iteration.
             recPrint(map, map.get(current).get(0));
        }
        else if(curNode==2){ //Cannot be more than 2. Each node has 2children.
        //Deciding which letter to print, since we have to take care the --
        //Lexicographically smallest.
            char ch0 = map.get(current).get(0).charAt(0);
            char ch1 = map.get(current).get(1).charAt(0);
            if((int)ch0>(int)ch1) {
                recPrint(map, map.get(current).get(1));
                recPrint(map, map.get(current).get(0));
            } else{
                recPrint(map, map.get(current).get(0));
                recPrint(map, map.get(current).get(1));
            }
        }

        System.out.print(")");
    }



    public static void main(String args[] ) throws Exception {
        //The scanner first read the entire input.
        Scanner sc = new Scanner(System.in);
        String errorCode = "";
        String stdIn = sc.nextLine();
        //The regex that we will use to check if the patterns are matched.
        String regex = "^\\(([A-Z]),([A-Z])\\)*$";
        Pattern pattern = Pattern.compile(regex);

        //If input contains 2 consecutive whitespaces, E1 will be thrown.
        if(stdIn.contains("  ")) errorCode = "E1";
        else {
            //If not, we will start reading with another scanner.
            Scanner sc1 = new Scanner(stdIn);
            while(sc1.hasNext()) {
                String cur = sc1.next();
                //If the current length of the string is lesser than 4, E1 will be thrown.
                if(cur.length()<4){errorCode = "E1"; break;}
                Matcher matcher = pattern.matcher(cur);
                //If the regex cannot finds the required pattern, E1 will be thrown
                if(!matcher.find()){errorCode = "E1"; break;}

                //Removing () and whitespaces etc and adding both letters inside a String arr.
                String [] temp = cur.replaceAll("[()]+", "").split(",");
                //HashMap is used to contain all inputs. It is globally declared.
                //Parents are stored as key, Children are stored as value.
                //Checking for duplicate. If a HashMap index is identical, E2 will thrown.
                if(tree.containsKey(temp[1]) && tree.get(temp[1]).equals(temp[0])) {
                    errorCode = "E2";
                    break;
                } else if(tree.containsKey(temp[1])) {
                    //There can be only 1 parent, if there is 2parent --
                    //it means that there is a cycle. Only case this wont happen is that --
                    //when the cycle is directed to the root node. E4 will thrown.
                    errorCode = "E4";
                    break;
                }
                //If E1, E2 and E4 not found, it will add to the hashmap.
                tree.put(temp[1], temp[0]);
            }
        }

        //It will check for E3 and E5 conditions PLUS E4 where the root is causing the cycle.
        if(errorCode == "") errorCode = checkE3E4E5();
        //If ther is an error, the system will print it out.
        if(errorCode != "") System.out.println(errorCode);
        //If not, we will print the result. This part is done alittle bit messy.
        //There is huge room for improvement in the sense of Space complexity + cleaner code.
        else printBinaryTree();

    }
}
