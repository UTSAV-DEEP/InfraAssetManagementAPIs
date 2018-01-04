package common.util;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import common.util.tools.LabelSeeker;
import common.util.tools.MeansBuilder;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.text.documentiterator.FileLabelAwareIterator;
import org.deeplearning4j.text.documentiterator.LabelAwareIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.primitives.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.api.Play;
import play.libs.Json;

import java.io.File;
import java.util.List;


public class ParagraphClassifier {

    private static final Logger LOG = LoggerFactory.getLogger(ParagraphClassifier.class);

    private ParagraphVectors paragraphVectors;
    private LabelAwareIterator iterator;
    private TokenizerFactory tokenizerFactory;
    private MeansBuilder meanBuilder;
    private LabelSeeker seeker;

    private static ParagraphClassifier instance = new ParagraphClassifier();

    private ParagraphClassifier() {
        File file = Play.current().getFile("conf/paravec/labeled");
        // build a iterator for our dataset
        iterator = new FileLabelAwareIterator.Builder()
                .addSourceFolder(file)
                .build();
        tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());
        // ParagraphVectors training configuration
        paragraphVectors = new ParagraphVectors.Builder()
                .learningRate(0.025)
                .minLearningRate(0.001)
                .batchSize(1000)
                .epochs(20)
                .iterate(iterator)
                .trainWordVectors(true)
                .tokenizerFactory(tokenizerFactory)
                .build();
        paragraphVectors.fit(); // Start model training
        meanBuilder = new MeansBuilder(
                (InMemoryLookupTable<VocabWord>)paragraphVectors.getLookupTable()
                ,tokenizerFactory);
        seeker = new LabelSeeker(iterator.getLabelsSource().getLabels(),
                (InMemoryLookupTable<VocabWord>) paragraphVectors.getLookupTable());
    }

    public static ParagraphClassifier getInstance() {
        return instance;
    }

    public ObjectNode getLabelsForComment(String text) {
        INDArray docAsCentroid = meanBuilder.textAsVector(text);
        List<Pair<String,Double>> scores = seeker.getScores(docAsCentroid);
        ObjectNode textLabels = Json.newObject();
        ArrayNode labels = Json.newArray();
        for (Pair<String, Double> score: scores) {
            ObjectNode label = Json.newObject();
            LOG.info("        " + score.getFirst() + ": " + score.getSecond());
            label.put(score.getFirst(), score.getSecond());
            labels.add(label);
        }
        textLabels.set("labels", labels);
        return textLabels;
    }
}
