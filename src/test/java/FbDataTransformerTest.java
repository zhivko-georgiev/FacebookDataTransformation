import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.junit.Rule;
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

	@Rule
	public EETRule eetRule = new EETRule();

	@Test
	public void testTransformationOfMatchedFbPostComment() {
		try {
			FbPostComment fbPostComment = new FbPostComment("1916334385254169_1916334465254161", "Good morning",
					fbDateFormat.parse("Thu Feb 02 09:46:37 EET 2017"));
			String expectedTransformedFbPostComment = "{\"test\":{\"id\":\"1916334385254169_1916334465254161\",\"message\":\"Good morning\",\"created_time\":\"2017-02-02T09:46:37+0200\"}}";
			List<String> transformedFbPostComment = transformer.transform(new ArrayList<>(Arrays.asList(fbPostComment)));

			assertEquals(expectedTransformedFbPostComment, transformedFbPostComment.get(0));
		} catch (ParseException exception) {
			logger.error("Something went wrong during date parsing of the mocked comments: ", exception);
		}
	}
	
	@Test
	public void testTransformationOfUnMatchedFbPostComment() {
		try {
			FbPostComment fbPostComment = new FbPostComment("1913261462228128_1913261495561458", "and it comes realtime!!!",
					fbDateFormat.parse("Thu Jan 26 14:54:21 EET 2017"));
			String expectedTransformedFbPostComment = "{\"id\":\"1913261462228128_1913261495561458\",\"message\":\"and it comes realtime!!!\",\"created_time\":\"2017-01-26T14:54:21+0200\"}";
			List<String> transformedFbPostComment = transformer.transform(new ArrayList<>(Arrays.asList(fbPostComment)));

			assertEquals(expectedTransformedFbPostComment, transformedFbPostComment.get(0));
		} catch (ParseException exception) {
			logger.error("Something went wrong during date parsing of the mocked comments: ", exception);
		}
	}
	
	@Test
	public void testTransformationOfMatchedAndUmatchedFbPostComment() {
		try {
			FbPostComment matchedfbPostComment = new FbPostComment("1916334385254169_1916334465254161", "Good morning",
					fbDateFormat.parse("Thu Feb 02 09:46:37 EET 2017"));
			FbPostComment notMatchedfbPostComment = new FbPostComment("1913261462228128_1913261495561458", "and it comes realtime!!!",
					fbDateFormat.parse("Thu Jan 26 14:54:21 EET 2017"));

			String expectedMatchedTransformedFbPostComment = "{\"test\":{\"id\":\"1916334385254169_1916334465254161\",\"message\":\"Good morning\",\"created_time\":\"2017-02-02T09:46:37+0200\"}}";
			String expectedNotmatchedTransformedFbPostComment = "{\"id\":\"1913261462228128_1913261495561458\",\"message\":\"and it comes realtime!!!\",\"created_time\":\"2017-01-26T14:54:21+0200\"}";

			List<String> transformedFbPostComment = transformer.transform(new ArrayList<>(Arrays.asList(matchedfbPostComment, notMatchedfbPostComment)));

			assertEquals(expectedNotmatchedTransformedFbPostComment, transformedFbPostComment.get(0));
			assertEquals(expectedMatchedTransformedFbPostComment, transformedFbPostComment.get(1));
			
		} catch (ParseException exception) {
			logger.error("Something went wrong during date parsing of the mocked comments: ", exception);
		}
	}
	
	@Test(expected = NullPointerException.class)
	public void testTransformationOfFbPostCommentWithNullArg() {
		try {
			FbPostComment fbPostComment = new FbPostComment("1913261462228128_1913261495561458", null,
					fbDateFormat.parse("Thu Jan 26 14:54:21 EET 2017"));
			transformer.transform(new ArrayList<>(Arrays.asList(fbPostComment)));
		} catch (ParseException exception) {
			logger.error("Something went wrong during date parsing of the mocked comments: ", exception);
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testTransformationOfFbPostCommentWithEmptyStringArg() {
		try {
			FbPostComment fbPostComment = new FbPostComment("1913261462228128_1913261495561458", "",
					fbDateFormat.parse("Thu Jan 26 14:54:21 EET 2017"));
			transformer.transform(new ArrayList<>(Arrays.asList(fbPostComment)));
		} catch (ParseException exception) {
			logger.error("Something went wrong during date parsing of the mocked comments: ", exception);
		}
	}
}
