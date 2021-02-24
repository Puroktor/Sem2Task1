package ru.vsu.cs.skofenko;

import ru.vsu.cs.skofenko.mathInterpreter.Formula;

import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Locale.setDefault(Locale.ROOT);
        Formula f = new Formula();
        tests(f);
        userTest(f);
    }

    private static void userTest(Formula f) {
        while (true) {
            try {
                System.out.println("Введите свою формулу:");
                Scanner in = new Scanner(System.in);
                f.prepare(in.nextLine());

                System.out.println("Введите свои значения:");
                String str = in.nextLine();
                double v;
                if (str.isEmpty()) {
                    v = f.execute();
                } else {
                    v = f.execute(Arrays.stream(str.split(" ")).mapToDouble(Double::parseDouble).toArray());
                }

                System.out.print(v);
                break;
            } catch (Exception e) {
                System.out.println("Попробуйте еще разок!");
            }
        }
    }

    private static void tests(Formula f) {
        f.prepare("2*x*y/3");
        System.out.println(f.execute(2.5, 3));//5

        f.prepare("2*x+y/3");
        System.out.println(f.execute(2.5, 3));//6

        f.prepare("1+2+3-1");
        System.out.println(f.execute());//5

        f.prepare("X+y-z+8.1");
        System.out.println(f.execute(1, 2.5, 3));//8.6

        f.prepare("7*xyz123-8.1");
        System.out.println(f.execute(1));//-1.1

        f.prepare("x*x +y");
        System.out.println(f.execute(2, 5.7));//9.7

        f.prepare("-1+3*x");
        System.out.println(f.execute(1));//2
        System.out.println(f.execute(2));//5
    }
}
