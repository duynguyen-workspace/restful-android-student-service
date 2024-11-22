package vn.edu.rmit.restfulstudentservice;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private String json_str = "";
    ProgressBar progressBar;
    private List<Student> studentList;

    ListView listView;
    // private StudentAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("STUDENT_NAME")) {
            String studentName = intent.getStringExtra("STUDENT_NAME");

            // Display the Toast message
            Toast.makeText(this, "New student name: " + studentName + " added!", Toast.LENGTH_LONG).show();
        }

        listView = findViewById(R.id.listView);

        new GetStudent().execute();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(MainActivity.this,"Clicked", Toast.LENGTH_SHORT).show();
            // Student currStudent = (Student) listView.getItemAtPosition(position);
            Student currStudent = studentList.get(position);
            String name = currStudent.getName();

            Log.d("Name: ", name);
            Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();
        });
    }

    public void onNavigateToForm(View v) {
        Intent i = new Intent(MainActivity.this, StudentFormActivity.class);
        startActivity(i);
    }

    public class GetStudent extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            json_str = HttpHandler.getJson("https://67405dc2d0b59228b7efc3c0.mockapi.io/api/v1/students");
            // json_str = HttpHandler.getJson("http://10.0.2.2:3005/students");
            // json_str = HttpHandler.getJson("https://my-json-server.typicode.com/cristalngo/demo/students");
            // Log.d("Load data!", json_str);

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);

            studentList = new ArrayList<>();
            // ListView listView = findViewById(R.id.listView);

            JSONObject root;

            try {
                JSONArray studentsData = new JSONArray(json_str);

                for (int i = 0; i < studentsData.length(); i++) {
                    root = studentsData.getJSONObject(i);
                    String name = root.getString("name");
                    int id = Integer.parseInt(root.getString("id"));

                    Student newStudent = new Student(name, id);
                    studentList.add(newStudent);
                }

                StudentAdapter adapter = new StudentAdapter(studentList, MainActivity.this);
                listView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}

