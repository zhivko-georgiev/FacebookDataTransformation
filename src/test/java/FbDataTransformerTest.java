import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.sentiment.common.Constants;
import com.sentiment.data.transformer.DataTransformer;
import com.sentiment.data.transformer.FbDataTransformer;
import com.sentiment.model.FbPostComment;
import com.sentiment.util.PropertiesUtil;

public class FbDataTransformerTest {
	private static final Logger logger = Logger.getLogger(FbDataTransformerTest.class);
	private static final DateFormat fbDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
	private final Properties appTestProps = PropertiesUtil.loadPropertiesFile(Constants.APP_PROPS_TEST_FILENAME);
	
	private DataTransformer<FbPostComment> transformer = new FbDataTransformer(appTestProps);
	private List<FbPostComment> mockedComments;
	private List<String> mockedTransformedComments;
	
	@Before
    public void setUp() {
		mockedComments = generateFbPostsComments();
		mockedTransformedComments = generateTransformedFbPostsComments();
    }

	@Test
	public void testFbPostsCommentsTransformation() {
		List<String> transformedComments = transformer.transform(mockedComments);
		
		for (int i = 0; i < transformedComments.size(); i++) {
			String transformedComment = transformedComments.get(i);
			String mockedTransformedComment = mockedTransformedComments.get(i);
			assertEquals(mockedTransformedComment, transformedComment);
		}
	}
	
	private List<FbPostComment> generateFbPostsComments() {
		List<FbPostComment> mockedComments = new ArrayList<>();
		
		try {
			mockedComments.add(new FbPostComment("1913261462228128_1913261495561458", "and it comes realtime!!!", fbDateFormat.parse("Thu Jan 26 14:54:21 EET 2017")));
			mockedComments.add(new FbPostComment("1916334385254169_1916334465254161", "Good morning", fbDateFormat.parse("Thu Feb 02 09:46:37 EET 2017")));
			mockedComments.add(new FbPostComment("1915852541969020_1916433698577571", "test reply", fbDateFormat.parse("Thu Feb 02 14:40:58 EET 2017")));
		} catch (ParseException exception) {
			logger.error("Something went wrong during date parsing of the mocked comments: ", exception);
		}
		
		return mockedComments;
	}
	
	private List<String> generateTransformedFbPostsComments() {
		List<String> mockedTransformedComments = new ArrayList<>();
		
		mockedTransformedComments.add("{\"id\":\"1913261462228128_1913261495561458\",\"message\":\"and it comes realtime!!!\",\"created_time\":\"2017-01-26T14:54:21+0200\"}");
		mockedTransformedComments.add("{\"test\":{\"id\":\"1916334385254169_1916334465254161\",\"message\":\"Good morning\",\"created_time\":\"2017-02-02T09:46:37+0200\"}}");
		mockedTransformedComments.add("{\"test\":{\"id\":\"1915852541969020_1916433698577571\",\"message\":\"test reply\",\"created_time\":\"2017-02-02T14:40:58+0200\"}}");
		
		return mockedTransformedComments;
	}
}
