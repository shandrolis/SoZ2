package nl.voorbeeld.SoZ;

import nl.saxion.act.playground.R;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

@SuppressWarnings("unused")
public class GameOverActivity extends Activity {
	private View view;
	private MediaPlayer musicPlayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameover);
		view = findViewById(R.id.view);
		
		view.setBackgroundResource(R.drawable.gameoverfinal);
		registerOnViewClick();
		
		musicPlayer = new MediaPlayer();
		musicPlayer = MediaPlayer.create(this, R.raw.gameovermusic);
		musicPlayer.setLooping(true);
		musicPlayer.start();
		
		
	}
	
	private void registerOnViewClick() {

		// Add a click listener to the button that calls initNewGame()
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				musicPlayer.reset();
				finish();
				
			}
		});
	}

	

}
