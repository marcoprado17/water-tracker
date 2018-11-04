package mprado.me.watertracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import mprado.me.watertracker.bottomtabcontent.dashboards.DashboardsFragment;
import mprado.me.watertracker.bottomtabcontent.HomeFragment;
import mprado.me.watertracker.bottomtabcontent.NotificationsFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_holder, new HomeFragment())
                            .commit();
                    return true;
                case R.id.navigation_dashboards:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_holder, new DashboardsFragment())
                            .commit();
                    return true;
                case R.id.navigation_notifications:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_holder, new NotificationsFragment())
                            .commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_holder, new HomeFragment())
                .commit();
    }
}
