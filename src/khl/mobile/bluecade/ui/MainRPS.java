package khl.mobile.bluecade.ui;

import khl.mobile.bluecade.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

@SuppressLint("NewApi")
public class MainRPS extends Fragment implements OnClickListener{

	Button buttonrps;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_blue_cade, null); 
        buttonrps = (Button) view.findViewById(R.id.buttonrps);
        buttonrps.setOnClickListener(this);
        return view;
    }

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonrps){
			Intent i2 = new Intent(getActivity(),RPSMainActivity.class);
			startActivity(i2);
		}
	}

}
