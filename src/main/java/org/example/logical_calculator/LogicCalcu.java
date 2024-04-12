package org.example.logical_calculator;

import java.util.*;

public class LogicCalcu {
    public static boolean isOperator(char c) {
        return c == '~' || c == '&' || c == '|' || c == '>' || c == '<';
    }

    public static int precedence(char c) {
        return switch (c) {
            case '~' -> 3; // Negasi
            case '&' -> 2; // AND
            case '|' -> 1; // OR
            case '>', '<' -> 0;
            default -> -1; // IMPLIKASI BIIMPLIKASI
        };
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
    // buat hitung hasil

    public static Stack<Boolean> evaluateExpression(String expression) {
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
                        case '&' -> stack.push(operand1 && operand2); // AND
                        case '|' -> stack.push(operand1 || operand2); // OR
                        case '>' -> stack.push(!operand1 || operand2); // IMPLIKASI
                        case '<' -> stack.push(operand1 == operand2); // BIIMPLIKASI
                    }
                }
            }
        }
        return stack;
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
        ArrayList<Character> temp = new ArrayList<>(); // buat simpan input dengan tipe data char
        for (int i = 0; i < input.toCharArray().length; i++) {
            temp.add(input.charAt(i));
        }

        // Membangkitkan kombinasi unik
        for (int i = 0; i < totalCombinations; i++) {
            ArrayList<Character> copyArr = new ArrayList<>();
            int index = 0;
            char prevPValue = ' ';
            char prevQValue = ' ';

            for (char ch : temp) {
                    char value;
                    if (ch == 'P') {
                        value = ((i >> index) & 1) == 1 ? '1' : '0';
                        if (prevPValue == ' ') { // Jika nilai P masih kosong
                            prevPValue = value;
                        } else {
                            value = prevPValue; // Masukkan nilai P sebelumnya ke nilai P sekarang
                        }
                    } else if(ch == 'Q') {
                        value = ((i >> index) & 1) == 1 ? '1' : '0';
                        if (prevQValue == ' ') {
                            prevQValue = value;
                        } else {
                            value = prevQValue; // Set nilai Q sekarang sama dengan nilai Q sebelumnya
                        }
                    } else if(ch == 'T') {
                        value = '1';
                    } else if(ch == 'F'){
                        value = '0';
                    } else {
                        value = ch;
                    }
                copyArr.add(value);
                index++;
            }
            // Tambahkan ke list jika unik
            if (isUnique(uniqueCombinations, copyArr)) {
                uniqueCombinations.add(copyArr);
            }
        }
        int count = 0;
        // Mangambil kombinasi unik dan menghitung hasil evaluasi
        for (ArrayList<Character> combination : uniqueCombinations) {
            StringBuilder result = new StringBuilder();
            for (char ch : combination) {
                result.append(ch).append(" ");
            }
            String withParentheses = addParentheses(result.toString()); // ubah ke postfix
            Stack<Boolean> evaluationResult = evaluateExpression(withParentheses); // mengoperasikan
            ArrayList<Character> valueArr = new ArrayList<>(); // buat simpan nilai P dan Q, serta hasil operasi

            int countP = -1;
            int countQ = -1;
            int countT = -1;
            int countF = -1;
            // ambil dan simpan nilai P dan Q hasil generate kombinasi, namun hanya menyimpan satu value saja per variabel (pasti sama)
            for (int i = 0; i < temp.size(); i++) {
                for (int j = 0; j < combination.size(); j++) {
                    if (countP == -1) {
                        if (i == j & (temp.get(i) == 'P')) {
                            valueArr.add(combination.get(i));
                            countP++;
                        }
                    }
                    if (countQ == -1) {
                        if (i == j & (temp.get(i) == 'Q')) {
                            valueArr.add(combination.get(i));
                            countQ++;
                        }
                    }
                    if (countT == -1){
                        if(i == j & (temp.get(i) == 'T')){
                            valueArr.add(combination.get(i));
                            countT++;
                        }
                    }
                    if (countF == -1){
                        if(i == j & (temp.get(i) == 'F')){
                            valueArr.add(combination.get(i));
                            countF++;
                        }
                    }
                }
            }
            // simpan hasil operasi ke valueArr
            while (!evaluationResult.isEmpty()) {
                boolean hasil = evaluationResult.pop();
                if (hasil) {
                    valueArr.add('1');
                    count++;
                } else {
                    valueArr.add('0');
                }
            }
            // coba print

            for (char ch : valueArr) {
                System.out.print(ch + " ");
            }
            System.out.println();
        }
        // menentukan tautologi, kontradiksi, kontingensi
            if(count == 4) {
                System.out.println("Tautologi");
            } else if(count == 0) {
                System.out.println("Kontradiksi");
            } else {
                System.out.println("Kontingensi");
            }
    }

    private static int getTotalCombinations(String input) {
        int countP = 0;
        int countQ = 0;

        // Hitung jumlah variabel P dan Q
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (ch == 'P') {
                countP++;
            } else if (ch == 'Q') {
                countQ++;
            }
        }

        // Hitung jumlah kombinasi
        return 1 << (countP + countQ);
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

