package hanifah.sipuk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import hanifah.sipuk.Kelas.MotorModel;

public class UbahMotorActivity extends AppCompatActivity {

    Button btnUbahSimpan,btnBrowse;
    EditText etNama,etKet,etHarga,etSilinder,etTahun,etDp;
    Spinner sp_jenis;
    Firebase Sref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private ProgressBar progressBar;

    MotorModel motorModel;
    private String key,gambarTerima,nama,ket,harga,dp,silinder,jenis,tahun;


    //dari sni kelengkapan untuk Firebase Storage
    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    private StorageReference storageReference;
    ImageView gambar;
    Bitmap bitmap;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_motor);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(UbahMotorActivity.this);
        fAuth = FirebaseAuth.getInstance();
        Sref = new Firebase("https://sipuk-6aea5.firebaseio.com/").child("motor");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnUbahSimpan = (Button) findViewById(R.id.btnUbahSimpan);
        btnBrowse = (Button) findViewById(R.id.btnBrowseGambar);
        etNama = (EditText) findViewById(R.id.etRegisNama);
        etKet = (EditText) findViewById(R.id.etRegisKet);
        etHarga = (EditText) findViewById(R.id.etRegisHarga);
        etSilinder = (EditText) findViewById(R.id.etRegisSilinder);
        etTahun = (EditText) findViewById(R.id.etRegisTahun);
        sp_jenis = (Spinner) findViewById(R.id.sp_jenis);
        etDp = (EditText) findViewById(R.id.etRegisDP);
        gambar = (ImageView) findViewById(R.id.image);

        fStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = fAuth.getCurrentUser();
                if (user != null ){
                    //user sedang login
                    Log.d("Fauth : ","onAuthStateChanged:signed_in:" + user.getUid());
                }
                //user sedang logout
                Log.d("Fauth : ","onAuthStateChanged:signed_out");
            }
        };



        i = getIntent();
        nama = i.getStringExtra("nama");
        ket = i.getStringExtra("ket");
        harga = i.getStringExtra("harga");
        silinder = i.getStringExtra("silinder");
        tahun = i.getStringExtra("tahun");
        jenis = i.getStringExtra("jenis");
        key = i.getStringExtra("key");
        dp = i.getStringExtra("dp");
        gambarTerima = i.getStringExtra("gambar");

        showbyte(gambarTerima);
        etNama.setText(nama);
        etKet.setText(ket);
        etHarga.setText(harga);
        etDp.setText(dp);
        etSilinder.setText(silinder);
        etTahun.setText(tahun);

        if (jenis.equals("Matic")){
            sp_jenis.setSelection(0);
        }else if (jenis.equals("Bebek")){
            sp_jenis.setSelection(1);
        }else if (jenis.equals("Sport")){
            sp_jenis.setSelection(2);
        }


        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGE_REQUEST);

                } catch (Exception e) {
                    Toast.makeText(UbahMotorActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnUbahSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (formcek()){
                    ubahData();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                filePath = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    gambar.setImageBitmap(bitmap);
                    btnUbahSimpan.setEnabled(true);
                   // uploadFile(filePath.getLastPathSegment());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Toast.makeText(UbahMotorActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void uploadFile(String nama) {
        storageReference = FirebaseStorage.getInstance().getReference();
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            String pic = "file/" + nama;
            StorageReference riversRef = storageReference.child(pic);
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            gambar.setImageBitmap(null);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
            //Toast.makeText(getApplicationContext(), "File tidak boleh Kosong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        fAuth.addAuthStateListener(fStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fStateListener != null) {
            fAuth.removeAuthStateListener(fStateListener);
        }
    }

    private boolean validateName() {
        if (etNama.getText().toString().trim().isEmpty()) {
            etNama.setError("Tidak boleh kosong!");
            etNama.requestFocus();
            return false;
        } else {
        }
        return true;
    }

    private boolean validateKet() {
        if (etKet.getText().toString().trim().isEmpty()) {
            etKet.setError("Tidak boleh kosong!");
            etKet.requestFocus();
            return false;
        } else {
        }
        return true;
    }

    private boolean validateHarga() {
        if (etHarga.getText().toString().trim().isEmpty()) {
            etHarga.setError("Tidak boleh kosong!");
            etHarga.requestFocus();
            return false;
        } else {
        }
        return true;
    }

    private boolean validateSilinder() {
        if (etSilinder.getText().toString().trim().isEmpty()) {
            etSilinder.setError("Tidak boleh kosong!");
            etSilinder.requestFocus();
            return false;
        } else {
        }
        return true;
    }

    private boolean validateTahun() {
        if (etTahun.getText().toString().trim().isEmpty()) {
            etTahun.setError("Tidak boleh kosong!");
            etTahun.requestFocus();
            return false;
        } else {
        }
        return true;
    }

    private boolean validateGambar() {

        if (etTahun.getText().toString().trim().isEmpty()) {
            etTahun.setError("Tidak boleh kosong!");
            etTahun.requestFocus();
            return false;
        } else {
        }
        return true;
    }

    private boolean validateDP() {
        if (etDp.getText().toString().trim().isEmpty()) {
            etDp.setError("Tidak boleh kosong!");
            etDp.requestFocus();
            return false;
        } else {
        }
        return true;
    }

    private boolean formcek() {

        if (!validateName()) {
            return false;
        }
        if (!validateKet()) {
            return false;
        }

        if (!validateHarga()) {
            return false;
        }
        if (!validateSilinder()) {
            return false;
        }
        if (!validateTahun()) {
            return false;
        }
        if (!validateDP()) {
            return false;
        }

        return true;

    }

    private void showbyte(String nama){
        /*FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://farmartcorp.appspot.com/file/");*/
        progressBar.setVisibility(View.VISIBLE);


        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference islandRef = storageReference.child("file/").child(nama);
        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                gambar.setImageBitmap(bitmap);
                progressBar.setVisibility(View.GONE);
            }
        });
        progressBar.setVisibility(View.GONE);

    }

    private void ubahData(){

        final String jenisMotor = sp_jenis.getSelectedItem().toString();
        progressBar.setVisibility(View.VISIBLE);
        Query queryUbah = Sref.child(key);

        /*Sref.child(key).child("nama").setValue(etNama.getText().toString());
        Sref.child(key).child("ket").setValue(etKet.getText().toString());
        Sref.child(key).child("harga").setValue(etHarga.getText().toString());
        Sref.child(key).child("tahun").setValue(etTahun.getText().toString());
        Sref.child(key).child("silinder").setValue(etSilinder.getText().toString());
        Sref.child(key).child("jenis").setValue(jenis);
        Sref.child(key).child("dp").setValue(etDp.getText().toString());

        if (filePath!=null){
            uploadFile(filePath.getLastPathSegment());
            Sref.child(key).child("gambar").setValue(filePath.getLastPathSegment());
        }else{
            Sref.child(key).child("gambar").setValue(gambarTerima);
        }

        progressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(),"Berhasil Ubah data !",Toast.LENGTH_SHORT).show();
        i = new Intent(getApplicationContext(),BerandaAdmin.class);
        startActivity(i);
        */

        try {

            queryUbah.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (!etNama.getText().toString().equals(nama)) {
                        dataSnapshot.getRef().child("nama").setValue(etNama.getText().toString());
                    }
                    if (!etKet.getText().toString().equals(ket)) {
                        dataSnapshot.getRef().child("ket").setValue(etKet.getText().toString());
                    }
                    if (!etHarga.getText().toString().equals(harga)) {
                        dataSnapshot.getRef().child("harga").setValue(etHarga.getText().toString());
                    }
                    if (!etTahun.getText().toString().equals(tahun)) {
                        dataSnapshot.getRef().child("tahun").setValue(etTahun.getText().toString());
                    }
                    if (!etSilinder.getText().toString().equals(silinder)) {
                        dataSnapshot.getRef().child("silinder").setValue(etSilinder.getText().toString());
                    }
                    if (!jenis.equals(jenisMotor)) {
                        dataSnapshot.getRef().child("jenis").setValue(jenisMotor);
                    }
                    if (!etDp.getText().toString().equals(dp)) {
                        dataSnapshot.getRef().child("dp").setValue(etDp.getText().toString());
                    }

                    if (filePath!=null){
                        uploadFile(filePath.getLastPathSegment());
                       dataSnapshot.getRef().child("gambar").setValue(filePath.getLastPathSegment());
                    }

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Berhasil Ubah data !",Toast.LENGTH_SHORT).show();
                    i = new Intent(getApplicationContext(),BerandaAdmin.class);
                    startActivity(i);


                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Eror ubah motor : "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("Eror tambah motor : ",e.toString());
        }
        progressBar.setVisibility(View.GONE);


    }


}
