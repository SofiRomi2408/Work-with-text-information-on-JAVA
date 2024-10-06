import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class WorkTimeCalculator {

    private Set<DayOfWeek> offDays = new HashSet<>();
    private Set<LocalDate> holidays = new HashSet<>();
    private Set<LocalDate> nonStandartOffDays = new HashSet<>();

    public void CalculateWorkTime(Scanner scanner) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        System.out.print("Enter working hours (HH:MM): ");
        String[] workTime = scanner.nextLine().split(" - ");
        LocalTime startTime = LocalTime.parse(workTime[0], timeFormatter);
        LocalTime endTime = LocalTime.parse(workTime[1], timeFormatter);

        System.out.println("Do you follow a fixed work schedule? (yes/no)");
        String scheduleType = scanner.nextLine().trim().toLowerCase();

        if (scheduleType.equals("yes")) {
            System.out.print("Enter days off (e.g., SATURDAY, SUNDAY): ");
            String[] offDaysInput = scanner.nextLine().split(", ");
            for (String day : offDaysInput) {
                offDays.add(DayOfWeek.valueOf(day.trim().toUpperCase()));
            }

            System.out.print("Enter holidays (format YYYY-MM-DD, separated by commas, if any): ");
            String holidayInput = scanner.nextLine().trim();
            if (!holidayInput.isEmpty()) {
                String[] holidaysInput = holidayInput.split(", ");
                for (String holiday : holidaysInput) {
                    holidays.add(LocalDate.parse(holiday));
                }
            }
        } else if (scheduleType.equals("no")) {
            System.out.println("Enter all off days (format YYYY-MM-DD, separated by commas): ");
            String[] offDaysInput = scanner.nextLine().split(", ");
            for (String offDay : offDaysInput) {
                nonStandartOffDays.add(LocalDate.parse(offDay));
            }
        } else {
            System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            return;
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.print("Enter date range (format YYYY-MM-DD to YYYY-MM-DD): ");
        String[] dateRange = scanner.nextLine().split(" to ");
        LocalDate startDate = LocalDate.parse(dateRange[0], dateFormatter);
        LocalDate endDate = LocalDate.parse(dateRange[1], dateFormatter);

        if (startDate.isAfter(endDate)) {
            System.out.println("Start date must be before the end date.");
            return;
        }

        long totalWorkHours = calculateWorkTime(startDate, endDate, startTime, endTime);
        System.out.println("Total working hours between the two dates: " + totalWorkHours + " hours.");
    }

    public long calculateWorkTime(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        long totalWorkHours = 0;
        LocalDate currentDate = startDate;
        
        while (!currentDate.isAfter(endDate)) {
            DayOfWeek currentDay = currentDate.getDayOfWeek();

            if (offDays.isEmpty()) {
                if (!nonStandartOffDays.contains(currentDate)) {
                    totalWorkHours += calculateDailyWorkHours(currentDate, startTime, endTime);
                }
            } else {
                if (!offDays.contains(currentDay) && !holidays.contains(currentDate)) {
                    totalWorkHours += calculateDailyWorkHours(currentDate, startTime, endTime);
                }
            }

            currentDate = currentDate.plusDays(1);
        }

        return totalWorkHours;
    }

    private long calculateDailyWorkHours(LocalDate date, LocalTime startTime, LocalTime endTime) {
        LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(date, endTime);
        return Duration.between(startDateTime, endDateTime).toHours();
    }
}
