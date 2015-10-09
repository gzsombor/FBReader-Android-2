/*
 * Copyright (C) 2007-2015 FBReader.ORG Limited <contact@fbreader.org>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.geometerplus.zlibrary.ui.android.view;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import org.geometerplus.android.fbreader.FBReaderMainActivity;

import org.fbreader.common.R;

public abstract class MainView extends View {
	protected Integer myColorLevel;
	private TextView myInfoView;
	private final Timer myInfoTimer = new Timer();
	private volatile TimerTask myInfoCancelTask;

	public MainView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MainView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MainView(Context context) {
		super(context);
	}

	protected final void showInfo(final String text) {
		if (myInfoView == null) {
			myInfoView = (TextView)((FBReaderMainActivity)getContext()).findViewById(R.id.main_view_info);
		}
		if (myInfoView == null) {
			return;
		}

		synchronized (myInfoTimer) {
			if (myInfoCancelTask != null) {
				myInfoCancelTask.cancel();
			}
			myInfoCancelTask = new TimerTask() {
				public void run() {
					synchronized (myInfoTimer) {
						post(new Runnable() {
							public void run() {
								myInfoView.setVisibility(View.GONE);
							}
						});
					}
				}
			};
			myInfoTimer.schedule(myInfoCancelTask, 1000);
		}
		final int color = myColorLevel == null ? 0x80 : 0x80 * myColorLevel / 0xFF;
		post(new Runnable() {
			public void run() {
				myInfoView.setVisibility(View.VISIBLE);
				myInfoView.setTextColor(Color.argb(0xCC, color, color, color));
				myInfoView.setText(text);
			}
		});
	}

	public final void setScreenBrightness(int percent, boolean showPercent) {
		if (percent < 1) {
			percent = 1;
		} else if (percent > 100) {
			percent = 100;
		}

		final Context context = getContext();
		if (!(context instanceof FBReaderMainActivity)) {
			return;
		}

		final float level;
		final Integer oldColorLevel = myColorLevel;
		if (percent >= 25) {
			// 100 => 1f; 25 => .01f
			level = .01f + (percent - 25) * .99f / 75;
			myColorLevel = null;
		} else {
			level = .01f;
			myColorLevel = 0x60 + (0xFF - 0x60) * Math.max(percent, 0) / 25;
		}

		final FBReaderMainActivity activity = (FBReaderMainActivity)context;
		activity.getZLibrary().ScreenBrightnessLevelOption.setValue(percent);
		if (showPercent) {
			showInfo(percent + "%");
		}
		activity.setScreenBrightnessSystem(level);
		if (oldColorLevel != myColorLevel) {
			updateColorLevel();
			postInvalidate();
		}
	}

	public final int getScreenBrightness() {
		if (myColorLevel != null) {
			return (myColorLevel - 0x60) * 25 / (0xFF - 0x60);
		}

		final Context context = getContext();
		if (!(context instanceof FBReaderMainActivity)) {
			return 50;
		}
		final float level = ((FBReaderMainActivity)context).getScreenBrightnessSystem();
		// level = .01f + (percent - 25) * .99f / 75;
		return 25 + (int)((level - .01f) * 75 / .99f);
	}

	protected abstract void updateColorLevel();
}
