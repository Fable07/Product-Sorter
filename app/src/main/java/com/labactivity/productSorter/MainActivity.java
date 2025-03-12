package com.labactivity.productSorter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.labactivity.productSorter.R;

public class MainActivity extends AppCompatActivity {

    EditText prodname, prodprice;
    Button btnadd, btnview, btnfoodview, btndrinkview, btnotherview;
    String nameofprod, priceofprod, type;
    RadioButton food, drink, Vother;
    RadioGroup prodtype;
    int checkgroup ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prodname = findViewById(R.id.edtTxtProduct);
        prodprice = findViewById(R.id.edtTxtPrice);
        btnadd = findViewById(R.id.Add);
        btnview = findViewById(R.id.View);

        btnfoodview = findViewById(R.id.btnFoodView);
        btndrinkview = findViewById(R.id.DrinkView);
        btnotherview = findViewById(R.id.btnOtherView);

        food = findViewById(R.id.rdFood);
        drink = findViewById(R.id.rdDrink);
        Vother = findViewById(R.id.rdOthers);
        prodtype = findViewById((R.id.rdProdType));

        prodtype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                checkgroup = i;

            }
        });



        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(prodname.getText().toString().length() == 0){
                    prodname.setError("Enter Product");

                }
                else if(prodprice.getText().toString().length() == 0 ){
                    prodprice.setError("Enter Price");

                }else if(checkgroup <= 0){

                    Toast.makeText(getApplicationContext(),"Please choose category", Toast.LENGTH_LONG).show();
                }else {
                    getSelectedProductType();

                    addrec();
                }

            }
        });

        btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentview = new Intent(getApplicationContext(), AllProduct.class);
                Toast.makeText(getApplicationContext(), "ALL PRODUCTS", Toast.LENGTH_SHORT).show();
                startActivity(intentview);


            }
        });


        btnfoodview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentview = new Intent(getApplicationContext(), FoodViews.class);
                Toast.makeText(getApplicationContext(), "FOODS", Toast.LENGTH_SHORT).show();
                startActivity(intentview);
            }
        });

        btndrinkview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentview = new Intent(getApplicationContext(), DrinksViews.class);
                Toast.makeText(getApplicationContext(), "DRINKS", Toast.LENGTH_SHORT).show();
                startActivity(intentview);
            }
        });

//        btnotherview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intentview = new Intent(getApplicationContext(), OtherViews.class);
//                Toast.makeText(getApplicationContext(), "FILTER OTHERS", Toast.LENGTH_SHORT).show();
//                startActivity(intentview);
//            }
//        });
        btnotherview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentview = new Intent(getApplicationContext(), OtherViews.class);
                Toast.makeText(getApplicationContext(), "OTHERS", Toast.LENGTH_SHORT).show();
                startActivity(intentview);
            }
        });


    }

    public void addrec(){


        try {
            nameofprod = prodname.getText().toString();
            priceofprod = prodprice.getText().toString();


            SQLiteDatabase db = openOrCreateDatabase("dbaseprod", Context.MODE_PRIVATE,null);
            db.execSQL("CREATE TABLE IF NOT EXISTS tblproduct(id INTEGER PRIMARY KEY AUTOINCREMENT, f_prodname VARCHAR, f_prodprice VARCHAR, f_prodtype VARCHAR)");

            String mysql = " insert into tblproduct(f_prodname, f_prodprice, f_prodtype) values(?,?,?)";
            SQLiteStatement statement = db.compileStatement(mysql);
            statement.bindString(1, nameofprod);
            statement.bindString(2, priceofprod);
            statement.bindString(3, type);
            statement.execute();

            prodname.setText("");
            prodprice.setText("");
            prodtype.clearCheck();
            prodname.requestFocus();
            Toast.makeText(getApplicationContext(), "Record Added Successfully", Toast.LENGTH_LONG).show();



        }catch(Exception exception){
            Toast.makeText(getApplicationContext(), "Record Failed", Toast.LENGTH_LONG).show();
        }

    }

    public void dropData(){
        try {
            SQLiteDatabase db = openOrCreateDatabase("dbaseprod", Context.MODE_PRIVATE, null);
            db.execSQL("DROP TABLE IF EXISTS tblproduct");
            Toast.makeText(getApplicationContext(), "Table Dropped Successfully", Toast.LENGTH_LONG).show();
        } catch (Exception exception) {
            Toast.makeText(getApplicationContext(), "Dropping Table Failed", Toast.LENGTH_LONG).show();
        }

    }

    private void getSelectedProductType(){
        if( food.isChecked()) {
            type = food.getText().toString();
        }else if(Vother.isChecked()){
            type = Vother.getText().toString();
        } else if (drink.isChecked()){
            type = drink.getText().toString();
        } else {
            type = "None";
        }


    }
}