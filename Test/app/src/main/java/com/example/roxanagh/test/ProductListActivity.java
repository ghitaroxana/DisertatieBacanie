package com.example.roxanagh.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by roxanagh on 4/21/2015.
 */
public class ProductListActivity extends Activity {
    SessionManager session;
    public ArrayList<ProductItem> products = new ArrayList<ProductItem>();
    Context mContext;
    int productCategory;
    Intent intent;
    ProductItem productItem = null;
    String vector[];
    LinearLayout myGallery;

    TextView tvName;
    TextView tvDescription;
    TextView tvDateFromTo;
    TextView tvPrice;
    TextView tvLocality;
    TextView tvCounty;
    TextView tvUserDetails;
    TextView tvPhoneNumber;
    ImageView selectedImageView;
    Menu menuItem;
    // int nr = 0;
    TextView cartTV;
    ImageView cartImage;

    ArrayList<Bitmap> bitmapArrayList;
    int nrClicksBack = 0;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        session = new SessionManager(getApplicationContext());
        mContext = ProductListActivity.this;
        intent = getIntent();
        productCategory = intent.getIntExtra("productCategory", -1);
        // nr = intent.getIntExtra("noProduct",0);
        ProductCategory productCat = ProductCategory.getEnumForValue(productCategory);
        lv = (ListView) findViewById(R.id.productListLV);

        tvName = (TextView) findViewById(R.id.full_item_name);
        tvDescription = (TextView) findViewById(R.id.full_item_description);
        tvDateFromTo = (TextView) findViewById(R.id.fullDateFromToTV);
        tvPrice = (TextView) findViewById(R.id.fullPriceTV);
        tvLocality = (TextView) findViewById(R.id.fullLocalityTV);
        tvCounty = (TextView) findViewById(R.id.fullCountyTV);
        tvUserDetails = (TextView) findViewById(R.id.userDetailsTV);
        tvPhoneNumber = (TextView) findViewById(R.id.phoneNumberTV);
        selectedImageView = (ImageView) findViewById(R.id.full_image);

        goBackToList(View.GONE);
        //pt a lua shared prefference, nu este cazul acum
        // HashMap<String, String> user = session.getUserDetails();

        products = dataForTest(productCat);
        // products = getProductsByCategory(productCat);
        if (products != null) populateListViewWithProducts(products);
        registerClickCallBack();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.buyer_menu, menu);
        final View menu_hotlist = menu.findItem(R.id.shopping_cart).getActionView();
        cartImage = (ImageView) menu_hotlist.findViewById(R.id.hotlist_bell);
        cartTV = (TextView) menu_hotlist.findViewById(R.id.hotlist_hot);
        updateHotCount();
        new MyMenuItemStuffListener(menu_hotlist, "Show hot message") {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductListActivity.this, "Click pe shop cart", LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ShoppingCartActivity.class);
                startActivity(intent);
                finish();
            }
        };

        menuItem = menu;

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        int visibilityInt = lv.getVisibility();
        if (visibilityInt != View.GONE) nrClicksBack = 1;

        if (nrClicksBack == 1) super.onBackPressed();
        else {
            nrClicksBack++;
            goBackToList(View.GONE);
            myGallery.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
            Toast.makeText(getBaseContext(), "MAI APASATI O DATA!!!", Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHotCount();
    }

    protected void goBackToList(int view) {
        tvName.setVisibility(view);
        tvDescription.setVisibility(view);
        tvDateFromTo.setVisibility(view);
        tvPrice.setVisibility(view);
        tvLocality.setVisibility(view);
        tvCounty.setVisibility(view);
        tvUserDetails.setVisibility(view);
        tvPhoneNumber.setVisibility(view);
        selectedImageView.setVisibility(view);

    }

    public void updateHotCount() {
        if (cartTV == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Utils.nr == 0) {
                    cartImage.setImageResource(R.drawable.ic_cart_empty);
                    cartTV.setVisibility(View.GONE);
                } else {
                    cartImage.setImageResource(R.drawable.ic_cart_full);
                    cartTV.setVisibility(View.VISIBLE);
                    cartTV.setText(Integer.toString(Utils.nr));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.shopping_cart:
                Toast.makeText(getBaseContext(), "COS DE CUMPARATURI", Toast.LENGTH_SHORT);
                return true;

            case R.id.search_buyer:
                Toast.makeText(getBaseContext(), "CAUTARE", Toast.LENGTH_SHORT);
                onSearchRequested();
                return true;

            case R.id.list_buyer:
                return true;

            case R.id.favorites_buyer:
                return true;

            case R.id.account_buyer:
                return true;

            case R.id.logout_icon_buyer:
                session.logoutUser();
                System.exit(0);
                return true;
        }
        return true;
    }

    private ArrayList<ProductItem> dataForTest(ProductCategory productCat) {
        User user = new User(1l, "roxana", "roxana", "ghita@gmail.com", UserRole.PRODUCER, new Detail(1l, "Ghita", "Roxana", "0724691892", new County(), new Locality()));

        Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cheese32);
        Bitmap bm1 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_floare);
        Bitmap bm2 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_rosie);
        Bitmap bm3 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_oaie);
        Bitmap bm4 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_pastaie);

        Utils.photo = bm2;
        byte[] imagebyte;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, out);
        imagebyte = out.toByteArray();
//		  byte[] bytepoza= Base64.decode(sugestiePoza.bytePoza,Base64.DEFAULT);
        String[] str = new String[5];
        String str1 = null;
        try {
            str1 = Base64.encodeToString(imagebyte, Base64.DEFAULT);
            str[0] = str1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        out = new ByteArrayOutputStream();
        bm1.compress(Bitmap.CompressFormat.JPEG, 50, out);
        imagebyte = out.toByteArray();
        try {
            str1 = Base64.encodeToString(imagebyte, Base64.DEFAULT);
            str[1] = str1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        out = new ByteArrayOutputStream();
        bm2.compress(Bitmap.CompressFormat.JPEG, 50, out);
        imagebyte = out.toByteArray();
        try {
            str1 = Base64.encodeToString(imagebyte, Base64.DEFAULT);
            str[2] = str1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        out = new ByteArrayOutputStream();
        bm3.compress(Bitmap.CompressFormat.JPEG, 50, out);
        imagebyte = out.toByteArray();
        try {
            str1 = Base64.encodeToString(imagebyte, Base64.DEFAULT);
            str[3] = str1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        out = new ByteArrayOutputStream();
        bm4.compress(Bitmap.CompressFormat.JPEG, 50, out);
        imagebyte = out.toByteArray();
        try {
            str1 = Base64.encodeToString(imagebyte, Base64.DEFAULT);
            str[4] = str1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date time;

        Calendar cal = Calendar.getInstance();
        time = new Date(cal.getTime().getTime());

        ArrayList<ProductItem> list = new ArrayList<ProductItem>();
        ProductItem product;
        switch (productCat.getIntValue()) {
            case 0:
                product = new ProductItem(1l, str, "rosii", "rosii bio de la DOBROGEA", new Float(10), time, time, user, ProductCategory.VEGETABLES,Per.KG);
                list.add(product);
                product = new ProductItem(2l, str, "castraveti", "castraveti bio de la DOBROGEA", new Float(10), time, time, user, ProductCategory.VEGETABLES,Per.KG);
                list.add(product);
                product = new ProductItem(3l, str, "ceapa", "ceapa bio de la DOBROGEA", new Float(10), time, time, user, ProductCategory.VEGETABLES,Per.KG);
                list.add(product);
                product = new ProductItem(4l, str, "varza", "varza bio de la DOBROGEA", new Float(10), time, time, user, ProductCategory.VEGETABLES,Per.KG);
                list.add(product);
                product = new ProductItem(100l, str, "ardei", "rosii bio de la DOBROGEA", new Float(10), time, time, user, ProductCategory.VEGETABLES,Per.KG);
                list.add(product);
                product = new ProductItem(200l, str, "patrunjel", "castraveti bio de la DOBROGEA", new Float(10), time, time, user, ProductCategory.VEGETABLES,Per.KG);
                list.add(product);
                product = new ProductItem(300l, str, "morcov", "ceapa bio de la DOBROGEA", new Float(10), time, time, user, ProductCategory.VEGETABLES,Per.KG);
                list.add(product);
                product = new ProductItem(400l, str, "gulie", "varza bio de la DOBROGEA", new Float(10), time, time, user, ProductCategory.VEGETABLES,Per.KG);
                list.add(product);
                break;
            case 1:
                product = new ProductItem(5l, str, "mere", "mere bio de la MOLDOVA", new Float(10), time, time, user, ProductCategory.FRUITS,Per.KG);
                list.add(product);
                product = new ProductItem(6l, str, "pere", "pere bio de la MOLDOVA", new Float(10), time, time, user, ProductCategory.FRUITS,Per.KG);
                list.add(product);
                product = new ProductItem(7l, str, "cirese", "cirese bio de la MOLDOVA", new Float(10), time, time, user, ProductCategory.FRUITS,Per.KG);
                list.add(product);
                product = new ProductItem(8l, str, "nuci", "nuci bio de la MOLDOVA", new Float(10), time, time, user, ProductCategory.FRUITS,Per.KG);
                list.add(product);
                break;
            case 2:
                product = new ProductItem(9l, str, "OUA", "oua bio de la ARDEAL", new Float(10), time, time, user, ProductCategory.EGGS,Per.BUC);
                list.add(product);
                product = new ProductItem(10l, str, "OUA", "oua bio de la VALCEA", new Float(10), time, time, user, ProductCategory.EGGS,Per.BUC);
                list.add(product);
                product = new ProductItem(11l, str, "OUA", "oua bio de la OLTENIA", new Float(10), time, time, user, ProductCategory.EGGS,Per.BUC);
                list.add(product);
                break;
            case 3:
                product = new ProductItem(12l, str, "porc", "porc bio de la ARDEAL", new Float(10), time, time, user, ProductCategory.MEAT,Per.KG);
                list.add(product);
                product = new ProductItem(13l, str, "pui", "pui bio de la ARDEAL", new Float(10), time, time, user, ProductCategory.MEAT,Per.KG);
                list.add(product);
                product = new ProductItem(14l, str, "salam", "salam bio de la ARDEAL", new Float(10), time, time, user, ProductCategory.MEAT,Per.KG);
                list.add(product);
                product = new ProductItem(15l, str, "carnati", "carnati bio de la ARDEAL", new Float(10), time, time, user, ProductCategory.MEAT,Per.KG);
                list.add(product);
                break;
            case 4:
                product = new ProductItem(16l, str, "PAINE", "paine bio de la IASI", new Float(10), time, time, user, ProductCategory.BREAD,Per.BUC);
                list.add(product);
                product = new ProductItem(17l, str, "cozonac", "cozonac bio de la IASI", new Float(10), time, time, user, ProductCategory.BREAD,Per.BUC);
                list.add(product);
                product = new ProductItem(18l, str, "gogosi", "gogosi bio de la IASI", new Float(10), time, time, user, ProductCategory.BREAD,Per.BUC);
                list.add(product);
                product = new ProductItem(19l, str, "covrigi", "covrigi bio de la IASI", new Float(10), time, time, user, ProductCategory.BREAD,Per.BUC);
                list.add(product);
                break;
            case 5:
                product = new ProductItem(20l, str, "BRANZA", "Branza bio de la Maramures", new Float(10), time, time, user, ProductCategory.MILK,Per.KG);
                list.add(product);
                product = new ProductItem(21l, str, "BRANZA FETA", "Branza feta bio de la Maramures", new Float(10), time, time, user, ProductCategory.MILK,Per.KG);
                list.add(product);
                product = new ProductItem(22l, str, "LAPTE", "Lapte bio de la Maramures", new Float(10), time, time, user, ProductCategory.MILK,Per.LITTER);
                list.add(product);
                break;
            case 6:
                product = new ProductItem(23l, str, "cuie", "cuie de la CLINCENI", new Float(10), time, time, user, ProductCategory.OTHERS,Per.KG);
                list.add(product);
                product = new ProductItem(24l, str, "flori", "flori de la CLINCENI", new Float(10), time, time, user, ProductCategory.OTHERS,Per.BUC);
                list.add(product);
                product = new ProductItem(25l, str, "rasad de rosii", "rasad de rosii de la CLINCENI", new Float(10), time, time, user, ProductCategory.OTHERS,Per.BUC);
                list.add(product);
                break;
        }
        return list;
    }

    ArrayList<ProductItem> getProductsByCategory(ProductCategory productCat) {
        //aici voi face un request la server sa imi aduca din baza produsele, dar filtrate dupa tipul de produs
        // va rezulta un arrayList de produse

        SoapObject Request1 = new SoapObject(Utils.NAMESPACE, Utils.METHOD_getProductByCategory);
        Request1.addProperty("productCategory", productCat.getIntValue());

        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        soapEnvelope.encodingStyle = SoapEnvelope.ENC;
        soapEnvelope.dotNet = true;
        //new MarshalBase64().register(soapEnvelope);   //serialization
        soapEnvelope.setOutputSoapObject(Request1);
        soapEnvelope.implicitTypes = true;

        HttpTransportSE ahttSE = new HttpTransportSE(Utils.URL, 6000);

        ahttSE.debug = true;

        try {
            ahttSE.call(Utils.SOAP_LOGIN, soapEnvelope);
            Log.v("connectare", "s-a conectat");

            SoapObject resultString = (SoapObject) soapEnvelope.bodyIn;
            String result;
            int nr = 0;
            if (resultString != null && resultString.getPropertyCount() > 0) {
                nr = resultString.getPropertyCount();
                vector = new String[nr];

                for (int i = 0; i < resultString.getPropertyCount(); i++) {
                    if (resultString.getProperty(i) != null) {
                        vector[i] = resultString.getProperty(i).toString();
                        Log.v("Rasp: ", i + "  " + resultString.getProperty(i).toString());
                        Log.v("Rasp: ", i + "  " + vector[i]);
                    }
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (HttpResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void populateListViewWithProducts(ArrayList<ProductItem> products) {

        ArrayAdapter<ProductItem> adapter = new ProductListAdapter(mContext, products);
        ListView list = (ListView) findViewById(R.id.productListLV);
        list.setVisibility(View.VISIBLE);
        list.setAdapter(adapter);
    }

    private void registerClickCallBack() {
        lv.setVisibility(View.VISIBLE);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position,
                                    long id) {

                nrClicksBack = 0;
                productItem = products.get(position);

                lv.setVisibility(View.GONE);
                View searchItem = findViewById(R.id.search_buyer);
                searchItem.setVisibility(View.GONE);

                ImageView icon = new ImageView(ProductListActivity.this); // Create an icon
                icon.setImageResource(R.drawable.ic_plus);

                FloatingActionButton actionButton = new FloatingActionButton.Builder(ProductListActivity.this)
                        .setContentView(icon)
                        .setTheme(R.style.Theme_AppCompat)
                        .build();
                actionButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ProductListActivity.this, "Ati adaugat un produs", LENGTH_LONG).show();

                        Utils.nr++;
                        updateHotCount();

                        addProductToList(productItem);
                    }
                });

                goBackToList(View.VISIBLE);


                tvName.setText(productItem.getName());
                tvDescription.setText(productItem.getDescription());
                tvDateFromTo.setText(productItem.getDateFrom().toString().substring(0, 10) + " - " + productItem.getDateTo().toString().substring(0, 10));
                tvPrice.setText(productItem.getPrice().toString() + " RON/per "+productItem.getPer().toString());
                tvLocality.setText(productItem.getUser().getDetail().getLocality().getDescription());
                tvCounty.setText(productItem.getUser().getDetail().getCounty().getDescription());
                tvUserDetails.setText(productItem.getUser().getDetail().getLastName() + " " + productItem.getUser().getDetail().getFirstName());
                tvPhoneNumber.setText("Telefon: " + productItem.getUser().getDetail().getPhoneNumber());

                Toast.makeText(ProductListActivity.this, "Ati ales produsul: " + productItem.getName(), LENGTH_LONG).show();
                myGallery = (LinearLayout) findViewById(R.id.mygallery);
                myGallery.setVisibility(View.VISIBLE);

                bitmapArrayList = new ArrayList<Bitmap>();
                int index = -1;
                for (String s : productItem.getBytePhotos()) {
                    index++;
                    myGallery.addView(insertPhoto(s, index));
                }
            }
        });

    }

    public void addProductToList(ProductItem productItem) {
        boolean ok = false;

        for (OrderItem item : Utils.orderList) {
            if (item.getProduct().equals(productItem)) {
                item.quantity++;
                ok = true;

            }
        }
        if (!ok) {
            OrderItem orderItem = new OrderItem(1, productItem);
            Utils.orderList.add(orderItem);
        }
        Utils.totalOrderBuyer+=productItem.getPrice();

    }

    View insertPhoto(String bytes, final int index) {

        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setLayoutParams(new LayoutParams(250, 250));
        layout.setGravity(Gravity.CENTER);

        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(new LayoutParams(220, 220));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        byte[] bytepoza = Base64.decode(bytes, Base64.DEFAULT);
        Bitmap bm = BitmapFactory.decodeByteArray(bytepoza, 0, bytepoza.length);
        imageView.setImageBitmap(bm);
        bitmapArrayList.add(bm);
        if (index == 0) selectedImageView.setImageBitmap(bm);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectedImageView.setImageBitmap(bitmapArrayList.get(index));
            }
        });
        layout.addView(imageView);
        return layout;
    }
}


