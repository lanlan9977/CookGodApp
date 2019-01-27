package idv.david.foodgodapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
//(登入專區)

/*
 * 登錄屏幕，提供通過電子郵件/密碼登錄。
 */

public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /*
     * 標識為READ_CONTACTS權限請求的標識。
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /*
     * 包含已知用戶名和密碼的虛擬身份驗證存儲。
     */
    private static final String[] DUMMY_CREDENTIALS =
            new String[]{"foo@example.com:hello","bar@example.com:world"};

    /*
     * 跟踪登錄任務，以確保我們可以根據要求取消登錄任務。
     */
    private UserLoginTask mAuthTask = null;

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mEmailSignInButton, fastBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        /*獲取引入的郵箱並顯示*/
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        /*在密碼編輯界面判斷軟鍵盤的選擇，做對應操作*/
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                /*判斷軟件盤選擇的內容*/
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        fastBtn = (Button) findViewById(R.id.fast_button);


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        /*提交按鍵響應處理*/
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
                /*確認填寫內容*/
            }
        });

        /*設定快捷帳號密碼按鈕*/
        fastBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mEmailView.setText("foo@example.com");
                mPasswordView.setText("hello");
            }
        });
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /*
     * 完成權限請求後收到回調。
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /*
     * 嘗試登錄或註冊登錄表單指定的帳戶。
     * 如果有表單錯誤（無效的電子郵件，缺少字段等），
     * 出現錯誤，未進行實際的登錄嘗試。
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        /*設置輸入框的錯誤提示為空*/
        mEmailView.setError(null);
        mPasswordView.setError(null);


        /*獲取輸入框的郵箱和密碼*/
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        /*設置密碼輸入框的格式（不能為空，不能小於4位）如果格式錯誤重新獲得焦點，並提示錯誤內容*/
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        /*設置郵箱格式*/
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {

            /*如果格式錯誤，輸入框重新獲得輸入焦點*/
            focusView.requestFocus();
        } else {

            /*如果輸入的格式正確，顯示驗證等待對話框，並啟動驗證線程*/
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        /*用你自己的邏輯替換它*/
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        /*用你自己的邏輯替換它*/
        return password.length() > 4;
    }

    /*顯示進度UI並隱藏登錄表單。*/

    /*
     * 指出應用程序的API版本
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        /*獲取運行平台的版本與應用的版本對比實現功能的兼容性*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            /*獲取系統定義的時間*/
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            /*設置驗證對話框為可顯*/
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            /*設置動畫顯示時間& //設置動畫漸變效果*/
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    /*跟據參數控制該控件顯示或隱藏*/
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            /*設置輸入界面可顯示*/
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            /*跟據參數控制該控件顯示或隱藏*/
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                /*檢索設備用戶的“配置文件”聯繫人的數據行*/
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                /*僅選擇電子郵件地址。*/
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                /*首先顯示主電子郵件地址請注意，不會有
                 *如果用戶未指定主電子郵件地址，則為主電子郵件地址*/
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        /*創建適配器以告知AutoCompleteTextView在其下拉列表中顯示的內容。*/
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /*
     * 表示用於對用戶進行身份驗證的異步登錄/註冊任務。
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            /*後台運行線程
             *(嘗試對網絡服務進行身份驗證。*/

            try {
                /*模擬用戶驗證耗時*/
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            /*模擬用戶驗證耗時*/
            for (String credential : DUMMY_CREDENTIALS) {
                /*分割字符串，將密碼個郵箱分離開*/
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            /*在這裡註冊新帳戶。*/
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            /*線程結束後的ui處理*/
            mAuthTask = null;
            /*隱藏驗證延時對話框*/
            showProgress(false);

            if (success) {
                finish();
            } else {
                /*密碼錯誤，輸入框獲得焦點，並提示錯誤*/
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        /*
         * 取消驗證
         */
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

