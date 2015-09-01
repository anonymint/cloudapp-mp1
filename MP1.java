import java.io.File;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
    "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
    "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
    "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
    "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
    "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
    "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
    "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
    "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
    "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];

        Integer[] indexToReadFrom = getIndexes();
        List stopWordsArrayList = Arrays.asList(stopWordsArray);
        List<String> lines = Files.readAllLines(Paths.get(inputFileName),Charset.defaultCharset());

        Map<String,Integer> wordCount = new HashMap<>();
        for (int index : indexToReadFrom) {
            String[] words = lines.get(index).split("["+Pattern.quote(delimiters)+"]+");

            if (words.length > 0) {
                for (String wordBefore : words) {
                    String word = wordBefore.toLowerCase();
                    if (stopWordsArrayList.contains(word))
                        continue;

                    if (wordCount.containsKey(word)) {
                        wordCount.put(word, wordCount.get(word)+1);
                    } else {
                        wordCount.put(word, 1);
                    }
                }
            }
        }

        List sort = new LinkedList<>(wordCount.entrySet());
        Collections.sort(sort, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Comparable valueA = (Comparable) (((Map.Entry) o1).getValue());
                Comparable valueB = (Comparable) (((Map.Entry) o2).getValue());

                if (valueA.compareTo(valueB) == 0) {
                    return ((Comparable) ((Map.Entry)o1).getKey()).compareTo(((Map.Entry)o2).getKey());
                }

                return valueB.compareTo(valueA);
            }
        });

        // sort the top20 and return
        for (int i=0; i<20; i++) {
            ret[i] = (String)((Map.Entry)sort.get(i)).getKey();
        }

        return ret;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1){
            System.out.println("MP1 <User ID>");
        }
        else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item: topItems){
                System.out.println(item);
            }
        }
    }
}
