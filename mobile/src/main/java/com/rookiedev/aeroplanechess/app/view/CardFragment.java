package com.rookiedev.aeroplanechess.app.view;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.rookiedev.aeroplanechess.app.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardFragment extends Fragment {
    public static final String CARD_TYPE = "CARD_TYPE";
    public static final int NEW_GAME=0, RESUME_GAME=1, TWO_PLAYERS=2, FOUR_PLAYERS=3, RED_VS_BLUE=4, YELLOW_VS_GREEN=5;

    private View viewRoot;
    private Activity activity;
    private int type;
    private CardView cardView;
    private ImageView imageView;
    private TextView textView;

    public CardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param cardType card type.
     * @return A new instance of fragment CardFragment.
     */
    public static CardFragment newInstance(int cardType) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        args.putInt(CARD_TYPE,cardType);
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnCardClickListener {
        public void onCardClicked(int cardType);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type=getArguments().getInt(CARD_TYPE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot=inflater.inflate(R.layout.fragment_card, container, false);

        cardView=viewRoot.findViewById(R.id.cardView_menu);
        imageView=viewRoot.findViewById(R.id.imageView_menu);
        textView=viewRoot.findViewById(R.id.textView_menu);

        switch (type){
            case NEW_GAME:
                imageView.setImageResource(R.drawable.ic_play);
                imageView.setBackgroundColor(getResources().getColor(R.color.green));
                cardView.setForeground(getResources().getDrawable(R.drawable.ripple_green));
                textView.setText(R.string.new_game);
                break;
            case RESUME_GAME:
                imageView.setImageResource(R.drawable.ic_resume);
                imageView.setBackgroundColor(getResources().getColor(R.color.amber));
                cardView.setForeground(getResources().getDrawable(R.drawable.ripple_amber));
                textView.setText(R.string.resume_game);
                break;
            case TWO_PLAYERS:
                imageView.setImageResource(R.drawable.ic_two);
                imageView.setBackground(getResources().getDrawable(R.drawable.background_two));
                cardView.setForeground(getResources().getDrawable(R.drawable.ripple_red));
                textView.setText(R.string.two_players);
                break;
            case FOUR_PLAYERS:
                imageView.setImageResource(R.drawable.ic_four);
                imageView.setBackground(getResources().getDrawable(R.drawable.background_four));
                cardView.setForeground(getResources().getDrawable(R.drawable.ripple_blue));
                textView.setText(R.string.four_players);
                break;
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    ((OnCardClickListener) activity).onCardClicked(type);
                }catch (ClassCastException cce){
                    Crashlytics.log(Log.ERROR, "CardFragment", String.valueOf(cce));
                }
            }

        });

        return viewRoot;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            activity=(Activity) context;
        }
    }
}
