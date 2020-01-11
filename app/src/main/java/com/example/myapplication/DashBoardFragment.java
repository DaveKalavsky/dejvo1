package com.example.myapplication;


import android.app.AlertDialog;
import android.graphics.drawable.Animatable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import com.example.myapplication.model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.DateFormat;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashBoardFragment extends Fragment {



    private FloatingActionButton fab_main_btn;
    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;

    //Floating button textview

    private TextView fab_income_txt;
    private TextView fab_expense_txt;

    //Dashboard income and expense result

    private TextView totalIncomeResult;
    private TextView totalExpenseResult;


    private boolean isOpen=false;
    private Animation FadOpen,FadClose;
    //animation

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    public DatabaseReference mExpenseDatabase;

    private RecyclerView mRecyclerIncome;
    private RecyclerView mRecyclerExpense;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View myview= inflater.inflate(R.layout.fragment_dash_board, container, false);


        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();


        mIncomeDatabase= FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDatabase=FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);

        mIncomeDatabase.keepSynced(true);
        mExpenseDatabase.keepSynced(true);

        //connect floating button to layout

        fab_main_btn=myview.findViewById(R.id.fb_main_plus_btn);
        fab_income_btn=myview.findViewById(R.id.income_Ft_btn);
        fab_expense_btn=myview.findViewById(R.id.expense_Ft_btn);

        fab_income_txt=myview.findViewById(R.id.income_ft_text);
        fab_expense_txt=myview.findViewById(R.id.expense_ft_text);

        //Total income and expense
        totalIncomeResult=myview.findViewById(R.id.income_set_result);
        totalExpenseResult=myview.findViewById(R.id.expense_set_result);

        //Recycler

        mRecyclerIncome=myview.findViewById(R.id.recycler_income);
        mRecyclerExpense=myview.findViewById(R.id.recycler_expense);

        //animation connect
        FadOpen= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        FadClose=AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);

        fab_main_btn.setOnClickListener(new View.OnClickListener()
                 {
            @Override
            public void onClick(View view) {
                  if (isOpen) {

                   addData();

                    fab_income_btn.startAnimation(FadClose);
                    fab_expense_btn.startAnimation(FadClose);
                    fab_income_btn.setClickable(false);
                    fab_expense_btn.setClickable(false);

                    fab_income_txt.startAnimation(FadClose);
                    fab_expense_txt.startAnimation(FadClose);
                    fab_income_txt.setClickable(false);
                    fab_expense_txt.setClickable(false);
                    isOpen=false;
                }else {

                    fab_income_btn.startAnimation(FadOpen);
                    fab_expense_btn.startAnimation(FadOpen);
                    fab_income_btn.setClickable(true);
                    fab_expense_btn.setClickable(true);

                    fab_income_txt.startAnimation(FadOpen);
                    fab_expense_txt.startAnimation(FadOpen);
                    fab_income_txt.setClickable(true);
                    fab_expense_txt.setClickable(true);
                    isOpen=true;

                }


            }
        });

        //vypocitanie celkoveho prijmu

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int totalsum =0;

                for(DataSnapshot mysnap:dataSnapshot.getChildren()){

                    Data data=mysnap.getValue(Data.class);

                    totalsum+=data.getAmount();
                    String stResult=String.valueOf(totalsum);
                    totalIncomeResult.setText(stResult+".00");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


        //vypocitanie celkovych vydajov

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int totalsum = 0;

                for(DataSnapshot mysnapshot:dataSnapshot.getChildren()) {

                    Data data=mysnapshot.getValue(Data.class);
                    totalsum+=data.getAmount();

                    String strTotalSum=String.valueOf(totalsum);

                    totalExpenseResult.setText(strTotalSum+".00");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Recycler

        LinearLayoutManager layoutManagerIncome=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

        layoutManagerIncome.setStackFromEnd(true);
        layoutManagerIncome.setReverseLayout(true);
        mRecyclerIncome.setHasFixedSize(true);
        mRecyclerIncome.setLayoutManager(layoutManagerIncome);

        LinearLayoutManager layoutManagerExpense= new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerExpense.setReverseLayout(true);
        layoutManagerExpense.setStackFromEnd(true);
        mRecyclerExpense.setHasFixedSize(true);
        mRecyclerExpense.setLayoutManager(layoutManagerExpense);

        return myview;
    }



    private void ftAnimation() {

        if (isOpen) {

            addData();

            fab_income_btn.startAnimation(FadClose);
            fab_expense_btn.startAnimation(FadClose);
            fab_income_btn.setClickable(false);
            fab_expense_btn.setClickable(false);

            fab_income_txt.startAnimation(FadClose);
            fab_expense_txt.startAnimation(FadClose);
            fab_income_txt.setClickable(false);
            fab_expense_txt.setClickable(false);
            isOpen=false;
        }else {

            fab_income_btn.startAnimation(FadOpen);
            fab_expense_btn.startAnimation(FadOpen);
            fab_income_btn.setClickable(true);
            fab_expense_btn.setClickable(true);

            fab_income_txt.startAnimation(FadOpen);
            fab_expense_txt.startAnimation(FadOpen);
            fab_income_txt.setClickable(true);
            fab_expense_txt.setClickable(true);
            isOpen=true;

        }



    }

    private void addData() {

        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                incomeDataInsert();


            }


        });

        fab_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                expenseDataInsert();
            }
        });

    }

    public void incomeDataInsert() {

        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());

        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview=inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        mydialog.setView(myview);
         final AlertDialog dialog=mydialog.create();

         dialog.setCancelable(false);

        final EditText edtAmount=myview.findViewById(R.id.ammount_edt);
        final EditText edtType=myview.findViewById(R.id.type_edt);
        final EditText edtNote=myview.findViewById(R.id.note_edt);

        Button btnSave=myview.findViewById(R.id.btnSave);
        Button btnCancel=myview.findViewById(R.id.btnCancel);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String type = edtType.getText().toString().trim();
                String amount = edtAmount.getText().toString().trim();
                String note = edtNote.getText().toString().trim();


                if (TextUtils.isEmpty(type)) {
                    edtType.setError("Required Field..");
                    return;

                }

                if (TextUtils.isEmpty(amount)) {

                    edtAmount.setError("Required Field..");
                    return;

                }
                int ourammontint = Integer.parseInt(amount);

                if (TextUtils.isEmpty(note)) {

                    edtNote.setError("Required field..");

                    return;
                }

                String id = mIncomeDatabase.push().getKey();

                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(ourammontint, type, note, id, mDate);

                mIncomeDatabase.child(id).setValue(data);
                Toast.makeText(getActivity(), "Data added.", Toast.LENGTH_SHORT).show();

                ftAnimation();

                dialog.dismiss();

            }

        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ftAnimation();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void expenseDataInsert() {

        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());

        View myview=inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        mydialog.setView(myview);



        final AlertDialog dialog=mydialog.create();

        mydialog.setCancelable(false);

        final EditText amount=myview.findViewById(R.id.ammount_edt);
        final EditText type=myview.findViewById(R.id.type_edt);
        final EditText note=myview.findViewById(R.id.note_edt);



        Button btnSave=myview.findViewById(R.id.btnSave);
        Button btnCancel=myview.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tmAmount=amount.getText().toString().trim();
                String tmtype=type.getText().toString().trim();
                String tmnote=note.getText().toString().trim();

                if (TextUtils.isEmpty(tmAmount)) {
                    amount.setError("Required Field..");
                    return;

                }

                int inamount=Integer.parseInt(tmAmount);

                if(TextUtils.isEmpty(tmtype)){
                    type.setError("Required Field..");
                    return;

                }
                if (TextUtils.isEmpty(tmnote)){

                    note.setError("Required Field..");
                    return;
                }

                String id=mExpenseDatabase.push().getKey();
                String mDate=DateFormat.getDateInstance().format(new Date());

                Data data=new Data(inamount,tmtype,tmnote,id,mDate);
                mExpenseDatabase.child(id).setValue(data);


                ftAnimation();

                Toast.makeText(getActivity(),"Data added",Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ftAnimation();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public void onStart() {

        super.onStart();

        FirebaseRecyclerAdapter<Data,IncomeViewHolder>incomeAdapter=new FirebaseRecyclerAdapter<Data, IncomeViewHolder>
                (
                        Data.class,
                        R.layout.dashboard_income,
                        DashBoardFragment.IncomeViewHolder.class,
                        mIncomeDatabase
                ) {
            @Override
            protected void populateViewHolder(IncomeViewHolder viewHolder, Data model, int position) {

                viewHolder.setIncomeType(model.getType());
                viewHolder.setIncomeAmount(model.getAmount());
                viewHolder.setIncomeDate(model.getDate());

            }
        };

        mRecyclerIncome.setAdapter(incomeAdapter);


        FirebaseRecyclerAdapter<Data,ExpenseViewHolder>expenseAdapter=new FirebaseRecyclerAdapter<Data, ExpenseViewHolder>

                (
                        Data.class,
                        R.layout.dashboard_expense,
                        DashBoardFragment.ExpenseViewHolder.class,
                        mExpenseDatabase
                ) {
            @Override
            protected void populateViewHolder(ExpenseViewHolder viewHolder, Data model, int position) {

                viewHolder.setExpenseType(model.getType());
                viewHolder.setExpenseAmount(model.getAmount());
                viewHolder.setExpenseDate(model.getDate());

            }
        };

        mRecyclerExpense.setAdapter(expenseAdapter);

    }
    //for income Data

    public static class IncomeViewHolder extends RecyclerView.ViewHolder{

        View mIncomeView;


        public IncomeViewHolder(View itemView) {
            super(itemView);
            mIncomeView=itemView;

        }

        public void setIncomeType(String type) {

          TextView mtype=mIncomeView.findViewById(R.id.type_Income_ds);
          mtype.setText(type);

        }

        public void setIncomeAmount(int amount) {

            TextView mAmount=mIncomeView.findViewById(R.id.amount_income_ds);

            String strAmount=String.valueOf(amount);

            mAmount.setText(strAmount);



        }

        public void setIncomeDate(String date) {

            TextView mDate=mIncomeView.findViewById(R.id.date_income_ds);
            mDate.setText(date);


        }




    }



    public static class ExpenseViewHolder extends RecyclerView.ViewHolder{


        View mExpenseView;

        public ExpenseViewHolder(View itemView) {

            super(itemView);
            mExpenseView=itemView;

        }

        public void setExpenseType(String type) {

            TextView mtype=mExpenseView.findViewById(R.id.type_expense_ds);
            mtype.setText(type);

        }

        public void setExpenseAmount(int amount) {

            TextView mAmount= mExpenseView.findViewById(R.id.amount_expense_ds);
            String stAmount=String.valueOf(amount);
            mAmount.setText(stAmount);

        }

        public void setExpenseDate(String date) {

            TextView mDate=mExpenseView.findViewById(R.id.date_expense_ds);
            mDate.setText(date);

        }

    }



}


