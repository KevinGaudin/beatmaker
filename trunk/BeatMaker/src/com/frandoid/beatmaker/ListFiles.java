package com.frandoid.beatmaker;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListFiles {

// Definition du repertoire initial de Scan
public File Scan_Repertoire_Ini = new File("/sdcard/BeatMakerSong");

// Definition des Listes de fichier à retourner
public List<File> ListSamples = new ArrayList<File>();
public List<File> ListPacks = new ArrayList<File>();

	public boolean setDirectory(File dir) {
		if (Test_Directory(dir)) {
			Scan_Repertoire_Ini = dir;
		}
		else return false;
		return true;
	}

	public boolean ScanSamples() {
		boolean Test_Scan = false;
		ListSamples = Scan_Samples(Scan_Repertoire_Ini);
		Test_Scan = true;
		return Test_Scan;
	}
	
	public boolean ScanPacks() {
		boolean Test_Scan = false;
		ListPacks = Scan_Packs(Scan_Repertoire_Ini);
		Test_Scan = true;
		return Test_Scan;
	}

	public List<File> getSamples() {
		return ListSamples;
	}
	
	public List<File> getPacks() {
		return ListPacks;
	}
	
	private List<File> Scan_Samples(File Repertoire){
		// Definition du resultat de fichier a envoyer sous forme de liste
		List<File> List_Fichier = new ArrayList<File>();
		
		// Si le repertoire est correct on scanne les fichiers
		if (Test_Directory(Repertoire)){
			File[] List_Fichier_Scan = Repertoire.listFiles();
			List<File> Array_List_Fichier = Arrays.asList(List_Fichier_Scan);
			for(File file : Array_List_Fichier){
				if (file.isFile()){
					// Ajout des Media Son
					if (file.getName().endsWith(".mp3") ||
							file.getName().endsWith(".ogg") ||
							file.getName().endsWith(".wav")){
						List_Fichier.add(file);
					}
				}
				// cas d'un dossier Dossier on continue l'iteration
				if (file.isDirectory()){
					List<File> Iteration_List = Scan_Samples(file);
					List_Fichier.addAll(Iteration_List);
				}
			}
		}
		return List_Fichier;
	}
	
	private List<File> Scan_Packs(File Repertoire){
		// Definition du resultat de fichier a envoyer sous forme de liste
		List<File> List_Fichier = new ArrayList<File>();
		
		// Si le repertoire est correct on scanne les fichiers
		if (Test_Directory(Repertoire)){
			File[] List_Fichier_Scan = Repertoire.listFiles();
			List<File> Array_List_Fichier = Arrays.asList(List_Fichier_Scan);
			for(File file : Array_List_Fichier){
				if (file.isFile()){
					// Ajout des Media Son
					if (file.getName().endsWith(".bmsp")){
						List_Fichier.add(file);
					}
				}
				// cas d'un dossier Dossier on continue l'iteration
				if (file.isDirectory()){
					List<File> Iteration_List = Scan_Packs(file);
					List_Fichier.addAll(Iteration_List);
				}
			}
		}
		return List_Fichier;
	}
	
	private boolean Test_Directory(File Repertoire) {
		// Analyse si le repertoire est correct
		boolean Test_Repertoire=true;
		if ( Repertoire == null || !Repertoire.exists() || 
				!Repertoire.isDirectory() || !Repertoire.canRead() ||
				Repertoire.getName().equals(".thumbnails")){
			Test_Repertoire=false;
		}
		return Test_Repertoire;
	}
} 
