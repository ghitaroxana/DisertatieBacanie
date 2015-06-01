package com.example.roxanagh.test;

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

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Roxana on 5/22/2015.
 */
public class TestClass {

     public static void main(String[] args){
//         Map<String, String> params=new HashMap<String, String>();
//
//         params.put("roxana","roxana");
//         params.put("diana","diana");
//         params.put("simo","simo");
//         params.put("robert","robert");
//
//
//         Set mapSet = (Set) params.entrySet();
//         Iterator mapIterator = mapSet.iterator();
//         while (mapIterator.hasNext()) {
//             Map.Entry mapEntry = (Map.Entry) mapIterator.next();
//             String keyValue = (String) mapEntry.getKey();
//             String value = (String) mapEntry.getValue();
//             System.out.println(keyValue+" "+ value);
//         }
         JsonSerializer<Date> ser = new JsonSerializer<Date>() {
             @Override
             public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext
                     context) {
                 return src == null ? null : new JsonPrimitive(src.getTime());
             }
         };

         JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
             @Override
             public Date deserialize(JsonElement json, Type typeOfT,
                                          JsonDeserializationContext context) throws JsonParseException {
                 return json == null ? null : new Date(json.getAsLong());
             }
         };



County county = new County(1l,"B","Bucuresti");

         User user = new User(1l,"roxana","parola","ghita@gmail.com",UserRole.PRODUCER,new Detail(2l,"Roxana","Ghita","072469892",county,new Locality(1l,county,"S1","Sector 1")));
        // Gson gson = new Gson();

         Calendar cal = Calendar.getInstance();
         Date time = new Date(cal.getTime().getTime());
         Bitmap bm2 = Utils.photo;
         ProductItem product = new ProductItem(1l, "rosii", "rosii bio de la DOBROGEA", new Float(10), time, time, user, ProductCategory.VEGETABLES,Per.KG,bm2);

         //         GsonBuilder gsonBuilder = new GsonBuilder();
//         gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
//         gsonBuilder.registerTypeAdapter(Timestamp.class, new TimpestampDeserializer());
//         Gson gson = gsonBuilder.create();

         Gson gson = new GsonBuilder()
                 .registerTypeAdapter(Date.class, ser)
                 .registerTypeAdapter(Date.class, deser).create();


         String json = gson.toJson(product);
         System.out.println(json);
       //  String jsson="{\"id\":1,\"name\":\"rosii\",\"description\":\"rosii bio de la DOBROGEA\",\"price\":10.0,\"date_from\":\"1300962900226\",\"date_to\":\"1304782298024\",\"seller\":{\"id\":1,\"username\":\"roxana\",\"password\":\"parola\",\"email\":\"ghita@gmail.com\",\"user_role\":\"PRODUCER\",\"detail\":{\"id\":2,\"first_name\":\"Roxana\",\"last_name\":\"Ghita\",\"phone_number\":\"072469892\",\"county\":{\"id\":1,\"code\":\"B\",\"description\":\"Bucuresti\"},\"locality\":{\"id\":1,\"county\":{\"id\":1,\"code\":\"B\",\"description\":\"Bucuresti\"},\"code\":\"S1\",\"description\":\"Sector 1\"}}},\"product_category\":\"VEGETABLES\",\"per\":\"KG\"}";
       //  System.out.println(json);

         ProductItem obj = gson.fromJson(json, ProductItem.class);
         System.out.println(obj.getDescription());
         System.out.println(obj.getBmp());

     }
}
