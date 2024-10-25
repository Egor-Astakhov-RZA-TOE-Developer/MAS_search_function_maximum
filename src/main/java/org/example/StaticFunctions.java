package org.example;

public class StaticFunctions {
    public static double STEP = 10;
    public static double INITIAL_X = 0.0;
    public static double PRECISION = 1e-3;
    public static double f1(double x){
        return - Math.pow(x, 2) + 5;
    }
    public static double f2(double x){
        return 2 * x + 2;
    }
    public static double f3(double x){
        return Math.sin(x);
    }

}
