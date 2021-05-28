/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validate;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 *
 * @author Nils Danielsen
 */
public class Validate {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String pnr = "198107249289"; // test
        System.out.println(validityCheck(pnr));
    }

    static boolean validityCheck(String str) {
        return luhnValid(str) && dateValid(str);
    }

    static String trim(String str) {
        str = str.replaceAll("[+,-]", "");
        return str;
    }

    static String checkLength(String str) {
        if (str.length() == 12) {
            str = str.substring(2);
        } else if (str.length() < 10) {
            throw new IllegalArgumentException("String too short");
        } else if (str.length() > 10) {
            throw new IllegalArgumentException("String too long");
        }
        return str;
    }

    static String checkIfDigits(String str) {
        if (str.matches("\\d+")) {
            return str;
        }
        throw new IllegalArgumentException("String must contain only digits");
    }

    static boolean luhnValid(String str) {
        
        str = trim(str);
        str = checkLength(str);
        str = checkIfDigits(str);

        int sum = 0;
        boolean isLegit = false;
        for (int i = str.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(str.substring(i, i + 1));
            if (isLegit) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            isLegit = !isLegit;
        }
        System.out.println("4:    " + str);
        return (sum % 10 == 0);
    }

    static boolean dateValid(String str) {
        String year;
        String month;
        String day;

        int thisYear = LocalDate.now().getYear();
        int thisMonth = LocalDate.now().getMonth().getValue();
        int thisDay = LocalDate.now().getDayOfMonth();

        if (str.length() >= 12) {
            year = str.substring(0, 4);
            month = str.substring(4, 6);
            day = str.substring(6, 8);
        } else {
            year = str.substring(0, 2);
            month = str.substring(2, 4);
            day = str.substring(4, 6);
            if (Integer.parseInt(year) > thisYear
                    && Integer.parseInt(month) > thisMonth
                    && Integer.parseInt(day) > thisDay) {
                year = 20 - (str.contains("+") ? 2 : 1) + year;
            } else {
                year = 20 - (str.contains("+") ? 1 : 0) + year;
            }
        }

        // Subtract 60 from day if (day>60)
        if (Integer.parseInt(day) > 60) {
            day = "" + (Integer.parseInt(day) - 60);
        }

        // return true if (month>= 20)
        if (Integer.parseInt(month) >= 20) {
            return true;
        }

        // Test if date is valid
        try {
            String date = year + "-" + month + "-" + day;
            LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;

    }

}
