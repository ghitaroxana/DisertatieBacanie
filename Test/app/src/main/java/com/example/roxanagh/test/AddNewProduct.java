package com.example.roxanagh.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by Roxana on 5/16/2015.
 */
public class AddNewProduct extends Activity implements AdapterView.OnItemSelectedListener {

    SessionManager session;
    Context mContext;
    TextView dateToTV;
    TextView dateFromTV;

    EditText phoneET;
    EditText emailET;
    EditText userDetailsET;
    EditText productDescriptionET;
    EditText productNameET;
    EditText productPriceET;

    private Calendar startDate;
    private Calendar endDate;
    ImageView addPhotoIV;
    Button btnAddNotice;

    static final int DATE_DIALOG_ID = 0;

    private TextView activeDateDisplay;
    private Calendar activeDate;

    Spinner comboCounty;
    Spinner comboLocality;
    Spinner comboCategory;
    Spinner comboPer;

    GalleryAdapter adapter;
    LinearLayout myGallery;
    ArrayList<Bitmap> bitmapArrayList;
    Bitmap bm;
    final String[] actions = {"Fotografiați", "Alegeți din galerie"};


    String action;
    ViewSwitcher viewSwitcher;
    ImageLoader imageLoader;
    Handler handler;
    GridView gridGallery;

    String productName;
    String productDescription;
    String priceString;
    Float price;
    Per per;
    County productCounty;
    Locality productLocality;
    ProductCategory productCategory;
    Date from;
    Date to;
    String phone;
    String email;
    String userDetailsString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        session = new SessionManager(getApplicationContext());
        mContext = AddNewProduct.this;
        bitmapArrayList = new ArrayList<Bitmap>();

        dateToTV = (TextView) findViewById(R.id.dateToTV);
        dateFromTV = (TextView) findViewById(R.id.dateFromTV);
        addPhotoIV = (ImageView) findViewById(R.id.add_photoIV);
        productNameET = (EditText) findViewById(R.id.full_item_nameET);
        productDescriptionET = (EditText) findViewById(R.id.add_full_item_description);
        productPriceET = (EditText) findViewById(R.id.fullPriceET);
        userDetailsET = (EditText) findViewById(R.id.userDetailsTV);
        phoneET = (EditText) findViewById(R.id.phoneNumberTV);
        emailET = (EditText) findViewById(R.id.etEmail);


        btnAddNotice = (Button) findViewById(R.id.btnAddNotice);

        startDate = Calendar.getInstance();

        dateFromTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(dateFromTV, startDate);
            }
        });
        endDate = Calendar.getInstance();

        dateToTV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateDialog(dateToTV, endDate);
            }
        });

        comboCounty = (Spinner) findViewById(R.id.add_comboCounty);
        comboLocality = (Spinner) findViewById(R.id.add_comboLocality);
        comboCategory = (Spinner) findViewById(R.id.add_comboCategory);
        comboPer = (Spinner) findViewById(R.id.per_combo);

        loadCountyComboData();

        comboCategory.setVisibility(View.VISIBLE);
        comboCategory.setEnabled(true);
        comboCategory.setAdapter(new ArrayAdapter<ProductCategory>(this, android.R.layout.simple_spinner_item, ProductCategory.values()));

        comboPer.setVisibility(View.VISIBLE);
        comboPer.setEnabled(true);
        comboPer.setAdapter(new ArrayAdapter<Per>(this, android.R.layout.simple_spinner_item, Per.values()));

        addPhotoIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bitmapArrayList.size() < 6) {
                    showRadioButtonAlert(mContext, "Adăugare poză", actions);

                } else Utils.showAlert(mContext, "Adăugare poză", "Puteți adăuga numai 6 poze!");
            }
        });

        initImageLoader();

        myGallery = (LinearLayout) findViewById(R.id.add_mygallery);
        myGallery.setVisibility(View.VISIBLE);

        handler = new Handler();

        adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
        adapter.setMultiplePick(true);

        btnAddNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productName = productNameET.getText().toString();
                productDescription = productDescriptionET.getText().toString();

                priceString = productPriceET.getText().toString();
                price = Float.parseFloat(priceString);
                per = (Per) comboPer.getSelectedItem();
                productCounty = (County) comboCounty.getSelectedItem();
                productLocality = (Locality) comboLocality.getSelectedItem();
                productCategory = (ProductCategory) comboCategory.getSelectedItem();

                from = new Date(dateFromTV.getText().toString().replace("-", "/"));
                to = new Date(dateToTV.getText().toString().replace("-", "/"));
                phone = phoneET.getText().toString();
                email = emailET.getText().toString();
                userDetailsString = userDetailsET.getText().toString();

                Bitmap[] bitmapList = (Bitmap[]) bitmapArrayList.toArray(new Bitmap[0]);

                ProductItem productItem = new ProductItem(productName, productDescription, price, from, to, Utils.userLogged, productCategory, per, bitmapList);
              //  Integer id = addProduct(productItem,dateFromTV.getText().toString().replace("-", "/"),dateToTV.getText().toString().replace("-", "/"));
                Announcement announcement = new Announcement(productItem, Utils.userLogged, userDetailsString, phone, email, productCounty, productLocality);

                Boolean ok = addAnnouncementToDB(announcement,dateFromTV.getText().toString().replace("-", "/"),dateToTV.getText().toString().replace("-", "/"));
//perform BACK button
            }
        });

    }

    private Integer addProduct(ProductItem productItem, String dateFrom,String dateTo) {

        Integer id = -1;
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("productName", productItem.getName());
        params.put("productDescription", productItem.getDescription());
        params.put("price", productItem.getPrice().toString());
        params.put("from", dateFrom);
        params.put("to", dateTo);
        params.put("idUser", Utils.userLogged.getId().toString());
        params.put("idProductCategory", String.valueOf(productItem.getProductCategory().getIntValue()));
        params.put("idPer", String.valueOf(productItem.getPer().getIntValue()));

        String result = Utils.invokeSOAPWS(params, Utils.METHOD_addProduct, Utils.SOAP_ADDPRODUCT);
        if (result != "" && result.compareTo("null") != 0) {

            id = Utils.gson.fromJson(result, Integer.class);
            if (id != -1) {
                Utils.showAlert(mContext, "Adăugare produs", "Produsul a fost adăugat cu succes!");
            } else
                Utils.showAlert(mContext, "Adăugare produs", "Produsul nu s-a putut adăuga!");
        } else Utils.showAlert(mContext, "Adăugare produs", "Produsul nu s-a putut adăuga!");
        return id;
    }

    private Boolean addAnnouncementToDB(Announcement announcement,String dateFrom, String dateTo) {

        Boolean ok = false;
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("productName", announcement.getProduct().getName());
        params.put("productDescription", announcement.getProduct().getDescription());
        params.put("price", announcement.getProduct().getPrice().toString());
        params.put("from", dateFrom);
        params.put("to", dateTo);
        params.put("idUser", Utils.userLogged.getId().toString());
        params.put("idProductCategory", String.valueOf(announcement.getProduct().getProductCategory().getIntValue()));
        params.put("idPer", String.valueOf(announcement.getProduct().getPer().getIntValue()));
        params.put("userDetailsString", announcement.getUserDescription());
        params.put("phone", announcement.getPhoneNumbe());
        params.put("email", announcement.getEmail());
        params.put("idProductCounty", announcement.getCounty().getId().toString());
        params.put("idProductLocality", announcement.getLocality().getId().toString());

        Bitmap[] bitmaps = announcement.getProduct().getBmpList();
        String[] photos = new String[6];

        byte[] imagebyte;
        for (int i = 0; i < bitmaps.length; i++) {

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmaps[i].compress(Bitmap.CompressFormat.JPEG, 50, out);
            imagebyte = out.toByteArray();
            try {
                photos[i] = Base64.encodeToString(imagebyte, Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (photos.length > 0 && photos[0] != null) params.put("photo1", photos[0]);
        else params.put("photo1", "0");

        if (photos.length > 1 && photos[1] != null) params.put("photo2", photos[1]);
        else params.put("photo2", "0");

        if (photos.length > 2 && photos[2] != null) params.put("photo3", photos[2]);
        else params.put("photo3", "0");

        if (photos.length > 3 && photos[3] != null) params.put("photo4", photos[3]);
        else params.put("photo4", "0");

        if (photos.length > 4 && photos[4] != null) params.put("photo5", photos[4]);
        else params.put("photo5", "0");

        if (photos.length > 5 && photos[5] != null) params.put("photo6", photos[5]);
        else params.put("photo6", "0");

        String result = Utils.invokeSOAPWS(params, Utils.METHOD_addAnnouncement, Utils.SOAP_ADDANNOUNCEMENT);

        if (result != "" && result.compareTo("null") != 0) {

            ok = Utils.gson.fromJson(result, Boolean.class);
            if (ok) {
                Utils.showAlert(mContext, "Adăugare anunț", "Anunțul a fost adăugat cu succes!");
            } else
                Utils.showAlert(mContext, "Adăugare anunț", "Anunțul nu s-a putut adăuga!");
        }

        return ok;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int index = bitmapArrayList.size() == 0 ? 0 : bitmapArrayList.size() - 1;

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Uri fileUri = data.getData();
            Toast.makeText(this, "Saved: " + fileUri.getPath(), Toast.LENGTH_LONG).show();
            Bitmap bm = (Bitmap) data.getExtras().get("data");
            myGallery.addView(insertPhoto(bm, index++));
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            String[] all_path = data.getStringArrayExtra("all_path");

            ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();

            for (String string : all_path) {
                CustomGallery item = new CustomGallery();
                item.sdcardPath = string;
                dataT.add(item);
            }


            int limit_size = 0;
            limit_size = bitmapArrayList.size() + dataT.size() < 6 ? dataT.size() : 6 - bitmapArrayList.size();
            for (int ind = 0; ind < limit_size; ind++) {
                bm = BitmapFactory.decodeFile(dataT.get(ind).sdcardPath);
                myGallery.addView(insertPhoto(bm, index++));
            }
        }
    }

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

        if (Utils.userLogged != null) {
            County usercounty = Utils.userLogged.getDetail().getCounty();
            //  County countyFromDB = db.getCountyById(usercounty.getId());
            int index = getIndexFromListForObject(counties, usercounty);
            comboCounty.setSelection(index);
        }
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

        if (Utils.userLogged != null) {
            Locality userLocality = Utils.userLogged.getDetail().getLocality();
            //Locality localityFromDB = db.getLocalityById(userLocality.getId(), county);
            int index = getIndexFromListForObject(localities, userLocality);
            comboLocality.setSelection(index);
        }
    }

    private void updateDisplay(TextView dateDisplay, Calendar date) {
        dateDisplay.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(date.get(Calendar.MONTH) + 1).append("-")
                        .append(date.get(Calendar.DAY_OF_MONTH)).append("-")
                        .append(date.get(Calendar.YEAR)).append(" "));

    }

    public void showDateDialog(TextView dateDisplay, Calendar date) {
        activeDateDisplay = dateDisplay;
        activeDate = date;
        showDialog(DATE_DIALOG_ID);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            activeDate.set(Calendar.YEAR, year);
            activeDate.set(Calendar.MONTH, monthOfYear);
            activeDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDisplay(activeDateDisplay, activeDate);
            unregisterDateDisplay();
        }
    };

    private void unregisterDateDisplay() {
        activeDateDisplay = null;
        activeDate = null;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, dateSetListener, activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
                break;
        }
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

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    View insertPhoto(Bitmap bm, final int index) {

        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setLayoutParams(new ViewGroup.LayoutParams(250, 250));
        layout.setGravity(Gravity.CENTER);

        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(220, 220));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        imageView.setImageBitmap(bm);
        bitmapArrayList.add(bm);
        if (index == 0) addPhotoIV.setImageBitmap(bm);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addPhotoIV.setImageBitmap(bitmapArrayList.get(index));
            }
        });
        layout.addView(imageView);
        return layout;
    }

    protected void showRadioButtonAlert(Context mContext, String title, String[] items) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle(title);
        alertDialog.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, 1);

                                break;
                            case 1:
                                Intent i = new Intent(Utils.ACTION_MULTIPLE_PICK);
                                startActivityForResult(i, 200);
                                break;
                        }
                        dialog.dismiss();
                    }
                }
        );
        alertDialog.create();
        alertDialog.show();
    }

    public int getIndexFromListForObject(List<County> list, County object) {
        int index = -1;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCode() != null)
                if (object.getCode().compareTo(list.get(i).getCode()) == 0)
                    index = i;
        }
        return index;
    }

    public int getIndexFromListForObject(List<Locality> list, Locality object) {
        int index = -1;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCode() != null)
                if (object.getCode().compareTo(list.get(i).getCode()) == 0)
                    index = i;
        }
        return index;
    }
}
