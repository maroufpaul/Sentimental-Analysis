import java.io.InputStream;
import java.util.*;

public class SentimentAnalysis {
    public static void main(String[] args)
    {
        final boolean PRINT_TREES = true;  // whether or not to print extra info about the maps.

        BSTMap<String, Integer> wordFreqs = new BSTMap<String, Integer>();
        BSTMap<String, Integer> wordTotalScores = new BSTMap<String, Integer>();
        Set<String> stopwords = new TreeSet<String>();

        System.out.print("Enter filename: ");
        Scanner scan = new Scanner(System.in);
        String filename = scan.nextLine();

        processFile(filename, wordFreqs, wordTotalScores);

        System.out.println("Number of words is: " + wordFreqs.size());
        System.out.println("Height of the tree is: " + wordFreqs.height());

        if (PRINT_TREES)
        {
            System.out.println("Preorder:  " + wordFreqs.preorderKeys());
            System.out.println("Inorder:   " + wordFreqs.inorderKeys());
            System.out.println("Postorder: " + wordFreqs.postorderKeys());
            printFreqsAndScores(wordFreqs, wordTotalScores);
        }

        removeStopWords(wordFreqs, wordTotalScores, stopwords);


        System.out.println("After removing stopwords:");
        System.out.println("Number of words is: " + wordFreqs.size());
        System.out.println("Height of the tree is: " + wordFreqs.height());

        if (PRINT_TREES)
        {
            System.out.println("Preorder:  " + wordFreqs.preorderKeys());
            System.out.println("Inorder:   " + wordFreqs.inorderKeys());
            System.out.println("Postorder: " + wordFreqs.postorderKeys());
            printFreqsAndScores(wordFreqs, wordTotalScores);
        }

        while (true)
        {
            System.out.print("\nEnter a new review to analyze: ");
            String line = scan.nextLine();
            if (line.equals("quit")) break;

            int wordsCtr = 0; // ctr to keep track of number of words
            double avgReview = 0; // sum of the averages of all word.
            String[] words = line.split(" ");
            for (int i = 0; i< words.length; i++)
            {
                if(wordFreqs.containsKey(words[i]) && !stopwords.contains(words[i]))
                {
                    double wordFreq = wordFreqs.get(words[i]); // get frequency of word.
                    double wordTotalScr = wordTotalScores.get(words[i]); // get score of word.
                    double avgWordSent = wordTotalScr/wordFreq; // calculating average sentiment of each word
                    System.out.println("The average sentiment of "+ words[i] +" is "+ avgWordSent );
                    avgReview += avgWordSent; // sum of the averages of all word.
                    wordsCtr ++;
                }
                 else if(stopwords.contains(words[i])){ // if it's a stop word
                    System.out.println("Skipping "+ words[i]+" (stopword)");
                }
                else if (!wordFreqs.containsKey(words[i])){ // if it doesn't exist in the wordFreq map (not seen this word before).
                    System.out.println("Skipping "+ words[i]+" (never seen before)");
                }
            }
            double avgRevSentScr = avgReview/wordsCtr; // sentiment score of the review.

            System.out.println("Sentiment score for this review is " + avgRevSentScr);

        }
    }

    /**
     * Read the file specified to add proper items to the word frequencies and word scores maps.
     */
    private static void processFile(String filename,
                                    BSTMap<String, Integer> wordFreqs, BSTMap<String, Integer> wordTotalScores)
    {
        InputStream is = SentimentAnalysis.class.getResourceAsStream(filename);
        if (is == null) {
            System.err.println("Bad filename: " + filename);
            System.exit(1);
        }
        Scanner scan = new Scanner(is);

        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            String[] words = line.split(" ");

            for (int i = 1; i< words.length; i++)
            {
                int score = Integer.parseInt(words[0]); // Store the Ist word of words as gives us score

                if(!wordFreqs.containsKey(words[i])){ // if word is not in wordFreq. Put it there
                    wordFreqs.put(words[i],1);
                }
                else {
                    int oldfreq = wordFreqs.get(words[i]);
                    wordFreqs.put(words[i],oldfreq+1); // if word already exits wordFreq. update the frequency of word.
                }
                if (!wordTotalScores.containsKey(words[i])){ // if word is not in wordTotalScores. Put it there
                    wordTotalScores.put(words[i],score);
                }
                else {
                    int oldfreq2 = wordTotalScores.get(words[i]);
                    wordTotalScores.put(words[i],oldfreq2 + score);  // if word already exits wordTotalScores. update the score of word.
                }
            }
        }
        scan.close();
    }

    /**
     * Print a table of the words found in the movie reviews, along with their frequencies and total scores.
     * Hint: Call wordFreqs.inorderKeys() to get a list of the words, and then loop over that list.
     */
    private static void printFreqsAndScores(BSTMap<String, Integer> wordFreqs, BSTMap<String, Integer> wordTotalScores)
    {
        List<String> words = wordFreqs.inorderKeys(); // List of words in inorder Transversal

        for(int i = 0 ; i< words.size(); i++)
        {
           int freq =  wordFreqs.get(words.get(i)); // get the frequency of each word
           int score = wordTotalScores.get(words.get(i)); // get the score of each word.

            System.out.println("Word: "+ words.get(i) + ", frequency: " + freq + ", total score: " + score );
        }
    }

    /**
     * Read the stopwords.txt file and add each word to the stopwords set.  Also remove each word from the
     * word frequencies and word scores maps.
     */
    private static void removeStopWords(BSTMap<String, Integer> wordFreqs,
                                        BSTMap<String, Integer> wordTotalScores, Set<String> stopwords)
    {
        InputStream is = SentimentAnalysis.class.getResourceAsStream("stopwords.txt");
        if (is == null) {
            System.err.println("Bad filename: " + "stopwords.txt");
            System.exit(1);
        }
        Scanner scan = new Scanner(is);

        while (scan.hasNextLine()) {
            String word = scan.nextLine();
            stopwords.add(word); // adding the word to the stopwords set.
            wordFreqs.remove(word);// removing thw word from the wordFreq
            wordTotalScores.remove(word); // removing the word from the worldTotalScores.
        }
        scan.close();
    }
}
