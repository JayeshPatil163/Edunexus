package com.example.test3;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*public class FilesAdapter extends ArrayAdapter<String> {

    public FilesAdapter(Context context, List<String> links) {
        super(context, 0, links);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String[] raw = getItem(position).split("#404#URIBABY",2);
        final String title = raw[0];
        final String type = raw[1];


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.file, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.item);
        TextView textView1 = convertView.findViewById(R.id.filetype);
        ImageView imageView = convertView.findViewById(R.id.itemIcon);

        if(type.equals("NotaFileIt'samessage"))
        {
            textView.setText(title);
        }
        else
        {
            textView.setText(title);

            switch (type) {
                case "application/pdf":
                    imageView.setImageResource(R.drawable.pdf);
                    textView1.setText("PDF");
                    break;

                case "image/jpeg":
                    imageView.setImageResource(R.drawable.jpeg);
                    textView1.setText("JPEG");
                    break;

                case "image/png":
                    imageView.setImageResource(R.drawable.jpeg);
                    textView1.setText("PNG");
                    break;

                case "image/gif":
                    imageView.setImageResource(R.drawable.jpeg);
                    textView1.setText("GIF");
                    break;

                case "text/plain":
                    imageView.setImageResource(R.drawable.txt);
                    textView1.setText("TXT");
                    break;

                case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                    imageView.setImageResource(R.drawable.pptx);
                    textView1.setText("GIF");
                    break;

                case "application/vnd.ms-powerpoint":
                    imageView.setImageResource(R.drawable.pptx);
                    textView1.setText("GIF");
                    break;

                case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                    imageView.setImageResource(R.drawable.xlsx);
                    textView1.setText("XLSX");
                    break;

                case "application/vnd.ms-excel":
                    imageView.setImageResource(R.drawable.xlsx);
                    textView1.setText("XLSX");
                    break;

                default:
                    imageView.setImageResource(R.drawable.common);
                    textView1.setText("DOCS");
            }

        }
        *//*if(!title.isEmpty())
            textView1.setText(title + ":");
        else
            textView1.setText("[Title unspecified]");*//*

        *//*convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLinkInBrowser(title);
            }
        });*//*

        return convertView;
    }

    *//*private void openLinkInBrowser(String link) {
        // Open the link in a browser or handle it as per your requirements
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            getContext().startActivity(intent);
        }
        catch (ActivityNotFoundException ae)
        {
            Toast.makeText(getContext(), "This link is invalid!", Toast.LENGTH_SHORT).show();
        }
    }*//*
}*/

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolder> {

   /* public FilesAdapter(Context context, List<String> links) {
        super(context, 0, links);
    }*/

    /*@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String[] raw = getItem(position).split("#404#URIBABY",2);
        final String title = raw[0];
        final String type = raw[1];


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.file, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.item);
        TextView textView1 = convertView.findViewById(R.id.filetype);
        ImageView imageView = convertView.findViewById(R.id.itemIcon);

        if(type.equals("NotaFileIt'samessage"))
        {
            textView.setText(title);
        }
        else
        {
            textView.setText(title);

            switch (type) {
                case "application/pdf":
                    imageView.setImageResource(R.drawable.pdf);
                    textView1.setText("PDF");
                    break;

                case "image/jpeg":
                    imageView.setImageResource(R.drawable.jpeg);
                    textView1.setText("JPEG");
                    break;

                case "image/png":
                    imageView.setImageResource(R.drawable.jpeg);
                    textView1.setText("PNG");
                    break;

                case "image/gif":
                    imageView.setImageResource(R.drawable.jpeg);
                    textView1.setText("GIF");
                    break;

                case "text/plain":
                    imageView.setImageResource(R.drawable.txt);
                    textView1.setText("TXT");
                    break;

                case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                    imageView.setImageResource(R.drawable.pptx);
                    textView1.setText("GIF");
                    break;

                case "application/vnd.ms-powerpoint":
                    imageView.setImageResource(R.drawable.pptx);
                    textView1.setText("GIF");
                    break;

                case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                    imageView.setImageResource(R.drawable.xlsx);
                    textView1.setText("XLSX");
                    break;

                case "application/vnd.ms-excel":
                    imageView.setImageResource(R.drawable.xlsx);
                    textView1.setText("XLSX");
                    break;

                default:
                    imageView.setImageResource(R.drawable.common);
                    textView1.setText("DOCS");
            }

        }
        *//*if(!title.isEmpty())
            textView1.setText(title + ":");
        else
            textView1.setText("[Title unspecified]");*//*

        *//*convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLinkInBrowser(title);
            }
        });*//*

        return convertView;
    }*/


    private List<FilesandMessages> messages;
    private final RecyclerViewInterface recyclerViewInterface;

    public FilesAdapter(List<FilesandMessages> messages, RecyclerViewInterface recyclerViewInterface) {
        this.messages = messages;
        this.recyclerViewInterface = recyclerViewInterface;
        //uploadstdmaterial uploadstdmaterial = new uploadstdmaterial(recyclerViewInterface);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.file, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FilesandMessages message = messages.get(position);

        //holder.linearLayoutMessage.setVisibility(View.VISIBLE);

        if(message.getMessage().isEmpty()) {
            holder.timestampTextView.setVisibility(View.GONE);
            holder.linearLayoutFile.setVisibility(View.VISIBLE);

            holder.senderTextView.setText(message.getTitle());

            switch (message.getType()) {
                case "application/pdf":
                    holder.messageTextView.setText("PDF");
                    holder.imageView.setImageResource(R.drawable.pdf);
                    break;

                case "image/jpeg":
                    holder.messageTextView.setText("JPEG");
                    holder.imageView.setImageResource(R.drawable.jpeg);
                    break;

                case "image/png":
                    holder.messageTextView.setText("PNG");
                    holder.imageView.setImageResource(R.drawable.jpeg);
                    break;

                case "image/gif":
                    holder.messageTextView.setText("GIF");
                    holder.imageView.setImageResource(R.drawable.jpeg);
                    break;

                case "text/plain":
                    holder.messageTextView.setText("TXT");
                    holder.imageView.setImageResource(R.drawable.txt);
                    break;

                case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                    holder.messageTextView.setText("GIF");
                    holder.imageView.setImageResource(R.drawable.pptx);
                    break;

                case "application/vnd.ms-powerpoint":
                    holder.messageTextView.setText("GIF");
                    holder.imageView.setImageResource(R.drawable.pptx);
                    break;

                case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                    holder.messageTextView.setText("XLSX");
                    holder.imageView.setImageResource(R.drawable.xlsx);
                    break;

                case "application/vnd.ms-excel":
                    holder.messageTextView.setText("XLSX");
                    holder.imageView.setImageResource(R.drawable.xlsx);
                    break;

                default:
                    holder.messageTextView.setText("DOCS");
                    holder.imageView.setImageResource(R.drawable.common);
            }

        } else if (message.getTitle().isEmpty() && message.getType().isEmpty()) {

            holder.timestampTextView.setVisibility(View.VISIBLE);
            holder.linearLayoutFile.setVisibility(View.GONE);
            holder.timestampTextView.setText(message.getMessage());

        } else {
            holder.timestampTextView.setVisibility(View.VISIBLE);
            holder.timestampTextView.setText(message.getMessage());
            holder.linearLayoutFile.setVisibility(View.VISIBLE);

            holder.senderTextView.setText(message.getTitle());

            switch (message.getType()) {
                case "application/pdf":
                    holder.messageTextView.setText("PDF");
                    holder.imageView.setImageResource(R.drawable.pdf);
                    break;

                case "image/jpeg":
                    holder.messageTextView.setText("JPEG");
                    holder.imageView.setImageResource(R.drawable.jpeg);
                    break;

                case "image/png":
                    holder.messageTextView.setText("PNG");
                    holder.imageView.setImageResource(R.drawable.jpeg);
                    break;

                case "image/gif":
                    holder.messageTextView.setText("GIF");
                    holder.imageView.setImageResource(R.drawable.jpeg);
                    break;

                case "text/plain":
                    holder.messageTextView.setText("TXT");
                    holder.imageView.setImageResource(R.drawable.txt);
                    break;

                case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                    holder.messageTextView.setText("GIF");
                    holder.imageView.setImageResource(R.drawable.pptx);
                    break;

                case "application/vnd.ms-powerpoint":
                    holder.messageTextView.setText("GIF");
                    holder.imageView.setImageResource(R.drawable.pptx);
                    break;

                case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                    holder.messageTextView.setText("XLSX");
                    holder.imageView.setImageResource(R.drawable.xlsx);
                    break;

                case "application/vnd.ms-excel":
                    holder.messageTextView.setText("XLSX");
                    holder.imageView.setImageResource(R.drawable.xlsx);
                    break;

                default:
                    holder.messageTextView.setText("DOCS");
                    holder.imageView.setImageResource(R.drawable.common);
            }

        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView senderTextView;
        public TextView messageTextView;
        public TextView timestampTextView;
        public ImageView imageView;
        public LinearLayout linearLayoutMessage, linearLayoutFile;

        public ViewHolder(View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.item);
            messageTextView = itemView.findViewById(R.id.filetype);
            timestampTextView = itemView.findViewById(R.id.messageContent);
            imageView = itemView.findViewById(R.id.itemIcon);
            linearLayoutFile = itemView.findViewById(R.id.onlyFile);
            linearLayoutMessage = itemView.findViewById(R.id.onlyMessage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null)
                    {
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION)
                        {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }

    /*private void openLinkInBrowser(String link) {

    TextView textView = convertView.findViewById(R.id.item);
        TextView textView1 = convertView.findViewById(R.id.filetype);
        ImageView imageView = convertView.findViewById(R.id.itemIcon);
        // Open the link in a browser or handle it as per your requirements
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            getContext().startActivity(intent);
        }
        catch (ActivityNotFoundException ae)
        {
            Toast.makeText(getContext(), "This link is invalid!", Toast.LENGTH_SHORT).show();
        }
    }*/
}

