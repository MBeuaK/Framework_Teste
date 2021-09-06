package com.test.framework.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.framework.R;
import com.test.framework.model.Albums;
import com.test.framework.model.Post;
import com.test.framework.model.Todos;

import java.util.ArrayList;
import java.util.List;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> implements Filterable {

    private List<Albums> albumsList;
    private List<Post> postList;
    private List<Todos> todosList;

    private Context context;

    public CustomAdapter(List<Albums> albumsList, List<Post> postList, List<Todos> todosList, Context context) {
        this.albumsList = albumsList;
        this.postList = postList;
        this.todosList = todosList;
        this.context = context;


    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        TextView textViewerUserId;
        TextView textViewerId;
        TextView textViewTitle;
        TextView textViewBody;
        TextView textViewCompleted;

        TextView textViewBodyHead;
        TextView textViewCompletedHead;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mView = itemView;

            textViewerUserId = mView.findViewById(R.id.userIdResource);
            textViewerId = mView.findViewById(R.id.idResource);
            textViewTitle = mView.findViewById(R.id.titleResource);
            textViewBody = mView.findViewById(R.id.bodyResource);
            textViewCompleted = mView.findViewById(R.id.completedResource);

            textViewBodyHead = mView.findViewById(R.id.textViewBody);
            textViewCompletedHead = mView.findViewById(R.id.textViewCompleted);


        }

    }


    @Override
    public Filter getFilter() {
        return listWithFilter;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        if (albumsList != null) {
            holder.textViewerUserId.setText(String.valueOf(albumsList.get(position).getUserId()));
            holder.textViewerId.setText(String.valueOf(albumsList.get(position).getUserId()));
            holder.textViewTitle.setText(albumsList.get(position).getTitle());
            holder.textViewCompleted.setVisibility(View.GONE);
            holder.textViewCompletedHead.setVisibility(View.GONE);
            holder.textViewBodyHead.setVisibility(View.GONE);
            holder.textViewBody.setVisibility(View.GONE);
        } else if (postList != null) {
            holder.textViewerUserId.setText(String.valueOf(postList.get(position).getUserId()));
            holder.textViewerId.setText(String.valueOf(postList.get(position).getUserId()));
            holder.textViewTitle.setText(postList.get(position).getTitle());
            holder.textViewBody.setText(postList.get(position).getBody());

            holder.textViewBody.setVisibility(View.VISIBLE);
            holder.textViewBodyHead.setVisibility(View.VISIBLE);
            holder.textViewCompleted.setVisibility(View.GONE);
            holder.textViewCompletedHead.setVisibility(View.GONE);

        } else if (todosList != null) {
            holder.textViewerUserId.setText(String.valueOf(todosList.get(position).getUserId()));
            holder.textViewerId.setText(String.valueOf(todosList.get(position).getUserId()));
            holder.textViewTitle.setText(todosList.get(position).getTitle());
            holder.textViewCompleted.setText(String.valueOf(todosList.get(position).isCompleted()));

            holder.textViewCompleted.setVisibility(View.VISIBLE);
            holder.textViewCompletedHead.setVisibility(View.VISIBLE);
            holder.textViewBodyHead.setVisibility(View.GONE);
            holder.textViewBody.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        int size = 0;

        if (albumsList != null) {
            size = albumsList.size();
        } else if (postList != null) {
            size = postList.size();
        } else if (todosList != null) {
            size = todosList.size();
        }

        return size;
    }


    Filter listWithFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (albumsList != null) {
                List<Albums> filteredList = new ArrayList<>();

                if (constraint.equals(null) || constraint.length() == 0) {
                    filteredList.addAll(albumsList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase();

                    for (Albums albums : albumsList) {
                        if (String.valueOf(albums.getId()).toLowerCase().contains(filterPattern) ||
                                String.valueOf(albums.getUserId()).toLowerCase().contains(filterPattern) ||
                                albums.getTitle().toLowerCase().contains(filterPattern)) {
                            filteredList.add(albums);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;


            } else if (postList != null) {
                List<Post> filteredList = new ArrayList<>();

                if (constraint.equals(null) || constraint.length() == 0) {
                    filteredList.addAll(postList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase();

                    for (Post post : postList) {
                        if (String.valueOf(post.getId()).toLowerCase().contains(filterPattern) ||
                                String.valueOf(post.getUserId()).toLowerCase().contains(filterPattern) ||
                                post.getTitle().toLowerCase().contains(filterPattern) ||
                                post.getBody().toLowerCase().contains(filterPattern)) {
                            filteredList.add(post);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;

            } else {
                List<Todos> filteredList = new ArrayList<>();

                if (constraint.equals(null) || constraint.length() == 0) {
                    filteredList.addAll(todosList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase();

                    for (Todos todos : todosList){
                        if(String.valueOf(todos.getId()).toLowerCase().contains(filterPattern) ||
                        String.valueOf(todos.getUserId()).toLowerCase().contains(filterPattern) ||
                        todos.getTitle().toLowerCase().contains(filterPattern) ||
                        String.valueOf(todos.isCompleted()).toLowerCase().contains(filterPattern)){
                            filteredList.add(todos);
                        }
                    }

                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;

            }


        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (albumsList != null) {
                albumsList.clear();
                albumsList.addAll((List) filterResults.values);
                notifyDataSetChanged();

            } else if (postList != null) {
                postList.clear();
                postList.addAll((List) filterResults.values);
                notifyDataSetChanged();

            } else if (todosList != null) {
                todosList.clear();
                todosList.addAll((List) filterResults.values);
                notifyDataSetChanged();

            }

        }
    };


}
