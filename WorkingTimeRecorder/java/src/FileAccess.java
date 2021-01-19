import isel.leic.utils.Time;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;


public class FileAccess {

   public static String File = "USERS.txt";

    public static void load () throws FileNotFoundException {
        Time.sleep(10);
       try{
           BufferedReader rd = new BufferedReader(new FileReader(File));
           Scanner in = new Scanner(rd);

           for(;in.hasNextLine();) {
            String txt = in.nextLine();
            String[] splited = txt.split(";");

            Users.User user =null;
            if(splited.length==5){
                user =new Users.User(Integer.parseInt(splited[0]),Integer.parseInt(splited[1]),splited[2],Integer.parseInt(splited[3]),Long.parseLong(splited[4]));            }
            else{
                user =new Users.User(Integer.parseInt(splited[0]),Integer.parseInt(splited[1]),splited[2],Integer.parseInt(splited[3]));
            }


            Users.userlist.add(user);

        }
        }
        catch(Exception e){
            System.out.println(e);
            System.out.println("File not found");
        }



    }
}
