package br.com.valdecipedroso.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.valdecipedroso.bakingapp.adapter.RecipeAdapter;
import br.com.valdecipedroso.bakingapp.connection.Connection;
import br.com.valdecipedroso.bakingapp.data.Recipe;
import br.com.valdecipedroso.bakingapp.widget.BakingWidgetService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<List<Recipe>>, RecipeAdapter.RecipeAdapterOnClickHandler {

    private static final String LOG = MainActivity.class.getSimpleName();

    private static final String ARG_PARAM_STEPS = "STEPS";
    private static final String ARG_PARAM_LIST_RECIPES = "LIST_RECIPES";
    private static final String EXTRA_RECIPE = "widget_recipe";
    private static final String ACTION_WIDGET = "br.com.valdecipedroso.bakingapp.action.widget_recipe";
    private List<Recipe> mListRecipe;
    private TextView mTextFailedView;
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private RecipeAdapter mRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mTextFailedView =  findViewById(R.id.tv_load_failed);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecipeAdapter = new RecipeAdapter(this, MainActivity.this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns(), GridLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRecipeAdapter);

        if (savedInstanceState == null){
            loadDataFromConnection();
        }else{
            mListRecipe = savedInstanceState.getParcelableArrayList(ARG_PARAM_LIST_RECIPES);
            mRecipeAdapter.setRecipeData(mListRecipe);
        }

    }

    private void loadDataFromConnection()  {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        Connection.build(getApplication()).getRecipes().enqueue(this);
    }


    @Override
    public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
        Log.i(LOG, getString(R.string.connection_successful));
        mRecipeAdapter.setRecipeData(response.body());
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if(response.body()!= null){
            mListRecipe = response.body();
            mRecipeAdapter.setRecipeData(response.body());
        }else{
            mListRecipe.clear();
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mTextFailedView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailure(Call<List<Recipe>> call, Throwable t) {
        Log.i(LOG, getString(R.string.connection_failure));
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 300;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        return nColumns;
    }

    @Override
    public void onClick(Recipe recipeSelected) {
        Intent it = new Intent(getBaseContext(), StepsRecipeActivity.class);
        it.putExtra(ARG_PARAM_STEPS, recipeSelected);
        startActivity(it);
        handleUpdateWidget(recipeSelected);
    }

    private void handleUpdateWidget(Recipe recipe) {
        Intent widgetIntent = new Intent(this, BakingWidgetService.class);
        widgetIntent.setAction(ACTION_WIDGET);
        widgetIntent.putExtra(EXTRA_RECIPE, recipe);
        this.startService(widgetIntent);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(ARG_PARAM_LIST_RECIPES, new ArrayList<Parcelable>(mListRecipe));
        super.onSaveInstanceState(outState);
    }
}
