package ml.melun.mangaview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.net.URI;

public class SettingsActivity extends AppCompatActivity {

    //다운로드 위치 설정
    //데이터 절약 모드 : 외부 이미지 로드 안함
    //
    Downloader d;
    Context context;
    ConstraintLayout s_setHomeDir, s_resetHistory, s_volumeKey, s_getSd;
    Switch s_volumeKey_switch;
    Preference p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        p = new Preference();
        d = new Downloader();
        context = this;
        s_setHomeDir = this.findViewById(R.id.setting_dir);
        s_setHomeDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (d.getStatus() == 0) {
                    Intent intent = new Intent(context, FolderSelectActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(context,"현재 다운로드가 진행중입니다. 작업이 끝난 후 변경 해 주세요",Toast.LENGTH_LONG).show();
                }
            }
        });
//        s_getSd = this.findViewById(R.id.setting_externalSd);
//        s_getSd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                p.setHomeDir("/sdcard");
//            }
//        });
        s_resetHistory = this.findViewById(R.id.setting_reset);
        s_resetHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                p.resetBookmark();
                                p.resetViewerBookmark();
                                Toast.makeText(context,"초기화 되었습니다.",Toast.LENGTH_LONG).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("최근 본 만화, 북마크 및 모든 만화 열람 기록이 사라집니다. 계속 하시겠습니까?\n(좋아요, 저장한 만화 제외)").setPositiveButton("네", dialogClickListener)
                        .setNegativeButton("아니오", dialogClickListener).show();
            }
        });
        s_volumeKey = this.findViewById(R.id.setting_volume);
        s_volumeKey_switch = this.findViewById(R.id.setting_volume_switch);
        s_volumeKey_switch.setChecked(p.getVolumeControl());
        s_volumeKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_volumeKey_switch.toggle();
            }
        });
        s_volumeKey_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                p.setVolumeControl(isChecked);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{
            if(requestCode==2){
                String tmp = data.getData().getPath();
                String tmp2 = java.net.URLDecoder.decode(tmp, "UTF-8");
                System.out.println("pppppp "+tmp2);
                p.setHomeDir(tmp2);
            }
        }catch (Exception e){}
    }
}