package com.demo.recyclerviewwithsearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView recyclerView;
    RecyclerviewAdapter myRecAdapter;
    List<DataObject> list;
    String searchString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<DataObject>();
        list.add(0, new DataObject("Cupcake",
                "The RecyclerView widget is a more advanced and flexible version of ListView."));
        list.add(1, new DataObject("Donut",
                "The RecyclerView widget is a more advanced and flexible version of ListView."));
        list.add(2, new DataObject("Eclair",
                "The RecyclerView widget is a more advanced and flexible version of ListView."));
        list.add(3, new DataObject("Froyo",
                "The RecyclerView widget is a more advanced and flexible version of ListView."));
        list.add(4, new DataObject("Gingerbread",
                "The RecyclerView widget is a more advanced and flexible version of ListView."));
        list.add(5, new DataObject("Honeycomb",
                "The RecyclerView widget is a more advanced and flexible version of ListView."));
        list.add(6, new DataObject("Ice Cream Sandwich",
                "The RecyclerView widget is a more advanced and flexible version of ListView."));
        list.add(7, new DataObject("Jelly Bean",
                "The RecyclerView widget is a more advanced and flexible version of ListView."));
        list.add(8, new DataObject("KitKat",
                "The RecyclerView widget is a more advanced and flexible version of ListView."));
        list.add(9, new DataObject("Lollipop",
                "The RecyclerView widget is a more advanced and flexible version of ListView."));
        list.add(10, new DataObject("Nougat",
                "The RecyclerView widget is a more advanced and flexible version of ListView."));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        myRecAdapter = new RecyclerviewAdapter(list, MainActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(myRecAdapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin_cardview);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<DataObject> filteredModelList = filter(list, newText);
        if (filteredModelList.size() > 0) {
            myRecAdapter.setFilter(filteredModelList);
            return true;
        } else {
            Toast.makeText(MainActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Adapter
    public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.VH> {

        public List<DataObject> DataList;
        public Context context;
        ArrayList<DataObject> mModel;

        public RecyclerviewAdapter(List<DataObject> parkingList, Context context) {
            this.DataList = parkingList;
            this.context = context;
        }

        @Override
        public RecyclerviewAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecyclerviewAdapter.VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerviewAdapter.VH holder, int position) {

            holder.htxt.setText(DataList.get(position).getHeading());
            holder.dtxt.setText(DataList.get(position).getDescription());

            DataObject txt = DataList.get(position);
            String name = txt.getHeading().toLowerCase(Locale.getDefault());
            if (name.contains(searchString)) {

                int startPos = name.indexOf(searchString);
                int endPos = startPos + searchString.length();

                Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.htxt.getText());
                spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                holder.htxt.setText(spanString);
            }
        }

        @Override
        public int getItemCount() {
            return DataList.size();
        }

        public class VH extends RecyclerView.ViewHolder {
            TextView htxt,dtxt;

            public VH(View itemView) {
                super(itemView);

                htxt = (TextView) itemView.findViewById(R.id.textView);
                dtxt = (TextView) itemView.findViewById(R.id.textView2);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String name = htxt.getText().toString();
                        Toast.makeText(getApplicationContext(),name,Toast.LENGTH_SHORT).show();

                    }
                });
            }

        }

        public void setFilter(List<DataObject> countryModels) {
            mModel = new ArrayList<>();
            mModel.addAll(countryModels);
            notifyDataSetChanged();
        }

    }

    // Search Method
    private List<DataObject> filter(List<DataObject> models, String query) {

        query = query.toLowerCase();
        this.searchString=query;

        final List<DataObject> filteredModelList = new ArrayList<>();
        for (DataObject model : models) {
            final String text = model.getHeading().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        myRecAdapter = new RecyclerviewAdapter(filteredModelList, MainActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(myRecAdapter);
        myRecAdapter.notifyDataSetChanged();

        return filteredModelList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem searchitem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchitem);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        TextView searchText = (TextView)
                searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        searchText.setTextColor(Color.parseColor("#FFFFFF"));
        searchText.setHintTextColor(Color.parseColor("#FFFFFF"));
        searchText.setHint("Search Android Version...");
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
