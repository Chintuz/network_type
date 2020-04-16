package com.network_type;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** TestPlPlugin */
public class NetworkTypePlugin implements FlutterPlugin{

  private EventChannel eventChannel;
  private MethodChannel methodChannel;


  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    setupChannels(flutterPluginBinding.getFlutterEngine().getDartExecutor(), flutterPluginBinding.getApplicationContext());
  }

  public static void registerWith(Registrar registrar) {
    NetworkTypePlugin plugin = new NetworkTypePlugin();
    plugin.setupChannels(registrar.messenger(), registrar.context());
  }

  private void setupChannels(BinaryMessenger messenger, Context context) {

    methodChannel = new MethodChannel(messenger, "network_type");
    eventChannel = new EventChannel(messenger, "connectivity_status");

    ConnectivityManager connectivityManager =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

    Connectivity connectivity = new Connectivity(connectivityManager, wifiManager);

    ConnectivityMethodChannelHandler methodChannelHandler = new ConnectivityMethodChannelHandler(connectivity);
    ConnectivityBroadcastReceiver receiver = new ConnectivityBroadcastReceiver(context, connectivity);

    methodChannel.setMethodCallHandler(methodChannelHandler);
    eventChannel.setStreamHandler(receiver);
  }


  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
  }
}
