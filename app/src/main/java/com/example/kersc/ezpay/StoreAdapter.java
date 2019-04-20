package com.example.kersc.ezpay;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kersc.ezpay.Classes.item;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder>{
    private Context mContext;
    private List<item> barcodes;

    private RecyclerViewAdapter.OnItemClickListener mListerner;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onItemClickRemove(int position);
    }

    public void setOnItemClickListener (RecyclerViewAdapter.OnItemClickListener listener){
        mListerner =listener;
    }

    public StoreAdapter(Context mContext, List<item> barcodes) {
        this.mContext = mContext;
        this.barcodes = barcodes;
    }

    @NonNull
    @Override
    public StoreAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view  = inflater.inflate(R.layout.storebuying_card,viewGroup,false);
        return new StoreAdapter.MyViewHolder(view,mListerner);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreAdapter.MyViewHolder holder, int i) {
        holder.priceValue.setText("$ " + barcodes.get(i).getPrice());
        holder.title.setText(barcodes.get(i).getTitle());
        holder.description.setText(barcodes.get(i).getDescription());
//        holder.icon.setImageResource(getResource(barcodes.get(i).getCategory().toLowerCase()));
//        holder.location.setText(barcodes.get(i).getLocation ());
//        holder.priceValue.setText(barcodes.get(i).getBarcode());
        holder.icon.setImageDrawable(barcodes.get(i).getImage());

    }

    @Override
    public int getItemCount() {
        return barcodes.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        EditText priceValue;
        ImageView icon;
        Button buy;
        TextView title,description;
        public MyViewHolder(View itemView, final RecyclerViewAdapter.OnItemClickListener listener){
            super(itemView);
            title = itemView.findViewById(R.id.title);
            priceValue = itemView.findViewById(R.id.price);
            buy =  itemView.findViewById(R.id.btnbuy);
            icon = itemView.findViewById(R.id.product_image);
            description = itemView.findViewById(R.id.description);

            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int pos = getAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION){
                            listener.onItemClick(pos);
                        }
                    }
                }
            });

        }
    }


}
