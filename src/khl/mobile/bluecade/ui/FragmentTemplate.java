package khl.mobile.bluecade.ui;
import khl.mobile.bluecade.R;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentTemplate extends Fragment{
	
	private String title;
	private int imageResourceId;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment, null); 
		ImageView i = (ImageView) view.findViewById(R.id.templateImage);
		TextView t = (TextView) view.findViewById(R.id.templateText);
		i.setImageDrawable(view.getResources().getDrawable(imageResourceId));
		t.setText(title);
        return view;
    }

	public void setTitle(String title) {
		this.title = title;
	}

	public void setImage(int imageResourceId) {
		this.imageResourceId = imageResourceId;
	}

}
