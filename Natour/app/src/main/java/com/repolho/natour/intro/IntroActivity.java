package com.repolho.natour.intro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.repolho.natour.R;
import com.repolho.natour.home.HomeActivity;


/**
 * Created by andrew on 11/17/16.
 */

public class IntroActivity extends AppIntro{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SliderPage sliderPage1 = new SliderPage();
        sliderPage1.setTitle(getString(R.string.intro_1_title));
        sliderPage1.setDescription(getString(R.string.intro_1_text));
        sliderPage1.setImageDrawable(R.mipmap.ic_waterfall_foreground_custom);
        sliderPage1.setBgColor(getColor(R.color.bg_intro_1));
        addSlide(AppIntroFragment.newInstance(sliderPage1));

        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle(getString(R.string.intro_2_title));
        sliderPage2.setDescription(getString(R.string.intro_2_text));
        sliderPage2.setImageDrawable(R.drawable.ic_accessibility_black_custom);
        sliderPage2.setBgColor(getColor(R.color.bg_intro_2));
        addSlide(AppIntroFragment.newInstance(sliderPage2));

        SliderPage sliderPage3 = new SliderPage();
        sliderPage3.setTitle(getString(R.string.intro_3_title));
        sliderPage3.setDescription(getString(R.string.intro_3_text));
        sliderPage3.setImageDrawable(R.drawable.ic_accessible_black_custom);
        sliderPage3.setBgColor(getColor(R.color.bg_intro_3));
        addSlide(AppIntroFragment.newInstance(sliderPage3));

        SliderPage sliderPage4 = new SliderPage();
        sliderPage4.setTitle(getString(R.string.intro_4_title));
        sliderPage4.setDescription(getString(R.string.intro_4_text));
        sliderPage4.setImageDrawable(R.drawable.ic_new_black_custom);
        sliderPage4.setBgColor(getColor(R.color.bg_intro_4));
        addSlide(AppIntroFragment.newInstance(sliderPage4));

        setSkipText(getString(R.string.intro_skip_text));
        setDoneText(getString(R.string.intro_done_text));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(IntroActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(IntroActivity.this, HomeActivity.class));
        finish();
    }
}
