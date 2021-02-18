package common.java.Http;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;

import java.io.File;

public class Mime {
	public static final String defaultMime = "text/plain";
	public final String getMime(byte[] buff){
		String rs;
		Magic parser = new Magic() ;     
		MagicMatch match;
		try {
            match = Magic.getMagicMatch(buff);
			rs = match.getMimeType();
		} catch (Exception e) {
			rs = defaultMime;
		} 
		return rs;
	}
	public final String getMime(File file){
		String rs;
		Magic parser = new Magic() ;     
		MagicMatch match;
		try {
            match = Magic.getMagicMatch(file, false);
			rs = match.getMimeType();
		} catch (Exception e) {
			rs = defaultMime;
		} 
		return rs;
	}
}
