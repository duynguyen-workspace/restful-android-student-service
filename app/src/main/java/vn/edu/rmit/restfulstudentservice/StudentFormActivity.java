package vn.edu.rmit.restfulstudentservice;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class StudentFormActivity extends AppCompatActivity {
    String json_str = null;
    Student addStudent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_form);

    }

    public void createStudent(View v) {
        new PostStudent().execute();
    }

    private class PostStudent extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            EditText inputIdEdit = findViewById(R.id.inputId);
            EditText inputNameEdit = findViewById(R.id.inputName);

            String inputId = inputIdEdit.getText().toString();
            String inputName = inputNameEdit.getText().toString();

            if (inputId.isEmpty()) {
                Toast.makeText(StudentFormActivity.this, "Please input a student ID!", Toast.LENGTH_SHORT).show();
            }

            if (inputName.isEmpty()) {
                Toast.makeText(StudentFormActivity.this, "Please input a student name!", Toast.LENGTH_SHORT).show();
            }

            if (!inputId.isEmpty() && !inputName.isEmpty()) {
                addStudent = new Student(inputName, Integer.parseInt(inputId));
                // json_str = HttpHandler.postJson(addStudent,"http://10.0.2.2:3005/students");
                json_str = HttpHandler.postJson(addStudent,"https://67405dc2d0b59228b7efc3c0.mockapi.io/api/v1/students");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Intent i = new Intent(StudentFormActivity.this, MainActivity.class);
            i.putExtra("STUDENT_NAME", addStudent.getName());
            setResult(RESULT_OK, i);
            finish();
        }
    }


}