package hanifah.sipuk;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailMotorAdmin extends AppCompatActivity {

    Intent i;
    TextView txtNama,txtKet,txtHarga,txtJenis,txtSilinder,txtTahun,txtMinDp;
    Button btnHapus,btnUbah;
   // private static final String key = "a";
    Firebase Sref,refHapus;
    DialogInterface.OnClickListener listener;

    ImageView gambar;

    private Uri filePath;
    private StorageReference storageReference;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_motor_admin);
        Firebase.setAndroidContext(this);

        Sref = new Firebase("https://sipuk-6aea5.firebaseio.com/").child("motor");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        gambar = (ImageView) findViewById(R.id.image);
        txtNama = (TextView) findViewById(R.id.txtNamaMotor);
        txtHarga = (TextView) findViewById(R.id.txtHarga);
        txtJenis = (TextView) findViewById(R.id.txtJenisMotor);
        txtKet = (TextView) findViewById(R.id.txtDeskripsi);
        txtSilinder = (TextView) findViewById(R.id.txtSilinder);
        txtTahun = (TextView) findViewById(R.id.txtTahun);
        txtMinDp = (TextView) findViewById(R.id.txtMinDP);
        btnHapus = (Button) findViewById(R.id.btnHapus);
        btnUbah = (Button) findViewById(R.id.btnUbahMotor);

        i = getIntent();
        final String nama = i.getStringExtra("nama");
        final String ket = i.getStringExtra("ket");
        final String harga = i.getStringExtra("harga");
        final String silinder = i.getStringExtra("silinder");
        final String tahun = i.getStringExtra("tahun");
        final String jenis = i.getStringExtra("jenis");
        final String  key = i.getStringExtra("key");
        final String dp = i.getStringExtra("dp");
        final String gambarTerima = i.getStringExtra("gambar");

        txtNama.setText(nama);
        txtKet.setText(ket);
        txtHarga.setText("Rp. "+harga);
        txtJenis.setText(jenis);
        txtSilinder.setText(silinder);
        txtTahun.setText(tahun);
        txtMinDp.setText(dp);
        showbyte(gambarTerima);


        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DetailMotorAdmin.this);
                builder.setMessage("Apakan anda yakin ?");
                builder.setCancelable(false);

                listener = new DialogInterface.OnClickListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == DialogInterface.BUTTON_POSITIVE){
                            //Sref.child(key).setValue(null);
                           // Toast.makeText(getApplicationContext(),"Key = "+key,Toast.LENGTH_SHORT).show();

                            Sref.child(key).setValue(null);

                            Toast.makeText(getApplicationContext(),"Terhapus",Toast.LENGTH_SHORT).show();
                            i = new Intent(getApplicationContext(),BerandaAdmin.class);
                            startActivity(i);

                           /* refHapus = Sref.child(key);
                            refHapus.removeValue(new Firebase.CompletionListener() {
                                @Override
                                public void onComplete(FirebaseError firebaseError, Firebase firebase) {

                                    if (firebaseError != null){

                                        Log.e("eror hapus = ",firebaseError.toString());
                                        Toast.makeText(getApplicationContext(),"Eror Hapus = "+firebaseError.toString(),Toast.LENGTH_SHORT).show();

                                    }else{

                                        Toast.makeText(getApplicationContext(),"Terhapus",Toast.LENGTH_SHORT).show();
                                        i = new Intent(getApplicationContext(),BerandaAdmin.class);
                                        startActivity(i);
                                    }
                                }
                            });*/




                        }

                        if(which == DialogInterface.BUTTON_NEGATIVE){
                            dialog.cancel();
                        }
                    }
                };
                builder.setPositiveButton("Ya",listener);
                builder.setNegativeButton("Tidak", listener);
                builder.show();


            }
        });

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                i = new Intent(getApplicationContext(),UbahMotorActivity.class);
                i.putExtra("nama",nama);
                i.putExtra("ket",ket);
                i.putExtra("harga",harga);
                i.putExtra("silinder",silinder);
                i.putExtra("tahun",tahun);
                i.putExtra("jenis",jenis);
                i.putExtra("dp",dp);
                i.putExtra("gambar",gambarTerima);
                i.putExtra("key",key);

                startActivity(i);

            }
        });
    }

    private void showbyte(String nama){
        /*FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://farmartcorp.appspot.com/file/");*/
        progressBar.setVisibility(View.VISIBLE);

        btnHapus.setEnabled(false);
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference islandRef = storageReference.child("file/").child(nama);
        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                gambar.setImageBitmap(bitmap);
                progressBar.setVisibility(View.GONE);

                btnHapus.setEnabled(true);
            }
        });

        btnHapus.setEnabled(true);

    }
}
