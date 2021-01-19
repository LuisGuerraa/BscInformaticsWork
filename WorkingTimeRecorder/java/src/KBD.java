import isel.leic.UsbPort;
import isel.leic.utils.Time;

public class KBD {


    public static final char NONE = 0;
 // private static char [] chars = {'1','4','7','*','2','5','8','0','3','6','9','#','A','B','C','D'};
    private static char [] charsSimul = {'1','2','3','4','5','6','7','8','9','*','0','#'};
    private static char lastchar;
    private static final int KEY = 15; //input mask : 0000 1111
    private static final int D_VAL = 16; //input mask :0001 0000
    private static final int ACK = 128; //output mask : 1000 0000

    public static void init(){
        HAL.clrBits(ACK);

    }
    public static char getKey(){
        if(HAL.isBit(D_VAL)){
            init();
            int keycurrent= HAL.readBits(KEY);
            HAL.setBits(ACK);
            while (HAL.isBit(D_VAL)) ;
            HAL.clrBits(ACK);
            return charsSimul[keycurrent];
        }
        return NONE;

        }



    public static char waitKey(long timeout){
        char k;
        timeout += Time.getTimeInMillis();
        while(Time.getTimeInMillis() < timeout){
            k = getKey();
            if(k != NONE) return k;
        }
        return NONE;
    }

    }




