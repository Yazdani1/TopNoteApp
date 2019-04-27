package yazdaniscodelab.topnoteapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

    String post_key;
    String mTitle,mBudget,mNote;


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
            protected void populateViewHolder(MyViewHolder viewHolder, final Data model, final int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setNote(model.getNote());
                viewHolder.setBudget(model.getBudget());
                viewHolder.setDate(model.getDate());

                viewHolder.myyview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        post_key=getRef(position).getKey();
                        mTitle=model.getTitle();
                        mBudget=model.getBudget();
                        mNote=model.getNote();

                        editData();
                    }
                });

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

    public void editData(){

        AlertDialog.Builder mydialog=new AlertDialog.Builder(this);
        LayoutInflater inflater=LayoutInflater.from(this);
        View mView=inflater.inflate(R.layout.edit_and_delete,null);
        mydialog.setView(mView);

        final AlertDialog dialog=mydialog.create();

        final EditText title=mView.findViewById(R.id.title);
        final EditText budget=mView.findViewById(R.id.budget);
        final EditText note=mView.findViewById(R.id.note);


        title.setText(mTitle);
        title.setSelection(mTitle.length());

        budget.setText(mBudget);
        budget.setSelection(mBudget.length());

        note.setText(mNote);
        note.setSelection(mNote.length());


        Button btnUpdate=mView.findViewById(R.id.update_btn);
        Button btnDelete=mView.findViewById(R.id.delete_btn);


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mTitle=title.getText().toString().trim();
                mBudget=budget.getText().toString().trim();
                mNote=note.getText().toString().trim();

                String mDate=DateFormat.getDateInstance().format(new Date());

                Data data=new Data(mTitle,mBudget,mNote,mDate,post_key);

                mDatabase.child(post_key).setValue(data);

                dialog.dismiss();

            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child(post_key).removeValue();
                dialog.dismiss();
            }
        });


        dialog.show();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
