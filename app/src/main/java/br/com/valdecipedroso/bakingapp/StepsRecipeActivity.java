package br.com.valdecipedroso.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import br.com.valdecipedroso.bakingapp.data.Recipe;

public class StepsRecipeActivity extends AppCompatActivity implements StepsRecipeFragment.OnFragmentInteractionListener {
    private static final String ARG_PARAM_STEP_POSITION = "STEP_POSITION";
    private static final String ARG_PARAM_STEPS = "STEPS";

    private FragmentManager mFragManager;
    private Recipe mRecipe;
    private boolean mTwoPane;
    private long mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        FrameLayout fragment = findViewById(R.id.fragment_step);
        mFragManager = getSupportFragmentManager();
        StepsRecipeFragment steps;

        Intent it = getIntent();

        if (it.resolveActivity(getPackageManager()) !=  null){
            if(it.hasExtra(ARG_PARAM_STEPS)){
                mRecipe = it.getParcelableExtra(ARG_PARAM_STEPS);

                if(savedInstanceState != null) {
                    mPosition = savedInstanceState.getInt(ARG_PARAM_STEP_POSITION, 0);
                }

                if (mRecipe != null){
                    setTitle(mRecipe.getName());
                    mTwoPane = getResources().getBoolean(R.bool.isTablet);

                    if(mTwoPane){
                        StepDetailFragment FindDetail = (StepDetailFragment) mFragManager.findFragmentById(R.id.fragment_detail);
                        if(FindDetail == null){
                            StepDetailFragment detail = StepDetailFragment.newInstance(mRecipe.getSteps(), 0 , mTwoPane);
                            mFragManager.beginTransaction().add(R.id.fragment_detail, detail).commit();
                        }else{
                            StepDetailFragment detail = StepDetailFragment.newInstance(mRecipe.getSteps(),(int) mPosition , mTwoPane);
                            mFragManager.beginTransaction().replace(R.id.fragment_detail, detail).commit();
                        }
                    }
                    StepsRecipeFragment FindRecipe = (StepsRecipeFragment) mFragManager.findFragmentById(fragment.getId());
                    steps = StepsRecipeFragment.newInstance(mRecipe.getIngredients(), mRecipe.getSteps(), mTwoPane, (int) mPosition);
                    if(FindRecipe == null) {
                        mFragManager.beginTransaction().add(fragment.getId(), steps).commit();
                    }else{
                        mFragManager.beginTransaction().replace(fragment.getId(), steps).commit();
                    }
                }
            }
        }
    }


    @Override
    public void itemSelected(long position) {
        mPosition = position;
        if (!mTwoPane){
            Bundle b = new Bundle();
            b.putInt(ARG_PARAM_STEP_POSITION, (int) mPosition);
            b.putParcelable(ARG_PARAM_STEPS, mRecipe);
            Intent it = new Intent(this, StepDetailActivity.class);
            it.putExtras(b);
            startActivity(it);
        }else{
            StepDetailFragment detail = StepDetailFragment.newInstance(mRecipe.getSteps(),  (int) mPosition , mTwoPane);
            mFragManager.beginTransaction().replace(R.id.fragment_detail, detail).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARG_PARAM_STEP_POSITION, (int) mPosition);
        super.onSaveInstanceState(outState);
    }
}

