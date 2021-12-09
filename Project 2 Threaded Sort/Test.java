
/**
 * Author:  Erl John Lydzustre
 *          Erik Eszilagyi
 * 
 * Instructor: Marc Schroeder
 * 
 * OS Project 2: Threaded Sorting Algorithms
 *      - Merge Sort
 *      - Quick Sort
 *      - Radix Sort
 *  
 *      This Project looks at different type of sorting algorithms (Merge, Quick, Radix).
 *      Where it will test:
 *              Number of Threads from (2 - 6)
 *                  
 *              And within the thread it will test Different sizes of Integer Items (10 - 1 000 000)
 * 
 *              ex.
 *                      Thread 2:
 *                          Test - 10 Integer Items to sort
 *                          Test - 100 Integer Items to sort
 *                          Test - 1 000 Integer Item to sort
 *                          Test - 10 000 Integer Item to sort
 *                          Test - 100 000 Integer Item to sort
 *                          Test - 1 000 000 Integer Item to sort
 *                      Thread 3:   
 *                          Test - 10 Integer Items to sort
 *                          Test - 100 Integer Items to sort
 *                          Test - 1 000 Integer Item to sort
 *                          Test - 10 000 Integer Item to sort
 *                          Test - 100 000 Integer Item to sort
 *                          Test - 1 000 000 Integer Item to sort
 *                      ....
 *                      Thread 6:
 *                          ....
 *  
 *      We are only sorting up until 1 000 000, beyond this number will take a lot of munites to finish the whole testing.
 * 
 *      After the Test.java is run it will write text files in the same root directory to show Latex graph to show
 *      different graphs for different threads with tested integer items.
 * 
 *      Conclusion:
 * 
 *          MergeSort:
 *              Testing roughly around 10 - 10 000 integer items to sort, the multi-threaded is worst for time, 
 *              while the regular (1-threaded) mergesort is way faster. But roughly around 10 000 - > 1 000 000
 *              multi-threaded is clearly the superior for time, because the difference in time is roughly around
 *              3 - 4 times faster in Multi-threaded MergeSort.
 * 
 *          QuickSort:
 *              Testing roughly around 10 - 10 000 integer items to sort, the multi-threaded is worst for time, 
 *              while the regular (1-threaded) quicksort is way faster. But roughly around 10 000 - > 1 000 000
 *              multi-threaded is little bit superior for time. because the difference in time is roughly around
 *              10 - 20 percent faster for time in Multi-threaded QuickSort.
 * 
 *          In conclusion MergeSort Multi-threading is the superior sorting algorithm for large quantity of items.         
 * 
 * 
 *      Known Bugs:
 *           Global variables in the MultithreadedRadixSort are not being properly set, resulting in null pointer 
 *           errors when being called. This is some Java quirk that probably has to do with how variables are 
 *           inherited when being called from another class.
 *  
 */

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

class Test {

    private static Random random = new Random();

    private static int max_threads = 6; // Number of threads it will assign

    private static int sort_size = 7; // How many Integer items will be sorted
                                      // = 10 ^ sort_size

    private static Integer merge_list[];
    private static int quick_list[];
    private static int radix_list[];

    private static Integer unsorted_x_list[];
    private static Integer sorted_x_list[];

    // private static final int test_size = 10;
    private static ArrayList<long[]> arr_non_threaded_time_list = new ArrayList<>();
    private static ArrayList<long[]> arr_threaded_time_list = new ArrayList<>();
    private static long[] non_thread_time_list = new long[sort_size];
    private static long[] threaded_time_list = new long[sort_size];

    /**
     * =========================================================================================================================================================================================
     * WRITING GRAPH ON LATEX
     * =========================================================================================================================================================================================
     */

    static String addPlot(long[] time, int size, String type) {
        // int time_size = 100;
        String color;
        if (type.compareTo("threaded") == 0) {
            color = "red";
        } else {
            color = "blue";
        }
        ;
        String str = "\\addplot[ color=" + color + ", mark=square,] coordinates {";
        for (int i = 1; i < size; i++) {
            str += "(" + Math.pow(10, i + 1) + "," + time[i] + ")";
        }
        str += "};\n";
        return str;
    }

    static String print_arr_sorted_and_unsorted() {
        String st = "\\\\Unsorted: [";
        for (int i = 0; i < unsorted_x_list.length; i++) {
            st += unsorted_x_list[i];
            if (i + 1 < unsorted_x_list.length)
                st += ", ";
        }
        st += "]\n\\\\Sorted: [";
        for (int i = 0; i < sorted_x_list.length; i++) {
            st += sorted_x_list[i];
            if (i + 1 < sorted_x_list.length)
                st += ", ";
        }
        st += "]\n";
        return st;
    }

    static void printLatex(String type_of_sort) {
        try {
            FileWriter myWriter = new FileWriter(type_of_sort + ".txt");

            myWriter.write("\\documentclass{article} \n");
            myWriter.write("\\usepackage[utf8]{inputenc} \n");
            myWriter.write("\\usepackage{tikz} \n");
            myWriter.write("\\usepackage{pgfplots}\n");
            myWriter.write("\\usetikzlibrary{calc}\n");
            myWriter.write("\\author{erljohn91 }\n");
            myWriter.write("\\date{November 2021}\n");

            myWriter.write("\\begin{document}\n");

            myWriter.write("\\\\10 item sort: test (showing that sorting works)\n");
            
            myWriter.write(print_arr_sorted_and_unsorted());

            myWriter.write("\\section{" + type_of_sort + "}\n");
            // myWriter.write("This shows the MergeSort: Thread#: " + (i + 2) + ".\n");

            for (int i = 0; i <= max_threads - 2; i++) {

                myWriter.write("\\begin{figure}[!ht]\n");
                myWriter.write("\\centering\n");
                myWriter.write("\\begin{tikzpicture}\n");
                myWriter.write(
                        "\\begin{axis}[name=plot, xlabel={Number Of Integer}, ylabel={Time}, xmin=0, xmax=10000, ymin=0, ymax=10, width=1.1\\textwidth]\n");
                myWriter.write(addPlot(arr_threaded_time_list.get(i), sort_size, "threaded"));
                myWriter.write("\\addlegendentry{Threaded: " + (i + 2) + "}\n");
                myWriter.write(addPlot(arr_non_threaded_time_list.get(i), sort_size, "non_threaded"));
                myWriter.write("\\addlegendentry{Non-Threaded}\n");
                myWriter.write("\\end{axis}\n");
                myWriter.write("\\end{tikzpicture}\n");
                myWriter.write("\\end{figure}\n");

                myWriter.write("\\begin{figure}[!ht]\n");
                myWriter.write("\\centering\n");
                myWriter.write("\\begin{tikzpicture}\n");
                int xmax = (int) Math.pow(10, sort_size);
                myWriter.write(
                        "\\begin{axis}[name=plot, xlabel={Number Of Integer Items: " + xmax
                                + "}, ylabel={Time}, xmin=0, xmax=" + xmax
                                + ", ymin=0, ymax=600, width=1.1\\textwidth]\n");
                myWriter.write(addPlot(arr_threaded_time_list.get(i), sort_size, "threaded"));
                myWriter.write("\\addlegendentry{Threaded: " + (i + 2) + "}\n");
                myWriter.write(addPlot(arr_non_threaded_time_list.get(i), sort_size, "non_threaded"));
                myWriter.write("\\addlegendentry{Non-Threaded}\n");
                myWriter.write("\\end{axis}\n");
                myWriter.write("\\end{tikzpicture}\n");
                myWriter.write("\\end{figure}\n");
            }

            myWriter.write("\\end{document}\n");

            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * =========================================================================================================================================================================================
     * 
     * =========================================================================================================================================================================================
     */

    /**
     * =========================================================================================================================================================================================
     * TESTING MERGE SORT
     * =========================================================================================================================================================================================
     */

    static void print_merge_list() {
        System.out.print("Input = [");
        for (Integer each : merge_list)
            System.out.print(each + ", ");
        System.out.print("] \n" + "Input.length = " + merge_list.length + '\n');
    }

    // Fill the initial array with random elements within range
    static Integer[] fill_arr(int size) {
        for (int i = 0; i < size; i++) {
            // add a +ve offset to the generated random number and subtract same offset
            // from total so that the number shifts towards negative side by the offset.
            // ex: if random_num = 10, then (10+100)-100 => -10
            merge_list[i] = random.nextInt(size + (size - 1)) - (size - 1);
        }
        return merge_list;
    }

    static void testMerge() {

        int size = 10;

        merge_list = new Integer[size];
        merge_list = fill_arr(size);

        print_merge_list();

        for (int j_threads = 2; j_threads <= max_threads; j_threads++) { // test for threads

            System.out.println("Number Of Thread: " + j_threads);
            for (int i = 0; i < sort_size; i++) { // test for number integer size

                System.out.println("Size: " + size);
                // Test custom single-threaded merge sort (recursive merge) implementation
                Integer[] arr2 = Arrays.copyOf(merge_list, merge_list.length);
                long t = System.currentTimeMillis();
                MergeSort.mergeSort(arr2, 0, arr2.length - 1);
                t = System.currentTimeMillis() - t;
                System.out.println("Time spent for custom single threaded recursive merge_sort(): " + t + "ms");
                non_thread_time_list[i] = t;

                // Test custom (multi-threaded) merge sort (recursive merge) implementation
                Integer[] arr = Arrays.copyOf(merge_list, merge_list.length);
                t = System.currentTimeMillis();
                MergeSort.threadedSort(arr, j_threads);
                t = System.currentTimeMillis() - t;
                System.out.println("Time spent for custom multi-threaded MAX_THREADS[" + j_threads
                        + "] recursive merge_sort(): " + t + "ms\n");
                threaded_time_list[i] = t;

                // testing that the sorting actually works, will be printed in latex
                if (j_threads == 2 && i == 0) {
                    unsorted_x_list = new Integer[merge_list.length];
                    sorted_x_list = new Integer[merge_list.length];
                    unsorted_x_list = Arrays.copyOf(merge_list, merge_list.length);
                    sorted_x_list = Arrays.copyOf(arr, arr.length);
                }

                size *= 10;
                merge_list = new Integer[size];
                merge_list = fill_arr(size);
            }
            size = 10;
            arr_non_threaded_time_list.add(non_thread_time_list);
            arr_threaded_time_list.add(threaded_time_list);
            non_thread_time_list = new long[sort_size];
            threaded_time_list = new long[sort_size];

            merge_list = new Integer[size];
            merge_list = fill_arr(size);
        }
        printLatex("Threaded Merge Sort");
    }

    /**
     * =========================================================================================================================================================================================
     * TESTING MERGE SORT
     * =========================================================================================================================================================================================
     */

    static int[] fill_quick_arr(int size) {
        for (int i = 0; i < size; i++) {
            quick_list[i] = random.nextInt(size + (size - 1)) - (size - 1);
        }
        return quick_list;
    }

    static void testQuickSort() {

        int size = 10;

        quick_list = new int[size];
        quick_list = fill_quick_arr(size);

        System.out.println("\n\nQUICK SORT THREADED SORT TESTING: \n");
        for (int j_threads = 2; j_threads <= max_threads; j_threads++) {

            ForkJoinPool pool = new ForkJoinPool(j_threads);
            System.out.println("Number Of Thread: " + j_threads);
            for (int i = 0; i < sort_size; i++) {

                System.out.println("Size: " + size);
                // Test custom single-threaded quick sort (recursive quick) implementation
                int[] arr2 = Arrays.copyOf(quick_list, quick_list.length);
                long t = System.currentTimeMillis();
                QuickSort.quickSort(arr2, 0, arr2.length - 1);
                t = System.currentTimeMillis() - t;
                System.out.println("Time spent for custom single threaded recursive quickSort(): " + t + "ms");
                non_thread_time_list[i] = t;

                // Test custom (multi-threaded) quick sort (recursive quick) implementation
                int[] arr = Arrays.copyOf(quick_list, quick_list.length);
                t = System.currentTimeMillis();
                pool.invoke(new QuickSortMutliThreading(0, arr.length - 1, arr));
                t = System.currentTimeMillis() - t;
                System.out.println("Time spent for custom multi-threaded MAX_THREADS[" + j_threads
                        + "] recursive quickSort(): " + t + "ms\n");
                threaded_time_list[i] = t;

                // testing that the sorting actually works, will be printed in latex

                if (j_threads == 2 && i == 0) {
                    unsorted_x_list = new Integer[quick_list.length];
                    sorted_x_list = new Integer[quick_list.length];
                    for(int i_list = 0; i_list < quick_list.length; i_list++){
                        unsorted_x_list[i_list] = Integer.valueOf(quick_list[i_list]);  
                        sorted_x_list[i_list] = Integer.valueOf(arr[i_list]);      
                    }
                }

                size *= 10;
                quick_list = new int[size];
                quick_list = fill_quick_arr(size);
            }

            size = 10;
            arr_non_threaded_time_list.add(non_thread_time_list);
            arr_threaded_time_list.add(threaded_time_list);
            non_thread_time_list = new long[sort_size];
            threaded_time_list = new long[sort_size];

            quick_list = new int[size];
            quick_list = fill_quick_arr(size);
        }

        printLatex("Threaded Quick Sort");
    }


    /**
     * =========================================================================================================================================================================================
     * TESTING RADIX SORT
     * =========================================================================================================================================================================================
     */

    static int[] fill_radix_arr(int size) {
        for (int i = 0; i < size; i++) {
            radix_list[i] = Math.abs(random.nextInt(size + (size - 1)) - (size - 1));
        }
        return radix_list;
    }

    static void testRadixSort() throws InterruptedException{
        int size = 10;

        radix_list = new int[size];
        radix_list = fill_radix_arr(size);

        System.out.println("\n\nRADIX SORT THREADED SORT TESTING: \n");
        for (int j_threads = 2; j_threads <= max_threads; j_threads++) {

            System.out.println("Number Of Thread: " + j_threads);
            for (int i = 0; i < sort_size; i++) {
                System.out.println("Size: " + size);
                // Test custom single-threaded radix sort implementation
                int[] arr2 = Arrays.copyOf(radix_list, radix_list.length);
                long t = System.currentTimeMillis();
                RadixSort.radixSort(arr2, arr2.length);
                t = System.currentTimeMillis() - t;
                System.out.println("Time spent for custom single threaded radixsort(): " + t + "ms");
                non_thread_time_list[i] = t;

                // Test custom (multi-threaded) radix sort implementation
                int[] arr = Arrays.copyOf(radix_list, radix_list.length);
                t = System.currentTimeMillis();
                //RadixSortMultiThreaded.radixSort(arr, j_threads);
                t = System.currentTimeMillis() - t;
                System.out.println("Time spent for custom multi-threaded MAX_THREADS[" + j_threads
                        + "] radixsort(): " + t + "ms\n");
                threaded_time_list[i] = t;

                if (j_threads == 2 && i == 0) {
                    unsorted_x_list = new Integer[radix_list.length];
                    sorted_x_list = new Integer[radix_list.length];
                    for(int i_list = 0; i_list < radix_list.length; i_list++){
                        unsorted_x_list[i_list] = Integer.valueOf(radix_list[i_list]);  
                        //sorted_x_list[i_list] = Integer.valueOf(arr[i_list]);      
                    }
                }

                size *= 10;
                radix_list = new int[size];
                radix_list = fill_radix_arr(size);
            }

            size = 10;
            arr_non_threaded_time_list.add(non_thread_time_list);
            arr_threaded_time_list.add(threaded_time_list);
            non_thread_time_list = new long[sort_size];
            threaded_time_list = new long[sort_size];

            radix_list = new int[size];
            radix_list = fill_radix_arr(size);
        }

        printLatex("Threaded Radix Sort");
    }


    /**
     * =========================================================================================================================================================================================
     * 
     * =========================================================================================================================================================================================
     * @throws InterruptedException
     */

    // Test the sorting methods performance
    public static void main(String[] args) throws InterruptedException {

        testMerge();

        arr_non_threaded_time_list.clear();
        arr_threaded_time_list.clear();

        testQuickSort();

        arr_non_threaded_time_list.clear();
        arr_threaded_time_list.clear();

        testRadixSort();
    }
}
