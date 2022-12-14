package com.example.subwaymateui.ui.community;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.subwaymateui.R;
import com.example.subwaymateui.databinding.FragmentCommunityBinding;

public class CommunityFragment extends Fragment {

    private FragmentCommunityBinding binding;
    private WebView fragment_community_webView;
    private String url = "https://eternal99.github.io/SubwayMateBlog/";
    public static final int FILECHOOSER_NORMAL_REQ_CODE = 0;
    private ValueCallback mFilePathCallback;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CommunityViewModel dashboardViewModel =
                new ViewModelProvider(this).get(CommunityViewModel.class);

        binding = FragmentCommunityBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        View view = inflater.inflate(R.layout.fragment_community, container, false);

        fragment_community_webView = (WebView) view.findViewById(R.id.fragment_community_webView);
        WebSettings webSettings = fragment_community_webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_USER);
        fragment_community_webView.setWebViewClient(new WebViewClient());
        fragment_community_webView.loadUrl(url);

        fragment_community_webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
//                Log.d(TAG, "***** onShowFileChooser()");
                //Callback ?????????
                //return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);

                /* ?????? ????????? */
                if (mFilePathCallback != null) {
                    //????????? ?????? ??????????????? mFilePathCallback ??? ???????????? ????????????
                    // -- ????????? ????????? ?????? ?????? ?????? ??? ????????? ?????? ?????? ??????
                    mFilePathCallback.onReceiveValue(null);
                    mFilePathCallback = null;
                }
                mFilePathCallback = filePathCallback;

                //?????? ??????
//            if(?????? ??????) {

                //????????? ????????? ??????
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");  //?????? contentType ?????? ??????
//            intent.setType("image/*");  //contentType ??? image ??? ????????? ??????
                startActivityForResult(intent, 0);

//            } else {

                //????????? ????????? ??????

//            }

                return true;
            }
        });

        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d(TAG, "***** onActivityResult() - requestCode : "+requestCode);
//        Log.d(TAG, "***** onActivityResult() - resultCode : "+resultCode);
//        Log.d(TAG, "***** onActivityResult() - data : "+data);
        /* ?????? ?????? ?????? ??? ?????? */
        switch(requestCode) {
            case FILECHOOSER_NORMAL_REQ_CODE:
                //fileChooser ??? ?????? ?????? ??? onActivityResult ?????? ????????? ?????? ?????????
                if(resultCode == Activity.RESULT_OK) {
                    //?????? ?????? ?????? ?????? ??????
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mFilePathCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                    }else{
                        mFilePathCallback.onReceiveValue(new Uri[]{data.getData()});
                    }
                    mFilePathCallback = null;
                } else {
                    //cancel ?????? ??????
                    if(mFilePathCallback != null) {
                        mFilePathCallback.onReceiveValue(null);
                        mFilePathCallback = null;
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}