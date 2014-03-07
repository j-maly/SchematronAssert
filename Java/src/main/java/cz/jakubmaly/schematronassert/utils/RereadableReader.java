package cz.jakubmaly.schematronassert.utils;

import java.io.*;

import org.apache.commons.io.*;

public class RereadableReader extends Reader {
	private Reader underlyingReader;

	public RereadableReader(Reader underlyingReader) throws IOException {
		if (underlyingReader.markSupported()) {
			this.underlyingReader = underlyingReader;
			this.underlyingReader.mark(Integer.MAX_VALUE);
		} else {
			throw new IOException();
		}
	}

	@Override
	public void close() throws IOException {
		underlyingReader.reset();
	}

	public void closeUnderlying() throws IOException {
		IOUtils.closeQuietly(underlyingReader);
	}

	@Override
	public int read() throws IOException {
		return underlyingReader.read();
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return underlyingReader.read(cbuf, off, len);
	}
}
