package com.feibai.study.demos.demos.collections.array;

import java.util.Arrays;
import java.util.List;

public class TestArray {

  public static void main(String[] args) {
    Object[] a1 = {1001, "feibai", 18};
    Object[] a2 = {1002, "feibai01", 19};
    Object[] a3 = {1003, "feibai02", 20};

    Object[][] emps = new Object[3][];
    emps[0] = a1;
    emps[1] = a2;
    emps[2] = a3;

    Object[] emps1 = new Object[3];
    emps1[0] = a1;
    emps1[1] = a2;
    emps1[2] = a3;

    List<Integer> list = Arrays.asList(1, 2, 3);

  }

}
