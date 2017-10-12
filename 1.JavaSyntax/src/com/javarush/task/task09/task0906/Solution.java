package com.javarush.task.task09.task0906;

/* 
Логирование стек трейса
*/

public class Solution {
    public static void main(String[] args) {
        log("In main method");
    }

    public static void log(String s) {
        StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();

        System.out.println(stackTraceElements[1].getClassName() + ": " +
                stackTraceElements[1].getMethodName() + ": " + s);
    }
}
