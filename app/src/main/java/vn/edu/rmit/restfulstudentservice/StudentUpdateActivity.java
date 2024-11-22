package vn.edu.rmit.restfulstudentservice;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StudentUpdateActivity extends AppCompatActivity {
    Student currStudent;
    TextView updateIdView;
    EditText currentNameView;
    EditText newNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_update);

        Intent intent = getIntent();
        String studentName = intent.getStringExtra("NAME");
        int id = Integer.parseInt(intent.getStringExtra("ID"));

        currStudent = new Student(studentName, id);

        updateIdView = findViewById(R.id.updateId);
        currentNameView = findViewById(R.id.currentName);
        newNameView = findViewById(R.id.inputNewName);

        updateIdView.setText(String.valueOf(id));
        currentNameView.setText(studentName);

        currentNameView.setEnabled(false);

    }

    public void updateStudent(View v) {
        String inputNewName = newNameView.getText().toString();

        if (inputNewName.isEmpty()) {
            Toast.makeText(this, String.format("Student ID (%d) is not updated", currStudent.getId()), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(StudentUpdateActivity.this, MainActivity.class);
            setResult(RESULT_OK, i);
            finish();
        } else {
            currStudent.setName(inputNewName);
            new UpdateStudent().execute();
        }

    }

    private class UpdateStudent extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String json_str = HttpHandler.putJson(currStudent, "https://67405dc2d0b59228b7efc3c0.mockapi.io/api/v1/students/" + currStudent.getId());
            // String json_str = HttpHandler.putJson(currStudent, "http://10.0.2.2:3005/students/" + currStudent.getId());
            Log.i("UPDATE_STATUS", json_str);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Toast.makeText(StudentUpdateActivity.this, "201 - updated successfully", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(StudentUpdateActivity.this, MainActivity.class);
            setResult(RESULT_OK, i);
            finish();
        }
    }
}