package com.vxhvx.democrachess.democrachess;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;

public class GameActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageButton[][] board = new ImageButton[8][8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        board[0][0] = (ImageButton) findViewById(R.id.a1);
        board[0][1] = (ImageButton) findViewById(R.id.a2);
        board[0][2] = (ImageButton) findViewById(R.id.a3);
        board[0][3] = (ImageButton) findViewById(R.id.a4);
        board[0][4] = (ImageButton) findViewById(R.id.a5);
        board[0][5] = (ImageButton) findViewById(R.id.a6);
        board[0][6] = (ImageButton) findViewById(R.id.a7);
        board[0][7] = (ImageButton) findViewById(R.id.a8);
        board[1][0] = (ImageButton) findViewById(R.id.b1);
        board[1][1] = (ImageButton) findViewById(R.id.b2);
        board[1][2] = (ImageButton) findViewById(R.id.b3);
        board[1][3] = (ImageButton) findViewById(R.id.b4);
        board[1][4] = (ImageButton) findViewById(R.id.b5);
        board[1][5] = (ImageButton) findViewById(R.id.b6);
        board[1][6] = (ImageButton) findViewById(R.id.b7);
        board[1][7] = (ImageButton) findViewById(R.id.b8);
        board[2][0] = (ImageButton) findViewById(R.id.c1);
        board[2][1] = (ImageButton) findViewById(R.id.c2);
        board[2][2] = (ImageButton) findViewById(R.id.c3);
        board[2][3] = (ImageButton) findViewById(R.id.c4);
        board[2][4] = (ImageButton) findViewById(R.id.c5);
        board[2][5] = (ImageButton) findViewById(R.id.c6);
        board[2][6] = (ImageButton) findViewById(R.id.c7);
        board[2][7] = (ImageButton) findViewById(R.id.c8);
        board[3][0] = (ImageButton) findViewById(R.id.d1);
        board[3][1] = (ImageButton) findViewById(R.id.d2);
        board[3][2] = (ImageButton) findViewById(R.id.d3);
        board[3][3] = (ImageButton) findViewById(R.id.d4);
        board[3][4] = (ImageButton) findViewById(R.id.d5);
        board[3][5] = (ImageButton) findViewById(R.id.d6);
        board[3][6] = (ImageButton) findViewById(R.id.d7);
        board[3][7] = (ImageButton) findViewById(R.id.d8);
        board[4][0] = (ImageButton) findViewById(R.id.e1);
        board[4][1] = (ImageButton) findViewById(R.id.e2);
        board[4][2] = (ImageButton) findViewById(R.id.e3);
        board[4][3] = (ImageButton) findViewById(R.id.e4);
        board[4][4] = (ImageButton) findViewById(R.id.e5);
        board[4][5] = (ImageButton) findViewById(R.id.e6);
        board[4][6] = (ImageButton) findViewById(R.id.e7);
        board[4][7] = (ImageButton) findViewById(R.id.e8);
        board[5][0] = (ImageButton) findViewById(R.id.f1);
        board[5][1] = (ImageButton) findViewById(R.id.f2);
        board[5][2] = (ImageButton) findViewById(R.id.f3);
        board[5][3] = (ImageButton) findViewById(R.id.f4);
        board[5][4] = (ImageButton) findViewById(R.id.f5);
        board[5][5] = (ImageButton) findViewById(R.id.f6);
        board[5][6] = (ImageButton) findViewById(R.id.f7);
        board[5][7] = (ImageButton) findViewById(R.id.f8);
        board[6][0] = (ImageButton) findViewById(R.id.g1);
        board[6][1] = (ImageButton) findViewById(R.id.g2);
        board[6][2] = (ImageButton) findViewById(R.id.g3);
        board[6][3] = (ImageButton) findViewById(R.id.g4);
        board[6][4] = (ImageButton) findViewById(R.id.g5);
        board[6][5] = (ImageButton) findViewById(R.id.g6);
        board[6][6] = (ImageButton) findViewById(R.id.g7);
        board[6][7] = (ImageButton) findViewById(R.id.g8);
        board[7][0] = (ImageButton) findViewById(R.id.h1);
        board[7][1] = (ImageButton) findViewById(R.id.h2);
        board[7][2] = (ImageButton) findViewById(R.id.h3);
        board[7][3] = (ImageButton) findViewById(R.id.h4);
        board[7][4] = (ImageButton) findViewById(R.id.h5);
        board[7][5] = (ImageButton) findViewById(R.id.h6);
        board[7][6] = (ImageButton) findViewById(R.id.h7);
        board[7][7] = (ImageButton) findViewById(R.id.h8);

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if((j + i) % 2 == 0) board[i][j].setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                else board[i][j].setBackgroundColor(getResources().getColor(android.R.color.white));
            }
        }

        clear_board();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void clear_board() {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                this.board[i][j].setImageResource(R.drawable.transparent_sector);
            }
        }
    }
}
