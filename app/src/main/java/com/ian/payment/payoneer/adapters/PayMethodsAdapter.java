package com.ian.payment.payoneer.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.ian.payment.payoneer.databinding.ListItemBinding;
import com.ian.payment.payoneer.model.Applicable;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class PayMethodsAdapter extends RecyclerView.Adapter<PayMethodsAdapter.PayMethodViewHolder> {
    private Context mContext;
    private List<Applicable> mList;
    private ListItemBinding binding;

    public PayMethodsAdapter(Context mContext, List<Applicable> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public PayMethodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        binding = ListItemBinding.inflate(inflater,parent,false);
        return new PayMethodViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PayMethodViewHolder holder, int position) {
        holder.itemBinding.label.setText(mList.get(position).getLabel());
        Glide.with(mContext).load(mList.get(position).getLinks().getLogo())
                .into(holder.itemBinding.imgholder);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    static class PayMethodViewHolder extends RecyclerView.ViewHolder{
        private ListItemBinding itemBinding;

        public PayMethodViewHolder(ListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }

    public  void updateList(List<Applicable> updatedList){
        mList = updatedList;
        notifyDataSetChanged();
    }

    public  Applicable getPayMethodAt(int position){
        return mList.get(position);
    }
}