package com.chandana.smartmarket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by ProgrammingKnowledge on 1/5/2016.
 */
public class BackgroundWorker extends AsyncTask<String,String,String> {
    Context context;
    AlertDialog alertDialog;
    String result="";

    public static final String EXTRA_NAME="com.chandana.smartmarket.EXTRA_NAME";
    public static final String EXTRA_COST="com.chandana.smartmarket.EXTRA_COST";
    public static final String EXTRA_BAR="com.chandana.smartmarket.EXTRA_BAR";
    String name,bar;
    int cost,quantity;
    BackgroundWorker (Context ctx) {
        context = ctx;
    }
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];


        String login_url = "http://2c496be3.ngrok.io/login.php";
        String register_url = "http://2c496be3.ngrok.io/register.php";
        String scan_url="http://2c496be3.ngrok.io/scan.php";
        if(type.equals("login")) {
            try {
                String user_name = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")
                              +"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("register"))
        {
            try {
                String str_name=params[1];
                String str_email=params[2];
                String str_phnum=params[3];
                String str_username = params[4];
                String str_password = params[5];
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(str_name,"UTF-8")
                              +"&"+URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(str_email,"UTF-8")
                              +"&"+URLEncoder.encode("ph_number","UTF-8")+"="+URLEncoder.encode(str_phnum,"UTF-8")
                              +"&"+URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(str_username,"UTF-8")
                              +"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(str_password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        else if(type.equals("scan"))
        {

            try {
                String barcode = params[1];
                URL url = new URL(scan_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("bar_code","UTF-8")+"="+URLEncoder.encode(barcode,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result;
                String res=bufferedReader.readLine();

                if(res.equals("Sorry, Item not available"))
                {
                    result=res;
                }
                else if(res.equals("Sorry, Out of stock"))
                {
                    result=res;
                }
                else
                {
                    String[] parts=res.split("<br>");
                    result=parts[0];
                    name=parts[0];
                    cost=Integer.parseInt(parts[1]);
                    quantity=Integer.parseInt(parts[2]);
                    bar=parts[3];
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }



        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        if(context!=null) {
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Status");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });

        }
    }

    @Override
    protected void onPostExecute(String result) {
        if(result==null)
            result="It's null..";
        String res=result.substring(0,2);

        if(res.equals("We"))
        {
            alertDialog.setMessage(result);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Ok",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    Intent intent=new Intent(context,list.class);
                    context.startActivity(intent);
                }
            });
           alertDialog.show();
        }
        else if(result.equals("login not successful"))
        {
            alertDialog.setMessage(result);
            alertDialog.show();

        }
        else if(result.equals("User With email is already present") || result.equals("Username available try another"))
        {
            alertDialog.setMessage(result);
            alertDialog.show();
        }
        else if(result.equals("Registered successfully"))
        {

            alertDialog.setMessage(result);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Ok",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    Intent intent=new Intent(context,MainActivity.class);
                    context.startActivity(intent);
                }
            });
            alertDialog.show();
        }
        else if (result.equals("Sorry, Item not available")) {
                alertDialog.setMessage(result);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, list.class);
                        context.startActivity(intent);
                    }
                });
                alertDialog.show();

            }
        else if (result.equals("Sorry, Out of stock")) {
                alertDialog.setMessage(result + " for now we will notify you.");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, list.class);
                        context.startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        else {
            if(!result.equals("It's null..")) {
                alertDialog.setMessage(result);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, list.class);
                        intent.putExtra(EXTRA_NAME, name);
                        intent.putExtra(EXTRA_COST, cost);
                        intent.putExtra(EXTRA_BAR, bar);
                        context.startActivity(intent);
                    }
                });
                alertDialog.show();
            }
            }

    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
}