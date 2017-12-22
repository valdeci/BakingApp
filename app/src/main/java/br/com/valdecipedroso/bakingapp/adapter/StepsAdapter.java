package br.com.valdecipedroso.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.valdecipedroso.bakingapp.R;
import br.com.valdecipedroso.bakingapp.StepsRecipeFragment;
import br.com.valdecipedroso.bakingapp.data.Step;

/**
 * Created by gemeos_valdeci on 19/12/2017.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsAdapterViewHolder> {

    private static String TAG = StepsAdapter.class.getSimpleName();
    private static List<Step> mListStepData;
    private static Context mContext;
    private boolean mTwoPane;
    private int mPositionSelected;
    private StepsRecipeFragment.OnFragmentInteractionListener mCallback;

    public StepsAdapter(Context context,StepsRecipeFragment.OnFragmentInteractionListener callback, boolean twoPane, int position) {
        this.mContext = context;
        this.mListStepData = new ArrayList<>();
        this.mCallback = callback;
        this.mTwoPane = twoPane;

        if(!twoPane && position == 0){
            mPositionSelected = -1;
        }else{
            mPositionSelected = position;
        }
    }

    @Override
    public StepsAdapter.StepsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.carview_step_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new StepsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsAdapter.StepsAdapterViewHolder convertView, int position) {
        String text;
        Step step = mListStepData.get(position);

        TextView idStep = convertView.mIdStep;

        if(step.getId() == 0) {
            text = String.format(step.getShortDescription(), mContext.getString(R.string.step_introduction));
        }else{
            text = String.format(mContext.getString(R.string.step_others), String.valueOf(step.getId()), step.getShortDescription());
        }

        if(mPositionSelected >= 0){
            convertView.mRelativeLayout.setSelected(mPositionSelected == position);
        }

        idStep.setText(text);
    }

    @Override
    public int getItemCount() {
        if (null == mListStepData) return 0;
        return mListStepData.size();
    }

    public class StepsAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdStep;
        public final RelativeLayout mRelativeLayout;
        public StepsAdapterViewHolder(View itemView) {
            super(itemView);
            mIdStep = itemView.findViewById(R.id.tv_title_id_step);
            mRelativeLayout = itemView.findViewById(R.id.relative_layout_step_item);
            CardView mCardView = itemView.findViewById(R.id.card_view_item_step);
            mCardView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            notifyItemChanged(mPositionSelected);
                            notifyItemChanged(getAdapterPosition());
                            mPositionSelected = getAdapterPosition();

                            mCallback.itemSelected(getAdapterPosition());
                        }
                    });
        }
    }

    public void setStepsData(List<Step> stepData) {
        if (stepData != null) {
            int position = stepData.size();
            mListStepData.addAll(stepData);
            notifyItemRangeInserted(position, stepData.size());
        }
    }
}
