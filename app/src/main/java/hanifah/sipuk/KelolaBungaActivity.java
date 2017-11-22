package hanifah.sipuk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class KelolaBungaActivity extends AppCompatActivity {

    Intent i;
    EditText et12,et24,et18,et36,et30;
    Button btnMin12,btnMin18,btnMin24,btnMin36,btnMin30;
    Button btnPlus12,btnPlus18,btnPlus24,btnPlus36,btnPlus30;
    Button btnUbah,btnSimpan,btnDefault;
    Firebase Sref;
    private Double bunga12,bunga18,bunga24,bunga36,bunga30;
    private Double def12,def18,def24,def30,def36;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_bunga);
        Firebase.setAndroidContext(this);
        Sref = new Firebase("https://sipuk-6aea5.firebaseio.com/").child("setting");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        def12 = 2.5374685138539;
        def18 = 1.91789623988577;
        def24 = 1.70596951735817;
        def30 = 1.43373065290874;
        def36 = 1.24559505409583;

        et12 = (EditText) findViewById(R.id.et12Bulan);
        et18 = (EditText) findViewById(R.id.et18Bulan);
        et24 = (EditText) findViewById(R.id.et24Bulan);
        et36 = (EditText) findViewById(R.id.et36Bulan);
        et30 = (EditText) findViewById(R.id.et30Bulan);

        ambilDataBunga();

        btnMin12 = (Button) findViewById(R.id.btnMin12);
        btnMin18 = (Button) findViewById(R.id.btnMin18);
        btnMin24 = (Button) findViewById(R.id.btnMin24);
        btnMin36 = (Button) findViewById(R.id.btnMin36);
        btnMin30 = (Button) findViewById(R.id.btnMin30);

        btnPlus12 = (Button) findViewById(R.id.btnPlus12);
        btnPlus24 = (Button) findViewById(R.id.btnPlus24);
        btnPlus18 = (Button) findViewById(R.id.btnPlus18);
        btnPlus36 = (Button) findViewById(R.id.btnPlus36);
        btnPlus30 = (Button) findViewById(R.id.btnPlus30);

        btnUbah = (Button) findViewById(R.id.btnUbahBunga);
        btnSimpan = (Button) findViewById(R.id.btnSimpanBunga);
        btnDefault = (Button) findViewById(R.id.btnSetDefault);

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et12.setEnabled(true);
                et18.setEnabled(true);
                et24.setEnabled(true);
                et36.setEnabled(true);
                et30.setEnabled(true);


                btnUbah.setVisibility(View.GONE);
                btnSimpan.setVisibility(View.VISIBLE);
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (formcek()){
                    et12.setEnabled(false);
                    et18.setEnabled(false);
                    et24.setEnabled(false);
                    et36.setEnabled(false);
                    et30.setEnabled(false);

                    bunga12 = Double.valueOf(et12.getText().toString());
                    Sref.child("bunga12").setValue(bunga12);

                    bunga18 = Double.valueOf(et18.getText().toString());
                    Sref.child("bunga18").setValue(bunga18);

                    bunga24 = Double.valueOf(et24.getText().toString());
                    Sref.child("bunga24").setValue(bunga24);

                    bunga36 = Double.valueOf(et36.getText().toString());
                    Sref.child("bunga36").setValue(bunga36);

                    bunga30 = Double.valueOf(et30.getText().toString());
                    Sref.child("bunga30").setValue(bunga30);


                    btnSimpan.setVisibility(View.GONE);
                    btnUbah.setVisibility(View.VISIBLE);

                    Toast.makeText(getApplicationContext(),"Berhasil",Toast.LENGTH_SHORT).show();
                }


            }
        });

        btnDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                Sref.child("bunga12").setValue(def12);
                Sref.child("bunga18").setValue(def18);
                Sref.child("bunga24").setValue(def24);
                Sref.child("bunga30").setValue(def30);
                Sref.child("bunga36").setValue(def36);

                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Berhasil",Toast.LENGTH_SHORT).show();
                
            }
        });

    }

    private void ambilDataBunga(){
        try{


            Sref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    progressBar.setVisibility(View.VISIBLE);

                    bunga12 = (Double) dataSnapshot.child("bunga12").getValue();
                    bunga18 = (Double) dataSnapshot.child("bunga18").getValue();
                    bunga24 = (Double) dataSnapshot.child("bunga24").getValue();
                    bunga36 = (Double) dataSnapshot.child("bunga36").getValue();
                    bunga30 = (Double) dataSnapshot.child("bunga30").getValue();

                    et12.setText(String.valueOf(bunga12));
                    et18.setText(String.valueOf(bunga18));
                    et24.setText(String.valueOf(bunga24));
                    et36.setText(String.valueOf(bunga36));
                    et30.setText(String.valueOf(bunga30));

                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }catch (Exception e){
            Log.e("Eror pilih motor ","Erornya : "+e);
            Toast.makeText(getApplicationContext(),"Gagal Mengambil data: "+e.toString() ,Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
    }

    public void min12(View view) {
        bunga12 = bunga12 - 0.1;
        Sref.child("bunga12").setValue(bunga12);
        et12.setText(String.valueOf(bunga12));
        Toast.makeText(getApplicationContext(),"Berhasil",Toast.LENGTH_SHORT).show();
    }

    public void plus12(View view) {
        bunga12 = bunga12 + 0.1;
        Sref.child("bunga12").setValue(bunga12);
        et12.setText(String.valueOf(bunga12));
        Toast.makeText(getApplicationContext(),"Berhasil",Toast.LENGTH_SHORT).show();
    }

    public void min18(View view) {
        bunga18 = bunga18 - 0.1;
        Sref.child("bunga18").setValue(bunga18);
        et18.setText(String.valueOf(bunga18));
        Toast.makeText(getApplicationContext(),"Berhasil",Toast.LENGTH_SHORT).show();
    }

    public void plus18(View view) {
        bunga18 = bunga18 + 0.1;
        Sref.child("bunga18").setValue(bunga18);
        et18.setText(String.valueOf(bunga18));
        Toast.makeText(getApplicationContext(),"Berhasil",Toast.LENGTH_SHORT).show();
    }

    public void min24(View view) {
        bunga24 = bunga24 - 0.1;
        Sref.child("bunga24").setValue(bunga24);
        et24.setText(String.valueOf(bunga24));
        Toast.makeText(getApplicationContext(),"Berhasil",Toast.LENGTH_SHORT).show();
    }


    public void plus24(View view) {
        bunga24 = bunga24 + 0.1;
        Sref.child("bunga24").setValue(bunga24);
        et24.setText(String.valueOf(bunga24));
        Toast.makeText(getApplicationContext(),"Berhasil",Toast.LENGTH_SHORT).show();
    }

    public void min36(View view) {
        bunga36 = bunga36 - 0.1;
        Sref.child("bunga36").setValue(bunga36);
        et36.setText(String.valueOf(bunga36));
        Toast.makeText(getApplicationContext(),"Berhasil",Toast.LENGTH_SHORT).show();
    }

    public void plus36(View view) {
        bunga36 = bunga36 + 0.1;
        Sref.child("bunga36").setValue(bunga36);
        et36.setText(String.valueOf(bunga36));
        Toast.makeText(getApplicationContext(),"Berhasil",Toast.LENGTH_SHORT).show();
    }

    private boolean validate12() {
        if (et12.getText().toString().trim().isEmpty()) {
            et12.setError("Tidak boleh kosong!");
            et12.requestFocus();
            return false;
        } else {
        }
        return true;
    }

    private boolean validate18() {
        if (et18.getText().toString().trim().isEmpty()) {
            et18.setError("Tidak boleh kosong!");
            et18.requestFocus();
            return false;
        } else {
        }
        return true;
    }

    private boolean validate24() {
        if (et24.getText().toString().trim().isEmpty()) {
            et24.setError("Tidak boleh kosong!");
            et24.requestFocus();
            return false;
        } else {
        }
        return true;
    }

    private boolean validate36() {
        if (et36.getText().toString().trim().isEmpty()) {
            et36.setError("Tidak boleh kosong!");
            et36.requestFocus();
            return false;
        } else {
        }
        return true;
    }

    private boolean validate30() {
        if (et30.getText().toString().trim().isEmpty()) {
            et30.setError("Tidak boleh kosong!");
            et30.requestFocus();
            return false;
        } else {
        }
        return true;
    }

    private boolean formcek() {

        if (!validate12()) {
            return false;
        }
        if (!validate18()) {
            return false;
        }

        if (!validate24()) {
            return false;
        }
        if (!validate36()) {
            return false;
        }
        if (!validate30()) {
            return false;
        }

        return true;

    }


    public void min30(View view) {
        bunga30 = bunga30 - 0.1;
        Sref.child("bunga30").setValue(bunga30);
        et30.setText(String.valueOf(bunga30));
        Toast.makeText(getApplicationContext(),"Berhasil",Toast.LENGTH_SHORT).show();
    }

    public void plus30(View view) {
        bunga30 = bunga30 + 0.1;
        Sref.child("bunga30").setValue(bunga30);
        et30.setText(String.valueOf(bunga30));
        Toast.makeText(getApplicationContext(),"Berhasil",Toast.LENGTH_SHORT).show();
    }
}
