package com.example.myapplication;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class IncomeFragment extends Fragment {

    //firebase databaza

    private FirebaseAuth mAuth;
    public DatabaseReference mIncomeDatabase;

    //recyclerView

    private RecyclerView recyclerView;

    public TextView incomeTotalSum;


    public IncomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_income, container, false);

        mAuth=FirebaseAuth.getInstance();

        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();

        mIncomeDatabase=FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);

        incomeTotalSum=myview.findViewById(R.id.income_txt_result);

        recyclerView=myview.findViewById(R.id.recycler_id_income);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int totalvalue = 0;

                for (DataSnapshot mysnapshot:dataSnapshot.getChildren()) {

                    Data data=mysnapshot.getValue(Data.class);

                    totalvalue+=data.getAmount();

                    String stTotalvalue=String.valueOf(totalvalue);

                    incomeTotalSum.setText(stTotalvalue);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return myview;
    }

    @Override
    public void onStart()  {

        super.onStart();

        FirebaseRecyclerAdapter<Data,MyviewHolder>adapter=new FirebaseRecyclerAdapter<Data, MyviewHolder>

                (
                Data.class,
                R.layout.income_recycler_data,
                MyviewHolder.class,
                        mIncomeDatabase
                )

                {
            @Override
            protected void populateViewHolder(MyviewHolder viewHolder, Data model, int position) {

                viewHolder.setType(model.getType());
                viewHolder.setNote(model.getNote());
                viewHolder.setDate(model.getDate());
                viewHolder.setAmount(model.getAmount());


            }
        };


        recyclerView.setAdapter(adapter);

    }
    public static class MyviewHolder extends RecyclerView.ViewHolder{

        View mView;


        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }

        private void setType(String type) {

            TextView mType=mView.findViewById(R.id.type_txt_income);
            mType.setText(type);


        }

        private void setNote(String note) {
            TextView mNote=mView.findViewById(R.id.note_txt_income);
            mNote.setText(note);

        }

        private void setDate(String date) {

            TextView mDate=mView.findViewById(R.id.date_txt_income);
            mDate.setText(date);

        }

        private void setAmount (int amount) {

            TextView mAmount=mView.findViewById(R.id.amount_txt_income);

            String stamount=String.valueOf(amount);
            mAmount.setText(stamount);


        }


    }


}
