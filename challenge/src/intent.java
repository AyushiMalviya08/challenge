import java.util.Properties;
import java.util.Scanner;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class SentimentAnalyzer {
    private static StanfordCoreNLP pipeline;
    
    // initialize StanfordCoreNLP pipeline
    static {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);
    }
    
    // determine sentiment of a given sentence
    private static String getSentiment(String sentence) {
        Annotation annotation = pipeline.process(sentence);
        for (CoreMap sentenceAnnotation : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            String sentiment = sentenceAnnotation.get(SentimentCoreAnnotations.SentimentClass.class);
            return sentiment;
        }
        return "Neutral"; // default to neutral sentiment if unable to determine
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String sentence = "";
        while (!sentence.equals("q")) {
            System.out.print("Enter a sentence (press q to quit): ");
            sentence = scanner.nextLine();
            if (!sentence.equals("q")) {
                String sentiment = getSentiment(sentence);
                System.out.println("Sentiment: " + sentiment);
            }
        }
        scanner.close();
    }
}
