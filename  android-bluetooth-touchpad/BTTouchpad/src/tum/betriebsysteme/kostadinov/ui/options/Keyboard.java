/**
    Copyright (C) 2011 Nikolay Kostadinov
   
    This file is part of BTTouchpad.

    BTTouchpad is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    BTTouchpad is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with BTTouchpad.  If not, see <http://www.gnu.org/licenses/>. 
    
 */

package tum.betriebsysteme.kostadinov.ui.options;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import tum.betriebsysteme.kostadinov.R;
import tum.betriebsysteme.kostadinov.btframework.report.HIDReportKeyboard;
import tum.betriebsysteme.kostadinov.ui.options.util.SharedFunctions;
import tum.betriebsysteme.kostadinov.util.ActivityResource;
import tum.betriebsysteme.kostadinov.util.State;

public class Keyboard extends Option implements TextWatcher {

	public Keyboard(OptionListener optionListener) {
		super(optionListener);
	}

	@Override
	public void initOptionUI() {
		
		State.setUIState(State.UI_STATE_OPTION);
	
		ActivityResource.setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		ViewGroup main = (ViewGroup) ActivityResource.get().findViewById(R.id.main);
		View keyboardView = ActivityResource.inflate(R.layout.keyboard);
		EditText keyboardInput = (EditText) keyboardView.findViewById(R.id.keyboard_input);
		keyboardInput.addTextChangedListener(this);
		main.removeAllViews();
		main.addView(keyboardView);
		main.invalidate();
		
		optionActive = true;   
		  
	}

	@Override
	public void destroyOptionUI() {
		optionActive = false;
	}

	@Override
	public void afterTextChanged(Editable s) {
		//do nothing
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		//do nothing
	}

	@Override
	public void onTextChanged(CharSequence sequence, int start, int before, int count) {
	
		if(!optionActive) return;
		
		if(before > 0) {
			HIDReportKeyboard report = new HIDReportKeyboard();
			report.setSingleKeycode(0x2A);
			this.optionListener.onOptionEvent(report);
			this.optionListener.onOptionEvent(new HIDReportKeyboard());
			return;
		}
		
		int sign = sequence.charAt(start);

		this.optionListener.onOptionEvent(SharedFunctions.getReportFromKey(sign));
		this.optionListener.onOptionEvent(new HIDReportKeyboard());
		
	}

	
	
	
}
