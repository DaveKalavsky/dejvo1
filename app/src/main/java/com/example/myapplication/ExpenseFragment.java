package com.example.myapplication;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.model.Data;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;


public class ExpenseFragment extends Fragment {


    private FirebaseAuth mAuth;
    public DatabaseReference mExpenseDatabase;

    //recyclerView
    public RecyclerView recyclerView;
    public TextView expenseSumResult;

    //edit data item

    public EditText edtAmount;
    public EditText edtType;
    public EditText edtNote;

    public Button btnUpdate;
    public Button btnDelete;

    //data variable

    public String type;
    public String note;
    public int amount;

    public String post_key;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview=inflater.inflate(R.layout.fragment_expense, container, false);

        mAuth=FirebaseAuth.getInstance();

        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();

        mExpenseDatabase= FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);

        expenseSumResult=myview.findViewById(R.id.expense_txt_result);

        recyclerView=myview.findViewById(R.id.recycler_id_expense);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int expenseSum=0;

                for(DataSnapshot mysnapshot:dataSnapshot.getChildren()) {

                    Data data=mysnapshot.getValue(Data.class);
                    expenseSum+=data.getAmount();

                    String srtExpensesum=String.valueOf(expenseSum);

                    expenseSumResult.setText(srtExpensesum+".00");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return myview;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data,MyViewHolder>adapter=new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (

                Data.class,
                R.layout.expense_recycler_data,
                MyViewHolder.class,
                mExpenseDatabase

                )    {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, final Data model, final int position) {

                viewHolder.setDate(model.getDate());
                viewHolder.setType(model.getType());
                viewHolder.setNote(model.getNote());
                viewHolder.setAmount(model.getAmount());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        post_key=getRef(position).getKey();
                        type=model.getType();
                        note=model.getNote();
                        amount=model.getAmount();

                        updateDataItem();

                    }
                });


            }

        };

        recyclerView.setAdapter(adapter);
    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mView=itemView;

        }

        private void  setDate(String date) {
            TextView mDate=mView.findViewById(R.id.date_txt_expense);
            mDate.setText(date);


        }

        private void setType(String type) {
            TextView mType=mView.findViewById(R.id.type_txt_expense);
            mType.setText(type);

        }

        private void setNote(String note) {
            TextView mNote=mView.findViewById(R.id.note_txt_expense);
            mNote.setText(note);

        }

        private void setAmount(int amount) {
            TextView mAmount=mView.findViewById(R.id.amount_txt_expense);

            String stramount=String.valueOf(amount);
            mAmount.setText(stramount);

        }

    }


    public void updateDataItem() {

        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview=inflater.inflate(R.layout.update_data_item,null);
        mydialog.setView(myview);

        edtAmount=myview.findViewById(R.id.ammount_edt);
        edtNote=myview.findViewById(R.id.note_edt);
        edtType=myview.findViewById(R.id.type_edt);

        edtType.setText(type);
        edtType.setSelection(type.length());

        edtNote.setText(note);
        edtNote.setSelection(note.length());

        edtAmount.setText(String.valueOf(amount));
        edtAmount.setSelection(String.valueOf(amount).length());



        btnUpdate=myview.findViewById(R.id.btn_upd_update);
        btnDelete=myview.findViewById(R.id.btnuPD_Delete);

        final AlertDialog dialog=mydialog.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type=edtType.getText().toString().trim();
                note=edtNote.getText().toString().trim();

                String stammount=String.valueOf(amount);

                stammount=edtAmount.getText().toString().trim();

                int intamount=Integer.parseInt(stammount);

                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data=new Data(intamount,type,note,post_key,mDate);

                mExpenseDatabase.child(post_key).setValue(data);

                dialog.dismiss();

            }
        });

       btnDelete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               mExpenseDatabase.child(post_key).removeValue();
               dialog.dismiss();

           }
       });

       dialog.show();


    }



}
