package com.example.roxanagh.test;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by roxanagh on 3/16/2015.
 */
public class SignUpActivity extends Activity implements TextWatcher, AdapterView.OnItemSelectedListener {
    AccountManager accountManager;
    Account ac;
    SessionManager session;

    EditText etFirstName;
    EditText etLastName;
    EditText etPhone;
    EditText etEmail;
    EditText etUser;
    EditText etPsw;

    String firstName;
    String lastName;
    String phone;
    String email;
    String locality;
    String idLocality;
    String county;
    String idCounty;
    String userRole;
    String idUserRole;
    String username;
    String password;

    Spinner comboCounty;
    Spinner comboLocality;
    Spinner comboUserRole;
    Button save;
    Button cancel;
    Boolean ok = false;
    public static Context mContext;
    User user;
    Detail detail;
    private static String responseJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mContext = SignUpActivity.this;
        session = new SessionManager(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        save = (Button) findViewById(R.id.btnSave);
        cancel = (Button) findViewById(R.id.btnCancel);

        comboCounty = (Spinner) findViewById(R.id.comboCounty);
        comboLocality = (Spinner) findViewById(R.id.comboLocality);
        comboUserRole = (Spinner) findViewById(R.id.comboUserRole);

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etFirstName.addTextChangedListener(this);

        etLastName = (EditText) findViewById(R.id.etLastName);
        etLastName.addTextChangedListener(this);

        etPhone = (EditText) findViewById(R.id.etPhone);
        etPhone.addTextChangedListener(this);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etEmail.addTextChangedListener(this);

        etUser = (EditText) findViewById(R.id.etUsername1);
        etUser.addTextChangedListener(this);

        etPsw = (EditText) findViewById(R.id.etPassword1);
        etPsw.addTextChangedListener(this);

        save.setVisibility(View.VISIBLE);
        save.setEnabled(false);
        save.setClickable(false);
        cancel.setVisibility(View.VISIBLE);
        cancel.setClickable(true);

        comboCounty.setEnabled(true);
        comboLocality.setVisibility(View.VISIBLE);
        comboLocality.setEnabled(false);

        loadCountyComboData();

        comboUserRole.setVisibility(View.VISIBLE);
        comboUserRole.setEnabled(true);
        comboUserRole.setAdapter(new ArrayAdapter<UserRole>(this, android.R.layout.simple_spinner_item, UserRole.values()));

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String cont;

                username = etUser.getText().toString();
                password = etPsw.getText().toString();
                firstName = etFirstName.getText().toString();
                lastName = etLastName.getText().toString();
                phone = etPhone.getText().toString();
                email = etEmail.getText().toString();

                county = comboCounty.getSelectedItem().toString();
                idCounty = String.valueOf(comboCounty.getSelectedItemId());

                locality = comboLocality.getSelectedItem().toString();
                idLocality = String.valueOf(comboLocality.getSelectedItemId());

                userRole = comboUserRole.getSelectedItem().toString();
                idUserRole = String.valueOf(((UserRole) comboUserRole.getSelectedItem()).getIntValue());
               // UserRole usrRole = UserRole.valueOf(userRole);

                Log.v("nume + prenume: ", lastName + " " + firstName);

                ok = signUp(username, password, firstName, lastName, phone, email, county, idCounty, locality, idLocality, userRole, idUserRole);

                if (ok) {
                    session.createLoginSession(user.getUsername(),user.getPassword(),user.getUserRole());

                    Intent i = new Intent(getApplicationContext(), MainMenu.class);
                    startActivity(i);
                    finish();
                } else cleanTV();

            }


        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                System.exit(0);

            }
        });
    }

    private void cleanTV() {
        etUser.setText("");
        etPsw.setText("");
        etLastName.setText("");
        etFirstName.setText("");
        etEmail.setText("");
        etEmail.setText("");
        comboCounty.setSelection(0);
        comboLocality.setSelection(0);
        comboUserRole.setSelection(0);
    }

    private Boolean signUp(String username, String password, String firstName, String lastName, String phone, String email, String county, String idCounty, String locality, String idLocality, String userRole, String idUserRole) {

        Boolean ok = false;
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("username", username);
        params.put("password", password);
        params.put("firstName", firstName);
        params.put("lastName", lastName);
        params.put("phone", phone);
        params.put("email", email);
        params.put("county", county);
        params.put("idCounty", idCounty);
        params.put("locality", locality);
        params.put("idLocality", idLocality);
        params.put("userRole", userRole);
        params.put("idUserRole", idUserRole);

        String result = Utils.invokeSOAPWS(params, Utils.METHOD_Signin, Utils.SOAP_SIGNIN);

        user = null;
        if (result != "" && result.compareTo("null") != 0) {

            user = Utils.gson.fromJson(result, User.class);
            if (user != null) {
                Utils.userLogged = user;
                Utils.showAlert(mContext, "Creare utilizator", "Utilizatorul a fost creat cu succes!");
                ok = true;
            } else
                Utils.showAlert(mContext, "Creare utilizator", "Utilizatorul nu s-a putut crea!");
        }

        return ok;
    }

//    private User getUserFromDB(String username) {
//
//        SoapObject Request2 = new SoapObject(Utils.NAMESPACE, Utils.METHOD_getUser);
//
//        Request2.addProperty("username", username);
//        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//
//        soapEnvelope.encodingStyle = SoapEnvelope.ENC;
//        soapEnvelope.dotNet = true;
//        //new MarshalBase64().register(soapEnvelope);   //serialization
//        soapEnvelope.setOutputSoapObject(Request2);
//        soapEnvelope.implicitTypes = true;
//
//        HttpTransportSE ahttSE = new HttpTransportSE(Utils.URL, 60000);
//
//        ahttSE.debug = true;
//
//
//        try {
//            ahttSE.call(Utils.SOAP_SIGNIN, soapEnvelope);
//            Log.v("connectare", "s-a conectat");
//
//            SoapObject resultString = (SoapObject) soapEnvelope.bodyIn;
//            String result;//primesc 0 = daca nu s-a creat userul, 1 = s-a creat userul
//
//            if (resultString.getPropertyCount() > 0 && resultString.getProperty(0) != null) {
//                result = resultString.getProperty(0).toString();
//            }
//        } catch (XmlPullParserException e) {
//            e.printStackTrace();
//        } catch (HttpResponseException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        return null;
//    }

    private void loadCountyComboData() {
        // database handler
        DBHelper db = new DBHelper(getApplicationContext());

        // Spinner Drop down elements
        final List<County> counties = db.getAllCounties();
        County[] arrayCounties = (County[]) counties.toArray(new County[0]);


        // Creating adapter for spinner
        final CountySpinAdapter adapter = new CountySpinAdapter(mContext, android.R.layout.simple_spinner_item, arrayCounties);
        comboCounty.setAdapter(adapter); // Set the custom adapter to the spinner
        // You can create an anonymous listener to handle the event when is selected an spinner item
        comboCounty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                County county = adapter.getItem(position);
                // Here you can do the action you want to...
                Toast.makeText(mContext, "ID: " + county.getId() + "\nName: " + county.getDescription().toString(), LENGTH_SHORT).show();
                loadLocalityComboData(county);
                comboLocality.setVisibility(View.VISIBLE);
                comboLocality.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, counties);
//
//        // Drop down layout style - list view with radio button
//        dataAdapter
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        comboCounty.setAdapter(dataAdapter);
    }

    private void loadLocalityComboData(County county) {
        // database handler
        DBHelper db = new DBHelper(getApplicationContext());

        // Spinner Drop down elements
        List<Locality> localities = db.getAllLocalities(county);
        Locality[] arrayLocalities = (Locality[]) localities.toArray(new Locality[0]);


        // Creating adapter for spinner
        final LocalitySpinAdapter adapter = new LocalitySpinAdapter(mContext, android.R.layout.simple_spinner_item, arrayLocalities);
        comboLocality.setAdapter(adapter); // Set the custom adapter to the spinner
        // You can create an anonymous listener to handle the event when is selected an spinner item
        comboLocality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                Locality locality = adapter.getItem(position);
                // Here you can do the action you want to...
                Toast.makeText(mContext, "ID: " + locality.getId() + "\nName: " + locality.getDescription().toString(), LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, localities);
//
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        comboLocality.setAdapter(dataAdapter);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Boolean okFN = false;
        Boolean okLocality = false;
        Boolean okCounty = false;
        Boolean okLN = false;
        Boolean okPhone = false;
        Boolean okEmail = false;
        Boolean okUserRole = false;
        Boolean ok1, ok2, ok3, ok4;

        firstName = etFirstName.getText().toString();
        if (firstName.length() < 3) {
            etFirstName.setError("Trebuie să introduceți un prenume de cel puțin 3 litere!");
            okFN = false;
        } else okFN = true;

        lastName = etLastName.getText().toString();
        if (lastName.length() < 3) {
            etLastName.setError("Trebuie să introduceți un nume de cel puțin 3 litere!");
            okLN = false;
        } else okLN = true;

        phone = etPhone.getText().toString();
        if (phone.length() < 10 || !phone.matches("[0-9]+")) {
            etPhone.setError("Trebuie să introduceți un număr de telefon valid!");
            okPhone = false;
        } else okPhone = true;

        email = etEmail.getText().toString();
        if (email.length() < 2 || !(email.contains("@") && email.contains("."))) {
            etEmail.setError("Trebuie să introduceți o adresă de email validă!");
            okEmail = false;
        } else okEmail = true;

        username = etUser.getText().toString();
        if (username.length() == 0) {
            etUser.setError("Trebuie sa introduceti un utilizator!");
            ok1 = false;
        } else ok1 = true;
        if (username.length() < 3) {
            etUser.setError("Utilizatorul trebuie sa aiba minim 3 caractere!");
            ok2 = false;
        } else ok2 = true;

        if (username.contains(" ")) {
            etUser.setError("Utilizatorul nu trebuie sa contina spatii!");
            ok3 = false;
        } else ok3 = true;


        password = etPsw.getText().toString();
        if (password.length() < 6) {
            etPsw.setError("Parola trebuie sa contina cel putin 6 caractere!");
            ok4 = false;
        } else ok4 = true;

        if (okFN && okLN && okPhone && okEmail && ok1 && ok2 && ok3 && ok4) {
            save.setEnabled(true);
            save.setClickable(true);
        } else {
            save.setEnabled(false);
            save.setClickable(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String label = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "You selected: " + label,
                Toast.LENGTH_LONG).show();

        comboLocality.setEnabled(true);
        comboLocality.setClickable(true);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}