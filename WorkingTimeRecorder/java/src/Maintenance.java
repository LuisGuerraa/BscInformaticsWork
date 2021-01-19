import java.util.Scanner;

public class Maintenance {

    public static void commands() {

        Scanner in = new Scanner(System.in);
        TUI.clearLCD();
        TUI.setCursor(0, 1);
        TUI.writeLCD("Out of Service");
        TUI.setCursor(1, 5);
        TUI.writeLCD("Wait");
        System.out.println("Turn M key to off, to terminate the maintenance mode.");
        System.out.println("Commands: NEW, DEL, LST, or OFF");


        while (M.MVerification()) {
            String str = in.nextLine();
            System.out.println("Maintenance> ");


            if (str.equals("OFF")) {
                System.exit(0);
            }


            if (str.equals("NEW")) {
                System.out.println("User ? ");
                String name = in.next();
                System.out.println("pass ?");
                int pass = in.nextInt();
                Users.User user = new Users.User(App.generateID(), pass, name, 0);
                Users.userlist.add(user);
                System.out.print("Adding user :" + user.getID() + ":" + user.getName());
                App.updateUserList();

            }

            if (str.equals("DEL")) {
                System.out.println("UIN?");
                int id = in.nextInt();
                System.out.println("Remove user" + id + ":" + Users.getName(id));
                System.out.println("Y/N?");
                if (in.next().equals("Y")) {
                    System.out.println("User " + id + ":" + Users.getName(id) + "removed.");
                    Users.userlist.remove(Users.getUser(id));
                    App.updateUserList();

                } else {
                    System.out.println("Command aborted.");
                }
            }

            if (str.equals("LST")) {
                for (Users.User m : Users.userlist) {
                    if(!m.getInNOut()) System.out.println(m.getID() + "," + m.getName()+"," + m.getEntryHour());

                }
            }

        }

    }
}


