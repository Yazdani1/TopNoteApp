package yazdaniscodelab.topnoteapp;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

import yazdaniscodelab.topnoteapp.Model.Data;

public class HomeActivity extends AppCompatActivity {

    private FloatingActionButton fab_btn;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    //Firebase..

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mAuth=FirebaseAuth.getInstance();

        FirebaseUser mUser=mAuth.getCurrentUser();

        String uid=mUser.getUid();

        mDatabase= FirebaseDatabase.getInstance().getReference().child("AllData").child(uid);


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

        final AlertDialog dialog=mydialog.create();

        final EditText title=mview.findViewById(R.id.title);
        final EditText budget=mview.findViewById(R.id.budget);
        final EditText note=mview.findViewById(R.id.note);

        Button btnSave=mview.findViewById(R.id.save_btn);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mTitle=title.getText().toString().trim();
                String mBudget=budget.getText().toString().trim();
                String mNote=note.getText().toString().trim();

                if (TextUtils.isEmpty(mTitle)){
                    title.setError("required field..");
                    return;
                }
                if (TextUtils.isEmpty(mBudget)){
                    budget.setError("required field...");
                }
                if (TextUtils.isEmpty(mNote)){
                    note.setError("required field...");
                }

                String id=mDatabase.push().getKey();

                String date= DateFormat.getDateInstance().format(new Date());

                Data mData=new Data(mTitle,mBudget,mNote,date,id);

                mDatabase.child(id).setValue(mData);

                dialog.dismiss();
            }
        });


        dialog.show();
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data,MyViewHolder>adapter=new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (
                        Data.class,
                        R.layout.showdata,
                        MyViewHolder.class,
                        mDatabase
                ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, Data model, int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setNote(model.getNote());
                viewHolder.setBudget(model.getBudget());
                viewHolder.setDate(model.getDate());

            }
        };
        recyclerView.setAdapter(adapter);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View myyview;

        public MyViewHolder(View itemView) {
            super(itemView);
            myyview=itemView;
        }

        public void setTitle(String title){
            TextView mTitle=myyview.findViewById(R.id.sh_title);
            mTitle.setText(title);
        }

        public void setNote(String note){
            TextView mNote=myyview.findViewById(R.id.sh_note);
            mNote.setText(note);
        }

        public void setBudget(String budget){
            TextView mBudget=myyview.findViewById(R.id.sh_budget);
            mBudget.setText("$"+ budget);
        }

        public void setDate(String date){
            TextView mDate=myyview.findViewById(R.id.sh_date);
            mDate.setText(date);
        }



    }


}
