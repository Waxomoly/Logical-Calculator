package org.example.logical_calculator;

import java.util.ArrayList;
import java.util.Scanner;

public class addKurung {
    //function to see if the number of kurung is equal
    public static int cekKurung(ArrayList<Character> soal) {
        int countBuka = 0, countTutup = 0;

        for (char c : soal) {
            if (c == '(') {
                countBuka++;
            } else if (c == ')') {
                countTutup++;
            }
        }

        if (countTutup == countBuka) {
            return  countTutup;  //cuma perlu angkanya sj
        } else return -1;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter soal : ");
        String soal = sc.nextLine();

//        char[] arr = soal.toCharArray();
        ArrayList<Character> arr = new ArrayList<>();
        for (int i = 0; i < soal.length(); i++) {
            arr.add(soal.charAt(i));
        }
        char[] operator = {'*', '/', '+', '-'};

        //cari jumlah kurung dgn method cekKurung
        int totalKurung = cekKurung(arr);

        int countKurung = 0;
        for (char x : operator) {
            for (int i = 0; i < arr.size(); i++) {
                //cek kurung ke brp yg sdh terlewati yipi
                if (arr.get(i) == '(') {
                    countKurung++;
                }
                if (arr.get(i) == x) {
                    if (i == 1) {   //i = operator's index
                        int targetIndex = i + 2;
                        if (arr.get(i+1) == '(') {
                            countKurung++;
                        }
                        int bukaFound = countKurung;
                        int tutupFound = totalKurung;
                        int insideCount = 0;

                        do {
                            if (tutupFound==1) {
                                while (arr.get(targetIndex)!= ')') {
                                    insideCount++;
                                    targetIndex++;
                                }
                                insideCount+=3;
                                break;
                            }
                            if (arr.get(targetIndex) == ')') {
                                tutupFound--;
                            }
                            targetIndex++;
                            insideCount++;
                        } while (tutupFound >= bukaFound);

                        arr.add(0, '(');  //add buka kurung di blkg operand sebelum operator
                        arr.add(i+insideCount, ')'); //add tutup kurung
                    }


                }
            }
        }
        System.out.println(arr);
    }
}
