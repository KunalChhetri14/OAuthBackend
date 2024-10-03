package Interview;

public class XorQuestion {

    public static void main(String args[]) {
        int x  = 12345%1000;
        System.out.println("Rem: "+x);

            int arr[] = {1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10};
//            int xor = 0;
//            System.out.println("Result "+ binarySearch(arr, 0, arr.length-1));

        findNextPermutation();


//            for(int i=0; i<arr.length; i++) {
//                xor = xor^arr[i];
//            }
//            System.out.println("The unique element is :: "+ xor);
    }

    public static int binarySearch(int arr[] , int start, int end) {
        int result = -1;
        if(arr.length == 1) {
            return arr[0];
        }
        while(start<end) {

            int mid = (start+end)/2;
            if(mid == 0 && arr[mid+1] != arr[mid]) {
                return arr[mid];
            }

            if(mid== arr.length && arr[mid-1] != arr[mid]) {
                return arr[mid];
            }

            // first condition if both the left and right side is not equal to mid
            if(arr[mid] != arr[mid-1] && arr[mid] != arr[mid+1]) {
                return arr[mid];
            } else if(mid%2 != 0) {  // lets say 9th index that means if all have pairs then 9th index should be the last number
                //compare the mid with next element
                if(arr[mid] != arr[mid+1]) {
                    start = mid+1;
                } else {
                    end=mid-1;
                }
            } else {
                // compare the mid with prev element
                if(arr[mid] == arr[mid+1]) {
                    start = mid+1;
                } else {
                    end = mid-1;
                }
            }

        }

        return result;
    }

    public static void  findNextPermutation() {
        int input[] = {1,2,3,5,4};  // 1 2 4 3 5

        //find the point which is lessan previous element
        int point = Integer.MIN_VALUE;
        int index = -1;
        for(int i=input.length-1; i>=0; i--) {
            if(input[i]<point) {
                index = i;
                break;
            }
            point = input[i];
        }

        if(index == -1) {
            reverse(input, 0, input.length-1);
        } else {
            swap(input, index, input.length-1);

            reverse(input, index+1, input.length-1);
        }

        System.out.println("Result is");

        for(int i=0; i<input.length;i++) {
            System.out.println("No is : "+input[i]);
        }





    }

    public static void reverse(int arr[], int start, int end) {

        while(start<end) {
            swap(arr, start, end);
            start++;
            end--;
        }
    }


    public static void swap(int arr[], int i, int j) {

        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;

    }

    // 1 1 2 3 3 4

}
