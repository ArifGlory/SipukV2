package hanifah.sipuk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailMotorActivity extends AppCompatActivity {

    Intent i;
    TextView txtNama,txtKet,txtHarga,txtJenis,txtSilinder,txtTahun,txtMinDp;
    Button btnSimulasi;
    private String key;
    ImageView gambar;
    Firebase Sref;
    private Uri filePath;
    private StorageReference storageReference;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_motor);
        Firebase.setAndroidContext(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtNama = (TextView) findViewById(R.id.txtNamaMotor);
        txtHarga = (TextView) findViewById(R.id.txtHarga);
        txtJenis = (TextView) findViewById(R.id.txtJenisMotor);
        txtKet = (TextView) findViewById(R.id.txtDeskripsi);
        txtSilinder = (TextView) findViewById(R.id.txtSilinder);
        txtTahun = (TextView) findViewById(R.id.txtTahun);
        txtMinDp = (TextView) findViewById(R.id.txtMinDP);
        btnSimulasi = (Button) findViewById(R.id.btnSimulasi);
        gambar = (ImageView) findViewById(R.id.image);

        i = getIntent();
        final String nama = i.getStringExtra("nama");
        String ket = i.getStringExtra("ket");
        final String harga = i.getStringExtra("harga");
        String silinder = i.getStringExtra("silinder");
        String tahun = i.getStringExtra("tahun");
        final String jenis = i.getStringExtra("jenis");
        key = i.getStringExtra("key");
        final String dp = i.getStringExtra("dp");
        String gambarTerima = i.getStringExtra("gambar");

        txtNama.setText(nama);
        txtKet.setText(ket);
        txtHarga.setText("Rp. "+harga);
        txtJenis.setText(jenis);
        txtSilinder.setText(silinder);
        txtTahun.setText(tahun);
        txtMinDp.setText(dp);
        showbyte(gambarTerima);

        btnSimulasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(getApplicationContext(),InputSimulasi.class);
                i.putExtra("nama",nama);
                i.putExtra("jenis",jenis);
                i.putExtra("harga",harga);
                i.putExtra("dp",dp);
                startActivity(i);
            }
        });

    }

    private void showbyte(String nama){
        /*FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://farmartcorp.appspot.com/file/");*/
        progressBar.setVisibility(View.VISIBLE);
        btnSimulasi.setEnabled(false);
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference islandRef = storageReference.child("file/").child(nama);
        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                gambar.setImageBitmap(bitmap);
                progressBar.setVisibility(View.GONE);
                btnSimulasi.setEnabled(true);
            }
        });
        btnSimulasi.setEnabled(true);

    }
}
