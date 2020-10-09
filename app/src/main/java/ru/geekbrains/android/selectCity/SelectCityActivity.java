package ru.geekbrains.android.selectCity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import ru.geekbrains.android.R;
import ru.geekbrains.android.selectCity.menu.MenuActionBar;
import ru.geekbrains.android.selectCity.menu.MenuBottom;

public class SelectCityActivity extends AppCompatActivity {
    private static final String TAG = "WEATHER";
    private CheckBox checkWindSpeed;
    private CheckBox checkPressure;
    private TextInputEditText editCity;

    private SelectCity selectCity;
    private CitiesFragment citiesFragment;

    private MenuBottom menuBottom;
    private MenuActionBar menuActionBar;
    private MenuDrawer menuDrawer;

    private GoogleAuth googleAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectCity = new SelectCity();
        googleAuth = new GoogleAuth();
        // setContentView(R.layout.activity_select_city);
        setContentView(R.layout.activity_select_city_drawer); // активити с drawer

        checkWindSpeed = findViewById(R.id.select_city_check_wind_speed);
        checkPressure = findViewById(R.id.select_city_check_pressure);
        editCity = findViewById(R.id.select_city_editText);

        SelectCity selectCity = (SelectCity) getIntent().getSerializableExtra(SelectCity.SELECT_CITY);

        if (selectCity != null) {
            checkWindSpeed.setChecked(selectCity.isWindSpeed());
            checkPressure.setChecked(selectCity.isPressure());
        }

        setButton();
        setSelectCity();

        // Обработчик BottomBar
        menuBottom = new MenuBottom(this);
        menuActionBar = new MenuActionBar(this);
        //Navigation Drawer — боковое навигационное меню приложения
        menuDrawer = new MenuDrawer();

    }

    // Обработка события по ножатию кнопки возврата
    // Если Drawer открыт то закрыть
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Здесь определяем меню приложения (активити)
        menuActionBar.createMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Обработка выбора пункта меню приложения (активити)
        //menuActionBar.selected(item);
        return super.onOptionsItemSelected(item);
    }


    private void setSelectCity() {

        // Проверим, что фрагмент существует в activity
        if (getSupportFragmentManager().findFragmentById(R.id.select_city_frame) == null) {
            citiesFragment = new CitiesFragment();
            // Добавим фрагмент на activity
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.select_city_frame, citiesFragment)
                    .commit();

        } else {
            citiesFragment = (CitiesFragment) getSupportFragmentManager().findFragmentById(R.id.select_city_frame);
        }

    }


    private void setButton() {

        // OK
        findViewById(R.id.select_city_button_ok).setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnMain();

            }
        }));


        // убрать кнопку
        findViewById(R.id.select_city_button_ok).setVisibility(View.GONE);
    }

    public void returnMain() {
        save();

        // Передача данных в main
        Intent intentResult = new Intent();
        intentResult.putExtra(SelectCity.SELECT_CITY, selectCity);
        setResult(RESULT_OK, intentResult);

        finish();
    }

    private void save() {
        selectCity.setPressure(checkPressure.isChecked());
        selectCity.setWindSpeed(checkWindSpeed.isChecked());

        if (editCity.getText().length() > 0 && citiesFragment.getArguments() != null) {
            String city = citiesFragment.getArguments().getString(CitiesFragment.CITY_ID);
            selectCity.setCity(city);
            int num_city = citiesFragment.getArguments().getInt(CitiesFragment.NUM_CITY_ID);
            selectCity.setNum_city(num_city);
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle saveInstanceState) {
        super.onRestoreInstanceState(saveInstanceState);

        if (saveInstanceState != null && saveInstanceState.get(SelectCity.SELECT_CITY) instanceof SelectCity) {
            selectCity = (SelectCity) saveInstanceState.get(SelectCity.SELECT_CITY);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        save();
        saveInstanceState.putSerializable(SelectCity.SELECT_CITY, selectCity);

    }

    // Получение результатов от окна регистрации пользователя
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        googleAuth.handleSignInResult(requestCode,data);
    }

    public void updateSearch(String searchText) {
        Log.i(TAG, searchText);
        citiesFragment.updateCityList(searchText);
    }

    public void updateHistory() {
        citiesFragment.updateHistory();
    }

    public MenuDrawer getMenuDrawer() {
        return menuDrawer;
    }

    public GoogleAuth getGoogleAuth() {
        return googleAuth;
    }


    public class MenuDrawer implements NavigationView.OnNavigationItemSelectedListener{
        SelectCityActivity activity = SelectCityActivity.this;
        DrawerLayout drawer;
        NavigationView navigationView;
        public MenuDrawer() {
            init();
        }

        private void init() {
            Toolbar toolbar = initToolbar();
            initDrawer(toolbar);
        }
        private Toolbar initToolbar() {
            Toolbar toolbar = activity.findViewById(R.id.toolbar);
            activity.setSupportActionBar(toolbar);
            return toolbar;
        }

        private void initDrawer(Toolbar toolbar) {
            drawer = activity.findViewById (R.id.drawer_layout);
            navigationView = activity.findViewById(R.id.nav_view_drawer);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    activity , drawer , toolbar , R.string.app_city_London ,
                    R.string.app_city_Berlin );
            drawer.addDrawerListener (toggle);

            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);

            setAuth();
        }


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();

            switch (id) {
                case R.id.nav_list_cities:
                    activity.updateSearch("");
                    break;

                case R.id.nav_history:
                    activity.updateHistory();
                    break;

                case R.id.nav_auth_in:
                    activity.getGoogleAuth().signIn();
                    break;

                case R.id.nav_auth_out:
                    activity.getGoogleAuth().signOut();
                    break;
            }

            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

        private void setAuth() {

            if (activity.getGoogleAuth().isSign()) {
                disableSign();
            } else {
                enableSign();
            }
        }

        public void enableSign(){
            navigationView.getMenu().findItem(R.id.nav_auth_in).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_auth_out).setVisible(false);
        }

        public void disableSign(){
            navigationView.getMenu().findItem(R.id.nav_auth_in).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_auth_out).setVisible(true);
        }
    }

    private class GoogleAuth {
        /*
        Google Auth API
        Build gradle
        implementation 'com.google.android.gms:play-services-auth:18.1.0'
        */

        // Используется, чтобы определить результат активити регистрации через Гугл
        public static final int RC_SIGN_IN = 40404;
        private static final String TAG = "GoogleAuth";
        private boolean sign = false;
        // Клиент для регистрации пользователя через Гугл
        private GoogleSignInClient googleSignInClient;
        private Context context;

        public GoogleAuth() {
            context = SelectCityActivity.this;
            init();
        }

        private void init() {
            // Конфигурация запроса на регистрацию пользователя, чтобы получить
            // идентификатор пользователя, его почту и основной профайл (регулируется параметром)
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            // Получить клиента для регистрации, а также данных по клиенту
            googleSignInClient = GoogleSignIn.getClient(context, gso);
            sign = false;

            // Проверим, заходил ли пользователь в этом приложении через Гугл
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
            if (account != null) {
                // Пользователь уже заходил
                sign = true;
            }

        }

        public void handleSignInResult(int requestCode, Intent data) {
            if (requestCode == RC_SIGN_IN) {
                // Когда сюда возвращается Task, результаты по нему уже готовы.
                Task<GoogleSignInAccount> completedTask= GoogleSignIn.getSignedInAccountFromIntent(data);
                // handleSignInResult(task);
                try {
                    GoogleSignInAccount account = completedTask.getResult(ApiException.class);

                    // Регистрация прошла успешно
                    sign = true;
                    in();

                    Toast.makeText(getApplicationContext(), "Email: "+account.getEmail(), Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "OK Email = "+account.getEmail());
                    Log.w(TAG, "getDisplayName = "+account.getDisplayName());

                } catch (ApiException e) {
                    // The ApiException status code indicates the detailed failure reason.
                    // Please refer to the GoogleSignInStatusCodes class reference for more information.
                    Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                }
            }
        }

        // Инициация регистрации пользователя
        private void signIn() {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }

        // Выход из учетной записи в приложении
        private void signOut() {
            googleSignInClient.signOut()
                    .addOnCompleteListener(SelectCityActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            sign = false;
                            out();
                        }
                    });
        }


        private void in() {
            menuDrawer.disableSign();
        }

        private void out() {
            Toast.makeText(getApplicationContext(), "Exit authorization", Toast.LENGTH_SHORT).show();
            menuDrawer.enableSign();
        }

        public boolean isSign() {
            return sign;
        }
    }
}
