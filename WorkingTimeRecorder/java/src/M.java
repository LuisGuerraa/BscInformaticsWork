public class M {

    private static final int MSignal = 0x40; // input mask

    public static boolean MVerification(){
        return (HAL.isBit(MSignal));

    }
}
