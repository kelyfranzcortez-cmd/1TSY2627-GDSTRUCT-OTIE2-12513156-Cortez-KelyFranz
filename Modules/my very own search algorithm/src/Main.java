/*
 * THE KFCORE METHOD (Kick off From the CORE)
 *
 * Q1: Must the data set be sorted or unsorted?
 * Neither. KFCore doesn't care about order, so an unsorted list is fine.
 *
 * Q2: What are the steps?
 * Think of dropping a pebble in a pond. The splash is in the middle
 * (the "core"), then the ripples spread outward.
 *   1. Find the core: the middle index of the list.
 *   2. Check the core. If it's the target, done.
 *   3. Start with ripple = 1.
 *   4. Check the spot "ripple" steps LEFT of the core, then the spot
 *      "ripple" steps RIGHT of the core (skip spots outside the list).
 *   5. If either one is the target, return that index.
 *   6. Increase ripple by 1 and go back to step 4.
 *   7. If both sides run out of the list, return -1 (not found).
 *
 * Example: [7, 3, 9, 1, 5, 8], looking for 5
 *   core = index 3 (value 1) -> no
 *   ripple 1: left = 9, right = 5 -> found at index 4
 */

class KFCore {

  static int kfCore(int[] data, int target) {
    if (data.length == 0) {
      return -1;
    }

    int core = data.length / 2;

    if (data[core] == target) {
      return core;
    }

    int ripple = 1;
    while (core - ripple >= 0 || core + ripple < data.length) {
      int leftSpot = core - ripple;
      int rightSpot = core + ripple;

      if (leftSpot >= 0 && data[leftSpot] == target) {
        return leftSpot;
      }
      if (rightSpot < data.length && data[rightSpot] == target) {
        return rightSpot;
      }
      ripple++;
    }

    return -1;
  }

  public static void main(String[] args) {
    int[] numbers = {7, 3, 9, 1, 5, 8};

    System.out.println("Search 5:  index " + kfCore(numbers, 5));
    System.out.println("Search 8:  index " + kfCore(numbers, 8));
    System.out.println("Search 7:  index " + kfCore(numbers, 7));
    System.out.println("Search 42: index " + kfCore(numbers, 42));
  }
}

