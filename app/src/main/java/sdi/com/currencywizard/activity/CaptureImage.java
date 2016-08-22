package sdi.com.currencywizard.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sdi.com.currencywizard.R;
import sdi.com.currencywizard.adapter.CountryCurrencyListAdapter;
import sdi.com.currencywizard.model.Country;
import sdi.com.currencywizard.model.CurrencyApplication;
import sdi.com.currencywizard.model.JsonResponse;
import sdi.com.currencywizard.model.ScanResultList;

@SuppressWarnings("deprecation")
public class CaptureImage extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback, Callback,
        OnClickListener, Comparator<Country> {

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private Button captureImage;
    private int cameraId;
    private int rotation;
    String pa="";

    private static final String CLOUD_VISION_API_KEY = "AIzaSyAJH5fDgCeVx62U4Ldd0FO5eUQ7zeZnwT8";
    private static final String TAG = CaptureImage.class.getSimpleName();

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    private Context context;

    ImageView camera_cur_conversion,camera_back;
    TextView camera_from_code,camera_to_code;

    //The "x" and "y" position of the "from_flag_name_img" on screen.
    Point p;
    //The "x" and "y" position of the "to_flag_name_img" on screen.
    Point p1;

    private ListView countryListView;

    private CountryCurrencyListAdapter adapter;

    private List<Country> allCountriesList;

    private List<Country> selectedCountriesList;

    String c_from_code="USD",c_to_code="EUR",to_conversion_symbol="€",from_conversion_symbol="$",
            from_dollar_name_con="US Dollar",to_dollar_name_con="Euro";

    private static final int REQUEST_CAMERA =2909;

    String response_value;

    private List<JsonResponse> responseList;

    public String camera_currency="1";

    private List<Country> getResponseCountriesList;

    public List<ScanResultList> scanResultsList;

    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_image);

        context = this;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted.

            requestCameraPermission();

        } else {

            // Camera permissions is already available, show the camera preview.
            Log.i(TAG,"CAMERA permission has already been granted. Displaying camera preview.");
            // camera surface view created

            takePicture();
        }

        //checkCameraPermission();

        camera_cur_conversion=(ImageView) findViewById(R.id.camera_cur_conversion);
        camera_back=(ImageView) findViewById(R.id.camera_back);
        camera_from_code=(TextView) findViewById(R.id.camera_from_code);
        camera_to_code=(TextView) findViewById(R.id.camera_to_code);


        camera_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent activityChangeIntent = new Intent(CaptureImage.this, CurrencyWizard.class);
                startActivity(activityChangeIntent);
                finish();
            }
        });

        camera_from_code.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(camera_from_code.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                //Open popup window
                if (p != null)
                    fromShowPopup(CaptureImage.this, p);

            }
        });

        camera_to_code.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(camera_to_code.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                //Open popup window
                if (p != null)
                    toShowPopup(CaptureImage.this, p);

            }
        });

        //retrieve shared preferences value

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String from_code = prefs.getString("from_code", null);
        String to_code = prefs.getString("to_code", null);
        if (to_code != null && from_code !=null) {

            c_to_code=prefs.getString("to_code", null);
            c_from_code=prefs.getString("from_code", null);

            camera_from_code.setText(c_from_code);
            camera_to_code.setText(c_to_code);
            from_conversion_symbol=prefs.getString("from_symbol_native",null);
            to_conversion_symbol=prefs.getString("to_symbol_native",null);
            from_dollar_name_con=prefs.getString("from_name",null);
            to_dollar_name_con=prefs.getString("to_name",null);

        }

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("to_code", c_to_code);
        editor.putString("from_code", c_from_code);
        editor.putString("to_symbol_native", to_conversion_symbol);
        editor.putString("from_symbol_native", from_conversion_symbol);
        editor.putString("from_name", from_dollar_name_con);
        editor.putString("to_name", to_dollar_name_con);
        editor.commit();


        camera_cur_conversion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                // Perform action on click

                //Toast.makeText(getApplication(), "Currency Exchanged.", Toast.LENGTH_LONG).show();

                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

                String to_code = prefs.getString("to_code", null);

                String from_code = prefs.getString("from_code", null);

                if (to_code != null && from_code != null) {

                    camera_from_code.setText(to_code);
                    camera_to_code.setText(from_code);

                    from_conversion_symbol=prefs.getString("from_symbol_native",null);
                    to_conversion_symbol=prefs.getString("to_symbol_native",null);
                    from_dollar_name_con=prefs.getString("from_name",null);
                    to_dollar_name_con=prefs.getString("to_name",null);

                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("to_code", from_code);
                    editor.putString("from_code", to_code);
                    editor.putString("to_symbol_native", from_conversion_symbol);
                    editor.putString("from_symbol_native", to_conversion_symbol);
                    editor.putString("from_name", to_dollar_name_con);
                    editor.putString("to_name",from_dollar_name_con);
                    editor.commit();


                }
                }
        });
    }

    void takePicture() {


        cameraId = android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;

        captureImage = (Button) findViewById(R.id.captureImage);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        captureImage.setOnClickListener(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void requestCameraPermission() {

        Log.i(TAG, "CAMERA permission has NOT been granted. Requesting permission.");

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

            ActivityCompat.requestPermissions(CaptureImage.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CAMERA);
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.

        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CAMERA);
        }
        // END_INCLUDE(camera_permission_request)
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.e("Permission", "Granted");

                    Intent ChangeIntent = new Intent(CaptureImage.this, CaptureImage.class);

                    startActivity(ChangeIntent);
                    finish();
                } else {
                    Log.e("Permission", "Denied");
                }
                return;
            }
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        int[] location = new int[2];
        int[] location1 = new int[2];
        TextView button = (TextView) findViewById(R.id.camera_from_code);
        TextView to_flag = (TextView) findViewById(R.id.camera_to_code);

        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.
        button.getLocationOnScreen(location);

        to_flag.getLocationOnScreen(location1);

        //Initialize the Point with x, and y positions
        p = new Point();
        p.x = location[0];
        p.y = location[1];

        p1 = new Point();
        p1.x = location1[0];
        p1.y = location1[1];
    }

    // The method that displays the popup.
    private void fromShowPopup(final Activity context, Point p) {
        int popupWidth = 470;
        int popupHeight = 520;

        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);

        final PopupWindow popup = new PopupWindow(context);

        getAllCountries();

        countryListView = (ListView) layout
                .findViewById(R.id.country_code_picker_listview);

        adapter = new CountryCurrencyListAdapter(this, selectedCountriesList);
        countryListView.setAdapter(adapter);

        countryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {



                String from_symbol=selectedCountriesList.get(position).getSymbol();

                String from_code= selectedCountriesList.get(position).getCode();

                String from_name =selectedCountriesList.get(position).getName();

                String from_symbol_native=selectedCountriesList.get(position).getSymbol_native();

                String from_decimal_digits=selectedCountriesList.get(position).getDecimal_digits();

                String from_rounding=selectedCountriesList.get(position).getRounding();

                String from_name_plural=selectedCountriesList.get(position).getName_plural();


                camera_from_code.setText(from_code);


                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("from_symbol", from_symbol);
                editor.putString("from_name", from_name);
                editor.putString("from_symbol_native", from_symbol_native);
                editor.putString("from_decimal_digits", from_decimal_digits);
                editor.putString("from_rounding", from_rounding);
                editor.putString("from_code", from_code);
                editor.putString("from_name_plural", from_name_plural);
                editor.commit();

                popup.dismiss();

            }
        });

        // Creating the PopupWindow

        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = -40;
        int OFFSET_Y = 60;

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);


    }


    private void toShowPopup(final Activity context, Point p) {
        int popupWidth = 470;
        int popupHeight = 520;

        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);

        final PopupWindow popup = new PopupWindow(context);

        getAllCountries();

        countryListView = (ListView) layout
                .findViewById(R.id.country_code_picker_listview);

        adapter = new CountryCurrencyListAdapter(this, selectedCountriesList);
        countryListView.setAdapter(adapter);

        countryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                String to_symbol=selectedCountriesList.get(position).getSymbol();

                String to_code= selectedCountriesList.get(position).getCode();

                String to_name =selectedCountriesList.get(position).getName();

                String to_symbol_native=selectedCountriesList.get(position).getSymbol_native();

                String to_decimal_digits=selectedCountriesList.get(position).getDecimal_digits();

                String to_rounding=selectedCountriesList.get(position).getRounding();

                String to_name_plural=selectedCountriesList.get(position).getName_plural();


                camera_to_code.setText(to_code);


                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("to_symbol", to_symbol);
                editor.putString("to_name", to_name);
                editor.putString("to_symbol_native", to_symbol_native);
                editor.putString("to_decimal_digits", to_decimal_digits);
                editor.putString("to_rounding", to_rounding);
                editor.putString("to_code", to_code);
                editor.putString("to_name_plural", to_name_plural);
                editor.commit();


                popup.dismiss();

            }
        });

        // Creating the PopupWindow

        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = -40;
        int OFFSET_Y = 60;

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);


    }


    //get all countries list
    private List<Country> getAllCountries() {
        if (allCountriesList == null) {
            try {
                allCountriesList = new ArrayList<Country>();

                String allCountriesCode = readEncodedJsonString(CaptureImage.this);

                JSONArray countrArray = new JSONArray(allCountriesCode);

                for (int i = 0; i < countrArray.length(); i++) {
                    JSONObject jsonObject = countrArray.getJSONObject(i);
                    String countrySymbol = jsonObject.getString("symbol");
                    String countryName = jsonObject.getString("name");
                    String countrySymbolNative = jsonObject.getString("symbol_native");
                    String countryDecimalDigits = jsonObject.getString("decimal_digits");
                    String countryRounding = jsonObject.getString("rounding");
                    String countryCode = jsonObject.getString("code");
                    String countryNamePlural = jsonObject.getString("name_plural");

                    Country country = new Country();
                    country.setSymbol(countrySymbol);
                    country.setName(countryName);
                    country.setSymbol_native(countrySymbolNative);
                    country.setDecimal_digits(countryDecimalDigits);
                    country.setRounding(countryRounding);
                    country.setCode(countryCode);
                    country.setName_plural(countryNamePlural);
                    allCountriesList.add(country);
                }

                Collections.sort(allCountriesList, CaptureImage.this);

                selectedCountriesList = new ArrayList<Country>();
                selectedCountriesList.addAll(allCountriesList);

                // Return
                return allCountriesList;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //encode json string
    private static String readEncodedJsonString(Context context)
            throws java.io.IOException {

        String base64 = context.getResources().getString(R.string.currency_code);

        byte[] data = Base64.decode(base64, Base64.DEFAULT);

        return new String(data, "UTF-8");
    }

    @Override
    public int compare(Country lhs, Country rhs) {
        return lhs.getName().compareTo(rhs.getName());
    }


    /*-----------------------------------------*/
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!openCamera(CameraInfo.CAMERA_FACING_BACK)) {
            alertCameraDialog();
        }

    }
    int zoom;

    private boolean openCamera(int id) {
        boolean result = false;
        cameraId = id;
        releaseCamera();
        try {
            camera = Camera.open(cameraId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (camera != null) {
            try {
                setUpCamera(camera);
                camera.setErrorCallback(new ErrorCallback() {

                    @Override
                    public void onError(int error, Camera camera) {

                    }
                });
                Parameters parameters = camera.getParameters();
                //parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
                parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                camera.setParameters(parameters);
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();

                result = true;
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
                releaseCamera();
            }
        }
        return result;
    }

    private void setUpCamera(Camera c) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;

            default:
                break;
        }


            // Back-facing
            rotation = (info.orientation - degree + 360) % 360;

        c.setDisplayOrientation(rotation);
        Parameters params = c.getParameters();


        params.setRotation(rotation);
    }


    private void releaseCamera() {
        try {
            if (camera != null) {
                camera.setPreviewCallback(null);
                camera.setErrorCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.toString());
            camera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        if (camera != null) {
            camera.stopPreview();
            camera.release();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.captureImage:
                takeImage();
                break;

            default:
                break;
        }
    }

    private void takeImage() {

        // Progress Dialog
        pd = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        camera.takePicture(null, null, new PictureCallback() {

            private File imageFile;

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    // convert byte array into bitmap
                    Bitmap loadedImage = null;
                    Bitmap rotatedBitmap = null;
                    Bitmap cropBitmap = null;
                   //s Bitmap.Config ARGB_8888;
                    loadedImage = BitmapFactory.decodeByteArray(data, 0,
                            data.length);

                    // rotate Image
                    Matrix rotateMatrix = new Matrix();
                    rotateMatrix.postRotate(rotation);

                    rotatedBitmap = Bitmap.createBitmap(loadedImage, 0, 0,
                            loadedImage.getWidth(), loadedImage.getHeight(),
                            rotateMatrix, false);

                    //cropBitmap=scaleCenterCrop(rotatedBitmap,50,100);

                    String state = Environment.getExternalStorageState();
                    File folder = null;
                    if (state.contains(Environment.MEDIA_MOUNTED)) {
                        folder = new File(Environment
                                .getExternalStorageDirectory() + "/Currency_wizard");
                    } else {
                        folder = new File(Environment
                                .getExternalStorageDirectory() + "/Currency_wizard");
                    }


                    boolean success = true;
                    if (!folder.exists()) {
                        success = folder.mkdirs();
                    }
                    if (success) {
                        java.util.Date date = new java.util.Date();
                        imageFile = new File(folder.getAbsolutePath()
                                + File.separator
                                + new Timestamp(date.getTime()).toString()
                                + "Image.jpg");


                        System.out.println("Image Path:"+imageFile);

                        ByteArrayOutputStream ostream = new ByteArrayOutputStream();


                        cropBitmap=scaleCenterCrop(rotatedBitmap,50,100);

                        //cropBitmap = scaleDown(rotatedBitmap, 512, true);

                        cropBitmap.compress(CompressFormat.JPEG, 100, ostream);

                        uploadImage(cropBitmap);

                        // save image into gallery

                        imageFile.createNewFile();

                        FileOutputStream fout = new FileOutputStream(imageFile);
                        fout.write(ostream.toByteArray());
                        fout.close();


                        ContentValues values = new ContentValues();


                        values.put(MediaStore.Images.Media.DATE_TAKEN,
                                System.currentTimeMillis());
                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                        values.put(MediaStore.MediaColumns.DATA,
                                imageFile.getAbsolutePath());

                        CaptureImage.this.getContentResolver().insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


                    } else {
                        Toast.makeText(getBaseContext(), "Image Not saved",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


    /*public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
    {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

*/
    public Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {

        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);


        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;


        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;


        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }

    private void alertCameraDialog() {
        AlertDialog.Builder dialog = createAlert(CaptureImage.this,
                "Camera info", "error to open camera");
        dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        dialog.show();
    }

    private Builder createAlert(Context context, String title, String message) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(
                new ContextThemeWrapper(context,
                        android.R.style.Theme_Holo_Light_Dialog));
        dialog.setIcon(R.mipmap.ic_launcher);
        if (title != null)
            dialog.setTitle(title);
        else
            dialog.setTitle("Information");
        dialog.setMessage(message);
        dialog.setCancelable(false);
        return dialog;

    }

    public void uploadImage(Bitmap uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                //
                Bitmap bitmap =scaleBitmapDown(uri,1200);

                callCloudVision(bitmap);


            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                Toast.makeText(this, "", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, "Image picking failed because ", Toast.LENGTH_LONG).show();
        }
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private void callCloudVision(final Bitmap bitmap) throws IOException {
        // Switch text to loading

        // Do the real work in an async task, because we need to use the network anyway
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(new
                            VisionRequestInitializer(CLOUD_VISION_API_KEY));
                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                        // Add the image
                        Image base64EncodedImage = new Image();
                        // Convert the bitmap to a JPEG
                        // Just in case it's a format that Android understands but Cloud Vision
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(CompressFormat.JPEG, 90, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();

                        // Base64 encode the JPEG
                        base64EncodedImage.encodeContent(imageBytes);
                        annotateImageRequest.setImage(base64EncodedImage);

                        // add the features we want
                        annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                            Feature labelDetection = new Feature();
                            labelDetection.setType("TEXT_DETECTION");
                            labelDetection.setMaxResults(10);
                            add(labelDetection);
                        }});

                        // Add the list of one thing to the request
                        add(annotateImageRequest);
                    }});

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    // Due to a bug: requests to Vision API containing large images fail when GZipped.
                    annotateRequest.setDisableGZipContent(true);
                    Log.d(TAG, "created Cloud Vision request object, sending request");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return convertResponseToString(response);

                } catch (GoogleJsonResponseException e) {
                    Log.d(TAG, "failed to make API request because " + e.getContent());

                    //Toast.makeText(CaptureImage.this.getApplicationContext(), e.getContent(), Toast.LENGTH_LONG).show();


                } catch (IOException e) {
                    Log.d(TAG, "failed to make API request because of other IOException " +
                            e.getMessage());
                }
                return "Cloud Vision API request failed. Check logs for details.";
            }

            protected void onPostExecute(String result) {

                imageResult(result);

            }
        }.execute();
    }
    private String convertResponseToString(BatchAnnotateImagesResponse response) {

        String message ="";

        responseList=new ArrayList<JsonResponse>();

        List<EntityAnnotation> labels = response.getResponses().get(0).getTextAnnotations();
        if (labels != null) {

            message += String.format("%s", labels.get(0).getDescription());

        } else {
            message += "";
        }

        return message;
    }



private void imageResult(String value)
{
    closeProgress();

    if(value.length()>0) {

        System.out.println("Response Value : " + value);


        String[] parts = value.split("\n");


        responseList = new ArrayList<JsonResponse>();
        //Iterating String array for adding to list object without empty and null elements
        for (String string : parts) {

            if (string != null && string.length() > 0) {

                JsonResponse jsonResponse = new JsonResponse();

                jsonResponse.setResponse(string);

                responseList.add(jsonResponse);

            }

        }


        getAllCountries();

        System.out.println("responseList size : " + responseList.size());

        if (responseList.size() == 1) {
            System.out.println("single");

            check_onestr(responseList);


        } else {

            System.out.println("multiple array");


            scanResultsList = new ArrayList<ScanResultList>();
            scanResultsList.clear();

            getResponseCountriesList = new ArrayList<Country>();
            getResponseCountriesList.clear();

            for (JsonResponse value1 : responseList) {


                System.out.println(" Response values--------------------->" + value1.getResponse());


                for (char ch : value1.getResponse().toCharArray()) {

                    System.out.println(" Response values search -------------------:" + ch);

                    for (int j = 0; j < allCountriesList.size(); j++) {

                        String symbol = allCountriesList.get(j).getSymbol();

                        if (symbol.equals(String.valueOf(ch))) {
                            getResponseCountriesList.add(allCountriesList.get(j));

                        }
                    }
                }


                System.out.println("getResponseCountriesList" + getResponseCountriesList.size());

                camera_currency = "";

                camera_currency = value1.getResponse().replaceAll("[^.0-9]", "");

                if(!camera_currency.equals(".") && !camera_currency.isEmpty())
                {
                    ScanResultList scanResultList = new ScanResultList();

                    scanResultList.setScanResult(camera_currency);

                    scanResultsList.add(scanResultList);
                }

            }


            if (scanResultsList.size() > 0) {
                if (getResponseCountriesList.size() > 0) {

                    String from_symbol = getResponseCountriesList.get(0).getSymbol();

                    String from_code = getResponseCountriesList.get(0).getCode();

                    String from_name = getResponseCountriesList.get(0).getName();

                    String from_symbol_native = getResponseCountriesList.get(0).getSymbol_native();

                    String from_decimal_digits = getResponseCountriesList.get(0).getDecimal_digits();

                    String from_rounding = getResponseCountriesList.get(0).getRounding();

                    String from_name_plural = getResponseCountriesList.get(0).getName_plural();


                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("from_symbol", from_symbol);
                    editor.putString("from_name", from_name);
                    editor.putString("from_symbol_native", from_symbol_native);
                    editor.putString("from_decimal_digits", from_decimal_digits);
                    editor.putString("from_rounding", from_rounding);
                    editor.putString("from_code", from_code);
                    editor.putString("from_name_plural", from_name_plural);
                    editor.commit();


                    if (scanResultsList.size() == 1) {
                        final CurrencyApplication globalVariable = (CurrencyApplication) getApplicationContext();

                        globalVariable.setCamera_currency(scanResultsList.get(0).getScanResult());

                        Intent activityChangeIntent = new Intent(CaptureImage.this, CurrencyWizard.class);
                        startActivity(activityChangeIntent);
                        finish();

                    } else {


                        final CurrencyApplication globalVariable = (CurrencyApplication) getApplicationContext();

                        globalVariable.setScanList(scanResultsList);

                        Intent activityChangeIntent = new Intent(CaptureImage.this, ScanResult.class);
                        startActivity(activityChangeIntent);
                        finish();

                    }

                } else {

                    final CurrencyApplication globalVariable = (CurrencyApplication) getApplicationContext();

                    globalVariable.setScanList(scanResultsList);

                    Intent activityChangeIntent = new Intent(CaptureImage.this, ScanResult.class);
                    startActivity(activityChangeIntent);

                    finish();
                }
            } else {

                //alert
                showAlert(context,"We still haven't found anything.","Center the item. Make sure it fills the screen. Hold your device steady and Capture the photo.");
            }

        }
    }
    else{

        //alert
        showAlert(context,"We still haven't found anything.","Center the item. Make sure it fills the screen. Hold your device steady and Capture the photo.");
    }

}

    private  void  check_onestr(List<JsonResponse> jsonResponses)
    {

        for (JsonResponse value1 : jsonResponses) {

            System.out.println(" jsonResponses values single--------------------->"+value1.getResponse());

            getResponseCountriesList=new ArrayList<Country>();
            getResponseCountriesList.clear();


            for (char ch: value1.getResponse().toCharArray()) {

                System.out.println("jsonResponses values search single -------------------:"+ch);


                for(int j=0;j<allCountriesList.size();j++){

                    String symbol= allCountriesList.get(j).getSymbol();

                    if(symbol.equals(String.valueOf(ch)))
                    {

                        String from_symbol=allCountriesList.get(j).getSymbol();

                        String from_code= allCountriesList.get(j).getCode();

                        String from_name =allCountriesList.get(j).getName();

                        String from_symbol_native=allCountriesList.get(j).getSymbol_native();

                        String from_decimal_digits=allCountriesList.get(j).getDecimal_digits();

                        String from_rounding=allCountriesList.get(j).getRounding();

                        String from_name_plural=allCountriesList.get(j).getName_plural();

                        getResponseCountriesList.add(allCountriesList.get(j));


                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("from_symbol", from_symbol);
                        editor.putString("from_name", from_name);
                        editor.putString("from_symbol_native", from_symbol_native);
                        editor.putString("from_decimal_digits", from_decimal_digits);
                        editor.putString("from_rounding", from_rounding);
                        editor.putString("from_code", from_code);
                        editor.putString("from_name_plural", from_name_plural);
                        editor.commit();

                    }

                }

            }

            System.out.println("getResponseCountriesList"+getResponseCountriesList.size());

            camera_currency= value1.getResponse().replaceAll("[^.0-9]", "");

            System.out.println("Response Currency Value : "+camera_currency);

        }

        try {

            if(getResponseCountriesList.size()==1)
            {

                final CurrencyApplication globalVariable = (CurrencyApplication) getApplicationContext();

                globalVariable.setCamera_currency(camera_currency);

                Intent activityChangeIntent = new Intent(CaptureImage.this, CurrencyWizard.class);
                startActivity(activityChangeIntent);
                finish();
            }
            else
            {
                scanResultsList = new ArrayList<ScanResultList>();

                scanResultsList.clear();

                if(getResponseCountriesList.size()==0)
                {

                    if(camera_currency.length()>0)
                    {

                        if(!camera_currency.equals(".")&&!camera_currency.isEmpty())
                        {
                            ScanResultList scanResultList = new ScanResultList();

                            scanResultList.setScanResult(camera_currency);

                            scanResultsList.add(scanResultList);

                            final CurrencyApplication globalVariable = (CurrencyApplication) getApplicationContext();


                            globalVariable.setScanList(scanResultsList);

                            Intent activityChangeIntent = new Intent(CaptureImage.this, ScanResult.class);
                            startActivity(activityChangeIntent);

                            finish();
                        }

                        else
                        {
                            //alert

                            showAlert(context,"We still haven't found anything.","Center the item. Make sure it fills the screen. Hold your device steady and Capture the photo.");
                        }

                    }
                    else{

                        //alert

                        showAlert(context,"We still haven't found anything.","Center the item. Make sure it fills the screen. Hold your device steady and Capture the photo.");
                    }

                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null) {
            camera.stopPreview();
            //camera.release();
        }
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (camera != null) {
            camera.release();
        }
    }

    private void showAlert(final Context context, final String title, final String msg) {

        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        final Dialog alertDialog = new Dialog(context);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View convertView = (View) inflater.inflate(R.layout.commom_alert, null);
        alertDialog.setContentView(convertView);
        alertDialog.setCanceledOnTouchOutside(false);
        TextView txtContent = (TextView) convertView.findViewById(R.id.txtContent1);
        TextView txtHeader=(TextView) convertView.findViewById(R.id.txtHeader1);
        Button btnOk = (Button) convertView.findViewById(R.id.btnOk);
        txtHeader.setText(title);
        txtContent.setText(msg);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });

        alertDialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = alertDialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    private void closeProgress() {
        // TODO Auto-generated method stub
        if (pd.isShowing())
            pd.cancel();
    }
}
