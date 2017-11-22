package hanifah.sipuk.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.Firebase;

import hanifah.sipuk.Adapter.RecycleAdapteraPilihMotor;
import hanifah.sipuk.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PilihMotorFragment extends Fragment {


    public PilihMotorFragment() {
        // Required empty public constructor
    }

    public static TextView txtInfoPilih;
    RecyclerView recycler_pilihMotor;
    RecycleAdapteraPilihMotor adapter;
    Intent i;
    public static ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_pilih_motor, container, false);
        txtInfoPilih = (TextView) view.findViewById(R.id.txtinfoPilih);
        recycler_pilihMotor = (RecyclerView) view.findViewById(R.id.recycler_pilihmotor);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        adapter = new RecycleAdapteraPilihMotor(view.getContext());
        recycler_pilihMotor.setLayoutManager(new LinearLayoutManager(view.getContext()));
      //  LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
       // linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //recycler_pilihMotor.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler_pilihMotor.setAdapter(adapter);



        return view;
    }

}
