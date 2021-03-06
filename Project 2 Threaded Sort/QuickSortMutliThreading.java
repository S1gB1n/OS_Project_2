/**
 *  Code source: https://www.geeksforgeeks.org/quick-sort-using-multi-threading/
 * 
 * 	NOTE:
 * 		This file is Modified and not an "EXACT original copy" in the test file to handle different sizes of threads.
 * 		Few lines of code are edited and added to achieve this goal.
 * 		ex.
 * 			If we only want to employ 2 threads, it will only fork only 2 times.
 */


// Java program for the above approach
import java.io.*;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class QuickSortMutliThreading
	extends RecursiveTask<Integer> {

	int start, end;
	int[] arr;

	/**
	* Finding random pivoted and partition
	* array on a pivot.
	* There are many different
	* partitioning algorithms.
	* @param start
	* @param end
	* @param arr
	* @return
	*/
	private int partition(int start, int end,
						int[] arr)
	{

		int i = start, j = end;

		// Decide random pivot
		int pivote = new Random()
						.nextInt(j - i)
					+ i;

		// Swap the pivote with end
		// element of array;
		int t = arr[j];
		arr[j] = arr[pivote];
		arr[pivote] = t;
		j--;

		// Start partitioning
		while (i <= j) {

			if (arr[i] <= arr[end]) {
				i++;
				continue;
			}

			if (arr[j] >= arr[end]) {
				j--;
				continue;
			}

			t = arr[j];
			arr[j] = arr[i];
			arr[i] = t;
			j--;
			i++;
		}

		// Swap pivote to its
		// correct position
		t = arr[j + 1];
		arr[j + 1] = arr[end];
		arr[end] = t;
		return j + 1;
	}

	// Function to implement
	// QuickSort method
	public QuickSortMutliThreading(int start,
								int end,
								int[] arr)
	{
		this.arr = arr;
		this.start = start;
		this.end = end;
	}

	@Override
	protected Integer compute()
	{
		// Base case
		if (start >= end)
			return null;

		// Find partition
		int p = partition(start, end, arr);

		// Divide array
		QuickSortMutliThreading left
			= new QuickSortMutliThreading(start,
										p - 1,
										arr);

		QuickSortMutliThreading right
			= new QuickSortMutliThreading(p + 1,
										end,
										arr);


		//System.out.print(Thread.currentThread().getName()+ ", "); // test to check what thread is working
		// Left subproblem as separate thread
		left.fork();
		right.compute();

		// Wait untill left thread complete
		left.join();

		// We don't want anything as return
		return null;
	}

	// Driver Code
	/*public static void main(String args[])
	{
		int n = 40;
		//int[] arr = { 54, 64, 95, 82, 12, 32, 63 };
		
		int size = 40;
		Random random = new Random();
		int[] arr = new int[size];
		for (int i = 0; i < size; i++) {
            // add a +ve offset to the generated random number and subtract same offset
            // from total so that the number shifts towards negative side by the offset.
            // ex: if random_num = 10, then (10+100)-100 => -10
            arr[i] = random.nextInt(size + (size - 1)) - (size - 1);
        }


		// Forkjoin ThreadPool to keep
		// thread creation as per resources
		//ForkJoinPool pool = ForkJoinPool.commonPool();
		ForkJoinPool pool = new ForkJoinPool(3);


		// Start the first thread in fork
		// join pool for range 0, n-1
		pool.invoke(
			new QuickSortMutliThreading(
				0, n - 1, arr));

		// Print shorted elements
		for (int i = 0; i < n; i++)
			System.out.print(arr[i] + " ");
	}*/
}
