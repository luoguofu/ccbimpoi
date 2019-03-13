package com.weqia.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.ClipboardManager;
import android.text.Spannable;
import android.text.style.URLSpan;
import android.widget.EditText;
import android.widget.TextView;

import com.weqia.BaseInit;
import com.weqia.data.StatedPerference;
import com.weqia.data.UtilData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author berwin
 */
@SuppressWarnings("deprecation")
public class StrUtil {

	/**
	 * 格式化文件大小，显示KB/MB/GB =>KB 传入单位KB
	 * 
	 * @return String
	 */

	@SuppressLint("DefaultLocale")
	public static String formatFileSize(String kbStr) {
		if (isEmptyOrNull(kbStr)) {
			return "0KB";
		}
		long SIZE_KB = 1024;
		long SIZE_MB = SIZE_KB * 1024;
		long SIZE_GB = SIZE_MB * 1024;
		float size = 0;
		try {
			size = Float.parseFloat(kbStr);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return "";
		}
		size = size * SIZE_KB;
		// if (size < SIZE_KB) {
		// return String.format("%d B", (int) size);
		// } else
		if (size < SIZE_MB) {
			return String.format("%.2f KB", (float) size / SIZE_KB);
		} else if (size < SIZE_GB) {
			return String.format("%.2f MB", (float) size / SIZE_MB);
		} else {
			return String.format("%.2f GB", (float) size / SIZE_GB);
		}
	}

	public static String shareEncode(String shareStr) {
		String ret = "";
		try {
			ret = URLEncoder.encode(Base64Util.encode(shareStr.getBytes()), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	// public static final String EMPTY_STRING = "";

	public static <T extends UtilData> T getDataObject(String obj, Class<? extends UtilData> cls) {
		if (obj == null) {
			return null;
		}
		return T.fromString(cls, obj);
	}

	public static <T extends UtilData> List<T> getDataArray(String content, Class<? extends UtilData> cls) {
		if (content == null) {
			return null;
		}
		ArrayList<T> datas = new ArrayList<T>();
		try {
			@SuppressWarnings("unchecked")
			T data = (T) cls.newInstance();
			JSONArray arr = new JSONArray(getUnBomString(content));
			for (int i = 0; i < arr.length(); i++) {
				JSONObject temp = (JSONObject) arr.get(i);
				data = UtilData.fromString(cls, temp.toString());
				datas.add(data);
			}
			return datas;
		} catch (JSONException e) {
			return null;
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * 内容复制到剪切板
	 * 
	 * @param text
	 * @Description
	 * @author Dminter
	 */
	public static void copyText(String text) {
		ClipboardManager cbm = (ClipboardManager) BaseInit.ctx.getSystemService(Context.CLIPBOARD_SERVICE);
		cbm.setText(text);
		L.toastShort("已复制");
	}

	public static <T> boolean listNotNull(List<T> t) {
		if (t != null && t.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static <T> boolean listNotNull(Collection<T> t) {
		if (t != null && t.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static <T> boolean listIsNull(List<T> t) {
		if (t == null || t.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static <T> List<T> listInvert(List<T> list) {
		List<T> out = new ArrayList<T>();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				out.add(0, list.get(i));
			}
		}
		return out;
	}

	/**
	 * 手机号码（头尾显示3位，中间的*号表示）
	 * 
	 * @param phoneNumber
	 * @return
	 * @Description
	 * @author Dminter
	 */
	public static String getPhoneHide(String phoneNumber) {
		if (RegexUtil.matchString(phoneNumber, RegexUtil.REGEX_PHONENUM)) {
			return phoneNumber.substring(0, 3) + "*****"
					+ phoneNumber.substring(phoneNumber.length() - 3, phoneNumber.length());
		}
		return null;

	}

	/**
	 * 公告内容最多1000字符
	 * 
	 * @param text
	 * @return
	 * @Description
	 * @author Dminter
	 */
	public static boolean isMaxLen1000(String text) {
		if (text != null && !text.equals("")) {
			if (text.length() <= 1000) {
				return true;
			} else {
				return false;
			}

		} else {
			return false;
		}

	}

	/**
	 * 注册新企业,最少两个字符
	 * 
	 * @param text
	 * @return
	 * @Description
	 * @author Dminter
	 */
	public static boolean isMinLen2(String text) {
		if (text != null && !text.equals("")) {
			if (text.length() >= 2) {
				return true;
			} else {
				return false;
			}

		} else {
			return false;
		}

	}

	/**
	 * 验证手机
	 * 
	 * @param mobiles
	 * @return
	 * @Description
	 */
	public static boolean isMobile(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * @param string
	 * @return boolean
	 * @throws @Title:
	 *             isEmptyOrNull
	 */
	public static boolean isEmptyOrNull(String string) {
		if (string == null || string.trim().length() == 0 || string.equalsIgnoreCase("null")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param string
	 * @return boolean
	 * @throws @Title:
	 *             notEmptyOrNull
	 */
	public static boolean notEmptyOrNullOr0(String string) {
		if (string != null && !string.equalsIgnoreCase("null") && string.trim().length() > 0 && !"0".equals(string)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean notEmptyOrNull(String string) {
		if (string != null && !string.equalsIgnoreCase("null") && !string.equalsIgnoreCase("[]")
				&& string.trim().length() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean intergerNotNull(Integer number) {
		if (number != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param fileS
	 * @return String
	 * @throws @Title:
	 *             FormetFileSize
	 */
	// public static String formetFileSize(long fileSize) {
	// DecimalFormat df = new DecimalFormat("#.00");
	// String fileSizeString = "";
	// if (fileSize < 0) {
	// fileSizeString = String.valueOf(fileSize);
	// } else if (fileSize < 1024) {
	// fileSizeString = df.format((double) fileSize) + "B";
	// } else if (fileSize < 1048576) {
	// fileSizeString = df.format((double) fileSize / 1024) + "K";
	// } else if (fileSize < 1073741824) {
	// fileSizeString = df.format((double) fileSize / 1048576) + "M";
	// } else {
	// fileSizeString = df.format((double) fileSize / 1073741824) + "G";
	// }
	// return fileSizeString;
	// }

	/**
	 * @param bytes
	 * @return String
	 * @throws @Title:
	 *             bytesToHexString
	 */
	public static String bytesToHexString(byte[] bytes) {
		// http://stackoverflow.com/questions/332079
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	/**
	 * 获取简短类名
	 * 
	 * @return String
	 * @throws @Title:
	 *             getShortClassName
	 */
	public static String getShortClassName(String className) {
		if (className == null)
			return null;
		int index = className.lastIndexOf(".");
		return className.substring(index + 1);
	}

	/**
	 * 获取MD5串名称
	 * 
	 * @param string
	 * @return String
	 * @throws @Title:
	 *             getMD5String
	 */
	public static String getMD5String(String string) {
		if (notEmptyOrNull(string)) {
			return MD5Util.md32(string);
		} else {
			return null;
		}
	}

	/**
	 * 获取时间命名的文件名
	 * 
	 * @return
	 * @Description
	 */
	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyy-MM-dd-HH-mm-ss", TimeUtils.getDefaultLocale());
		return dateFormat.format(date);
	}

	/**
	 * @param string
	 * @param length
	 * @return String
	 * @throws @Title:
	 *             makeLongRepeatString
	 */
	public static String makeLongRepeatString(String string, int length) {
		if (string == null) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			buffer.append(string);
		}
		return buffer.toString();
	}

	/**
	 * @param str
	 * @return boolean
	 * @throws @Title:
	 *             isNumeric
	 */
	// public static boolean isNumeric(String str){
	// for (int i = str.length();--i>=0;){
	// if (!Character.isDigit(str.charAt(i))){
	// return false;
	// }
	// }
	// return true;
	// }
	//
	// public static boolean isNumeric(String str){
	// Pattern pattern = Pattern.compile("[0-9]*");
	// return pattern.matcher(str).matches();
	// }
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			int chr = str.charAt(i);
			if (chr < 48 || chr > 57)
				return false;
		}
		return true;
	}

	public static boolean isChinese(String str) {
		boolean isChinese = false;
		if (str.length() < str.getBytes().length) {
			isChinese = true;
		} else {
			isChinese = false;
		}
		return isChinese;
	}

	public static ArrayList<String> TenRandom(int max) {
		ArrayList<String> temp = new ArrayList<String>();
		Random random = new Random();
		HashSet<Integer> set = new HashSet<Integer>();
		while (true) {
			int number = random.nextInt(max);
			set.add(number);
			if (set.size() == 15)
				break;
		}
		for (Iterator<Integer> it = set.iterator(); it.hasNext();) {
			temp.add(it.next().toString());
		}
		return temp;
	}

	/**
	 * 超过100显示...
	 * 
	 * @param num
	 * @return
	 * @Description
	 */
	public static String getNumber(Integer num) {
		if (num != null) {
			int numVaule = num.intValue();
			if (numVaule <= 0) {
				return "0";
			} else if (numVaule > 0 && numVaule <= 100) {
				return num.toString();
			} else {
				return "...";
			}
		}
		return "0";
	}

	/**
	 * 根据路径获取文件名
	 * 
	 * @param path
	 * @return
	 * @Description
	 */
	public static String getFileNameByPath(String path) {
		if (path == null)
			return null;
		int index = path.lastIndexOf("/");
		return path.substring(index + 1);
	}

	/**
	 * 根据路径获取文件后缀
	 * 
	 * @param path
	 * @return
	 * @Description
	 */
	public static String getFileNameExtensionByPath(String path) {
		if (path == null)
			return null;
		int index = path.lastIndexOf(".");
		String extension = path.substring(index + 1);
		if (extension != null) {
			return extension.toLowerCase(TimeUtils.getDefaultLocale());
		} else {
			return null;
		}
	}

	public static boolean contains(String str, char searchChar) {
		if ((str == null) || (str.length() == 0)) {
			return false;
		}

		return str.indexOf(searchChar) >= 0;
	}

	/**
	 * 是否小写
	 * 
	 * @param cr
	 * @return
	 * @Description
	 */
	public static boolean isLowerChar(char cr) {
		if (cr >= 'a' && cr <= 'z') {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否小写
	 * 
	 * @param cr
	 * @return
	 * @Description
	 */
	public static boolean isUpperChar(char cr) {
		if (cr >= 'A' && cr <= 'Z') {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 去掉UTF-8存储的时候文件头的BOM
	 * 
	 * @param str
	 * @return
	 * @Description
	 */
	public static String getUnBomString(String str) {
		if (str != null && str.startsWith("\ufeff")) {
			str = str.substring(1);
		}
		return str.toString();
	}

	/**
	 * 获取图片路径
	 * 
	 * @param ctx
	 * @param uri
	 * @return
	 */
	public static String getAbsoluteImagePath(Activity ctx, Uri uri) {
		if (uri.toString().startsWith("file://")) {
			// String str;
			// try {
			// str = java.net.URLEncoder.encode(uri.toString(), "UTF-8");
			// str = str.replaceAll("%2F", "/");// 需要把网址的特殊字符转过来
			// str = str.replaceAll("%3A", ":");
			return uri.toString().replace("file://", "");
			// } catch (UnsupportedEncodingException e) {
			// CheckedExceptionHandler.handleException(e);
			// return null;
			// }

		} else {
			return null;
		}
	}

	// 截取字符串
	public static String subStr(String input, int len) {
		int inpuLen = input.length();
		if (len >= inpuLen) {
			return input;
		} else {
			return input.substring(0, len);
		}

	}

	public static String subStrDot(String input, int len) {
		if (isEmptyOrNull(input)) {
			return null;
		}
		int inpuLen = input.length();
		if (len >= inpuLen) {
			return input;
		} else {
			return input.substring(0, len) + "...";
		}
	}

	/**
	 * 半角转换为全角
	 * 
	 * @param input
	 * @return
	 */
	public static String toDbc(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	// /**
	// * 根据Uri获取路径
	// *
	// * @param ctx
	// * @param uri
	// * @return
	// */
	// public static String getImagePath(Context context, Uri uri) {
	// String[] projection = {MediaStore.Images.Media.DATA};
	//
	// Cursor cursor =
	// context.getContentResolver().query(uri, projection, null, null, null);
	// int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	// cursor.moveToFirst();
	// String path = cursor.getString(index);
	// cursor.close();
	// cursor = null;
	// return path;
	// }

	public static String getRealImagePathFromURI(Uri contentUri) {
		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		StringBuffer buff = new StringBuffer();
		buff.append("(").append(MediaStore.Images.ImageColumns._ID).append("=")
				.append("'" + contentUri.getLastPathSegment() + "'").append(")");
		Cursor cursor = BaseInit.ctx.getContentResolver().query(contentUri, proj, buff.toString(), null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
			cursor.close();
		}
		return res;
	}

	public static String getRealVideoPathFromURI(Uri contentUri) {
		String res = null;
		String[] proj = { MediaStore.Video.Media.DATA };
		StringBuffer buff = new StringBuffer();
		buff.append("(").append(MediaStore.Video.Media._ID).append("=")
				.append("'" + contentUri.getLastPathSegment() + "'").append(")");
		Cursor cursor = BaseInit.ctx.getContentResolver().query(contentUri, proj, buff.toString(), null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
			res = cursor.getString(column_index);
			cursor.close();
		}
		return res;
	}

	public static boolean isFlagContain(int sourceFlag, int compareFlag) {
		return (sourceFlag & compareFlag) == compareFlag;
	}

	public static void stripUnderlines(TextView textView) {
		if (null != textView && textView.getText() instanceof Spannable) {
			Spannable s = (Spannable) textView.getText();
			URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
			if (spans != null && spans.length > 0) {
				for (URLSpan span : spans) {
					int start = s.getSpanStart(span);
					int end = s.getSpanEnd(span);
					s.removeSpan(span);
					span = new CURLSpanUtils(span.getURL());
					s.setSpan(span, start, end, 0);
				}
			}
		}
	}

	// 从resources中的raw 文件夹中获取文件并读取数据
	public static String getFromRaw(Context ctx, int id) {
		String result = "";
		try {
			InputStream in = ctx.getResources().openRawResource(id);
			// 获取文件的字节数
			int lenght = in.available();
			// 创建byte数组
			byte[] buffer = new byte[lenght];
			// 将文件中的数据读到byte数组中
			in.read(buffer);
			result = new String(buffer, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static boolean isExpired(String netUrl) {
		boolean expired = true;
		String expireKey = "Expires=";
		if (StrUtil.notEmptyOrNull(netUrl) && netUrl.contains(expireKey)) {
			int keyIndex = netUrl.indexOf(expireKey);
			if (keyIndex != -1) {
				int begin = keyIndex + expireKey.length();
				int end = begin + 10;
				if (begin > netUrl.length() || end > netUrl.length()) {
					expired = true;
				} else {
					String tmpTime = netUrl.substring(begin, end);
					try {
						long expTime = Long.parseLong(tmpTime);
						if (expTime >= System.currentTimeMillis() / 1000) {
							expired = false;
						} else {
							expired = true;
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
						expired = true;
					}
				}
			}
		}
		// if (L.D) L.e(expired ? ("失效 " + netUrl) : ("还有效果 " + netUrl));
		return expired;
	}

	private static void setHistoryEtMap(String key, String value) {
		if (StrUtil.isEmptyOrNull(value)) {
			StatedPerference.getInstance().remove(key);
		} else {
			StatedPerference.getInstance().put(key, value, true);
		}
	}

	/**
	 * ET默认选中最后一个字符
	 * 
	 * @param ets
	 * @Description
	 * @author Dminter
	 */

	public static void etSelectionLast(EditText... ets) {
		for (int i = 0; i < ets.length; i++) {
			ets[i].setSelection(ets[i].getText().toString().length());
		}

	}

	public static void etSave(EditText... ets) {
		for (int i = 0; i < ets.length; i++) {
			if (StrUtil.notEmptyOrNull(ets[i].getText().toString().trim())
					&& !ets[i].getText().toString().trim().contains("@")) {
				setHistoryEtMap(String.valueOf(ets[i].getId()), ets[i].getText().toString().trim());
			}
		}
	}

	public static void etSave(String key, EditText... ets) {
		for (int i = 0; i < ets.length; i++) {
			if (!ets[i].getText().toString().trim().contains("@")) {
				setHistoryEtMap(String.valueOf(ets[i].getId()) + key, ets[i].getText().toString().trim());
			}
			// if (notEmptyOrNull(ets[i].getText().toString().trim())
			// && !ets[i].getText().toString().trim().contains("@")) {
			// UtilApplication.setHistoryEtMap(String.valueOf(ets[i].getId()) +
			// key, ets[i]
			// .getText().toString().trim());
			// }
		}

	}

	public static void etResume(EditText... ets) {
		for (int i = 0; i < ets.length; i++) {
			String realKey = String.valueOf(ets[i].getId());
			getEtData(i, realKey, ets);
		}
	}

	public static void etResume(String key, EditText... ets) {
		for (int i = 0; i < ets.length; i++) {
			String realKey = String.valueOf(ets[i].getId() + key);
			getEtData(i, realKey, ets);
		}
	}

	private static void getEtData(int i, String realKey, EditText... ets) {
		String input = StatedPerference.getInstance().get(realKey, String.class);
		if (StrUtil.notEmptyOrNull(input)) {
			ets[i].setText(input);
		}
	}

	public static void etClear(EditText... ets) {
		for (int i = 0; i < ets.length; i++) {
			ets[i].setText("");
			setHistoryEtMap(String.valueOf(ets[i].getId()), "");
		}

	}

	public static void etClear(String key, EditText... ets) {
		for (int i = 0; i < ets.length; i++) {
			ets[i].setText("");
			setHistoryEtMap(String.valueOf(ets[i].getId()) + key, "");
		}
	}
}
