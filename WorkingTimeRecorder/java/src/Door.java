public class Door {
    private static boolean end;

    public static void init(){
        end=false;

    }

    public static void open (int speed){
        speed <<= 1;
        speed |= 1;
        SerialEmitter.send(SerialEmitter.Destination.DoorMechanism,speed);
        if(isFinished()) end=true;

    }

    public static void close (int speed){
        speed <<=1;
        SerialEmitter.send(SerialEmitter.Destination.DoorMechanism,speed);
        if(isFinished()) end =true;
    }

    public static boolean isFinished(){
        return (!SerialEmitter.isBusy());


    }


}
