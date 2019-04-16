package yazdaniscodelab.topnoteapp;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    private FloatingActionButton fab_btn;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("TopNote Application");
        setSupportActionBar(toolbar);

        fab_btn=findViewById(R.id.fab);

        recyclerView=findViewById(R.id.recycler);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
            }
        });
    }
    public void addData(){
        AlertDialog.Builder mydialog=new AlertDialog.Builder(this);
        LayoutInflater inflater=LayoutInflater.from(this);
        View mview=inflater.inflate(R.layout.add_data,null);
        mydialog.setView(mview);

        AlertDialog dialog=mydialog.create();

        dialog.show();

    }

}
