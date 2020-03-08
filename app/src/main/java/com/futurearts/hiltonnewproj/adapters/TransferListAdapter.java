package com.futurearts.hiltonnewproj.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.futurearts.hiltonnewproj.R;
import com.futurearts.hiltonnewproj.activities.ZoomImageViewActivity;
import com.futurearts.hiltonnewproj.interfaces.CompletedListener;
import com.futurearts.hiltonnewproj.interfaces.RecyclerOperations;
import com.futurearts.hiltonnewproj.modelclasses.MaterialIssueDetails;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TransferListAdapter extends RecyclerView.Adapter<TransferListAdapter.ViewHolder> {

    List<MaterialIssueDetails> materialIssue = new ArrayList<MaterialIssueDetails>();
    List<String> materialKeys = new ArrayList<String>();
    Context activity;
    RecyclerOperations completedListener;

    public TransferListAdapter(Context activity, List<MaterialIssueDetails> materialIssueDetails,List<String> materialKeys, RecyclerOperations completedListener) {
        this.materialIssue =  materialIssueDetails;
        this.activity = activity;
        this.completedListener = completedListener;
        this.materialKeys=materialKeys;
    }

    public void updateItem(String barCode,int position){
        materialIssue.get(position).setWhereTo(barCode);
        notifyItemChanged(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transfer_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.jobNumber.setText("Job Num: " + materialIssue.get(position).getJob_Num());
        holder.partNumber.setText("Part Num: " + materialIssue.get(position).getPart_Num());
        holder.qtyShort.setText("Quantity : " + materialIssue.get(position).getQty_shortage());
        holder.tvWhereFrom.setText("Where From : "+materialIssue.get(position).getWhereFrom());

        if(materialIssue.get(position).getWhereTo().equals("")){
            holder.etWhereTo.setText("");
        }else{
            holder.etWhereTo.setText(materialIssue.get(position).getWhereTo());
        }


        holder.btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.etWhereTo.getText().toString().length()==0){
                    materialIssue.get(position).setWhereTo("");
                    Toast.makeText(activity, "Enter Where To", Toast.LENGTH_SHORT).show();
                }else{
                    materialIssue.get(position).setWhereTo(holder.etWhereTo.getText().toString());
                    holder.etWhereTo.setText("");
                    completedListener.onCompleted(position, materialIssue.get(position), materialKeys.get(position));
                }

            }
        });

        holder.imgScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completedListener.onBarCodeScanClicked(position);
            }
        });


    }
    @Override
    public int getItemCount() {
        return materialIssue.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView jobNumber,partNumber,qtyShort,tvWhereFrom;
        EditText etWhereTo;
        ImageView imgScan;
        RelativeLayout btnComplete;
        public ViewHolder(View itemView) {
            super(itemView);

            jobNumber=itemView.findViewById(R.id.jobNumber);
            partNumber=itemView.findViewById(R.id.partNumber);
            qtyShort=itemView.findViewById(R.id.qtyShort);
            tvWhereFrom=itemView.findViewById(R.id.tv_where_from);
            btnComplete=itemView.findViewById(R.id.btnComplete);
            etWhereTo=itemView.findViewById(R.id.etWhereTo);
            imgScan=itemView.findViewById(R.id.imgScan);

        }
    }
}
