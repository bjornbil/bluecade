package khl.mobile.bluecade.model.games.pictionary;

import java.util.Random;

public class WordList {
	
	//TODO: read words from text file
	private static String[] words = {"cat", 
		"sun", 
		"cup", 
		"ghost", 
		"flower", 
		"pie", 
		"cow", 
		"banana", 
		"snowflake", 
		"bug", 
		"book", 
		"jar", 
		"snake", 
		"light", 
		"tree", 
		"lips", 
		"apple", 
		"slide", 
		"socks", 
		"smile", 
		"swing", 
		"coat", 
		"shoe", 
		"water", 
		"heart", 
		"hat", 
		"ocean", 
		"kite", 
		"dog", 
		"mouth", 
		"milk", 
		"duck", 
		"eyes", 
		"skateboard", 
		"bird", 
		"boy", 
		"apple", 
		"person", 
		"girl", 
		"mouse", 
		"ball", 
		"house", 
		"star", 
		"nose", 
		"bed", 
		"whale", 
		"jacket", 
		"shirt", 
		"hippo", 
		"beach", 
		"egg", 
		"face", 
		"cookie", 
		"cheese", 
		"ice cream cone", 
		"drum", 
		"circle", 
		"spoon", 
		"worm", 
		"spider web"};
	
	public static String random(){
		Random r = new Random();
		return words[r.nextInt(words.length)];
	}
	
	//ignores case, spaces and e/es at the end of a word
	public static boolean equalsLenient(String s1, String s2)
	{
		String s1Lenient = replaceLast(s1.replaceAll(" ", ""), "s|es", "");
		String s2Lenient = replaceLast(s2.replaceAll(" ", ""), "s|es", "");
		if(s1Lenient.equalsIgnoreCase(s2Lenient)) return true;
		return false;
	}
	
    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }

}
