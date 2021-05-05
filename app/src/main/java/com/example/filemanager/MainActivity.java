package com.example.filemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.filemanager.fragments.AddNewFolderDialog;
import com.example.filemanager.fragments.fileListFragment;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.io.File;

public class MainActivity extends AppCompatActivity implements AddNewFolderDialog.AddNewFolderCallBack {

   public static ViewType viewType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewType=ViewType.ROW;

        EditText etSearch=findViewById(R.id.etSearch);

        MaterialButtonToggleGroup materialButtonToggleGroup=findViewById(R.id.toggleGroup_main);
        materialButtonToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.frame_main_fragmentContainer);
                if (checkedId==R.id.btn_main_list && isChecked){
                    if (fragment instanceof fileListFragment){
                        viewType=ViewType.ROW;
                        ((fileListFragment) fragment).setViewType(ViewType.ROW);
                    }
                }
                else if (checkedId==R.id.btn_main_grid && isChecked){
                    if (fragment instanceof fileListFragment){
                        viewType=ViewType.GRID;
                        ((fileListFragment) fragment).setViewType(ViewType.GRID);
                    }
                }
            }
        });

        if (StorageHelper.isExternalStorageReadable()){
            File externalFilesDir = getExternalFilesDir(null);
            listFiles(externalFilesDir.getPath(), false);
        }


        findViewById(R.id.iv_main_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewFolderDialog dialog=new AddNewFolderDialog();
                dialog.show(getSupportFragmentManager(),null);
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.frame_main_fragmentContainer);
                if (fragment instanceof fileListFragment){
                    ((fileListFragment) fragment).Search(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void listFiles(String path, Boolean addToBackStack) {
        fileListFragment fileListFragment = new fileListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        fileListFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main_fragmentContainer, fileListFragment);
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    public void listFiles(String path) {
        listFiles(path, true);
    }


    @Override
    protected void onPause() {
        Toast.makeText(this, "it is paused!!", Toast.LENGTH_SHORT).show();
        super.onPause();
    }

    @Override
    public void OnCreateFolderButtonClick(String folderName) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_main_fragmentContainer);
        if (fragment instanceof fileListFragment) {
            ((fileListFragment) fragment).createNewFolder(folderName);
        }

    }

}