package com.helloar.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;





public class HttpUtils {


	/**
	 * 发送get请求
	 */
	public static String sendGet(String url) {
		String result = "";
		BufferedReader in = null;
		HttpURLConnection connection = null;
		try {
			URL realUrl = new URL(url);
			try {
				Log.i("test", "成功");
			 connection = (HttpURLConnection) realUrl.openConnection();
				
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.connect();
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			Log.i("test", "失败："+e);
//			System.out.println("失败" + e);
			e.printStackTrace();
		}
		} catch (MalformedURLException e) {
//			System.out.println("url 閿欒");
			Log.i("test", "url为空");
			e.printStackTrace();
		}
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	
	public static String sendPostRequest(String urlPath, String content, int connectTimeout, int readTimeout, String charset) throws Exception {
		try {
			URL url = new URL(urlPath);
			URLConnection urlConnection = url.openConnection();
			System.setProperty("sun.net.client.defaultConnectTimeout", connectTimeout + "");
			System.setProperty("sun.net.client.defaultReadTimeout", readTimeout + "");
			urlConnection.setRequestProperty("Content-Type", "text/plain;charset=" + charset);
			HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
			httpUrlConnection.setDoOutput(true);
			httpUrlConnection.setDoInput(true);
			httpUrlConnection.setUseCaches(false);
			httpUrlConnection.setRequestMethod("POST");
			System.out.println("http开始链接");
			httpUrlConnection.connect();
			System.out.println("http链接成功");
			OutputStream outStrm = httpUrlConnection.getOutputStream();
			outStrm.write(content.getBytes(charset));
			outStrm.flush();
			outStrm.close();
			int resultCode = httpUrlConnection.getResponseCode();
			if (HttpURLConnection.HTTP_OK == resultCode) {
				System.out.println("HTTP_OK");
				InputStream in = urlConnection.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
				StringBuffer temp = new StringBuffer();
				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					temp.append(line).append("\r\n");
				}
				bufferedReader.close();
				return new String(temp.toString().getBytes(), charset);
			} else {
				throw new RuntimeException("" + resultCode);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}