package com.cookgod.chef;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.cookgod.R;
import com.cookgod.main.Util;
import com.cookgod.task.RetrieveChefSchTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.savvi.rangedatepicker.CalendarPickerView;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//(論壇專區)
public class ChefZoneActivity extends AppCompatActivity {
    private final static String TAG = "ChefZoneActivity";
    private ArrayList<Integer> list;
    private CalendarPickerView calendar;
    private Button get_selected_dates, get_clear_dates, get_add_dates;
    private ArrayList<Date> arrayList, newChefSchList;
    private RetrieveChefSchTask retrieveChefSchTask;
    private String chef_ID;
    private List<ChefSchVO> chefSchList;

    @Override
    protected void onStart() {
        super.onStart();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        retrieveChefSchTask = new RetrieveChefSchTask(Util.Servlet_URL + "ChefSchServlet", chef_ID, null, "select");
        try {
            String jsonIn = retrieveChefSchTask.execute().get();
            Type chefSchType = new TypeToken<List<ChefSchVO>>() {
            }.getType();
            chefSchList = gson.fromJson(jsonIn, chefSchType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (ChefSchVO chefSchVO : chefSchList) {
            arrayList.add(chefSchVO.getChef_sch_date());
        }
        for(Date date:arrayList){
            newChefSchList.add(date);
        }
        addDate();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        chef_ID = intent.getExtras().getString("chef_ID");
        setContentView(R.layout.activity_chefzone);
        list = new ArrayList<>();
        arrayList = new ArrayList<>();
        newChefSchList = new ArrayList<>();
        calendar = findViewById(R.id.calendar_view);
        get_selected_dates = findViewById(R.id.get_selected_dates);
        get_clear_dates = findViewById(R.id.get_clear_dates);
        get_add_dates = findViewById(R.id.get_add_dates);
        CalendarPickerView.SelectionMode selectionMode;
        selectionMode=CalendarPickerView.SelectionMode.RANGE;


        get_selected_dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Date> dateList = calendar.getSelectedDates();
                for (Date date : dateList) {
                    arrayList.add(date);
                }
                addDate();
            }
        });
        get_clear_dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList = null;
                arrayList = new ArrayList<>();
            }
        });
        get_add_dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ChefZoneActivity.this);
                builder.setTitle("是否要發送選擇的日期？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(Date date:newChefSchList){
                            if(arrayList.contains(date)){
                                arrayList.remove(date);
                            }
                        }
                        retrieveChefSchTask = (RetrieveChefSchTask) new RetrieveChefSchTask(Util.Servlet_URL + "ChefSchServlet", chef_ID, arrayList, "add").execute();
                        Util.showToast(ChefZoneActivity.this,"行程發送完畢");

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();

            }
        });
    }

    public void addDate() {
        list.add(1);
        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 10);
        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -10);
        calendar.deactivateDates(list);
        calendar.init(lastYear.getTime(), nextYear.getTime(), new SimpleDateFormat("MMMM, yyyy", Locale.getDefault())) //
                .inMode(CalendarPickerView.SelectionMode.RANGE) //
                .withSelectedDate(new Date())
                .withDeactivateDates(list)
                .withHighlightedDates(arrayList);

    }
}



