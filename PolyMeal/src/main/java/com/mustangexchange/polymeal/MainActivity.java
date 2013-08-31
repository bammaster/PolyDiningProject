package com.mustangexchange.polymeal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends Activity{

    public static ArrayList<ItemSet> vgItems;
    public static ArrayList<ItemSet> sandItems;
    private Parser parseHtml;
    private Thread internet;
    public static int vgOrSand;
    private Handler uiUpdate= new Handler();
    private TextView download;
    private ProgressBar downloadProgress;
    private Type gsonType = new TypeToken<ArrayList<ItemSet>>() {}.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        download = (TextView)findViewById(R.id.textDownload);
        downloadProgress = (ProgressBar)findViewById(R.id.progressBar);
        final TextView welcome = (TextView)findViewById(R.id.welcomeText);
        final TextView select = (TextView)findViewById(R.id.selectText);
        final Button sandwich = (Button)findViewById(R.id.buttonSand);
        final Button vista = (Button)findViewById(R.id.buttonVista);
        vgItems = new ArrayList<ItemSet>();
        sandItems = new ArrayList<ItemSet>();
        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        final Animation in2 = new AlphaAnimation(0.0f, 1.0f);
        select.setVisibility(View.INVISIBLE);
        sandwich.setVisibility(View.INVISIBLE);
        vista.setVisibility(View.INVISIBLE);
        in.setDuration(1000);
        in2.setDuration(1000);
        welcome.startAnimation(in);
        in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                select.setVisibility(View.VISIBLE);
                sandwich.setVisibility(View.VISIBLE);
                vista.setVisibility(View.VISIBLE);
                select.startAnimation(in2);
                sandwich.startAnimation(in2);
                vista.startAnimation(in2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        internet = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection one = Jsoup.connect("http://dining.calpoly.edu/menus/?lid=1014&name=VG%20Cafe").timeout(10000);
                    Connection two = Jsoup.connect("http://dining.calpoly.edu/menus/?lid=1012&name=Sandwich%20Factory").timeout(10000);
                    uiUpdate.post(new Runnable() {
                        @Override
                        public void run() {
                            vista.setEnabled(false);
                            sandwich.setEnabled(false);
                            download.setVisibility(View.VISIBLE);
                            downloadProgress.setVisibility(View.VISIBLE);
                        }
                    });
                    Document docVg = one.get();
                    uiUpdate.post(new Runnable() {
                        @Override
                        public void run() {
                            download.setText("Parsing Vista Grande Menu Data...");
                        }
                    });
                    try
                    {
                        parseHtml = new Parser(vgItems);
                        parseHtml.parse(docVg,false);
                    }
                    catch(Exception e)
                    {
                       uiUpdate.post(new Runnable() {
                           @Override
                           public void run() {
                               AlertDialog.Builder onErrorConn= new AlertDialog.Builder(MainActivity.this);
                               onErrorConn.setTitle("Error Parsing!");
                               onErrorConn.setMessage("There was an error parsing menu data. Please relaunch the app and try again. If the issue persists contact the developer.");
                               onErrorConn.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int button) {
                                       finish();
                                   }
                               });
                               onErrorConn.show();
                           }
                       });
                    }
                    uiUpdate.post(new Runnable() {
                        @Override
                        public void run() {
                            download.setText("Downloading Sandwich Factory Menu Data...");
                        }
                    });
                    Document docSand = two.get();
                    uiUpdate.post(new Runnable() {
                        @Override
                        public void run() {
                            download.setText("Parsing Sandwich Factory Menu Data...");
                        }
                    });
                    try
                    {
                        parseHtml = new Parser(sandItems);
                        parseHtml.parse(docSand,true);
                    }
                    catch(Exception e)
                    {
                        uiUpdate.post(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder onErrorConn= new AlertDialog.Builder(MainActivity.this);
                                onErrorConn.setTitle("Error Parsing!");
                                onErrorConn.setMessage("There was an error parsing menu data. Please relaunch the app and try again. If the issue persists contact the developer.");
                                onErrorConn.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int button) {
                                        finish();
                                    }
                                });
                                onErrorConn.show();
                            }
                        });
                    }
                    uiUpdate.post(new Runnable() {
                        @Override
                        public void run() {
                            vista.setEnabled(true);
                            sandwich.setEnabled(true);
                            download.setVisibility(View.INVISIBLE);
                            downloadProgress.setVisibility(View.INVISIBLE);
                        }
                    });
                } catch (Exception e) {
                    if(getSharedPreferences("PolyMeal",MODE_PRIVATE).getString("Sandwich Factory Items","").equals("")||getSharedPreferences("PolyMeal",MODE_PRIVATE).getString("Vista Grande Items","").equals(""))
                    {
                        uiUpdate.post(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder onErrorConn= new AlertDialog.Builder(MainActivity.this);
                                onErrorConn.setTitle("Error Connecting!");
                                onErrorConn.setMessage("There was an error connecting to the website to download the menu and no previous menu data was found. Please check your data connection and try again.");
                                onErrorConn.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int button) {
                                        finish();
                                    }
                                });
                                onErrorConn.show();
                            }
                        });
                    }
                    else
                    {
                        SharedPreferences appSharedPrefs = getSharedPreferences("PolyMeal",MODE_PRIVATE);
                        Gson gson = new Gson();
                        String sand = appSharedPrefs.getString("Sandwich Factory Items", "");
                        sandItems = gson.fromJson(sand, gsonType);
                        String vg = appSharedPrefs.getString("Vista Grande Items", "");
                        vgItems = gson.fromJson(vg,gsonType);
                        vista.setEnabled(true);
                        sandwich.setEnabled(true);
                        download.setVisibility(View.INVISIBLE);
                        downloadProgress.setVisibility(View.INVISIBLE);
                    }
                }
                SharedPreferences appSharedPrefs = getSharedPreferences("PolyMeal",MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                Gson gson = new Gson();
                String sand = gson.toJson(sandItems,gsonType);
                String vg = gson.toJson(vgItems,gsonType);
                prefsEditor.putString("Sandwich Factory Items", sand);
                prefsEditor.putString("Vista Grande Items", vg);
                prefsEditor.commit();
            }
        });
        internet.start();
    }

    public void onResume()
    {
        super.onResume();
    }

    public void vg(View v)
    {
        final Intent i = new Intent(this,VistaActivity.class);
        if(vgOrSand==2&&Cart.getCart().size()>0)
        {
            AlertDialog.Builder notifyClear = new AlertDialog.Builder(MainActivity.this);
            notifyClear.setTitle("Warning!");
            notifyClear.setMessage("Your cart contains Sandwich Factory items. If you continue the cart will be cleared and these items will be removed. Do you want to continue?");
            notifyClear.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int button) {
                    Cart.clear();
                    Toast.makeText(MainActivity.this, "Cart Cleared!", Toast.LENGTH_SHORT).show();
                    startActivity(i);

                }
            });
            notifyClear.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int button) {
                }
            });
            notifyClear.show();
        }
        else
        {
            vgOrSand = 1;
            startActivity(i);
        }

    }

    public void sandwich(View v)
    {
        final Intent i = new Intent(this,SandwichActivity.class);
        if(vgOrSand==1&&Cart.getCart().size()>0)
        {
            AlertDialog.Builder notifyClear = new AlertDialog.Builder(MainActivity.this);
            notifyClear.setTitle("Warning!");
            notifyClear.setMessage("Your cart contains Vista Grande items. If you continue the cart will be cleared and these items will be removed. Do you want to continue?");
            notifyClear.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int button) {
                    Cart.clear();
                    Toast.makeText(MainActivity.this,"Cart Cleared!",Toast.LENGTH_SHORT).show();
                    startActivity(i);

                }
            });
            notifyClear.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int button) {
                }
            });
            notifyClear.show();
        }
        else
        {
            vgOrSand = 2;
            startActivity(i);
        }

    }
}