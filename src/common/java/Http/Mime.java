package common.java.Http;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;

import java.io.File;

public class Mime {
	public static final String defaultMime = "text/plain";

	public static final String getMime(byte[] buff) {
		try {
			MagicMatch match = Magic.getMagicMatch(buff);
			return match.getMimeType();
		} catch (Exception e) {
			return defaultMime;
		}
	}

	public static final String getMime(File file) {
		try {
			MagicMatch match = Magic.getMagicMatch(file, true);
			return match.getMimeType();
		} catch (Exception e) {
			return defaultMime;
		}
	}
}
