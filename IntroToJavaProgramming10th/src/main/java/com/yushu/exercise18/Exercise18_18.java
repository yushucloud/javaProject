package com.yushu.exercise18;

import java.util.Scanner;

public class Exercise18_18 {
  /** Main method */
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    
    // Read number of disks, n
    System.out.print("Enter number of disks: ");
    int n = input.nextInt();
    
    // Find the solution recursively
    System.out.println("The moves are:");
    moveDisks(n, 'A', 'B', 'C');

    System.out.println("The total number of moves is " + count);
  }

  static int count = 0;

  /** The method for finding the solution to move n disks
      from fromTower to toTower with auxTower */
  public static void moveDisks(int n, char fromTower,
      char toTower, char auxTower) {
    count++;
    if (n == 1) // Stopping condition
      System.out.println("Move disk " + n + " from " +
        fromTower + " to " + toTower);
    else {
      moveDisks(n - 1, fromTower, auxTower, toTower);
      System.out.println("Move disk " + n + " from " +
        fromTower + " to " + toTower);
      moveDisks(n - 1, auxTower, toTower, fromTower);
    }
  }
}
