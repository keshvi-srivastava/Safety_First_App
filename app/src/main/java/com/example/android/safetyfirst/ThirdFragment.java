package com.example.android.safetyfirst;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.Set;
import static android.content.Context.BLUETOOTH_SERVICE;
import static android.os.Build.VERSION_CODES.KITKAT;

/**
 * Created by Srivastava on 12-10-2016.
 */
public class ThirdFragment extends Fragment {

    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate (R.layout.third_layout,container,false);
        BluetoothManager bm= (BluetoothManager) getActivity().getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter ba=bm.getAdapter();
        Set<BluetoothDevice> s=ba.getBondedDevices();
        Object a[]=s.toArray();
        String array[]=new String [a.length];
        BluetoothDevice bd=null;
        for(int i=0;i<a.length;i++)
        {
            bd=(BluetoothDevice)a[i];
            array[i]=bd.getName();
        }
        ListView list=(ListView)myView.findViewById(R.id.listView3);
        list.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, array));
        IntentFilter inf= null;
        if (android.os.Build.VERSION.SDK_INT >= KITKAT) {
            inf = new IntentFilter(bd.ACTION_PAIRING_REQUEST);
        }
        getActivity().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context,"bye",Toast.LENGTH_SHORT).show();
                Bundle b=intent.getExtras();
                Object o=b.get("data");
            }
        }, inf);
        return myView;
    }




}
