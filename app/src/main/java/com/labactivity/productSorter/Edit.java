package com.labactivity.productSorter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.labactivity.productSorter.R;

public class Edit extends AppCompatActivity {

    TextView txtrecno;
    EditText produkto, presyo;
    Button editbutton, deletebutton;
    String type;
    RadioButton food, drink, Vother;
    RadioGroup prodtype;
    int checkgroup ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        txtrecno = findViewById(R.id.record_id);
        produkto = findViewById(R.id.id_pname);
        presyo = findViewById(R.id.id_price);

        food = findViewById(R.id.id_food);
        drink = findViewById(R.id.id_drink);
        Vother = findViewById(R.id.id_others);
        prodtype = findViewById((R.id.rdProdType));

        editbutton = findViewById(R.id.id_edit);
        deletebutton = findViewById(R.id.id_delete);

        Intent i_edel = getIntent();

        String col1 = i_edel.getStringExtra("id").toString();
        String col2 = i_edel.getStringExtra("prod").toString();
        String col3 = i_edel.getStringExtra("price").toString();
        String col4 = i_edel.getStringExtra("type").toString();

        txtrecno.setText(""+col1);
        produkto.setText(""+col2);
        presyo.setText(""+col3);

        if ("food".equals(col4.toLowerCase())){
            food.setChecked(true);
        } else if ("drink".equals(col4.toLowerCase())){
            drink.setChecked(true);
        } else if ("other".equals(col4.toLowerCase())) {
            Vother.setChecked(true);
        }else{
            prodtype.clearCheck();
        }

        prodtype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                checkgroup = i;

            }
        });

        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(produkto.getText().toString().length() == 0){
                    produkto.setError("Enter Product!");

                }
                else if(presyo.getText().toString().length() == 0 ){
                    presyo.setError("Enter Price!");

                }else {
                    getSelectedProductType();
                    editrec();
                }


            }
        });

        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showmsg();
            }
        });




    }

    public void editrec(){
        try {
            String nameofprod = produkto.getText().toString();
            String priceofprod = presyo.getText().toString();
            String recno = txtrecno.getText().toString();


            SQLiteDatabase db = openOrCreateDatabase("dbaseprod", Context.MODE_PRIVATE,null);

            String mysql = "update tblproduct set f_prodname = ?, f_prodprice = ?, f_prodtype = ? where id =?";
            SQLiteStatement statement = db.compileStatement(mysql);
            statement.bindString(1, nameofprod);
            statement.bindString(2, priceofprod);
            statement.bindString(3, type);
            statement.bindString(4, recno);
            statement.execute();

            Toast.makeText(getApplicationContext(), "Record Saved Succesfully", Toast.LENGTH_LONG).show();

            finish();
            Intent intent_return = new Intent(getApplicationContext(), AllProduct.class);
            startActivity(intent_return);

        }catch(Exception exception){
            Toast.makeText(getApplicationContext(), "Record Failed", Toast.LENGTH_LONG).show();
        }
    }

    public void deleterec(){
        try {
            String recno = txtrecno.getText().toString();


            SQLiteDatabase db = openOrCreateDatabase("dbaseprod", Context.MODE_PRIVATE,null);

            String mysql = "delete from tblproduct where id =?";
            SQLiteStatement statement = db.compileStatement(mysql);
            statement.bindString(1, recno);
            statement.execute();

            Toast.makeText(getApplicationContext(), "Record Deleted Succesfully", Toast.LENGTH_LONG).show();

            finish();
            Intent intent_return = new Intent(getApplicationContext(), AllProduct.class);
            startActivity(intent_return);

        }catch(Exception exception){
            Toast.makeText(getApplicationContext(), "Deleting Failed", Toast.LENGTH_LONG).show();
        }

    }
    private void getSelectedProductType(){
        if( food.isChecked()) {
            type = food.getText().toString();
        }else if(drink.isChecked()){
            type = drink.getText().toString();
        } else if (Vother.isChecked()) {
            type = Vother.getText().toString();
        } else {
            type = "None";
        }


    }
    private void showmsg() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Delete this item?")
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleterec();
                    }
                }).show();
    }
}
