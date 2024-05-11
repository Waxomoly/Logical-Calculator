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
    public static String addParentheses(String expression) {
        StringBuilder result = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c)) {
                result.append(c);
            } else if (c == '(') {
                stack.push(c);
                if (i > 0 && expression.charAt(i - 1) == '(') {
                    // Kurung ganda terdeteksi, tambahkan operator AND ('&') di antara kurung ganda
                    stack.push('&');
                }
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


    public static void main(String[] args) {}
    public static void generateCombinations(ArrayList<Character> input) {
        //initialize error message
        errorString = "You're Good :)";

        errorFound = hasError(input);
        if(errorFound){
            System.out.println("ma-----------------------------");
            return;
        }

        // Inisialisasi arraylist untuk menyimpan kombinasi yang unik
        ArrayList<ArrayList<Character>> uniqueCombinations = new ArrayList<>();
        ArrayList<Character> temp = new ArrayList<>(); // buat simpan input dengan tipe data char
        for (int i = 0; i < input.size(); i++) {
            temp.add(input.get(i));
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
            String withParentheses = addParentheses(result.toString()); // ubah ke postfix
            Stack<Boolean> evaluationResult = evaluateExpression(withParentheses); // mengoperasikan

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
        if(count == 4 || (valueArrSize <= 3 && hasilAkhir)) {
            System.out.println("Tautologi");
            //below buat UI
            tautologiStatus = true;
            kontradiksiStatus = false;
            kontingensiStatus = false;
        } else if(count == 0) {
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
    public static boolean hasError(ArrayList<Character> input){
        // input kosong
        if (input.isEmpty()){
            errorString = "Input kosong! Masukkan input";
            return true;
        }

        //input karakter tidak sesuai
        for (char c : input) {
            if (!(c == 'P' || c == 'Q' || c == '&' || c == '|' || c== '>'|| c =='<'|| c == '~' || c == '(' || c == ')' || c == ' ' || c == 'F' || c == 'T' || c == '1' || c == '0')) {
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
            if ((current == '&' || current == '|' || current == '>' || current == '<'  ) &&
                    (next == '&' || next == '|' || next == '>' || next == '<' || next == ')' )) {
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
            if ((current == 'P' || current == 'Q' || current == 'F' || current == 'T' ) &&
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
                    errorString="Operasi dalam kurung tidak lengkap";
                    return true;
                }
                stack.pop();
                insideCount = 0;
                continue;
            }
            if (x == '(') {
                if (insideCount-1 == 1) {
                    if ((prevChar == '&' || prevChar == '|' || prevChar == '>' || prevChar == '<') && prevPrevChar == ')') {
                        continue;
                    } else if (prevChar == '~') {
                        continue;
                    } else {
                        errorString="Tanda kurung tidak lengkap/benar";
                        return true;
                    }
                } else if (insideCount-1 > 1) {
                    if (prevChar == '~' || prevChar == '&' || prevChar == '|' || prevChar == '>' || prevChar == '<') {
                        continue;
                    } else {
                        errorString="Tanda kurung tidak lengkap/benar";
                        return true;
                    }
                }
                //error klo sebelum kurung '(' adanya bukan operasi, malah variabel dgn nilai true/false
                if (prevChar == 'P' || prevChar == 'Q' || prevChar == 'F' || prevChar == 'T') {
                    errorString="Kekurangan operator";
                    return true;
                }
                stack.push(x);
                insideCount=0;
                countingInsideBrackets = true;
            }
            if (countingInsideBrackets) insideCount++; //untuk count inside kurung ada berapa character
            prevPrevChar = prevChar;
            prevChar = x;
        }
        if (!stack.isEmpty()) {
            errorString="Tanda kurung tidak lengkap/benar";
            return true;
        }

        //error ngecek kesalah setelah tutup kurung ')'
        if (countingOutsideBrackets(input, kurungTutup)) {
            errorString = "Operator invalid";
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
                if (lastCount == 1 && input.get(input.size()-1) == x) {
                    return true;
                } else {
                    //kalau setelah kurung tutup, true skrg di di lastCount yang ke 2 atau lebih,
                    //cek klo yg sebelum karakter di count ke-2 ini, operator atau tidak
                    if (prevChar == 'T' || prevChar == 'F' || prevChar=='~' || prevChar=='P' || prevChar=='Q') {
                        return true;
                    }
                }
            }

            if (x == ')') {
                if (count == 1) {
                    return true;
                } else {
                    //ini sama kyk logic sebelumnya, bedanya ini cek posisi tergantung posisinya kurung tutup
                    char test = input.get(i-count);
                    if (test == 'T' || test == 'F' || test=='~' || test=='P' || test=='Q') {
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