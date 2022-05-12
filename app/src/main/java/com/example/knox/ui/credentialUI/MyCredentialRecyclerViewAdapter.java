package com.example.knox.ui.credentialUI;

import static com.example.knox.systemComponents.Requestor.decrypt;

import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.knox.R;
import com.example.knox.activities.VaultActivity;
import com.example.knox.systemComponents.Credentials;
import com.example.knox.systemComponents.Database;
import com.example.knox.systemComponents.Requestor;
import com.example.knox.ui.placeholder.PlaceholderContent.PlaceholderItem;
import com.example.knox.databinding.FragmentItemBinding;

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
                final Dialog dialog = new Dialog(holder.edit.getContext());
                //We have added a title in the custom layout. So let's disable the default title.
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
                dialog.setCancelable(true);
                //Mention the name of the layout of your custom dialog.
                dialog.setContentView(R.layout.password_dialog);
                TextView header = dialog.findViewById(R.id.randomDia);
                header.setText("Edit credential");
                //Initializing the views of the dialog.
                final EditText urlEt = dialog.findViewById(R.id.etURL);
                final EditText userEt = dialog.findViewById(R.id.etEmail);
                final EditText passwordEt = dialog.findViewById(R.id.etPassword);
                Button submitButton = dialog.findViewById(R.id.add_password);
                Button cancelButton = dialog.findViewById(R.id.cancel_password);

                Credentials cred = Database.getInstance(dialog.getOwnerActivity())
                        .getFullCred(allCreds.get(holder.getBindingAdapterPosition()).url);
                urlEt.setText(cred.getUrl());
                userEt.setText(cred.getUName());
                passwordEt.setText(decrypt(cred.getPasswd()));

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                dialog.show();
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