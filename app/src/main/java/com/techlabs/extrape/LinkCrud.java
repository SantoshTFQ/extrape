package com.techlabs.extrape;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class LinkCrud {

    private static final String BASE_URL = "https://api.example.com/links"; // Replace with your API base URL

    private RequestQueue requestQueue;
    private Gson gson;

    public interface VolleyResponseListener<T> {
        void onResponse(T response);
        void onError(String message);
    }

    public LinkCrud(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        gson = new Gson();
    }

    public void addLink(Link link, final VolleyResponseListener<Link> listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL, 
                response -> listener.onResponse(gson.fromJson(response, Link.class)),
                error -> listener.onError(error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                return gson.fromJson(gson.toJson(link), new TypeToken<Map<String, String>>() {}.getType());
            }
        };
        requestQueue.add(stringRequest);
    }

    public void editLink(long id, Link link, final VolleyResponseListener<Link> listener) {
        String url = BASE_URL + "/" + id;
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                response -> listener.onResponse(gson.fromJson(response, Link.class)),
                error -> listener.onError(error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                return gson.fromJson(gson.toJson(link), new TypeToken<Map<String, String>>() {}.getType());
            }
        };
        requestQueue.add(stringRequest);
    }

    public void deleteLink(long id, final VolleyResponseListener<String> listener) {
        String url = BASE_URL + "/" + id;
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, 
                response -> listener.onResponse("Link deleted successfully"),
                error -> listener.onError(error.toString()));
        requestQueue.add(stringRequest);
    }

    public void fetchLinkById(long id, final VolleyResponseListener<Link> listener) {
        String url = BASE_URL + "/" + id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, 
                response -> listener.onResponse(gson.fromJson(response, Link.class)),
                error -> listener.onError(error.toString()));
        requestQueue.add(stringRequest);
    }

    public void fetchAllLinks(final VolleyResponseListener<List<Link>> listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL, 
                response -> {
                    Type listType = new TypeToken<List<Link>>() {}.getType();
                    listener.onResponse(gson.fromJson(response, listType));
                },
                error -> listener.onError(error.toString()));
        requestQueue.add(stringRequest);
    }

    public void fetchLinksByRange(int start, int end, final VolleyResponseListener<List<Link>> listener) {
        String url = BASE_URL + "?start=" + start + "&end=" + end;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, 
                response -> {
                    Type listType = new TypeToken<List<Link>>() {}.getType();
                    listener.onResponse(gson.fromJson(response, listType));
                },
                error -> listener.onError(error.toString()));
        requestQueue.add(stringRequest);
    }
    
    public void updateLink(long id, Link link, final VolleyResponseListener<Link> listener) {
        editLink(id, link, listener);
    }
}