package ru.geekbrains.android.selectCity.menu;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.SearchView;

import com.google.android.material.snackbar.Snackbar;

import ru.geekbrains.android.R;
import ru.geekbrains.android.selectCity.SelectCityActivity;

public class MenuActionBar {
    private static final String TAG = "WEATHER";
    SelectCityActivity activity;
    public MenuActionBar(SelectCityActivity activity) {
        this.activity = activity;
    }

    public void createMenu(Menu menu) {
        activity.getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        MenuItem search = menu.findItem(R.id.action_bar_search); // поиск пункта меню поиска
        // Строка поиска
        final SearchView searchText = (SearchView) search.getActionView();
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Реагирует на конец ввода поиска
            @Override
            public boolean onQueryTextSubmit(String query) {
                Snackbar.make(searchText, query, Snackbar.LENGTH_LONG).show();
                //Log.i(TAG, "search");

                // закрытие клавиатуры
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                // обновление окна поиска города
                activity.updateSearch(query);
                return true;
            }
            // Реагирует на нажатие каждой клавиши
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    public boolean selected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.add_context:
//                return true;
//        }
            return false;
    }
}
