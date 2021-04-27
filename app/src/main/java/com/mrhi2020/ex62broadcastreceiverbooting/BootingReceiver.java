package com.mrhi2020.ex62broadcastreceiverbooting;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class BootingReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //수신한 방송 액션값 얻어오기
        String action= intent.getAction();

        //수신한 방송이 "Boot completed"인지 확인
        //누가 버전(api 25)부터 부팅완료를 들으려면 적어도 앱 설치 후 1번이상
        //사용자가 직접 앱런처화면에서 앱을 실행한 적이 있어야만 함!!! [악성 앱 방지하기 위해]
        if( action.equals(Intent.ACTION_BOOT_COMPLETED) ){
            Toast.makeText(context, "boot received!", Toast.LENGTH_SHORT).show();

            //내 Ex62 앱의 MainActivity 가 자동으로 실행되도록!!!
            // android 10버전(api 28)부터는 리시버에서 직접 액티비티가 실행되는 것을 금지.
            // 리시버에서는 알림(Notification)을 띄우고 이 알림을 사용자가 클릭했을때 액티비티가 실행되도록..
            if(Build.VERSION.SDK_INT>=28){

                NotificationManager notificationManager= (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

                //알림객체를 만들어주는 건축가 객체 참조변수
                NotificationCompat.Builder builder= null;
                //알림채널객체 생성
                NotificationChannel channel= new NotificationChannel("ch1", "channel 01", NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);

                builder= new NotificationCompat.Builder(context, "ch1");

                //건축가에게 원하늘 알림 설정
                builder.setSmallIcon(R.drawable.ic_noti);

                builder.setContentTitle("부팅이 완료되었습니다.~~~");
                builder.setContentText("이제 Ex62앱의 MainActivity를 실행 할 수 있습니다.");
                builder.setSubText("앱 실행");

                builder.setAutoCancel(true);//알림창을 클릭하면 자동 아이콘 없애기

                //알림창 눌렀을때 실행할 MainActivity를 실행시켜주는 Intent객체 생성
                Intent intent1= new Intent(context, MainActivity.class);
                //바로 실행하지 않기에 보류중인(pending) Intent로 변환
                PendingIntent pendingIntent= PendingIntent.getActivity(context, 100, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);

                Notification notification= builder.build();
                notificationManager.notify(1, notification);

            }else{
                Intent intent1= new Intent(context, MainActivity.class);
                //새로운 Activity를 기존 Activity가 아닌 곳에서 실행하려면..
                //새로운 task(새로운 MainThread)에서 실행하도록 해야함..
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            }

        }

    }
}
