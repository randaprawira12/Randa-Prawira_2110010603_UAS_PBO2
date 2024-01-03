package util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JLabel;

/**
 *
 * @author Randa
 */
public class JamDigital {
    
    public static void getJam(JLabel label){
        new Thread(){
            @Override
            public void run(){
                int waktu_mulai = 0;
                while (waktu_mulai == 0){
                Calendar kalender = new GregorianCalendar();
                int jam = kalender.get(Calendar.HOUR);
                int menit = kalender.get(Calendar.MINUTE);
                int detik = kalender.get(Calendar.SECOND);
                int AM_PM = kalender.get(Calendar.AM_PM);
                String siang_malam = "";
                if (AM_PM == 1){
                    siang_malam = "PM";
                }
                else {
                    siang_malam = "AM";
                }
                String time = jam+" : "+menit+" : "+detik+" "+siang_malam;
                label.setText(time);
                }
            }
        }.start();
    }
    
}
