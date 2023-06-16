package com.kingmarco.myclientmodel.Auxiliary.Classes.Static;

import androidx.navigation.NavOptions;

import com.kingmarco.myclientmodel.R;

public class FragmentAnimation {
    public static NavOptions navigateBehavior(){
        return new NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_right)
                .setExitAnim(R.anim.slide_out_left)
                .setPopEnterAnim(R.anim.slide_in_left)
                .setPopExitAnim(R.anim.slide_out_right)
                .build();
    }
}
