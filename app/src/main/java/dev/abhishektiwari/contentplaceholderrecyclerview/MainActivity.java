package dev.abhishektiwari.contentplaceholderrecyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // creating variables for
    // our ui components.
    private RecyclerView courseRV;

    // variable for our adapter
    // class and array list
    private CourseRVAdapter adapter;
    private ArrayList<CourseModal> courseModalArrayList;
    private ShimmerFrameLayout shimmerFrameLayout;

    // below line is the variable for url from
    // where we will be querying our data.
    String url = "https://reqres.in/api/users?page=2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing our variables.
        courseRV = findViewById(R.id.idRVCourses);
        shimmerFrameLayout = findViewById(R.id.shimmerLayout);
        shimmerFrameLayout.startShimmer( );

        // below line we are creating a new array list
        courseModalArrayList = new ArrayList<>( );

        // calling a method to load data.
        getData( );

        // calling method to
        // build recycler view.
        buildRecyclerView( );
    }

    private void getData() {
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);


        // in this case the data we are getting is in the form
        // of array so we are making a json array request.
        // below is the line where we are making an json array
        // request and then extracting data from each json object.


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>( ) {

                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response.getString("data"));
                        } catch (JSONException e) {
                            e.printStackTrace( );
                        }


                        shimmerFrameLayout.stopShimmer( );
                        shimmerFrameLayout.setVisibility(View.GONE);

                        // on below line we are making the
                        // recycler view visibility visible.
                        courseRV.setVisibility(View.VISIBLE);
                        for (int i = 0; i < jsonArray.length( ); i++) {
                            // creating a new json object and
                            // getting each object from our json array.
                            try {

                                // we are getting each json object.
                                JSONObject responseObj = jsonArray.getJSONObject(i);


                                // now we get our response from API in json object format.
                                // in below line we are extracting a string with
                                // its key value from our json object.
                                // similarly we are extracting all the strings from our json object.
                                String name = responseObj.getString("first_name");
                                String lastName = responseObj.getString("last_name");
                                String id = responseObj.getString("id");
                                String avatar = responseObj.getString("avatar");
                                courseModalArrayList.add(new CourseModal(name, avatar, id, lastName));
                                buildRecyclerView( );
                            } catch (JSONException e) {
                                e.printStackTrace( );
                            }
                        }
                    }
                }, new Response.ErrorListener( ) {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                        System.out.println("error " + error);
                        Toast.makeText(MainActivity.this, error.toString( ), Toast.LENGTH_SHORT).show( );
                    }
                });


// Access the RequestQueue through your singleton class.

        //to clear cache files from volley request api
        queue.getCache().clear();
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);



    }

    private void buildRecyclerView() {

        // initializing our adapter class.
        adapter = new CourseRVAdapter(courseModalArrayList, MainActivity.this);

        // adding layout manager
        // to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this);
        courseRV.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        courseRV.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        courseRV.setAdapter(adapter);
    }
}