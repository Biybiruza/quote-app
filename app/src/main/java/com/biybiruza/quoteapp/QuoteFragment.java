package com.biybiruza.quoteapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.biybiruza.quoteapp.data.Quote;
import com.biybiruza.quoteapp.data.QuoteData;
import com.biybiruza.quoteapp.databinding.FragmentQuoteBinding;
import com.biybiruza.quoteapp.networking.ApiClient;
import com.biybiruza.quoteapp.networking.ApiService;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuoteFragment extends Fragment {

    public QuoteFragment() {
        super(R.layout.fragment_quote);
    }

    private FragmentQuoteBinding binding;
    private List<Quote> list;
    QuoteAdapter adapter;
    MediaPlayer mediaPlayer;

    private static final int STORAGE_PERMISSION_CODE = 23;
    private static final String TAG = "tag";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQuoteBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        Quote.quoteList().add(new Quote(
//                "",
//                " ",
//                ""
//        ));
//        list = Quote.quoteList();
//        adapter = new QuoteAdapter(list);
//        binding.viewpager.setAdapter(adapter);

//        Random random = new Random();

//        setViewpager(random);

        if (checkStoragePermissions()) {
            Quote.quoteList().add(new Quote(
                    "",
                    " ",
                    ""
            ));
            list = Quote.quoteList();
            adapter = new QuoteAdapter(list);
            binding.viewpager.setAdapter(adapter);
            Random random = new Random();
            setViewpager(random);
        } else {
            requestForStoragePermissions();
        }

        Toast.makeText(requireActivity(), "current item: "+binding.viewpager.getCurrentItem(), Toast.LENGTH_SHORT).show();

        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.camera_shutter_click);

//        verifystoragepermissions(requireActivity());
        binding.ibScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    takeScreenshot(mediaPlayer);

            }
        });

        binding.ibShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareScreenshot();
            }
        });

    }

    public boolean checkStoragePermissions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11 (R) or above
            return Environment.isExternalStorageManager();
        }else {
            //Below android 11
            int write = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

            return read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestForStoragePermissions() {
        //Android is 11 (R) or above
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            try {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
                intent.setData(uri);

                storageActivityResultLauncher.launch(intent);
            }catch (Exception e){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                storageActivityResultLauncher.launch(intent);
            }
        }else{
            //Below android 11
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    STORAGE_PERMISSION_CODE
            );
        }

    }

    private ActivityResultLauncher<Intent> storageActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>(){

                        @Override
                        public void onActivityResult(ActivityResult o) {
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                                //Android is 11 (R) or above
                                if(Environment.isExternalStorageManager()){
                                    //Manage External Storage Permissions Granted
                                    Log.d(TAG, "onActivityResult: Manage External Storage Permissions Granted");
                                    Quote.quoteList().add(new Quote(
                                            "",
                                            " ",
                                            ""
                                    ));
                                    list = Quote.quoteList();
                                    adapter = new QuoteAdapter(list);
                                    binding.viewpager.setAdapter(adapter);
                                    setViewpager(new Random());
                                }else{
                                    Toast.makeText(requireContext(), "Storage Permissions Denied", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                //Below android 11
                                Quote.quoteList().add(new Quote(
                                        "",
                                        " ",
                                        ""
                                ));
                                list = Quote.quoteList();
                                adapter = new QuoteAdapter(list);
                                binding.viewpager.setAdapter(adapter);
                            }
                        }
                    });

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0){
                boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if(read && write){
                    Toast.makeText(requireContext(), "Storage Permissions Granted", Toast.LENGTH_SHORT).show();
                    Quote.quoteList().add(new Quote(
                            "",
                            " ",
                            ""
                    ));
                    list = Quote.quoteList();
                    adapter = new QuoteAdapter(list);
                    binding.viewpager.setAdapter(adapter);
                    setViewpager(new Random());
                }else{
                    Toast.makeText(requireContext(), "Storage Permissions Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void requestPermissionForReadExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission_group.STORAGE)) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(requireContext());
            dialog.setTitle("Ruxat beriw");
            dialog.setMessage("screen shot qiliw ushin ruxsat beriwin'iz kerek")
                    .setPositiveButton("Ruxsat beriw", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(
                                    requireActivity(),
                                    new String[]{Manifest.permission_group.STORAGE},
                                    2);
                        }
                    })
                    .setNegativeButton("Ruxsat bermew", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            dialog.show();

        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission_group.STORAGE}, 1);
        }
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takeScreenshot(mediaPlayer);
        } else if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                takeScreenshot(mediaPlayer);
            } else {
                Intent intent = new Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", requireContext().getPackageName(), null));

                requireContext().startActivity(intent);
            }
        }
    }*/

    /*
    1) https://medium.com/@kezzieleo/manage-external-storage-permission-android-studio-java-9c3554cf79a7
    2) https://youtube.com/watch?v=7ctoTyG0nu4
    3) https://developer.android.com/training/permissions/requesting#java
    4) https://www.youtube.com/watch?v=6cxTVPFYaTw
    5) how to check permission white external storage in android 13 java
    */

    private void shareScreenshot(){
        Date now = new Date();

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpg");

        View v1 = binding.fragment.getViewById(R.id.viewpager);
        v1.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);
        v1.destroyDrawingCache();

        String dirPath = Environment.getExternalStorageDirectory().toString();
        String path = dirPath + "/" + "Screenshots" + "-" + now.getTime() + ".jpg";
        Log.d("test", path);

        File imgFile = new File(path);
        try {
            FileOutputStream outputStream = new FileOutputStream(imgFile);

            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
        } catch (Throwable e){
            e.printStackTrace();
        }


        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(dirPath+"Screenshots" +"-" + now.getTime() + ".jpg"));
        startActivity(Intent.createChooser(share, "Share Image"));
    }

    private void takeScreenshot(MediaPlayer mediaPlayer) {
        Date now = new Date();

        CharSequence date = android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            View v1 = binding.fragment.getViewById(R.id.viewpager);
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            v1.destroyDrawingCache();

            String dirPath = Environment.getExternalStorageDirectory().toString();
            String path = dirPath + "/" + "Screenshots" + "-" + now.getTime() + ".jpg";
            Log.d("test", path);

            File imgFile = new File(path);


            FileOutputStream outputStream = new FileOutputStream(imgFile);

            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);

            outputStream.flush();
            outputStream.close();
            Toast.makeText(requireContext(), "Saved: "+Environment.getExternalStorageDirectory().toString()+"/" + "Screenshots" + "-" + now.getTime() + ".jpg", Toast.LENGTH_SHORT).show();

            mediaPlayer.start();


        } catch (Throwable e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("TAG_YE", "takeScreenshot: "+e.getMessage());
        }

    }

    public void setViewpager(Random random) {
        binding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                setData(position, random);

                adapter.notifyItemChanged(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    public void setData(int position, Random random) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        apiService.getQuotes().enqueue(new Callback<QuoteData>() {
            @Override
            public void onResponse(Call<QuoteData> call, Response<QuoteData> response) {
                if (response.isSuccessful()){
                    binding.progress.setVisibility(View.GONE);

                    Log.d("logd",response.body().getQuote());
                    Log.d("log","https://picsum.photos/id/"+ random.nextInt(60)+"/1080/2340?grayscale&blur=2");

                    Quote.quoteList().add(new Quote(
                            "https://picsum.photos/id/"+ random.nextInt(60)+"/1080/2340?grayscale&blur=2",
                            response.body().getQuote(),
                            response.body().getAuthor()
                    ));

                    Log.d("log", "Quote.quoteList(): "+ Quote.quoteList().toString());
                }
            }

            @Override
            public void onFailure(Call<QuoteData> call, Throwable t) {
                Toast.makeText(requireContext(), "onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}