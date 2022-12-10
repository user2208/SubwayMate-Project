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
                //Callback 초기화
                //return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);

                /* 파일 업로드 */
                if (mFilePathCallback != null) {
                    //파일을 한번 오픈했으면 mFilePathCallback 를 초기화를 해줘야함
                    // -- 그렇지 않으면 다시 파일 오픈 시 열리지 않는 경우 발생
                    mFilePathCallback.onReceiveValue(null);
                    mFilePathCallback = null;
                }
                mFilePathCallback = filePathCallback;

                //권한 체크
//            if(권한 여부) {

                //권한이 있으면 처리
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");  //모든 contentType 파일 표시
//            intent.setType("image/*");  //contentType 이 image 인 파일만 표시
                startActivityForResult(intent, 0);

//            } else {

                //권한이 없으면 처리

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
        /* 파일 선택 완료 후 처리 */
        switch(requestCode) {
            case FILECHOOSER_NORMAL_REQ_CODE:
                //fileChooser 로 파일 선택 후 onActivityResult 에서 결과를 받아 처리함
                if(resultCode == Activity.RESULT_OK) {
                    //파일 선택 완료 했을 경우
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mFilePathCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                    }else{
                        mFilePathCallback.onReceiveValue(new Uri[]{data.getData()});
                    }
                    mFilePathCallback = null;
                } else {
                    //cancel 했을 경우
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