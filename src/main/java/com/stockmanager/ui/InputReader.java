package com.stockmanager.ui;

import java.util.Scanner;

public class InputReader {
    private Scanner scanner;

    public InputReader() {
        this.scanner = new Scanner(System.in);
    }

    public String readString(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    public int readInt(String message) {
        while (true) {
            System.out.print(message);

            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println("Erreur : veuillez entrer un nombre entier.");
            }
        }
    }

    public double readDouble(String message) {
        while (true) {
            System.out.print(message);

            String input = scanner.nextLine().trim();

            // Permet d'accepter 49,99 au lieu de seulement 49.99
            input = input.replace(",", ".");

            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException exception) {
                System.out.println("Erreur : veuillez entrer un nombre valide.");
            }
        }
    }

    public void close() {
        scanner.close();
    }
}