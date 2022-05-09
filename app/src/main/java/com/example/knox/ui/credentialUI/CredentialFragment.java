package com.example.knox.ui.credentialUI;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.knox.R;

import com.example.knox.systemComponents.Credentials;
import com.example.knox.systemComponents.Database;
import com.example.knox.systemComponents.PasswordDAO;
import com.example.knox.ui.placeholder.PlaceholderContent;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class CredentialFragment extends Fragment {

    Database db = Database.getInstance(getActivity());
    List<Credentials> testing= db.passDao().getAllCreds();

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CredentialFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CredentialFragment newInstance(int columnCount) {
        CredentialFragment fragment = new CredentialFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        System.out.println(testing);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        //database creation; call dao.vaultDisplay() to get all credentials
        Database db = Room.databaseBuilder(requireContext(), Database.class,
                "credentials").allowMainThreadQueries().build();
        PasswordDAO dao = db.passDao();

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
//            recyclerView.setAdapter(new MyCredentialRecyclerViewAdapter(PlaceholderContent.ITEMS));
            recyclerView.setAdapter(new MyCredentialRecyclerViewAdapter(testing));
        }
        return view;
    }
}