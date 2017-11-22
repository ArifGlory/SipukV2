package hanifah.sipuk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TentangActivity extends AppCompatActivity {

    TextView txtKet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang);
        txtKet = (TextView) findViewById(R.id.txtKet);

        txtKet.setText("PT Tunas Motor Pratama yang berada di jalan" +
                " Ikan Tenggiri no 49 Teluk Betung Bandar Lampung merupakan" +
                "  dealer honda motor resmi yang berafilasi dengan jaringan dealer" +
                " dari Astra Honda. Dealer ini merupakan bengkel resmi Astra Honda" +
                " Authorized Service Station (AHASS) yang melayani servis motor." +
                " Dealer ini melayani penjualan motor secara tunai maupun kredit yang menjual" +
                " berbagai produk-produk andalan dari PT Tunas Motor Pratama khususnya sepeda motor." +
                " Aplikasi ini dibuat untuk mensimulasikan perhitungan kredit di Perusahaan tersebut.");
    }
}
