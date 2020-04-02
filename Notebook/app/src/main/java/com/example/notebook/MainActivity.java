package com.example.notebook;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final static String FILENAME = "sample.txt"; // имя файла
    private EditText notePad;
    private Button savebtn, openbtn, copybtn, pastebtn, ctrlzbtn, ctrlybtn,lagetxtbtn, smalltxtbtn;
    private ArrayList<String> ctrlz = new ArrayList<String>();
    private ArrayList<String> ctrly = new ArrayList<String>();
    private boolean needToSave=true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       setContentView(R.layout.activity_main);

        notePad = (EditText) findViewById(R.id.editText);
        savebtn = (Button) findViewById(R.id.button1);
        openbtn = (Button) findViewById(R.id.button2);
        copybtn = (Button) findViewById(R.id.button3);
        pastebtn = (Button) findViewById(R.id.button4);
        ctrlzbtn = (Button) findViewById(R.id.button5);
        ctrlybtn = (Button) findViewById(R.id.button6);
        lagetxtbtn = (Button) findViewById(R.id.button7);
        smalltxtbtn = (Button) findViewById(R.id.button8);
        savebtn.setOnClickListener(clcksavebtn);
        openbtn.setOnClickListener(clckopenbtn);
        copybtn.setOnClickListener(clckcopybtn);
        pastebtn.setOnClickListener(clckpastebtn);
        ctrlzbtn.setOnClickListener(clckctrlzbtn);
        ctrlybtn.setOnClickListener(clckctrlybtn);
        lagetxtbtn.setOnClickListener(clcklagetxtbtn);
        smalltxtbtn.setOnClickListener(clcksmalltxtbtn);

        ctrlz.add(" ");
        ctrly.add(" ");

        notePad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (needToSave) {
                    ctrlz.add(notePad.getText().toString());
                }
                else {
                    needToSave=true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private View.OnClickListener clcksavebtn = new View.OnClickListener() {
        public void onClick(View v) {
            saveFile(FILENAME);
        }
    };
    private View.OnClickListener clckopenbtn = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void onClick(View v) {
            openFile(FILENAME);
        }
    };
    private View.OnClickListener clckcopybtn = new View.OnClickListener() {
        public void onClick(View v) {
           copy();
        }
    };
    private View.OnClickListener clckpastebtn = new View.OnClickListener() {
        public void onClick(View v) {
            paste();
            }
    };
    private View.OnClickListener clckctrlzbtn = new View.OnClickListener() {
        public void onClick(View v) {
            back();
        }
    };
    private View.OnClickListener clckctrlybtn = new View.OnClickListener() {
        public void onClick(View v) {
            ahead();
            }
    };
    private View.OnClickListener clcklagetxtbtn = new View.OnClickListener() {
        public void onClick(View v) {
            notePad.setTextSize(TypedValue.COMPLEX_UNIT_SP ,notePad.getTextSize()*1f);

        }
    };
    private View.OnClickListener clcksmalltxtbtn = new View.OnClickListener() {
        public void onClick(View v) {
            notePad.setTextSize(TypedValue.COMPLEX_UNIT_SP , notePad.getTextSize()*0.1f);
        }
    };

    // Метод для открытия файла
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void openFile(String fileName) { //open text file
        String openf = "";
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    openFileInput(fileName)));
            // читаем содержимое
            while ((openf = reader.readLine()) != null) {
                notePad.setText(openf);
            }
        } catch (IOException ex) {
        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Метод для сохранения файла
    private void saveFile(String fileName) {
        String text = notePad.getText().toString();
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(fileName, MODE_PRIVATE)));
            writer.write(text);
            writer.close();
            Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    // Метод для копирования
    private void copy() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", notePad.getText().toString());
        clipboard.setPrimaryClip(clip);
    }
    // Метод для вставки
    private void paste() {
        ClipboardManager cb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (cb.hasPrimaryClip()) {
            ClipData cd = cb.getPrimaryClip();
            int index = notePad.getSelectionStart() >= 0 ? notePad.getSelectionStart() : 0;
            StringBuilder sBuilder = new StringBuilder(cd.getItemAt(0).getText().toString()); sBuilder.insert(index, cd.getItemAt(0).getText().toString());
            notePad.setText(sBuilder.toString());
            notePad.setSelection(index + cd.getItemAt(0).getText().toString().length());
        }
    }

    // Метод отмена
    private void back() {
        needToSave = false;
        if (ctrlz.size() > 0) {
            ctrly.add(notePad.getText().toString());
            notePad.setText(ctrlz.get(ctrlz.size() - 1));
            ctrlz.remove(ctrlz.size() - 1);
        }
    }
    // Метод шаг вперед
   private void ahead() {
       if (ctrly.size() > 0) {
           notePad.setText(ctrly.get(ctrly.size() - 1));
           ctrly.remove(ctrly.size() - 1);
           needToSave = true;
       }
   }
}
