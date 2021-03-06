package bot.life

import edu.stanford.nlp.dcoref.CorefChain
import edu.stanford.nlp.dcoref.CorefCoreAnnotations
import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.ling.CoreLabel
import edu.stanford.nlp.pipeline.Annotation
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.semgraph.SemanticGraph
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations
import edu.stanford.nlp.trees.Tree
import edu.stanford.nlp.trees.TreeCoreAnnotations
import edu.stanford.nlp.util.CoreMap

import javax.annotation.PostConstruct

class NaturalLanguageService {
    StanfordCoreNLP pipeline

    @PostConstruct
    def initialize (){
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    }

    def analyze() {
        if (pipeline) {
            String text = "Which plant grows in New South Wales and has flower colour white." // Add your text here!

            // create an empty Annotation just with the given text
            Annotation document = new Annotation(text);

            // run all Annotators on this text
            pipeline.annotate(document);

            // these are all the sentences in this document
            // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
            List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

            for(CoreMap sentence: sentences) {
                // traversing the words in the current sentence
                // a CoreLabel is a CoreMap with additional token-specific methods
                for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                    // this is the text of the token
                    String word = token.get(CoreAnnotations.TextAnnotation.class);
                    log.debug(word)
                    // this is the POS tag of the token
                    String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                    // this is the NER label of the token
                    String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                }

                // this is the parse tree of the current sentence
                Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);

                // this is the Stanford dependency graph of the current sentence
                SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
            }

            // This is the coreference link graph
            // Each chain stores a set of mentions that link to each other,
            // along with a method for getting the most representative mention
            // Both sentence and token offsets start at 1!
            Map<Integer, CorefChain> graph =
                    document.get(CorefCoreAnnotations.CorefChainAnnotation.class);
        }
    }
}
