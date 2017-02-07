import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sentiment.common.Constants;
import com.sentiment.data.transformer.DataTransformer;
import com.sentiment.data.transformer.FbDataTransformer;
import com.sentiment.model.FbPostComment;
import com.sentiment.util.PropertiesUtil;

public class FbDataTransformerTest {
	private static final LocalDateTime MATCHED_FBPOSTCOMMENT_DATE = LocalDateTime.of(2017, 2, 2, 9, 46, 37);
	private static final LocalDateTime UNMATCHED_FBPOSTCOMMENT_DATE = LocalDateTime.of(2017, 1, 26, 14, 54, 21);
	private static final ZoneId EET_TIME_ZONE = ZoneId.of("Europe/Sofia");
	private static final String EET_TIME_ZONE_STRING = "Europe/Sofia";

	private final FbPostComment MATCHED_FBPOSTCOMMENT = new FbPostComment("1916334385254169_1916334465254161",
			"Good morning", Date.from(MATCHED_FBPOSTCOMMENT_DATE.atZone(EET_TIME_ZONE).toInstant()));
	private static final FbPostComment UNMATCHED_FBPOSTCOMMENT = new FbPostComment("1913261462228128_1913261495561458",
			"and it comes realtime!!!", Date.from(UNMATCHED_FBPOSTCOMMENT_DATE.atZone(EET_TIME_ZONE).toInstant()));

	private static final String MATCHED_TRANSFORMED_FBPOSTCOMMENT = "{\"test\":{\"id\":\"1916334385254169_1916334465254161\",\"message\":\"Good morning\",\"created_time\":\"2017-02-02T09:46:37+0200\"}}";
	private static final String UNMATCHED_TRANSFORMED_FB_POST_COMMENT = "{\"id\":\"1913261462228128_1913261495561458\",\"message\":\"and it comes realtime!!!\",\"created_time\":\"2017-01-26T14:54:21+0200\"}";

	private static String originalTimeZone;

	private final Properties appTestProps = PropertiesUtil.loadPropertiesFile(Constants.APP_PROPS_TEST_FILENAME);
	private final DataTransformer<FbPostComment> transformer = new FbDataTransformer(appTestProps);

	@BeforeClass
	public static void setAppropriateTimeZone() {
		originalTimeZone = System.getProperty("user.timezone");
		System.setProperty("user.timezone", EET_TIME_ZONE_STRING);
	}

	@AfterClass
	public static void returnBackTheOriginalTimeZone() {
		System.setProperty("user.timezone", originalTimeZone);
	}

	@Test
	public void testTransformationOfMatchedFbPostComment() {
		List<String> transformedFbPostComment = transformer
				.transform(new ArrayList<>(Arrays.asList(MATCHED_FBPOSTCOMMENT)));
		assertEquals(MATCHED_TRANSFORMED_FBPOSTCOMMENT, transformedFbPostComment.get(0));
	}

	@Test
	public void testTransformationOfUnMatchedFbPostComment() {
		List<String> transformedFbPostComment = transformer
				.transform(new ArrayList<>(Arrays.asList(UNMATCHED_FBPOSTCOMMENT)));
		assertEquals(UNMATCHED_TRANSFORMED_FB_POST_COMMENT, transformedFbPostComment.get(0));
	}

	@Test
	public void testTransformationOfMatchedAndUmatchedFbPostComment() {
		List<String> transformedFbPostComment = transformer
				.transform(new ArrayList<>(Arrays.asList(MATCHED_FBPOSTCOMMENT, UNMATCHED_FBPOSTCOMMENT)));

		assertEquals(UNMATCHED_TRANSFORMED_FB_POST_COMMENT, transformedFbPostComment.get(0));
		assertEquals(MATCHED_TRANSFORMED_FBPOSTCOMMENT, transformedFbPostComment.get(1));
	}

	@Test(expected = NullPointerException.class)
	public void testTransformationOfFbPostCommentWithNullArg() {
		FbPostComment fbPostComment = new FbPostComment("1913261462228128_1913261495561458", null,
				Date.from(UNMATCHED_FBPOSTCOMMENT_DATE.atZone(EET_TIME_ZONE).toInstant()));
		transformer.transform(new ArrayList<>(Arrays.asList(fbPostComment)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTransformationOfFbPostCommentWithEmptyStringArg() {
		FbPostComment fbPostComment = new FbPostComment("1913261462228128_1913261495561458", "",
				Date.from(UNMATCHED_FBPOSTCOMMENT_DATE.atZone(EET_TIME_ZONE).toInstant()));
		transformer.transform(new ArrayList<>(Arrays.asList(fbPostComment)));
	}
}
