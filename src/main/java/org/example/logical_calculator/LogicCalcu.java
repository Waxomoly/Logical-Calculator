package org.example.logical_calculator;

import java.util.*;

public class LogicCalcu {
    public static boolean isOperator(char c) {
        return c == '~' || c == '&' || c == '|' || c == '>' || c == '<';
    }

    public static int precedence(char c) {
        switch (c) {
            case '~':
                return 3; // Negasi
            case '&':
                return 2; // AND
            case '|':
                return 1; // OR
            case '>':
            case '<':
                return 0; // IMPLIKASI BIIMPLIKASI
        }
        return -1;
    }

    public static String addParentheses(String expression) {
        StringBuilder result = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c)) {
                result.append(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    result.append(stack.pop());
                }
                stack.pop(); // Pop '('
            } else if (isOperator(c)) {
                while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek())) {
                    result.append(stack.pop());
                }
                stack.push(c);
            }
        }

        while (!stack.isEmpty()) {
            result.append(stack.pop());
        }

        return result.toString();
    }

    public static ArrayList<Boolean> evaluateExpression(String expression) {
        Stack<Boolean> stack = new Stack<>();
        ArrayList<Boolean> temp = new ArrayList<>();
        for (char c : expression.toCharArray()) {
            if (Character.isDigit(c)) {
                temp.add(stack.push(c == '1'));
            } else if (isOperator(c)) {
                if (c == '~') {
                    temp.add(stack.push(!stack.pop())); // Negasi
                } else {
                    boolean operand2 = stack.pop();
                    boolean operand1 = stack.pop();
                    switch (c) {
                        case '&':
                            temp.add(stack.push(operand1 && operand2)); // AND
                            break;
                        case '|':
                            temp.add(stack.push(operand1 || operand2)); // OR
                            break;
                        case '>':
                            temp.add(stack.push(!operand1 || operand2)); // IMPLIKASI
                            break;
                        case '<':
                            temp.add(stack.push(operand1 == operand2)); // BIIMPLIKASI
                            break;
                    }
                }
            }
        }

        return temp;
    }

    public static boolean evaluatingResult(String expression) {
        Stack<Boolean> stack = new Stack<>();
        for (char c : expression.toCharArray()) {
            if (Character.isDigit(c)) {
                stack.push(c == '1');
            } else if (isOperator(c)) {
                if (c == '~') {
                    stack.push(!stack.pop()); // Negasi
                } else {
                    boolean operand2 = stack.pop();
                    boolean operand1 = stack.pop();
                    switch (c) {
                        case '&':
                            stack.push(operand1 && operand2); // AND
                            break;
                        case '|':
                            stack.push(operand1 || operand2); // OR
                            break;
                        case '>':
                            stack.push(!operand1 || operand2); // IMPLIKASI
                            break;
                        case '<':
                            stack.push(operand1 == operand2); // BIIMPLIKASI
                            break;
                    }
                }
            }
        }

        return stack.pop();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the string:");
        String input = sc.nextLine();
        generateCombinations(input);
    }

    public static void generateCombinations(String input) {
        int totalCombinations = getTotalCombinations(input);

        // Inisialisasi arraylist untuk menyimpan kombinasi yang unik
        ArrayList<ArrayList<Character>> uniqueCombinations = new ArrayList<>();

        // Membangkitkan kombinasi unik
        for (int i = 0; i < totalCombinations; i++) {
            ArrayList<Character> copyArr = new ArrayList<>();
            int index = 0;
            char prevPValue = ' ';
            char prevQValue = ' ';

            for (char ch : input.toCharArray()) {
                if (ch == 'P' || ch == 'Q') {
                    char value;
                    if (ch == 'P') {
                        value = ((i >> index) & 1) == 1 ? '1' : '0';
                        if (prevPValue == ' ') { // Jika nilai P masih kosong
                            prevPValue = value;
                        } else {
                            value = prevPValue; // Masukkan nilai P sebelumnya ke nilai P sekarang
                        }
                    } else {
                        value = ((i >> index) & 1) == 1 ? '1' : '0';
                        if (prevQValue == ' ') {
                            prevQValue = value;
                        } else {
                            value = prevQValue; // Set nilai Q sekarang sama dengan nilai Q sebelumnya
                        }
                    }
                    copyArr.add(value);
                    index++;
                } else {
                    copyArr.add(ch);
                }
            }
            // Tambahkan ke list jika unik
            if (isUnique(uniqueCombinations, copyArr)) {
                uniqueCombinations.add(copyArr);
            }
        }
        boolean cek = true;
        ArrayList<Boolean> evaluationResults = new ArrayList<>();


        // Print kombinasi dan hasilnya
        for (ArrayList<Character> combination : uniqueCombinations) {
            StringBuilder result = new StringBuilder();
            for (char ch : combination) {
                result.append(ch).append(" ");
            }
            String withParentheses = addParentheses(result.toString());
            boolean resultValue = evaluatingResult(withParentheses);
            evaluationResults.add(resultValue);

            for (boolean hasil : evaluateExpression(withParentheses)) {
                if (hasil) {
                    System.out.print(1 + " ");
                } else {
                    System.out.print(0 + " ");
                }
            }
            System.out.println();
        }
        int count = 0;
        for (boolean hasil : evaluationResults) {
            if (!hasil) {
                cek = false;
                count++;
            }
        }
        if (cek) {
            System.out.println("Tautologi");
        } else if (count == 4) {
            System.out.println("Kontradiksi");
        }
    }

    private static int getTotalCombinations(String input) {
        ArrayList<Character> arr = new ArrayList<>();
        int countP = 0;
        int countQ = 0;

        // Isi array dengan input, hitung jumlah variabel p dan q masing"
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (ch == 'P' || ch == 'Q') {
                arr.add(ch);
                if (ch == 'P') {
                    countP++;
                } else {
                    countQ++;
                }
            }
        }

        // Hitung jumlah kombinasi
        int totalCombinations = 1 << (countP + countQ);
        return totalCombinations;
    }

    // Metode untuk memeriksa apakah kombinasi tersebut unik
    public static boolean isUnique(ArrayList<ArrayList<Character>> combinations, ArrayList<Character> combination) {
        for (ArrayList<Character> existingCombination : combinations) {
            if (existingCombination.equals(combination)) {
                return false;
            }
        }
        return true;
    }
}
