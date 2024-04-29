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
    public static boolean isOperand(char c) {
        return c == 'P' || c == 'Q';
    }
    public static void generateCombinations(String input) {
        // ERROR
        // input kosong
        if (input.isEmpty()){
            System.out.println("Error: Input kosong! Masukkan input");
            return;
        }

        // input hanya spasi
        if (input.trim().isEmpty()) {
            System.out.println("Error: Input hanya spasi! Masukkan input valid");
            return;
        }

        //input karakter tidak sesuai
        for (char c : input.toCharArray()) {
            if (!(c == 'P' || c == 'Q' || c == '&' || c == '|' || c== '>'|| c =='<'|| c == '~' || c == '(' || c == ')' || c == ' ')) {
                System.out.println("Error: Invalid character '" + c + "' ");
                return;
            }
        }

        // cek kurung
        int kurungBuka = 0;
        int kurungTutup = 0;
        for (char c : input.toCharArray()) {
            if (c == '(') {
                kurungBuka++;
            } else if (c == ')') {
                kurungTutup++;
            }
        }
        if (kurungBuka != kurungTutup) {
            System.out.println("Error: Kesalahan jumlah kurung");
            return;
        }

        // cek operand dan operator
        // operator berturut-turut
        for (int i = 0; i < input.length() - 1; i++) {
            char current = input.charAt(i);
            char next = input.charAt(i + 1);
            if ((current == '&' || current == '|' || current == '>' || current == '<' ) &&
                    (next == '&' || next == '|' || next == '>' || next == '<' || next == ')' )) {
                System.out.println("Error: operator invalid");
                return;
            }
        }

        // operator di awal
        char firstChar = input.charAt(0);
        if (firstChar == '&' || firstChar == '|' || firstChar == '>' || firstChar == '<') {
            System.out.println("Error: operator invalid ");
            return;
        }

        // operator di akhir
        char lastChar = input.charAt(input.length() - 1);
        if (lastChar == '&' || lastChar == '|' || lastChar == '>' || lastChar == '<') {
            System.out.println("Error: operator invalid ");
            return;
        }

        // operator negasi
        for (int i = 0; i < input.length() - 1; i++) {
            char current = input.charAt(i);
            char next = input.charAt(i + 1);
            if (current == '~' && (next == '&' || next == '|' || next == '>' || next == '<')) {
                System.out.println("Error: Invalid operand");
                return;
            }
        }

        // double negasi (hilangkan negasinya)
        while (input.contains("~~")) {
            input = input.replace("~~", "");
        }

        //ERROR : kurung aneh
        char[] arrKurung = input.toCharArray();
        Stack<Character> stack = new Stack<>();
        boolean countingInsideBrackets = false;
        int insideCount = 0, outsideCount = 0, firstCount = 0;
        char prevChar = 0, prevPrevChar = 0;

        for (char x : arrKurung) {
            if (countingInsideBrackets) insideCount++; //untuk count inside kurung ada berapa character
            if (x == '(' && !stack.isEmpty()) {
                //ini coba hapus aj
//                if (insideCount < 2 && prevChar != '~' && insideCount!=0) {
//                    System.out.println("Error: Operasi dalam kurung TIDAK lengkap");
//                    return;
//                }
                stack.push(x);
                insideCount = 0;
                continue;
            }
            if (x == ')' && !stack.isEmpty()) {   //CEK SBLM KURUNG TUTUPa
//                if (insideCount <= 2 && prevChar != 'T' && prevChar != 'F' && prevChar != 'P' && prevChar != 'Q') {
//                    System.out.println("Error: Operasi dalam kurung tidak lengkap");
//                    return;
//                }
                stack.pop();
                continue;
            }

            if (x == '(') {
                //error kyk yang di line 175  if (x == '(' && !stack.isEmpty()), tapi ini untuk yang dari awal TIDAK ada kurung buka (arr[1] = '(')
                if (insideCount == 1) {
                    if ((prevChar == '&' || prevChar == '|' || prevChar == '>' || prevChar == '<') && prevPrevChar == ')') {
                        continue;
                    } else if (prevChar == '~') {
                        continue;
                    } else {
                        System.out.println("ERROR");
                        return;
                    }
                } else if (insideCount > 1) {
                    if (prevChar == '~' && (prevPrevChar == '&' || prevPrevChar == '|' || prevPrevChar == '>' || prevPrevChar == '<')) {
                        continue;
                    }
                }
                //error klo sebelum kurung '(' adanya bukan operasi, malah variabel dgn nilai true/false
                if (prevChar == 'P' || prevChar == 'Q' || prevChar == 'F' || prevChar == 'T') {
                    System.out.println("Error: Operasi tidak valid");
                    return;
                }
                stack.push(x);
                countingInsideBrackets = true;
            } else if (x == ')') {
                //cek kurung kosong atau tidak, bisa juga cek kurung yang klo ternyata yang dluan kurung tutup ")"
                if (stack.empty()) {
                    System.out.println("Error: Kurung bermasalah");
                    return;
                }
                //cek di dalam kurung jumlah operasi dan operand tidak kurang dari 2
                if (stack.peek() != '(') {
                    System.out.println("Error: Kurung bermasalah");
                    return;
                } else {
                    stack.pop();
                    insideCount = 0;
                }
            }
            prevPrevChar = prevChar;
            prevChar = x;
        }
        if (!stack.isEmpty()) {
            System.out.println("Error: Kurung bermasalah");
            return;
        }
        if (!countingOutsideBrackets(input, kurungTutup)) {
            System.out.println("Error: Invalid operator");
            return;
        }
        
        //-----------------------------------------------
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

    //cek setelah kurung tutup ada '~' atau isinya cuma 1
    public static boolean countingOutsideBrackets(String input, int kurungTutup) {
        char[] arrKurung = input.toCharArray();
        boolean counting = false, last = false;
        int count = 0, lastCount = 0;
        char prevChar = 0;

        for (int i = 0; i < arrKurung.length; i++) {
            char x = arrKurung[i];
            if (last) {
                lastCount++;
                if (lastCount == 1 && arrKurung[arrKurung.length-1] == x) {
                    return false;
                } else {
                    //kalau setelah kurung tutup, true skrg di di lastCount yang ke 2 atau lebih,
                    //cek klo yg sebelum karakter di count ke-2 ini, operator atau tidak
                    //klo operator, biarin aje. Klo negasi, pasti salah
                    if (prevChar == 'T' || prevChar == 'F' || prevChar=='~' || prevChar=='P' || prevChar=='Q') {
                        return false;
                    }
                }
            }

            if (x == ')') {
                if (count == 1) {
                    return false;
                } else {
                    //ini sama kyk logic sebelumnya, bedanya ini cek posisi tergantung posisinya kurung tutup
                    char test = arrKurung[i-count];
                    if (test == 'T' || test == 'F' || test=='~' || test=='P' || test=='Q') {
                        return false;
                    }
                }
                if (kurungTutup == 1) {
                    last = true;
                }
                kurungTutup--;
                count = 0;
                counting = true;
                continue;
            }
            if (counting) count++;
            prevChar = x;
        }
        return true;
    }
}