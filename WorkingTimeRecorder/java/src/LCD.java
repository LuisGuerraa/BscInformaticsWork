import isel.leic.UsbPort;
import isel.leic.utils.Time;

public class LCD {
        private static final int LINES = 2, COLS = 16;

    private static final int RS_MASK = 0x4;
    private static final int EN_MASK = 0x8;
    private static final int NIBBLE_MASK = 0xF0;

   public static void main(String[] args) {
       SerialEmitter.init();
       LCD.init();

       cursor(0,2);
        write("ola");
        Time.sleep(10000);
        clear();
    }

    private static void writeNibble(boolean rs, int data){

        if(!SerialEmitter.isBusy()) {
            data <<= 1;
            if (rs) data |= 1;
            SerialEmitter.send(SerialEmitter.Destination.LCD, data);
            Time.sleep(10);
        }

        }


        private static void writeByte(boolean rs, int data){
            int aux = data;
            aux>>=4;
            writeNibble(rs,aux);
            Time.sleep(5);
            writeNibble(rs,data);
            Time.sleep(1);
        }

        private static void writeCMD(int data){
            writeByte(false,data);
        }

        private static void writeDATA(int data){
            writeByte(true,data);

        }


        public static void init(){

            Time.sleep(15);
            writeNibble(false,0x03);
            Time.sleep(5);
            writeNibble(false,0x03);
            Time.sleep(1);
            writeNibble(false,0x03);
            Time.sleep(1);
            writeNibble(false,0x02);
            writeCMD(0x028);
            writeCMD(0x08);
            writeCMD(0x01);
            writeCMD(0x06);
            writeCMD(0x0f);

        }

        public static void write (char c){
            writeDATA(c);
        }

        public static void write (String txt){
            for(int i=0; i<txt.length(); i++) writeDATA(txt.charAt(i));
        }

        public static void cursor (int lin , int col){
            int DB7 = 0x80, DB6 = 0x40;
            if(lin>0) writeCMD(DB7+DB6+col);
            else writeCMD(DB7+col);

        }

        public static void clear (){
            writeCMD(0x01);

        }



}
