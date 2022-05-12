package com.example.knox.ui.credentialUI;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.knox.systemComponents.Credentials;
import com.example.knox.ui.placeholder.PlaceholderContent.PlaceholderItem;
import com.example.knox.databinding.FragmentItemBinding;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyCredentialRecyclerViewAdapter extends RecyclerView.Adapter<MyCredentialRecyclerViewAdapter.ViewHolder> {

    private final List<Credentials> allCreds;


    public MyCredentialRecyclerViewAdapter(List<Credentials> creds) {
        allCreds = creds;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = allCreds.get(position);
        holder.mIdView.setText(allCreds.get(position).url);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo: popup window to edit the login
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo: popup window to delete a credential
            }
        });
        //holder.itemView.setOnClickListener(holder.);
        //holder.mContentView.setText(allCreds.get(position).uName);
//        holder.mIdView.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).content);
    }

    @Override
    public int getItemCount() {
        return allCreds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        //public final TextView mContentView;
        public Credentials mItem;
        public final Button edit;
        public final Button delete;
        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            edit = binding.editButton;
            delete = binding.deleteButton;
            //mContentView = binding.content;
        }

        @Override
        public String toString() {
            return super.toString() + " '"/* + mContentView.getText() + "'"*/;
        }
    }
}