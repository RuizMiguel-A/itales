package pe.miguelruiz.itana.ui.places;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import pe.miguelruiz.itana.R;
import pe.miguelruiz.itana.data.local.entity.PlaceEntity;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    public interface OnPlaceClickListener {
        void onPlaceClick(PlaceEntity place);
    }

    private List<PlaceEntity> places = new ArrayList<>();
    private OnPlaceClickListener listener;

    public void setPlaces(List<PlaceEntity> places) {
        this.places = places;
        notifyDataSetChanged();
    }

    public void setOnPlaceClickListener(OnPlaceClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        PlaceEntity place = places.get(position);
        holder.bind(place, listener);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    static class PlaceViewHolder extends RecyclerView.ViewHolder {
        private final TextView textName;
        private final TextView textDescription;
        private final ImageView imagePlace;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textPlaceName);
            textDescription = itemView.findViewById(R.id.textPlaceDescription);
            imagePlace = itemView.findViewById(R.id.imagePlace);
        }

        public void bind(PlaceEntity place, OnPlaceClickListener listener) {
            textName.setText(place.getName());
            textDescription.setText(place.getShortDescription());

            Glide.with(itemView.getContext())
                    .load(place.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_error)
                    .centerCrop()
                    .into(imagePlace);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPlaceClick(place);
                }
            });
        }
    }
}
