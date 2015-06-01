package com.example.roxanagh.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;


public class LoginActivity extends Activity implements TextWatcher {
    public String username;
    String password;
    int id = 0;
    EditText user;
    EditText psw;
    Button login;
    Button signUp;
    Boolean ok = true;
    public static Context mContext;

    SessionManager session;

    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mContext = LoginActivity.this;
        session = new SessionManager(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        login = (Button) findViewById(R.id.btnLogin);
        signUp = (Button) findViewById(R.id.btnSignup);
        login.setVisibility(View.VISIBLE);
        login.setEnabled(false);
        login.setClickable(false);
        signUp.setVisibility(View.VISIBLE);
        signUp.setClickable(true);

        user = (EditText) findViewById(R.id.etUsername);
        user.addTextChangedListener(this);

        psw = (EditText) findViewById(R.id.etPassword);
        psw.addTextChangedListener(this);

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String cont;

                username = user.getText().toString();
                password = psw.getText().toString();
                Log.v("username: ", username);
                Log.v("pass: ", password);

                int ok = checkUser(username, password);

                if (ok != -1) {
                    UserRole userRole = UserRole.getEnumForValue(ok);
                    session.createLoginSession(username, password, userRole);
                    ok=2;
                    Intent i;
                    switch (ok) {
                        case 1:
                            i = new Intent(getApplicationContext(), ProducerMainMenu.class);
                            startActivity(i);
                            finish();
                            break;
                        case 2:
                            i = new Intent(getApplicationContext(), BuyerMainMenu.class);
                            startActivity(i);
                            finish();
                            break;
                        case 3:
                            i = new Intent(getApplicationContext(), TransporterMainMenu.class);
                            startActivity(i);
                            finish();
                            break;
                    }
                } else
                    Utils.showAlert(mContext, "Login", "Parola sau/și username-ul introduse nu sunt corecte!");
                //else Utils.showAlert(mContext,"Login", "Nu se poate conecta la internet!");

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createDB();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void createDB() throws IOException {

        DBHelper dbHelper = new DBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        dbHelper.executeSqlFromFile(db, mContext, R.raw.data_base);
//        dbHelper.executeSqlFromFile(db, mContext, R.raw.insert);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.exit) {
            session.logoutUser();
            System.exit(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Boolean ok1, ok2, ok3, ok4;

        username = user.getText().toString();
        if (username.length() == 0) {
            user.setError("Trebuie sa introduceti un utilizator!");
            ok1 = false;
        } else ok1 = true;
        if (username.length() < 3) {
            user.setError("Utilizatorul trebuie sa aiba minim 3 caractere!");
            ok2 = false;
        } else ok2 = true;

        if (username.contains(" ")) {
            user.setError("Utilizatorul nu trebuie sa contina spatii!");
            ok3 = false;
        } else ok3 = true;


        password = psw.getText().toString();
        if (password.length() < 6) {
            psw.setError("Parola trebuie sa contina cel putin 6 caractere!");
            ok4 = false;
        } else ok4 = true;

        if (ok1 && ok2 && ok3 && ok4) {
            login.setEnabled(true);
            login.setClickable(true);
        } else {
            login.setEnabled(false);
            login.setClickable(false);
        }
    }

    int checkUser(String username, String password) {
        int idUserRole = -1;
        login.setEnabled(false);
        boolean ok = false;

        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("username", username);

        params.put("password", password);


        String result = Utils.invokeSOAPWS(params, Utils.METHOD_login, Utils.SOAP_LOGIN);

        User user = null;
        if (result != "") {
            user = Utils.gson.fromJson(result, User.class);

            if (user != null) {
                Utils.userLogged = user;
                idUserRole = user.getUserRole().getIntValue();
            } else
                Utils.showAlert(mContext, "Login", "Parola sau/și username-ul introduse nu sunt corecte!");
        }
        return idUserRole;
    }


}