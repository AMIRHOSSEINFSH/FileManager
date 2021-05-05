package com.example.filemanager.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import com.example.filemanager.R;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddNewFolderDialog extends DialogFragment {

    private AddNewFolderCallBack addNewFolderCallBack;

    @Override
    public void onAttach(@NonNull Context context) {
        addNewFolderCallBack = (AddNewFolderCallBack) context;
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_new_folder, null, false);
        builder.setView(view);

        TextInputEditText et_add = view.findViewById(R.id.et_addNewFolder);
        TextInputLayout et1 = view.findViewById(R.id.etl_addNewFolder);
        MaterialButton btn_Create = view.findViewById(R.id.btn_addNewFolder_create);
        btn_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_add.getText().length() > 0) {
                    addNewFolderCallBack.OnCreateFolderButtonClick(et_add.getText().toString());
                    dismiss();
                } else {
                    et1.setError("Folder Name Can not be Empty!!");
                }
            }
        });
        return builder.create();
    }

    public interface AddNewFolderCallBack {
        void OnCreateFolderButtonClick(String folderName);
    }

}
