package com.codevinci.ceva.pinentry;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PinEntryFragment extends DialogFragment {
    private static final String TAG = "PinEntryFragment";
    private OnResultListener onResultListener;
    //@BindView(R.id.pin_lock_view)
    private PinLockView mPinLockView;
    //@BindView(R.id.indicator_dots)
    private IndicatorDots mIndicatorDots;
    //@BindView(R.id.tvCancel)
    private TextView tvCancel;

    private void onClick(View v) {
        dismissAllowingStateLoss();
    }

    public interface OnResultListener {
        void onResult(String pinCode);
    }

    public PinEntryFragment() {
        // Required empty public constructor
    }

    public static PinEntryFragment newInstance() {
        final PinEntryFragment fragment = new PinEntryFragment();
        //fragment.setOnResultListener(listener);
        return fragment;
    }

    private void setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pin_entry, container, false);
        //ButterKnife.bind(this, view);
        mPinLockView = view.findViewById(R.id.pin_lock_view);
        mIndicatorDots = view.findViewById(R.id.indicator_dots);
        tvCancel = view.findViewById(R.id.tvCancel);

       hideKeyboard(getActivity());

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLength(5);
        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FIXED);
        /*@ColorInt int color = ImageUtils.getColorByThemeAttr(getContext(), R.attr._ubnColorPrimaryDark);
        mPinLockView.setDeleteButtonPressedColor(color);*/
        mPinLockView.setShowDeleteButton(true);
        mPinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                onResultListener.onResult(pin);
                dismissAllowingStateLoss();
            }

            @Override
            public void onEmpty() {
                /*Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.wrong_pin_shake);
                view.findViewById(R.id.indicator_dots).startAnimation(shake);*/
            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {

            }
        });

        tvCancel.setOnClickListener(this::onClick);


        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        try {
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        } catch (Exception e) {
            //Log.Error(e);
        }

        return dialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    private static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static class Builder{
        private OnResultListener listener;
        private AppCompatActivity activity;

        public Builder(AppCompatActivity activity) {
            this.activity = activity;
        }

        public Builder setResultListener(OnResultListener listener){
            this.listener = listener;
            return this;
        }

        public void build(){
            final PinEntryFragment fragment = new PinEntryFragment();
            fragment.setOnResultListener(listener);

            FragmentManager manager = activity.getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(android.R.id.content, fragment)
                    .addToBackStack(null).commitAllowingStateLoss();
            //return fragment;
        }

        /*public void show(){
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(android.R.id.content, fragment)
                    .addToBackStack(null).commitAllowingStateLoss();
        }*/
    }

}