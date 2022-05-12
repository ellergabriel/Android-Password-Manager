package com.example.knox.ui.credentialUI;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
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

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            //mContentView = binding.content;
        }

        @Override
        public String toString() {
            return super.toString() + " '"/* + mContentView.getText() + "'"*/;
        }
    }
}