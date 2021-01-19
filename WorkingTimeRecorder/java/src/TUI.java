import isel.leic.utils.Time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TUI {
    public static int readInteger(long timeout){

        char c =KBD.waitKey(timeout);
        return c-'0';

    }
    public static void writeLCD(String txt){
        LCD.write(txt);

    }
    public static void writeIntLcd(int i){
        LCD.write(""+i);

    }

    public static String time (){
       String time = "";

          if (LocalDateTime.now().getMinute() <= 9) {
              time = "" + LocalDate.now().getDayOfMonth() + "/" + LocalDate.now().getMonthValue() + "/" + LocalDate.now().getYear() + " " + LocalDateTime.now().getHour() + ":" + "0" + LocalDateTime.now().getMinute();
          } else
              time = "" + LocalDate.now().getDayOfMonth() + "/" + LocalDate.now().getMonthValue() + "/" + LocalDate.now().getYear() + " " + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute();
          return time;

   }
    public static void init(){
        LCD.cursor(0,0);
        LCD.write(TUI.time());


    }

    public static void setCursor (int l, int c ){
        LCD.cursor(l,c);
    }

    public static void openDoor(){
        Door.open(15);
    }

    public static void closeDoor(){
        Door.close(15);
    }
    public static void clearLCD(){
        LCD.clear();
    }
}
