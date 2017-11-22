package hanifah.sipuk.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import hanifah.sipuk.R;

/**
 * Created by Glory on 03/10/2016.
 */
public class RecycleViewHolderPilihMotor extends RecyclerView.ViewHolder {

    public TextView txtNamaPilih,txtKet,txtHarga;
    public ImageView img_iconlistPilih;


    public RecycleViewHolderPilihMotor(View itemView) {
        super(itemView);

        txtNamaPilih = (TextView) itemView.findViewById(R.id.txt_namaMotorPilih);
        txtKet = (TextView) itemView.findViewById(R.id.txt_platNomorPilih);
        txtHarga = itemView.findViewById(R.id.txtListHarga);
        img_iconlistPilih = (ImageView) itemView.findViewById(R.id.img_iconlistMotor);


    }
}
