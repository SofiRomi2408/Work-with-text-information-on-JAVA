import java.io.BufferedReader;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        WorkTimeCalculator calculator = new WorkTimeCalculator();

        int choice;

        do {
            System.out.println("1. Calculate working hours");
            System.out.println("2. Extract sentences containing dates");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();  // Очищення буфера

            switch (choice) {
                case 1:
                    calculator.CalculateWorkTime(scanner);
                    break;
                case 2:
                    findSentencesWithDates();
                    break;
                case 3:
                    System.out.println("Exiting... Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 3);

        scanner.close();
    }

    private static void findSentencesWithDates() {
        String filePath = "textWithDates.txt"; 
        StringBuilder text = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line).append(" "); 
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
            return;
        }

        String regex = "\\b[^.?!]*\\d{4}\\.\\d{1,2}\\.\\d{1,2}[^.?!]*[.?!]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        System.out.println("Sentences containing dates in the format YYYY.MM.DD: \n");
        while (matcher.find()) {
            String sentence = matcher.group().trim();
            if (isValidDate(sentence)) {
                System.out.println(sentence);
            }
        }
    }


    private static boolean isValidDate(String sentence) {
     
        String dateRegex = "(\\d{4})\\.(\\d{1,2})\\.(\\d{1,2})";
        Pattern datePattern = Pattern.compile(dateRegex);
        Matcher dateMatcher = datePattern.matcher(sentence);
        
        while (dateMatcher.find()) {
            int year = Integer.parseInt(dateMatcher.group(1));
            int month = Integer.parseInt(dateMatcher.group(2));
            int day = Integer.parseInt(dateMatcher.group(3));

           
            if (month < 1 || month > 12) {
                return false; 
            }
            if (day < 1 || day > 31) {
                return false;
            }
            if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
                return false;
            }
            if (month == 2) {
                if (day > 29 || (day == 29 && !isLeapYear(year))) {
                    return false; 
                }
            }
        }

        return true; 
        }
    

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }


}


