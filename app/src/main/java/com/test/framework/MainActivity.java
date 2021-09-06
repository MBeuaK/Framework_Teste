package com.test.framework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.test.framework.adapter.CustomAdapter;
import com.test.framework.di.ServiceClient;
import com.test.framework.di.ServiceModule;
import com.test.framework.model.Albums;
import com.test.framework.model.Post;
import com.test.framework.model.Todos;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    private ServiceModule service;

    private SwipeRefreshLayout swipeContainer;

    private Boolean flagPosts = false;
    private Boolean flagAlbums = false;
    private Boolean flagTodos = false;



    private RecyclerView list;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        service = ServiceClient.getRetrofitInstance().create(ServiceModule.class);

        swipeContainer = findViewById(R.id.swipeContainer);

        list = findViewById(R.id.list);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPostsResources();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.example_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.page_1:
                    if(!flagPosts){
                        flagPosts = true;
                        getPostsResources();
                        list.setVisibility(View.VISIBLE);
                        flagAlbums = false;
                        flagTodos = false;

                    } else {
                        flagPosts = false;
                        list.setVisibility(View.GONE);
                    }

                    break;

                case R.id.page_2:
                    if(!flagAlbums){
                        flagAlbums = true;
                        list.setVisibility(View.VISIBLE);
                        getAlbumsResources();
                        flagPosts = false;
                        flagTodos = false;

                    } else {
                        list.setVisibility(View.GONE);
                        flagAlbums = false;
                    }

                    break;

                case R.id.page_3:
                    if(!flagTodos){
                        flagTodos = true;
                        list.setVisibility(View.VISIBLE);
                        getTodosResources();
                        flagPosts = false;
                        flagAlbums = false;

                    }
                    else{
                        list.setVisibility(View.GONE);
                        flagTodos = false;
                    }
                    break;

            }

            return true;
        }
    };


    private void generateDataList(List<Post> postList, List<Albums> albumsList, List<Todos> todosList) {

        adapter = new CustomAdapter(albumsList, postList, todosList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        list.setLayoutManager(layoutManager);
        list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        list.setAdapter(adapter);

    }


    private void getAlbumsResources() {
        //Progress Dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Call<List<Albums>> callAlbums = service.getListAlbums();

        callAlbums.enqueue(new Callback<List<Albums>>() {
            @Override
            public void onResponse(Call<List<Albums>> call, Response<List<Albums>> response) {
                Log.d("getAlbumsResources", "resources: " + response.body());
                progressDialog.dismiss();
                swipeContainer.setRefreshing(false);
                generateDataList(null, response.body(), null);

            }

            @Override
            public void onFailure(Call<List<Albums>> call, Throwable t) {
                Log.d("getAlbumsResources", "Throwable: " + t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Ops! Something went wrong! Try again!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getTodosResources() {
        //Progress Dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Call<List<Todos>> callTodos = service.getListTodos();

        callTodos.enqueue(new Callback<List<Todos>>() {
            @Override
            public void onResponse(Call<List<Todos>> call, Response<List<Todos>> response) {
                Log.d("getTodosResources", "resources: " + response.body());
                generateDataList(null, null, response.body());
                swipeContainer.setRefreshing(false);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Todos>> call, Throwable t) {
                Log.d("getTodosResources", "Throwable: " + t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Ops! Something went wrong! Try again!", Toast.LENGTH_LONG).show();
            }
        });


    }


    private void getPostsResources() {
        //Progress Dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Call<List<Post>> callPost = service.getListPost();

        callPost.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                Log.d("getPostResources", "resources: " + response.body());
                progressDialog.dismiss();
                swipeContainer.setRefreshing(false);
                generateDataList(response.body(), null, null);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d("getPostResources", "Throwable: " + t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Ops! Something went wrong! Try again!", Toast.LENGTH_LONG).show();
            }
        });


    }

}