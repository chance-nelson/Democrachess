package com.vxhvx.democrachess.democrachess;

import android.graphics.Color;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
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
import android.widget.TextView;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;


import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageButton[][] boardButtonArray = new ImageButton[8][8];
    private API client;
    private Board board;
    private Map<String, Integer> votes;
    private boolean pieceSelected = false;
    private ImageButton selected;
    private TextView textViewVoteStats;
    private ImageView imageViewTurn;
    private ImageView imageViewNavPlayerTeam;
    private String[] playerStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle bundle = getIntent().getExtras();
        String jwt = null;
        String username = null;
        if (bundle != null) {
            jwt = bundle.getString("jwt");
            username = bundle.getString("username");
        }

        if(jwt == null) jwt = getIntent().getStringExtra("jwt");
        if(username == null) username = getIntent().getStringExtra("username");

        this.client = new API("http://192.168.0.5:5000", jwt, username);

        boardButtonArray[0][0] = (ImageButton) findViewById(R.id.a1);
        boardButtonArray[0][1] = (ImageButton) findViewById(R.id.a2);
        boardButtonArray[0][2] = (ImageButton) findViewById(R.id.a3);
        boardButtonArray[0][3] = (ImageButton) findViewById(R.id.a4);
        boardButtonArray[0][4] = (ImageButton) findViewById(R.id.a5);
        boardButtonArray[0][5] = (ImageButton) findViewById(R.id.a6);
        boardButtonArray[0][6] = (ImageButton) findViewById(R.id.a7);
        boardButtonArray[0][7] = (ImageButton) findViewById(R.id.a8);
        boardButtonArray[1][0] = (ImageButton) findViewById(R.id.b1);
        boardButtonArray[1][1] = (ImageButton) findViewById(R.id.b2);
        boardButtonArray[1][2] = (ImageButton) findViewById(R.id.b3);
        boardButtonArray[1][3] = (ImageButton) findViewById(R.id.b4);
        boardButtonArray[1][4] = (ImageButton) findViewById(R.id.b5);
        boardButtonArray[1][5] = (ImageButton) findViewById(R.id.b6);
        boardButtonArray[1][6] = (ImageButton) findViewById(R.id.b7);
        boardButtonArray[1][7] = (ImageButton) findViewById(R.id.b8);
        boardButtonArray[2][0] = (ImageButton) findViewById(R.id.c1);
        boardButtonArray[2][1] = (ImageButton) findViewById(R.id.c2);
        boardButtonArray[2][2] = (ImageButton) findViewById(R.id.c3);
        boardButtonArray[2][3] = (ImageButton) findViewById(R.id.c4);
        boardButtonArray[2][4] = (ImageButton) findViewById(R.id.c5);
        boardButtonArray[2][5] = (ImageButton) findViewById(R.id.c6);
        boardButtonArray[2][6] = (ImageButton) findViewById(R.id.c7);
        boardButtonArray[2][7] = (ImageButton) findViewById(R.id.c8);
        boardButtonArray[3][0] = (ImageButton) findViewById(R.id.d1);
        boardButtonArray[3][1] = (ImageButton) findViewById(R.id.d2);
        boardButtonArray[3][2] = (ImageButton) findViewById(R.id.d3);
        boardButtonArray[3][3] = (ImageButton) findViewById(R.id.d4);
        boardButtonArray[3][4] = (ImageButton) findViewById(R.id.d5);
        boardButtonArray[3][5] = (ImageButton) findViewById(R.id.d6);
        boardButtonArray[3][6] = (ImageButton) findViewById(R.id.d7);
        boardButtonArray[3][7] = (ImageButton) findViewById(R.id.d8);
        boardButtonArray[4][0] = (ImageButton) findViewById(R.id.e1);
        boardButtonArray[4][1] = (ImageButton) findViewById(R.id.e2);
        boardButtonArray[4][2] = (ImageButton) findViewById(R.id.e3);
        boardButtonArray[4][3] = (ImageButton) findViewById(R.id.e4);
        boardButtonArray[4][4] = (ImageButton) findViewById(R.id.e5);
        boardButtonArray[4][5] = (ImageButton) findViewById(R.id.e6);
        boardButtonArray[4][6] = (ImageButton) findViewById(R.id.e7);
        boardButtonArray[4][7] = (ImageButton) findViewById(R.id.e8);
        boardButtonArray[5][0] = (ImageButton) findViewById(R.id.f1);
        boardButtonArray[5][1] = (ImageButton) findViewById(R.id.f2);
        boardButtonArray[5][2] = (ImageButton) findViewById(R.id.f3);
        boardButtonArray[5][3] = (ImageButton) findViewById(R.id.f4);
        boardButtonArray[5][4] = (ImageButton) findViewById(R.id.f5);
        boardButtonArray[5][5] = (ImageButton) findViewById(R.id.f6);
        boardButtonArray[5][6] = (ImageButton) findViewById(R.id.f7);
        boardButtonArray[5][7] = (ImageButton) findViewById(R.id.f8);
        boardButtonArray[6][0] = (ImageButton) findViewById(R.id.g1);
        boardButtonArray[6][1] = (ImageButton) findViewById(R.id.g2);
        boardButtonArray[6][2] = (ImageButton) findViewById(R.id.g3);
        boardButtonArray[6][3] = (ImageButton) findViewById(R.id.g4);
        boardButtonArray[6][4] = (ImageButton) findViewById(R.id.g5);
        boardButtonArray[6][5] = (ImageButton) findViewById(R.id.g6);
        boardButtonArray[6][6] = (ImageButton) findViewById(R.id.g7);
        boardButtonArray[6][7] = (ImageButton) findViewById(R.id.g8);
        boardButtonArray[7][0] = (ImageButton) findViewById(R.id.h1);
        boardButtonArray[7][1] = (ImageButton) findViewById(R.id.h2);
        boardButtonArray[7][2] = (ImageButton) findViewById(R.id.h3);
        boardButtonArray[7][3] = (ImageButton) findViewById(R.id.h4);
        boardButtonArray[7][4] = (ImageButton) findViewById(R.id.h5);
        boardButtonArray[7][5] = (ImageButton) findViewById(R.id.h6);
        boardButtonArray[7][6] = (ImageButton) findViewById(R.id.h7);
        boardButtonArray[7][7] = (ImageButton) findViewById(R.id.h8);

        textViewVoteStats = (TextView) findViewById(R.id.textViewVoteStats);
        imageViewTurn = (ImageView) findViewById(R.id.imageViewTurn);

        this.board = new Board();

        clear_board();
        update_game();

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++)
                boardButtonArray[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageButton currentSquare = findViewById(v.getId());
                        MoveList legalMoves = null;

                        try {
                            legalMoves = MoveGenerator.getInstance().generateLegalMoves(board);
                        } catch (MoveGeneratorException e) {
                            e.printStackTrace();
                        }

                        // Check if a square has already been selected
                        if (pieceSelected) {
                            // If a square is selected, check if the square just tapped is among the
                            // selected square's legal moves. If it is, sent a move vote to the
                            // server. Otherwise, deselect the current piece and select the tapped
                            // square

                            if (square_among_legal_moves(get_string_from_square_id(selected.getId()),
                                    get_string_from_square_id(v.getId()))) {
                                client.vote(get_string_from_square_id(selected.getId()) + get_string_from_square_id(v.getId()));
                                System.out.println("Voted!");
                                clear_board_selections();
                                pieceSelected = false;
                                return;
                            } else {
                                pieceSelected = false;
                                clear_board_selections();
                            }
                        }

                        select_legal_moves(get_string_from_square_id(v.getId()));
                        pieceSelected = true;
                        selected = currentSquare;
                    }
                });
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        new Thread() {
            public void run() {
                try{
                    synchronized (this) {
                        while(true) {
                            wait(3000);
                            String[] gameState = null;
                            board = new Board();

                            try {
                                gameState = client.get_game_state();
                                playerStats = client.get_player_info(client.get_username());
                            } catch (Exception e) {
                                e.printStackTrace();
                                continue;
                            }

                            board.loadFromFEN(gameState[0]);

                            JSONObject votesJSON;
                            try {
                                votesJSON = new JSONObject(gameState[1]);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return;
                            }

                            votes = to_map(votesJSON);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    update_game();
                                }
                            });
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch(NetworkOnMainThreadException e) {e.printStackTrace();}
                //Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                //intent.putExtra("jwt", client.get_jwt());
                //startActivity(intent);
            }
        }.start();
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
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void clear_board() {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                this.boardButtonArray[i][j].setImageResource(R.drawable.transparent_sector);
            }
        }

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if((j + i) % 2 == 0) this.boardButtonArray[i][j].setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                else this.boardButtonArray[i][j].setBackgroundColor(getResources().getColor(android.R.color.white));
            }
        }

        if(pieceSelected) select_legal_moves(get_string_from_square_id(selected.getId()));
    }

    public void update_game() {
        Piece[] pieces = board.boardToArray();

        clear_board();

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                switch(pieces[8 * j + i].value()) {
                    case("WHITE_PAWN"):
                        boardButtonArray[i][j].setImageResource(R.drawable.pawn_white);
                        continue;
                    case("BLACK_PAWN"):
                        boardButtonArray[i][j].setImageResource(R.drawable.pawn_black);
                        continue;
                    case("WHITE_ROOK"):
                        boardButtonArray[i][j].setImageResource(R.drawable.rook_white);
                        continue;
                    case("BLACK_ROOK"):
                        boardButtonArray[i][j].setImageResource(R.drawable.rook_black);
                        continue;
                    case("WHITE_KNIGHT"):
                        boardButtonArray[i][j].setImageResource(R.drawable.knight_white);
                        continue;
                    case("BLACK_KNIGHT"):
                        boardButtonArray[i][j].setImageResource(R.drawable.knight_black);
                        continue;
                    case("WHITE_BISHOP"):
                        boardButtonArray[i][j].setImageResource(R.drawable.bishop_white);
                        continue;
                    case("BLACK_BISHOP"):
                        boardButtonArray[i][j].setImageResource(R.drawable.bishop_black);
                        continue;
                    case("WHITE_KING"):
                        boardButtonArray[i][j].setImageResource(R.drawable.king_white);
                        continue;
                    case("BLACK_KING"):
                        boardButtonArray[i][j].setImageResource(R.drawable.king_black);
                        continue;
                    case("WHITE_QUEEN"):
                        boardButtonArray[i][j].setImageResource(R.drawable.queen_white);
                        continue;
                    case("BLACK_QUEEN"):
                        boardButtonArray[i][j].setImageResource(R.drawable.queen_black);
                        continue;
                }
            }
        }

        imageViewTurn.setBackgroundColor(Color.parseColor("#999999"));

        if(board.getSideToMove() == Side.BLACK) {
            imageViewTurn.setImageResource(R.drawable.king_black);
        } else {
            imageViewTurn.setImageResource(R.drawable.king_white);
        }
        if(votes != null) textViewVoteStats.setText(votes.toString());

        if(this.playerStats != null) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View hView = navigationView.getHeaderView(0);

            TextView navUsername = (TextView) hView.findViewById(R.id.textViewUsername);
            TextView navStats = (TextView) hView.findViewById(R.id.textViewStats);
            ImageView navTeam = (ImageView) hView.findViewById(R.id.imageViewPlayerTeam);

            if(playerStats[1] .equals("0")) {
                navTeam.setImageResource(R.drawable.king_white);
            } else {
                navTeam.setImageResource(R.drawable.king_black);
            }

            navUsername.setText(this.playerStats[0]);
            navStats.setText("Wins: " + this.playerStats[2] + "\nLosses: " + this.playerStats[3]);
        }
    }

    private Map<String, Integer> to_map(JSONObject object) {
        Map<String, Integer> map = new HashMap<String, Integer>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            int value = 0;
            try {
                value = (Integer) object.get(key);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

            map.put(key, value);
        }

        return map;
    }

    private void clear_board_selections() {
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((j + i) % 2 == 0)
                    this.boardButtonArray[i][j].setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                else
                    this.boardButtonArray[i][j].setBackgroundColor(getResources().getColor(android.R.color.white));
            }
        }
    }

    private void select_legal_moves(String from) {
        if(playerStats != null) {
            if (board.getSideToMove() == Side.BLACK) {
                if (playerStats[1].equals("0")) return;
            } else {
                if (playerStats[1].equals("1")) return;
            }
        }

        MoveList legalMoves = null;

        try {
            legalMoves = MoveGenerator.getInstance().generateLegalMoves(board);
        } catch (MoveGeneratorException e) {
            e.printStackTrace();
        }

        for(String move : legalMoves.toString().split(" ")) {
            if(!move.substring(0, 2).equals(from)) continue;

            switch(move.substring(2, 4)) {
                case("a1"):
                    boardButtonArray[0][0].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("a2"):
                    boardButtonArray[0][1].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("a3"):
                    boardButtonArray[0][2].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("a4"):
                    boardButtonArray[0][3].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("a5"):
                    boardButtonArray[0][4].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("a6"):
                    boardButtonArray[0][5].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("a7"):
                    boardButtonArray[0][6].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("a8"):
                    boardButtonArray[0][7].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("b1"):
                    boardButtonArray[1][0].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("b2"):
                    boardButtonArray[1][1].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("b3"):
                    boardButtonArray[1][2].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("b4"):
                    boardButtonArray[1][3].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("b5"):
                    boardButtonArray[1][4].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("b6"):
                    boardButtonArray[1][5].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("b7"):
                    boardButtonArray[1][6].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("b8"):
                    boardButtonArray[1][7].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("c1"):
                    boardButtonArray[2][0].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("c2"):
                    boardButtonArray[2][1].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("c3"):
                    boardButtonArray[2][2].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("c4"):
                    boardButtonArray[2][3].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("c5"):
                    boardButtonArray[2][4].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("c6"):
                    boardButtonArray[2][5].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("c7"):
                    boardButtonArray[2][6].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("c8"):
                    boardButtonArray[2][7].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("d1"):
                    boardButtonArray[3][0].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("d2"):
                    boardButtonArray[3][1].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("d3"):
                    boardButtonArray[3][2].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("d4"):
                    boardButtonArray[3][3].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("d5"):
                    boardButtonArray[3][4].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("d6"):
                    boardButtonArray[3][5].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("d7"):
                    boardButtonArray[3][6].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("d8"):
                    boardButtonArray[3][7].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("e1"):
                    boardButtonArray[4][0].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("e2"):
                    boardButtonArray[4][1].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("e3"):
                    boardButtonArray[4][2].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("e4"):
                    boardButtonArray[4][3].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("e5"):
                    boardButtonArray[4][4].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("e6"):
                    boardButtonArray[4][5].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("e7"):
                    boardButtonArray[4][6].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("e8"):
                    boardButtonArray[4][7].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("f1"):
                    boardButtonArray[5][0].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("f2"):
                    boardButtonArray[5][1].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("f3"):
                    boardButtonArray[5][2].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("f4"):
                    boardButtonArray[5][3].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("f5"):
                    boardButtonArray[5][4].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("f6"):
                    boardButtonArray[5][5].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("f7"):
                    boardButtonArray[5][6].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("f8"):
                    boardButtonArray[5][7].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("g1"):
                    boardButtonArray[6][0].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("g2"):
                    boardButtonArray[6][1].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("g3"):
                    boardButtonArray[6][2].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("g4"):
                    boardButtonArray[6][3].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("g5"):
                    boardButtonArray[6][4].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("g6"):
                    boardButtonArray[6][5].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("g7"):
                    boardButtonArray[6][6].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("g8"):
                    boardButtonArray[6][7].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("h1"):
                    boardButtonArray[7][0].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("h2"):
                    boardButtonArray[7][1].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("h3"):
                    boardButtonArray[7][2].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("h4"):
                    boardButtonArray[7][3].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("h5"):
                    boardButtonArray[7][4].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("h6"):
                    boardButtonArray[7][5].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("h7"):
                    boardButtonArray[7][6].setBackgroundColor(Color.parseColor("#009900"));
                    continue;

                case("h8"):
                    boardButtonArray[7][7].setBackgroundColor(Color.parseColor("#009900"));
                    continue;
            }
        }
    }

    private boolean square_among_legal_moves(String moveTo, String moveFrom) {
        MoveList legalMoves = null;

        try {
            legalMoves = MoveGenerator.getInstance().generateLegalMoves(board);
        } catch (MoveGeneratorException e) {
            e.printStackTrace();
        }

        for(String move : legalMoves.toString().split(" ")) {
            if((moveTo + moveFrom).equals(move)) return true;
        }

        return false;
    }

    private String get_string_from_square_id(int id) {
        switch(id) {
            case(R.id.a1):
                return "a1";

            case(R.id.a2):
                return "a2";

            case(R.id.a3):
                return "a3";

            case(R.id.a4):
                return "a4";

            case(R.id.a5):
                return "a5";

            case(R.id.a6):
                return "a6";

            case(R.id.a7):
                return "a7";

            case(R.id.a8):
                return "a8";

            case(R.id.b1):
                return "b1";

            case(R.id.b2):
                return "b2";

            case(R.id.b3):
                return "b3";

            case(R.id.b4):
                return "b4";

            case(R.id.b5):
                return "b5";

            case(R.id.b6):
                return "b6";

            case(R.id.b7):
                return "b7";

            case(R.id.b8):
                return "b8";

            case(R.id.c1):
                return "c1";

            case(R.id.c2):
                return "c2";

            case(R.id.c3):
                return "c3";

            case(R.id.c4):
                return "c4";

            case(R.id.c5):
                return "c5";

            case(R.id.c6):
                return "c6";

            case(R.id.c7):
                return "c7";

            case(R.id.c8):
                return "c8";

            case(R.id.d1):
                return "d1";

            case(R.id.d2):
                return "d2";

            case(R.id.d3):
                return "d3";

            case(R.id.d4):
                return "d4";

            case(R.id.d5):
                return "d5";

            case(R.id.d6):
                return "d6";

            case(R.id.d7):
                return "d7";

            case(R.id.d8):
                return "d8";

            case(R.id.e1):
                return "e1";

            case(R.id.e2):
                return "e2";

            case(R.id.e3):
                return "e3";

            case(R.id.e4):
                return "e4";

            case(R.id.e5):
                return "e5";

            case(R.id.e6):
                return "e6";

            case(R.id.e7):
                return "e7";

            case(R.id.e8):
                return "e8";

            case(R.id.f1):
                return "f1";

            case(R.id.f2):
                return "f2";

            case(R.id.f3):
                return "f3";

            case(R.id.f4):
                return "f4";

            case(R.id.f5):
                return "f5";

            case(R.id.f6):
                return "f6";

            case(R.id.f7):
                return "f7";

            case(R.id.f8):
                return "f8";

            case(R.id.g1):
                return "g1";

            case(R.id.g2):
                return "g2";

            case(R.id.g3):
                return "g3";

            case(R.id.g4):
                return "g4";

            case(R.id.g5):
                return "g5";

            case(R.id.g6):
                return "g6";

            case(R.id.g7):
                return "g7";

            case(R.id.g8):
                return "g8";

            case(R.id.h1):
                return "h1";

            case(R.id.h2):
                return "h2";

            case(R.id.h3):
                return "h3";

            case(R.id.h4):
                return "h4";

            case(R.id.h5):
                return "h5";

            case(R.id.h6):
                return "h6";

            case(R.id.h7):
                return "h7";

            case(R.id.h8):
                return "h8";
        }

        return null;
    }

    private int get_id_from_square_string(String square) {
        switch(square) {
            case("a1"):
                return R.id.a1;

            case("a2"):
                return R.id.a2;

            case("a3"):
                return R.id.a3;

            case("a4"):
                return R.id.a4;

            case("a5"):
                return R.id.a5;

            case("a6"):
                return R.id.a6;

            case("a7"):
                return R.id.a7;

            case("a8"):
                return R.id.a8;

            case("b1"):
                return R.id.b1;

            case("b2"):
                return R.id.b2;

            case("b3"):
                return R.id.b3;

            case("b4"):
                return R.id.b4;

            case("b5"):
                return R.id.b5;

            case("b6"):
                return R.id.b6;

            case("b7"):
                return R.id.b7;

            case("b8"):
                return R.id.b8;

            case("c1"):
                return R.id.c1;

            case("c2"):
                return R.id.c2;

            case("c3"):
                return R.id.c3;

            case("c4"):
                return R.id.c4;

            case("c5"):
                return R.id.c5;

            case("c6"):
                return R.id.c6;

            case("c7"):
                return R.id.c7;

            case("c8"):
                return R.id.c8;

            case("d1"):
                return R.id.d1;

            case("d2"):
                return R.id.d2;

            case("d3"):
                return R.id.d3;

            case("d4"):
                return R.id.d4;

            case("d5"):
                return R.id.d5;

            case("d6"):
                return R.id.d6;

            case("d7"):
                return R.id.d7;

            case("d8"):
                return R.id.d8;

            case("e1"):
                return R.id.e1;

            case("e2"):
                return R.id.e2;

            case("e3"):
                return R.id.e3;

            case("e4"):
                return R.id.e4;

            case("e5"):
                return R.id.e5;

            case("e6"):
                return R.id.e6;

            case("e7"):
                return R.id.e7;

            case("e8"):
                return R.id.e8;

            case("f1"):
                return R.id.f1;

            case("f2"):
                return R.id.f2;

            case("f3"):
                return R.id.f3;

            case("f4"):
                return R.id.f4;

            case("f5"):
                return R.id.f5;

            case("f6"):
                return R.id.f6;

            case("f7"):
                return R.id.f7;

            case("f8"):
                return R.id.f8;

            case("g1"):
                return R.id.g1;

            case("g2"):
                return R.id.g2;

            case("g3"):
                return R.id.g3;

            case("g4"):
                return R.id.g4;

            case("g5"):
                return R.id.g5;

            case("g6"):
                return R.id.g6;

            case("g7"):
                return R.id.g7;

            case("g8"):
                return R.id.g8;

            case("h1"):
                return R.id.h1;

            case("h2"):
                return R.id.h2;

            case("h3"):
                return R.id.h3;

            case("h4"):
                return R.id.h4;

            case("h5"):
                return R.id.h5;

            case("h6"):
                return R.id.h6;

            case("h7"):
                return R.id.h7;

            case("h8"):
                return R.id.h8;
        }

        return Integer.parseInt(null);
    }
}
