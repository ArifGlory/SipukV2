package hanifah.sipuk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

public class HasilSimulasi extends AppCompatActivity {

    Intent i;
    TextView txtJenis,txtNama,txtHarga,txtDp,txtBulan,txtAngsuran,txtKet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_simulasi);

        txtJenis = (TextView) findViewById(R.id.txtJenis);
        txtNama = (TextView) findViewById(R.id.txtNama);
        txtHarga = (TextView) findViewById(R.id.txtHarga);
        txtDp = (TextView) findViewById(R.id.txtDP);
        txtBulan = (TextView) findViewById(R.id.txtBulan);
        txtAngsuran  = (TextView) findViewById(R.id.txtAngsuran);
        txtKet = (TextView) findViewById(R.id.txtKet);


        i = getIntent();
        String nama = i.getStringExtra("nama");
        String jenis = i.getStringExtra("jenis");
        String harga = i.getStringExtra("harga");
        String dp = i.getStringExtra("dp");
        String bulan = i.getStringExtra("bulan");
        String angsuran = i.getStringExtra("angsuran");
        String ket = i.getStringExtra("ket");

        txtNama.setText(nama);
        txtJenis.setText(jenis);
        txtAngsuran.setText("Rp. "+angsuran);
        txtBulan.setText(bulan+ " Bulan");
        txtDp.setText("Rp. "+dp);
        txtHarga.setText("Rp. "+harga);

        if (ket.equals("no")){
            txtKet.setText("Maaf, Anda tidak dapat mengajukan kredit dengan uang muka dan periode yang anda ajukan, karena " +
                    "pendapatan anda kurang mencukupi untuk angsuran motor per bulan. Silahkan ajukan dengan jumlah uang yang " +
                    "lebih besar atau periode angsuran yang lebih lama");
        }else {
            txtKet.setText("Selamat anda dapat mengajukan kredit motor anda !");
        }

    }

    public void keDataMotor(View view) {
        i = new Intent(getApplicationContext(),BerandaActivity.class);
        startActivity(i);
    }
}
