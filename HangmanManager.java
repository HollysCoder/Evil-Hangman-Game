import java.util.Set;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Random;


/**
 * Manages the details of EvilHangman. This class keeps
 * tracks of the possible words from a dictionary during
 * rounds of hangman, based on guesses so far.
 *
 */
public class HangmanManager {

	// instance vars
	private boolean debugOn;
	private ArrayList<String> wordsList;
	private ArrayList<String> activeList;
	private ArrayList<Character> guessedList;
	private int numGuessLeft;
	private int wordLen;
	private HangmanDifficulty diff;
	private String currPattern;
	private int numGuessed;

	
	/**
	 * Create a new HangmanManager from the provided set of words and phrases.
	 * pre: words != null, words.size() > 0
	 * @param words A set with the words for this instance of Hangman.
	 * @param debugOn true if we should print out debugging to System.out.
	 */
	public HangmanManager(Set<String> words, boolean debugOn) {
		// This if statement checks the preconditions.
		if (words == null || words.size() <= 0) {
			throw new IllegalArgumentException("Words can't be null or the length of the ");
		}
		this.debugOn = debugOn;
		wordsList = new ArrayList<>();
		activeList = new ArrayList<>();
		guessedList = new ArrayList<>();
		
		// This for loop make a deep copy of the words set into ArrayList.
		for (String word : words) {
			wordsList.add(word);
		}	
	}

	
	/**
	 * Create a new HangmanManager from the provided set of words and phrases. 
	 * Debugging is off.
	 * pre: words != null, words.size() > 0
	 * @param words A set with the words for this instance of Hangman.
	 */
	public HangmanManager(Set<String> words) {
		// This if statement checks that words set is not null and the size of the words set is greater than 1 or not.
		// if it does not satisfies the preconditions.
		if (words == null || words.size() <= 0) {
			throw new IllegalArgumentException("Words can't be null or the length of the ");
		}
		debugOn = false;
		wordsList = new ArrayList<>();
		activeList = new ArrayList<>();
		guessedList = new ArrayList<>();
		
		// This for loop make a deep copy of the words set into ArrayList.
		for (String word : words) {
			wordsList.add(word);
		}
	}


	/**
	 * Get the number of words in this HangmanManager of the given length.
	 * pre: none
	 * @param length The given length to check.
	 * @return the number of words in the original Dictionary with the given length
	 */
	public int numWords(int length) {
		int count = 0;

		// This for loop and if statement checks how many word with given length exists
		// in the wordsList(dictionary) 
		for (String word : wordsList) {
			if (word.length() == length) {
				count++;
			}
		}
		return count;
	}


	/**
	 * Get for a new round of Hangman. Think of a round as a complete game of Hangman.
	 * pre: wordLen > 0, numGuesses > 0.
	 * @param wordLen the length of the word to pick this time. numWords(wordLen) > 0
	 * @param numGuesses the number of wrong guesses before the player loses the round. numGuesses >= 1
	 * @param diff The difficulty for this round.
	 */
	public void prepForRound(int wordLen, int numGuesses, HangmanDifficulty diff) {
		// This if statement checks the preconditions.
		if (numWords(wordLen) <= 0 || numGuesses <= 0) {
			throw new IllegalStateException("a word length and number of guess should greater than 0");
		}
		currPattern = "";

		// This for loop make a default pattern based on the word length.
		for (int index = 0; index < wordLen; index++) {
			currPattern += "-";
		}
		numGuessLeft = numGuesses;
		this.wordLen = wordLen;
		this.diff = diff;
		activeList.clear();
		guessedList.clear();
		numGuessed = 0;

		// This for loop and if statement checks all the words in the dictionary 
		// to find the words with the given length. Store them in the activeList.
		for (String check : wordsList) {
			if (check.length() == wordLen) {
				activeList.add(check);
			}
		}
	}


	/**
	 * The number of words still possible (live) based on the guesses so far. Guesses will eliminate possible words.
	 * @return the number of words that are still possibilities based on the original dictionary and the guesses so far.
	 */
	public int numWordsCurrent() {
		return activeList.size();
	}


	/**
	 * Get the number of wrong guesses the user has left in this round (game) of Hangman.
	 * @return the number of wrong guesses the user has left in this round (game) of Hangman.
	 */
	public int getGuessesLeft() {
		return numGuessLeft;
	}


	/**
	 * Return a String that contains the letters the user has guessed so far during this round.
	 * The String is in alphabetical order. The String is in the form [let1, let2, let3, ... letN].
	 * For example [a, c, e, s, t, z]
	 * @return a String that contains the letters the user has guessed so far during this round.
	 */
	public String getGuessesMade() {
		// If the size is 0, then return [](empty list).
		if (guessedList.size() == 0) {
			return "[]";
		}
		String result = "[" + guessedList.get(0);

		// This for loop adds the already guessed character to the return string.
		for (int index = 1; index < guessedList.size(); index++) {
			result +=  ", " + guessedList.get(index);
		}
		result += "]";
		return result;
	}


	/**
	 * Check the status of a character.
	 * @param guess The characater to check.
	 * @return true if guess has been used or guessed this round of Hangman, false otherwise.
	 */
	public boolean alreadyGuessed(char guess) { 
		// This if statement return true if guess has been used 
		// or guessed this round of Hangman, false otherwise.
		if (guessedList.contains(guess)) {
			return true;
		}
		return false;
	}


	/**
	 * Get the current pattern. The pattern contains '-''s for unrevealed (or guessed)
	 * characters and the actual character for "correctly guessed" characters.
	 * @return the current pattern.
	 */
	public String getPattern() {
		return currPattern;
	}


	// pre: !alreadyGuessed(ch)
	// post: return a tree map with the resulting patterns and the number of
	// words in each of the new patterns.
	// the return value is for testing and debugging purposes
	/**
	 * Update the game status (pattern, wrong guesses, word list), based on the give
	 * guess.
	 * @param guess pre: !alreadyGuessed(ch), the current guessed character
	 * @return return a tree map with the resulting patterns and the number of
	 * words in each of the new patterns.
	 * The return value is for testing and debugging purposes.
	 */
	public TreeMap<String, Integer> makeGuess(char guess) {
		numGuessed++;

		// This if statement checks the preconditions.
		if (alreadyGuessed(guess)) {
			throw new IllegalStateException("You already guessed it!");
		}
		guessedList.add(guess);
		TreeMap<String, Integer> result = new TreeMap<>();
		TreeMap<String, ArrayList<String>> guessedMap = new TreeMap<>();
		guessedMap = makeFamilyList(guess);
		String pickedFamily = "";
		
		// This for loop stores the keys(patterns) and their values(number of the pattern).
		for (String key : guessedMap.keySet()) {
			result.put(key, guessedMap.get(key).size());
		}
		
		// This if statement checks the conditions such as difficulty and number of the user guessed,
		// and decided to send the hardest family or second hardest family.
		if ((diff.equals(HangmanDifficulty.MEDIUM) && numGuessed % 4 == 0) 
				|| (diff.equals(HangmanDifficulty.EASY) && numGuessed % 2 == 0)) {
			pickedFamily = makeHardList(guessedMap).get(1);
		} else {
			pickedFamily = makeHardList(guessedMap).get(0);
		}
		currPattern = pickedFamily;
		numGuessLeft--;
		activeList = guessedMap.get(pickedFamily);
		return result;
	}
	
	
	/**
	 * This method makes an ArrayList that has hardest and second hardest family patterns.
	 * @param guessedMap is the treeMap that has a key(pattern) and value(list of the words).
	 * @return the ArrayList such that index 0 element is the hardest and index 1 element is the second hardest.
	 */
	public ArrayList<String> makeHardList(TreeMap<String, ArrayList<String>> guessedMap) {
		ArrayList<String> twoDifficultWords = new ArrayList<>();
		String hardest = pickHardest(guessedMap, "");
		String secondHardest = pickHardest(guessedMap, hardest);
		twoDifficultWords.add(hardest);
		
		// This if statement checks that if the secondHardest pattern does not exist(""), 
		// then store the hardest pattern as the second hardest pattern.
		if (!secondHardest.equals("")) {
			twoDifficultWords.add(secondHardest);
		} else {
			twoDifficultWords.add(hardest);
		}	
		return twoDifficultWords;
	}

	
	/**
	 * This method picks the hardest(or second hardest) family and return that pattern. 
	 * @param guessedMap is the treeMap that has a key(pattern) and value(list of the words).
	 * @param except is used to exclude the hardest pattern when it tries to find the second hardest.
	 * @return the pattern that has the hardest(or second hardest) family list.
	 */
	public String pickHardest(TreeMap<String, ArrayList<String>> guessedMap, String except) {
		int max = 0;
		String maxPattern = "";
		Set<String> keys = guessedMap.keySet();
		
		// This for loop checks the all the possible words. 
		for (String key : keys) {
			
			// This if statement checks that is there any word that need to be excluded for other if statements.
			if (!key.toString().equals(except)) {
				
				/*
				 *  This if statement check the size of the each family list.
				 *  If there are two or more families with the maximum number 
				 * of elements break the tie by picking the one that reveals the fewest characters
				 * If they are still tied then use lexigraphical ordering of Strings 
				 * to find the hardest(negative number).
				 */
				if (guessedMap.get(key).size() > max) {
					max = guessedMap.get(key).size();
					maxPattern = key;
				} else if(guessedMap.get(key).size() == max && getNumDash(key) > getNumDash(maxPattern)) {
					maxPattern = key;
				} else if (guessedMap.get(key).size() == max && getNumDash(key) == getNumDash(maxPattern) 
						&& key.compareTo(maxPattern) < 0) {
					maxPattern = key;
				}
			}
		}
		return maxPattern;
	}


	/**
	 * This method count the number of dashes("-") and return the number of it.
	 * @param key is the given pattern.
	 * @return the number of dashes in the pattern.
	 */
	public int getNumDash(String key) {
		int count = 0;
		
		// This for loop and if statement count the number of dashes in the given pattern.
		for (int index = 0; index < wordLen; index++) {
			if (key.charAt(index) == '-') {
				count++;
			}
		}
		return count;
	}
	
	
	/**
	 * This method make the Family list of each possible patterns.
	 * @param guess is the character that the user guessed.
	 * @return it return the TreeMap such that contains key(pattern) and the value(list of the words).
	 */
	public TreeMap<String, ArrayList<String>> makeFamilyList(char guess) {
		TreeMap<String, ArrayList<String>> guessedMap = new TreeMap<>();
		
		// This for loop checks the all the words in the active word list.
		for (String check : activeList) {
			
			// This if statement checks that the current word contains the guess character or not.
			if (check.indexOf(guess) != -1) {
				String pattern = makePattern(check, guess);
				
				// This if Statement checks that the current pattern exists in the guessedMap or not.
				// If it does not exists then make a new Arraylist with a current word and put it.
				// Else just add a current word to the list.
				if (guessedMap.get(pattern) == null) {
					ArrayList<String> makeArray = new ArrayList<>();
					makeArray.add(check);
					guessedMap.put(pattern, makeArray);
				} else 
					guessedMap.get(pattern).add(check);
			} else {
				
				// This if Statement checks that the current pattern exists in the guessedMap or not.
				// If it does not exists then make a new Arraylist with a current word and put it.
				// Else just add a current word to the list.
				if (guessedMap.get(currPattern) == null) {
					ArrayList<String> makeArray = new ArrayList<>();
					makeArray.add(check);
					guessedMap.put(currPattern, makeArray);
				} else 
					guessedMap.get(currPattern).add(check);
			}
		}
		
		// This if statement stores new pattern to the currPattern variable only when the size of 
		// the guessedMap is equal to 1.
		if (guessedMap.size() == 1) 
			currPattern = guessedMap.firstKey();
		return guessedMap;
	}
	
	
	/**
	 * This method creates the new pattern.
	 * @param check is a current word.
	 * @param guess is a guessed character by the user.
	 * @return
	 */
	public String makePattern(String check, char guess) {
		StringBuilder patternBuild = new StringBuilder();
		patternBuild.append(currPattern);
		
		// This for loop and the if statement checks that the current word's current index
		// contains the given character or not.
		// If it has the given character at the certain index, then replace the dash to that character.
		for (int index = 0; index < wordLen; index++) {
			if (check.charAt(index) == guess) {
				patternBuild.replace(index, index + 1, "" + guess);
			}
		}
		return patternBuild.toString(); 
	}


	/**
	 * Return the secret word this HangmanManager finally ended up picking for this round.
	 * If there are multiple possible words left one is selected at random.
	 * <br> pre: numWordsCurrent() > 0
	 * @return return the secret word the manager picked.
	 */
	public String getSecretWord() {
		// This if statement checks the precondition.
		if (numWordsCurrent() <= 0) {
			throw new IllegalArgumentException("There does not exists any words in the activeList");
		}
		
		// This if statement checks that if there is only one word left in the active list
		// then return that word.
		// If there is more than one word, then randomly pick one word among them.
		if (numWordsCurrent() == 1) {
			return activeList.get(0);
		} else {
			Random rand = new Random();
			int randPick = rand.nextInt(activeList.size());
			return activeList.get(randPick);
		}
	}
}