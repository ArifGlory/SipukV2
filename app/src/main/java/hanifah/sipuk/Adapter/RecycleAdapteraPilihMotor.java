package hanifah.sipuk.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import hanifah.sipuk.DetailMotorActivity;
import hanifah.sipuk.Fragment.PilihMotorFragment;
import hanifah.sipuk.R;


/**
 * Created by Glory on 03/10/2016.
 */
public class RecycleAdapteraPilihMotor extends RecyclerView.Adapter<RecycleViewHolderPilihMotor> {

    LayoutInflater inflater;
    Context context;

    public static List<String> list_namaMotorPilih = new ArrayList();
    public static List<String> list_ketMotorPilih = new ArrayList();
    public static List<String> list_keyMotorPilih = new ArrayList();
    public static List<String> list_hargaPilih = new ArrayList();
    public static List<String> list_jenisPilih = new ArrayList();
    public static List<String> list_silinderPilih = new ArrayList();
    public static List<String> list_tahunPilih = new ArrayList();
    public static List<String> list_dpPilih = new ArrayList();
    public static List<String> list_gambarPilih = new ArrayList();
    String key = "";
    Firebase Sref;
    Bitmap bitmap;
    //ProgressDialog progressBar;
    private StorageReference storageReference;
    Intent i;

    String[] nama ={"Motor 1","Motor 2","Motor 3"};

    public RecycleAdapteraPilihMotor(final Context context) {

        this.context = context;
        inflater = LayoutInflater.from(context);
        Firebase.setAndroidContext(this.context);

        Sref = new Firebase("https://sipuk-6aea5.firebaseio.com/").child("motor");
        try{


            Sref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    PilihMotorFragment.progressBar.setVisibility(View.VISIBLE);
                    list_ketMotorPilih.clear();
                    list_ketMotorPilih.clear();
                    list_hargaPilih.clear();
                    list_jenisPilih.clear();
                    list_namaMotorPilih.clear();
                    list_silinderPilih.clear();
                    list_tahunPilih.clear();
                    list_dpPilih.clear();
                    list_gambarPilih.clear();

                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        String nama = (String) child.child("nama").getValue();
                        String jenis = (String) child.child("jenis").getValue();
                        String key = child.getKey();
                        String ket = (String) child.child("ket").getValue();
                        String silinder = (String) child.child("silinder").getValue();
                        String tahun = (String) child.child("tahun").getValue();
                        String harga = (String) child.child("harga").getValue();
                        String dp = (String) child.child("dp").getValue();
                        String gambar = (String) child.child("gambar").getValue();

                        list_namaMotorPilih.add(nama);
                        list_ketMotorPilih.add(ket);
                        list_keyMotorPilih.add(key);
                        list_silinderPilih.add(silinder);
                        list_tahunPilih.add(tahun);
                        list_hargaPilih.add(harga);
                        list_jenisPilih.add(jenis);
                        list_dpPilih.add(dp);
                        list_gambarPilih.add(gambar);
                    }
                    PilihMotorFragment.progressBar.setVisibility(View.GONE);
                 //   Toast.makeText(context.getApplicationContext(),"berhasil ambil data",Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }catch (Exception e){
            Log.e("Eror pilih motor ","Erornya : "+e);
            Toast.makeText(context.getApplicationContext(),"Gagal Mengambil data: "+e.toString() ,Toast.LENGTH_LONG).show();
            PilihMotorFragment.progressBar.setVisibility(View.GONE);
        }

        if (list_namaMotorPilih != null){
            PilihMotorFragment.txtInfoPilih.setVisibility(View.GONE);
        }else {
            PilihMotorFragment.txtInfoPilih.setVisibility(View.VISIBLE);
        }


    }


    @Override
    public RecycleViewHolderPilihMotor onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist_pilihmotor, parent, false);
        //View v = inflater.inflate(R.layout.item_list,parent,false);
        RecycleViewHolderPilihMotor viewHolderPilihMotor = new RecycleViewHolderPilihMotor(view);
        return viewHolderPilihMotor;
    }

    @Override
    public void onBindViewHolder(RecycleViewHolderPilihMotor holder, int position) {


       holder.txtNamaPilih.setText(list_namaMotorPilih.get(position).toString());
        holder.txtKet.setText(list_ketMotorPilih.get(position).toString());
        holder.txtHarga.setText("Rp. "+list_hargaPilih.get(position).toString());
        //holder.contentWithBackground.setGravity(Gravity.LEFT);

        holder.txtKet.setOnClickListener(clicklistener);
        holder.txtHarga.setOnClickListener(clicklistener);
        holder.txtNamaPilih.setOnClickListener(clicklistener);
        holder.img_iconlistPilih.setOnClickListener(clicklistener);


        holder.txtNamaPilih.setTag(holder);
        holder.txtKet.setTag(holder);
        holder.img_iconlistPilih.setTag(holder);


    }

    View.OnClickListener clicklistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            RecycleViewHolderPilihMotor vHolder = (RecycleViewHolderPilihMotor) v.getTag();
            int position = vHolder.getPosition();
            Toast.makeText(context.getApplicationContext(), "Item diklik", Toast.LENGTH_SHORT).show();
            i  = new Intent(context, DetailMotorActivity.class);
            i.putExtra("nama",list_namaMotorPilih.get(position).toString());
            i.putExtra("ket",list_ketMotorPilih.get(position).toString());
            i.putExtra("key",list_keyMotorPilih.get(position).toString());
            i.putExtra("silinder",list_silinderPilih.get(position).toString());
            i.putExtra("jenis",list_jenisPilih.get(position).toString());
            i.putExtra("tahun",list_tahunPilih.get(position).toString());
            i.putExtra("harga",list_hargaPilih.get(position).toString());
            i.putExtra("dp",list_dpPilih.get(position).toString());
            i.putExtra("gambar",list_gambarPilih.get(position).toString());
            context.startActivity(i);


        }
    };


    public int getItemCount() {

        return list_namaMotorPilih == null ? 0 : list_namaMotorPilih.size();

       // return nama.length;


    }

    private void showbyte(String nama){
        /*FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://farmartcorp.appspot.com/file/");*/
        PilihMotorFragment.progressBar.setVisibility(View.VISIBLE);
       // RecycleViewHolderPilihMotor vHolder = (RecycleViewHolderPilihMotor) view.getTag();
        //int position = vHolder.getPosition();

        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference islandRef = storageReference.child("file/").child(nama);
        final long ONE_MEGABYTE = 1024 * 1024;

        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
               // gambar.setImageBitmap(bitmap);
                PilihMotorFragment.progressBar.setVisibility(View.GONE);

            }
        });
        PilihMotorFragment.progressBar.setVisibility(View.GONE);
    }



}
