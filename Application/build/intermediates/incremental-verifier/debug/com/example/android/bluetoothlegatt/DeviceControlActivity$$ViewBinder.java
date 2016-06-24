// Generated code from Butter Knife. Do not modify!
package com.example.android.bluetoothlegatt;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class DeviceControlActivity$$ViewBinder<T extends com.example.android.bluetoothlegatt.DeviceControlActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492958, "field 'deviceAddress'");
    target.deviceAddress = finder.castView(view, 2131492958, "field 'deviceAddress'");
    view = finder.findRequiredView(source, 2131492959, "field 'connectionState'");
    target.connectionState = finder.castView(view, 2131492959, "field 'connectionState'");
    view = finder.findRequiredView(source, 2131492960, "field 'sendDataValue'");
    target.sendDataValue = finder.castView(view, 2131492960, "field 'sendDataValue'");
    view = finder.findRequiredView(source, 2131492961, "field 'dataValue'");
    target.dataValue = finder.castView(view, 2131492961, "field 'dataValue'");
    view = finder.findRequiredView(source, 2131492962, "field 'sendVerify' and method 'sendVerifyClick'");
    target.sendVerify = finder.castView(view, 2131492962, "field 'sendVerify'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.sendVerifyClick();
        }
      });
    view = finder.findRequiredView(source, 2131492963, "field 'sendReadAlarmSleep' and method 'sendReadAlarm_sleep'");
    target.sendReadAlarmSleep = finder.castView(view, 2131492963, "field 'sendReadAlarmSleep'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.sendReadAlarm_sleep();
        }
      });
    view = finder.findRequiredView(source, 2131492964, "field 'sendReadAlarm' and method 'sendReadAlarm'");
    target.sendReadAlarm = finder.castView(view, 2131492964, "field 'sendReadAlarm'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.sendReadAlarm();
        }
      });
    view = finder.findRequiredView(source, 2131492965, "field 'sendReadClock' and method 'sendReadClock'");
    target.sendReadClock = finder.castView(view, 2131492965, "field 'sendReadClock'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.sendReadClock();
        }
      });
    view = finder.findRequiredView(source, 2131492967, "field 'sendSetSound' and method 'sendSetSound'");
    target.sendSetSound = finder.castView(view, 2131492967, "field 'sendSetSound'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.sendSetSound();
        }
      });
    view = finder.findRequiredView(source, 2131492966, "field 'setSound'");
    target.setSound = finder.castView(view, 2131492966, "field 'setSound'");
    view = finder.findRequiredView(source, 2131492969, "field 'sendSetClock' and method 'sendSetClock'");
    target.sendSetClock = finder.castView(view, 2131492969, "field 'sendSetClock'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.sendSetClock();
        }
      });
    view = finder.findRequiredView(source, 2131492968, "field 'setClock'");
    target.setClock = finder.castView(view, 2131492968, "field 'setClock'");
    view = finder.findRequiredView(source, 2131492971, "field 'sendSetAlarm' and method 'sendSetAlarm'");
    target.sendSetAlarm = finder.castView(view, 2131492971, "field 'sendSetAlarm'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.sendSetAlarm();
        }
      });
    view = finder.findRequiredView(source, 2131492970, "field 'setAlarm'");
    target.setAlarm = finder.castView(view, 2131492970, "field 'setAlarm'");
    view = finder.findRequiredView(source, 2131492972, "field 'sendSetTime' and method 'sendSetTime'");
    target.sendSetTime = finder.castView(view, 2131492972, "field 'sendSetTime'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.sendSetTime();
        }
      });
    view = finder.findRequiredView(source, 2131492973, "field 'sendSleepTime' and method 'sendSetSleep'");
    target.sendSleepTime = finder.castView(view, 2131492973, "field 'sendSleepTime'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.sendSetSleep();
        }
      });
  }

  @Override public void unbind(T target) {
    target.deviceAddress = null;
    target.connectionState = null;
    target.sendDataValue = null;
    target.dataValue = null;
    target.sendVerify = null;
    target.sendReadAlarmSleep = null;
    target.sendReadAlarm = null;
    target.sendReadClock = null;
    target.sendSetSound = null;
    target.setSound = null;
    target.sendSetClock = null;
    target.setClock = null;
    target.sendSetAlarm = null;
    target.setAlarm = null;
    target.sendSetTime = null;
    target.sendSleepTime = null;
  }
}
