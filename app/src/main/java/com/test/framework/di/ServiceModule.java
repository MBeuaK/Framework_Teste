package com.test.framework.di;

import com.test.framework.model.Albums;
import com.test.framework.model.Post;
import com.test.framework.model.Todos;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceModule {

    @GET("/posts")
    Call<List<Post>> getListPost();

    @GET("/albums")
    Call<List<Albums>> getListAlbums();

    @GET("/todos")
    Call<List<Todos>> getListTodos();

}
