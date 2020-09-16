package com.example.android.safetyfirst;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import com.example.android.safetyfirst.Finding;
import com.example.android.safetyfirst.KnightsDBAdapter;


public class SecondFragment extends Fragment{
    View myView;
    private String data;
    private TextView mTextViewPno2, mTextViewPno1, mTextViewPno3;
    private EditText mEditTextPno1, mEditTextPno2, mEditTextPno3;
    int i = 0;
    private static final SecondFragment second = new SecondFragment();
    public static SecondFragment getInstance() {return second;}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.second_layout, container, false);
        final KnightsDBAdapter a = new KnightsDBAdapter(getActivity());
        mEditTextPno1 = (EditText) myView.findViewById(R.id.editText);
        mEditTextPno2 = (EditText) myView.findViewById(R.id.editText2);
        mEditTextPno3 = (EditText) myView.findViewById(R.id.editText3);
        mTextViewPno1 = (TextView) myView.findViewById(R.id.textViewPhoneNumber1);
        mTextViewPno2 = (TextView) myView.findViewById(R.id.textViewPhoneNumber2);
        mTextViewPno3 = (TextView) myView.findViewById(R.id.textViewPhoneNumber3);
        Button save = (Button) myView.findViewById(R.id.button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditTextPno1.getText().toString().length() != 0) {
                    boolean y = a.insertFindings(0, 0, mEditTextPno1.getText().toString(), " ");
                    mEditTextPno1.setText("");
                }
                if (mEditTextPno2.getText().toString().length() != 0) {
                    boolean y = a.insertFindings(0, 0, mEditTextPno2.getText().toString(), " ");
                    mEditTextPno2.setText("");
                }
                if (mEditTextPno3.getText().toString().length() != 0) {
                    boolean y = a.insertFindings(0, 0, mEditTextPno3.getText().toString(), " ");
                    mEditTextPno3.setText("");
                }
                KnightsDBAdapter dbAdapter = new KnightsDBAdapter(getActivity());
                ArrayList<Finding> findings = dbAdapter.getFindings();
                for (int i = 1; i <= findings.size(); i++) {
                    Finding cn = findings.get(findings.size() - i);
                    if (i == 1) {
                        mTextViewPno1.setText(cn.getAddress());
                        data = mTextViewPno1.toString();
                    }
                    if (i == 2) {
                        mTextViewPno2.setText(cn.getAddress());
                        data = mTextViewPno2.toString();
                    }
                    if (i == 3) {
                        mTextViewPno3.setText(cn.getAddress());
                        data = mTextViewPno3.toString();
                    }
                }
            }


        });
        KnightsDBAdapter dbAdapter = new KnightsDBAdapter(getActivity());
        ArrayList<Finding> findings = dbAdapter.getFindings();
        for (int i = 1; i <= findings.size(); i++) {
            Finding cn = findings.get(findings.size() - i);
            if (i == 1)
                mTextViewPno1.setText(cn.getAddress());
            if (i == 2)
                mTextViewPno2.setText(cn.getAddress());
            if (i == 3)
                mTextViewPno3.setText(cn.getAddress());
        }
        mTextViewPno1.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                registerForContextMenu(mTextViewPno1);
                i = 1;
                return false;
            }
        });
        mTextViewPno2.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                registerForContextMenu(mTextViewPno2);
                i = 2;
                return false;
            }

        });
        mTextViewPno3.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                registerForContextMenu(mTextViewPno3);
                i = 3;
                return false;
            }

        });
        getGlobalVariable();
        return myView;
    }

    public String getGlobalVariable()
    {
        return data;
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String[] menuItems = getResources().getStringArray(R.array.menu);
        for (int i = 0; i < menuItems.length; i++) {
            menu.add(Menu.NONE, i, i, menuItems[i]);
        }
    }


}



