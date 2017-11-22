package hanifah.sipuk;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import hanifah.sipuk.Adapter.RecycleAdapteraKelolaMotor;

public class KelolaMotorActivity extends AppCompatActivity {

    public static TextView txtInfoPilih;
    RecyclerView recycler_pilihMotor;
    RecycleAdapteraKelolaMotor adapter;
    FloatingActionButton fab;

    Intent i;
    public static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_motor);

        txtInfoPilih = (TextView) findViewById(R.id.txtinfoPilih);
        recycler_pilihMotor = (RecyclerView) findViewById(R.id.recycler_pilihmotor);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        adapter = new RecycleAdapteraKelolaMotor(this);
        recycler_pilihMotor.setAdapter(adapter);
        recycler_pilihMotor.setLayoutManager(new LinearLayoutManager(this));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(getApplicationContext(), TambahMotor.class);
                startActivity(i);
            }
        });
    }
}
