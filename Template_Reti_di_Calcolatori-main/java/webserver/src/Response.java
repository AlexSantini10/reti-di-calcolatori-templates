package source.scratch.java.webserver.src;

import java.io.*;

public class Response {

	public File path;
	public int replyCode;

	public Response(File path, int replyCode) {
		this.path = path;
		this.replyCode = replyCode;
	}

}
