package com.example.jtopia.tagger;

import com.example.jtopia.configurations.JTopiaConfiguration;
import com.example.jtopia.container.TaggedTermsContainer;
import com.example.jtopia.models.TermDocument;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class StanfordTagger extends DefaultTagger implements Tagger {

    private MaxentTagger maxentTagger = null;

    public StanfordTagger() {
        this.maxentTagger = new MaxentTagger(JTopiaConfiguration.getModelFileLocation());
    }

    public TermDocument tag(TermDocument termDocument) {
        TaggedTermsContainer taggedTermsContainer = new TaggedTermsContainer();
        for (String term : termDocument.getTerms()) {
            String tag = maxentTagger.tagString(term);
            // Since Stanford POS has tagged terms like establish_VB , we only need the POS tag
            // Some POS tags in Stanford has special charaters at end like their/PRP$
            try {
                tag = tag.split("_")[1].replaceAll("\\$", "");
                taggedTermsContainer.addTaggedTerms(term, tag, term);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        termDocument.setTaggedContainer(taggedTermsContainer);
        termDocument = postTagProcess(termDocument);
        return termDocument;
    }

}
