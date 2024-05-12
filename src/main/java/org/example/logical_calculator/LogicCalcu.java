package org.example.logical_calculator;
import java.util.*;
import java.util.ArrayList;
public class LogicCalcu {
    //Variabel Found Status
    public static boolean PFound, QFound;

    //String buat nyimpen error message
    public static String errorString;
    private static boolean errorFound;

    //ini array jawaban
    private static ArrayList<Character> valueArr = new ArrayList<>();
    //boolean values for lights in output section
    private static boolean tautologiStatus = false;
    private static boolean kontradiksiStatus = false;
    private static boolean kontingensiStatus = false;

    //nyimpen string infix, postfix, prefix
    private static String infix, postfix, prefix;

    public static ArrayList<Character> getValueArr() {
        return valueArr;
    }

    public static boolean isTautologiStatus() {
        return tautologiStatus;
    }

    public static boolean isKontradiksiStatus() {
        return kontradiksiStatus;
    }

    public static boolean isKontingensiStatus() {
        return kontingensiStatus;
    }

    public static boolean isErrorFound() {
        return errorFound;
    }

    public static String getInfix() {
        return infix;
    }

    public static String getPostfix() {
        return postfix;
    }

    public static String getPrefix() {
        return prefix;
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
            case '>', '<' -> 0;
            default -> -1; // IMPLIKASI BIIMPLIKASI
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
        while (operandStack.size() > 1) {
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
        while (operandStack.size() > 1) {
            String operand2 = operandStack.pop();
            String operand1 = operandStack.pop();
            String expression = operand1 + operand2;
            operandStack.push(expression);
        }
        return postfix.append(operandStack.pop()).reverse().toString();
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
//        Scanner sc = new Scanner(System.in);
//        int choice;
//        String input;
//        String postfix;
//        try {
//            while (true) {
//                System.out.println(".:INPUT MENU:.");
//                System.out.println("1. Infix");
//                System.out.println("2. Prefix");
//                System.out.println("3. Postfix");
//                System.out.print("Enter your choice: ");
//                choice = sc.nextInt();
//                if (choice == 0) {
//                    System.exit(0);
//                }
//                sc.nextLine();
//                System.out.print("Input the expression: ");
//                input = sc.nextLine();
////                if(input.contains(" ")){
////                    errorFound = true;
////                    errorString = "Input kosong! Masukkan input!";
//////                    System.out.println(errorString);
////                    break;
////                }
//                if (choice == 1) {
//                    if(!checkInfix(input)){
//                        return;
//                    }
//                    postfix = postfix(input);
//                    String prefix = infixToPrefix(input);
//                    System.out.println("Prefix: " + prefix);
//                    System.out.println("Postfix: " + postfix);
//                    generateCombinations(postfix);
//                } else if (choice == 2) {
//                    postfix = prefixToPostfix(input);
//                    String infix = addParentheses(postfix);
//                    generateCombinations(postfix);
//                    if(errorFound){
//                        break;
//                    } else{
//                        System.out.println("Infix: " + infix);
//                        System.out.println("Postfix: " + postfix);
//                    }
//                } else if (choice == 3) {
//                    postfix = input;
//                    String infix = addParentheses(input);
//                    String prefix = infixToPrefix(infix);
//                    generateCombinations(postfix);
//                    if(errorFound){
//                        break;
//                    } else{
//                        System.out.println("Infix: " + infix);
//                        System.out.println("Prefix: " + prefix);
//                    }
//                }
//            }
//        } catch(EmptyStackException e) {
//            System.out.println("Input ekspresi tidak valid");
//        }
    }
    public static boolean checkInfix(String input){
        boolean valid = true;
        ArrayList<Character> infix = new ArrayList<>();
        //initialize error message
        errorString = "You're Good :)";
        for (int i = 0; i < input.length(); i++) {
            infix.add(input.charAt(i));
        }
        errorFound = hasError(infix);
        if (errorFound) {
            valid = false;
            System.out.println("ma-----------------------------");
        }
        return valid;
    }

    public static void startCalculating(String equation, String formType){
        infix = " ";
        postfix = " ";
        prefix = " ";
        try {
            if (formType.equalsIgnoreCase("infix")) {
                if(!checkInfix(equation)){
                    return;
                }
                postfix = postfix(equation);
                prefix = infixToPrefix(equation);
                System.out.println("Prefix: " + prefix);
                System.out.println("Postfix: " + postfix);
                generateCombinations(postfix);
            } else if (formType.equalsIgnoreCase("prefix")) {
                postfix = prefixToPostfix(equation);
                infix = addParentheses(postfix);
                generateCombinations(postfix);
                if(!errorFound){
                    System.out.println("Infix: " + infix);
                    System.out.println("Postfix: " + postfix);
                }
            } else if (formType.equalsIgnoreCase("postfix")) {
                postfix = equation;
                infix = addParentheses(equation);
                prefix = infixToPrefix(infix);
                generateCombinations(postfix);
                if(!errorFound){
                    System.out.println("Infix: " + infix);
                    System.out.println("Prefix: " + prefix);
                }
            } else {
                System.out.println("Something went went wrong -> startCalculating()[1]");
            }
        }catch(EmptyStackException e){
            errorString = "Pastikan 'input form' sudah benar.";
            errorFound = true;
        }
    }

    public static void generateCombinations(String postfix) {

        if(postfix.isEmpty()){
            errorFound = true;
            errorString = "Input kosong! Masukkan input!";
//                    System.out.println(errorString);
            return;
        }

        ArrayList<Character> input = new ArrayList<>();
        String infix = addParentheses(postfix);
        for (int i = 0; i < infix.length(); i++) {
            input.add(infix.charAt(i));
        }
        //initialize error message
        errorString = "You're Good :)";

        errorFound = hasError(input);
        if (errorFound) {
            System.out.println("ma-----------------------------");
            return;
        }
        // Inisialisasi arraylist untuk menyimpan kombinasi yang unik
        ArrayList<ArrayList<Character>> uniqueCombinations = new ArrayList<>();
        ArrayList<Character> temp = new ArrayList<>(); // buat simpan input dengan tipe data char
        for (int i = 0; i < postfix.toCharArray().length; i++) {
            temp.add(postfix.charAt(i));
        }
        char[] valuesP = {'0', '1'};
        char[] valuesQ = {'0', '1'};
        for (char valueP : valuesP) {
            for (char valueQ : valuesQ) {
                //System.out.println("p = " + valueP + "   q = " + valueQ);
                ArrayList<Character> copyArr = new ArrayList<>();
                for (int i = 0; i < temp.size(); i++) {
                    char value;
                    if (temp.get(i) == 'P') {
                        value = valueP;
                        LogicCalcu.PFound = true;
                    } else if (temp.get(i) == 'Q') {
                        value = valueQ;
                        LogicCalcu.QFound = true;
                    } else if (temp.get(i) == 'T') {
                        value = '1';
                    } else if (temp.get(i) == 'F') {
                        value = '0';
                    } else {
                        value = temp.get(i);
                    }
                    copyArr.add(value);
                }
                if (isUnique(uniqueCombinations, copyArr)) {
                    uniqueCombinations.add(copyArr);
                }
            }
        }

        int count = 0, valueArrSize = 0;
        boolean hasilAkhir = true;
        // Mangambil kombinasi unik dan menghitung hasil evaluasi
        valueArr = new ArrayList<>(); // buat simpan nilai P dan Q, serta hasil operasi
        for (ArrayList<Character> combination : uniqueCombinations) {
            StringBuilder result = new StringBuilder();
            for (char ch : combination) {
                result.append(ch).append(" ");
            }
            Stack<Boolean> evaluationResult = evaluateExpression(result.toString()); // mengoperasikan

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
                if (ch == '0') {
                    hasilAkhir = false;
                }
                valueArrSize++;
            }
            System.out.println();
        }
        // Buat message --> menentukan tautologi, kontradiksi, kontingensi
        if (count == 4 || (valueArrSize <= 3 && hasilAkhir)) {
            System.out.println("Tautologi");
            //below buat UI
            tautologiStatus = true;
            kontradiksiStatus = false;
            kontingensiStatus = false;
        } else if (count == 0) {
            System.out.println("Kontradiksi");
            //below buat UI
            tautologiStatus = false;
            kontradiksiStatus = true;
            kontingensiStatus = false;
        } else {
            System.out.println("Kontingensi");
            //below buat UI
            tautologiStatus = false;
            kontradiksiStatus = false;
            kontingensiStatus = true;

        }
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


    //check for errors
    public static boolean hasError(ArrayList<Character> input) {
        // input kosong
        if (input.isEmpty()) {
            errorString = "Input kosong! Masukkan input";
            return true;
        }

        //input karakter tidak sesuai
        for (char c : input) {
            if (!(c == 'P' || c == 'Q' || c == '&' || c == '|' || c == '>' || c == '<' || c == '~' || c == '(' || c == ')' || c == ' ' || c == 'F' || c == 'T' || c == '1' || c == '0')) {
                errorString = "Invalid character '" + c + "' ";
                return true;
            }
        }

        // cek kurung
        int kurungBuka = 0;
        int kurungTutup = 0;
        for (char c : input) {
            if (c == '(') {
                kurungBuka++;
            } else if (c == ')') {
                kurungTutup++;
            }
        }
        if (kurungBuka != kurungTutup) {
            errorString = "Tanda kurung tidak lengkap/benar";
            return true;
        }

        // cek operand dan operator
        // operator berturut-turut
        for (int i = 0; i < input.size() - 1; i++) {
            char current = input.get(i);
            char next = input.get(i + 1);
            if ((current == '&' || current == '|' || current == '>' || current == '<') &&
                    (next == '&' || next == '|' || next == '>' || next == '<' || next == ')')) {
                errorString = "Operator invalid";
                return true;
            }
        }

        // semuanya variabel, tidak ada operator
        for (int i = 0; i < input.size() - 1; i++) {
            char current = input.get(i);
            char next = input.get(i + 1);
            if ((current == 'P' || current == 'Q' || current == 'T' || current == 'F') && (next == 'P' || next == 'Q' || next == 'T' || next == 'F')) {
                errorString = "Kekurangan operator";
                return true;
            }
        }

        // setelah karakter langsung negasi
        for (int i = 0; i < input.size() - 1; i++) {
            char current = input.get(i);
            char next = input.get(i + 1);
            if ((current == 'P' || current == 'Q' || current == 'F' || current == 'T') &&
                    (next == '~' || next == '(')) {
                errorString = "Operator invalid";
                return true;
            }
        }

        // operator di awal
        char firstChar = input.get(0);
        if (firstChar == '&' || firstChar == '|' || firstChar == '>' || firstChar == '<') {
            errorString = "Operator invalid ";
            return true;
        }

        // operator di akhir
        char lastChar = input.get(input.size() - 1);
        if (lastChar == '&' || lastChar == '|' || lastChar == '>' || lastChar == '<') {
            errorString = "Operator invalid ";
            return true;
        }

        // operator negasi
        for (int i = 0; i < input.size() - 1; i++) {
            char current = input.get(i);
            char next = input.get(i + 1);
            if (current == '~' && (next == '&' || next == '|' || next == '>' || next == '<')) {
                errorString = "Operator invalid";
                return true;
            }
        }

        //double negasi (hilangkan negasinya)
        StringBuilder stringBuilder = new StringBuilder();
        for (Character character : input) {
            stringBuilder.append(character);
        }
        String inputString = stringBuilder.toString();
        while (inputString.contains("~~")) {
            inputString = inputString.replace("~~", "");
        }
        // jumlah negasi ganjil
        if (inputString.equalsIgnoreCase("~")) {
            errorString = "Tidak ada variabel";
            return true;
        }
        // jumlah negasi genap
        if (inputString.isEmpty()) {
            errorString = "Tidak ada variabel";
            return true;
        }
        // Convert the modified String back to ArrayList<Character>
        input.clear();
        char[] charArray = inputString.toCharArray();
        for (char c : charArray) {
            input.add(c);
        }
        System.out.println(input);

        //ERROR : kurung aneh
        Stack<Character> stack = new Stack<>();
        boolean countingInsideBrackets = false;
        int insideCount = 0;
        char prevChar = 0, prevPrevChar = 0;

        for (char x : input) {
            if (x == '(' && !stack.isEmpty()) {
                stack.push(x);
                insideCount = 0;
                continue;
            }
            if (x == ')' && !stack.isEmpty()) {   //CEK SBLM KURUNG TUTUP
                if (insideCount <= 2 && prevChar != 'T' && prevChar != 'F' && prevChar != 'P' && prevChar != 'Q') {
                    errorString = "Operasi dalam kurung tidak lengkap";
                    return true;
                }
                stack.pop();
                insideCount = 0;
                continue;
            }
            if (x == '(') {
                if (insideCount - 1 == 1) {
                    if ((prevChar == '&' || prevChar == '|' || prevChar == '>' || prevChar == '<') && prevPrevChar == ')') {
                        continue;
                    } else if (prevChar == '~') {
                        continue;
                    } else {
                        errorString = "Tanda kurung tidak lengkap/benar";
                        return true;
                    }
                } else if (insideCount - 1 > 1) {
                    if (prevChar == '~' || prevChar == '&' || prevChar == '|' || prevChar == '>' || prevChar == '<') {
                        continue;
                    } else {
                        errorString = "Tanda kurung tidak lengkap/benar";
                        return true;
                    }
                }
                //error klo sebelum kurung '(' adanya bukan operasi, malah variabel dgn nilai true/false
                if (prevChar == 'P' || prevChar == 'Q' || prevChar == 'F' || prevChar == 'T') {
                    errorString = "Kekurangan operator";
                    return true;
                }
                stack.push(x);
                insideCount = 0;
                countingInsideBrackets = true;
            }
            if (countingInsideBrackets) insideCount++; //untuk count inside kurung ada berapa character
            prevPrevChar = prevChar;
            prevChar = x;
        }
        if (!stack.isEmpty()) {
            errorString = "Tanda kurung tidak lengkap/benar";
            return true;
        }

        //error ngecek kesalahan setelah tutup kurung ')'
        if (countingOutsideBrackets(input, kurungTutup)) {
            errorString = "Kekurangan operator";
            return true;
        }
        return false;
    }

    public static boolean countingOutsideBrackets(ArrayList<Character> input, int kurungTutup) {
        boolean counting = false, last = false;
        int count = 0, lastCount = 0;
        char prevChar = 0;

        for (int i = 0; i < input.size(); i++) {
            char x = input.get(i);
            if (last) {
                lastCount++;
                if (lastCount == 1 && input.get(input.size() - 1) == x) {
                    return true;
                } else {
                    //kalau setelah kurung tutup, true skrg di di lastCount yang ke 2 atau lebih,
                    //cek klo yg sebelum karakter di count ke-2 ini, operator atau tidak
                    if (prevChar == 'T' || prevChar == 'F' || prevChar == '~' || prevChar == 'P' || prevChar == 'Q') {
                        return true;
                    }
                }
            }

            if (x == ')') {
                if (count == 1) {
                    return true;
                } else {
                    //ini sama kyk logic sebelumnya, bedanya ini cek posisi tergantung posisinya kurung tutup
                    char test = input.get(i - count);
                    if (test == 'T' || test == 'F' || test == '~' || test == 'P' || test == 'Q') {
                        return true;
                    }
                }
                if (kurungTutup == 1) {
                    last = true;
                }
                kurungTutup--;
                count = 0;
                counting = true;
                prevChar = x;
                continue;
            }
            if (counting) count++;
            prevChar = x;
        }
        return false;
    }
}