package hanifah.sipuk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
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

public class TambahMotor extends AppCompatActivity {

    Button btnSimpan,btnBrowse;
    EditText etNama,etKet,etHarga,etSilinder,etTahun,etDp;
    Spinner sp_jenis;
    Firebase Sref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private ProgressBar progressBar;

    MotorModel motorModel;

    //dari sni kelengkapan untuk Firebase Storage
    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    private StorageReference storageReference;
    ImageView gambar;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_motor);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(TambahMotor.this);
        fAuth = FirebaseAuth.getInstance();
        Sref = new Firebase("https://sipuk-6aea5.firebaseio.com/").child("motor");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSimpan = (Button) findViewById(R.id.btnRegisMotor);
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

        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGE_REQUEST);

                } catch (Exception e) {
                    Toast.makeText(TambahMotor.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (formcek()){
                    progressBar.setVisibility(View.VISIBLE);
                    etNama.setEnabled(false);
                    etKet.setEnabled(false);
                    etHarga.setEnabled(false);
                    etSilinder.setEnabled(false);
                    etTahun.setEnabled(false);
                    sp_jenis.setEnabled(false);
                    etDp.setEnabled(false);

                    tambahMotor();
                    uploadFile(filePath.getLastPathSegment());
                    //btnSimpan.setEnabled(false);
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
                    btnSimpan.setEnabled(true);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Toast.makeText(TambahMotor.this, e.toString(), Toast.LENGTH_LONG).show();
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
                            btnSimpan.setEnabled(false);
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

    private void tambahMotor(){

        String jenis = sp_jenis.getSelectedItem().toString();
        try{

            motorModel = new MotorModel(etNama.getText().toString(),
                    etKet.getText().toString(),
                    etHarga.getText().toString(),
                    etTahun.getText().toString(),
                    etSilinder.getText().toString(),
                    jenis,
                    etDp.getText().toString(),
                    filePath.getLastPathSegment()
                    );
            Sref.push().setValue(motorModel);

            Toast.makeText(TambahMotor.this,"Berhasil",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            etNama.setText("");
            etKet.setText("");
            etHarga.setText("");
            etTahun.setText("");
            etSilinder.setText("");
            etDp.setText("");

            etNama.setEnabled(true);
            etKet.setEnabled(true);
            etHarga.setEnabled(true);
            etSilinder.setEnabled(true);
            etTahun.setEnabled(true);
            sp_jenis.setEnabled(true);
            etDp.setEnabled(true);

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Eror tambah mtoor : "+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("Eror tambah motor : ",e.toString());
        }

    }




}
