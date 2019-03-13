package com.weqia.utils.http.okgo.model;

import com.weqia.utils.StrUtil;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;

public class RequestParams implements Serializable {

	private static final long serialVersionUID = 7369819159227055048L;

	public static final MediaType MEDIA_TYPE_PLAIN = MediaType
			.parse("text/plain;charset=utf-8");
	public static final MediaType MEDIA_TYPE_JSON = MediaType
			.parse("application/json;charset=utf-8");
	public static final MediaType MEDIA_TYPE_STREAM = MediaType
			.parse("application/octet-stream");

	public static final boolean IS_REPLACE = true;

	/** 普通的键值对参数 */
	public LinkedHashMap<String, String> urlParams;

	/** 文件的键值对参数 */
	public LinkedHashMap<String, FileWrapper> fileParams;

	public RequestParams() {
		init();
	}

	public RequestParams(String key, String value) {
		init();
		put(key, value);
	}

	public RequestParams(String key, File file) {
		init();
		put(key, file);
	}

	private void init() {
		urlParams = new LinkedHashMap<>();
		fileParams = new LinkedHashMap<>();
	}

	public void put(RequestParams params) {
		if (params != null) {
			if (params.urlParams != null && !params.urlParams.isEmpty())
				urlParams.putAll(params.urlParams);
			if (params.fileParams != null && !params.fileParams.isEmpty())
				fileParams.putAll(params.fileParams);
		}
	}

	public void put(Map<String, String> params) {
		if (params == null || params.isEmpty())
			return;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	public void put(String key, String value) {
//		put(key, String.valueOf(value));
		urlParams.put(key, value);
	}

	public void put(String key, int value) {
		put(key, String.valueOf(value));
	}

	public void put(String key, long value) {
		put(key, String.valueOf(value));
	}

	public void put(String key, float value) {
		put(key, String.valueOf(value));
	}

	public void put(String key, double value) {
		put(key, String.valueOf(value));
	}

	public void put(String key, char value) {
		put(key, String.valueOf(value));
	}

	public void put(String key, boolean value) {
		put(key, String.valueOf(value));
	}

	// public void putUrlParams(String key, List<String> values) {
	// if (key != null && values != null && !values.isEmpty()) {
	// for (String value : values) {
	// put(key, value, false);
	// }
	// }
	// }

	public void put(String key, File file) {
		put(key, file, file.getName());
	}

	public void put(String key, File file, String fileName) {
		put(key, file, fileName, guessMimeType(fileName));
	}

	public void put(String key, FileWrapper fileWrapper) {
		if (key != null && fileWrapper != null) {
			put(key, fileWrapper.file, fileWrapper.fileName,
					fileWrapper.contentType);
		}
	}

	public void put(String key, File file, String fileName,
			MediaType contentType) {
		if (key != null) {
			fileParams.put(key, new FileWrapper(file, fileName, contentType));
		}
	}
	
	public void put(String key, InputStream inputStream, String fileName, String contentType) {
        if (key != null) {
			if (StrUtil.isEmptyOrNull(contentType))
				contentType = "application/octet-stream";
			fileParams.put(key, new FileWrapper(inputStream, fileName, MediaType.parse(contentType)));
		}
    }

	public void putFileParams(String key, List<File> files) {
		if (key != null && files != null && !files.isEmpty()) {
			for (File file : files) {
				put(key, file);
			}
		}
	}

	public void putFileWrapperParams(String key, List<FileWrapper> fileWrappers) {
		if (key != null && fileWrappers != null && !fileWrappers.isEmpty()) {
			for (FileWrapper fileWrapper : fileWrappers) {
				put(key, fileWrapper);
			}
		}
	}

	public void removeUrl(String key) {
		urlParams.remove(key);
	}

	public void removeFile(String key) {
		fileParams.remove(key);
	}

	public void remove(String key) {
		removeUrl(key);
		removeFile(key);
	}

	public void clear() {
		urlParams.clear();
		fileParams.clear();
	}

	private MediaType guessMimeType(String path) {
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		path = path.replace("#", ""); // 解决文件名中含有#号异常的问题
		String contentType = fileNameMap.getContentTypeFor(path);
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		return MediaType.parse(contentType);
	}

	/** 文件类型的包装类 */
	public static class FileWrapper {
		public File file;
		public String fileName;
		public MediaType contentType;
		public long fileSize;
		public InputStream inputStream;

		public FileWrapper(File file, String fileName, MediaType contentType) {
			this.file = file;
			this.fileName = fileName;
			this.contentType = contentType;
			this.fileSize = file.length();
		}
		
		public FileWrapper(InputStream inputStream, String fileName, MediaType contentType) {
			this.inputStream = inputStream;
			this.fileName = fileName;
			this.contentType = contentType;
//			this.fileSize = inputStream.length();
		}

		@Override
		public String toString() {
			return "FileWrapper{" + "file=" + file + ", fileName='" + fileName
					+ ", contentType=" + contentType + ", fileSize=" + fileSize
					+ '}';
		}
	}

	@Override
	public String toString() {
		return urlParams.toString();
	}
}