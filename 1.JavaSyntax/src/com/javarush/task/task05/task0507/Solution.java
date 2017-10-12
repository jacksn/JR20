package com.javarush.task.task05.task0507;

/* 
Среднее арифметическое
*/

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Solution {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int count = 0;
        int sum = 0;
        int n;
        String s;
        while (true) {
            s = reader.readLine();
            n = Integer.parseInt(s);
            if (n == -1) break;
            sum += n;
            count++;
        }
        System.out.println((double)sum / count);
    }
}

