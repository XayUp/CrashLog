package com.xayup.debug;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.xayup.debug.Debug;

 
public class CrashLog {
     
    public static boolean CrashLog(Activity context, Runnable set_default_content_view)  {
        Debug.StackTrace stackTrace = new Debug.StackTrace(context);
        if (stackTrace.getStackTrace() != null) {
            context.setContentView(R.layout.crash);

            TextView textLog = context.findViewById(R.id.logText);
            textLog.setText(stackTrace.getStackTrace());

            Button copyToClipboard = context.findViewById(R.id.copyLog);
            Button finishApp = context.findViewById(R.id.exitcrash);
            Button restartApp = context.findViewById(R.id.restartApp);

            copyToClipboard.setOnClickListener((v) -> {
                ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(
                        new ClipData(
                                "MultiPad error log",
                                new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                                new ClipData.Item(stackTrace.getStackTrace())
                        )
                );
                Toast.makeText(context, R.string.crash_button_copy_message, Toast.LENGTH_SHORT).show();
            });
            finishApp.setOnClickListener((v) -> context.finishAffinity());
            restartApp.setOnClickListener((v) -> context.recreate());
            return true;
        } else {
            new Debug(context) {
                @Override
                public void afterCrash(){
                    super.afterCrash();
                    context.runOnUiThread(()-> Toast.makeText(
                            context,
                            context.getString(R.string.crash_message),
                            Toast.LENGTH_SHORT).show());
                }
            };
            set_default_content_view.run();
            return false;
        }
    }
}
