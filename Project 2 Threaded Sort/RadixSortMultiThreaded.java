/**
 *  Source Code: https://www.programiz.com/dsa/radix-sort
 * 
 */


import java.lang.Math;
import java.util.Arrays;
import java.util.Random;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.Timer;

class RadixSortMultiThreaded extends Thread{  
  
static int[] array;
static int[] sorted;
static int sortedArrayFillLevel =0;

/* public RadixSort(int[] array)
{
    array = this.array;
} */

static int getMax(int a[], int n) 
{  
   int max = a[0];  
   for(int i = 1; i<n; i++) 
   {  
      if(a[i] > max)  
         max = a[i];  
   }  
   return max; //maximum element from the array  
}  
  
static void countingSort(int a[], int n, int place) // function to implement counting   
    
{  
   int[] output = new int[n+1];  
 int[] count = new int[10];  
  
  // Calculate count of elements  
  for (int i = 0; i < n; i++)  
    count[(a[i] / place) % 10]++;  
      
  // Calculate cumulative frequency  
  for (int i = 1; i < 10; i++)  
    count[i] += count[i - 1];  
  
  // Place the elements in sorted order  
  for (int i = n - 1; i >= 0; i--) {  
    output[count[(a[i] / place) % 10] - 1] = a[i];  
    count[(a[i] / place) % 10]--;  
  }  
  
  for (int i = 0; i < n; i++)  
    a[i] = output[i];  
}  
  
// function to implement radix sort  
static void  radixsort(int a[], int n) 
{  
    
  // get maximum element from array  
  int max = getMax(a, n);  
  
  // Apply counting sort to sort elements based on place value  
  
  for (int place = 1; max / place > 0; place *= 10)  
    countingSort(a, n, place);
    
    
}  
  
// function to print array elements  
void printArray(int a[], int n) {  
  for (int i = 0; i < n; ++i)   
    System.out.print(a[i] + " ");  
}
  
 /* public static void main(String args[]) throws InterruptedException 
 {

    sort();


 } */
//{
   /* array  = generateIntegers(1000000); // edit this to sort # of integers
   sorted = new int[array.length];
   int numOfThreads = 6; // 
   int remainder = array.length % numOfThreads;
   int numberOfIntPerThread = array.length / numOfThreads;
   int start = 0;
   int end = numberOfIntPerThread + remainder;
   int[] copy = Arrays.copyOfRange(array,0,array.length); */ 
  
   
   
   
   
   
   
    /* int counter = 0;

    System.out.print("Array before sorting: ");
    while(counter < copy.length)
    {
        System.out.print(copy[counter] + ",");
        counter++;
    }
    System.out.println(); */

    /* long startTime = System.nanoTime();

     while(numOfThreads > 0)
    {
        array = Arrays.copyOfRange(copy,start,end);
        RadixSort thread = new RadixSort();
        thread.start();

        thread.join();
        start = end;
        end = end + numberOfIntPerThread;
        numOfThreads--;
    }  */

    
    
    public static int[] radixSort(int[]array,int threads) throws InterruptedException
    {
      //array = this.array;

    sorted = new int[array.length];
    int numOfThreads = threads; // 
    int remainder = array.length % numOfThreads;
    int numberOfIntPerThread = array.length / numOfThreads;
    int start = 0;
    int end = numberOfIntPerThread + remainder;
    int[] copy = Arrays.copyOfRange(array,0,array.length);

    


    //long startTime = System.nanoTime();

     while(numOfThreads > 0)
    {
        array = Arrays.copyOfRange(copy,start,end);
        RadixSortMultiThreaded thread = new RadixSortMultiThreaded();
        thread.start();

        thread.join();
        start = end;
        end = end + numberOfIntPerThread;
        numOfThreads--;
    } 
    radixsort(sorted,sorted.length);

    return sorted;

    
    }


    // final sort after all the seperate sorted arrays are combined
    

    //long endTime = System.nanoTime();
    //System.out.println("Time in miliseconds:" + (endTime - startTime) / 1000000.0);

    /* counter = 0;
    // prints out the sorted array
    System.out.print("Array after sorting: ");
    while(counter < sorted.length)
    {
        System.out.print(sorted[counter] + ",");
        counter++;
    } */
   



   



public static void appendArray(int[] sortedArray, int[] otherArray)
{
    int counter = 0;

    while(counter < otherArray.length)
    {
        sortedArray[sortedArrayFillLevel] = otherArray[counter];
        counter++;
        sortedArrayFillLevel++;
    }
    

}


public void run()
{
    //System.out.println("array length " + array.length);
    radixsort(array, array.length);

    appendArray(sorted,array);
    //System.out.println("Thread here");

}


public static int[]generateIntegers(int numberOfIntegers)
{
    Random rand = new Random();
    array = new int[numberOfIntegers];
    int counter = 0;

    while(counter < numberOfIntegers)
    {
        array[counter] = rand.nextInt(1000);
        counter++;
    }
    return array;
}
}