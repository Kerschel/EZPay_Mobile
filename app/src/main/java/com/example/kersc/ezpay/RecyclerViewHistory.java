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

public class RecyclerViewHistory extends RecyclerView.Adapter<RecyclerViewHistory.MyViewHolder>{
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

    public RecyclerViewHistory(Context mContext, List<item> barcodes) {
        this.mContext = mContext;
        this.barcodes = barcodes;
    }

    @NonNull
    @Override
    public RecyclerViewHistory.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view  = inflater.inflate(R.layout.card_history,viewGroup,false);
        return new RecyclerViewHistory.MyViewHolder(view,mListerner);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHistory.MyViewHolder holder, int i) {
        holder.priceValue.setText(barcodes.get(i).getPrice());
        holder.description.setText(barcodes.get(i).getDescription());
//        holder.icon.setImageResource(getResource(barcodes.get(i).getCategory().toLowerCase()));
        holder.location.setText(barcodes.get(i).getLocation ());
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
    Button buy,remove;
    TextView description;
    TextView location;
    public MyViewHolder(View itemView, final RecyclerViewAdapter.OnItemClickListener listener){
        super(itemView);
        description = itemView.findViewById(R.id.description);
        priceValue = itemView.findViewById(R.id.price);
        buy = (Button) itemView.findViewById(R.id.btnbuy);
        icon = itemView.findViewById(R.id.product_image);
        location = itemView.findViewById(R.id.location);
//        remove = itemView.findViewById(R.id.btnremove);

//        buy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(listener != null){
//                    int pos = getAdapterPosition();
//                    if(pos!=RecyclerView.NO_POSITION){
//                        listener.onItemClick(pos);
//                    }
//                }
//            }
//        });
//
//
//        remove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(listener != null){
//                    int pos = getAdapterPosition();
//                    if(pos!=RecyclerView.NO_POSITION){
//                        listener.onItemClickRemove(pos);
//                    }
//                }
//            }
//        });

    }
}

    public int getResource(String category){
        if(category.equals("food"))
            return R.drawable.food;
        else
        if(category.equals("icecream"))
            return R.drawable.ice;
        return 0;
    }
}
