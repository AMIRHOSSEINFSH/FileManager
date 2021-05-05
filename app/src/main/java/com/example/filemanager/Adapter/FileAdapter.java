package com.example.filemanager.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filemanager.MainActivity;
import com.example.filemanager.R;
import com.example.filemanager.ViewType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.MyViewHolder> {

    public List<File> getFiles() {
        return files;
    }

    private List<File> files;
    private List<File> filteredFiles;
    private FileItemEventListener itemClickEventListener;
    private ViewType viewType=ViewType.ROW;

    public FileAdapter(List<File> files, FileItemEventListener itemClickEventListener) {
        this.files = new ArrayList<>(files);
        this.filteredFiles=this.files;
        this.itemClickEventListener = itemClickEventListener;
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(ViewType.ROW.getValue()==MainActivity.viewType.getValue() ? R.layout.item_file : R.layout.item_file_grid/*viewType==ViewType.ROW.getValue() ? R.layout.item_file : R.layout.item_file_grid*/, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindFile(filteredFiles.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView fileIconIv, img_pop;
        TextView fileNameTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fileIconIv = itemView.findViewById(R.id.iv_file);
            img_pop = itemView.findViewById(R.id.iv_file_more);
            fileNameTv = itemView.findViewById(R.id.tv_file_name);
        }

        public void bindFile(File file) {
            if (file.isDirectory()) {
                fileIconIv.setImageResource(R.drawable.ic_folder_black_32dp);
            } else {
                fileIconIv.setImageResource(R.drawable.ic_file_black_32dp);
            }
            fileNameTv.setText(file.getName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickEventListener.onFileItemClick(file);
                }
            });

            img_pop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_file_item, popupMenu.getMenu());
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.menu_item_delete:
                                    itemClickEventListener.onDeleteFileItemClick(file);
                                    break;
                                case R.id.menu_item_copy:
                                    itemClickEventListener.onCopyFileItemClick(file);
                                    break;
                                case R.id.menu_item_move:
                                    itemClickEventListener.onMOveFileItemClick(file);
                                    break;
                            }
                            return false;
                        }
                    });
                }
            });
        }


    }

    public ViewType getViewType() {
        return viewType;
    }

    public interface FileItemEventListener {
        void onFileItemClick(File file);

        void onDeleteFileItemClick(File file);

        void onCopyFileItemClick(File file);

        void onMOveFileItemClick(File file);
    }

    public void addFile(File file) {
        files.add(0, file);
        notifyItemInserted(0);
    }

    public void deleteFile(File file) {

        int index = files.indexOf(file);
        if (index > -1) {
            files.remove(index);
            notifyItemRemoved(index);
        }

    }

    public void Search(String query){
        Log.i("TAG", "Search: "+query);
        if (query.length()>0){
            List<File> result=new ArrayList<>();
            for (File file:this.files) {
                if (file.getName().toLowerCase().contains(query.toLowerCase())){
                    result.add(file);
                }
            }
            this.filteredFiles=result;
            notifyDataSetChanged();
        }
        else{
            this.filteredFiles=files;
            notifyDataSetChanged();
        }

    }

    @Override
    public int getItemViewType(int position) {
        return viewType.getValue();
    }
}

