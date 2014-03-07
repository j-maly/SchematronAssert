package cz.jakubmaly.schematronassert.utils;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.*;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

@RunWith(MockitoJUnitRunner.class)
public class RereadableReaderTest {

	@Mock
	private Reader underlyingReader;

	private RereadableReader rereadableReader;

	@Before
	public void setUp() throws IOException {
		when(underlyingReader.markSupported()).thenReturn(true);
		rereadableReader = new RereadableReader(underlyingReader);
	}

	@Test
	public void testClose() throws Exception {
		// ACT
		rereadableReader.close();
		// ASSERT 
		verify(underlyingReader, times(1)).reset();
	}

	@Test
	public void testRead() throws Exception {
		// ARRANGE 
		when(underlyingReader.read()).thenReturn(54235);
		// ACT
		int read = rereadableReader.read();
		// ASSERT 
		assertThat(read).isEqualTo(54235);
	}

	@Test
	public void testCloseUnderlying() throws Exception {
		// ACT
		rereadableReader.closeUnderlying();
		// ASSERT 
		verify(underlyingReader, times(1)).close();
	}

	@SuppressWarnings("resource")
	@Test(expected = IOException.class)
	public void testCreateRereadableSource_from_reader_fails_on_reader_not_supporting_mark() throws Exception {
		// ARRANGE
		Reader forwardOnlyReader = mock(Reader.class);
		when(forwardOnlyReader.markSupported()).thenReturn(false);
		// ACT
		new RereadableReader(forwardOnlyReader);
	}
}
