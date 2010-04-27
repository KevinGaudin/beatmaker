package com.frandoid.beatmaker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Xml;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PlayBeatMaker extends Activity {

	private int numPad = 0;
	private int selectedItem = -1;
	private String filenameSave = "";
	private String pathFile = "/sdcard/BeatMakerSong/";
	private ListFiles files = new ListFiles();
	private SoundPoolManager MP = new SoundPoolManager(this);
	private Vector<Object> vectUri = new Vector<Object>();
	private static final int MENU_QUIT = 0;
	private static final int HELP_DEV = 1;
	private static final int SAVE_PACK = 2;
	private static final int OPEN_PACK = 3;
	private int VueHeight = -1;
	private int VueWidth = -1;
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /////////////////////////////////////////////////////////////////
		// LES METHODES ASSOCIEES AUX PADS //////////////////////////////
		/////////////////////////////////////////////////////////////////
		// On va chercher l'élèment vue qui est dans le layout
	    final View vue = (View) findViewById(R.id.View01);
	    vue.setBackgroundResource(R.drawable.background);
	    // Et on fait une action dès qu'on le touche = faire de la musique
	    vue.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// Position du pad 
				final int x = (int) (event.getX()/vue.getWidth()*4);
				final int y = (int) (event.getY()/vue.getHeight()*4);
				// Numéro associé au Pad
				numPad = (int) y*4+x+1;
				// Le son associé est joué
				if (event.getAction()==MotionEvent.ACTION_DOWN) {
					MP.playSound(numPad-1);
				}
				return false;
			}
		});
	    // Et on fait une action dès que fait un appui long = changer de sample
	    vue.setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View v) {
				// On ouvre le dossier contenant les sons en attente d'une selection par l'utilisateur
				changeSample();
				return false;
			}
		});

	    
	    // On ouvre un pack pour initialiser les pads
	    vectUri.clear();
	    changePack();
	    
	}
	
    /////////////////////////////////////////////////////////////////
	// LES METHODES DE MENU /////////////////////////////////////////
	///////////////////////////////////////////////////////////////// 
	// Menu pour quitter ou aider
    public final boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, OPEN_PACK, 0, "Open Pack");
        menu.add(0, SAVE_PACK, 0, "Save Pack");
        menu.add(0, HELP_DEV, 0, "Help");
        menu.add(0, MENU_QUIT, 0, "Back");
        return true;
    }
    public final boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_QUIT: //QUIT
            	MP.release();
            	this.finish();
                return true;
            case HELP_DEV: //HELP
            	new AlertDialog.Builder(this)
	                .setMessage("- Touch a pad for play sample.\n" +
	                			"- Long touch a pad for change the sample.\n" +
	                			"For adding your own samples, put your mp3 files " +
	                			"in the directory BeatMakerSong on your SDCard.\n" +
	                			"- Menu->Save for saving your sampler configuration.\n" +
	                			"- Menu->Open for opening a sampler configuration.")
	                .setPositiveButton("OK", null) 
	                .show();
        		return true;
            case OPEN_PACK:
            	changePack();
            	return true;	
            case SAVE_PACK:
            	savePack();
            	return true;
        }
        return false;
	}

    /////////////////////////////////////////////////////////////////
	// LES METHODES DE MAJ DES SAMPLES //////////////////////////////
	/////////////////////////////////////////////////////////////////
    public final void updateSoundPoolManager() {
    	MP.init(vectUri);
    }
    public final void displaySamplesNamesOnView() {
    	final Integer[] RtextView = {R.id.TextView01, R.id.TextView02, R.id.TextView03, R.id.TextView04,
					    			R.id.TextView05, R.id.TextView06, R.id.TextView07, R.id.TextView08,
					    			R.id.TextView09, R.id.TextView10, R.id.TextView11, R.id.TextView12,
					    			R.id.TextView13, R.id.TextView14, R.id.TextView15, R.id.TextView16};
    	final View vue = (View) findViewById(R.id.View01);
		VueWidth = vue.getWidth();
	    VueHeight = vue.getHeight();
    	int textW = (int) (VueWidth/4*0.8);
    	int textH = (int) (VueHeight/4*0.8);
    	for (int i=0; i<RtextView.length; i++) {
    		final TextView textPad = (TextView) findViewById(RtextView[i]);
        	int offHeight = (int)(i/4)*(int)(VueHeight/4)+(int)(VueHeight/4*0.1);
        	int offWidth = (int)(i%4)*(int)(VueWidth/4)+(int)(VueWidth/4*0.1);
        	textPad.setWidth(textW+offWidth);
        	textPad.setHeight(textH+offHeight);
        	textPad.setPadding(offWidth, offHeight, 0, 0);
        	textPad.setText(new File((String) vectUri.get(i)).getName());
    	}
    }
    
    /////////////////////////////////////////////////////////////////
	// LES METHODES DE CHANGEMENT DE SAMPLE /////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean changeSample() {
		// On scanne les samples dans le répertoire BeatMakerSong
		files.ScanSamples();
		// On récupère la liste des fichiers samples
		final List<File> MySamples = files.getSamples();
		// On remplis la sequence pour le dialog
		final CharSequence[] items = new CharSequence[MySamples.size()];
		for (int i=0; i<MySamples.size(); i++) {
			items[i] = MySamples.get(i).getName();
		}
		// AlertDialog pour choisir le sample
		selectedItem = -1;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Pick a sample for pad "+numPad);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	if (selectedItem!=-1) {
		    		vectUri.set(numPad-1, MySamples.get(selectedItem).getAbsolutePath());
		    	}
		    	updateSoundPoolManager();
		    	displaySamplesNamesOnView();
		        dialog.dismiss();
		    }
		});
		builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		        selectedItem = item;
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
		return true;
	}

	/////////////////////////////////////////////////////////////////
	// LES METHODES DE CHANGEMENT DE PACK ///////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean changePack() {
		// On scanne les samples dans le répertoire BeatMakerSong
		files.ScanPacks();
		// On récupère la liste des fichiers samples
		final List<File> MyPacks = files.getPacks();
		// On remplis la sequence pour le dialog
		final CharSequence[] items = new CharSequence[MyPacks.size()];
		for (int i=0; i<MyPacks.size(); i++) {
			items[i] = MyPacks.get(i).getName();
		}
		// AlertDialog pour choisir le sample
		selectedItem = -1;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Pick a pack of samples");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	if (selectedItem!=-1) {
		    		try {readXmlFile(new File(MyPacks.get(selectedItem).getAbsolutePath()));}
					catch (IOException e) {e.printStackTrace();} catch (XmlPullParserException e) {e.printStackTrace();}
		    	}
		    	updateSoundPoolManager();
		    	displaySamplesNamesOnView();
		        dialog.dismiss();
		    }
		});
		builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		        selectedItem = item;
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
		return true;
	}
	protected void readXmlFile(File xmlfile) throws IOException, XmlPullParserException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		FileInputStream fileos = null;          
	    fileos = new FileInputStream(xmlfile);
	    XmlPullParser xpp = factory.newPullParser();
	    xpp.setInput(fileos, "UTF-8");
	    
	    int eventType = xpp.getEventType(); 
	    int iter = 0;
	    while (eventType != XmlPullParser.END_DOCUMENT) {
	    	if(eventType == XmlPullParser.START_TAG) {
	    		if (xpp.getName().contains("pad")) {
	    			File path = new File(xpp.getAttributeValue(null, "pathSample"));
	    			vectUri.add(iter, path.getAbsolutePath());
	    			iter = iter+1;
	    		}
	    	}
	    	eventType = xpp.next(); 	
	    }
	}
	
	/////////////////////////////////////////////////////////////////
	// LES METHODES DE SAUVEGARDE DE PACK ///////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void savePack() {
		final FrameLayout fl = new  FrameLayout(this);
		final EditText input = new EditText(this);
		input.setGravity(Gravity.CENTER);
		fl.addView(input, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
		input.setText("");
		filenameSave = "";
		new AlertDialog.Builder(this)
		     .setView(fl)
		     .setTitle("Enter Pack name:")
		     .setPositiveButton("OK", new DialogInterface.OnClickListener(){
		          public void onClick(DialogInterface d, int which) {
		        	  filenameSave = input.getText().toString();
		        	  try {writeXmlFile(new File(pathFile+filenameSave+".bmsp"));} 
		        	  catch (IOException e) {e.printStackTrace();}
		        	  Toast.makeText(getApplicationContext(), "Pack saved in "+pathFile+filenameSave+".bmsp", Toast.LENGTH_LONG).show();
		              d.dismiss();
		          }
		     })
		     .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
		          public void onClick(DialogInterface d, int which) {
		               d.dismiss();
		          }
		     })
		     .create().show();
	}
	protected void writeXmlFile(File xmlfile) throws IOException {
		xmlfile.createNewFile();
	    FileOutputStream fileos = null;          
	    fileos = new FileOutputStream(xmlfile);
	    XmlSerializer serializer = Xml.newSerializer();
    	serializer.setOutput(fileos, "UTF-8");
    	serializer.startDocument(null, Boolean.valueOf(true));
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        serializer.startTag(null, "beatmakerPack");
        for (int i=1; i<=16; i++) {
        	serializer.startTag(null, "pad"+i);
	        serializer.attribute(null, "pathSample", vectUri.get(i-1).toString());
	        serializer.endTag(null, "pad"+i);
        }
        serializer.endTag(null, "beatmakerPack");
        serializer.endDocument();
        serializer.flush();
        fileos.close(); 
	}
	
}
