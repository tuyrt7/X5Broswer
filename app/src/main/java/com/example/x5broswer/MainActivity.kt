package com.example.x5broswer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.AppUtils
import com.example.x5broswer.databinding.ActivityMainBinding
import com.example.x5broswer.utils.AppUtil
import com.example.x5broswer.utils.log
import com.example.x5broswer.web.X5WebActivity
import com.permissionutil.AdapterPermissionListener
import com.permissionutil.Permission
import com.permissionutil.PermissionImpl
import com.tencent.smtt.export.external.interfaces.UrlRequest
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var mViewModel: MainViewModel
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        PermissionImpl.init(this)
            .permission(
                Permission.WRITE_EXTERNAL_STORAGE,
                Permission.CAMERA,
                Permission.ACCESS_COARSE_LOCATION,
                Permission.RECORD_AUDIO
            )
            .requestPermission(object : AdapterPermissionListener() {
                override fun onGranted() {
                    Log.d("aaaa", "onGranted: has write")
                }
            })

        binding.run {
            viewModel = mViewModel

            btnEnter.setOnClickListener {
                mViewModel.saveUrl()
                X5WebActivity.start(this@MainActivity)
                //OriginWebActivity.start(this@MainActivity)

                //pickPhoto()
            }
        }

        mViewModel.initData()

        val isSysApp = AppUtils.isAppSystem()
        val isSysApp2 = AppUtil.isSystemApp(this,intent)

        log("是否是系统签名：$isSysApp")
        log("是否是系统签名2：$isSysApp2")
    }


    private var imageUri: Uri? = null
    private fun pickPhoto() {
        // 指定拍照存储位置的方式调起相机
        // 指定拍照存储位置的方式调起相机
        val filePath: String =
            (Environment.getExternalStorageDirectory().absolutePath + File.separator + Environment.DIRECTORY_PICTURES + File.separator)
        val fileName = "IMG_" + SimpleDateFormat("yyyyMMdd_hhmmss", Locale.CHINA).format(System.currentTimeMillis()) + ".jpg"
        imageUri = Uri.fromFile(File(filePath + fileName))

        //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //startActivityForResult(intent, REQUEST_CODE);

        // 选择图片（不包括相机拍照）,则不用成功后发刷新图库的广播
        //Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        //i.addCategory(Intent.CATEGORY_OPENABLE);
        //i.setType("image/*");
        //startActivityForResult(Intent.createChooser(i, "Image Chooser"), REQUEST_CODE);

        //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //startActivityForResult(intent, REQUEST_CODE);

        // 选择图片（不包括相机拍照）,则不用成功后发刷新图库的广播
        //Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        //i.addCategory(Intent.CATEGORY_OPENABLE);
        //i.setType("image/*");
        //startActivityForResult(Intent.createChooser(i, "Image Chooser"), REQUEST_CODE);
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        val Photo = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val chooserIntent = Intent.createChooser(Photo, "Image Chooser")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf<Parcelable>(captureIntent))
        startActivityForResult(chooserIntent, 1024)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1024 && resultCode == RESULT_OK) {
            Log.d("aaaa", "onActivityResult=")
            data?.data?.let {
                Log.d("aaaa", "onActivityResult: ${it.path}")
            } ?: run {
                Log.d("aaaa", "data is null, onActivityResult: ${imageUri?.path}")
            }
        }
    }
}