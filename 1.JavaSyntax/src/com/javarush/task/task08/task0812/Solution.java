package com.javarush.task.task08.task0812;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/* 
Cамая длинная последовательность
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<Integer> nums = new ArrayList<>();
        int longestSequence = 1;
        int count = 1;

        for (int i = 0; i < 10; i++) {
            nums.add(Integer.parseInt(reader.readLine()));
            if (i > 0 && nums.get(i - 1).equals(nums.get(i))) {
                count++;
                if (longestSequence < count) {
                    longestSequence = count;
                }
            } else {
                count = 1;
            }
        }
        System.out.println(longestSequence);
    }
}