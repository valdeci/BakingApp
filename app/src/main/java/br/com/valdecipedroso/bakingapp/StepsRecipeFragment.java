package br.com.valdecipedroso.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.valdecipedroso.bakingapp.adapter.IngredientsAdapter;
import br.com.valdecipedroso.bakingapp.adapter.StepsAdapter;
import br.com.valdecipedroso.bakingapp.data.Ingredient;
import br.com.valdecipedroso.bakingapp.data.Step;

public class StepsRecipeFragment extends Fragment {
    private static final String TAG = StepsRecipeFragment.class.getSimpleName();

    private static final String ARG_INGREDIENTS = "ingredients";
    private static final String ARG_STEPS = "steps";

    private List<Ingredient> mIngredients;
    private List<Step> mSteps;
    private static boolean mTwoPane;

    private static Integer mPosition;

    private OnFragmentInteractionListener mCallback;

    public interface OnFragmentInteractionListener {
        void itemSelected(long position);
    }

    public StepsRecipeFragment() {
        // Required empty public constructor
    }

    public static StepsRecipeFragment newInstance(List<Ingredient> paramIngredients, List<Step> paramSteps, boolean twoPane, int position ) {
        StepsRecipeFragment fragment = new StepsRecipeFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_INGREDIENTS, new ArrayList<Parcelable>(paramIngredients));
        args.putParcelableArrayList(ARG_STEPS, new ArrayList<Parcelable>(paramSteps));
        fragment.setArguments(args);
        mTwoPane = twoPane;
        mPosition = position;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIngredients = new ArrayList<>();
        mSteps = new ArrayList<>();
        if (getArguments() != null) {
            List<Ingredient> tempIngredients = getArguments().getParcelableArrayList(
                    ARG_INGREDIENTS);
            List<Step> tempSteps = getArguments().getParcelableArrayList(ARG_STEPS);
            if (tempIngredients != null) {
                mIngredients.addAll(tempIngredients);
            }
            if (tempSteps != null) {
                mSteps.addAll(tempSteps);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(ARG_INGREDIENTS, new ArrayList<>(mIngredients));
        outState.putParcelableArrayList(ARG_STEPS , new ArrayList<>(mSteps));
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step,container, false);

        if (savedInstanceState != null) {
            mIngredients = savedInstanceState.getParcelableArrayList(ARG_INGREDIENTS);
            mSteps = savedInstanceState.getParcelableArrayList(ARG_STEPS);
        }

        RecyclerView mRecyclerIngredient = rootView.findViewById(R.id.recycler_view_ingredient);
        mRecyclerIngredient.setLayoutManager(new GridLayoutManager(getContext(), 1
                , GridLayoutManager.VERTICAL, false));
        mRecyclerIngredient.setHasFixedSize(true);
        IngredientsAdapter mIngredientsAdapter = new IngredientsAdapter(getContext());
        mRecyclerIngredient.setAdapter(mIngredientsAdapter);
        mIngredientsAdapter.setDataIngredients(mIngredients);

        RecyclerView mRecyclerStep = rootView.findViewById(R.id.recycler_view_steps);
        mRecyclerStep.setLayoutManager(new GridLayoutManager(getContext(), 1
                , GridLayoutManager.VERTICAL, false));
        mRecyclerStep.setHasFixedSize(true);
        StepsAdapter mStepAdapter = new StepsAdapter(getContext(), mCallback, mTwoPane, mPosition);
        mRecyclerStep.setAdapter(mStepAdapter);
        mStepAdapter.setStepsData(mSteps);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnFragmentInteractionListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement OnFragmentInteractionListener");
        }
    }
}
