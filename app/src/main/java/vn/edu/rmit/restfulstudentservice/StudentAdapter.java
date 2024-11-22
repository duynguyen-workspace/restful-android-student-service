package vn.edu.rmit.restfulstudentservice;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class StudentAdapter extends ArrayAdapter<Student> {
    private List<Student> students;
    private Context context;

    public StudentAdapter(List<Student> studentList, Context context) {
        super(context, R.layout.student_item_layout, studentList);

        this.students = studentList;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.student_item_layout, parent, false);
        }

        Student student = students.get(position);

        if (student != null) {
            TextView textId = convertView.findViewById(R.id.studentId);
            textId.setText(String.valueOf(student.getId()));

            TextView textName = convertView.findViewById(R.id.studentName);
            textName.setText(student.getName());

            ImageButton btnEdit = convertView.findViewById(R.id.btnEdit);
            ImageButton btnDelete = convertView.findViewById(R.id.btnDelete);

            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(context, StudentUpdateActivity.class);
                intent.putExtra("NAME", student.getName());
                intent.putExtra("ID", String.valueOf(student.getId()));
                context.startActivity(intent);
            });

            btnDelete.setOnClickListener(v -> {
                // Show confirmation dialog
                new AlertDialog.Builder(context)
                    .setTitle("Delete Confirmation")
                    .setMessage("Are you sure you want to delete this student: "
                            + student.getId() + " - " + student.getName() + "?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        new DeleteStudent(student).execute();
                    })
                    .setNegativeButton("No", null) // Do nothing on "No"
                    .show();
            });

            convertView.setOnClickListener(v -> {
                // Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                String displayMsg = student.getName() + " is clicked!";

                Log.d("ITEM_CLICKED ", displayMsg);
                Toast.makeText(context, displayMsg, Toast.LENGTH_SHORT).show();
            });
        }

        return convertView;
    }

    private class DeleteStudent extends AsyncTask<Void, Void, Void> {
        Student student;

        public DeleteStudent(Student student) {
            super();
            this.student = student;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // String json_str = HttpHandler.deleteJson("http://10.0.2.2:3005/students/" + String.valueOf(student.getId()));
            String json_str = HttpHandler.deleteJson("https://67405dc2d0b59228b7efc3c0.mockapi.io/api/v1/students/" + student.getId());;
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            students.remove(student);
            notifyDataSetChanged();

            Toast.makeText(context, "Delete successfully!", Toast.LENGTH_SHORT).show();

        }
    }
}
