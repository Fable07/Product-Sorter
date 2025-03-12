package com.labactivity.productSorter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.labactivity.productSorter.R;

import java.util.ArrayList;

public class DrinksViews extends AppCompatActivity {
    ListView viewlist;

    ArrayList<String> productlist = new ArrayList<>();
    ArrayAdapter prodlistAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinks_views);

        SQLiteDatabase db = openOrCreateDatabase("dbaseprod", Context.MODE_PRIVATE,null);
        viewlist = findViewById(R.id.ListView);
        final Cursor tmptable = db.rawQuery("Select * from tblproduct where f_prodtype = 'Drink'  order by f_prodname", null);

        int id = tmptable.getColumnIndex("id");
        int prodname = tmptable.getColumnIndex("f_prodname");
        int priceprod = tmptable.getColumnIndex("f_prodprice");
        int prodtype = tmptable.getColumnIndex("f_prodtype");


        productlist.clear();

        prodlistAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,productlist);
        viewlist.setAdapter(prodlistAdapter);

        final ArrayList<Product> productdetails = new ArrayList<Product>();



        if(tmptable.moveToFirst()){
            do {

                Product prod = new Product();
                prod.refid = tmptable.getString(id);
                prod.productname = tmptable.getString(prodname);
                prod.productprice = tmptable.getString(priceprod);
                prod.producttype = tmptable.getString(prodtype);
                productdetails.add(prod);


                productlist.add(tmptable.getString(prodname)+ " \t "
                        + tmptable.getString(priceprod)+ " -\t " + tmptable.getString(prodtype));
            }while(tmptable.moveToNext());
            prodlistAdapter.notifyDataSetChanged();
            viewlist.invalidateViews();


        }

        viewlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Product prod = productdetails.get(i);
                Intent intentedit = new Intent(getApplicationContext(), Edit.class);

                intentedit.putExtra("id", prod.refid);
                intentedit.putExtra("prod",prod.productname);
                intentedit.putExtra("price",prod.productprice);
                intentedit.putExtra("type", prod.producttype);
                finish();
                startActivity(intentedit);




            }
        });

    }
}