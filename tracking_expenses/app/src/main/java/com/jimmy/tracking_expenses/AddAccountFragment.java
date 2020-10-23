package com.jimmy.tracking_expenses;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jimmy.tracking_expenses.StockDataBase.Account;
import com.jimmy.tracking_expenses.StockDataBase.DataBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "param";

    // TODO: Rename and change types of parameters
    private String viewAccountType;
    MyListAdapter myListAdapter;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    List<Account> assetsAccount = new ArrayList<>();
    List<Account> liabilitiesAccount = new ArrayList<>();
    public AddAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AddCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddAccountFragment newInstance(String param1) {
        AddAccountFragment fragment = new AddAccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            viewAccountType = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_account, container, false);

        recyclerView = v.findViewById(R.id.AddAccountRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        myListAdapter = new MyListAdapter();
        recyclerView.setAdapter(myListAdapter);


        new Thread(() -> {
            assetsAccount =
                    DataBase.getInstance(getContext()).getDataUao().getClassificationAccount("assets");
            liabilitiesAccount =
                    DataBase.getInstance(getContext()).getDataUao().getClassificationAccount("liabilities");
            Log.i("assets", String.valueOf(assetsAccount));
            switch (viewAccountType) {
                case "assets":
                    for (Account now : assetsAccount) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("Id", now.getAccountName());
                        hashMap.put("money", String.valueOf(now.getInitialMoney()));

                        arrayList.add(hashMap);
                    }
                    break;
                case "liabilities":
                    for (Account now : liabilitiesAccount) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("Id", now.getAccountName());
                        hashMap.put("money", String.valueOf(now.getInitialMoney()));

                        arrayList.add(hashMap);
                    }
                    break;
            }
            getActivity().runOnUiThread(()-> myListAdapter.notifyDataSetChanged());

        }).start();



        fab = v.findViewById(R.id.AddAccountFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("Id","座號："+String.format("%02d",1));
                hashMap.put("money",String.valueOf(new Random().nextInt(80) + 20));
                arrayList.add(hashMap);
                myListAdapter.notifyDataSetChanged();
            }
        });

        return v;
    }

    private class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{

        class ViewHolder extends RecyclerView.ViewHolder{
            private TextView tv_id,tv_money;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_id = itemView.findViewById(R.id.accountIdTextView);
                tv_money = itemView.findViewById(R.id.accountMoneyTextView);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_account_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.tv_id.setText(arrayList.get(position).get("Id"));
            holder.tv_money.setText(arrayList.get(position).get("money"));
        }



        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
}