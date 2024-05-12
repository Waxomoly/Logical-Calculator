package org.example.logical_calculator;

import java.util.*;

public class ExpressionLogicCalcu {
    //String buat nyimpen error message
    private static boolean errorFound = false;

    public static boolean isErrorFound() {
        return errorFound;
    }

    public static void setErrorFound(boolean errorFound) {
        ExpressionLogicCalcu.errorFound = errorFound;
    }


    // Pengecekan apakah sebuah operator
    public static boolean isOperator(char c) {
        return c == '~' || c == '&' || c == '|' || c == '>' || c == '<';
    }

    public static int precedence(char c) {
        return switch (c) {
            case '~' -> 3; // Negasi
            case '&' -> 2; // AND
            case '|' -> 1; // OR
            case '>', '<' -> 0; //biimpli, impli
            default -> -1; // operand
        };
    }

    public static String postfix(String expression) {
        StringBuilder result = new StringBuilder(); // buat simpan hasil postfix
        Stack<Character> stack = new Stack<>(); // buat simpan operator sementara

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isLetter(c)) { //OPERAND
                result.append(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') { // pop + append ke result sampai ketemu '('
                    result.append(stack.pop());
                }
                stack.pop(); // Pop '('
            } else if (isOperator(c)) {
                while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek())) { // pop operator dengan precedence lebih tinggi/ sama
                    result.append(stack.pop());
                }
                stack.push(c); // masukkan operator saat ini ke stack jika precedence c lebih kecil dari precedence elemen teratas stack
            }
        }
        while (!stack.isEmpty()) {
            result.append(stack.pop());  // pop semua operator yang tersisa dari stack dan tambahkan ke result
        }
        return result.toString();
    }

    // prefix
    public static String infixToPrefix(String infixExpression) {
        StringBuilder prefixExpression = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        // Balik ekspresi infix - > supaya stack bisa bekerja dari kiri ke kanan (cara kerja stack yg terakhir msk, di pop pertama)
        StringBuilder reversedInfix = new StringBuilder(infixExpression).reverse();

        for (int i = 0; i < reversedInfix.length(); i++) {
            char c = reversedInfix.charAt(i);

            if (Character.isLetter(c)) {
                // karakter adalah huruf -> tambahkan ke ekspresi prefix
                prefixExpression.append(c);
            } else if (c == ')') {
                // karakter adalah ')' -> tambahkan ke stack
                stack.push(c);
            } else if (c == '(') {
                while (!stack.isEmpty() && stack.peek() != ')') { // pop dan append ke result sampai ketemu '('
                    prefixExpression.append(stack.pop());
                }
                stack.pop(); // Pop '('

            } else if (isOperator(c)) {
                // karakter adalah operator -> tambahkan operator dengan precedence lebih rendah ke ekspresi prefix
                while (!stack.isEmpty() && precedence(stack.peek()) > precedence(c)) {
                    prefixExpression.append(stack.pop());
                }
                stack.push(c); // jika precedence c lebih tinggi, maka akan dimasukkan ke stack
            }
        }

        // Pop sisa operator dari stack dan tambahkan ke ekspresi prefix
        while (!stack.isEmpty()) {
            prefixExpression.append(stack.pop());
        }

        // Balik ekspresi prefix untuk mendapatkan hasil akhir
        return prefixExpression.reverse().toString();
    }

    public static String addParentheses(String postfix) {
        Stack<String> operandStack = new Stack<>();

        for (char c : postfix.toCharArray()) {
            if (Character.isLetter(c)) { // kalau P atau Q atau T atau F, mka dimasukkan dalam stack
                operandStack.push(String.valueOf(c));
            } else if (isOperator(c)) { // jika sebuah operator, pop elemen teratas dalam stack sebagai operand 2
                String operand2 = operandStack.pop();
                if (c == '~') { // untuk negasi karena negasi hanya butuh satu operand
                    String expression = "(" + c + operand2 + ")";
                    operandStack.push(expression); // setelah diubah dengan menambahkan kurung buka dan kurung tutup yang sesuai,
                    // maka dimasukkan kembali ke stack
                } else { // untuk yang bukan sebuah negasi
                    String operand1 = operandStack.pop(); // mengambil stack teratas kembali sebagai operand 1
                    String expression = "(" + operand1 + c + operand2 + ")"; // menggabungkan kedua operand dengan sebuah operator sebagai operand yang baru
                    // ,serta menambahkan kurung buka dan tutup
                    operandStack.push(expression); //masukkan kembali operand baru ke dalam stack
                }
            }
        }
        // BUAT UBAH KE INFIX YANG BENAR kalau input kurang operator
        while(operandStack.size() > 1){
            String operand2 = operandStack.pop();
            String operand1 = operandStack.pop();
            String expression = operand1 + operand2;
            operandStack.push(expression);
        }
        return operandStack.pop();
    }

    public static String prefixToPostfix(String prefix) {
        StringBuilder reversed = new StringBuilder(prefix).reverse(); // buat bisa baca dari kanan ke kiri
        Stack<String> operandStack = new Stack<>(); // stack buat simpan operand String
        StringBuilder postfix = new StringBuilder(); // buat simpan hasil dari stack, supaya bisa di reverse lagi

        for (int i = 0; i < reversed.length(); i++) {
            char c = reversed.charAt(i);
            if (Character.isLetter(c)) { // kalau P atau Q atau T atau F, maka dimasukkan dalam stack
                operandStack.push(String.valueOf(c));
            } else if (isOperator(c)) { // jika sebuah operator, pop elemen teratas dalam stack sebagai operand 2
                String operand2 = operandStack.pop();
                if (c == '~') { // untuk negasi karena negasi hanya butuh satu operand
                    String expression = c + operand2;
                    operandStack.push(expression); // setelah diubah dengan menambahkan kurung buka dan kurung tutup yang sesuai,
                    // maka dimasukkan kembali ke stack
                } else { // untuk yang bukan sebuah negasi
                    String operand1 = operandStack.pop(); // mengambil stack teratas kembali sebagai operand 1
                    String expression = c + operand1 + operand2; // menggabungkan kedua operand dengan sebuah operator sebagai operand yang baru
                    operandStack.push(expression); //masukkan kembali operand baru ke dalam stack
                }
            }
        }
        // BUAT UBAH KE POSTFIX YANG BENAR kalau input kurang operator
        while(operandStack.size() > 1){
            String operand2 = operandStack.pop();
            String operand1 = operandStack.pop();
            String expression = operand1 + operand2;
            operandStack.push(expression);
        }
        return postfix.append(operandStack.pop()).reverse().toString();
    }

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



    // Metode untuk memeriksa apakah kombinasi tersebut unik
    public static boolean isUnique(ArrayList<ArrayList<Character>> combinations, ArrayList<Character> combination) {
        for (ArrayList<Character> existingCombination : combinations) {
            if (existingCombination.equals(combination)) {
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;
        String input;
        String postfix = "";
        try {
            while (true) {
                System.out.println(".:INPUT MENU:.");
                System.out.println("1. Infix");
                System.out.println("2. Prefix");
                System.out.println("3. Postfix");
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();
                if (choice == 0) {
                    System.exit(0);
                }
                sc.nextLine();
                System.out.print("Input the expression: ");
                input = sc.nextLine();

                if (choice == 1) {
                    postfix = postfix(input);
                    String prefix = infixToPrefix(input);
                    System.out.println("Prefix: " + prefix);
                    System.out.println("postfix: " + postfix);
                } else if (choice == 2) {
                    postfix = prefixToPostfix(input);
                    String infix = addParentheses(postfix);
                    System.out.println("Infix: " + infix);
                    System.out.println("Postfix: " + postfix);
                } else if (choice == 3) {
                    postfix = input;
                    String infix = addParentheses(input);
                    System.out.println("Infix: " + infix);
                    String prefix = infixToPrefix(infix);
                    System.out.println("Prefix: " + prefix);
                }
                //generateCombinations(postfix);
            }
        } catch(EmptyStackException e) {
            System.out.println("Input ekspresi tidak valid");
        } catch(InputMismatchException e){
            System.out.println("Input ekspresi tidak valid");
        }
    }


}
