package com.example.nrst1.scheduleguide;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TagListActivity extends AppCompatActivity {

    FirebaseHandler firebaseHandler;
    DatabaseReference tagTable;

    DrawerLayout drawerLayout;
    FrameLayout sideMenuContainer;
    ImageView addTagImg;

    ArrayList<Tag> tags;

    RecyclerView recyclerView;
    RecyclerView.Adapter tagAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_list);

        firebaseHandler = new FirebaseHandler(getApplicationContext());
        tagTable = firebaseHandler.getTagTable();

        initActionBar();

        tags = new ArrayList<>();

        addTagImg = (ImageView) findViewById(R.id.add_tag_img);
        addTagImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tag tag = new Tag("", "#FF0000", 0);
                tags.add(tag);
                tagAdapter.notifyDataSetChanged();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.tag_recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        tagAdapter = new TagAdapter(this, tags);
        recyclerView.setAdapter(tagAdapter);
        setTagTable();

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                tagTable.child(String.valueOf(tags.get(position).getKey())).removeValue();
                tags.remove(position);
                tagAdapter.notifyItemRemoved(position);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void initActionBar() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        sideMenuContainer = (FrameLayout) findViewById(R.id.side_bar_fragment_container);

        ActionBarHandler actionBarHandler = new ActionBarHandler(this, getSupportActionBar());
        actionBarHandler.setBasicActionBar();
        actionBarHandler.setTitle("태그");
        actionBarHandler.setDrawerMenu(drawerLayout, sideMenuContainer);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(sideMenuContainer)) drawerLayout.closeDrawer(sideMenuContainer);
        else super.onBackPressed();
    }

    public void setTagTable() {
        tagTable.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tags.clear();

                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    Tag tag = data.getValue(Tag.class);
                    tags.add(tag);
                }

                tagAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
