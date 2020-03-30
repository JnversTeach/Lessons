package com.example.xoxoxo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private String[][] table = {{"1","1","1"},{"1","1","1"},{"1","1","1"}};
    private TextView arr00, arr01, arr02, arr10, arr11, arr12, arr20, arr21, arr22;
    private int nhod = 1;

    public View.OnClickListener clk = new View.OnClickListener() {
        public void onClick(View v) {
            int x = -1, y = -1;
            if (v.getId() == R.id.arr00) {x = 0;y = 0;}
            if (v.getId() == R.id.arr01) {x = 0;y = 1;}
            if (v.getId() == R.id.arr02) {x = 0;y = 2;}
            if (v.getId() == R.id.arr10) {x = 1;y = 0;}
            if (v.getId() == R.id.arr11) {x = 1;y = 1;}
            if (v.getId() == R.id.arr12) {x = 1;y = 2;}
            if (v.getId() == R.id.arr20) {x = 2;y = 0;}
            if (v.getId() == R.id.arr21) {x = 2;y = 1;}
            if (v.getId() == R.id.arr22) {x = 2;y = 2;}
            if (nhod % 2 == 0) {
                ((TextView) v).setText("O");
            } else {
                ((TextView) v).setText("X");
            }
            nhod++;
            table[x][y] = (String) ((TextView) v).getText();
            if (nhod >=5 && IsWin(table[x][y])) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "You Win!", Toast.LENGTH_SHORT);
                toast.show();
            }
            if (nhod == 10) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Game Over.", Toast.LENGTH_SHORT);
                toast.show();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arr00 = (TextView) findViewById(R.id.arr00);
        arr01 = (TextView) findViewById(R.id.arr01);
        arr02 = (TextView) findViewById(R.id.arr02);
        arr10 = (TextView) findViewById(R.id.arr10);
        arr11 = (TextView) findViewById(R.id.arr11);
        arr12 = (TextView) findViewById(R.id.arr12);
        arr20 = (TextView) findViewById(R.id.arr20);
        arr21 = (TextView) findViewById(R.id.arr21);
        arr22 = (TextView) findViewById(R.id.arr22);
        arr00.setOnClickListener(clk);
        arr01.setOnClickListener(clk);
        arr02.setOnClickListener(clk);
        arr10.setOnClickListener(clk);
        arr11.setOnClickListener(clk);
        arr12.setOnClickListener(clk);
        arr20.setOnClickListener(clk);
        arr21.setOnClickListener(clk);
        arr22.setOnClickListener(clk);
    }

    boolean IsWin(String s) {
        for (int i = 0; i < 3; i++) {
            if ((table[i][0].equals(s) && table[i][1].equals(s) && table[i][2].equals(s)) ||
                    (table[0][i].equals(s)  && table[1][i].equals(s) && table[2][i].equals(s)))
                return true;
            if ((table[0][0].equals(s) && table[1][1].equals(s) && table[2][2].equals(s)) ||
                    (table[2][0].equals(s) && table[1][1].equals(s) && table[0][2].equals(s)))
                return true;
        }
        return false;
    }
}
