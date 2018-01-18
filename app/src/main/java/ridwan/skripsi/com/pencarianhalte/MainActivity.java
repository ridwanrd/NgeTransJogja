package ridwan.skripsi.com.pencarianhalte;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.lang.reflect.Field;

import butterknife.Bind;
import butterknife.ButterKnife;
import ridwan.skripsi.com.pencarianhalte.Pages.DaftarHaltePage;
import ridwan.skripsi.com.pencarianhalte.Pages.MorePage;
import ridwan.skripsi.com.pencarianhalte.Pages.PencarianPage;
import ridwan.skripsi.com.pencarianhalte.Pages.PencarianRuteHaltePage;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.main_bottom_nav) protected BottomNavigationView bottom_nav;
    @Bind(R.id.main_container) protected FrameLayout frameLayout;

    private Fragment fragment;
    private FragmentManager fragmentManager;

    MenuItem selectedItem;
    private int mSelectedItem;
    private static final String SELECTED_ITEM = "arg_selected_item";
    static BottomNavigationMenuView menuView;

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setBottom_nav();
        disableShiftMode(bottom_nav);
        checkPermission();
    }

    public static void disableShiftMode(BottomNavigationView view) {
        menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            //Timber.e(e, "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            //Timber.e(e, "Unable to change value of shift mode");
        }
    }

    private void setBottom_nav() {
        bottom_nav.inflateMenu(R.menu.menu_main);

        fragmentManager = getFragmentManager();
        fragment = new PencarianPage();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment).commit();

        bottom_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });
    }

    private void selectFragment(MenuItem selectedItem) {
        int id = selectedItem.getItemId();
        switch (id){
            case R.id.menu_search:
                fragment = new PencarianPage();
                break;
            case R.id.menu_list:
                fragment = new DaftarHaltePage();
                break;
            case R.id.menu_search_halte:
                fragment = new PencarianRuteHaltePage();
                break;
            case R.id.menu_more:
                fragment = new MorePage();
                break;
        }
        // update selected item
        mSelectedItem = selectedItem.getItemId();
        if(getIntent().getExtras() !=null){
            MenuItem menuItem = bottom_nav.getMenu().getItem(2);
            menuItem.setChecked(true);
        }
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment).commit();
    }


}