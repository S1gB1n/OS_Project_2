
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

class Test {

    // Array Size
    private static Random random = new Random();
    // private static final int size = random.nextInt(100);
    private static int sort_size = 6;
    private static int size = 10;
    // private static final Integer list[] = new Integer[size];
    private static Integer list[];
    

    //private static final int test_size = 10;
    private static ArrayList<long[]> arr_non_threaded_time_list = new ArrayList<>();
    private static ArrayList<long[]> arr_threaded_time_list = new ArrayList<>();
    private static long[] non_thread_time_list = new long[sort_size];
    private static long[] threaded_time_list = new long[sort_size];

    /**
     * =========================================================================================================================================================================================
     * PRODUCING RANDOM ARRAYS
     * =========================================================================================================================================================================================
     */

    // Fill the initial array with random elements within range
    static Integer[] fill_arr() {
        for (int i = 0; i < size; i++) {
            // add a +ve offset to the generated random number and subtract same offset
            // from total so that the number shifts towards negative side by the offset.
            // ex: if random_num = 10, then (10+100)-100 => -10
            list[i] = random.nextInt(size + (size - 1)) - (size - 1);
        }
        return list;
    }

    /**
     * =========================================================================================================================================================================================
     * 
     * =========================================================================================================================================================================================
     */

    /**
     * =========================================================================================================================================================================================
     * WRITING GRAPH ON LATEX
     * =========================================================================================================================================================================================
     */

    static String addPlot(long[] time, int size, String type) {
        //int time_size = 100;
        String color;
        if (type.compareTo("threaded") == 0) {
            color = "red";
        } else {
            color = "blue";
        }
        ;
        String str = "\\addplot[ color=" + color + ", mark=square,] coordinates {";
        for (int i = 1; i < size; i++) {
            str += "(" + Math.pow(10, i+1) + "," + time[i] + ")";
        }
        str += "};\n";
        return str;
    }

    static void printLatex(int x_max, int n_threads) {
        try {
            FileWriter myWriter = new FileWriter("filename.txt");

            myWriter.write("\\documentclass{article} \n");
            myWriter.write("\\usepackage[utf8]{inputenc} \n");
            myWriter.write("\\usepackage{tikz} \n");
            myWriter.write("\\usepackage{pgfplots}\n");
            myWriter.write("\\usetikzlibrary{calc}\n");
            myWriter.write("\\author{erljohn91 }\n");
            myWriter.write("\\date{November 2021}\n");

            myWriter.write("\\begin{document}\n");

            myWriter.write("\\section{MergeSort}\n");
            //myWriter.write("This shows the MergeSort: Thread#: " + (i + 2) + ".\n");

            for (int i = 0; i <= n_threads - 2; i++) {

                myWriter.write("\\begin{figure}[!ht]\n");
                myWriter.write("\\centering\n");
                myWriter.write("\\begin{tikzpicture}\n");
                myWriter.write(
                        "\\begin{axis}[name=plot, xlabel={Number Of Integer}, ylabel={Time}, xmin=0, xmax=10000, ymin=0, ymax=10, width=1.1\\textwidth]\n");
                myWriter.write(addPlot(arr_threaded_time_list.get(i), x_max, "threaded"));
                myWriter.write("\\addlegendentry{Threaded: " + (i+2) + "}\n");
                myWriter.write(addPlot(arr_non_threaded_time_list.get(i), x_max, "non_threaded"));
                myWriter.write("\\addlegendentry{Non-Threaded}\n");
                myWriter.write("\\end{axis}\n");
                myWriter.write("\\end{tikzpicture}\n");
                myWriter.write("\\end{figure}\n");


                myWriter.write("\\begin{figure}[!ht]\n");
                myWriter.write("\\centering\n");
                myWriter.write("\\begin{tikzpicture}\n");
                int xmax = (int)Math.pow(10, x_max);
                myWriter.write(
                        "\\begin{axis}[name=plot, xlabel={Number Of Integer Items: " + xmax + "}, ylabel={Time}, xmin=0, xmax=" + xmax + ", ymin=0, ymax=600, width=1.1\\textwidth]\n");
                myWriter.write(addPlot(arr_threaded_time_list.get(i), x_max, "threaded"));
                myWriter.write("\\addlegendentry{Threaded: " + (i+2) + "}\n");
                myWriter.write(addPlot(arr_non_threaded_time_list.get(i), x_max, "non_threaded"));
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
    static void testMerge() {

        int max_threads = 6;

        list = new Integer[size];
        list = fill_arr();

        System.out.print("Input = [");
        for (Integer each : list)
            System.out.print(each + ", ");
        System.out.print("] \n" + "Input.length = " + list.length + '\n');

        for (int j_threads = 2; j_threads <= max_threads; j_threads++) {

            System.out.println("Number Of Thread: " + j_threads);
            for (int i = 0; i < sort_size; i++) {

                System.out.println("Size: " + size);
                // Test custom single-threaded merge sort (recursive merge) implementation
                Integer[] arr2 = Arrays.copyOf(list, list.length);
                long t = System.currentTimeMillis();
                MergeSort.mergeSort(arr2, 0, arr2.length - 1);
                t = System.currentTimeMillis() - t;
                System.out.println("Time spent for custom single threaded recursive merge_sort(): " + t + "ms");
                non_thread_time_list[i] = t;

                // Test custom (multi-threaded) merge sort (recursive merge) implementation
                Integer[] arr = Arrays.copyOf(list, list.length);
                t = System.currentTimeMillis();
                MergeSort.threadedSort(arr, j_threads);
                t = System.currentTimeMillis() - t;
                System.out.println("Time spent for custom multi-threaded MAX_THREADS[" + j_threads
                        + "] recursive merge_sort(): " + t + "ms\n");
                threaded_time_list[i] = t;

                size *= 10;
                list = new Integer[size];
                list = fill_arr();
            }
            size = 10;
            arr_non_threaded_time_list.add(non_thread_time_list);
            arr_threaded_time_list.add(threaded_time_list);
            non_thread_time_list = new long[sort_size];
            threaded_time_list = new long[sort_size];
        }
        printLatex(sort_size, max_threads);

    }


    static void testQuickSort(){
        int max_threads = 6;
        size = 100;

        int[] list_quicksort;
        list_quicksort = new int[size];
        for (int i = 0; i < size; i++) {
            // add a +ve offset to the generated random number and subtract same offset
            // from total so that the number shifts towards negative side by the offset.
            // ex: if random_num = 10, then (10+100)-100 => -10
            list_quicksort[i] = random.nextInt(size + (size - 1)) - (size - 1);
        }


        ForkJoinPool pool = new ForkJoinPool(max_threads);
        pool.invoke(new QuickSortMutliThreading(0, size-1, list_quicksort));


        //3 5 7 11 9 13
        //for (int i = 0; i < size; i++)
		//	System.out.print(list_quicksort[i] + " ");


        for(int j_threads = 2; j_threads <= max_threads; j_threads++){
            
        }
    }


    /**
     * =========================================================================================================================================================================================
     * 
     * =========================================================================================================================================================================================
     */

    // Test the sorting methods performance
    public static void main(String[] args) {

        //testMerge();

        testQuickSort();
    }
}
