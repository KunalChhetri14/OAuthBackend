import java.util.*;

public class SampleQuestion {


    /*
    Input:  n = 36 -> 1,2,3,4,6,9   -> 49 36/9=4 , 4/4 = 1   -> 94
    Output: p = 49  //
    // Note that 4*9 = 36 and 49 is the smallest such number
    Input:  n = 100 -> 1,2,4,5 -> O(noOfFactors) 10, 20, 40, 50 -> 100/5 = 20, 20/5=4, 4/4=1  -> 5 5 4
    Output: p = 455
    // Note that 4*5*5 = 100 and 455 is the smallest such number
    Input: n = 1
    Output:p = 11
    n=2
    p=12 // 2/2 = 1
    n=3
    12 -> 1, 2,3,4,6 -> 6 2
    // Note that 1*1 = 1
    Input: n = 13  (1)  not possible
    Output: Not Possible
    no of output digits is greater than 2//  2 digit -> output -> 4 digit
    2 ->
     */

    public static void main(String args[]) {

        int start = 0;
        int n= 100;
        if(n < 10) {
            System.out.println(10+n);
        }
        List<Integer> factorsList = listOfFactors(n);
        if(factorsList.size() <=1) {
            System.out.println("No possible");
            return;
        }

        int end = factorsList.size()-1;
        String res = ""; //4  1,2,3,4,6,9  //13 -> empty 14 (2,7)

        while(n>1) {
            int endNo = factorsList.get(end);
            if((n/endNo) != 0) {
                n=n/endNo; // 36/9 = 4
                res+=endNo;
            }
            while(factorsList.get(end) > n && end>=0) {
                end--;
            }




        }
        int i = 0;
        int j = res.length();

        System.out.println("result Needs to be reversed"+res);




    }

    public static List<Integer> listOfFactors(int n) {

        List<Integer> factors = new ArrayList<>();

        for(int i=1; i<10; i++) {
            if(n%i == 0) {
                factors.add(i);
            }
        }



        return factors;


    }


}
