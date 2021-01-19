import isel.leic.utils.Time;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class App {
    private static Users.User updatedUser;

    public static void main(String[] args) throws FileNotFoundException {
        HAL.init();
        SerialEmitter.init();
        LCD.init();
        TUI.clearLCD();
        Users.loadUsers();
        for (; ; ) {
            TUI.clearLCD();
            TUI.init();
            if (M.MVerification()) Maintenance.commands();
            if (userInput()) {
                routine();
            }

        }
    }

    private static void routine() throws FileNotFoundException {

        TUI.clearLCD();
        TUI.setCursor(0, 5);
        TUI.writeLCD("HELLO");
        TUI.setCursor(1, 3);
        TUI.writeLCD(updatedUser.getName());
        Time.sleep(2000);
        TUI.clearLCD();
        showEntry();
    }

    public static int readID(long timeout) {

        TUI.setCursor(1, 0);
        LCD.write("UIN:??? ");
        TUI.setCursor(1, 4);

        int c = 4;
        int count = 0;
        int aux = 0;

        timeout += Time.getTimeInMillis();



        do {
            int k = TUI.readInteger(timeout);

            if (k >= 0 && k <= 9) {
                TUI.setCursor(1, c++);
                TUI.writeLCD(k + "");
                aux += k;
                aux *= 10;
                ++count;

            }
            if (k == -6) {
                aux = 0;
                count = 0;
                TUI.setCursor(1, 4);
                TUI.writeLCD("??? ");
                c = 4;
                TUI.setCursor(1, c);
            }
            if (M.MVerification()){
                Maintenance.commands();
                return -1;
            }

        } while ((Time.getTimeInMillis() < timeout) && count < 3);

        if (count == 3) return aux / 10;

        else return -1;

    }

    public static int readPass(long timeout) {
        TUI.setCursor(1, 0);
        LCD.write("PIN:????");

        TUI.setCursor(1, 4);


        int c = 4;
        int count = 0;
        int aux = 0;
        timeout += Time.getTimeInMillis();

        do {
            int k = TUI.readInteger(timeout);
            if (k >= 0 && k <= 9) {
                TUI.setCursor(1, c++);
                TUI.writeLCD(  "*");
                aux += k;
                aux *= 10;
                ++count;
            }
            if (k == -6) {
                aux = 0;
                count = 0;
                TUI.setCursor(1, 4);
                TUI.writeLCD("???? ");
                c = 4;
                TUI.setCursor(1, c);
            }


        } while ((Time.getTimeInMillis() < timeout) && count < 4);


        if (count == 4) return aux / 10;
        else return -1;
    }

    public static boolean userInput() {


        int a = readID(15000);
        if(Users.getUser(a)==null)return false;
        int b = readPass(15000);

        int c = 0;


        if (Users.getUser(a) != null) {
            updatedUser = Users.getUser(a);
            c = Users.getPassword(a);
            return c == b;
        }

        return false;


    }

    public static String convertDays(LocalDate day) {
        LocalDateTime d = LocalDateTime.now();
        String dia = getFormattedWeekDay(d.getDayOfWeek());
        return dia;

    }

    public static String convertEntryDay(String day) {
        String d = updatedUser.getEntryDay();
        String daay = "";
        switch (d) {
            case "MONDAY":
                daay = "seg.";
                break;
            case "TUESDAY":
                daay = "ter.";
                break;

            case "WEDNESDAY":
                daay = "qua.";
                break;

            case "THURSDAY":
                daay = "qui.";
                break;

            case "FRIDAY":
                daay = "sex.";
                break;

            case "SATURDAY":

                daay = "sab.";
                break;

            case "SUNDAY":
                daay = "dom.";
                break;

        }
        return daay;
    }

    public static String getFormattedWeekDay(DayOfWeek d) {
        String str = "";
        switch (d) {
            case MONDAY:
                str = "seg.";
                break;
            case TUESDAY:
                str = "ter.";
                break;

            case WEDNESDAY:
                str = "qua.";
                break;

            case THURSDAY:
                str = "qui.";
                break;

            case FRIDAY:
                str = "sex.";
                break;

            case SATURDAY:
                str = "sab.";
                break;

            case SUNDAY:
                str = "dom.";
                break;
        }
        return str;
    }


    public static void showEntry() throws FileNotFoundException {

        if (updatedUser.getInNOut()) {
            updatedUser.setEntryHour(Time.getTimeInMillis());
            updatedUser.setEntryDay(LocalDate.now().getDayOfWeek() + "");
            Log.Logged(updatedUser);
            updatedUser.setInNOut(false);


            TUI.setCursor(0, 0);
            TUI.writeLCD(convertDays(LocalDate.now()) + " " + LocalTime.now().getHour() + ":" + (LocalTime.now().getMinute() < 10 ? "0" + LocalTime.now().getMinute() : LocalTime.now().getMinute()));
            Time.sleep(3000);
            TUI.clearLCD();
            TUI.setCursor(0, 2);
            TUI.writeLCD(updatedUser.getName());
        } else {
            updatedUser.addTime(Time.getTimeInMillis() - updatedUser.getEntryHour());
            Log.Logged(updatedUser);
            updatedUser.setInNOut(true);
            updateUserList();

            TUI.setCursor(0, 0);
            long minutoEntrada = (int)(updatedUser.getEntryHour() / ((1000 * 60)) % 60);                                                    //TimeUnit.MILLISECONDS.toHours(updatedUser.getEntryHour());
            long horaEntrada = (int)(updatedUser.getEntryHour() / ((1000 * 60 * 60)) % 24);                                              //TimeUnit.MILLISECONDS.toMinutes(updatedUser.getEntryHour());
            int minutos = (int) TimeUnit.MILLISECONDS.toMinutes(updatedUser.getTime());
            int horas = (int) TimeUnit.MILLISECONDS.toHours(updatedUser.getTime());

            TUI.writeLCD(convertEntryDay(updatedUser.getEntryDay() + "") + "" + (horaEntrada+1) + ":" + (minutoEntrada < 10 ? "0" + minutoEntrada : minutoEntrada));
            TUI.setCursor(1, 0);
            TUI.writeLCD(convertDays(LocalDate.now()) + LocalTime.now().getHour() + ":" + LocalTime.now().getMinute() + " " + horas + ":" + (minutos < 10 ? "0" + minutos : minutos));
            Time.sleep(3000);
            TUI.clearLCD();
            TUI.setCursor(0, 2);
            TUI.writeLCD(updatedUser.getName());

        }
        openingDoor();
        Time.sleep(2000);
        closingDoor();
        Time.sleep(2000);
        TUI.clearLCD();
        updateUserList();
    }

    public static void openingDoor() throws FileNotFoundException {


        TUI.setCursor(1, 2);
        TUI.writeLCD("Door Opened");
        do Door.open(5);
        while (!Door.isFinished());
        Time.sleep(1000);
        TUI.clearLCD();

    }

    public static void closingDoor() {

        TUI.setCursor(1, 0);
        TUI.writeLCD("Closing Door..");
        do Door.close(5);
        while (!Door.isFinished());
        Time.sleep(1000);
        TUI.clearLCD();
        Time.sleep(1000);


    }

    public static void updateUserList() {
        try {
            PrintWriter pw = new PrintWriter(new File("USERS.TXT"));
            for (Users.User u : Users.userlist) {
                u.saveParameters(pw);
                pw.println();

            }
            pw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static int generateID() {
        int id =(int) (Math.random()*(999-1)+1);

        for (Users.User u :Users.userlist) {
            if(id==u.getID())id =(int) (Math.random()*(999-1)+1);

        }
          return id;
    }

}
