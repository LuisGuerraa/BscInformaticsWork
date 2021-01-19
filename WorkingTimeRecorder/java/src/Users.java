import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Users {

    public static ArrayList<User> userlist  = new ArrayList ();

    public static void loadUsers ()throws FileNotFoundException {
        FileAccess.load();
    }

    public static String searchUser(int id){
        for (User m : userlist){
            if(m.getNumber()==id) return m.getName();
        }
        return null;
    }

    public static int getPassword(int id){
        for (User m : userlist){
            if(m.getNumber()==id) return m.getPass();
        }
        return -1;
    }
    public static String getName(int id){
        for (User m : userlist){
            if(m.getNumber()==id) return m.getName();
        }
        return null;
    }
    public static User getUser(int id){
        for (User m : userlist){
            if(m.getNumber()==id) return m;
        }
        return null;
    }

    public static class User {

        private int identification;
        private int pass;
        private String worker;
        private int timespent;
        private boolean InNOut;
        private long entryHour;
        private String entryDay;


        public User (int ID,int password, String name, int acumulatedTime){
            pass=password;
            worker=name;
            identification=ID;
            timespent=acumulatedTime;
            InNOut = true;
        }

        public User (int ID,int password, String name, int acumulatedTime,long EntryHour){
            pass=password;
            worker=name;
            identification=ID;
            timespent=acumulatedTime;
            InNOut = false;
            entryHour=EntryHour;

        }

        public int getNumber() {
            return identification;
        }

        public String getName() {
            return worker;
        }
        public int getPass(){
            return pass;
        }
        public int getTime(){
            return timespent;

        }

        public void setInNOut(boolean inNOut) {
            InNOut = inNOut;
        }

        public boolean getInNOut(){
            return InNOut;
        }
        public int getID(){
            return identification;
        }

        public void setEntryHour(long entryHour) {
            this.entryHour = entryHour;
        }

        public long getEntryHour() {
            return entryHour;
        }

        public void addTime(long time){
            timespent+=time;
        }

        public void saveParameters(PrintWriter pw) {
            pw.print(identification+";"+pass+";"+worker+";"+timespent+(!getInNOut()? ";" + entryHour :""));
        }

        public String getEntryDay() {
            return entryDay;
        }

        public void setEntryDay(String entryDay) {
            this.entryDay = entryDay;
        }
    }
}
