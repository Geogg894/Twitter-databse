package FinalProject_Template;

import java.util.ArrayList;


public class Twitter {

	private MyHashTable<String, ArrayList<Tweet>> table1;
	private MyHashTable<String, String> table2;
	private MyHashTable<String, Tweet> table3;
	private MyHashTable<String,Integer> IntMap;
	private ArrayList<Tweet> tweet;

	public Twitter(ArrayList<Tweet> tweets, ArrayList<String> stopWords) {

		this.table1=new MyHashTable<String, ArrayList<Tweet>>(tweets.size());
		this.table2=new MyHashTable<String, String>(1); 
		this.IntMap=new  MyHashTable<String,Integer>(tweets.size());
		this.table3=new MyHashTable<String, Tweet>(tweets.size());
		this.tweet=new ArrayList<Tweet>();


		for(Tweet t:tweets) {

			this.addTweet(t);
		}

		for(Tweet f:tweets) {

			this.tweet.add(f);
		}

		for(String s:stopWords) {

			table2.put(s.toLowerCase(),s.toLowerCase());
		}

	}


	/**
	 * Add Tweet t to this Twitter
	 * O(1)
	 */
	public void addTweet(Tweet t) {

		
		if(table3.get(t.getAuthor())!=null) {

			if(table3.get(t.getAuthor()).getDateAndTime().compareTo(t.getDateAndTime())<0) {

				table3.put(t.getAuthor(), t);
			}
		}

		else {

			table3.put(t.getAuthor(), t);
		}

		if(table1.get(t.getDateAndTime().split(" ")[0])!=null) {

			table1.get(t.getDateAndTime().split(" ")[0]).add(t);
		}

		else {

			table1.put(t.getDateAndTime().split(" ")[0],new ArrayList <Tweet>());
			table1.get(t.getDateAndTime().split(" ")[0]).add(t);
		}

	}

	/**
	 * Search this Twitter for the latest Tweet of a given author.
	 * If there are no tweets from the given author, then the 
	 * method returns null. 
	 * O(1)  
	 */
	public Tweet latestTweetByAuthor(String author) {

		return table3.get(author);
	}


	/**
	 * Search this Twitter for Tweets by `date' and return an 
	 * ArrayList of all such Tweets. If there are no tweets on 
	 * the given date, then the method returns null.
	 * O(1)
	 */
	public ArrayList<Tweet> tweetsByDate(String date) {

		return table1.get(date);
		
	}


	/**
	 * Returns an ArrayList of words (that are not stop words!) that
	 * appear in the tweets. The words should be ordered from most 
	 * frequent to least frequent by counting in how many tweet messages
	 * the words appear. Note that if a word appears more than once
	 * in the same tweet, it should be counted only once. 
	 */

	public ArrayList<String> trendingTopics() {

	
		for(Tweet t:this.tweet) {

			ArrayList<String> list=getWords(t.getMessage());

			for(String word:list) {
				
				if(this.table2.get(word.toLowerCase())!=null) {
					
					continue;
				}

				else {

					if(list.lastIndexOf(word)!=list.indexOf(word)) {

						continue;
					}

					else {

						if(this.IntMap.get(word.toLowerCase())==null) {

							this.IntMap.put(word.toLowerCase(),1);
						}

						else {

							this.IntMap.put(word.toLowerCase(),IntMap.get(word.toLowerCase())+1);

						}

					}
					
				}
			}

		}

		return MyHashTable.fastSort(IntMap);
	}



	/**
	 * An helper method you can use to obtain an ArrayList of words from a 
	 * String, separating them based on apostrophes and space characters. 
	 * All character that are not letters from the English alphabet are ignored. 
	 */
	private static ArrayList<String> getWords(String msg) {
		msg = msg.replace('\'', ' ');
		String[] words = msg.split(" ");
		ArrayList<String> wordsList = new ArrayList<String>(words.length);
		for (int i=0; i<words.length; i++) {
			String w = "";
			for (int j=0; j< words[i].length(); j++) {
				char c = words[i].charAt(j);
				if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))
					w += c;

			}
			wordsList.add(w);
		}
		return wordsList;
	}



}
