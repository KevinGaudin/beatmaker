package com.frandoid.beatmaker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;



public class BeatMaker extends Activity {

	private String pathFile = "/sdcard/BeatMakerSong/";
	private static final int MENU_QUIT = 0;
	private static final int HELP_DEV = 1;
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        
        // A la première utilisation, on copie les Ressources MP3 dans un dossier sur la SDCard
        File dir = new File(pathFile);
        if (!dir.exists()) {
        	dir.mkdirs();
        }
        initialiseBeatMaker();

        /////////////////////////////////////////////////////////////////
		// LES METHODES ASSOCIEES AU BUTTONS ////////////////////////////
		/////////////////////////////////////////////////////////////////
		// Button01
        final Button but1 = (Button) findViewById(R.id.Button01);
	    but1.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent myIntent = new Intent(BeatMaker.this, PlayBeatMaker.class);
				BeatMaker.this.startActivity(myIntent);
			}
	    });
	 // Button01
        final Button but2 = (Button) findViewById(R.id.Button02);
	    but2.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Toast.makeText(getApplicationContext(), "Download packs is not currently allowed", Toast.LENGTH_LONG).show();
			}
	    });
	    
	    
	 // Alerte Dialogue à l'ouverture du l'appli
		new AlertDialog.Builder(this)
		    .setMessage("This is a beta of BeatMaker. You can try it and maybe help developers ?\n" +
		    		"dev.beatmaker@gmail.com")
		    .setPositiveButton("Try", null) 
		    .show();
	}
	
    /////////////////////////////////////////////////////////////////
	// LES METHODES DE MENU /////////////////////////////////////////
	///////////////////////////////////////////////////////////////// 
	// Menu pour quitter ou aider
    public final boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, HELP_DEV, 0, "Help");
        menu.add(0, MENU_QUIT, 0, "Quit");
        return true;
    }
    public final boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_QUIT: //QUIT
            	this.finish();
                return true;
            case HELP_DEV: //HELP
            	new AlertDialog.Builder(this)
	                .setMessage("Welcome to BeatMaker.\n" +
	                			"Start just by clicking a Button.\n" +
	                			"dev.beatmaker@gmail.com")
	                .setPositiveButton("OK", null) 
	                .show();
        		return true;
        }
        return false;
	}
		
	/////////////////////////////////////////////////////////////////
	// LES METHODES D'INITIALISATION DE L'APPLI /////////////////////
	///////////////////////////////////////////////////////////////// 
	// Copie des samples de base sur la SDCard, ne s'execute qu'une fois
    private boolean initialiseBeatMaker() {
    	final Integer[] Rsamples = {	R.raw.pad1,		R.raw.pad2, 	R.raw.pad3,		R.raw.pad4,
    									R.raw.pad5,		R.raw.pad6, 	R.raw.pad7,		R.raw.pad8,
    									R.raw.pad9,		R.raw.pad10, 	R.raw.pad11,	R.raw.pad12,
    									R.raw.pad13,	R.raw.pad14, 	R.raw.pad15,	R.raw.pad16, 
    									R.raw.defaultpack};
    	String pathPack = pathFile+"default_pack/";
    	File dir = new File(pathPack);
        if (!dir.exists()) {
        	dir.mkdirs();
        }
    	for (int i=1;i<=17;i++) {
    		// Copie du fichier dans BeatMakerSong/default_pack/ s'il n'existe pas
    		String filename="default_pad"+i+".mp3";
    		if (i==17) {filename="default_pack.bmsp";}
    		File k = new File(pathPack, filename);
    		boolean exists = k.exists();
    	    if (!exists){
		    	byte[] buffer=null;
		    	InputStream fIn = getBaseContext().getResources().openRawResource(Rsamples[i-1]);
		    	int size=0;
		    	try {
		    		size = fIn.available();
		    	    buffer = new byte[size];
		    	    fIn.read(buffer);
		    	    fIn.close();
		    	} catch (IOException e) {return false;}
		    	FileOutputStream save;
		    	try {
		    		save = new FileOutputStream(pathPack+filename);
		    	    save.write(buffer);
		    	    save.flush();
		    	    save.close();
		    	} catch (FileNotFoundException e) {return false;} catch (IOException e) {return false;} 
		    	// Ajout à la bibliothèque
//		        values.put(MediaStore.MediaColumns.DATA, k.getAbsolutePath());
//		        values.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);
//		        values.put(MediaStore.MediaColumns.MIME_TYPE, "BeatMaker");
//		        values.put(MediaStore.Audio.Media.ARTIST, "BeatMaker");
//		        values.put(MediaStore.Audio.Media.ALBUM, "BeatMaker");
//		        values.put(MediaStore.Audio.Media.MIME_TYPE, "BeatMaker");
//		        values.put(MediaStore.Audio.Media.DISPLAY_NAME, filename);
//		        values.put(MediaStore.Audio.Media.TITLE, filename);
//		        this.getContentResolver().insert(MediaStore.Audio.Media.getContentUriForPath(k.getAbsolutePath()),values); 
//		    	values.clear();
    	    }
	    }
    	return true;
	}


}
