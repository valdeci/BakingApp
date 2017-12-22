package br.com.valdecipedroso.bakingapp.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import br.com.valdecipedroso.bakingapp.R;
import br.com.valdecipedroso.bakingapp.connection.Connection;
import br.com.valdecipedroso.bakingapp.data.Recipe;

/**
 * Created by gemeos_valdeci on 18/12/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {
    private final static String TAG = RecipeAdapter.class.getName();
    private final List<Recipe> mRecipeData;
    private RecipeAdapterOnClickHandler mClickHandler;
    private Context mContext;

    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipeSelected);
    }

    public RecipeAdapter(Context mContext, RecipeAdapterOnClickHandler onclick) {
        this.mContext = mContext;
        this.mRecipeData = new ArrayList<>();
        this.mClickHandler = onclick;
    }

    @Override
    public RecipeAdapter.RecipeAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.cardview_recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.RecipeAdapterViewHolder convertView, int position) {
        Recipe recipe = mRecipeData.get(position);

        ImageView image = convertView.mRecipeImageView;
        TextView title = convertView.mRecipeTitleView;
        TextView serve = convertView.mRecipeServeView;

        image.setAdjustViewBounds(true);
        String imageURL;

        if(!recipe.getImage().isEmpty()){
            imageURL = recipe.getImage();
        }else{
            switch (recipe.getId()){
            case 1:
                imageURL = Connection.NUTELLA_PIE;
                break;
            case 2:
                imageURL = Connection.BROWNIES;
                break;
            case 3:
                imageURL = Connection.YELLOW_CAKE;
                break;
            case 4:
                imageURL = Connection.CHEESECAKE;
                break;
            default: imageURL = Connection.DEFAULT_IMAGE;
                break;
            }
        }

        if(TextUtils.isEmpty(recipe.getImage())) {
            recipe.setImage(mContext.getResources().getString(R.string.image_url) + imageURL);
        }

        title.setText(recipe.getName());
        serve.setText(String.valueOf(recipe.getServings()));

        Glide.with(mContext)
                .load(recipe.getImage())
                .into(image);
     }

    @Override
    public int getItemCount() {
        if (null == mRecipeData) return 0;
        return mRecipeData.size();
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        public final ImageView mRecipeImageView;
        public final TextView mRecipeTitleView;
        public final TextView mRecipeServeView;

        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);
            mRecipeImageView = itemView.findViewById(R.id.list_item_recipe);
            mRecipeTitleView = itemView.findViewById(R.id.tv_title_recipe);
            mRecipeServeView = itemView.findViewById(R.id.tv_count_serve);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(mRecipeData.get(adapterPosition));
            Log.d(TAG, "-> View.OnClickListener");
        }
    }

    public void setRecipeData(List<Recipe> recipeData) {
        mRecipeData.clear();
        if (recipeData != null) {
           mRecipeData.addAll(recipeData);
           notifyDataSetChanged();
        }
    }
}
