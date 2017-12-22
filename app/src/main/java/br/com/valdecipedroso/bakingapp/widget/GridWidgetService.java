package br.com.valdecipedroso.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import br.com.valdecipedroso.bakingapp.R;
import br.com.valdecipedroso.bakingapp.data.Ingredient;
import br.com.valdecipedroso.bakingapp.data.Recipe;

/**
 * Created by gemeos_valdeci on 22/12/2017.
 */
public class GridWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext(), BakingWidgetProvider.mRecipe );
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    Context mContext;
    Recipe  mRecipe;

    public GridRemoteViewsFactory(Context mContext, Recipe recipe) {
        this.mContext = mContext;
        this.mRecipe = recipe;
    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
     @Override
    public void onDataSetChanged() {
        //changed
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        if (mRecipe == null) return 0;
        return mRecipe.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        List<Ingredient> ingredient = mRecipe.getIngredients();

        String nameIngredient = ingredient.get(position).getIngredient();
        String measure = String.format(mContext.getString(R.string.quantity_measure), ingredient.get(position).getQuantity(), ingredient.get(position).getMeasure());

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_widget);

        views.setTextViewText(R.id.widget_ingredient_name, nameIngredient);
        views.setTextViewText(R.id.widget_measure, measure);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
