package idv.david.foodgodapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MemberActivity extends AppCompatActivity {
    TextView textViewNo, textViewId, textViewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        findViews();
    }

    private void findViews() {
//        Customer customer=new Customer();
//        textViewNo=findViewById(R.id.idNo);
//        textViewId=findViewById(R.id.idId);
//        textViewPassword=findViewById(R.id.idPassword);
//        textViewNo.setText(customer.getCustomerNo());
//        textViewId.setText(customer.getCustomerId());
//        textViewPassword.setText(customer.getCustomerPassword());
    }
}

