package idv.david.foodgodapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeActivity extends Activity {

    private static final int GOTO_MAIN_ACTIVITY = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mHandler.sendEmptyMessageDelayed(GOTO_MAIN_ACTIVITY, 1000);
        //Android提供sendMessage的機制
        //用Handler延遲傳送一個訊息，這樣Handler在2秒後收到訊息後再去執行跳轉程式碼
    }

    private Handler mHandler = new Handler() {//Handler負責派送訊息
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                //檢查msg.what若為GOTO_MAIN_ACTIVITY，就執行，
                case GOTO_MAIN_ACTIVITY:
                    Intent intent = new Intent();
                    //將原本Activity的換成MainActivity
                    intent.setClass(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }

    };
}