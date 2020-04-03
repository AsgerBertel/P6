package TopicModelling;

import java.util.regex.Pattern;

public class SymbolAndWordRemovePatterns {
    private static String[] stopWords = {"pm","understand","ppl","idc","waited","wait","waiting","release","released","released","weird","copy","change","learn","breaking","purpose","searching","making","funny","fun","opening","days","changed","click","plays","reveal","revealed","forget","playing","sweet","honestly","listen","loving","incredible","today","tonight","thinking","cares","absolute","absolutely","amazing","hardest","total","repeat","growing","blessing","quick","white","black","ended","stay","called","times","matter","alive","opinion","hours","level","checked","automatically","age","check","sending","leave","cute","drop","pick","minutes","seconds","cm","idk","wanted","kinda","pic","hahaha","tho","chance","win","ago","crying","forever","months","talking","hurt","smh","leave","type","ya","imagine","poor","finally","apply","luck","latest","late","np","back","front","feat","ft","remember","hey","hear","jk","soft","member","way","ways","calling","success","pictures","pics","point","sad","wrong","fake","bc","fast","simple","problems","problem","knowing","barely","dumb","literally","start","friends","friend","true","thought","doesnt","lmfao","find","gorl","things","live","lives","lot","wanna","yo","wow","guys","pls","ty","huh","one","two","three","four","five","six","seven","eight","nine","ten","beautiful","wait","join","hate","lose","low","ive","long","hard","top","big","hit","haha","great","person","coming","life","everyday","takes","gonna","years","fucking","damn","bitch","fuck","shit","sa","loves","good","bad","feel","happy","love","month","talk","choose","work","people","thing","will","well","hopefully","currently","wonder","sure","exactly","meanwhile","definitely","consider","hello","appreciate","better","help","cant","best","keep","going","imo","wtf","ur", "lol", "omg", "rt","ig","pm", "dont", "lil", "lmao", "nigga", "niggas","day","second","today", "time","year","tomorrow","yesterday","a", "won't","able", "about", "above", "abst", "accordance", "according", "accordingly", "across", "act", "actually", "added", "adj", "affected", "affecting", "affects", "after", "afterwards", "again", "against", "ah", "all", "almost", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "announce", "another", "any", "anybody", "anyhow", "anymore", "anyone", "anything", "anyway", "anyways", "anywhere", "apparently", "approximately", "are", "aren", "arent", "arise", "around", "as", "aside", "ask", "asking", "at", "auth", "available", "away", "awfully", "b", "back", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "begin", "beginning", "beginnings", "begins", "behind", "being", "believe", "below", "beside", "besides", "between", "beyond", "biol", "both", "brief", "briefly", "but", "by", "c", "ca", "came", "can", "cannot", "can't", "cause", "causes", "certain", "certainly", "co", "com", "come", "comes", "contain", "containing", "contains", "could", "couldnt", "d", "date", "did", "didn't", "different", "do", "does", "doesn't", "doing", "done", "don't", "down", "downwards", "due", "during", "e", "each", "ed", "edu", "effect", "eg", "eight", "eighty", "either", "else", "elsewhere", "end", "ending", "enough", "especially", "et", "et-al", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "except", "f", "far", "few", "ff", "fifth", "first", "five", "fix", "followed", "following", "follows", "for", "former", "formerly", "forth", "found", "four", "from", "further", "furthermore", "g", "gave", "get", "gets", "getting", "give", "given", "gives", "giving", "go", "goes", "gone", "got", "gotten", "h", "had", "happens", "hardly", "has", "hasn't", "have", "haven't", "having", "he", "hed", "hence", "her", "here", "hereafter", "hereby", "herein", "heres", "hereupon", "hers", "herself", "hes", "hi", "hid", "him", "himself", "his", "hither", "home", "how", "howbeit", "however", "hundred", "i", "id", "ie", "if", "i'll", "im", "immediate", "immediately", "importance", "important", "in", "inc", "indeed", "index", "information", "instead", "into", "invention", "inward", "is", "isn't", "it", "itd", "it'll", "its", "itself", "i've", "j", "just", "k", "keep	keeps", "kept", "kg", "km", "know", "known", "knows", "l", "largely", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "line", "little", "'ll", "look", "looking", "looks", "ltd", "m", "made", "mainly", "make", "makes", "many", "may", "maybe", "me", "mean", "means", "meantime", "meanwhile", "merely", "mg", "might", "million", "miss", "ml", "more", "moreover", "most", "mostly", "mr", "mrs", "much", "mug", "must", "my", "myself", "n", "na", "name", "namely", "nay", "nd", "near", "nearly", "necessarily", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "ninety", "no", "nobody", "non", "none", "nonetheless", "noone", "nor", "normally", "nos", "not", "noted", "nothing", "now", "nowhere", "o", "obtain", "obtained", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "omitted", "on", "once", "one", "ones", "only", "onto", "or", "ord", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "owing", "own", "p", "page", "pages", "part", "particular", "particularly", "past", "per", "perhaps", "placed", "please", "plus", "poorly", "possible", "possibly", "potentially", "pp", "predominantly", "present", "previously", "primarily", "probably", "promptly", "proud", "provides", "put", "q", "que", "quickly", "quite", "qv", "r", "ran", "rather", "rd", "re", "readily", "really", "recent", "recently", "ref", "refs", "regarding", "regardless", "regards", "related", "relatively", "research", "respectively", "resulted", "resulting", "results", "right", "run", "s", "said", "same", "saw", "say", "saying", "says", "sec", "section", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sent", "seven", "several", "shall", "she", "shed", "she'll", "shes", "should", "shouldn't", "show", "showed", "shown", "showns", "shows", "significant", "significantly", "similar", "similarly", "since", "six", "slightly", "so", "some", "somebody", "somehow", "someone", "somethan", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specifically", "specified", "specify", "specifying", "still", "stop", "strongly", "sub", "substantially", "successfully", "such", "sufficiently", "suggest", "sup", "sure	t", "take", "taken", "taking", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "that'll", "thats", "that've", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "thered", "therefore", "therein", "there'll", "thereof", "therere", "theres", "thereto", "thereupon", "there've", "these", "they", "theyd", "they'll", "theyre", "they've", "think", "this", "those", "thou", "though", "thoughh", "thousand", "throug", "through", "throughout", "thru", "thus", "til", "tip", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "ts", "twice", "two", "u", "un", "under", "unfortunately", "unless", "unlike", "unlikely", "until", "unto", "up", "upon", "ups", "us", "use", "used", "useful", "usefully", "usefulness", "uses", "using", "usually", "v", "value", "various", "'ve", "very", "via", "viz", "vol", "vols", "vs", "w", "want", "wants", "was", "wasnt", "way", "we", "wed", "welcome", "we'll", "went", "were", "werent", "we've", "what", "whatever", "what'll", "whats", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "wheres", "whereupon", "wherever", "whether", "which", "while", "whim", "whither", "who", "whod", "whoever", "whole", "who'll", "whom", "whomever", "whos", "whose", "why", "widely", "willing", "wish", "with", "within", "without", "wont", "words", "world", "would", "wouldnt", "www", "x", "y", "yes", "yet", "you", "youd", "you'll", "your", "youre", "yours", "yourself", "yourselves", "you've", "z", "zero"};
    private static String stopWordsPatternString = String.join("|",stopWords);
    public static Pattern stopWordPattern = Pattern.compile("\\b(?:" + stopWordsPatternString + ")\\b\\s*",Pattern.CASE_INSENSITIVE);
    private static String[] apostropheWords= {"I'm", "I'll", "I've", "I'd", "you're", "you'll", "you'd", "you've", "he's", "he'll", "he'd", "he's", "she's", "she'll", "she'd", "it's", "it'll", "it'd", "we're", "we'll", "we'd", "we've", "they're", "they'll", "they'd", "they've", "that's", "that'll", "that'd", "that's", "who's", "who'll", "who'd", "what's", "what're", "what'll", "what'd", "where's", "where'll", "where'd", "when's", "when'll", "when'd", "when's", "why's", "why'll", "why'd", "why's", "how's", "how'll", "how'd", "how's", "isn't", "aren't", "wasn't", "weren't", "haven't", "hasn't", "hadn't", "won't", "wouldn't", "don't", "doesn't", "didn't", "can't", "couldn't", "shouldn't", "mightn't", "mustn't", "would've", "should've", "could've", "might've", "must've","I’m", "I’ll", "I’ve", "I’d", "you’re", "you’ll", "you’d", "you’ve", "he’s", "he’ll", "he’d", "he’s", "she’s", "she’ll", "she’d", "it’s", "it’ll", "it’d", "we’re", "we’ll", "we’d", "we’ve", "they’re", "they’ll", "they’d", "they’ve", "that’s", "that’ll", "that’d", "that’s", "who’s", "who’ll", "who’d", "what’s", "what’re", "what’ll", "what’d", "where’s", "where’ll", "where’d", "when’s", "when’ll", "when’d", "when’s", "why’s", "why’ll", "why’d", "why’s", "how’s", "how’ll", "how’d", "how’s", "isn’t", "aren’t", "wasn’t", "weren’t", "haven’t", "hasn’t", "hadn’t", "won’t", "wouldn’t", "don’t", "doesn’t", "didn’t", "can’t", "couldn’t", "shouldn’t", "mightn’t", "mustn’t", "would’ve", "should’ve", "could’ve", "might’ve", "must’ve","y'all","y’all", "ain't", "ain’t",};
    private static String apostropheWordsPatternString = String.join("|",apostropheWords);
    public static Pattern apostropheWordPattern = Pattern.compile("\\b(?:" + apostropheWordsPatternString + ")\\b\\s*",Pattern.CASE_INSENSITIVE);
}
