package com.javarush.task.task07.task0718;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/* 
Проверка на упорядоченность
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> strings = new ArrayList<>();
        int index = -1;
        for (int i = 0; i < 10; i++) {
            strings.add(reader.readLine());
            if (i > 0
                    && index < 0
                    && (strings.get(i).length() - strings.get(i - 1).length() < 0)) {
                index = i;
            }
        }

        if (index > 0) {
            System.out.println(index);
        }
    }
}

