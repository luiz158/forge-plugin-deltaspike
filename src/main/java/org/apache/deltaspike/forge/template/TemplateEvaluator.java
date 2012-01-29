package org.apache.deltaspike.forge.template;

import com.google.common.io.CharStreams;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;

/**
 * @author Rudy De Busscher
 */
public class TemplateEvaluator {

    private final static TemplateEvaluator evaluator = new TemplateEvaluator();

    private VelocityContext context;

    private TemplateEvaluator() {
        try {
            Velocity.init();
            context = null;
        } catch (Exception e) {
            //throw new VelocityInitializationException(e);
            e.printStackTrace();
        }
    }

    public static TemplateEvaluator getInstance() {
        return evaluator;
    }

    public void addParameters(final String key, final String value) {
        if (context == null) {
            context = new VelocityContext();
        }

        context.put(key, value);

    }


    public InputStream evaluate(final String velocityTemplateName) {
        InputStream result = null;

        try {
            CharArrayWriter target = new CharArrayWriter();

            Reader template = new InputStreamReader(TemplateEvaluator.class.getResourceAsStream(velocityTemplateName));
            Velocity.evaluate(context, target, velocityTemplateName, template);

            target.flush();
            target.close();

            result = new ByteArrayInputStream(target.toString().getBytes());
        } catch (IOException e) {
            // FIXME
            e.printStackTrace();
        }

        return result;
    }
    
    public String evaluateToString(final String velocityTemplateName) {
        String result = "";
        InputStream stream = evaluate(velocityTemplateName);
        try {
            result = CharStreams.toString(new InputStreamReader(stream));

        } catch (IOException e) {
            // FIXME
            e.printStackTrace();
        }
        return result;
    }
}