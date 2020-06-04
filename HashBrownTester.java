package FinalProject_Template;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
/**
 * Expanded tester. Assumes stopWords.txt is setup properly (should be if {@link HashTableStressTester} is setup properly).
 * @author George
 */
public class HashBrownTester {
	//ATTRIBUTES

	//Debug tweetList creation parameters
	private int maxDebugEntries;
	private int testTimes = 12;
	private int multiplier = 3000;
	
	//ArrayLists from which to create hash tables
	private ArrayList<String> stopWordsDebug;
	private ArrayList<String> stopWordsReal;
	private ArrayList<String> correctTrendingDebug;
	private ArrayList<Tweet> tweetListDefault;
	private ArrayList<Tweet> tweetListDebug;
	
	//Created hash tables for debugging and avoiding rebuilding each time
	/** Original test table. Keys: dates, Values: tweets */
	private MyHashTable<String, Tweet> tweetTableDefault;
	/** New test table. Keys: authors, Values: tweets */
	private MyHashTable<String, Tweet> tweetTableDebug;
	/** Simple table to test remove and put... */
	private MyHashTable<String, String> tableSimple;
	
	//Creating Twitters to stress test twitter();
	private Twitter smallTwitter;
	private Twitter mediumTwitter;
	private Twitter bigTwitter;
	private ArrayList<Twitter> incrementalTwitters;
	
	//Twitter related
	private Twitter defaultTwitter;

	private HashBrownTester() {
		
		System.out.println("This tester assumes stopWords.txt is placed at the root of the project folder...\n");
		
		this.maxDebugEntries = 2000;
		
		//Creating tweetLists
		this.tweetListDefault = generateTweetListDefault();
		this.tweetListDebug = generateTweetListDebug(2000);
		
		//Initializing incremental twitter
		this.incrementalTwitters = new ArrayList<Twitter>();
		
		//Creating debug stopWordList
		this.stopWordsDebug = new ArrayList<String>();
		this.stopWordsDebug.add("messageOfAuthor");
		this.stopWordsDebug.add("stopWordOne");
		this.stopWordsDebug.add("stopWordTwo");
		this.stopWordsDebug.add("stopWordThree");
		this.stopWordsDebug.add("stopWordFour");
		this.stopWordsDebug.add("stopWordFive");
		
		//Creating correct debug trending
		this.correctTrendingDebug = new ArrayList<String>();
		this.correctTrendingDebug.add("mostPopular");
		this.correctTrendingDebug.add("secondPopular");
		this.correctTrendingDebug.add("thirdPopular");
		this.correctTrendingDebug.add("fourthPopular");
		this.correctTrendingDebug.add("fifthPopular");
		this.correctTrendingDebug.add("sixthPopular");
		this.correctTrendingDebug.add("seventhPopular");
		this.correctTrendingDebug.add("eighthPopular");
		
		//Creating real stopWordList
		try {
			
			this.stopWordsReal = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new FileReader("stopWords.txt"));
			
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				this.stopWordsReal.add(currentLine);
			}
			br.close();
			
			System.out.println("Stop words loaded without issue.\n");
		} catch (IOException e) {
			System.out.println("Could not load stopWords.txt. Proceeding without...\n");
			e.printStackTrace();
		}
		
		System.out.println("Tests with index 0 were in the original tester.\n");
	}
	
	/** Replaces System.out.println() because laziness */
	private static void write(String string) {
		System.out.print(string + "\n");
	}
	
	@SuppressWarnings("unused")
	private static double log2(double x) {
		return Math.log(x) / Math.log(2);
	}
	
	private void resetTables() {
		this.tweetTableDefault = new MyHashTable<String, Tweet>(7);
		for (Tweet t : generateTweetListDefault()) {
			this.tweetTableDefault.put(t.getDateAndTime(), t);
		}
		this.tweetTableDebug = new MyHashTable<String, Tweet>(7);
		for (Tweet t : generateTweetListDebug(2000)) {
			this.tweetTableDebug.put(t.getAuthor(), t);
		}
		this.tableSimple = new MyHashTable<String, String>(1);
		for (int i = 0; i < 500; i++) {
			this.tableSimple.put("Key" + i, "Value" + i);
		}
	}
	
	private void resetTwitters(boolean emptyStopWords) {
		this.smallTwitter = new Twitter(generateTweetListDebug(4000), this.stopWordsDebug);
		this.mediumTwitter = new Twitter(generateTweetListDebug(8000), this.stopWordsDebug);
		this.bigTwitter = new Twitter(generateTweetListDebug(16000), this.stopWordsDebug);
		
		this.incrementalTwitters = new ArrayList<Twitter>();
		for (int i = 0; i < testTimes; i++) {
			this.incrementalTwitters.add(new Twitter(generateTweetListDebug( (i+1) * this.multiplier), this.stopWordsDebug));
		}
		if (emptyStopWords) this.defaultTwitter = new Twitter(this.tweetListDefault, new ArrayList<String>());
		else this.defaultTwitter = new Twitter(this.tweetListDefault, this.stopWordsReal);
		
		
		
	}

	/** TweetList creator from the original tester. */
	private ArrayList<Tweet> generateTweetListDefault() {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		tweets.add(new Tweet("USER_989b85bb","2010-03-04 15:34:46","@USER_6921e61d I can be made into one twitter superstar."));
		tweets.add(new Tweet("USER_a75657c2","2010-03-03 00:02:54","@USER_13e8a102 They reached a compromise just on time"));
		tweets.add(new Tweet("USER_989b85bb","2010-03-04 15:34:47","I can be MADE into a need."));
		tweets.add(new Tweet("USER_a75657c2","2010-03-07 21:45:48","So SunChips made a bag that is 100% biodegradeable. It is about damn time somebody did."));
		tweets.add(new Tweet("USER_ee551c6c","2010-03-07 15:40:27","drthema: Do something today that feeds your spirit and empowers you to start the week from a higher place."));
		tweets.add(new Tweet("USER_6c78461b","2010-03-03 05:13:34","@USER_a3d59856 yes, i watched that foolery done disturbed my spirit. @USER_b1d28f26"));
		tweets.add(new Tweet("USER_92b2293c","2010-03-04 14:00:11","@USER_5aac9e88: Let no one push u around today! Be at Peace! If u dont have restful spirit, u'll definitely have a stressful spirit"));
		tweets.add(new Tweet("USER_75c62ed9","2010-03-07 03:35:38","@USER_cb237f7f Congrats on everything I am there in spirit my brother."));
		tweets.add(new Tweet("USER_7f72a368","2010-03-07 07:18:22","Actions speak louder than words but feelings and spirits speak louder than anything #FACT"));
		tweets.add(new Tweet("USER_b6cc1831","2010-03-07 04:04:37","@USER_be777094 urban spirit cafe. On Long st"));
		tweets.add(new Tweet("USER_65006b55","2010-03-05 00:58:28","RT @USER_86e8d97f: @USER_65006b55's spirit just took a turn for the worst. Lol please."));
		tweets.add(new Tweet("USER_60b9991b","2010-03-04 22:33:23","Who on my time ever flew on spirit airlines let me kno if there decent"));
		tweets.add(new Tweet("USER_36607a99","2010-03-03 02:06:01","@USER_561fe280: Nourish your spirit with your own achievement."));
		tweets.add(new Tweet("USER_9506fb5f","2010-03-04 01:16:34","Great spirits have often encountered violent opposition from weak minds"));
		tweets.add(new Tweet("USER_d3ca457f","2010-03-03 04:53:06","RT @USER_6d6bfb4d: The things that make a woman beautiful are her character, intellect, and spirituality."));
		tweets.add(new Tweet("USER_14f78255","2010-03-03 17:07:45","@USER_9afbc367 Oh in spirit. That's all that matters lol"));
		tweets.add(new Tweet("USER_3dfae4fe","2010-03-05 00:44:33","time for a spiritual cleansing of my facebook friend list"));
		tweets.add(new Tweet("USER_bd852fb7","2010-03-03 14:19:51","RT @USER_24bd1961:God's spirit is like a Radio station, broadcasting all the time. You just have to learn how to tune in and receive his signal"));
		tweets.add(new Tweet("USER_136c16da","2010-03-07 19:56:54","RT @USER_11d35e61: @USER_136c16da finally a kindred spirit. *daps* lol thanks"));
		tweets.add(new Tweet("USER_47063e51","2010-03-04 12:47:54","cathartic - noun - a purification or purgation that brings about spiritual renewal or release from tension"));
		tweets.add(new Tweet("USER_1e4eb302","2010-03-03 20:13:18","Anything worth having you have to contribute yourself heart, mind, soul and spirit to. It is so rewarding. Have u contributed lately?"));
		tweets.add(new Tweet("USER_5d246e83","2010-03-04 14:57:01","@USER_8e090edb That's always good to hear. Starting off to a good morning, always puts your spirit in a great place."));
		tweets.add(new Tweet("USER_b7117680","2010-03-03 06:55:17","I got a hustlas spirit, period!"));
		tweets.add(new Tweet("USER_25ecff25","2010-03-05 17:33:20","RT @USER_3a117437: The woman at the rental car spot tried 2 give us a Toyota! No ma'am lk the old spiritual says \"aint got time 2 die!\""));   
		tweets.add(new Tweet("USER_f91d8165","2010-03-03 22:33:24","#RandomThought why do people grab guns or knives when they think theres a ghost? DUMBASS! You can't shoot a spirit, grab some holy water! duh"));
		tweets.add(new Tweet("USER_86c542b8","2010-03-04 02:52:06","@USER_8cd1512d haha, maybe your right. I use to watch gymnastics all the time. I love the olympics. That's why I have so much spirit lol"));
		
		return tweets;
	}
	
	/** Generates debug tweetList. Dates centered around March 11, 1998. 
	 *  @param maxEntries Number of entries in the table. */
	private ArrayList<Tweet> generateTweetListDebug(int maxEntries) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		Random randomizer = new Random();
		
		//Setting up time
		long startingUnixTime = 889629335; //March 11, 1998 3:15:15 UTC
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		for (int i = 0; i < maxEntries; i++) {
			//Creating user identifier
			String user = "AUTHOR_" + i;
			
			//Generating time
			startingUnixTime = startingUnixTime + 5400; //Offset by 1.5hrs each
			Date date = new Date(startingUnixTime * 1000L); //Getting Date
			String dateStr = dateFormatter.format(date); //Getting formatted String out of Date
			
			//Initializing generic message
			String message = "messageOf" + user;
			
			//Distributing popular words
			double random = randomizer.nextDouble();
			if (random < 0.8) message += " mostPopular";
			if (random < 0.6) message += " secondPopular";
			if (random < 0.5) message += " thirdPopular";
			if (random < 0.4) message += " fourthPopular";
			if (random < 0.3) message += " fifthPopular";
			if (random < 0.2) message += " sixthPopular";
			if (random < 0.1) message += " seventhPopular";
			if (random < 0.05) message += " eighthPopular";
			
			//Distributing stopWords
			if (random < 0.9) message += " stopWordOne";
			if (random < 0.8) message += " stopWordTwo";
			if (random < 0.7) message += " stopWordThree";
			if (random < 0.6) message += " stopWordFour";
			if (random < 0.5) message += " stopWordFive";
			
			tweets.add(new Tweet(user, dateStr, message));
		}
		
		/*
		//Adding lastTweetByUser debug users
		Date date = new Date(currentUnixTime * 1000L); //Getting Date out of it
		String today = dateFormatter.format(date); //Getting formatted String out of Date
		tweets.add(new AUTHOR_("USER_10", today,"mostRecentTweet"));
		tweets.add(new AUTHOR_("USER_100", today,"mostRecentTweet"));
		tweets.add(new AUTHOR_("USER_20", today,"mostRecentTweet"));
		tweets.add(new AUTHOR_("USER_200", today,"mostRecentTweet"));
		*/
		
		return tweets;
	}
	
	/** Tests if load factor is respected and that null is returned */
	private void testPut_1() {
		String str = "";
		String pass = "    [PASS]testPut1: ";
		String fail = "    [FAIL]testPut1: ";
		try {
			//Adding things and see if the limit is reached
			this.tableSimple = new MyHashTable<String, String>(1);
			for (int i = 0; i < 5000; i++) {
				//Check for returning null
				if (this.tableSimple.put("Key" + i, "Value" + i) != null) {
					throw new CustomException1();
				}
				//Check for load factor
				if (1D * this.tableSimple.size() / this.tableSimple.numBuckets() > 0.75D) {
					throw new CustomException2();
				}
			}
			str += (pass + "Max load factor and adding new elements.\n");
		} catch (CustomException1 e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Table did not return null while adding a value with a brand new key\n.");
			e.printStackTrace();
		} catch (CustomException2 e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Table exceeded the max load factor after a put() operation.\n");
			e.printStackTrace();
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}
	
	/** Tests if adding values to already existing keys returns the previous value. */
	private void testPut_2() {
		String str = "";
		String pass = "    [PASS]testPut2: ";
		String fail = "    [FAIL]testPut2: ";
		try {
			//Putting elements back into the table and see if the previous one is returned
			for (int i = 0; i < 500; i++) {
				String value = this.tableSimple.put("Key" + i, "NewValue" + i);
				if (!value.equals("Value" + i)) {
					str += ("      Failed to return previous value: returned " + value + "instead of " + "Value" + 1 + "\n");
					throw new CustomException1();
				}
			}
			str += (pass + "Returning previous value.\n");
		} catch (CustomException1 e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Table did not return proper previous value.\n");
			e.printStackTrace();
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}

	/** Test basic hash table construction */
	private void testConstructor_1() {
		String str = "";
		String pass = "    [PASS]testConstructor1: ";
		String fail = "    [FAIL]testConstructor1: ";
		int[] correct = {0, 0, 1, 0, 0, 2, 0, 1, 0, 1, 1, 0, 2, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 2, 1, 0, 1, 0, 0, 0, 0, 0};
		String[] grid;
		
		try {
			
			//Create table
			this.tweetTableDefault = new MyHashTable<String,Tweet>(7);
			for (Tweet t: this.tweetListDefault) {
				this.tweetTableDefault.put(t.getDateAndTime(), t);
			}
			
			//Getting created grid size
			str += ("  New MyHashtable created with " + this.tweetTableDefault.size() + " keys and " + this.tweetTableDefault.numBuckets() + " buckets (Load Factor: " 
					+ 1D*this.tweetTableDefault.size()/this.tweetTableDefault.numBuckets() + "). With shape:\n");
			str += ("  ------------------------------------------------------------------------------\n");
			grid = displayTableShape(this.tweetTableDefault);
			str += grid[0];
			str += ("  ------------------------------------------------------------------------------\n");
			
			//Comparing theoretical size and given size
			if (this.tweetTableDefault.size() == this.tweetListDefault.size()) {
				str += pass + "Table size\n";
			}
			else {
				str += (fail + "Table created should have " + this.tweetListDefault.size() + " entries. Constructor instead created " + this.tweetTableDefault.size() + "entries\n");
			}
			
			//Comparing theoretical size and given size
			if (this.tweetTableDefault.numBuckets() == 56) {
				str += pass + "Bucket count\n";
			}
			else {
				str += (fail + "Table created should have " + 56 + " buckets. Constructor instead created " +  this.tweetTableDefault.numBuckets() +  " buckets\n");
			}
			//Comparing contents
			if (grid[1].equals(Arrays.toString(correct))) {
				str += pass + "Bucket Content\n";
			}
			else {
				str += (fail + "Entry bucket distribution is wrong\n    Supposed:" + Arrays.toString(correct)) + "\n    Created: " + grid[1] + "\n";
			}
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}
	
	
	
	/** Test creating a much bigger and much more complex tweetTable*/
	private void testConstructor_4() {
		String str = "";
		String pass = "    [PASS]testConstructor4: ";
		String fail = "    [FAIL]testConstructor4: ";
		try {
			
			//Create table with users as keys
			this.tweetTableDebug = new MyHashTable<String,Tweet>(7);
			for (Tweet t: this.tweetListDebug) {
				this.tweetTableDebug.put(t.getAuthor(), t);
			}
			
			//Getting created grid size
			str += ("  New MyHashtable created with " + this.tweetTableDebug.size() + " keys and " + this.tweetTableDebug.numBuckets() + " buckets (Load Factor: " 
					+ 1D*this.tweetTableDebug.size()/this.tweetTableDebug.numBuckets() + "). Not displayed.\n\n");
			
			//Comparing theoretical size and given size
			if (this.tweetTableDebug.size() == this.maxDebugEntries) {
				str += (pass + "Table size\n");
			}
			else {
				str += (fail + "Table created should have " + (this.maxDebugEntries) + " entries. Constructor instead created " + this.tweetTableDebug.size() + "entries\n");
			}
			
			//Comparing theoretical size and given size
			if (this.tweetTableDebug.numBuckets() == 3584) {
				str += (pass + "Bucket count\n");
			}
			else {
				str += (fail + "Table created should have " + 3584 + " buckets. Constructor instead created " +  this.tweetTableDebug.numBuckets() +  " buckets\n");
			}
			
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}
	
	/** Original get test */
	@SuppressWarnings("null")
	private void testGet_0() {
		String str = "";
		String pass = "    [PASS]testGet0: ";
		String fail = "    [FAIL]testGet0: ";
		try {
			//Gets whatever tweetTableDefault returns
			Tweet testTweet0 = this.tweetTableDefault.get("2010-03-04 15:34:47");
			
			if (testTweet0 != null || testTweet0.getAuthor().equals("USER_989b85bb") || testTweet0.getMessage().equals("I can be MADE into a need.")) {
				str += (pass + "Oringinal Minitester test. Get tweet from '2010-03-04 15:34:47'.\n");
			}
			else {
				str += (fail + "Oringinal Minitester test. Failed to retrieve tweet from correctly '2010-03-04 15:34:47'.\n");
			}
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}
	
	/** Trying to access lots of keys that exist */
	private void testGet_1() {
		String str = "";
		String pass = "    [PASS]testGet1: ";
		String fail = "    [FAIL]testGet1: ";
		try {
			//Check a bunch of keys for their values
			for (int i = 446; i < 876; i++) {
				if (!this.tweetTableDebug.get("AUTHOR_" + i).getMessage().split(" ")[0].equals("messageOfAUTHOR_" + i)) {
					str += ("      Failed to retrieve tweet from " + "AUTHOR_" + i + ".\n");
					str += ("      Should have been \"messageOfAUTHOR_" + i + "\" but instead returned: " + this.tweetTableDebug.get("AUTHOR_" + i).getMessage().split(" ")[0] + "\n");
					throw new CustomException1();
				}
			}
			//No error found
			str += (pass + "Checking lots of keys.\n");
		}catch (CustomException1 e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Failed to retrieve tweet: Incorrect tweet.\n");
			e.printStackTrace();
		} catch (NullPointerException e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Failed to retrieve tweet: Returned null where it was supposed to return something.\n");
			e.printStackTrace();
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}
	
	/** Trying to access nonexistent keys */
	private void testGet_2() {
		String str = "";
		String pass = "    [PASS]testGet2: ";
		String fail = "    [FAIL]testGet2: ";
		try {
			//Check a bunch of keys for their values
			for (int i = 2500; i < 2600; i++) {
				if (this.tweetTableDebug.get("AUTHOR_" + i) != null) {
					throw new CustomException1();
				}
			}
			//No error found
			str += (pass + "Getting nonexistant keys.\n");
		}catch (CustomException1 e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Returned a value while getting a nonexistant key.\n");
			e.printStackTrace();
		} catch (NullPointerException e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "NullpointerException occured while trying to get nonexistant key.\n");
			e.printStackTrace();
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}
	
	/** Original remove test */
	private void testRemove_0() {
		String str = "";
		String pass = "    [PASS]testRemove0: ";
		String fail = "    [FAIL]testRemove0: ";
		try {
			// Try to remove a tweet
			Tweet removedTweet = this.tweetTableDefault.remove("2010-03-03 06:55:17");
			Tweet retrievedTweet = this.tweetTableDefault.get("2010-03-03 06:55:17");
			
			//Returned wrong entry
			if (removedTweet == null || !removedTweet.getAuthor().equals("USER_b7117680") || !removedTweet.getMessage().equals("I got a hustlas spirit, period!")) {
				str += ("      Failed to return tweet from when calling return '2010-03-03 06:55:17'.\n");
				
				if (removedTweet != null) str += ("      Returned tweet from " + removedTweet.getDateAndTime() + "\n");
				else str += ("      Returned null.\n");
				
				throw new CustomException1();
			}
			str += (pass + "Returned right value.\n");
			
			//Entry still present
			if (retrievedTweet != null) {
				str += ("      Tweet from '2010-03-03 06:55:17' is still present after remove.\n");
				str += ("      Calling return on previously removed 2010-03-03 06:55:17 returned tweet from " + retrievedTweet.getDateAndTime() + "\n");
				throw new CustomException2();
			}
			str += (pass + "Entry was removed.\n");
			
		} catch (CustomException1 e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Remove returned wrong entry or null after removing key that exists (2010-03-03 06:55:17).\n");
			e.printStackTrace();
		} catch (CustomException2 e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Didnt remove entry for 2010-03-03 06:55:17).\n");
			e.printStackTrace();
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}
	
	/** Original remove test */
	private void testRemove_1() {
		String str = "";
		String pass = "    [PASS]testRemove1: ";
		String fail = "    [FAIL]testRemove1: ";
		try {
			//Try to remove everything
			for (int i = 0; i < 500; i++) {
				if (!this.tableSimple.remove("Key" + i).equals("Value" + i)) {
					throw new CustomException1();
				}
			}
			if (this.tableSimple.size() == 0) {
				str += (pass + "Removed all elements.\n");
			}
			else {
				str += (fail + "Table is not empty after removing all elements.\n");
			}
			str += (pass + "All return values correct.\n");
		} catch (CustomException1 e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Wrong value returned by remove.\n");
			e.printStackTrace();
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}
	
	/** Original remove test */
	private void testRemove_2() {
		String str = "";
		String pass = "    [PASS]testRemove2: ";
		String fail = "    [FAIL]testRemove2: ";
		try {
			Tweet tweet = this.tweetTableDebug.remove("randomkey");
			if (tweet == null) {
				str += (pass + "Removing nonexistent key returns null.\n");
			}
			else {
				str += (fail + "Removing nonexistent key returns a value:" + tweet.getAuthor() + ".\n");
			}
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}
	
	/** Try to remove all elements */
	private void testKeys_1() {
		String str = "";
		String pass = "    [PASS]testKeys1: ";
		String fail = "    [FAIL]testKeys1: ";
		try {
			//Get all the keys from the complex table
			ArrayList<String> keysList = this.tweetTableDebug.keys();
			
			//Try to remove all the elements from the table with the returned key list
			for (String currentKey : keysList) {
				//If a remove operation fails
				if (this.tweetTableDebug.remove(currentKey) == null) {
					throw new CustomException1();
				}
			}
			str += (pass + "All keys from the keyList are unique and correspond to a value.\n");
			
			//Check that the table has been emptied
			if (this.tweetTableDebug.size() == 0) str += (pass + "All keys were returned.\n");
			else str += (fail + "Not all keys were returned: table is not empty after removing all corresponding entries.\n");
			
			
		} catch (CustomException1 e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "One or more key is either duplicate or nonexistant.\n");
			e.printStackTrace();
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}

	/** Try on empty table */
	private void testKeys_2() {
		String str = "";
		String pass = "    [PASS]testKeys_2: ";
		String fail = "    [FAIL]testKeys_2: ";
		try {
			//Create empty table
			MyHashTable<Integer, Integer> dummy = new MyHashTable<Integer, Integer>(1);
			ArrayList<Integer> keysList = dummy.keys();
			
			if (keysList.size() == 0) str += (pass + "Keys on an empty table.\n");
			else str += (fail + "Returned list is not empty when called on empty table.\n");
			
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}

	/** Original rehash test */
	private void testRehash_0() {
		String str = "";
		String pass = "    [PASS]testRehash0: ";
		String fail = "    [FAIL]testRehash0: ";
		try {
			//Rehashing changes the capacity of the table, but not the number of entries
			int oldBucketCount = this.tweetTableDefault.numBuckets();
			int oldSize = this.tweetTableDefault.size();
			
			this.tweetTableDefault.rehash();
			
			int newBucketCount = this.tweetTableDefault.numBuckets();
			
			//Checking that bucket count has doubled
			if (2 * oldBucketCount != newBucketCount) str += (fail + "Bucket count has not doubled after rehashing\n");
			else str += (pass + "Bucket size doubles\n");
			
			//Checking that element count has not changed
			if (oldSize != this.tweetTableDefault.size()) str += (fail + "Entry count has changed\n");
			else str += (pass + "Entry count does not change\n");
			
			// Try to retrieve a tweet
			Tweet testTweet1 = this.tweetTableDefault.get("2010-03-04 15:34:47");
			if (testTweet1 == null || !testTweet1.getAuthor().equals("USER_989b85bb") || !testTweet1.getMessage().equals("I can be MADE into a need.")) {
				str += (fail + "Failed to retrieve tweet from '2010-03-04 15:34:47'.\n");
			}
			else {
				str += (pass + "Managed to retrieve tweet from '2010-03-04 15:34:47'.\n");
			}
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}
	
	/** Checks that all values returned are unique. */
	private void testValues_1() {
		String str = "";
		String pass = "    [PASS]testValues1: ";
		String fail = "    [FAIL]testValues1: ";
		try {
			MyHashTable<String, String> valueTable = new MyHashTable<String, String>(1);
			int count = 0;
			
			//Creating a table with 143 unique values. The rest are duplicates
			for (int i = 0; i < 1000; i++) {
				
				//Add a unique value each 7th element
				switch (i % 7) {
					case 0:
						//Add a unique element
						valueTable.put("Key" + i, "Unique" + i);
						count++;
						break;
					case 1:
						valueTable.put("Key" + i, "NonUnique1");
						break;
					case 2:
						valueTable.put("Key" + i, "NonUnique2");
						break;
					case 3:
						valueTable.put("Key" + i, "NonUnique3");
						break;
					case 4:
						valueTable.put("Key" + i, "NonUnique4");
						break;
					case 5:
						valueTable.put("Key" + i, "NonUnique5");
						break;
					case 6:
						valueTable.put("Key" + i, "NonUnique6");
						break;
					default:
						throw new Exception("This is not supposed to happen");
				}
			}
			//Get the unique list
			ArrayList<String> uniqueList = new ArrayList<String>();	
			uniqueList = valueTable.values();
			
			//Converting into set
			Set<String> uniqueSet = new HashSet<String>(uniqueList);
			
			
			
			//Check that right amount of values returned
			if (uniqueList.size() == count + 6) str += (pass + "Right amount of uniques returned.\n");
			else str += (fail + "Wrong amount of uniques returned: " + uniqueList.size() + ". Should be " + (count + 6) + ".\n");
			
			//Check that all values are unique
			if (uniqueList.size() == uniqueSet.size() && uniqueList.size() != 0) str += (pass + "All values within unique list are unique.\n");
			else str += (fail + "Not all values within unique list are unique.\n");
			
			
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}
	
	/** Compares sort to slowSort and Collections */
	private void testFastSort_1() {
		String str = "";
		String pass = "    [PASS]testFastSort0: ";
		String fail = "    [FAIL]testFastSort0: ";
		try {
			long slowTime;
			long fastTime;
			long collectionsTime;
			long start;
			
			//Timing and doing the slow sort
			start = System.currentTimeMillis();
			ArrayList<String> slowResult = MyHashTable.slowSort(this.tweetTableDebug);
			slowTime = System.currentTimeMillis() - start;
			
			//Timing and doing the fast sort
			start = System.currentTimeMillis();
			ArrayList<String> fastResult = MyHashTable.fastSort(this.tweetTableDebug);
			fastTime = System.currentTimeMillis() - start;
			
			//Timing and doing with collections
			start = System.currentTimeMillis();
			
			//Getting keys list
			ArrayList<String> collectionsResult = this.tweetTableDebug.keys();
			
			//Creating inline comparator and sorting
			Collections.sort(collectionsResult, new Comparator<String>() {
				//Make comparator identical to value-compare
				@Override
				public int compare(String key1, String key2) {
					//Inverse
					return tweetTableDebug.get(key2).compareTo(tweetTableDebug.get(key1));
				}
			} );
			collectionsTime = System.currentTimeMillis() - start;
			
			//Displaying results
			str += ("      Time comparison for 2000 entries (for reference)\n");
			str += ("      +----------------------------------------------------\n");
			str += ("      |                   slowSort() | " + slowTime + " ms\n");
			str += ("      |                   fastSort() | " + fastTime + " ms\n");
			str += ("      | Java.util.Collections.sort() | " + collectionsTime + " ms\n");
			str += ("      +----------------------------------------------------\n\n");
			
			//At least 3 times faster
			if (fastTime < slowTime / 3) str += (pass + "fastSort at least 3x faster than slowSort.\n");
			else str += (fail + "fastSort is slower than slowSort!!!.\n");
			
			//Check accuracy
			if (fastResult.equals(slowResult)) str += (pass + "fastSort sorted correctly.\n");
			else str += (fail + "fastSort did not sort correctly.\n");
			
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}
	
	/** Checks that the iterator gives the right values */
	private void testIterator_1() {
		String str = "";
		String pass = "    [PASS]testIterator1: ";
		String fail = "    [FAIL]testIterator1: ";
		try {
			//Normal keysList
			ArrayList<String> normKeysList = this.tweetTableDebug.keys();
			
			//Iterator generated ketsList
			ArrayList<String> iterKeysList = new ArrayList<String>();
			
			//Get iterator for debug
			Iterator<HashPair<String, Tweet>> iter = this.tweetTableDebug.iterator();
			
			//Try loop and check result
			while (iter.hasNext()) {
				//Store the key of the return
				iterKeysList.add(iter.next().getKey());
			}
			
			if (iterKeysList.size() == this.tweetTableDebug.size()) str += (pass + "Iterator returns the right number of entries.\n");
			else str += (fail + "Iterator does not return the right number of entries.\n");
			
			//Compare the two lists
			Collections.sort(normKeysList);
			Collections.sort(iterKeysList);
			if (normKeysList.equals(iterKeysList)) str += (pass + "Iterator returns all entries.\n");
			else str += (fail + "Iterator does not returns all entries.\n");
			
			//Try to get a NoSuchElementException
			iter.next();
			str += (fail + "Iterator did not return NoSuchElementException when calling next after finishing.\n");
			
			
		} catch (NoSuchElementException e) {
			str += (pass + "Iterator returned NoSuchElementException when there are no more next().\n");
		} catch (IndexOutOfBoundsException e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "IndexOutOfBoundsException when calling out of bound next(). Should be NoSuchElementException.\n");
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}
	
	private void testTwitterConstructor_1() {
		String str = "";
		String pass = "    [PASS]testTwitterConstructor_1: ";
		String fail = "    [FAIL]testTwitterConstructor_1: ";
		try {
			
			//Creating multiple twitter and checking how long it takes to see if curve is linear
			ArrayList<ArrayList<Tweet>> list = new ArrayList<ArrayList<Tweet>>();
			ArrayList<Long> timeList = new ArrayList<Long>();
			
			//Generating tweetLists of different lengths
			for (int i = 1; i < 7; i++) {
				list.add(generateTweetListDebug(multiplier * i));
			}
			
			//Stress testing these to see pattern
			for (ArrayList<Tweet> currentTweetList : list) {
				long startTime = System.currentTimeMillis();
				//Creating dummy twitter
				this.incrementalTwitters.add(new Twitter(currentTweetList, this.stopWordsDebug));
				timeList.add(System.currentTimeMillis() - startTime);
			}
			
			//APART -- Initialize other test twitters
			this.smallTwitter = new Twitter(generateTweetListDebug(4000), this.stopWordsDebug);
			this.mediumTwitter = new Twitter(generateTweetListDebug(8000), this.stopWordsDebug);
			this.bigTwitter = new Twitter(generateTweetListDebug(16000), this.stopWordsDebug);
			
			this.defaultTwitter = new Twitter(this.tweetListDefault, this.stopWordsReal);
			
			//Displaying times
			str += "      Stress test results (stopWords constant, tweetList variable):    \n";
			str += "      +----------------------------------------------------------------\n";
			for (int i = 0; i < timeList.size(); i++) {
				str += "      |  " + (multiplier * (i+1)) + " tweets: " + timeList.get(i) + " ms";
				if (i != 0) str += "     delta: " + (timeList.get(i) - timeList.get(i - 1));
				str += "\n";
			}
			str += "      +----------------------------------------------------------------\n";
			str += "      Change test amount and steps by changing inputs. Default: 6, 1000\n\n";
			
			//Trying to create with empty arrayLists
			try {
				new Twitter(new ArrayList<Tweet>(), new ArrayList<String>());
				new Twitter(new ArrayList<Tweet>(), this.stopWordsDebug);
				new Twitter(this.tweetListDefault, new ArrayList<String>());
				str += (pass + "Created twitters with empty arrayLists without crashing.\n");
			} catch (Exception e) {
				str += "      Exception Found!!!!!!!!\n";
				str += (fail + "Creating twitters with empty arrayLists causes crash.\n");
				e.printStackTrace();
			}
			
			
			str += (pass + "Initialized all test twitters without crashing.\n");
			
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}
	
	/** Original tester */
	private void testLastTweetByUser_0() {
		String str = "";
		String pass = "    [PASS]testLastTweetByUser0: ";
		String fail = "    [FAIL]testLastTweetByUser0: ";
		try {
			Tweet tweetByAuthor = this.defaultTwitter.latestTweetByAuthor("USER_989b85bb");
			if (tweetByAuthor == null || !tweetByAuthor.getAuthor().equals("USER_989b85bb") || 
					!tweetByAuthor.getMessage().equals("I can be MADE into a need.") || 
					!tweetByAuthor.getDateAndTime().equals("2010-03-04 15:34:47")) {
				
				str+=(fail + "Failed to retrieve the latest tweet by USER_989b85bb. Instead retrieved:\n" + tweetByAuthor.toString() + "\n");
			}
			else {
				str += (pass + "Retrieved right tweet.\n");
			}
			
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}
	
	/** Compares runtime. */
	private void testLatestTweetByUser_1() {
		String str = "";
		String pass = "    [PASS]testLatestTweetByUser1: ";
		String fail = "    [FAIL]testLatestTweetByUser1: ";
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Long calibratedTime = System.currentTimeMillis();
			String today;
			
			for (int i = 0; i < 5; i++) {
				//Getting today's date
				today = (dateFormatter.format(new Date(calibratedTime - (i * 3000)))); //Getting formatted String out of Date
				for (Twitter currentTwitter : this.incrementalTwitters) {
					currentTwitter.addTweet(new Tweet("AUTHOR_555", today, "Most current " + i));
				}
				this.smallTwitter.addTweet(new Tweet("AUTHOR_555", today, "Most current " + i));
				this.mediumTwitter.addTweet(new Tweet("AUTHOR_555", today, "Most current " + i));
				this.bigTwitter.addTweet(new Tweet("AUTHOR_555", today, "Most current " + i));
			}
			
			Long time, start = 0L;
			Tweet smallTweet;
			Tweet mediumTweet;
			Tweet bigTweet;
			
			smallTweet = this.smallTwitter.latestTweetByAuthor("AUTHOR_555");
			mediumTweet = this.mediumTwitter.latestTweetByAuthor("AUTHOR_555");
			bigTweet = this.bigTwitter.latestTweetByAuthor("AUTHOR_555");
			
			//Displaying times
			str += "      Eyeballed Stress test results:    \n";
			str += "      +----------------------------------------------------------------\n";
			
			for (int i = 0; i < this.incrementalTwitters.size(); i++) {
				start = System.nanoTime();
				this.incrementalTwitters.get(i).latestTweetByAuthor("AUTHOR_555");
				time = System.nanoTime() - start;
				str += "      | " + ((i+1) * this.multiplier) +  " tweets  : " + time + " ns \n";
				
			}
			
			str += "      +----------------------------------------------------------------\n\n";
			
			if (smallTweet.getMessage().equalsIgnoreCase("Most current 0") && smallTweet.getMessage().equalsIgnoreCase("Most current 0") && smallTweet.getMessage().equalsIgnoreCase("Most current 0")) {
				str += (pass + "Returned most current are correct.\n");
			}
			else {
				str += ("      " + smallTweet.toString() + "\n      " + mediumTweet.toString()) + "\n      " + bigTweet.toString() + "\n";
				str += (fail + "Did not return the most recent.\n");
			}
			
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}
	
	/** Original test */
	private void testTweetsByDate_0() {
		String str = "";
		String pass = "    [PASS]testTweetsByDate0: ";
		String fail = "    [FAIL]testTweetsByDate0: ";
		try {
			ArrayList<Tweet> tweetsByDate = defaultTwitter.tweetsByDate("2010-03-03");
			if(tweetsByDate.size() != 9){
				str += ("      " + tweetsByDate.size() + " tweets posted on 2010-03-03. Should by 9.");
				str += (fail + "Failed to retrieve all 9 tweets posted on 2010-03-03\n");
			} 
			else {
				str += (pass + "Retrieved all 9 tweets posted on 2010-03-03\n");
			}
			
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}
	
	/** Compares runtime. */
	private void testTweetsByDate_1() {
		String str = "";
		String pass = "    [PASS]testTweetsByDate1: ";
		String fail = "    [FAIL]testTweetsByDate1: ";
		try {
			
			//Try to get all tweets on march 12, 1998
			Long time, start = 0L;
			ArrayList<Tweet> smallTweets = this.smallTwitter.tweetsByDate("1998-03-12");
			ArrayList<Tweet> mediumTweets = this.mediumTwitter.tweetsByDate("1998-03-12");
			ArrayList<Tweet> bigTweets = this.bigTwitter.tweetsByDate("1998-03-12");
			
			//Displaying times
			str += "      Eyeballed Stress test results:    \n";
			str += "      +----------------------------------------------------------------\n";
			
			for (int i = 0; i < this.incrementalTwitters.size(); i++) {
				start = System.nanoTime();
				this.incrementalTwitters.get(i).tweetsByDate("1998-03-12");
				time = System.nanoTime() - start;
				str += "      | " + ((i+1) * this.multiplier) +  " tweets  : " + time + " ns \n";
			}
			
			str += "      +----------------------------------------------------------------\n\n";
			
			if (smallTweets.size() == 16 && mediumTweets.size() == 16 && bigTweets.size() == 16) str += (pass + "Retrieved all 16 tweets posted on 1998-03-12.\n");
			else str += (fail + "Retrieved " + smallTweets.size() +  ", " + mediumTweets.size() + ", and  " + bigTweets.size() + " instead of 16 tweets posted on 1998-03-12.\n");
			
			for (Tweet currentTweet : smallTweets) {
				if (!currentTweet.getDateAndTime().split(" ")[0].equalsIgnoreCase("1998-03-12")) throw new CustomException1();
			}
			str += (pass + "All retrieved tweets are from the right day.\n");
			
		} catch (CustomException1 e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Not all retrieved tweets are from the right day.\n");
			e.printStackTrace();
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}
	
	private void testTrending_1() {
		String str = "";
		String pass = "    [PASS]testTrending1: ";
		String fail = "    [FAIL]testTrending1: ";
		try {
			
			//Get the trending topics
			ArrayList<String> defaultTrending = this.defaultTwitter.trendingTopics();
			
			//Resetting twitters and making sure the default has no banned words
			this.resetTwitters(true);
			ArrayList<String> defaultTrendingStop = this.defaultTwitter.trendingTopics();
			ArrayList<String> debugTrending = this.bigTwitter.trendingTopics();
			
			//Verifying that the default trending words are correct
			str += ("          Expected: spirit, a, that, time.\n");
			str += ("          Returned: " + defaultTrendingStop.get(0) + ", " + defaultTrendingStop.get(1) + ", " + defaultTrendingStop.get(2) + ", " + defaultTrendingStop.get(3) + "\n\n");
			
			if (!defaultTrendingStop.get(0).equalsIgnoreCase("spirit") || !defaultTrendingStop.get(1).equalsIgnoreCase("a")
					|| !defaultTrendingStop.get(2).equalsIgnoreCase("that") || !defaultTrendingStop.get(3).equalsIgnoreCase("time")) {
				str += (fail + "Returned words for default twitter (disregarding stopWords) are not correct.\n\n");
			} else {
				str += (pass + "Returned words for default twitter (disregarding stopWords) correct.\n\n");
			}
			
			//Verifying that the default trending words are correct considering stop words
			str += ("          Expected: spirit, time, rt, lol.\n");
			str += ("          Returned: " + defaultTrending.get(0) + ", " + defaultTrending.get(1) + ", " + defaultTrending.get(2) + ", " + defaultTrending.get(3) + "\n\n");
			
			if (!defaultTrendingStop.get(0).equalsIgnoreCase("spirit") || !defaultTrendingStop.get(1).equalsIgnoreCase("a")
					|| !defaultTrendingStop.get(2).equalsIgnoreCase("that") || !defaultTrendingStop.get(3).equalsIgnoreCase("time")) {
				str += (fail + "Returned words for default twitter are not correct.\n\n");
			} else {
				str += (pass + "Returned words for default twitter correct.\n\n");
			}
			
			//Verifying that the default trending words are correct considering stop words
			str += ("          Expected: mostPopular, secondPopular, thirdPopular, fourthPopular, fifthPopular, sixthPopular, seventhPopular, eighthPopular\n");
			str += ("          Returned: " + debugTrending.get(0) + ", " + debugTrending.get(1) + ", " + debugTrending.get(2) + ", " + debugTrending.get(3));
			str += (                  ", " + debugTrending.get(4) + ", " + debugTrending.get(5) + ", " + debugTrending.get(6) + ", " + debugTrending.get(7) + "\n\n");
			
			if (!debugTrending.get(0).equalsIgnoreCase("mostPopular") || !debugTrending.get(1).equalsIgnoreCase("secondPopular") || !debugTrending.get(2).equalsIgnoreCase("thirdPopular")
					|| !debugTrending.get(3).equalsIgnoreCase("fourthPopular") || !debugTrending.get(4).equalsIgnoreCase("fifthPopular") || !debugTrending.get(5).equalsIgnoreCase("sixthPopular")
					|| !debugTrending.get(6).equalsIgnoreCase("seventhPopular") || !debugTrending.get(7).equalsIgnoreCase("eighthPopular")) {
				str += (fail + "Returned words for debug twitter are not correct.\n\n");
			} else {
				str += (pass + "Returned words for debug twitter correct.\n\n");
			}
			
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}
	
	private void testTrending_2() {
		String str = "";
		String pass = "    [PASS]testTrending2: ";
		String fail = "    [FAIL]testTrending2: ";
		try {
			//Creating massive tweet tables
			this.incrementalTwitters = new ArrayList<Twitter>();
			for (int i = 0; i < 12; i++) {
				this.incrementalTwitters.add(new Twitter(generateTweetListDebug( (int)(100 * Math.pow(2, i+1) )), this.stopWordsDebug));
			}
			
			//Displaying times for massive tweet tables
			Long time, start = 0L;
			int errors = 0;
			
			str += "\n      Eyeballed Stress test results:    \n";
			str += "      +----------------------------------------------------------------\n";
			for (int i = 0; i < this.incrementalTwitters.size(); i++) {
				//Get time of execution
				start = System.nanoTime();
				ArrayList<String> dummy = this.incrementalTwitters.get(i).trendingTopics();
				time = System.nanoTime() - start;
				
				//Check for correctness
				for (int j = 0; j < 7; j++) {
					if (!dummy.get(j).equalsIgnoreCase(this.correctTrendingDebug.get(j))) {
						errors ++;
					}
				}
				str += "      | " + ( (int)(100 * Math.pow(2, i+1)) ) +  " tweets  : " + time + " ns\n";
			}
			str += "      +----------------------------------------------------------------\n";
			str += "      NOTE: This test ONLY looks at the time it takes to execute twitter.trendingTopics().\n\n";
			
			if (errors == 0) str += (pass + "Stress test finished without issue. \n");
			else str += (fail + "Stress test finished with " + errors + " errors. \n");
			
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}
	
	
	public static void main(String[] args) {
		HashBrownTester tester = new HashBrownTester();
		write("---------------\nTesting put: O(1)\n---------------\n");
		tester.testPut_1();
		tester.testPut_2();
		
		write("---------------\nTesting Constructor (With some put):\n---------------\n");
		tester.testConstructor_1();
		tester.testConstructor_4();
		
		write("---------------\nTesting get: O(1)\n---------------\n");
		tester.resetTables();
		tester.testGet_0();
		tester.testGet_1();
		tester.testGet_2();
		
		write("---------------\nTesting remove: O(1)\n---------------\n");
		tester.resetTables();
		tester.testRemove_0();
		tester.testRemove_1();
		tester.testRemove_2();
		
		write("---------------\nTesting keys: O(m)\n---------------\n");
		tester.resetTables();
		tester.testKeys_1();
		tester.testKeys_2();
		
		write("---------------\nTesting rehash: O(m)\n---------------\n");
		tester.resetTables();
		tester.testRehash_0();
		
		write("---------------\nTesting values: 0(m)\n---------------\n");
		tester.resetTables();
		tester.testValues_1();
		
		write("---------------\nTesting fastSort: O(nlogn)\n---------------\n");
		tester.resetTables();
		tester.testFastSort_1();
		
		write("---------------\nTesting iterator: O(m)\n---------------\n");
		tester.testIterator_1();
		
		write("---------------\nTesting twitter: O(n + m)\n---------------\n");
		tester.testTwitterConstructor_1();
		
		write("---------------\nTesting lastTweetsByUser: O(1)\n---------------\n");
		tester.testLastTweetByUser_0();
		tester.testLatestTweetByUser_1();
		
		write("---------------\nTesting tweetsByDate: O(1)\n---------------\n");
		tester.testTweetsByDate_0();
		tester.testTweetsByDate_1();
		
		write("---------------\nTesting trending: O(nlogn)\n---------------\n");
		tester.testTrending_1();
		tester.testTrending_2();
	}
	
	private <K, V extends Comparable<V>> String[] displayTableShape(MyHashTable<K, V> tweetTable){
		String[] toRet = new String[2];
		ArrayList<Integer> grid = new ArrayList<Integer>();
		String str = "";
		try {
			for (int i = 0; i < tweetTable.numBuckets() / 4; i++) {
				String cushion1 = "";
				String cushion2 = "";
				String cushion3 = "";
				String cushion4 = "";
				for (int j = 0; j < 4 - Integer.toString(    (i                            )).length(); j++) {
					cushion1 = cushion1 + " ";
				}
				for (int j = 0; j < 4 - Integer.toString(    (i + tweetTable.numBuckets()/4)).length(); j++) {
					cushion2 = cushion2 + " ";
				}
				for (int j = 0; j < 4 - Integer.toString(    (i + tweetTable.numBuckets()/2)).length(); j++) {
					cushion3 = cushion3 + " ";
				}
				for (int j = 0; j < 4 - Integer.toString(3 * (i + tweetTable.numBuckets()/4)).length(); j++) {
					cushion4 = cushion4 + " ";
				}
				str+=  (""    + ("    " + (i                              )) + ": " + cushion1 + tweetTable.getBuckets().get(i                              ).size());
				grid.add(tweetTable.getBuckets().get(i                              ).size());
				str+=  ("    "+ ("    " + (i +   tweetTable.numBuckets()/4)) + ": " + cushion2 + tweetTable.getBuckets().get(i +   tweetTable.numBuckets()/4).size());
				grid.add(tweetTable.getBuckets().get(i +   tweetTable.numBuckets()/4).size());
				str+=  ("    "+ ("    " + (i +   tweetTable.numBuckets()/2)) + ": " + cushion3 + tweetTable.getBuckets().get(i +   tweetTable.numBuckets()/2).size());
				grid.add(tweetTable.getBuckets().get(i +   tweetTable.numBuckets()/2).size());
				str+=  ("    "+ ("    " + (i + 3*tweetTable.numBuckets()/4)) + ": " + cushion4 + tweetTable.getBuckets().get(i + 3*tweetTable.numBuckets()/4).size()) + "\n";
				grid.add(tweetTable.getBuckets().get(i + 3*tweetTable.numBuckets()/4).size());
			}
			return toRet;
		} catch (Exception e) {
			return toRet;
		} finally {
			toRet[0] = str;
			toRet[1] = Arrays.toString(grid.toArray());
		}
	}
	
	@SuppressWarnings("unused")
	private void testTemplate_0() {
		String str = "";
		String pass = "    [PASS]testTemplate0: ";
		String fail = "    [FAIL]testTemplate0: ";
		try {
			str += (pass + "Pass condition\n");
		} catch (Exception e) {
			str += "      Exception Found!!!!!!!!\n";
			str += (fail + "Unexpected exception: " + e.getClass().getName() + "\n");
			e.printStackTrace();
		} finally {
			write(str);
		}
	}
	
	
	//Custom exceptions
	@SuppressWarnings("serial")
	private class CustomException1 extends RuntimeException {
		String name;
		@SuppressWarnings("unused")
		public CustomException1(String n) {
			this.name = n;
		}
		public CustomException1() {
			this.name = "";
		}
		public String toString() {
			return "CustomException1 found. " + this.name;
		}
	}
	@SuppressWarnings("serial")
	private class CustomException2 extends RuntimeException {
		String name;
		@SuppressWarnings("unused")
		public CustomException2(String n) {
			this.name = n;
		}
		public CustomException2() {
			this.name = "";
		}
		public String toString() {
			return "CustomException1 found. " + this.name;
		}
	}
}
