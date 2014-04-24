package edu.kit.imi.knoholem.cu.rules.ontology;

import edu.kit.imi.knoholem.cu.rules.process.processors.Function;
import edu.kit.imi.knoholem.cu.rulesconversion.SWRLRule;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:kiril.tonev@kit.edu">Tonev</a>
 */
public class RuleAnnotator implements Function<SWRLRule, Set<OWLAnnotation>> {

    private final OntologyContext ontology;

    private int ruleCounter;

    public RuleAnnotator(OntologyContext ontologyContext) {
        this(ontologyContext, 0);
    }

    public RuleAnnotator(OntologyContext ontologyContext, int initialCount) {
        this.ontology = ontologyContext;
        this.ruleCounter = initialCount;
    }

    @Override
    public Set<OWLAnnotation> apply(SWRLRule input) {
        return ruleAnnotations(getSuggestionText(input), input.getMetadata().getWeight());
    }

    public int incrementRuleId() {
        ruleCounter++;
        return ruleCounter;
    }

    private Set<OWLAnnotation> ruleAnnotations(String suggestionText, double weight) {
        Set<OWLAnnotation> annotations = new HashSet<OWLAnnotation>();
        annotations.addAll(ruleIDAnnotation(incrementRuleId()));
        annotations.addAll(ruleSuggestionAnnotation(suggestionText));
        annotations.addAll(ruleWeightAnnotation(weight));
        return annotations;
    }

    private Set<OWLAnnotation> ruleIDAnnotation(int ruleId) {
        OWLAnnotation idLabel = ontology.getFactory().getOWLAnnotation(
                ontology.getFactory().getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI()),
                ontology.getFactory().getOWLLiteral(ruleId));
        return Collections.singleton(idLabel);
    }

    private Set<OWLAnnotation> ruleSuggestionAnnotation(String suggestionText) {
        OWLAnnotation suggestion = ontology.getFactory().getOWLAnnotation(
                ontology.getFactory().getOWLAnnotationProperty(ontology.iri("hasSuggestion")),
                ontology.getFactory().getOWLLiteral(suggestionText));
        return Collections.singleton(suggestion);
    }

    private Set<OWLAnnotation> ruleWeightAnnotation(double weight) {
        OWLAnnotation suggestion = ontology.getFactory().getOWLAnnotation(
                ontology.getFactory().getOWLAnnotationProperty(ontology.iri("hasWeight")),
                ontology.getFactory().getOWLLiteral(weight));
        return Collections.singleton(suggestion);
    }

    private String getSuggestionText(SWRLRule input) {
        return input.getMetadata().getType() + " " + input.getMetadata().getReduction();
    }

}