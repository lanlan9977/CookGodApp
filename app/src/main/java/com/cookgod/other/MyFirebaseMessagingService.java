package com.cookgod.other;

public class MyFirebaseMessagingService  {

//    private static final String TAG = "MyFirebaseMsgService";
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//    }
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//        Log.i("Service", "onMessageReceived: " + remoteMessage.getFrom());
//        if (remoteMessage.getData().size() > 0) {
//            Log.i("Service", "" + remoteMessage.getData());
//        }
//        if (remoteMessage.getNotification() != null) {
//            Log.i("Service", "" + remoteMessage.getNotification().getBody());
//            sendNotification(remoteMessage.getNotification().getBody());
//        }
//    }
//
//    @Override
//    public void onMessageSent(String s) {
//        super.onMessageSent(s);
//        Log.i("Service", "onMessageSent" + s);
//    }
//
//    private void sendNotification(String messageBody) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("主廚直播推播通知!")
//                .setContentText(messageBody)
//                .setAutoCancel(true)
//                .setPriority(Notification.PRIORITY_MAX)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }
}
//
//public class MyFirebaseMessagingService extends FirebaseMessagingService {
//
//    private static final String TAG = "MyFirebaseMsgService";
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//    }
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//        Log.i("Service", "onMessageReceived: " + remoteMessage.getFrom());
//        if (remoteMessage.getData().size() > 0) {
//            Log.i("Service", "" + remoteMessage.getData());
//        }
//        if (remoteMessage.getNotification() != null) {
//            Log.i("Service", "" + remoteMessage.getNotification().getBody());
//            sendNotification(remoteMessage.getNotification().getBody());
//        }
//    }
//
//    @Override
//    public void onMessageSent(String s) {
//        super.onMessageSent(s);
//        Log.i("Service", "onMessageSent" + s);
//    }
//
//    private void sendNotification(String messageBody) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("主廚直播推播通知!")
//                .setContentText(messageBody)
//                .setAutoCancel(true)
//                .setPriority(Notification.PRIORITY_MAX)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }
//}