package com.example.sistembillwarnet;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.Arrays;
import java.util.List;

public class PaketActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.packetactivity); // ganti dengan nama layout XML kamu

        viewPager2 = findViewById(R.id.imageSlider);
        tabLayout = findViewById(R.id.tabs_indicator);

        // Daftar gambar untuk slider (ganti dengan drawable yang kamu punya)
        List<Integer> images = Arrays.asList(
                R.drawable.banner1,
                R.drawable.banner1,
                R.drawable.banner1
        );

        ImageSliderAdapter adapter = new ImageSliderAdapter(images);
        viewPager2.setAdapter(adapter);

        // Hubungkan TabLayout dengan ViewPager2 supaya indikator dot muncul
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> {
                    // Kosongkan, karena kita hanya pakai dot indicator tanpa label
                }
        ).attach();
    }
}
