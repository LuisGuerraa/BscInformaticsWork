import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;

public class Log {

    public static String LOGFile = "LOG.txt";
    public static void Logged (Users.User u) {

       try {
           FileOutputStream f = new FileOutputStream(LOGFile, true);
           PrintWriter pr = new PrintWriter(f);
           LocalDate ld = LocalDate.now();
           LocalTime lt = LocalTime.now();

           pr.write(ld + " " + lt + " " + (u.getInNOut() ? "->" : "<-") + " " + u.getID() + ":" + u.getName());
           pr.println();
           pr.close();
       }
       catch(Exception e){ System.out.println("FIle not found");}

    }



}
