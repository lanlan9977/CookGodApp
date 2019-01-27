package idv.david.foodgodapp;

public class JDBCAndroid  {
//        extends AsyncTask<Void, Void, Void>
//    private List<Customer> address = new ArrayList();
//    public static Customer customer=new Customer();
//
//    @Override
//    protected void onPreExecute() {//執行前 設定可以在這邊設定
//        super.onPreExecute();
//    }
//
//    @Override
//    protected void onPostExecute(Void aVoid) {//執行中 在背景做事情
//        super.onPostExecute(aVoid);
//        for (int i = 0; i < address.size(); i++) {
//            // ...
//        }
//    }
//
//    @Override
//    protected void onProgressUpdate(Void... values) {//執行中 可以在這邊告知使用者進度
//        super.onProgressUpdate(values);
//    }
//
//    @Override
//    protected Void doInBackground(Void... voids) {//執行中 在背景做事情
//        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        String url = "jdbc:oracle:thin:@localhost:1521:xe";
//        String username = "FOODGOD";
//        String password = "123456";
//        try {
//            Connection con = DriverManager.getConnection(url, username, password);
//            Statement st = con.createStatement();
//            ResultSet rs = st.executeQuery("SELECT * FROM CUSTOMER WHERE 1;");
//            while (rs.next()) {
//                customer.setCustomerNo(rs.getInt("CUSTOMERNO"));
//                customer.setCustomerId(rs.getString("CUSTOMERID"));
//                customer.setCustomerPassword(rs.getInt("CUSTOMERPASSWORD"));
//            }
//
//            try {
//                rs.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                st.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                con.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//        } catch (SQLException se) {
//            se.printStackTrace();
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}

