package com.style;import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;



public class MainActivity extends Activity {

    private static final String TAG = "AIDLActivity";
    private Button btnOk;
    private Button btnCancel;
    private Button btnCallBack;

    private void Log(String str){
        Log.d(TAG,"----------" + str + "----------");
    }


    mInterface mService;
    private ServiceConnection mConnection = new ServiceConnection(){
        public void onServiceConnected(ComponentName className,
                                       IBinder service){
            Log("connect service");
            mService = mInterface.Stub.asInterface(service);
        }

        public void onServiceDisconnected(ComponentName className){
            Log("disconnect service");
            mService = null;
        }
    };



    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOk = (Button)findViewById(R.id.btn_ok);
        btnCancel = (Button)findViewById(R.id.btn_cancel);
        btnCallBack = (Button)findViewById(R.id.btn_callback);

        btnOk.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                Bundle args = new Bundle();
                Intent intent = new Intent("com.styling.service");
                intent.putExtras(args);
                bindService(intent,mConnection,Context.BIND_AUTO_CREATE);
            }
        });

        btnCancel.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                unbindService(mConnection);
            }
        });
        btnCallBack.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                try{
                    Log.i(TAG,"current Thread id = " + Thread.currentThread().getId());
                    mService.invokeTest();
                }
                catch(RemoteException e){
                    e.printStackTrace();
                }
            }
        });


    }
}