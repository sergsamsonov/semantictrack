package com.bugtrack.service.ticket;

import com.bugtrack.config.HibernateUtil;
import com.bugtrack.entity.lemma;
import com.bugtrack.entity.systvar;
import com.bugtrack.entity.ticket;
import com.bugtrack.service.acronymService;
import com.bugtrack.service.lemmaService;
import com.bugtrack.service.systvarService;
import edu.illinois.cs.cogcomp.nlp.lemmatizer.IllinoisLemmatizer;
import edu.mit.jwi.*;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.IIndexWord;
import java.io.*;
import java.lang.Math;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.lang.ClassLoader;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.POSModel;
import org.apache.lucene.analysis.core.*;
import org.apache.lucene.analysis.en.*;
import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.*;
import org.apache.lucene.analysis.util.*;
import org.apache.lucene.util.AttributeFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.MySQLCodec;
import org.owasp.esapi.Encoder;
import org.owasp.esapi.ESAPI;
import org.springframework.stereotype.Service;
import slib.sml.sm.core.measures.vector.*;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * The ticketService provides data structure
 * and methods to process tickets
 * @version 0.9.9 26 July 2016
 * @author  Sergey Samsonov
 */
@Service
public class ticketService {

    private Encoder encoder;
    private Codec codec;
    private IDictionary dict;
    private IllinoisLemmatizer Lemmatizer;
    private POSTaggerME tagger;
    private SessionFactory sessionFactory;
    private String POSModelPath;
    private String WNPath;

    private acronymService acrService = new acronymService();
    private lemmaService lemmaService = new lemmaService();
    private systvarService systvarService = new systvarService();

    private static final String[] Measures = {"CosineSimilarity"};
    private static final String WNPathStr = "WordNet-3.0/dict";
    private static final String POSModPathStr = "taggers/OpenNLP/en-pos-maxent.bin";


    /**
     * Constructs a ticketService. This will create Lemmatizer object,
     * Dictionary object to access WordNet, POSModel object
     */
    public ticketService() {
        try{
            this.sessionFactory = HibernateUtil.getSessionFactory();
        }catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        this.encoder = ESAPI.encoder();
        this.codec = new MySQLCodec(MySQLCodec.MYSQL_MODE);
        ClassLoader classLoader = getClass().getClassLoader();
        POSModelPath = classLoader.getResource(POSModPathStr).getPath();
        WNPath = classLoader.getResource(WNPathStr).getPath();
        Lemmatizer = new IllinoisLemmatizer(false, WNPath);
        try{
            URL url = new URL("file", null, WNPath);
            dict = new Dictionary(url);
        }catch(Exception e){
            e.printStackTrace();
        }
        InputStream modelIn = null;
        try {
            modelIn = new FileInputStream(POSModelPath);
            POSModel model = new POSModel(modelIn);
            tagger = new POSTaggerME(model);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (modelIn != null) {
                try {
                    modelIn.close();
                }
                catch (IOException e) {
                }
            }
        }
    }

    public void addEntity(ticket ticket) {
        if (ticket != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.save(ticket);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public void updateEntity(ticket ticket) {
        if (ticket != null) {
            Session session = sessionFactory.openSession();
            Transaction tr = null;
            try {
                tr = session.beginTransaction();
                session.update(ticket);
                tr.commit();
            } catch (HibernateException e) {
                if (tr != null) tr.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    public List<ticket> getAll(){
        List<ticket> resultList = new ArrayList<>();
        String hql = "from ticket t";
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Query q = session.createQuery(hql);
            resultList.addAll(q.list());
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return resultList;
    }

    /**
     * Returns list of ticket objects sent to specified user.
     *
     * @param   username   login of responsible user
     * @return             list of issue appointed to user with login from username
     */
    public List<ticket> getTicketsByResponsible(Integer userId) {
        List<ticket> resultList = new ArrayList<>();
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        try {
            tr = session.beginTransaction();
            Criteria ticketCriteria = session.createCriteria(ticket.class);
            ticketCriteria.add(Restrictions.eq("responsible", userId));
            ticketCriteria.addOrder(Order.asc("number"));
            resultList.addAll(ticketCriteria.list());
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return resultList;
    }

    /**
     * Returns ticket object with specified number.
     *
     * @param   number   number of ticket
     * @return           ticket object with specified number
     */
    public ticket getTicketByNumber(Integer number) {
        List<ticket> resultList = new ArrayList<>();
        Session session = sessionFactory.openSession();
        Transaction tr = null;
        ticket ticket = null;
        try {
            tr = session.beginTransaction();
            Criteria ticketCriteria = session.createCriteria(ticket.class);
            ticketCriteria.add(Restrictions.eq("number", number));
            ticket = (ticket) ticketCriteria.uniqueResult();
            tr.commit();
        } catch (HibernateException e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return ticket;
    }


    /**
     * Returns semantic search results.
     *
     * @param   usrreq             user request
     * @param   fldSim             name of ticket field to caclculate semantic similarity
     *                             between tickets and user request
     *                             Possible values:
     *                             ticket,
     *                             issuedescr,
     *                             solution,
     *                             solutiondet
     * @param   simMeasure         code of measure to calculate semantic
     *                             similarity between tickets and user request.
     *                             Measures codes:
     *                             1 - CosineSimilarity
     * @return                     array of found tickets
     */
    public ticket[] getTickets(String  usrreq,
                               String  fldSim,
                               Integer simMeasure) {
        if (usrreq.equals("")) {
            return null;
        }
        ticket[] result = null;
        try {
            String NonStopWords  = removeStopWords(usrreq);
            String SQLConditions = "";
            if (!NonStopWords.equals("")) {
                String newStr = getExtendedAcronyms(NonStopWords, "\\s");
                List<String> words = Pattern.compile("\\s")
                                            .splitAsStream(newStr)
                                            .filter(word -> ((word != null) && (!word.isEmpty())))
                                            .collect(toList());
                TreeMap<String, Word> lemmasMap = new TreeMap<>();
                TreeMap<String, String> tokensMap = getWords(words.stream()
                                                                  .distinct()
                                                                  .toArray(size -> new String[size]), lemmasMap);
                if ((tokensMap != null) && !tokensMap.isEmpty()) {
                    Integer wordsNum = words.size();
                    double[] valArray = words.stream()
                                             .map(term -> tokensMap.get(term))
                                             .filter(lemma -> lemma != null)
                                             .collect(groupingBy(lemma -> lemma,
                                                      TreeMap::new,
                                                      counting()))
                                             .entrySet()
                                             .stream()
                                             .mapToDouble(lemmaSet -> lemmasMap.get(lemmaSet.getKey()).getIdf() *
                                                                      (double) lemmaSet.getValue() / wordsNum)
                                             .toArray();
                    String fldName = getLemmasFldName(fldSim);
                    String encTableField = encoder.encodeForSQL(codec, fldName);
                    SQLConditions = lemmasMap.entrySet()
                                             .stream()
                                             .map(lemmaSet -> lemmaSet.getValue())
                                             .map(lemma -> encoder.encodeForSQL(codec, lemma.getLemma())
                                                        + ((lemma.getSyns() != null)&&!lemma.getSyns().isEmpty()?
                                                             lemma.getSyns()
                                                                  .stream()
                                                                  .map(syn -> encoder.encodeForSQL(codec, syn))
                                                                  .collect(Collectors.joining("*", "*", "")):""))
                                            .collect(Collectors.joining("*",
                                                     "MATCH (" + encTableField + ") AGAINST ('*",
                                                     "*' IN BOOLEAN MODE)"));
                    if ((SQLConditions != null) || (SQLConditions.isEmpty())) {
                        TreeMap<String, ticket> ticketsMap = new TreeMap();
                        Session session = sessionFactory.openSession();
                        Transaction tr = null;
                        try {
                            tr = session.beginTransaction();
                            Criteria tickCriteria = session.createCriteria(ticket.class);
                            tickCriteria.add(Restrictions.sqlRestriction(SQLConditions));
                            List<ticket> tickList = tickCriteria.list();
                            if ((tickList != null) && !tickList.isEmpty()) {
                                tickList.stream()
                                        .forEach(ticket -> {
                                            if (ticket != null) {
                                                String simStr = "";
                                                String fldVal = getFldValue(ticket, fldSim);
                                                if (!(fldVal == null) && !fldVal.isEmpty()) {
                                                    Double sim = calculateVectorsSim(fldVal,getTermsNum(ticket,fldSim),
                                                            valArray, lemmasMap, simMeasure);
                                                    simStr = (((sim != null) && (sim > 0.0)) ? sim.toString() : 0.0)
                                                            + "_" + String.valueOf(ticket.getNumber());
                                                } else {
                                                    simStr = "0.0_0.0" + "_" + String.valueOf(ticket.getNumber());
                                                }
                                                ticketsMap.put(simStr, ticket);
                                            }
                                        });
                                result = ticketsMap.descendingMap()
                                                   .values()
                                                   .toArray(new ticket[ticketsMap.size()]);
                            }
                            tr.commit();
                        } catch (HibernateException e) {
                            if (tr != null) tr.rollback();
                            e.printStackTrace();
                        } finally {
                            session.close();
                        }
                    }
                }
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        return result;
    }

    /**
     * Returns string without stop words
     *
     * @param   text   unprocessed string
     * @return         string without stop words
     */
    private static String removeStopWords(String text) {
        StringBuilder sb = null;
        try{
            CharArraySet stopWords = EnglishAnalyzer.getDefaultStopSet();
            AttributeFactory factory = AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY;
            StandardTokenizer tokenizer = new StandardTokenizer(factory);
            tokenizer.setReader(new StringReader(text.trim()));
            TokenStream tokenStream = new StopFilter(tokenizer, stopWords);
            sb = new StringBuilder();
            CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                String term = charTermAttribute.toString();
                sb.append(term + " ");
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        return sb.toString();
    }

    /**
     * Returns string with extended acronyms. Acronyms are replaced by
     * their interpretations.
     *
     * @param   str       string to extend acronyms
     * @param   regex     regular expression to split str into tokens
     * @return            string with extended acronyms
     */
    private String getExtendedAcronyms(String str, String regex) throws IOException {
        String  newStr     = str;
        String[] tokens    = str.split(regex);
        for (String token : tokens) {
            String interpret = acrService.getInterpretByName(token);
            if ((interpret != null)&&!interpret.isEmpty()) {
                String nonStopWords = removeStopWords(interpret);
                if ((nonStopWords != null)&&!nonStopWords.isEmpty()) {
                    newStr = newStr.replace(token, nonStopWords);
                }
            }
        }
        return newStr;
    }

    /**
     * Returns Map with lemmas of tokens from specified array
     * and maps these lemmas into Word's objects.
     *
     * @param   tokens        array of tokens
     * @param   lemmasMap     Map to write lemmas and corresponded
     *                        Word's objects
     * @return                Map with lemmas of tokens
     */
    private TreeMap<String,String> getWords(String[] tokens, TreeMap<String, Word> lemmasMap) throws IOException {
        TreeMap<String, String> tokensMap = new TreeMap();
        if (tokens.length > 0) {
            String[] POStags = tagger.tag(tokens);
            Integer ticketNum = 0;
            systvar ticknum = systvarService.getSystVarByCode("ticknum");
            if (ticknum != null) {
                ticketNum = Integer.parseInt(ticknum.getValue());
                List<String> blackList = new ArrayList<>();
                for (int i = 0; i < tokens.length; ++i) {
                    String lemma = Lemmatizer.getLemma(tokens[i], POStags[i]);
                    if ((lemma != null) && !lemma.isEmpty() && !blackList.contains(lemma)) {
                        if (lemmasMap.get(lemma) == null) {
                            List<String> syns = getSynonyms(dict, lemma, POStags[i]);
                            List<lemma> lemmas = lemmaService.getLemmasByList(getLemmasList(lemma, syns));
                            if ((lemmas != null) && !lemmas.isEmpty()) {
                                Word w = new Word(lemma,
                                                  Math.log((double) ticketNum /
                                                       lemmas.stream()
                                                             .collect(summingInt(lemmaObj -> lemmaObj.getTicknum()))),
                                                  syns);
                                lemmasMap.put(lemma, w);
                                tokensMap.put(tokens[i], lemma);
                            } else {
                                blackList.add(lemma);
                            }
                        } else {
                            tokensMap.put(tokens[i], lemma);
                        }
                    }
                }
            }
        }
        return tokensMap;
    }

    /**
     * Returns conditions string to search synonyms for specified lemma
     *
     * @param   dict       WordNet instance
     * @param   Lemma      lemma to search synonyms
     * @param   POStag     POStag for Lemma
     * @return             conditions string to search synonyms for Lemma
     */
    private List<String> getSynonyms (IDictionary dict, String Lemma, String POStag) {
        List<String> synsArray = new ArrayList<>();
        try{
            dict.open();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        if (dict != null) {
            POS POSObj = getPOSObject(POStag);
            if (POSObj != null) {
                IIndexWord idxWord = dict.getIndexWord(Lemma, POSObj);
                if (idxWord != null) {
                    synsArray = idxWord.getWordIDs()
                                       .stream()
                                       .flatMap(word -> dict.getWord(word)
                                                            .getSynset()
                                                            .getWords()
                                                            .stream())
                                       .map(word -> word.getLemma())
                                       .filter(word -> !word.equalsIgnoreCase(Lemma))
                                       .collect(toList());
                }
            }
        }
        return synsArray;
    }

    /**
     * Maps POS tag to WordNet POS object
     *
     * @param   POStag     POS tag
     * @return             WordNet POS object
     */
    private POS getPOSObject (String POStag) {
        switch(POStag){
            case "NN":
            case "NNS":
            case "NNP":
            case "NNPS": return POS.NOUN;
            case "VB":
            case "VBD":
            case "VBG":
            case "VBN":
            case "VBP":
            case "VBZ": return POS.VERB;
            case "JJ":
            case "JJR":
            case "JJS": return POS.ADJECTIVE;
            case "RB":
            case "RBR":
            case "RBS": return POS.ADVERB;
            default: return null;
        }
    }

    /**
     * Returns values' list to search lemmas into table lemmas
     *
     * @param   lemma      lemma to add to values' list
     * @param   syns       syns to add to values' list
     * @return             values' list
     */
    private List<String> getLemmasList (String lemma, List<String> syns) {
        List<String> resultList = new ArrayList<>();
        if ((lemma != null)&&(!lemma.isEmpty())) {
            resultList.add(lemma);
            if (syns != null) {
                resultList.addAll(syns);
            }
        }
        return resultList;
    }

    /**
     * Returns lemmas extracted from ticket's field with specified name.
     *
     * @param    ticket             issue object to get lemmas
     * @param    fldName            name of ticket's field
     * @return                      string of lemmas extracted from ticket's field
     */
    public String getFldValue(ticket ticket, String fldName) {
        switch(fldName) {
            case "issue": return ticket.getIssuelem();
            case "issuedescr": return ticket.getIssdesclem();
            case "solution": return ticket.getSolutionlem();
            case "solutiondet": return ticket.getSoldetlem();
            default: return ticket.getLemmas();
        }
    }

    /**
     * Returns name of field which contains lemmas extracted from field with specified name.
     *
     * @param    ticket             issue object to get lemmas
     * @param    fldName            name of ticket's field
     * @return                      name of ticket's field which
     *                              contains lemmas extracted from fldName
     */
    public String getLemmasFldName(String fldName) {
        switch(fldName) {
            case "issue": return "issuelem";
            case "issuedescr": return "issdesclem";
            case "solution": return "solutionlem";
            case "solutiondet": return "soldetlem";
            default: return "lemmas";
        }
    }

    /**
     * Returns number of terms from a ticket field with specified name.
     *
     * @param    ticket             issue object to get number of terms
     * @param    fldName            name of ticket's field
     * @return                      number of terms
     */
    public Integer getTermsNum(ticket ticket, String fldName) {
        switch(fldName) {
            case "issue": return ticket.getIssuenum();
            case "issuedescr": return ticket.getIssdescnum();
            case "solution": return ticket.getSolutnum();
            case "solutiondet": return ticket.getSoldetnum();
            default: return ticket.getLemmasnum();
        }
    }

    /**
     * Returns value of similarity between user request and tickets fields
     *
     * @param   fldStr     string from a ticket field
     * @param   termsNum   number of terms from a ticket field
     * @param   valArray   tf-idf vector calculted for user request
     * @param   lemmasMap  Map with Word's objects corresponded with
     *                     lemmas of requested words
     * @param   measure    code of semantic similarity measure
     * @return             value of semantic similarity between user request and fldStr
     */
    private double calculateVectorsSim(String fldStr, Integer termsNum, double[] valArray,
                                       TreeMap<String,Word> lemmasMap, Integer measure) {
        double result = 0.0;
        if (!(fldStr.equals("") || (fldStr == null))) {
            double[] fldArray = calculateTicketWordsFreq(Pattern.compile("\\s")
                                                                .splitAsStream(fldStr)
                                                                .filter(token -> !token.isEmpty()
                                                                        && (token.lastIndexOf("/") != -1)
                                                                        && (token.lastIndexOf("/") > 0))
                                                                .collect(Collectors.toMap(
                                                                    token -> token.substring(0,token.lastIndexOf("/")),
                                                                    token -> Double.valueOf(
                                                                          token.substring(token.lastIndexOf("/") + 1)))
                                                                        ), termsNum, lemmasMap);
            switch (measure) {
                case 1:
                    CosineSimilarity CosSimilarity = new CosineSimilarity();
                    result = CosSimilarity.sim(valArray, fldArray);
            }
        }
        return result;
    }

    /**
     * Returns value of similarity between user request and tickets fields
     *
     * @param   ticketField    list of lemmas extracted from ticket's field
     * @param   lemmasMap      Map with Word's objects corresponded with lemmas of requested words
     * @return                 value of semantic similarity between user request and ticket's field
     */
    private double[] calculateTicketWordsFreq(Map<String,Double> ticketField,
                                              Integer lemmasNum,
                                              TreeMap<String,Word> lemmasMap){
        TreeMap<String, Double> ticketMap = new TreeMap();
        lemmasMap.entrySet()
                 .stream()
                 .map(lemmaSet -> lemmaSet.getKey())
                 .forEach(lemma -> ticketMap.put(lemma, 0.0));
        lemmasMap.entrySet()
                 .stream()
                 .map(lemmaSet -> lemmaSet.getValue())
                 .forEach(lemma -> {
                                     if (ticketField.get(lemma.getLemma()) != null) {
                                         ticketMap.put(lemma.getLemma(),
                                                       ticketMap.get(lemma.getLemma())
                                                       + ticketField.get(lemma.getLemma()));
                                     } else if (lemma.getSyns() != null) {
                                         lemma.getSyns()
                                              .stream()
                                              .filter(syn -> (ticketField.get(syn) != null))
                                              .forEach(syn -> ticketMap.put(lemma.getLemma(),
                                                                            ticketMap.get(lemma.getLemma())
                                                                            + ticketField.get(syn)));
                                     }
                });
        return ticketMap.entrySet()
                        .stream()
                        .mapToDouble(lemmaSet -> lemmasMap.get(lemmaSet.getKey()).getIdf()
                                * (double)lemmaSet.getValue() / lemmasNum)
                        .toArray();
    }

    /**
     * Returns list of lemmas extracted from specified text
     *
     * @param   fieldVal     text to process
     * @return               processed text
     */
    public List<String> processField(String fieldVal){
        List<String> fieldLemmas = new ArrayList<>();
        try {
            String nonStopWords = removeStopWords(fieldVal);
            if (!nonStopWords.isEmpty()) {
                String newStr = getExtendedAcronyms(nonStopWords, "\\s");
                fieldLemmas = getLemmas(newStr, "\\s");
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        return fieldLemmas;
    }

    /**
     * Maps string of tokens into list of corresponded lemmas
     *
     * @param   str       string to extract lemmas
     * @param   regex     regular expression to split str into tokens
     * @return            list of lemmas
     */
    private List<String> getLemmas(String str, String regex) throws IOException {
        List<String> lemmas = new ArrayList<>();
        if ((str != null)&&!str.isEmpty()) {
            String[] tokens = str.split(regex);
            String[] POStags = tagger.tag(tokens);
            for (int i = 0; i < tokens.length; ++i) {
                String lemma = Lemmatizer.getLemma(tokens[i], POStags[i]);
                if ((lemma != null)&&!lemma.isEmpty()) {
                    lemmas.add(lemma);
                }
            }
        }
        return lemmas;
    }

    /**
     * Processes fields for all tickets and fills in table lemmas
     */
    public void processAllTickets() {
        List<Word> words = new ArrayList<>();
        TreeMap<String, Integer> lemmasMap = new TreeMap();
        List<ticket> tickets = this.getAll();
        if ((tickets != null)&&!tickets.isEmpty()) {
            tickets.stream()
                    .forEach(ticket -> {
                        processTicketFields(ticket)
                            .stream()
                            .distinct()
                            .filter(lemma -> !lemma.isEmpty())
                            .forEach(lemma -> lemmasMap.put(lemma,
                                    ((lemmasMap.get(lemma) != null) ? lemmasMap.get(lemma) + 1 : 1)));
                        this.updateEntity(ticket);
                    });
            lemmaService.delAllLemmas();
            lemmasMap.entrySet()
                    .stream()
                    .forEach(lemmaSet -> lemmaService.addEntity(new lemma(lemmaSet.getKey(),
                            lemmaSet.getValue())));
            systvarService.saveOrUpdateEntity(new systvar("ticknum",
                    "Number of tickets", String.valueOf(tickets.size())));
            systvarService.saveOrUpdateEntity(new systvar("tickproc",
                    "Are tickets processed?", "YES"));
        }
    }

    /**
     * Returns string of lemmas extracted from ticket's fields: issue, issuedescr,
     * solution, solutiondet
     *
     * @param   ticket       ticket object to process it's fields
     * @return               string of lemmas
     */
    public List<String> processTicketFields (ticket ticket) {
        List<String> lemmas = new ArrayList <>();
        if (ticket.getNumber() == null) {
            ticket ticketWithMax = this.getAll()
                                       .stream()
                                       .max(Comparator.comparing(ticketObj -> ticketObj.getNumber()))
                                       .get();
            if (ticketWithMax != null) {
                Integer ticketNumber = 1 + ticketWithMax.getNumber();
                ticket.setNumber(ticketNumber);
            }
        }
        if ((ticket.getIssue() != null)&&!ticket.getIssue().isEmpty()) {
            List<String> fieldLemmas = processField(ticket.getIssue());
            if ((fieldLemmas != null)&&!fieldLemmas.isEmpty()) {
                lemmas.addAll(fieldLemmas);
                ticket.setIssuelem(getLemmasWithOcur(fieldLemmas));
                ticket.setIssuenum(fieldLemmas.size());
            }
        }
        if ((ticket.getIssuedescr() != null)&&!ticket.getIssuedescr().isEmpty()) {
            List<String> fieldLemmas = processField(ticket.getIssuedescr());
            if ((fieldLemmas != null)&&!fieldLemmas.isEmpty()) {
                lemmas.addAll(fieldLemmas);
                ticket.setIssdesclem(getLemmasWithOcur(fieldLemmas));
                ticket.setIssdescnum(fieldLemmas.size());
            }
        }
        if ((ticket.getSolution() != null)&&!ticket.getSolution().isEmpty()) {
            List<String> fieldLemmas = processField(ticket.getSolution());
            if ((fieldLemmas != null)&&!fieldLemmas.isEmpty()) {
                lemmas.addAll(fieldLemmas);
                ticket.setSolutionlem(getLemmasWithOcur(fieldLemmas));
                ticket.setSolutnum(fieldLemmas.size());
            }
        }
        if ((ticket.getSolutiondet() != null)&&!ticket.getSolutiondet().isEmpty()) {
            List<String> fieldLemmas = processField(ticket.getSolutiondet());
            if ((fieldLemmas != null)&&!fieldLemmas.isEmpty()) {
                lemmas.addAll(fieldLemmas);
                ticket.setSoldetlem(getLemmasWithOcur(fieldLemmas));
                ticket.setSoldetnum(fieldLemmas.size());
            }
        }
        if ((lemmas != null)&&!lemmas.isEmpty()) {
            if (ticket.getNumber() != null) {
                lemmas.add(String.valueOf(ticket.getNumber()));
            }
            String str = getLemmasWithOcur(lemmas);
            if ((str != null)&&!str.isEmpty()) {
                ticket.setLemmas(str);
                ticket.setLemmasnum(lemmas.size());
            }
        }
        return lemmas;
    }

    /**
     * Returns string with lemmas and their occurencies
     *
     * @param   lemmas     list of lemmas
     * @return             string with lemmas and their occurencies
     */
    public String getLemmasWithOcur(List<String> lemmas){
        String str = "";
        if ((lemmas != null)&&!lemmas.isEmpty()) {;
            str = lemmas.stream()
                        .collect(groupingBy(lemma -> lemma,
                                TreeMap::new,
                                counting()))
                        .entrySet()
                        .stream()
                        .map(lemmaSet -> lemmaSet.getKey() + "/" + (double)lemmaSet.getValue())
                        .collect(Collectors.joining(" "));
        }
        return str;
    }

    /**
     * Processes ticket's fields and fills in table lemmas with
     * lemmas extracted from these fields
     *
     * @param   ticket  ticket object to process it's fields
     */
    public void processTicket (ticket ticket) {
        TreeMap<String, Integer> lemmasMap = new TreeMap<>();
        processTicketFields(ticket).stream()
                                   .distinct()
                                   .filter(lemma -> !lemma.isEmpty())
                                   .forEach(lemma -> lemmasMap.put(lemma,
                                                   ((lemmasMap.get(lemma) != null)?lemmasMap.get(lemma) + 1:1)));
        lemmasMap.entrySet()
                 .stream()
                 .forEach(lemmaSet -> { lemma lemma = lemmaService.getLemmaObjByLemma(lemmaSet.getKey());
                                 if (lemma == null) {
                                     lemmaService.addEntity(new lemma(lemmaSet.getKey(),lemmaSet.getValue()));
                                 } else {
                                     lemmaService.updateEntity(new lemma(lemmaSet.getKey(),
                                                                         lemmaSet.getValue() + lemma.getTicknum()));
                                 }
                         });
    }

}
