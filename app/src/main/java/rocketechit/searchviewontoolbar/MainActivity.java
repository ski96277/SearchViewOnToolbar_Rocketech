package rocketechit.searchviewontoolbar;

import android.app.SearchManager;
import android.content.Context;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    private static final String[] SUGGESTIONS = {
            "Bauru", "Sao Paulo", "Rio de Janeiro",
            "Bahia", "Mato Grosso", "Minas Gerais",
            "Tocantins", "Rio Grande do Sul"
    };
    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setting the title
        toolbar.setTitle("My Toolbar");
        //placing toolbar in place of actionbar
        setSupportActionBar(toolbar);

        final String[] from = new String[]{"cityName"};
        final int[] to = new int[]{android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        //getting the search view from the menu
        MenuItem searchViewItem = menu.findItem(R.id.menuSearch);

        //getting search manager from systemservice
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        //getting the search view
        final SearchView searchView = (SearchView) searchViewItem.getActionView();

        //you can put a hint for the search input field
        searchView.setQueryHint("Search and show suggestion...");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        //by setting it true we are making it iconified
        //so the search input will show up after taping the search iconified
        //if you want to make it visible all the time make it false
//        searchView.setIconifiedByDefault(true);
        searchView.setSuggestionsAdapter(mAdapter);
//
        searchView.setIconifiedByDefault(true);
        // Getting selected (clicked) item suggestion
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                // Your code here
                Toast.makeText(MainActivity.this, "onSuggestionClick - -" + SUGGESTIONS[position+1], Toast.LENGTH_SHORT).show();

                searchView.setQuery(SUGGESTIONS[position+1],false);
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                // Your code here
                return true;
            }
        });

        //here we will get the search query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //do the search here
                Toast.makeText(MainActivity.this, "" + query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                populateAdapter(s);
                return false;
            }
        });

       /* searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // This is where you can be notified when the `SearchView` is closed
                // and change your views you see fit.
                return true;
            }
        });*/

        return true;

    }


    // You must implements your logic to get data using OrmLite
    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, "cityName"});
        for (int i = 0; i < SUGGESTIONS.length; i++) {
            if (SUGGESTIONS[i].toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[]{i, SUGGESTIONS[i]});
        }
        mAdapter.changeCursor(c);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAbout:
                Toast.makeText(this, "You clicked about", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuSettings:
                Toast.makeText(this, "You clicked settings", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuLogout:
                Toast.makeText(this, "You clicked logout", Toast.LENGTH_SHORT).show();
                break;

        }
        return true;
    }
}
