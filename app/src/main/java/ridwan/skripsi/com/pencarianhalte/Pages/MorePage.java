package ridwan.skripsi.com.pencarianhalte.Pages;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import ridwan.skripsi.com.pencarianhalte.R;

public class MorePage extends Fragment implements View.OnClickListener {
    @Bind(R.id.toolbar_default) protected Toolbar toolbar_default;
    @Bind(R.id.more_about_menu) protected RelativeLayout more_about_menu;
//    @Bind(R.id.more_feedback_menu) protected RelativeLayout more_feedback_menu;

    ActionBar actionBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View tampilan = inflater.inflate(R.layout.activity_more_page, container, false);
        ButterKnife.bind(this, tampilan);
        setToolbar();

        return tampilan;
    }

    private void setToolbar() {
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar_default);

        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("More");

        more_about_menu.setOnClickListener(this);
//        more_feedback_menu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.more_about_menu:
                startActivity(new Intent(getActivity(), AboutPage.class));
                break;
            /*case R.id.more_feedback_menu:
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, "ridwan@ridwan.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

                startActivity(Intent.createChooser(intent, "Send Email"));
                break;*/
        }
    }
}