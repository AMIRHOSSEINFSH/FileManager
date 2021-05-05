package com.example.filemanager.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filemanager.Adapter.FileAdapter;
import com.example.filemanager.MainActivity;
import com.example.filemanager.R;
import com.example.filemanager.StorageHelper;
import com.example.filemanager.ViewType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class fileListFragment extends Fragment implements FileAdapter.FileItemEventListener {

    private String path;
    private FileAdapter fileAdapter;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path = getArguments().getString("path");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files, container, false);
        recyclerView = view.findViewById(R.id.rv_files);
        layoutManager=new GridLayoutManager(getContext(),MainActivity.viewType.getValue(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        File currentFolder = new File(path);
        if (StorageHelper.isExternalStorageReadable()){
            File[] files = currentFolder.listFiles();
            fileAdapter = new FileAdapter(Arrays.asList(files), this);
            recyclerView.setAdapter(fileAdapter);
        }


        TextView pathTv = view.findViewById(R.id.tv_files_path);

        pathTv.setText(currentFolder.getPath());
        //pathTv.setText(currentFolder.getName().equalsIgnoreCase("files")?"External Storage":currentFolder.getName());

        view.findViewById(R.id.iv_files_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        return view;
    }

    /*@Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final Handler handler = new Handler();
        final int delay = 5000 ; //1000 milliseconds = 1 sec

        handler.postDelayed(new Runnable(){
            public void run(){

                for (int i = 0; i < fileAdapter.getFiles().size(); i++) {
                    if (!fileAdapter.getFiles().get(i).isDirectory() && !fileAdapter.getFiles().get(i).isFile()){
                        fileAdapter.getFiles().remove(i);
                        fileAdapter.notifyItemRemoved(i);
                    }
                }

                //fileAdapter.notifyDataSetChanged();

                handler.postDelayed(this, delay);
            }
        }, delay);
    }*/

    @Override
    public void onFileItemClick(File file) {
        if (file.isDirectory()) {
            ((MainActivity) getActivity()).listFiles(file.getPath());
        }
    }

    @Override
    public void onDeleteFileItemClick(File file) {
        if (StorageHelper.isExternalStorageWritable()){
            if (file.delete()){
                fileAdapter.deleteFile(file);
            }
        }


    }

    @Override
    public void onCopyFileItemClick(File file) {
        if (StorageHelper.isExternalStorageWritable()){
            copy(file,getDestinationPath());
            Toast.makeText(getContext(), "File is Copied !!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onMOveFileItemClick(File file) {

        if (StorageHelper.isExternalStorageWritable()){
            copy(file,getDestinationPath());
            onDeleteFileItemClick(file);
            Toast.makeText(getContext(), "File is Copied !!", Toast.LENGTH_SHORT).show();
        }

    }

    private void copy(File source,File destination) {

        if (!destination.exists()){
            destination.mkdir();
        }

        File destinationFile=new File(destination.getPath()+File.separator+source.getName());
        if (destinationFile.exists()){
            destinationFile=new File(destinationFile.getPath()+"-C");
        }

        FileInputStream fileInputStream=null;
        FileOutputStream fileOutputStream=null;
        try {
            fileInputStream=new FileInputStream(source);
            fileOutputStream=new FileOutputStream(destinationFile);
            //todo Any we read from file will store in Array of bytes (there size of reading is 1024)
            byte[] buffer=new byte[1024];
            //todo the lenght is for the size of bytes that we are reading the minimum is 1 and maximum is 1024 there
            int lenght;
            while ( (lenght=fileInputStream.read(buffer))>0 ){
                //todo fileOutPutStream write source file into the destination part by part(ArrayBytes)
                fileOutputStream.write(buffer,0,lenght);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try {
                fileInputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public File getDestinationPath(){
        File file=new File(getContext().getExternalFilesDir(null).getPath()+File.separator+"Destination");
        if (!file.exists()) {
            file.mkdir();
        }

        return file;
    }


    public void createNewFolder(String folderName) {

        if (StorageHelper.isExternalStorageWritable()){
            File newFolder = new File(path + File.separator + folderName);
            if (!newFolder.exists()) {
                if (newFolder.mkdir()) {
                    fileAdapter.addFile(newFolder);
                    recyclerView.smoothScrollToPosition(0);
                }
            }
        }

    }

    public void Search(String query){
        fileAdapter.Search(query);
    }

    public void setViewType(ViewType viewType){
        if (fileAdapter!=null){
            fileAdapter.setViewType(viewType);
            if (viewType==ViewType.ROW){
                layoutManager.setSpanCount(1);
            }else{
                layoutManager.setSpanCount(2);
            }
            recyclerView.setAdapter(fileAdapter);
        }
    }
}
