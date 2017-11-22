package hanifah.sipuk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.DecimalFormat;

public class InputSimulasi extends AppCompatActivity {

    Intent i;
    EditText etNama,etJenis,etHarga,etDP,etGaji;
    Spinner sp_angsuran;
    Button btnHitung;
    private String minDP,bulan;
    Firebase Sref;
    private ProgressBar progressBar;
    private Double bunga12,bunga18,bunga24,bunga30,bunga36,bungaAktif,bungaFungsi,jumlahAngsuran;
    private int sisaPokok,harga,gaji,dpMinimal,dpUser,hargaMotor,perempatGaji,lamaAngsuran,totalAngsuran;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_simulasi);
        Firebase.setAndroidContext(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Sref = new Firebase("https://sipuk-6aea5.firebaseio.com/").child("setting");

        ambilDataBunga();

        etNama = (EditText) findViewById(R.id.etRegisNama);
        etJenis = (EditText) findViewById(R.id.etJenis);
        etHarga = (EditText) findViewById(R.id.etHarga);
        etDP = (EditText) findViewById(R.id.etDP);
        etGaji = (EditText) findViewById(R.id.etGaji);
        sp_angsuran = (Spinner) findViewById(R.id.sp_angsuran);
        btnHitung = (Button) findViewById(R.id.btnHitung);

        i = getIntent();
        final String nama = i.getStringExtra("nama");
        final String jenis = i.getStringExtra("jenis");
        final String harga = i.getStringExtra("harga");
        minDP = i.getStringExtra("dp");

        etNama.setText(nama);
        etHarga.setText(harga);
        etJenis.setText(jenis);
        etDP.setHint("Minimal Rp. "+minDP);

        btnHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (formcek()) {

                    bulan = sp_angsuran.getSelectedItem().toString();
                    bungaAktif = getBungaAktif();
                    dpUser = Integer.valueOf(etDP.getText().toString());
                    gaji = Integer.parseInt(etGaji.getText().toString());
                    perempatGaji = (int) (gaji * 0.25);

                    if (cekKesiapanDp()) {

                        /*
                        * Rumus Angsuran
                        * Angsuran = ((Harga-DP)+(Harga/Bunga))/Periode Angsuran
                        * */

                        hargaMotor = Integer.valueOf(harga);
                        dpUser = Integer.valueOf(etDP.getText().toString());
                        sisaPokok = hargaMotor - dpUser;
                        //jumlahAngsuran = sisaPokok * (bungaAktif/100);

                        bulan = sp_angsuran.getSelectedItem().toString();
                        lamaAngsuran = Integer.valueOf(bulan);

                        jumlahAngsuran = ((hargaMotor-dpUser)+(hargaMotor/(bungaAktif)))/lamaAngsuran;
                        //totalAngsuran = (int) (((hargaMotor-dpUser)+(hargaMotor/(bungaAktif/100)))/lamaAngsuran);
                        String jml = new DecimalFormat("##.##").format(jumlahAngsuran);

                        if (perempatGaji < jumlahAngsuran){
                            /*Toast.makeText(getApplicationContext(), "Anda belum bisa mengambil motor, karena gaji anda belum mencukupi untuk membayar angsuran bulanan", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "angsuran = " + jml, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "perempatgaji = " + perempatGaji, Toast.LENGTH_SHORT).show();*/

                            i = new Intent(getApplicationContext(),HasilSimulasi.class);
                            i.putExtra("jenis",jenis);
                            i.putExtra("harga",harga);
                            i.putExtra("nama",nama);
                            i.putExtra("dp",etDP.getText().toString());
                            i.putExtra("bulan",bulan);
                            i.putExtra("angsuran",jml);
                            i.putExtra("ket","no");
                            startActivity(i);
                            Toast.makeText(getApplicationContext(), "perempatgaji = " + perempatGaji, Toast.LENGTH_SHORT).show();
                        }else {
                          /*  Toast.makeText(getApplicationContext(), "Anda bisa mengambil motor", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "angsuran = " + jml, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "perempatgaji = " + perempatGaji, Toast.LENGTH_SHORT).show();*/

                            i = new Intent(getApplicationContext(),HasilSimulasi.class);
                            i.putExtra("jenis",jenis);
                            i.putExtra("harga",harga);
                            i.putExtra("nama",nama);
                            i.putExtra("dp",etDP.getText().toString());
                            i.putExtra("bulan",bulan);
                            i.putExtra("angsuran",jml);
                            i.putExtra("ket","yes");
                            Toast.makeText(getApplicationContext(), "perempatgaji = " + perempatGaji, Toast.LENGTH_SHORT).show();
                            startActivity(i);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "DP kurang", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    private Double getBungaAktif(){

        bulan = sp_angsuran.getSelectedItem().toString();
        switch (bulan){
            case "12" :
                bungaFungsi = bunga12;
                break;

            case "18" :
                bungaFungsi = bunga18;
                break;

            case "24" :
                bungaFungsi = bunga24;
                break;

            case "30" :
                bungaFungsi = bunga30;
                break;

            case "36" :
                bungaFungsi = bunga36;
                break;
        }

        return bungaFungsi;
    }

    private boolean cekKesiapanDp(){

        dpUser = Integer.valueOf(etDP.getText().toString());
        dpMinimal = Integer.valueOf(minDP);

        if (dpUser < dpMinimal ){
            return false;
        }else{
            return true;
        }
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

    private boolean validateDP() {
        if (etDP.getText().toString().trim().isEmpty()) {
            etDP.setError("Tidak boleh kosong!");
            etDP.requestFocus();
            return false;
        } else {
        }
        return true;
    }

    private boolean validateGaji() {
        if (etGaji.getText().toString().trim().isEmpty()) {
            etGaji.setError("Tidak boleh kosong!");
            etGaji.requestFocus();
            return false;
        } else {
        }
        return true;
    }

    private boolean formcek() {

        if (!validateDP()) {
            return false;
        }
        if (!validateGaji()) {
            return false;
        }


        return true;

    }
}
