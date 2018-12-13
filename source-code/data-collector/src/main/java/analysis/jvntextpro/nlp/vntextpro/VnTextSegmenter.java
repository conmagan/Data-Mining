/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.nlp.vntextpro;

import java.io.*;
import java.util.List;
import analysis.jvntextpro.lib.filesystem.DirFileUtil;
import analysis.jvntextpro.lib.string.StrUtil;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

/**
 *
 * @author hieupx
 */
public class VnTextSegmenter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        CmdOption option = new CmdOption();
        CmdLineParser parser = new CmdLineParser(option);
        
        try {
            if (args.length == 0) {
                showHelp(parser);
                return;
            }
            
            parser.parseArgument(args);
            
            perform(parser, option);
            
        } catch (CmdLineException cle) {
            System.out.println("Command line error: " + cle.getMessage());
            showHelp(parser); 
            
        } catch (Exception ex) {
            System.out.println("Error in main: " + ex.getMessage());
        }
    }
    
    public static void perform(CmdLineParser parser, CmdOption option) {
        // VnTextSegmenter -sentseg/-texttoken/-wordseg 
        //-indir <input dir> -outdir <output dir> -ext <input file extension>
        
        // VnTextSegmenter -sentseg/-texttoken/-wordseg 
        //-infile <input file>

        if (option.indir.isEmpty() && option.infile.isEmpty()) {
            System.out.println("Please provide input data filename or input data directory.");
            showHelp(parser);
            return;
        }
        
        if (!option.indir.isEmpty() && !option.infile.isEmpty()) {
            System.out.println("Please provide input data filename or input data directory exclusively.");
            showHelp(parser);
            return;
        }
        
        VnTextPro textPro = new VnTextPro(option.sentseg, option.texttoken, option.wordseg);
        textPro.init();
        
        if (!option.indir.isEmpty()) {
            if (option.outdir.isEmpty()) {
                option.outdir = option.indir;
            }
            
            List<String> infiles = DirFileUtil.listDir(option.indir, option.ext);
            
            if (infiles == null || infiles.isEmpty()) {
                System.out.println("No input files provided.");
                showHelp(parser);
                return;
            }
            
            int count = infiles.size();
            for (int i = 0; i < count; i++) {
                String filename = infiles.get(i);
                String infile = DirFileUtil.getFullFilename(option.indir, filename);
                String outfile = DirFileUtil.getFullFilename(option.outdir, filename + option.extout);
                
                System.out.println("Processing file " + (i + 1) + " of " + count + ": " + infile);
                perform(textPro, option, infile, outfile);
            }
        }
        
        if (!option.infile.isEmpty()) {
            System.out.println("Processing file: " + option.infile);
            perform(textPro, option, option.infile, option.infile + option.extout);
        }
    }
    
    public static void perform(VnTextPro textPro, CmdOption option, 
            String infile, String outfile) {
        BufferedReader fin;
        BufferedWriter fout;
        
        try {
            fin = new BufferedReader(
                    new InputStreamReader(
                    new FileInputStream(infile), "UTF8"));
            
            fout = new BufferedWriter(
                    new OutputStreamWriter(
                    new FileOutputStream(outfile), "UTF8"));
            
            String line;
            while ((line = fin.readLine()) != null) {
                line = StrUtil.normalizeStr(line);
                if (line.isEmpty()) {
                    fout.write("\n");
                    continue;
                }
                
                List sents = null;
                
                if (option.wordseg) {
                    sents = textPro.segmentText(line);
                } else if (option.texttoken) {
                    sents = textPro.tokenizeText(line);
                } else if (option.sentseg) {
                    sents = textPro.segmentSent(line);
                } else {
                    // for future use: pos tagger or phrase chunker
                }
                
                if (sents != null) {
                    for (int i = 0; i < sents.size(); i++) {
                        fout.write((String)sents.get(i) + "\n");
                    }
                }
            }
            
            fin.close();
            fout.close();
            
        } catch (UnsupportedEncodingException ex) {
            System.err.println(ex.toString());
            System.exit(1);
        } catch (IOException ex) {
            System.err.println(ex.toString());
            System.exit(1);
        }        
    }
    
    public static void showHelp(CmdLineParser parser) {
        System.out.println("VnTextSegmenter [options ...] [arguments ...]");
        parser.printUsage(System.out);
    }
}

