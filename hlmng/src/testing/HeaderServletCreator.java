package testing;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

public class HeaderServletCreator {

	/**
	 * We need this class to mock a login
	 */
	public static HttpServletResponse returnServletResponse() {
		return new HttpServletResponse() {
			@Override
			public void setLocale(Locale arg0) {

			}

			@Override
			public void setContentType(String arg0) {

			}

			@Override
			public void setContentLength(int arg0) {

			}

			@Override
			public void setCharacterEncoding(String arg0) {

			}

			@Override
			public void setBufferSize(int arg0) {

			}

			@Override
			public void resetBuffer() {

			}

			@Override
			public void reset() {

			}

			@Override
			public boolean isCommitted() {

				return false;
			}

			@Override
			public PrintWriter getWriter() throws IOException {

				return null;
			}

			@Override
			public ServletOutputStream getOutputStream() throws IOException {

				return null;
			}

			@Override
			public Locale getLocale() {

				return null;
			}

			@Override
			public String getContentType() {

				return null;
			}

			@Override
			public String getCharacterEncoding() {

				return null;
			}

			@Override
			public int getBufferSize() {

				return 0;
			}

			@Override
			public void flushBuffer() throws IOException {

			}

			@Override
			public void setStatus(int arg0, String arg1) {

			}

			@Override
			public void setStatus(int arg0) {

			}

			@Override
			public void setIntHeader(String arg0, int arg1) {

			}

			@Override
			public void setHeader(String arg0, String arg1) {

			}

			@Override
			public void setDateHeader(String arg0, long arg1) {

			}

			@Override
			public void sendRedirect(String arg0) throws IOException {

			}

			@Override
			public void sendError(int arg0, String arg1) throws IOException {

			}

			@Override
			public void sendError(int arg0) throws IOException {

			}

			@Override
			public int getStatus() {

				return 0;
			}

			@Override
			public Collection<String> getHeaders(String arg0) {

				return null;
			}

			@Override
			public Collection<String> getHeaderNames() {

				return null;
			}

			@Override
			public String getHeader(String arg0) {

				return null;
			}

			@Override
			public String encodeUrl(String arg0) {

				return null;
			}

			@Override
			public String encodeURL(String arg0) {

				return null;
			}

			@Override
			public String encodeRedirectUrl(String arg0) {

				return null;
			}

			@Override
			public String encodeRedirectURL(String arg0) {

				return null;
			}

			@Override
			public boolean containsHeader(String arg0) {

				return false;
			}

			@Override
			public void addIntHeader(String arg0, int arg1) {

			}

			@Override
			public void addHeader(String arg0, String arg1) {

			}

			@Override
			public void addDateHeader(String arg0, long arg1) {

			}

			@Override
			public void addCookie(javax.servlet.http.Cookie arg0) {

			}
		};
	}

	public static HttpHeaders returnHTTPHeaders(final String headersString) {
		return new HttpHeaders() {
			@Override
			public String getHeaderString(String arg0) {
				return headersString;
			}

			@Override
			public List<Locale> getAcceptableLanguages() {
				return null;
			}

			@Override
			public List<MediaType> getAcceptableMediaTypes() {
				return null;
			}

			@Override
			public Map<String, Cookie> getCookies() {
				return null;
			}

			@Override
			public Date getDate() {
				return null;
			}

			@Override
			public Locale getLanguage() {
				return null;
			}

			@Override
			public int getLength() {
				return 0;
			}

			@Override
			public MediaType getMediaType() {
				return null;
			}

			@Override
			public List<String> getRequestHeader(String arg0) {
				return null;
			}

			@Override
			public MultivaluedMap<String, String> getRequestHeaders() {
				return null;
			}
		};

	}
}
