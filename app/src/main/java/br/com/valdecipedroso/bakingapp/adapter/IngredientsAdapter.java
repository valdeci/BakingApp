package br.com.valdecipedroso.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.valdecipedroso.bakingapp.R;
import br.com.valdecipedroso.bakingapp.data.Ingredient;

/**
 * Created by gemeos_valdeci on 19/12/2017.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsAdapterViewHolder> {
    private final static String TAG = IngredientsAdapter.class.getName();
    private final List<Ingredient> mIngredientData;
    private Context mContext;

    public IngredientsAdapter(Context context ) {
        this.mContext = context;
        this.mIngredientData = new ArrayList<>();
    }

    @Override
    public IngredientsAdapter.IngredientsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.cardview_ingredients_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new IngredientsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsAdapter.IngredientsAdapterViewHolder convertView, int position) {
        Ingredient ingredient = mIngredientData.get(position);

        TextView description  = convertView.mIngredientTextView;
        TextView quantityMeasure  = convertView.mMeasureQuantityTextView;
        description.setText(ingredient.getIngredient());
        quantityMeasure.setText(String.format(mContext.getString(R.string.quantity_measure), ingredient.getQuantity(), ingredient.getMeasure()));
    }

    @Override
    public int getItemCount() {
        if (null == mIngredientData) return 0;
        return mIngredientData.size();
    }

    public class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIngredientTextView;
        public final TextView mMeasureQuantityTextView;

        public IngredientsAdapterViewHolder(View itemView) {
            super(itemView);
            mIngredientTextView = itemView.findViewById(R.id.tv_ingredient);
            mMeasureQuantityTextView = itemView.findViewById(R.id.tv_quantity_measure);
        }
    }

    public void setDataIngredients(List<Ingredient> ingredientsData){
        if (ingredientsData != null) {
            int position = ingredientsData.size();
            mIngredientData.addAll(ingredientsData);
            notifyItemRangeInserted(position, ingredientsData.size());
        }
    }
}
