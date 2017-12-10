package IM.Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipUtil {
	public static final String GZIP_ENCODE_UTF_8 = "UTF-8";
	public static final String GZIP_ENCODE_ISO_8859_1 = "ISO-8859-1";

	public static byte[] compress(byte[] bytes) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(bytes);
			gzip.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	public static byte[] compress(String str) throws IOException {
		return compress(str.getBytes(GZIP_ENCODE_UTF_8));
	}

	public static byte[] uncompress(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		try {
			GZIPInputStream ungzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = ungzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	public static String uncompressToString(byte[] bytes, String encoding) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		try {
			GZIPInputStream ungzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = ungzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
			return out.toString(encoding);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String uncompressToString(byte[] bytes) {
		return uncompressToString(bytes, GZIP_ENCODE_UTF_8);
	}
}
