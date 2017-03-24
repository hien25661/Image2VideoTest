package com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.activities;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import butterknife.Bind;
import butterknife.OnClick;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.R;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.models.ImageManager;
import com.phototovideomaker.photovideomakerwithmusic.photoslideshowwithmusic.models.Picture;

import java.util.List;

import nl.changer.polypicker.Config;
import nl.changer.polypicker.ImagePickerActivity;

public class MenuActivity extends BaseActivity {
    @Bind(R.id.btn_create_video)
    ImageView btn_create_video;
    @Bind(R.id.btn_watch_video)
    ImageView btn_watch_video;
    public static final int INTENT_REQUEST_GET_IMAGES = 111;
    ImageManager imageManager;
//    @Bind(R.id.adView)
    NativeExpressAdView adView;
    InterstitialAd interstitialAd;
//    private static final String ADMOB_AD_UNIT_ID = getRe;
//    private static final String ADMOB_APP_ID = "ca-app-pub-3940256099942544~3347511713";
    @Bind(R.id.ln_adsView)
    LinearLayout ln_adsView;
    @Override
    public int setContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
boolean isRun;
    float ratio = 250f/280f;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !isRun) {
            int height = ln_adsView.getHeight();
            int width = ln_adsView.getWidth();
            AdRequest adRequest = new AdRequest.Builder().build();
            Log.e("onWindowFocusChanged: ", width+" " + height);
            AdSize adSize = new AdSize(AdSize.FULL_WIDTH, (int)convertPixelsToDp (width*ratio,this));
            adView = new NativeExpressAdView(this);
            adView.setAdSize(adSize);
            adView.setAdUnitId(getString(R.string.ads_banner));
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    ln_adsView.removeAllViews();
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    ln_adsView.addView(adView,params);
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    super.onAdFailedToLoad(errorCode);
                }
            });
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            ln_adsView.addView(adView,params);
            adView.loadAd(adRequest);
            isRun =true;

        }

    }

    @Override
    public void initView() {
        imageManager = new ImageManager();
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.ads_interstitial));
        AdRequest adRequestInter = new AdRequest.Builder().build();

        // Load ads into Interstitial Ads
        interstitialAd.loadAd(adRequestInter);
//        refreshAd(false, true);

    }

    @Override
    public void initData() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        // Permission Granted
                        if (isOpenLibrary && i == 0) {
                            getImages();
                        }
                    } else {
                        // Permission Denied
                        Toast.makeText(MenuActivity.this, STRING_DENY[i], Toast.LENGTH_SHORT)
                                .show();
                    }
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private final static String[] MULTI_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private final static String[] STRING_DENY = new String[]{
            "Camera denied",
            "Read photo denied", "Write photo denied"
    };
    public static final int REQUEST_CODE_ASK_PERMISSIONS = 111;
    boolean isOpenLibrary;

    @OnClick(R.id.btn_create_video)
    public void openLirabry() {
        isOpenLibrary = true;
        Log.e("openLirabry", "openLirabry");
        int hasCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int hasRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int hasWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasCamera != PackageManager.PERMISSION_GRANTED || hasRead != PackageManager.PERMISSION_GRANTED || hasWrite != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(MULTI_PERMISSIONS,
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }
        getImages();
    }

    @OnClick(R.id.btn_watch_video)
    public void startWatch() {
        isOpenLibrary = false;
        int hasCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int hasRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int hasWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasCamera != PackageManager.PERMISSION_GRANTED || hasRead != PackageManager.PERMISSION_GRANTED || hasWrite != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(MULTI_PERMISSIONS,
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }
        startActivity(new Intent(this, WatchVideoActivity.class));
    }

    private void getImages() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        Config config = new Config.Builder()
                .setTabBackgroundColor(R.color.white)    // set tab background color. Default white.
                .setTabSelectionIndicatorColor(R.color.blue)
                .setCameraButtonColor(R.color.green)
                .build();
        ImagePickerActivity.setConfig(config);
        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_GET_IMAGES) {
                Parcelable[] parcelableUris = intent.getParcelableArrayExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

                if (parcelableUris == null) {
                    return;
                }

                // Java doesn't allow array casting, this is a little hack
                Uri[] uris = new Uri[parcelableUris.length];
                System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);

                if (uris != null) {
                    imageManager.clear();
                    for (int i = 0; i < uris.length; i++) {
                        Log.i("show uri", " uri: " + uris[i]);
//                        mMedia.add(uri);
                        Picture mPicture = new Picture(i, uris[i].toString());
                        imageManager.addUrl(mPicture);

                    }

//                    showMedia();
                }
                Intent mIntent = new Intent(MenuActivity.this, CreateVideoActivity.class);
                mIntent.putExtra("data", imageManager.converToString());
                startActivityForResult(mIntent, 222);

            } else if (requestCode == 222) {
                Log.e("onActivityResult: ", " " + interstitialAd.isLoaded());
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }
        }
    }

    @OnClick(R.id.btn_share)
    public void rateUs() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }

    }

    @OnClick(R.id.app1)
    public void openApp1() {
        final String appPackageName = "media.musicplayer.songs.mp3player.audio"; // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }

    }

    @OnClick(R.id.app2)
    public void openApp2() {
        final String appPackageName = "com.appgalaxy.pedometer"; // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    @OnClick(R.id.app3)
    public void openApp3() {
        final String appPackageName = "com.miniclueswebbrowser.fastestbrowser.speedybrowser.quickbrowser"; // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    @OnClick(R.id.game1)
    public void openGame1() {
        final String appPackageName = "com.minicluesgames.impossiblerun"; // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    @OnClick(R.id.game2)
    public void openGame2() {
        final String appPackageName = "com.minicluesgames.amazingjungle"; // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    @OnClick(R.id.game3)
    public void openGame3() {
        final String appPackageName = "com.minicluesgames.bounceball.graphball"; // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    /**
     * Populates a {@link NativeAppInstallAdView} object with data from a given
     * {@link NativeAppInstallAd}.
     *
     * @param nativeAppInstallAd the object containing the ad's assets
     * @param adView             the view to be populated
     */
    private void populateAppInstallAdView(NativeAppInstallAd nativeAppInstallAd,
                                          NativeAppInstallAdView adView) {
        adView.setHeadlineView(adView.findViewById(R.id.appinstall_headline));
        adView.setImageView(adView.findViewById(R.id.appinstall_image));
        adView.setBodyView(adView.findViewById(R.id.appinstall_body));
        adView.setCallToActionView(adView.findViewById(R.id.appinstall_call_to_action));
        adView.setIconView(adView.findViewById(R.id.appinstall_app_icon));
        adView.setPriceView(adView.findViewById(R.id.appinstall_price));
        adView.setStarRatingView(adView.findViewById(R.id.appinstall_stars));
        adView.setStoreView(adView.findViewById(R.id.appinstall_store));

        // Some assets are guaranteed to be in every NativeAppInstallAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAppInstallAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAppInstallAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAppInstallAd.getCallToAction());
        ((ImageView) adView.getIconView()).setImageDrawable(nativeAppInstallAd.getIcon()
                .getDrawable());

        List<NativeAd.Image> images = nativeAppInstallAd.getImages();

        if (images.size() > 0) {
            ((ImageView) adView.getImageView()).setImageDrawable(images.get(0).getDrawable());
        }

        // Some aren't guaranteed, however, and should be checked.
        if (nativeAppInstallAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAppInstallAd.getPrice());
        }

        if (nativeAppInstallAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAppInstallAd.getStore());
        }

        if (nativeAppInstallAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAppInstallAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAppInstallAd);
    }

    /**
     * Populates a {@link NativeContentAdView} object with data from a given
     * {@link NativeContentAd}.
     *
     * @param nativeContentAd the object containing the ad's assets
     * @param adView          the view to be populated
     */
    private void populateContentAdView(NativeContentAd nativeContentAd,
                                       NativeContentAdView adView) {
        adView.setHeadlineView(adView.findViewById(R.id.contentad_headline));
        adView.setImageView(adView.findViewById(R.id.contentad_image));
        adView.setBodyView(adView.findViewById(R.id.contentad_body));
        adView.setCallToActionView(adView.findViewById(R.id.contentad_call_to_action));
        adView.setLogoView(adView.findViewById(R.id.contentad_logo));
        adView.setAdvertiserView(adView.findViewById(R.id.contentad_advertiser));

        // Some assets are guaranteed to be in every NativeContentAd.
        ((TextView) adView.getHeadlineView()).setText(nativeContentAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeContentAd.getBody());
        ((TextView) adView.getCallToActionView()).setText(nativeContentAd.getCallToAction());
        ((TextView) adView.getAdvertiserView()).setText(nativeContentAd.getAdvertiser());

        List<NativeAd.Image> images = nativeContentAd.getImages();

        if (images.size() > 0) {
            ((ImageView) adView.getImageView()).setImageDrawable(images.get(0).getDrawable());
        }

        // Some aren't guaranteed, however, and should be checked.
        NativeAd.Image logoImage = nativeContentAd.getLogo();

        if (logoImage == null) {
            adView.getLogoView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getLogoView()).setImageDrawable(logoImage.getDrawable());
            adView.getLogoView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeContentAd);
    }

    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     *
     * @param requestAppInstallAds indicates whether app install ads should be requested
     * @param requestContentAds    indicates whether content ads should be requested
     */
    private void refreshAd(boolean requestAppInstallAds, boolean requestContentAds) {
        if (!requestAppInstallAds && !requestContentAds) {
            Toast.makeText(this, "At least one ad format must be checked to request an ad.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        AdLoader.Builder builder = new AdLoader.Builder(this, getResources().getString(R.string.ads_banner));

        if (requestAppInstallAds) {
            builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
                @Override
                public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
//                    FrameLayout frameLayout =
//                            (FrameLayout) findViewById(R.id.fl_adplaceholder);
//                    NativeAppInstallAdView adView = (NativeAppInstallAdView) getLayoutInflater()
//                            .inflate(R.layout.ad_app_install, null);
//                    populateAppInstallAdView(ad, adView);
//                    frameLayout.removeAllViews();
//                    frameLayout.addView(adView);
                }
            });
        }

        if (requestContentAds) {
            builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
                @Override
                public void onContentAdLoaded(NativeContentAd ad) {
//                    FrameLayout frameLayout =
//                            (FrameLayout) findViewById(R.id.fl_adplaceholder);
//                    NativeContentAdView adView = (NativeContentAdView) getLayoutInflater()
//                            .inflate(R.layout.ad_content, null);
//                    populateContentAdView(ad, adView);
//                    frameLayout.removeAllViews();
//                    frameLayout.addView(adView);
                }
            });
        }

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(MenuActivity.this, "Failed to load native ad: "
                        + errorCode, Toast.LENGTH_SHORT).show();
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }
}
