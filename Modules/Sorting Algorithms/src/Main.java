public class Main {
    public static void main(String[] args) {
        int[] numbers = {64, 34, 25, 12, 22, 11, 90};
        System.out.println("BUBBLE SORT (Descending):");
        System.out.print("Original: ");
        printArray(numbers);
        bubbleSort(numbers);
        System.out.print("Sorted:   ");
        printArray(numbers);

        System.out.println();

        int[] numbers2 = {64, 34, 25, 12, 22, 11, 90};
        System.out.println("SELECTION SORT (Descending - Smallest to End):");
        System.out.print("Original: ");
        printArray(numbers2);
        selectionSort(numbers2);
        System.out.print("Sorted:   ");
        printArray(numbers2);
    }

    public static void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] < arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    public static void selectionSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = 0;
            for (int j = 1; j <= arr.length - 1 - i; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            int endIndex = arr.length - 1 - i;
            int temp = arr[endIndex];
            arr[endIndex] = arr[minIndex];
            arr[minIndex] = temp;
        }
    }

    public static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
}
