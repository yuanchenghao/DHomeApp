package com.yuemei.dejia.windows;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;

import com.yuemei.dejia.adapter.PermissionAdapter;
import com.yuemei.dejia.model.PermsissionData;

import java.util.List;
import com.yuemei.dejia.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ApplyPermissionDialog extends Dialog {
    public Button knowBtn;
    private PermissionAdapter permissionAdapter;
    private RecyclerView recyclerView;
    private List<PermsissionData> mDataList;
    public ApplyPermissionDialog(@NonNull Context context, List<PermsissionData> list) {
        super(context, R.style.CustomDialog);
        setContentView(R.layout.splash_permission_pop);
        mDataList = list;
        recyclerView = findViewById(R.id.permission_list);
        knowBtn = findViewById(R.id.permission_btn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        permissionAdapter = new PermissionAdapter(R.layout.permission_list_item, mDataList);
        recyclerView.setAdapter(permissionAdapter);
    }


    public void refershData(List<PermsissionData> list){
        if (permissionAdapter != null){
            permissionAdapter = null;
            permissionAdapter = new PermissionAdapter(R.layout.permission_list_item, list);
            recyclerView.setAdapter(permissionAdapter);
        }
    }
}
