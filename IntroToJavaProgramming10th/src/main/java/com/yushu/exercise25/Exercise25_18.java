package com.yushu.exercise25;

import java.io.*;
import java.util.ArrayList;

public class Exercise25_18 {
  /** Global Variables */
  private static final int SIZE = 2 * 128; // Number of ASCII Characters
  private static BufferedInputStream inFile; // Input file stream
  private static ObjectOutputStream outKey; // Object output stream
  private static BitOutputStream outFile; // Bit output stream
  private static int[] charCounts; // Array to hold frequency of characters
  private static String[] charCodes; // Array to hold huffman codes of chars
  private static HuffmanTree huffmanTree; // Huffman Coding Tree for the input

  /** Main Method */
  public static void main(String[] args) {
    /** Check parameters & Get the file names from parameters */
    if ((args.length != 2)) {
      System.out.println("Usage: java Exercise22 input output");
      System.exit(1);
    }

    /**
     * Get the frequencies of characters in the file Create a Huffman Coding
     * Tree for the file Get the Huffman Codes for the characters in the file
     */
    charCounts = getCharacterFrequencyForFile(args[0]);
    huffmanTree = getHuffmanTree(charCounts);
    getHuffmanCode(huffmanTree.root);

    /**
     * Write the Huffman Key to the output file Write the compressed data to the
     * output file
     */
    writeHuffmanKey(args[0], args[1]);
    writeCompressedData(args[0], args[1]);
    System.out.println("Done!");
  }

  /** Write the key to file (Huffman codes as array of strings) */
  public static void writeHuffmanKey(String inputFile, String outputFile) {
    try {
      long originalSize = new File(inputFile).length();
      outKey = new ObjectOutputStream(new FileOutputStream(outputFile));
      outKey.writeLong(originalSize); // Write the original file size
      outKey.writeObject(charCodes); // Write the codes array object

      // Write the key file size to file & Close file
      long keySize = new File(outputFile).length();
      outKey.writeLong(keySize);
      outKey.close();
    } catch (FileNotFoundException ex) {
      System.out.println(inputFile + " file cannot be found in the folder");
    } catch (IOException ex) {
      ex.printStackTrace(System.out);
    }
  }

  /** Read input file & Write compressed data to output file (Append) */
  public static void writeCompressedData(String inputFile, String outputFile) {
    try {
      inFile = new BufferedInputStream(new FileInputStream(inputFile));
      outFile = new BitOutputStream(new File(outputFile), true);
      int p = 0; // Pointer in the input file

      // Write the according Huffman Code for the input to the output
      while ((p = inFile.read()) != -1) {
        outFile.writeBit(charCodes[p]);
      }

      // Close files after reading & Writing
      outFile.close();
      inFile.close();
    } catch (FileNotFoundException ex) {
      System.out.println(inputFile + " file cannot be found in the folder");
    } catch (IOException ex) {
      ex.printStackTrace(System.out);
    }
  }

  /** Return the frequency of characters in a file */
  public static int[] getCharacterFrequencyForFile(String fileName) {
    int[] counts = new int[SIZE];
    int tempChar = 0; // Temp variable to read file char by char

    // Count the occurrences of characters in the text
    try {
      inFile = new BufferedInputStream(new FileInputStream(fileName));
      while ((tempChar = inFile.read()) != -1)
        // Read char by char
        counts[tempChar]++;

      // Close the file after reading
      inFile.close();
    } catch (FileNotFoundException ex) {
      System.out.println(fileName + " file cannot be found in the folder");
      ex.printStackTrace(System.out);
      System.exit(2);
    } catch (IOException ex) {
      ex.printStackTrace(System.out);
    }
    return counts;
  }

  /** Return a Huffman Tree using the character frequencies */
  public static HuffmanTree getHuffmanTree(int[] counts) {
    // Create a heap to hold trees (Listing 24.8 is used from the book)
    Heap<HuffmanTree> heap = new Heap<HuffmanTree>();
    for (int i = 0; i < counts.length; i++) {
      if (counts[i] > 0)
        heap.add(new HuffmanTree(counts[i], (char) i));
    }

    // Remove the two smallest weight trees & Combine those
    while (heap.getSize() > 1) {
      HuffmanTree t1 = heap.remove();
      HuffmanTree t2 = heap.remove();
      heap.add(new HuffmanTree(t1, t2));
    }

    // Return the final tree
    return heap.remove();
  }

  /** Get Huffman Codes for the characters in the Huffman Tree */
  public static void getHuffmanCode(HuffmanTree.Node root) {
    if (root == null)
      return;
    charCodes = new String[SIZE];
    assignCode(root);
  }

  /** Recursively assign codes to the leaf node */
  public static void assignCode(HuffmanTree.Node root) {
    if (root.left != null) {
      root.left.code = root.code + "0";
      assignCode(root.left);

      root.right.code = root.code + "1";
      assignCode(root.right);
    } else
      charCodes[(int) root.element] = root.code;
  }

  /**
   * Heap Class from the book Data Structures & Algorithms
   */

  public static class Heap<E extends Comparable> {
    /** Global Variables */
    private ArrayList<E> list = new ArrayList<E>();

    /** Constructors */
    public Heap() {
    }

    public Heap(E[] objects) {
      for (int i = 0; i < objects.length; i++)
        add(objects[i]);
    }

    /** Add a new object into the heap */
    public void add(E newObject) {
      list.add(newObject);
      int currentIndex = list.size() - 1;

      while (currentIndex > 0) {
        int parentIndex = (currentIndex - 1) / 2;
        // Swap if the current object is greater than its parent
        if (list.get(currentIndex).compareTo(list.get(parentIndex)) > 0) {
          E temp = list.get(currentIndex);
          list.set(currentIndex, list.get(parentIndex));
          list.set(parentIndex, temp);
        } else
          break; // The tree is a heap now

        currentIndex = parentIndex;
      }
    }

    /** Remove the root from the heap */
    public E remove() {
      if (list.size() == 0)
        return null;

      E removedObject = list.get(0);
      list.set(0, list.get(list.size() - 1));
      list.remove(list.size() - 1);

      int currentIndex = 0;
      while (currentIndex < list.size()) {
        int leftChildIndex = 2 * currentIndex + 1;
        int rightChildIndex = 2 * currentIndex + 2;

        // Find the maximum between two children
        if (leftChildIndex >= list.size())
          break; // Now tree is a heap
        int maxIndex = leftChildIndex;
        if (rightChildIndex < list.size()) {
          if (list.get(maxIndex).compareTo(list.get(rightChildIndex)) < 0)
            maxIndex = rightChildIndex;
        }

        // Swap if the current node is less than the maximum
        if (list.get(currentIndex).compareTo(list.get(maxIndex)) < 0) {
          E temp = list.get(maxIndex);
          list.set(maxIndex, list.get(currentIndex));
          list.set(currentIndex, temp);
          currentIndex = maxIndex;
        } else
          break; // Now tree is a heap
      }
      return removedObject;
    }

    /** Get the number of nodes in the tree */
    public int getSize() {
      return list.size();
    }
  }

  /**
   * Huffman Coding Tree from the book Data Structures & Algorithms
   */

  public static class HuffmanTree implements Comparable<HuffmanTree> {
    // Attributes
    Node root; // Root of the tree

    /** Huffman Tree Constructor - Tree with two subtrees */
    public HuffmanTree(HuffmanTree t1, HuffmanTree t2) {
      root = new Node();
      root.left = t1.root;
      root.right = t2.root;
      root.weight = t1.root.weight + t2.root.weight;
    }

    /** Huffman Tree Constructor - Tree containing a leaf node */
    public HuffmanTree(int weight, char element) {
      root = new Node(weight, element);
    }

    @Override /** Compare Huffman Trees based on their weights */
    public int compareTo(HuffmanTree o) {
      if (root.weight < o.root.weight) // In reverse order
        return 1;
      else if (root.weight == o.root.weight)
        return 0;
      else
        return -1;
    }

    /**
     * Inner class Node for storing each Node
     */
    public class Node {
      // Attributes
      char element; // Storing char for a leaf node
      int weight; // Weight of the subtree rooted at this node
      public Node left; // Left subtree
      public Node right; // Right subtree
      String code = ""; // Code of this node from the root

      /** Constrcutors for inner class Node */
      public Node() {
      }

      public Node(int weight, char element) {
        this.weight = weight;
        this.element = element;
      }
    }

  }

  /**
   * BitOutputStream Class (from Exercise in Chapter 19) Data Structures &
   * Algorithms
   */

  public static class BitOutputStream {
    /** Attributes */
    private FileOutputStream output;
    private int value;
    private int count = 0;
    private int mask = 1; // The bits are all zeros except the last one

    /** Constructor */
    public BitOutputStream(File file, boolean append) throws IOException {
      output = new FileOutputStream(file, append);
    }

    /** Overloaded write bit function */
    public void writeBit(char bit) throws IOException {
      count++;
      value = value << 1;

      if (bit == '1')
        value = value | mask;

      if (count == 8) {
        output.write(value);
        count = 0;
      }
    }

    public void writeBit(String bitString) throws IOException {
      for (int i = 0; i < bitString.length(); i++)
        writeBit(bitString.charAt(i));
    }

    /**
     * Write the last byte and close the stream. If the last byte is not full,
     * right-shfit with zeros
     */
    public void close() throws IOException {
      if (count > 0) {
        value = value << (8 - count);
        output.write(value);
      }

      output.close();
    }
  }
}
