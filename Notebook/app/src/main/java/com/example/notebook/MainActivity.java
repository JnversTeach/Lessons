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
    private Button saveBtn, openBtn, copyBtn, pasteBtn, ctrlzBtn, ctrlyBtn, lageTxtBtn, smallTxtBtn;
    private ArrayList<String> ctrlz = new ArrayList<String>();
    private ArrayList<String> ctrly = new ArrayList<String>();
    private boolean needToSave=true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        notePad = (EditText) findViewById(R.id.editText);
        saveBtn = (Button) findViewById(R.id.button1);
        openBtn = (Button) findViewById(R.id.button2);
        copyBtn = (Button) findViewById(R.id.button3);
        pasteBtn = (Button) findViewById(R.id.button4);
        ctrlzBtn = (Button) findViewById(R.id.button5);
        ctrlyBtn = (Button) findViewById(R.id.button6);
        lageTxtBtn = (Button) findViewById(R.id.button7);
        smallTxtBtn = (Button) findViewById(R.id.button8);
        saveBtn.setOnClickListener(clckSaveBtn);
        openBtn.setOnClickListener(clckOpenBtn);
        copyBtn.setOnClickListener(clckCopyBtn);
        pasteBtn.setOnClickListener(clckPasteBtn);
        ctrlzBtn.setOnClickListener(clckCtrlzBtn);
        ctrlyBtn.setOnClickListener(clckCtrlyBtn);
        lageTxtBtn.setOnClickListener(clckLageTxtBtn);
        smallTxtBtn.setOnClickListener(clckSmallTxtBtn);

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

    private View.OnClickListener clckSaveBtn = new View.OnClickListener() {
        public void onClick(View v) {
            saveFile(FILENAME);
        }
    };
    private View.OnClickListener clckOpenBtn = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void onClick(View v) {
            openFile(FILENAME);
        }
    };
    private View.OnClickListener clckCopyBtn = new View.OnClickListener() {
        public void onClick(View v) {
           copy();
        }
    };
    private View.OnClickListener clckPasteBtn = new View.OnClickListener() {
        public void onClick(View v) {
            paste();
            }
    };
    private View.OnClickListener clckCtrlzBtn = new View.OnClickListener() {
        public void onClick(View v) {
            back();
        }
    };
    private View.OnClickListener clckCtrlyBtn = new View.OnClickListener() {
        public void onClick(View v) {
            ahead();
            }
    };
    private View.OnClickListener clckLageTxtBtn = new View.OnClickListener() {
        public void onClick(View v) {
            notePad.setTextSize(TypedValue.COMPLEX_UNIT_SP ,notePad.getTextSize()*1f);

        }
    };
    private View.OnClickListener clckSmallTxtBtn = new View.OnClickListener() {
        public void onClick(View v) {
            notePad.setTextSize(TypedValue.COMPLEX_UNIT_SP , notePad.getTextSize()*0.1f);
        }
    };

    // Метод для открытия файла
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void openFile(String fileName) { //open text file
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    openFileInput(fileName)));
            String openf = "";
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
        String stringExtr = notePad.getText().toString();
        int startIndex = notePad.getSelectionStart();
        int endIndex = notePad.getSelectionEnd();
        stringExtr = stringExtr.substring(startIndex, endIndex);
        ClipData clip;
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);;
        if(startIndex != endIndex) {
            clip = ClipData.newPlainText("", stringExtr);
            clipboard.setPrimaryClip(clip);
        }
        else {
            clip = ClipData.newPlainText("", notePad.getText().toString());
            clipboard.setPrimaryClip(clip);
        }
    }
    // Метод для вставки
    private void paste() {
        ClipboardManager cb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (cb.hasPrimaryClip()) {
            String strPast1 = notePad.getText().toString();
            String strPast2 = notePad.getText().toString();
            int stInd = notePad.getSelectionStart();
            int endInd = notePad.getSelectionEnd();
            strPast1 = strPast1.substring(0, stInd);
            strPast2 = strPast2.substring(endInd, strPast2.length());
            ClipData cd = cb.getPrimaryClip();
            //String stringPast = cd.getItemAt(0).getText().toString();
            StringBuilder sBuilder = new StringBuilder(strPast1);
            sBuilder.insert(stInd, cd.getItemAt(0).getText().toString());
            notePad.setText(sBuilder.toString()+ strPast2);
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
