package com.example.corre;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportFragment extends Fragment {

    ListView listViewRun;
    DatabaseReference databaseRuns = FirebaseDatabase.getInstance().getReference("Runs");
    List<Run> runList;
    Button share;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_report, container, false);

        listViewRun = view.findViewById(R.id.listViewRun);
        runList = new ArrayList<>();

        //Share Button
        share =view.findViewById(R.id.btn_Share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = Run.runTOShare(runList.get(0));  // share run or profile?
                String shareSub = "Compare Stats with Me in Corre!";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(myIntent.createChooser(myIntent, "Share Corre with..."));
            }

        });

return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseRuns.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                runList.clear();
                for (DataSnapshot runSnapshot : dataSnapshot.getChildren()) {
                    Run run = runSnapshot.getValue(Run.class);
                    runList.add(run);
                }
                //makes list Chronological
                Collections.reverse(runList);

                // Maintains List size
                if (runList.size() > 5) {
                    onlyFive(runList.size());
                }

                RunList adapter = new RunList((AppCompatActivity) getContext(), runList);
                listViewRun.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

            public void onlyFive(int t) {
                while (t > 5) {
                    t -= 1;
                    String id = runList.get(t).id;
                    databaseRuns.child(id).removeValue();

                }
            }
        });

 }

}
