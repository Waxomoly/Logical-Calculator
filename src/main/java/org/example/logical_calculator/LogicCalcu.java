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

        public static boolean evaluateExpression(String expression) {
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
                            case '=':
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
            ArrayList<Character> arr = new ArrayList<>();
            int countP = 0;
            int countQ = 0;

            // isi array dengan input, hitung jumlah variabel p dan q masing"
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

            // Inisiaslisais arraylist untuk simpan kombinasi yang unik
            ArrayList<ArrayList<Character>> uniqueCombinations = new ArrayList<>();

            // Generating kombinasi Unik
            for (int i = 0; i < totalCombinations; i++) {
                ArrayList<Character> copyArr = new ArrayList<>();
                int index = 0;
//            boolean prevPisZero = false;
//            boolean prevQisZero = false;
                char prevPValue = ' ';
                char prevQValue = ' ';

                for (char ch : input.toCharArray()) {
                    if (ch == 'P' || ch == 'Q') {
                        char value;
                        if (ch == 'P') {
//                        if (prevPisZero) {
//                            value = '0'; // Cek kalau nilai P sebelumnya = 0, maka P sekarang diberi nilai 0
//                        } else {
                            value = ((i >> index) & 1) == 1 ? '1' : '0';
                            if (prevPValue == ' ') { // kalau nilai P masih kosong
                                prevPValue = value;
                            } else {
                                value = prevPValue; // Masukkan nilai P sebelumnya ke nilai P sekarang
                            }
//                            if (value == '0') {
//                                prevPisZero = true;
//                            }
                        } else {
//                        if (prevQisZero) {
//                            value = '0'; // If previous Q is 0, set current Q to 0
//                        } else {
                            value = ((i >> index) & 1) == 1 ? '1' : '0';
                            if (prevQValue == ' ') {
                                prevQValue = value;
                            } else {
                                value = prevQValue; // Set current Q value same as previous Q value
                            }
//                            if (value == '0') {
//                                prevQisZero = true;
//                            }
                        }
//                    }
                        copyArr.add(value);
                        index++;
                    } else {
                        copyArr.add(ch);
                    }
                }
                // tambahin ke list kalau unik
                if (isUnique(uniqueCombinations, copyArr)) {
                    uniqueCombinations.add(copyArr);
                }
            }

            boolean cek = true;
            ArrayList<Boolean> evaluationResults = new ArrayList<>();

            // print kombinasi sama hasil
            for (ArrayList<Character> combination : uniqueCombinations) {
                StringBuilder result = new StringBuilder();
                for (char ch : combination) {
                    result.append(ch).append(" ");
                }
                String withParentheses = addParentheses(result.toString());
                boolean resultValue = evaluateExpression(withParentheses);
                System.out.println(withParentheses + " => " + (resultValue ? " 1(true)" : "0(false)"));
                evaluationResults.add(resultValue);
            }
            int count = 0;
            for (boolean result: evaluationResults) {
                if (!result) {
                    cek = false;
                    count++;
                }
            }
            if(cek){
                System.out.println("Tautologi");
            } else if(count == 4){
                System.out.println("Kontradiksi");
            }

        }

        // method untuk cek apakah kombinasinya unik
        public static boolean isUnique(ArrayList<ArrayList<Character>> combinations, ArrayList<Character> combination) {
            for (ArrayList<Character> existingCombination : combinations) {
                if (existingCombination.equals(combination)) {
                    return false;
                }
            }
            return true;
        }
}
