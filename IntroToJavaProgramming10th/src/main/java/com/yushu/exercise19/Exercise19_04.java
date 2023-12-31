package com.yushu.exercise19;

public class Exercise19_04 {
public static <E extends Comparable<E>> void selectionSort(E[] list) {
  for (int i = 1; i < list.length; i++) {
      /** insert list[i] into a sorted sublist list[0..i-1] so that
           list[0..i] is sorted. */
      E currentElement = list[i];
      int k;
      for (k = i - 1; k >= 0 && list[k].compareTo(currentElement) > 0; k--) {
        list[k + 1] = list[k];
      }

      // Insert the current element into list[k+1]
      list[k + 1] = currentElement;
    }
  }
}
