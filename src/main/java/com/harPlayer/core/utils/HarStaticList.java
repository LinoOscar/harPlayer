package com.harPlayer.core.utils;

import java.util.ArrayList;
import java.util.HashMap;

import com.harPlayer.core.utils.har.HarEntry;

public class HarStaticList {
	
	//Current remedy List

	
	private static ArrayList<HarEntry> myNavegationHar = new ArrayList<HarEntry>();


	public static ArrayList<HarEntry> getMyNavegationHar() {
		return myNavegationHar;
	}

	public static void setMyNavegationHar(ArrayList<HarEntry> myNavegationHar) {
		HarStaticList.myNavegationHar = myNavegationHar;
	}
}
