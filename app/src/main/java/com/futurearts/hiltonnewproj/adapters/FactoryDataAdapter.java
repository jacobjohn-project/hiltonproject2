package com.futurearts.hiltonnewproj.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.futurearts.hiltonnewproj.R;
import com.futurearts.hiltonnewproj.interfaces.CompletedListener;
import com.futurearts.hiltonnewproj.activities.ZoomImageViewActivity;
import com.futurearts.hiltonnewproj.modelclasses.MaterialIssueDetails;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FactoryDataAdapter extends RecyclerView.Adapter<FactoryDataAdapter.ViewHolder> {

    List<MaterialIssueDetails> materialIssue = new ArrayList<MaterialIssueDetails>();
    List<String> materialKeys = new ArrayList<String>();
    Context activity;
    CompletedListener completedListener;

    public FactoryDataAdapter(Context activity, List<MaterialIssueDetails> materialIssueDetails,List<String> materialKeys, CompletedListener completedListener) {

        this.materialIssue =  materialIssueDetails;
        this.activity = activity;
        this.completedListener = completedListener;
        this.materialKeys=materialKeys;
    }

    @NonNull
    @Override
    public FactoryDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_factory_data, parent, false);
        return new FactoryDataAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final FactoryDataAdapter.ViewHolder holder, final int position) {



        holder.jobNumber.setText("Job Num: "+materialIssue.get(position).getJob_Num());
        holder.partNumber.setText("Part Num: "+materialIssue.get(position).getPart_Num());
        holder.qtyShort.setText("Quantity Short: "+materialIssue.get(position).getQty_shortage());
        holder.signedBy.setText(materialIssue.get(position).getWho());

        if(materialIssue.get(position).getMaterialJobImage()!=null){
            holder.imgProgBar.setVisibility(View.VISIBLE);
            /*Picasso.get().load(materialIssue.get(position).getMaterialJobImage()).placeholder(R.drawable.img_placeholder).into(holder.imgView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    //do smth when picture is loaded successfully
                    holder.imgProgBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception ex) {
                    //do smth when there is picture loading error
                    holder.imgProgBar.setVisibility(View.GONE);
                }
            });*/

            Picasso.get()
                    .load(materialIssue.get(position).getMaterialJobImage())
                    .placeholder(R.drawable.img_placeholder)
                    .networkPolicy(NetworkPolicy.OFFLINE)//user this for offline support
                    .into(holder.imgView, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.imgProgBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get()
                                    .load(materialIssue.get(position).getMaterialJobImage())
                                    .placeholder(R.drawable.img_placeholder)
                                    .error(R.drawable.img_placeholder)//user this for offline support
                                    .into(holder.imgView, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            holder.imgProgBar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            holder.imgProgBar.setVisibility(View.GONE);
                                        }


                                    });
                        }

                    });

        }else{
            holder.imgProgBar.setVisibility(View.GONE);
        }


        holder.imgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(materialIssue.get(position).materialJobImage!=null){
                    Intent i=new Intent(activity, ZoomImageViewActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("product_image",materialIssue.get(position).materialJobImage);
                    activity.startActivity(i);
                }

            }
        });

        holder.btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                completedListener.onCompleted(position,materialIssue.get(position),materialKeys.get(position));

            }
        });



    }

    @Override
    public int getItemCount() {
        return materialIssue.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView jobNumber,partNumber,qtyShort,signedBy;
        ImageView imgView;
        ProgressBar imgProgBar;
        RelativeLayout imgLayout;
        RelativeLayout btnComplete;
        public ViewHolder(View itemView) {
            super(itemView);

            jobNumber=itemView.findViewById(R.id.jobNumber);
            partNumber=itemView.findViewById(R.id.partNumber);
            qtyShort=itemView.findViewById(R.id.qtyShort);
            signedBy=itemView.findViewById(R.id.signedBy);
            imgView=itemView.findViewById(R.id.imgView);
            imgProgBar=itemView.findViewById(R.id.progBar);
            imgLayout=itemView.findViewById(R.id.imgLayout);
            btnComplete=itemView.findViewById(R.id.btnComplete);

        }
    }
}
