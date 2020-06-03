package com.pandasdroid.scanner.fileView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pandasdroid.scanner.PdfrendererActivity;
import com.pandasdroid.scanner.R;
import com.pandasdroid.scanner.persistance.Document;
import com.scanlibrary.PolygonView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.pandasdroid.scanner.MainActivity.file_;

public class FLViewHolder extends RecyclerView.ViewHolder {

    private ImageView categoryIcon;
    private TextView textViewLabel;
    //private TextView textViewTime;
    //private TextView textViewCategory;
    private TextView textPageCount;
    private LinearLayout itemLayout;
    private ActionMode.Callback actionModeCallbacks;
    private com.pandasdroid.scanner.fileView.FLAdapter adapter;
    private Document documnt;
    private LinearLayout ll_email;

    private Map<String, Integer> categoryImageMap = new HashMap<>();

    public FLViewHolder(View itemView, ActionMode.Callback actionModeCallbacks, com.pandasdroid.scanner.fileView.FLAdapter adapter ) {
        super(itemView);

        ll_email = itemView.findViewById(R.id.ll_image_email);
        this.categoryIcon =  itemView.findViewById(R.id.imageView);
        this.textViewLabel = itemView.findViewById(R.id.fileName);
        //this.textViewTime = itemView.findViewById(R.id.timeLabel);
        //this.textViewCategory = itemView.findViewById(R.id.categoryLabel);
        this.textPageCount = itemView.findViewById(R.id.pageCount);
        this.itemLayout = itemView.findViewById(R.id.relativeLayout);
        this.adapter = adapter;
        this.actionModeCallbacks  = actionModeCallbacks;

        categoryImageMap.put( "Others", R.drawable.ic_category_others );
        categoryImageMap.put( "Shopping", R.drawable.ic_category_shopping );
        categoryImageMap.put( "Vehicle", R.drawable.ic_category_vehicle );
        categoryImageMap.put( "Medical", R.drawable.ic_category_medical );
        categoryImageMap.put( "Legal", R.drawable.ic_category_legal );
        categoryImageMap.put( "Housing", R.drawable.ic_category_housing );
        categoryImageMap.put( "Books", R.drawable.ic_category_books );
        categoryImageMap.put( "Food", R.drawable.ic_category_food );
        categoryImageMap.put( "Banking", R.drawable.ic_category_banking );
        categoryImageMap.put( "Receipts", R.drawable.ic_category_receipt );
        categoryImageMap.put( "Manuals", R.drawable.ic_category_manuals );
        categoryImageMap.put( "Travel", R.drawable.ic_category_travel );
        categoryImageMap.put( "Notes", R.drawable.ic_category_notes );
        categoryImageMap.put( "ID", R.drawable.ic_category_id );
    }

    void selectItem(Document item) {
        if (this.adapter.multiSelect) {
            if (this.adapter.selectedItems.contains(item)) {
                this.adapter.selectedItems.remove(item);
                itemLayout.setBackgroundColor(Color.WHITE);

            } else {
                this.adapter.selectedItems.add(item);
                itemLayout.setBackgroundResource(R.color.colorPrimaryLight);
            }
        }
    }

    private void ShowDialog(final Document document) {
        final AlertDialog.Builder alertdialog = new AlertDialog.Builder(itemView.getContext());
        LayoutInflater inflater = (LayoutInflater) itemView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final AlertDialog builder = alertdialog.create();
        builder.setCancelable(false);
        assert inflater != null;
        View dialogLayout = inflater.inflate(R.layout.dialog_send_email, null,false);
        final EditText et_email = (EditText) dialogLayout.findViewById(R.id.et_email);
        final EditText et_sub = (EditText) dialogLayout.findViewById(R.id.et_subject);
        TextView tv_submit = (TextView) dialogLayout.findViewById(R.id.dialog_submit);
        TextView textView_close = (TextView) dialogLayout.findViewById(R.id.dialog_close);

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Do Share
                Toast.makeText(view.getContext(),et_email.getText().toString() + et_sub.getText().toString(),Toast.LENGTH_LONG).show();
                try {


                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    final File sd = Environment.getExternalStorageDirectory();
                    String baseDirectory = itemView.getContext().getString(R.string.base_storage_path);
                    String newFileName = baseDirectory + document.getPath();
                    File toOpen = new File( sd, newFileName );

                    Uri sharedFileUri = FileProvider.getUriForFile(itemView.getContext(), "com.pandasdroid.scanner.provider", toOpen);

                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("*/*");

                    emailIntent.putExtra(Intent.EXTRA_STREAM, sharedFileUri);//path of video
                    itemView.getContext().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    /*//intent.setDataAndType( sharedFileUri, "application/pdf");

                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { et_email.getText().toString() });
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,et_sub.getText().toString());
                    itemView.getContext().startActivity(intent);*/
                } catch(Exception e)  {
                    System.out.println("is exception raises during sending mail"+e);
                }
            }
        });

        textView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
            }
        });

        builder.setView(dialogLayout);
        builder.show();
        if (builder.getWindow() != null) {
            builder.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }
    }

    public void setDocument(final Document document ){

        this.documnt = document;
        this.textViewLabel.setText( document.getName() );

        this.ll_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ShowDialog(document);
                try {


                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    final File sd = Environment.getExternalStorageDirectory();
                    String baseDirectory = itemView.getContext().getString(R.string.base_storage_path);
                    String newFileName = baseDirectory + document.getPath();
                    File toOpen = new File( sd, newFileName );

                    Uri sharedFileUri = FileProvider.getUriForFile(itemView.getContext(), "com.pandasdroid.scanner.provider", toOpen);

                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("*/*");

                    emailIntent.putExtra(Intent.EXTRA_STREAM, sharedFileUri);//path of video
                    itemView.getContext().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    /*//intent.setDataAndType( sharedFileUri, "application/pdf");

                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { et_email.getText().toString() });
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,et_sub.getText().toString());
                    itemView.getContext().startActivity(intent);*/
                } catch(Exception e)  {
                    System.out.println("is exception raises during sending mail"+e);
                }
            }
        });

        //this.textViewTime.setText( document.getScanned() );
        //this.textViewCategory.setText( document.getCategory() );

        if( document.getPageCount() > 1 ) {
            this.textPageCount.setVisibility( View.VISIBLE );
            this.textPageCount.setText( String.valueOf(document.getPageCount()) + " Pages" );

        } else {
            this.textPageCount.setVisibility( View.GONE );

        }

        if (adapter.selectedItems.contains(document)) {
            itemLayout.setBackgroundColor(Color.LTGRAY);

        } else {
            itemLayout.setBackgroundColor(Color.WHITE);

        }

        Integer resourceId = categoryImageMap.get( document.getCategory() );
        if( resourceId == null ){
            this.categoryIcon.setImageResource(R.drawable.ic_category_others);

        } else {

            this.categoryIcon.setImageResource(resourceId);
        }

        this.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( adapter.multiSelect ){

                    selectItem(document);

                    if( adapter.selectedItems.size() == 0 ){
                        adapter.mActionMode.finish();

                    } else {
                        adapter.mActionMode.invalidate();
                    }

                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    final File sd = Environment.getExternalStorageDirectory();
                    String baseDirectory = v.getContext().getString(R.string.base_storage_path);
                    String newFileName = baseDirectory + document.getPath();
                    File toOpen = new File( sd, newFileName );

                    Uri sharedFileUri = FileProvider.getUriForFile(v.getContext(), "com.pandasdroid.scanner.provider", toOpen);
                    intent.setDataAndType( sharedFileUri, "application/pdf");
                    PackageManager pm = v.getContext().getPackageManager();
                    if (intent.resolveActivity(pm) != null) {
                        v.getContext().startActivity(intent);
                    }

                    //Intent intent = new Intent(v.getContext(), PdfrendererActivity.class);
                    //file_ = toOpen;
                    //v.getContext().startActivity(new Intent(intent));
                }

            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                adapter.mActionMode = ((AppCompatActivity)view.getContext()).startSupportActionMode(actionModeCallbacks);
                selectItem(document);
                return true;
            }
        });

    }


}
