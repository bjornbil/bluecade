package khl.mobile.bluecade.ui;
import khl.mobile.bluecade.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentTemplate extends Fragment{
	
	private String title;
	private int imageResourceId;
	private Context context;
	private Class<?> gameclass;
	private Class<?> infoclass;
	private ImageButton infobutton;
	private ImageButton startgamebutton;
	private int id;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment, null); 
		ImageView i = (ImageView) view.findViewById(R.id.templateImage);
		TextView t = (TextView) view.findViewById(R.id.templateText);
		infobutton = (ImageButton) view.findViewById(R.id.imageButton1);
		infobutton.setOnClickListener(new View.OnClickListener(){
    		@Override
    		public void onClick(View arg0) {
    			Intent i=new Intent();
    			i.setClass(context,infoclass);
    	        i.putExtra("id",id);      
    			startActivity(i);
    		}
    	});
		startgamebutton = (ImageButton) view.findViewById(R.id.imageButton2);
		startgamebutton.setOnClickListener(new View.OnClickListener(){
    		@Override
    		public void onClick(View arg0) {
    			Intent i=new Intent();
    			i.setClass(context,gameclass);
    	        i.putExtra("id",id);      
    			startActivity(i);
    		}
    	});
		i.setImageDrawable(view.getResources().getDrawable(imageResourceId));
		t.setText(title);
        return view;
    }
    
    public void setInfoClass(Class<?> infoclass){
    	this.infoclass = infoclass;
    }
    
    public void setGameClass(Class<?> gameclass){
    	this.gameclass = gameclass;
    }
    
    public void setContext(Context context){
    	this.context = context;
    }

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setId(int id){
		this.id = id;
	}

	public void setImage(int imageResourceId) {
		this.imageResourceId = imageResourceId;
	}

}
