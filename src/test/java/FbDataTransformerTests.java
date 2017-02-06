import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.sentiment.model.FbPostComment;

public class FbDataTransformerTests {
	private static final Logger logger = Logger.getLogger(FbDataTransformerTests.class);
	private static final DateFormat fbDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
	
	private List<FbPostComment> mockedComments;
	
	@Before
    public void setUp() {
		mockedComments = generateFbPostsComments();
    }

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	private List<FbPostComment> generateFbPostsComments() {
		List<FbPostComment> mockedComments = new ArrayList<>();
		
		try {
			mockedComments.add(new FbPostComment("1916334385254169_1916334465254161", "Good morning", fbDateFormat.parse("Thu Feb 02 11:46:37 EET 2017")));
			mockedComments.add(new FbPostComment("1915852541969020_1916433698577571", "test reply", fbDateFormat.parse("Thu Feb 02 16:40:58 EET 2017")));
			mockedComments.add(new FbPostComment("1913261462228128_1913261495561458", "and it comes realtime!!!", fbDateFormat.parse("Thu Jan 26 16:54:21 EET 2017")));
			mockedComments.add(new FbPostComment("1913261462228128_1913262698894671", "another strange TeSt message", fbDateFormat.parse("Thu Jan 26 16:59:31 EET 2017")));
			mockedComments.add(new FbPostComment("1913261462228128_1913262712228003", "testy mesty", fbDateFormat.parse("Thu Jan 26 16:59:37 EET 2017")));
			mockedComments.add(new FbPostComment("1906842419536699_1912175849003356", "Happy not friday 13th", fbDateFormat.parse("Tue Jan 24 17:32:04 EET 2017")));
			mockedComments.add(new FbPostComment("1906842419536699_1912175932336681", "Oh, yet another test message", fbDateFormat.parse("Tue Jan 24 17:32:30 EET 2017")));
			mockedComments.add(new FbPostComment("1906842419536699_1914928008728140", "hello, mike", fbDateFormat.parse("Mon Jan 30 11:20:11 EET 2017")));
			mockedComments.add(new FbPostComment("1906842419536699_1914928055394802", "good morning", fbDateFormat.parse("Mon Jan 30 11:20:21 EET 2017")));
			mockedComments.add(new FbPostComment("664929053668776_677949559033392", "comment com", fbDateFormat.parse("Mon Jan 09 11:58:14 EET 2017")));
		} catch (ParseException exception) {
			logger.error("Something went wrong during date parsing of the mocked comments: ", exception);
		}
		
		return mockedComments;
	}
}
