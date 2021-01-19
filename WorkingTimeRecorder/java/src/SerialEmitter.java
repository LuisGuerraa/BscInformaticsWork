import isel.leic.utils.Time;

public class SerialEmitter {

    public enum Destination {DoorMechanism,LCD};
    private static final int SCLK = 0x01; //output
    private static final int SDX = 0x02; //output
    private static final int BUSY_MASK = 0x20;

    public static void init(){
        HAL.setBits(SDX);
        HAL.clrBits(SCLK);
    }


    public static void start(){
        HAL.clrBits(SCLK);
   //     HAL.setBits(SDX);
        HAL.clrBits(SDX);
    }

    public static void send(Destination addr, int data) {
        start();
        int paridade = 0;
        int auxiliar = data;

        if(addr.ordinal() == 1){
            HAL.setBits(SDX);
            paridade++;
        }
        else HAL.clrBits(SDX);

        HAL.setBits(SCLK);
        Time.sleep(1);

        for(int i=0; i<5; i++){

            Time.sleep(1);
            if((auxiliar & 1) == 1) {
                HAL.setBits(SDX);
                paridade++;

            }
            else HAL.clrBits(SDX);
            HAL.clrBits(SCLK);
            auxiliar >>= 1;
            HAL.setBits(SCLK);
            Time.sleep(1);

            }

        if(paridade % 2 != 0)  HAL.setBits(SDX);
        else HAL.clrBits(SDX);

        HAL.clrBits(SCLK);
        Time.sleep(1);

        HAL.setBits(SCLK);
        Time.sleep(1);

        HAL.setBits(SDX);
        HAL.clrBits(SCLK);



    }
    public static boolean isBusy(){
        if (HAL.readBits(BUSY_MASK) == 1 ) return true;
        return false;
    }
}
