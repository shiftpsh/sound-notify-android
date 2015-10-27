package net.shiftstudios.soundnotify.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import net.shiftstudios.soundnotify.R;
import net.shiftstudios.soundnotify.helper.PreferencesHelper;

public class TtsSettingsFragment extends Fragment {

    public PreferencesHelper preferencesHelper;

    public Switch headsetSwitch;
    public TextView pitchText, rateText;
    public SeekBar pitchSeek, rateSeek;
    public Button testButton, notiButton;

    public TextToSpeech tts;

    public TtsSettingsFragment() {
    }

    public static Fragment newInstance() {
        TtsSettingsFragment mFrgment = new TtsSettingsFragment();
        return mFrgment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tts_settings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View parent = getView();

        tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

            }
        });

        preferencesHelper = new PreferencesHelper(getActivity());

        headsetSwitch = (Switch) parent.findViewById(R.id.headset_switch);
        pitchText = (TextView) parent.findViewById(R.id.pitch_text);
        pitchSeek = (SeekBar) parent.findViewById(R.id.pitch_seekbar);
        rateText = (TextView) parent.findViewById(R.id.rate_text);
        rateSeek = (SeekBar) parent.findViewById(R.id.rate_seekbar);
        testButton = (Button) parent.findViewById(R.id.testButton);
        notiButton = (Button) parent.findViewById(R.id.notiButton);

        headsetSwitch.setChecked(preferencesHelper.isHeadsetNeeded());

        pitchSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                pitchText.setText(i <= 12 ? String.valueOf(i - 12) : "+" + (i - 12));
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int i = seekBar.getProgress();
                float pitch = 1f + (float) (i - 12) / 12f;
                preferencesHelper.setPitch(pitch);
            }
        });

        rateSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b){
                float rate = 1f + (float) (i - 10) / 20f;
                rateText.setText(rate + "x");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar){}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar){
                int i = seekBar.getProgress();
                float rate = 1f + (float) (i - 10) / 20f;
                rateText.setText(rate + "x");
                preferencesHelper.setRate(rate);
            }
        });

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.setPitch(preferencesHelper.getPitch());
                tts.setSpeechRate(preferencesHelper.getRate());
                tts.speak("테스트 메시지입니다.", TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        notiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
            }
        });

        headsetSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferencesHelper.setHeadsetNeeded(b);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
