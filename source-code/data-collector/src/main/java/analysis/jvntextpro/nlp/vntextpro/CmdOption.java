/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.nlp.vntextpro;

import org.kohsuke.args4j.Option;

/**
 *
 * @author hieupx
 */
public class CmdOption {
    // VnTextSegmenter -sentseg/-texttoken/-wordseg 
    //-indir <input dir> -outdir <output dir> -ext <input file extension>

    // VnTextSegmenter -sentseg/-texttoken/-wordseg 
    //-infile <input file>
    
    @Option(name="-sentseg", usage="perform sentence segmentation")
    public boolean sentseg = false;
    
    @Option(name="-texttoken", usage="perform text tokenization")
    public boolean texttoken = false;
    
    @Option(name="-wordseg", usage="perform word segmentation")
    public boolean wordseg = false;
    
    @Option(name="-postag", usage="perform part-of-speech tagging")
    public boolean postag = false;
    
    @Option(name="-phrasechunk", usage="perform phrase chunking")
    public boolean phrasechunk = false;
    
    @Option(name="-ner", usage="perform named entity recognition")
    public boolean ner = false;
    
    @Option(name="-indir", usage="the input data directory")
    public String indir = "";
    
    @Option(name="-outdir", usage="the output directory")
    public String outdir = "";
    
    @Option(name="-infile", usage="input filename")
    public String infile = "";
    
    @Option(name="-ext", usage="input file extension")
    public String ext = ".*";
    
    @Option(name="-extout", usage="output file extension")
    public String extout = ".out";
}
