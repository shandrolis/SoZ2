package nl.voorbeeld.SoZ;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import nl.saxion.act.playground.R;
import nl.saxion.act.playground.model.Game;
import nl.saxion.act.playground.model.GameBoard;
import nl.voorbeeld.SoZ.objects.Enemy;
import nl.voorbeeld.SoZ.objects.Muur;
import nl.voorbeeld.SoZ.objects.Player;
import nl.voorbeeld.SoZ.objects.Projectile;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

public class SoZGame extends Game {
	public static final String TAG = "SoZGame";
	private MainActivity activity;
	private Player player = new Player();
	private boolean gameOver = false;
	public boolean _stop = false;
	private int enemiesToSpawn;

	private ArrayList<Enemy> enemyList = new ArrayList<Enemy>(5);
	private ArrayList<Muur> muurList = new ArrayList<Muur>();

	private Timer enemyMovementTimer;
	private Timer bulletTimer;
	
	private TimerTask shootingTimer;

	// init desoundpool om gamesounds te laden
	@SuppressWarnings("deprecation")
	private SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,
			0);

	// geeft de soundpool die gebruikt word voor de game
	public SoundPool getSoundPool() {
		return soundPool;
	}

	// laad een sound in
	public final int AK47_ONE_SHOT_SOUND;
	public final int GUNSHOT_ak;
	public final int GUNSHOT_shotgun;
	

	/*
	 * matthijs hier is het example:
	 * game.getSoundPool().play(game.AK47_ONE_SHOT, 1, 1, 1, 0, 1);
	 */

	public SoZGame(MainActivity activity) {

		
		super(new SoZBoard(), new Savegame(activity));
		this.activity = activity;
		savegame = new Savegame(activity);
		savegame.leesSaveGameUitFile();
		

		enemiesToSpawn = (int) Math.pow(((savegame.getLevel() * 12) + 1),1.05);

		initNewGame();

		SoZBoardView gameView = activity.getGameBoardView();
		GameBoard gameBoard = getGameBoard();
		gameView.setGameBoard(gameBoard);

		gameView.setFixedGridSize(gameBoard.getWidth(), gameBoard.getHeight());
		AK47_ONE_SHOT_SOUND = soundPool.load(activity.getApplicationContext(),
				R.raw.ak47_1, 1);
		GUNSHOT_ak=soundPool.load(activity.getApplicationContext(), R.raw.gunshot, 1);
		GUNSHOT_shotgun=soundPool.load(activity.getApplicationContext(), R.raw.shotgunsound, 1);
		
		//GUNSHOT=soundPool.load(activity.getApplicationContext(), R.raw.)
	}

	public void initNewGame() {
	//	score = 0;
		gameOver = false;

		GameBoard board = getGameBoard();
		board.removeAllObjects();

		board.addGameObject(player, 4, board.getHeight() - 1);

		muurList.add(new Muur());
		board.addGameObject(muurList.get(muurList.size() - 1), 0,
				board.getHeight() - 2);
		muurList.add(new Muur());
		board.addGameObject(muurList.get(muurList.size() - 1), 1,
				board.getHeight() - 2);
		muurList.add(new Muur());
		board.addGameObject(muurList.get(muurList.size() - 1), 2,
				board.getHeight() - 2);
		muurList.add(new Muur());
		board.addGameObject(muurList.get(muurList.size() - 1), 3,
				board.getHeight() - 2);
		muurList.add(new Muur());
		board.addGameObject(muurList.get(muurList.size() - 1), 4,
				board.getHeight() - 2);
		muurList.add(new Muur());
		board.addGameObject(muurList.get(muurList.size() - 1), 5,
				board.getHeight() - 2);
		muurList.add(new Muur());
		board.addGameObject(muurList.get(muurList.size() - 1), 6,
				board.getHeight() - 2);
		muurList.add(new Muur());
		board.addGameObject(muurList.get(muurList.size() - 1), 7,
				board.getHeight() - 2);
		muurList.add(new Muur());
		board.addGameObject(muurList.get(muurList.size() - 1), 8,
				board.getHeight() - 2);

		for (Muur muur : muurList) {
			muur.muurDamagedCheck(board);
		}
		enemyMovementTimer = new Timer();
		bulletTimer = new Timer();
		startEnemyMovementTimer();
		// startEnemySpawnTimer();
		board.updateView();
	}

	public void gameOver() {
		getGameBoard().removeAllObjects();

		while (muurList.size() > 0) {
			muurList.remove(0);
		}
		while (enemyList.size() > 0) {
			enemyList.remove(0);
		}
		enemyMovementTimer.cancel();
		enemyMovementTimer.purge();

		gameOver = true;
		gameBoard.updateView();

		Intent intent = new Intent(activity, GameOverActivity.class);
		activity.beginActivity(intent);
	}

	public boolean isGameOver() {
		return gameOver;
	}

	//public void changeScore() {
	//	score=score+60;
	//	activity.updateScoreLabel(score);
	//}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	public void startEnemyMovementTimer() {
		Log.i("Timer", "EnemyMovement timer created");

		enemyMovementTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// Move enemies
						for (int i = 0; i < enemyList.size(); i++) {
							enemyList.get(i).callMovement(gameBoard);
						}
						if (!gameOver) {
							if (enemiesToSpawn > 0) {
								boolean enemySpawned = false;
								while (!enemySpawned) {
									int random = (int) (Math.random() * 9);
									if (getGameBoard().getObject(random, 0) == null) {

										enemyList.add(new Enemy());
										getGameBoard()
												.addGameObject(
														enemyList.get(enemyList
																.size() - 1),
														random, 0);
										enemySpawned = true;
										Log.i("log enemy spawn", "loloihiybj");
										
									}
								}
							}
							enemiesToSpawn--;
							int tempInt;
							if(enemiesToSpawn<0){
								tempInt=0;
							}else{
								tempInt=enemiesToSpawn;
							}
						
							
						}
						gameBoard.updateView();
					}
				});

				Log.i("Timer", "enemyMovement and spawning timer fired");

			}
		}, 600, 1000);
		
	}
	
	public void projectileFire(final Projectile projectile){

		shootingTimer=new TimerTask() {
			@Override
			public void run() {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (projectile.Bestaat()){
							projectile.update(gameBoard);
							Log.i("Timer", "prjectile movement");
							gameBoard.updateView();
						}else{
							Log.i("Timer", "cancel");
							cancel();
							enemyMovementTimer.purge();
						}
						
					}
					
				});
			}
		};
		
		bulletTimer.schedule(shootingTimer, 45, 45);
	}

	public void levelCompleted() {
		getGameBoard().removeAllObjects();
		int i=0;
		while (muurList.size() > i) {
			muurList.remove(i);
			i++;
		}
		int i2=0;
		while (enemyList.size() > i2) {
			enemyList.remove(i2);
			i2++;
		}
		enemyMovementTimer.cancel();
		enemyMovementTimer.purge();

		gameOver = true;
		gameBoard.updateView();
		savegame.setLevel(savegame.getLevel()+1);
		savegame.schrijfSaveGame();
		Intent intent = new Intent(activity, CutsceneActivity.class);
		activity.beginActivity(intent);
		activity.finish();
	}

	public MainActivity getActivity() {
		return activity;
	}
	
	public void RemoveEnemy(Enemy enemy){
		int i =0;
		while (i< enemyList.size()){
			if (enemyList.get(i).getId()==enemy.getId()){
				Log.i("enemy founs", "fhiewuvigwvyuwej");
				enemyList.remove(i);
				
				savegame.setPoints(savegame.getPoints()+25);
				
			}
			i++;
		}
		
	}
	public int getEnemiesToSpawn(){
		return enemiesToSpawn;
	}
	public int getEnemiesAantal(){
		return enemyList.size();
	}
}
