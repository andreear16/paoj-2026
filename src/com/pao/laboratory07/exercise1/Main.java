package com.pao.laboratory07.exercise1;

import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StareComanda stareCurenta = StareComanda.valueOf(scanner.next());
        Stack<StareComanda> istoric = new Stack<>();

        System.out.println("Initial order state: " + stareCurenta);

        while (scanner.hasNext()) {
            String comanda = scanner.next();

            if (comanda.equals("QUIT")) {
                System.out.println("User quit the program.");
                return;
            }

            if (comanda.equals("undo")) {
                if (istoric.isEmpty()) {
                    System.out.println("Cannot undo the initial order state.");
                } else {
                    stareCurenta = istoric.pop();
                    System.out.println("Order state reverted to: " + stareCurenta);
                }
                continue;
            }

            if (comanda.equals("next")) {
                if (stareCurenta == StareComanda.DELIVERED || stareCurenta == StareComanda.CANCELED) {
                    System.out.println("Order is already in a final state.");
                } else {
                    istoric.push(stareCurenta);

                    if (stareCurenta == StareComanda.PLACED) {
                        stareCurenta = StareComanda.PROCESSED;
                    } else if (stareCurenta == StareComanda.PROCESSED) {
                        stareCurenta = StareComanda.SHIPPED;
                    } else if (stareCurenta == StareComanda.SHIPPED) {
                        stareCurenta = StareComanda.DELIVERED;
                    }

                    System.out.println("Order state updated to: " + stareCurenta);
                }
                continue;
            }

            if (comanda.equals("cancel")) {
                if (stareCurenta == StareComanda.DELIVERED || stareCurenta == StareComanda.CANCELED) {
                    System.out.println("Cannot cancel a final state order.");
                } else {
                    istoric.push(stareCurenta);
                    stareCurenta = StareComanda.CANCELED;
                    System.out.println("Order has been canceled.");
                }
            }
        }
    }
}