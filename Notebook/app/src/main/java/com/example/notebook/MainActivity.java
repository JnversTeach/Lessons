package com.example.notebook;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final static String FILENAME = "sample.txt"; // имя файла
    private EditText mEditText;
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8;
    private ArrayList<String> ctrlz = new ArrayList<String>();
    private ArrayList<String> ctrly = new ArrayList<String>();
    private boolean needToSave=true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.editText);
        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        btn4 = (Button) findViewById(R.id.button4);
        btn5 = (Button) findViewById(R.id.button5);
        btn6 = (Button) findViewById(R.id.button6);
        btn7 = (Button) findViewById(R.id.button7);
        btn8 = (Button) findViewById(R.id.button8);
        btn1.setOnClickListener(clckbtn1);
        btn2.setOnClickListener(clckbtn2);
        btn3.setOnClickListener(clckbtn3);
        btn4.setOnClickListener(clckbtn4);
        btn5.setOnClickListener(clckbtn5);
        btn6.setOnClickListener(clckbtn6);
        btn7.setOnClickListener(clckbtn7);
        btn8.setOnClickListener(clckbtn8);

        ctrlz.add(" ");
        ctrly.add(" ");

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (needToSave) {
                    ctrlz.add(mEditText.getText().toString());
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

    private View.OnClickListener clckbtn1 = new View.OnClickListener() {
        public void onClick(View v) {
            saveFile(FILENAME);
        }
    };
    private View.OnClickListener clckbtn2 = new View.OnClickListener() {
        public void onClick(View v) {
            openFile(FILENAME);
        }
    };
    private View.OnClickListener clckbtn3 = new View.OnClickListener() {
        public void onClick(View v) {
           copy();
        }
    };
    private View.OnClickListener clckbtn4 = new View.OnClickListener() {
        public void onClick(View v) {
            paste();
            }
    };
    private View.OnClickListener clckbtn5 = new View.OnClickListener() {
        public void onClick(View v) {
            back();
        }
    };
    private View.OnClickListener clckbtn6 = new View.OnClickListener() {
        public void onClick(View v) {
            ahead();
            }
    };
    private View.OnClickListener clckbtn7 = new View.OnClickListener() {
        public void onClick(View v) {
            mEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP ,mEditText.getTextSize()*1f);

        }
    };
    private View.OnClickListener clckbtn8 = new View.OnClickListener() {
        public void onClick(View v) {
            mEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP , mEditText.getTextSize()*0.1f);
        }
    };

    // Метод для открытия файла
    private void openFile(String fileName) {
        FileInputStream fin = null;
        try {
            fin = openFileInput(fileName);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String(bytes);
            mEditText.setText(text);

        } catch (IOException ex) {
        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{

        try{
            if(fin!=null)
                fin.close();
        }
        catch(IOException ex){

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    }
    // Метод для сохранения файла
    private void saveFile(String fileName) {
        FileOutputStream fos = null;
        try {
            String text = mEditText.getText().toString();

            fos = openFileOutput(fileName, MODE_PRIVATE);
            fos.write(text.getBytes());
            Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{
            try{
                if(fos!=null)
                    fos.close();
            }
            catch(IOException ex){

                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
    }
}
    // Метод для копирования
    private void copy() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", mEditText.getText().toString());
        clipboard.setPrimaryClip(clip);
    }
    // Метод для вставки
    private void paste() {
        ClipboardManager cb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (cb.hasPrimaryClip()) {
            ClipData cd = cb.getPrimaryClip();
            mEditText.setText(cd.getItemAt(0).getText().toString());
        }
    }
    // Метод отмена
    private void back() {
        needToSave = false;
        if (ctrlz.size() > 0) {
            ctrly.add(mEditText.getText().toString());
            mEditText.setText(ctrlz.get(ctrlz.size() - 1));
            ctrlz.remove(ctrlz.size() - 1);
        }
    }
    // Метод шаг вперед
   private void ahead() {
       if (ctrly.size() > 0) {
           mEditText.setText(ctrly.get(ctrly.size() - 1));
           ctrly.remove(ctrly.size() - 1);
           needToSave = true;
       }
   }
}
