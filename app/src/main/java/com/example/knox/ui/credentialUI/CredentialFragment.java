package com.example.knox.ui.credentialUI;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

    //Database db = Database.getInstance(getActivity());

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

        //System.out.println(testing + " - CredentialFragment.java");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        TextView content = new TextView(container.getContext());
        //content.findViewById(R.id.content);
        //content.setEllipsize(TextUtils.TruncateAt.END);
        //database creation; call dao.vaultDisplay() to get all credentials
        //Database db = Room.databaseBuilder(requireContext(), Database.class,
         //       "credentials").allowMainThreadQueries().build();
        //PasswordDAO dao = db.passDao();

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
            recyclerView.setAdapter(new MyCredentialRecyclerViewAdapter(Database.
                                                                        getInstance(getActivity()).
                                                                        getAllCreds()));
        }

        return view;
    }
}