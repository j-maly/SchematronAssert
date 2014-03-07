package cz.jakubmaly.schematronassert.utils;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.*;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

@RunWith(MockitoJUnitRunner.class)
public class RereadableStreamTest {

	@Mock
	private InputStream underlyingStream;

	private RereadableStream rereadableStream;

	@Before
	public void setUp() throws IOException {
		when(underlyingStream.markSupported()).thenReturn(true);
		rereadableStream = new RereadableStream(underlyingStream);
	}

	@Test
	public void testClose() throws Exception {
		// ACT
		rereadableStream.close();
		// ASSERT 
		verify(underlyingStream, times(1)).reset();
	}

	@Test
	public void testRead() throws Exception {
		// ARRANGE 
		when(underlyingStream.read()).thenReturn(54235);
		// ACT
		int read = rereadableStream.read();
		// ASSERT 
		assertThat(read).isEqualTo(54235);
	}

	@Test
	public void testCloseUnderlying() throws Exception {
		// ACT
		rereadableStream.closeUnderlying();
		// ASSERT 
		verify(underlyingStream, times(1)).close();
	}

	@SuppressWarnings("resource")
	@Test(expected = IOException.class)
	public void testCreateRereadableSource_from_stream_fails_on_stream_not_supporting_mark() throws Exception {
		// ARRANGE
		InputStream forwardOnlyStream = mock(InputStream.class);
		when(forwardOnlyStream.markSupported()).thenReturn(false);
		// ACT
		new RereadableStream(forwardOnlyStream);
	}

}
