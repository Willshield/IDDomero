package hu.bme.aut.iDDomero.userInterface;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.iDDomero.R;
import hu.bme.aut.iDDomero.model.ProfileData;
import hu.bme.aut.iDDomero.model.SettingsData;

public class ProfileViewAdapter extends RecyclerView.Adapter<ProfileViewAdapter.ProfileViewHolder>  {
    private List<ProfileData> items;
    private Context context;

    public ProfileViewAdapter(Context context) {
        this.context = context;
        items = new ArrayList<>();
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, parent, false);
        return new ProfileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProfileViewHolder holder, int position) {
        ProfileData item = items.get(position);
        holder.nameTextView.setText(item.name);

        if(item.name.equals(SettingsData.getInstance(context).getActivePlayer())){
            holder.selectButton.setText(context.getResources().getText(R.string.selected));
        } else {
            holder.selectButton.setText(context.getResources().getText(R.string.select));
        }
        holder.selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.selectButton.getText().equals(context.getResources().getText(R.string.select))){
                    SettingsData.getInstance(context).setActivePlayer("" + holder.nameTextView.getText());
                    holder.selectButton.setText((context.getResources().getText(R.string.selected)));
                    notifyDataSetChanged();
                }
            }
        });

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(holder.getAdapterPosition());
            }
        });
    }

    public boolean contains(ProfileData item){
        boolean contains = false;
        for (int i = 0; i < items.size(); i++){
            if(items.get(i).name.equals(item.name)){
                contains = true;
            }
        }
        return contains;
    }

    public void addItem(ProfileData item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void removeItem(int position) {
        ProfileData removed = items.remove(position);
        removed.delete();
        notifyItemRemoved(position);
        if (position < items.size()) {
            notifyItemRangeChanged(position, items.size() - position);
        }
    }

    public void update(List<ProfileData> profileDatas) {
        items.clear();
        items.addAll(profileDatas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        Button selectButton;
        ImageButton removeButton;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.profile_name);
            selectButton = (Button) itemView.findViewById(R.id.select_button);
            removeButton = (ImageButton) itemView.findViewById(R.id.remove_button);
        }
    }

}
