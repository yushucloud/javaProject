package com.yushu.exercise27;

import java.util.*;

public class Exercise27_11 {
  public static void main(String[] args) {

  }
  
  public static <E> ArrayList<E> setToList(Set<E> s) {
    ArrayList<E> list = new ArrayList<>();
    for (E e: s) {
      list.add(e);
    }
    return list;
  }
}