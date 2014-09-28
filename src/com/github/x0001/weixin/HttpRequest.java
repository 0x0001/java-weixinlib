package com.github.x0001.weixin;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

/**
 * @author 0x0001
 */
public final class HttpRequest {
	private String url;
	private String encoder = "UTF-8";
	private boolean isPost = false;
	private final Map<String, List<String>> attrs = new HashMap<String, List<String>>();
	private final Map<String, List<File>> files = new HashMap<String, List<File>>();
	private final Map<String, String> requestProperty;

	private boolean followRedict = false;

	private HttpURLConnection conn;
//	static {
//		CookieManager.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
//	}

	private HttpRequest(String url) {
		requestProperty = new HashMap<String, String>();
		requestProperty.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.52 Safari/537.17");
		requestProperty.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		this.url = url;
	}

	public static HttpRequest get(String url) {
		return new HttpRequest(url).setGet();
	}

	public static HttpRequest post(String url) {
		return new HttpRequest(url).setPost();
	}

	public HttpRequest setFollowRedirects(boolean follow) {
		this.followRedict = follow;
		return this;
	}

	public HttpRequest addAttr(String key, String value) {
		List<String> list = attrs.get(key);
		if (null == list) {
			list = new ArrayList<String>();
			attrs.put(key, list);
		}
		list.add(String.valueOf(value));
		return this;
	}

	public HttpRequest setHeader(String key, String value) {
		requestProperty.put(key, value);
		return this;
	}

	public HttpRequest addAttr(String key, File file) {
		if (!file.exists()) {
			throw new IllegalArgumentException("file not exists : " + file.getName());
		}
		List<File> list = files.get(key);
		if (null == list) {
			list = new ArrayList<File>();
			files.put(key, list);
		}
		list.add(file);
		return this;
	}

	public HttpRequest setReferer(String referer) {
		return setHeader("Referer", referer);
	}

	public HttpRequest setEncoder(String encoder) {
		this.encoder = encoder;
		return this;
	}

	public HttpRequest setRequestWithAjax() {
		return setHeader("X-Requested-With", "XMLHttpRequest");
	}

	/**
	 * 请求服务器获取返回的字符串(每次调用都会重新发送请求)
	 *
	 * @return
	 * @throws java.io.IOException
	 */
	public String getResponse() throws IOException {
		InputStream in = getInputStream();
		StringBuilder response = new StringBuilder();
		String line;
		BufferedReader br = new BufferedReader(new InputStreamReader(in, encoder));
		while ((line = br.readLine()) != null) {
			response.append(line).append("\n");
		}
		// 去掉最后多拼上去的换行
		response.setLength(response.length() - 1);
		return response.toString();
	}

	/**
	 * 消耗请求返回的内容
	 *
	 * @throws java.io.IOException
	 */
	public void consume() throws IOException {
		InputStream in = getInputStream();
		while (in.read() != -1) /*empty body*/ ;
		in.close();
	}

	/**
	 * 只读取head,不读取内容
	 *
	 * @throws java.io.IOException
	 */
	public void consumeHead() throws IOException {
		getInputStream().close();
	}

	private InputStream in;

	/**
	 * 如果响应代码不为200则返回错误流
	 *
	 * @return
	 * @throws java.io.IOException
	 */
	public InputStream getInputStream() throws IOException {
		if(null != in) {
			return in;
		}

		if (!isPost && isFileUpLoad()) {
			throw new IllegalArgumentException("GET request can not support file upload!");
		}

		sendData();

		if (getURLConnection().getResponseCode() != HttpURLConnection.HTTP_OK) {
			in = getURLConnection().getErrorStream();
		} else {
			in = getURLConnection().getInputStream();
		}

		String encode = getURLConnection().getContentEncoding() == null ? "" : getURLConnection().getContentEncoding().toLowerCase();
		if ("gzip".equals(encode)) {
			return in = new GZIPInputStream(in);
		}
		return in;
	}

	public HttpURLConnection getURLConnection() throws IOException {
		buildConnection();
		return this.conn;
	}

	public String getUrl() {
		return url;
	}

	// ---------------------------------------------------------- Private Methods

	private void sendData() throws IOException {
		if (isPost) {
			if (isFileUpLoad()) {
				mutilPartPost();
			} else {
				plainPost();
			}
		}
	}

	private void buildConnection() throws IOException {
		while (null == conn) {
			HttpURLConnection conn = newConnection();
			if (followRedict) {
				int responseCode = conn.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == HttpURLConnection.HTTP_MOVED_PERM) {
					String url = conn.getHeaderField("location");
					this.url = url;
					// 重定向以后,不需要再发送数据过去, 并且都是get请求
					setGet();
					attrs.clear();
					files.clear();
					continue;
				}
			}
			this.conn = conn;
			return;
		}
	}


	private void plainPost() throws IOException {
		buildConnection();
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		String requestArgs = attr2UrlString();
		if (!requestArgs.isEmpty()) {
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.getOutputStream().write(requestArgs.getBytes());
		}
	}

	private void mutilPartPost() throws IOException {
		buildConnection();
		String boundary = "--------" + UUID.randomUUID().toString();
		conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

		LengthOutputStream lengthOut = new LengthOutputStream();
		writeMultiPart(lengthOut, boundary);
		conn.setFixedLengthStreamingMode(lengthOut.getLength());
		conn.setDoInput(true);
		conn.setDoOutput(true);

		OutputStream out = new BufferedOutputStream(conn.getOutputStream());
		writeMultiPart(out, boundary);
		out.flush();

	}

	private void writeMultiPart(OutputStream out, String boundary) throws IOException {

		byte[] buf = new byte[512];
		int bufLen;
		for (Entry<String, List<File>> entry : files.entrySet()) {
			String key = entry.getKey();
			for (File file : entry.getValue()) {
				String contentType = "application/octet-stream";//"text/x-"+file.getName();
				out.write(("--" + boundary + "\r\n").getBytes());
				out.write(("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + file.getName() + "\"\r\n").getBytes());
				out.write(("Content-Type: " + contentType + "\r\n\r\n").getBytes());

				InputStream in = null;
				try {
					in = new FileInputStream(file);
					while ((bufLen = in.read(buf)) != -1) {
						out.write(buf, 0, bufLen);
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				} finally {
					if (null != in) {
						in.close();
					}
				}
				out.write("\r\n".getBytes());
			}
		}

		if (!attrs.isEmpty()) {
			for (Entry<String, List<String>> entry : attrs.entrySet()) {
				String key = entry.getKey();
				for (String value : entry.getValue()) {
					out.write(("--" + boundary + "\r\n").getBytes());
					out.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n").getBytes());
					out.write((value + "\r\n").getBytes());
				}
			}
		}
		out.write(("--" + boundary + "--").getBytes());
	}

	private boolean isFileUpLoad() {
		return !files.isEmpty();
	}


	private HttpURLConnection newConnection() throws IOException {

		String url = this.url;
		String method = "POST";
		if (!isPost) {
			method = "GET";
			String requestArgs = attr2UrlString();
			if (!requestArgs.isEmpty()) {
				if (url.contains("?")) {
					url = url + "&" + requestArgs;
				} else {
					url = url + "?" + requestArgs;
				}
			}
		}


		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
		con.setRequestMethod(method);
		for (Entry<String, String> entry : requestProperty.entrySet()) {
			con.setRequestProperty(entry.getKey(), entry.getValue());
		}
		if (url.toLowerCase().startsWith("https")) {
			final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
				@Override
				public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
//					System.out.println("checkClientTrusted: chain = [" + Arrays.toString(chain) + "], authType = [" + authType + "]");
				}

				@Override
				public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
//					System.out.println("checkServerTrusted: chain = [" + Arrays.toString(chain) + "], authType = [" + authType + "]");
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			}};
			try {
				SSLContext sslContext = SSLContext.getInstance("SSL");
				sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
				final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
				((HttpsURLConnection) con).setSSLSocketFactory(sslSocketFactory);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (KeyManagementException e) {
				e.printStackTrace();
			}
		}
		return con;
	}

	private HttpRequest setGet() {
		isPost = false;
		return this;
	}

	private HttpRequest setPost() {
		isPost = true;
		return this;
	}

	private String attr2UrlString() {
		if (attrs.isEmpty()) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (Entry<String, List<String>> entry : attrs.entrySet()) {
			String key = entry.getKey();
			for (String value : entry.getValue()) {
				if (first) {
					first = false;
				} else {
					builder.append("&");
				}
				builder.append(key).append("=").append(encode(value));
			}
		}
		return builder.toString();
	}

	private String encode(String value) {
		try {
			return URLEncoder.encode(value, encoder);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return value;
	}

	static final class LengthOutputStream extends OutputStream {
		private int length = 0;

		@Override
		public void write(int b) throws IOException {
			length++;
		}

		public int getLength() {
			return length;
		}
	}
}
