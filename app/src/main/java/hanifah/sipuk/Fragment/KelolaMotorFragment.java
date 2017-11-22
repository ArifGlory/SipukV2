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

import hanifah.sipuk.Adapter.RecycleAdapteraKelolaMotor;
import hanifah.sipuk.Adapter.RecycleAdapteraPilihMotor;
import hanifah.sipuk.R;
import hanifah.sipuk.TambahMotor;


/**
 * A simple {@link Fragment} subclass.
 */
public class KelolaMotorFragment extends Fragment {


    public KelolaMotorFragment() {
        // Required empty public constructor
    }

    public static TextView txtInfoPilih;
    RecyclerView recycler_pilihMotor;
    RecycleAdapteraKelolaMotor adapter;
    FloatingActionButton fab;

    Intent i;
    public static ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_kelola_motor, container, false);
        txtInfoPilih = (TextView) view.findViewById(R.id.txtinfoPilih);
        recycler_pilihMotor = (RecyclerView) view.findViewById(R.id.recycler_pilihmotor);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        fab = view.findViewById(R.id.fab);

        adapter = new RecycleAdapteraKelolaMotor(view.getContext());
        recycler_pilihMotor.setLayoutManager(new LinearLayoutManager(view.getContext()));
      //  LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
       // linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //recycler_pilihMotor.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler_pilihMotor.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(getActivity().getApplicationContext(), TambahMotor.class);
                startActivity(i);
            }
        });


        return view;
    }

}
