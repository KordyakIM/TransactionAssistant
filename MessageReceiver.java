//класс обрабатывает полученные сообщен
package ru.project.MobileAssistant;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class MessageReceiver extends BroadcastReceiver {

    private static MessageListener mListener;
    String message = "";
    String cache;
    SmsMessage smsMessage;
    @Override
    public void onReceive(Context context, Intent intent) {
//            Bundle data = intent.getExtras();
//            Object[] pdus = (Object[]) data.get("pdus");
//            smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
//            if (smsMessage.getDisplayOriginatingAddress().equals("900")) {
//                for (int i = 0; i < pdus.length; i++) {
//                    smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
//                    cache = smsMessage.getDisplayOriginatingAddress() + "::" + smsMessage.getMessageBody();
//                    if (cache.contains("900::")) {
//                        message += cache.split("::")[1];
//                    }
//                }
//                mListener.messageReceived(message);
//            }
        Bundle data = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
        if (smsMessage.getDisplayOriginatingAddress().equals("900")) {
            for (int i = 0; i < pdus.length; i++) {
                smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    message += smsMessage.getMessageBody();
            }
            mListener.messageReceived(message);
        }
    }

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        if (intent != null && intent.getAction() != null &&
//                ACTION.compareToIgnoreCase(intent.getAction()) == 0) {
////        Bundle data = intent.getExtras();
////        Object[] pdus = (Object[]) data.get("pdus");
//            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
//            smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
//            if (smsMessage.getDisplayOriginatingAddress().equals("900")) {
//                for (int i = 0; i < pdus.length; i++) {
//                    smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
//                    cache = smsMessage.getDisplayOriginatingAddress() + "::" + smsMessage.getMessageBody();
//                    if (cache.contains("900::")) {
//                        message += cache.split("::")[1];
//                    }
//                }
//                mListener.messageReceived(message);
//            }
//        }
//    }
    public static void bindListener(MessageListener listener){
        mListener = listener;
    }
}
