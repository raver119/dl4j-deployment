package org.deeplearning4j.serving;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.deeplearning4j.classes.Sentiment;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.api.buffer.DataType;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

@Slf4j
public class ModelHolder {
    private static final ModelHolder INSTANCE = new ModelHolder();

    private ComputationGraph sentimentModel;

    private ModelHolder() {
        // acquire model from somewhere here
    }


    public static ModelHolder getInstance() {
        return INSTANCE;
    }

    protected INDArray featuresFromText(@NonNull String text) {
        return Nd4j.create(DataType.FLOAT, 1, 32, 128);
    }

    /**
     * This method takes text in, converts it to INDArray, and feeds it into neural networks to get Sentiment evaluation out of it
     *
     * PLEASE NOTE: synchronized is intentional here, and it's here just to simplify this toy.
     * @param text
     * @return
     */
    public synchronized Sentiment evaluateSentiment(@NonNull String text) {
        val features = featuresFromText(text);
        val output = sentimentModel.output(features)[0];
        val resp = output.argMax(-1);

        return Sentiment.values()[0];
    }
}
