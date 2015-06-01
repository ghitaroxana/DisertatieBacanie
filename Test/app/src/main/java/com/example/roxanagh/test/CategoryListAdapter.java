package com.example.roxanagh.test;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by Roxana on 4/19/2015.
 */
public class CategoryListAdapter extends ArrayAdapter<CategoryItem> {
    public CategoryListAdapter(Context context){
        super(context,R.layout.category_item_view);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        String descr;

        View itemView=convertView;
//        if ( itemView==null){
//            itemView=getLayoutInflater().inflate(R.layout.item_view, parent,false);
//        }
//
//        Poza pozacurenta=sugestii.get(position);
//        if (pozacurenta.bytePoza==null){
//            TextView name=(TextView)itemView.findViewById(R.id.item_nume);
//            name.setText(pozacurenta.numePoza);
//            TextView descriere=(TextView)itemView.findViewById(R.id.item_descriere);
//
//            descriere.setText(pozacurenta.tip);
//            ImageView imageView=(ImageView)itemView.findViewById(R.id.item_icon);
//            imageView.setImageResource(R.drawable.afis1);
//        }
//        else{
//            ImageView imageView=(ImageView)itemView.findViewById(R.id.item_icon);
//            byte[] b= Base64.decode(pozacurenta.getBytePoza(), Base64.DEFAULT);
//            Bitmap bm= BitmapFactory.decodeByteArray(b, 0, b.length);
//            imageView.setImageBitmap(bm);
//
//            TextView name=(TextView)itemView.findViewById(R.id.item_nume);
//            name.setText(pozacurenta.numePoza);
//
//            TextView descriere=(TextView)itemView.findViewById(R.id.item_descriere);
//            descr=pozacurenta.tip.substring(0, pozacurenta.tip.indexOf("."));
//            descriere.setText(descr);
//        }

        return itemView;
    }
}