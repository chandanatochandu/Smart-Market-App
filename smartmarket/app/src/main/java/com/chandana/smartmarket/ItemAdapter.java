package com.chandana.smartmarket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private ArrayList<Item> mList;


    private OnItemClickListener mListener;

    public interface OnItemClickListener {
       // void onItemClick(int position);
        void onDeleteClick(int position);
        void onChangeAmountInc(int am);
        void onChangeAmountDec(int am);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener=listener;

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mName;
        public TextView mBarcode;
        public TextView mCost;
        public TextView mQuantity;
        public ImageView mdeleteImage;
        public Button mInc,mDec;


        public ViewHolder(View itemView, final OnItemClickListener listener)
        {
            super(itemView);
            mName=itemView.findViewById(R.id.tvname);
            mBarcode=itemView.findViewById(R.id.tvbarcode);
            mCost=itemView.findViewById(R.id.tvcost);
            mQuantity=itemView.findViewById(R.id.tvquantity);
            mdeleteImage=itemView.findViewById(R.id.deleteimg);
            mInc=itemView.findViewById(R.id.inc);
            mDec=itemView.findViewById(R.id.dec);

            int quan;

        /*    itemView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });*/
            mdeleteImage.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                    int quan = Integer.parseInt(mQuantity.getText().toString());
                    int cost = Integer.parseInt(mCost.getText().toString());
                    int a = quan*cost;
                    listener.onChangeAmountDec(a);
                }
            });
            mInc.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int quan = Integer.parseInt(mQuantity.getText().toString());
                    int oq=quan;
                    quan=quan+1;
                    mQuantity.setText(quan+"");
                    int cost=Integer.parseInt(mCost.getText().toString());
                    int q=Integer.parseInt(mQuantity.getText().toString());
                    int a = cost*(abs(oq-q));
                    listener.onChangeAmountInc(a);
                }
            });
            mDec.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int quan = Integer.parseInt(mQuantity.getText().toString());
                    int oq=quan;
                    quan=quan-1;
                    mQuantity.setText(quan+"");
                    int cost=Integer.parseInt(mCost.getText().toString());
                    int q=Integer.parseInt(mQuantity.getText().toString());
                    int a = cost*(abs(oq-q));
                    listener.onChangeAmountDec(a);

                }
            });
        }
    }

    public ItemAdapter(ArrayList<Item> itemList)
    {
        mList=itemList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(v,mListener);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item=mList.get(position);
        holder.mName.setText(item.getName());
        holder.mBarcode.setText(item.getBarcode());
        holder.mCost.setText(item.getCost()+"");
        holder.mQuantity.setText(item.getQuantity()+"");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
