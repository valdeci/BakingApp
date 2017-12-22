package br.com.valdecipedroso.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import br.com.valdecipedroso.bakingapp.data.Recipe;

public class StepDetailActivity extends AppCompatActivity {

    private static final String ARG_PARAM_STEP_POSITION = "STEP_POSITION";
    private static final String ARG_PARAM_STEPS = "STEPS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        boolean twoPane = findViewById(R.id.divisor_tablet) != null;
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Intent it = getIntent();
            if (it.resolveActivity(getPackageManager()) != null) {
                if (it.hasExtra(ARG_PARAM_STEPS)) {
                    Recipe recipe = it.getParcelableExtra(ARG_PARAM_STEPS);
                    if (recipe != null) {
                        android.support.v4.app.FragmentManager fragManager = getSupportFragmentManager();
                        int position = it.getIntExtra(ARG_PARAM_STEP_POSITION,0);
                        setTitle(recipe.getSteps().get(position).getShortDescription());
                        StepDetailFragment detail = StepDetailFragment.newInstance(recipe.getSteps(), position, twoPane);
                        fragManager.beginTransaction().add(R.id.fragment_detail_activity, detail).commit();
                    }
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
