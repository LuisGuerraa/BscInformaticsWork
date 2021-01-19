import isel.leic.UsbPort;


public class HAL {
   private static  int lastout;
    public static void init(){
         UsbPort.out(~lastout);
         }
         public static boolean isBit(int mask){
            return (mask &in())!=0;

        }
        public static int readBits(int mask){
            return in()& mask;
            }

        public static void writeBits (int mask , int value){
           lastout=((~mask)&lastout)|(mask&value);
           out(lastout);


        }

        public static void setBits(int mask){
            lastout = lastout|mask;
            UsbPort.out(~lastout);
            }
            private static int in (){
                return ~UsbPort.in();

            }
            private static void out (int out){UsbPort.out(~out);}


        public static void clrBits(int mask){
            lastout=lastout&(~mask);
            UsbPort.out(~lastout);


        }

}
