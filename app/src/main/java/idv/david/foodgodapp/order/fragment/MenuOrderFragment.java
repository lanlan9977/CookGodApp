package idv.david.foodgodapp.order.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import idv.david.foodgodapp.MenuOrder;
import idv.david.foodgodapp.OrderActivity;
import idv.david.foodgodapp.R;
import idv.david.foodgodapp.Util;

import static android.support.v4.content.ContextCompat.getSystemService;

public class MenuOrderFragment extends Fragment {

    private List<MenuOrder> MenuOrderList;
    private AsyncTask retrieveMenuOrderTask;
    private

    public MenuOrderFragment() {
//        利用建構子帶入MENU_ORDER VO
        MenuOrderList = new ArrayList<>();
        MenuOrder menuOrder = new MenuOrder("M02091", "M1", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), new Date(), 0, "", "C00001", "C00001", "M00001");

        MenuOrderList.add(menuOrder);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_menuorder, container, false);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new MenuOrderAdapter(inflater));

        if (networkConnected()) {
            retrieveMenuOrderTask = new RetrieveMenuOrderTask().execute(Util.URL);
        } else {
            Toast.makeText(this, "沒有網路", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
    private boolean networkConnected() {
        ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }





    private class MenuOrderAdapter extends RecyclerView.Adapter<MenuOrderAdapter.ViewHolder> {
        private LayoutInflater inflater;

        public MenuOrderAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView idMenu_or_id, idMenu_or_appt;

            public ViewHolder(View itemView) {
                super(itemView);
                idMenu_or_id = itemView.findViewById(R.id.idMenu_or_id);
                idMenu_or_appt = itemView.findViewById(R.id.idMenu_or_appt);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.card_menuorder, parent, false);
            ViewHolder viewHolder = new ViewHolder(itemView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            final MenuOrder MenuOrder = MenuOrderList.get(position);
            viewHolder.idMenu_or_id.setText(MenuOrder. getMenu_ID());
            viewHolder.idMenu_or_appt.setText((MenuOrder.getMenu_od_book()).toString());

            viewHolder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return MenuOrderList.size();
        }


    }










    private class RetrieveMenuOrderTask extends AsyncTask<String, String, List<String>> {
        private ProgressDialog progressDialog;
        private final static String TAG="OrderActivity";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(OrderActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected List<String> doInBackground(String... params) {
            String url = params[0];
            String jsonIn;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("param", "area");
            jsonIn = getRemoteData(url, jsonObject.toString());
            Gson gson = new Gson();
            Type listType = new TypeToken<List<String>>() {
            }.getType();

            return gson.fromJson(jsonIn,listType);
        }

        @Override
        protected void onPostExecute(List<String> items) {
            ArrayAdapter<String> adapter=new ArrayAdapter<>(OrderActivity.this,android.R.layout.simple_list_item_1,items);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            reviewStauts.setAdapter(adapter);
            progressDialog.cancel();


        }

        private String getRemoteData(String url, String outStr) {
            HttpURLConnection connection = null;
            StringBuilder inStr = new StringBuilder();
            try {
                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setDoInput(true); // allow inputs
                connection.setDoOutput(true); // allow outputs
                // 不知道請求內容大小時可以呼叫此方法將請求內容分段傳輸，設定0代表使用預設大小
                connection.setChunkedStreamingMode(0);
                connection.setUseCaches(false); // do not use a cached copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("charset", "UTF-8");
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                bw.write(outStr);
                Log.d(TAG, "output: " + outStr);
                bw.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = br.readLine()) != null) {
                        inStr.append(line);
                    }
                } else {
                    Log.d(TAG, "response code: " + responseCode);
                }
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            Log.d(TAG, "input: " + inStr);
            return inStr.toString();
        }


}

