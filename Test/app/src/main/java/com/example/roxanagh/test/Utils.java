package com.example.roxanagh.test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Roxana on 4/17/2015.
 */
public class Utils {

    public static final String ACTION_PICK = "luminous.ACTION_PICK";
    public static final String ACTION_MULTIPLE_PICK = "luminous.ACTION_MULTIPLE_PICK";


    protected static final String SOAP_SIGNIN = "http://server.roxana.com/signin";
    protected static final String SOAP_LOGIN = "http://server.roxana.com/login";
    protected static final String SOAP_ADDANNOUNCEMENT = "http://server.roxana.com/addAnnouncement";
    protected static final String SOAP_ADDPRODUCT = "http://server.roxana.com/addProduct";

    protected static final String METHOD_Signin = "signin";
    protected static final String METHOD_login = "login";
    protected static final String METHOD_getProductByCategory = "getProductsByCategory";
    protected static final String METHOD_getUser = "getUser";
    protected static final String METHOD_addAnnouncement = "addAnnouncement";
    protected static final String METHOD_addProduct = "addProduct";

    protected static final String NAMESPACE = "http://server.roxana.com/";
 //   protected static final String URL = "http://10.0.2.2:8070/WebServiceTest/services/FunctionsService";
    protected static final String URL = "http://192.168.0.105:8070/WebServiceTest/services/FunctionsService";

    protected static final String IS_LOGIN = "IsLoggedIn";

    protected static final String KEY_NAME = "username";
    protected static final String PASSWORD = "password";
    protected static final String ID_USER_ROLE = "idUserRole";

    public static int nr = 0;
    public static Float totalOrderBuyer = 0f;

    public static ArrayList<OrderItem> orderList = new ArrayList<OrderItem>();
    public static ArrayList<SettingCheckBox> settingList = new ArrayList<SettingCheckBox>();

    public static User userLogged;
    public static Bitmap photo;
    protected static void showAlert(Context mContext, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // On pressing Settings button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //inchide app
                System.exit(0);
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }



    ///////////////////////////////////////////////

   public static JsonSerializer<Date> ser = new JsonSerializer<Date>() {
        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext
                context) {
            return src == null ? null : new JsonPrimitive(src.getTime());
        }
    };

    public static JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
            return json == null ? null : new Date(json.getAsLong());
        }
    };

    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, ser)
            .registerTypeAdapter(Date.class, deser).create();


    public static String invokeSOAPWS(Map<String, String> params, String methName, String soapRequest) {
        String responseJSON = "";
        // Create request
        SoapObject request = new SoapObject(Utils.NAMESPACE, methName);
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.encodingStyle = SoapEnvelope.ENC;
        soapEnvelope.dotNet = true;

        Set mapSet = (Set) params.entrySet();
        Iterator mapIterator = mapSet.iterator();
        while (mapIterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) mapIterator.next();
            String keyValue = (String) mapEntry.getKey();
            String value = (String) mapEntry.getValue();
            request.addProperty(keyValue, value);
        }

        soapEnvelope.setOutputSoapObject(request);
        soapEnvelope.implicitTypes = true;

        HttpTransportSE ahttSE = new HttpTransportSE(Utils.URL, 60000);

        ahttSE.debug = true;
//String productName,String productDescription,String price,String from,String to,String idUser,
// String idProductCategory,String idPer,String userDetailsString,String phone,String email,String idProductCounty,
// String idProductLocality,String photo1,String photo2,String photo3,String photo4,String photo5,String photo6) throws ParseException{
        try {
            ahttSE.call(soapRequest, soapEnvelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) soapEnvelope.getResponse();
            // Assign it to static variable
            responseJSON = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseJSON;
    }

}
