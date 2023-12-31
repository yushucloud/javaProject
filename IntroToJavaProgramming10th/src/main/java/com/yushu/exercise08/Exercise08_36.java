package com.yushu.exercise08;

import java.util.Arrays;
import java.util.Scanner;

public class Exercise08_36 {
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    System.out.print("Enter number n: ");
    int n = input.nextInt();

    // Create letters
    char[] letters = new char[n];
    for (int i = 0; i < n; i++)
      letters[i] = (char)('A' + i);
       
    // Enter matrix
    char[][] matrix = new char[n][n];
    System.out.println("Enter " + n + " rows of letters separated by spaces:  ");
    for (int i = 0; i < n; i++) {
      String s = input.nextLine();
      String[] items = s.split("[ .+]");
      System.out.println(s);
      System.out.println(items.length);
      if (items.length != n) {
        System.out.println("Wrong input: you need to enter exactly " + n + " letters");
        System.exit(1);
      }
      
      for (int j = 0; j < items.length; j++) {
        if (items[j].length() != 1) {
          System.out.println("Wrong input: you need to enter single letters");
          System.exit(2);
        }
        
        matrix[i][j] = items[j].charAt(0);
      }
      
      char[] clonedRow = matrix[i].clone();
      Arrays.sort(clonedRow);
      if (!Arrays.equals(clonedRow, letters)) {
        System.out.println("Wrong input: the letters must be from " + letters[0] 
          + " to " + letters[letters.length - 1]);
      }
    }

    char[][] transposedMatrix = getTransposed(matrix);
        
    for (int i = 0; i < matrix.length; i++) {
      Arrays.sort(transposedMatrix[i]);
      if (!Arrays.equals(letters, transposedMatrix[i])) {
        System.out.println("The input array is not a Latin square");
        System.exit(3);
      }
    }
    
    System.out.println("The input array is a Latin square");
  }
  
  public static char[][] getTransposed(char[][] matrix) {
    char[][] result = new char[matrix.length][matrix.length];
    
    for (int j = 0; j < matrix.length; j++) {
      for (int i = 0; i < matrix.length; i++) {
        result[i][j] = matrix[j][i];
      }
    }
    
    return result;
  }
}
