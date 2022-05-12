package com.example.knox.ui.credentialUI;

import static com.example.knox.systemComponents.Requestor.decrypt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

                Credentials credit = Database.getInstance(dialog.getOwnerActivity())
                        .getFullCred(allCreds.get(holder.getBindingAdapterPosition()).url);
                urlEt.setText(credit.getUrl());
                userEt.setText(credit.getUName());
                passwordEt.setText(decrypt(credit.getPasswd()));

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!userEt.getText().toString().equals("") &&
                                !passwordEt.getText().toString().equals("") &&
                                !urlEt.getText().toString().equals("")) {
                            Credentials cred = new Credentials(userEt.getText().toString(),
                                    Requestor.encrypt(passwordEt.getText().toString()),
                                    urlEt.getText().toString());
                            if(!cred.equals(credit)) {
                                Database.getInstance(dialog.getOwnerActivity()).delete(credit);
                                Database.getInstance(dialog.getOwnerActivity()).insert(cred);
                                Toast.makeText(holder.edit.getContext(), "Added edited successfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else{
                                Toast.makeText(holder.edit.getContext(), "No changes detected", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(dialog.getOwnerActivity().getApplicationContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //create popup window
                final Dialog dialog = new Dialog(holder.edit.getContext());
                //We have added a title in the custom layout. So let's disable the default title.
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
                dialog.setCancelable(true);
                //Mention the name of the layout of your custom dialog.
                dialog.setContentView(R.layout.delete_dialog);
                Button confirm = dialog.findViewById(R.id.delete_confirm);
                Button deny = dialog.findViewById(R.id.delete_deny);
                deny.setBackgroundColor(Color.RED);

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Credentials credit = Database.getInstance(holder.edit.getContext())
                                .getFullCred(allCreds.get(holder.getBindingAdapterPosition()).url);
                        Database.getInstance(holder.edit.getContext()).delete(credit);
                        Toast.makeText(holder.edit.getContext(), "Credential deleted", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                deny.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
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
            //edit.setBackgroundColor(Color.GRAY);
            delete = binding.deleteButton;
            delete.setBackgroundColor(Color.RED);
        }

        @Override
        public String toString() {
            return super.toString() + " '"/* + mContentView.getText() + "'"*/;
        }
    }

    public class FragmentStarter extends AppCompatActivity{
        public FragmentStarter(){}
        public FragmentManager getFT(){
            return this.getSupportFragmentManager();
        }
    }
}