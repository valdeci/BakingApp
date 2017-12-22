package br.com.valdecipedroso.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;

import br.com.valdecipedroso.bakingapp.R;
import br.com.valdecipedroso.bakingapp.data.Recipe;

/**
 * Created by gemeos_valdeci on 22/12/2017.
 */

public class BakingWidgetService extends IntentService {

    private static final String EXTRA_RECIPE_ID = "widget_recipe";
    private static final String ACTION_WIDGET = "br.com.valdecipedroso.bakingapp.action.widget_recipe";
    private Recipe mRecipe;

    public BakingWidgetService() {
        super("BakingWidgetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_WIDGET.equals(action)) {
                mRecipe = intent.getParcelableExtra(EXTRA_RECIPE_ID);
                handleAction(mRecipe);
            }
        }
    }

    private void handleAction(Recipe recipe) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        BakingWidgetProvider.updatePlantWidgets(this, appWidgetManager, recipe, appWidgetIds);
    }
}
