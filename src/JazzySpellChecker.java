
 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.engine.Word;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;
import com.swabunga.spell.event.TeXWordFinder;
 
public class JazzySpellChecker implements SpellCheckListener {
  
 private SpellChecker spellChecker;
 private List<String> misspelledWords;
 private int typoCount;
 private int charCount;
 /**
  * get a list of misspelled words from the text
  * @param text
  */
 public List<String> getMisspelledWords(String text) {
  StringWordTokenizer texTok = new StringWordTokenizer(text);
  spellChecker.checkSpelling(texTok);
  return misspelledWords;
 }
 
 /**
  * get a list of misspelled words from the text
  * @param text
  */
 public int getMisspelledWordsCount(String text) {
  StringWordTokenizer texTok = new StringWordTokenizer(text);
 spellChecker.checkSpelling(texTok);
  typoCount = misspelledWords.size();
  return typoCount;
 }
  
 private static SpellDictionaryHashMap dictionaryHashMap;
  
 static{
  
  File dict = new File("dictionary/dictionary.txt");
  try {
   dictionaryHashMap = new SpellDictionaryHashMap(dict);
  } catch (FileNotFoundException e) {
   e.printStackTrace();
  } catch (IOException e) {
   e.printStackTrace();
  }
 }
  
 private void initialize(){
   spellChecker = new SpellChecker(dictionaryHashMap);
   spellChecker.addSpellCheckListener(this);  
 }
  
  
 public JazzySpellChecker() {
   
  misspelledWords = new ArrayList<String>();
  initialize();
 }
 
 /**
  * correct the misspelled words in the input string and return the result
  */
 public String getCorrectedLine(String line){
  List<String> misSpelledWords = getMisspelledWords(line);
   typoCount = misSpelledWords.size();
  for (String misSpelledWord : misSpelledWords){
   List<String> suggestions = getSuggestions(misSpelledWord);
   if (suggestions.size() == 0)
    continue;
   String bestSuggestion = suggestions.get(0);
   line = line.replace(misSpelledWord, bestSuggestion);
  }
  return line;
 }
  
 public String getCorrectedText(String line){
  StringBuilder builder = new StringBuilder();
  String[] tempWords = line.split(" ");
  for (String tempWord : tempWords){
  // if (!spellChecker.isCorrect(tempWord)){
    List<Word> suggestions = spellChecker.getSuggestions(tempWord, 0);
    if (suggestions.size() > 0){
     builder.append(spellChecker.getSuggestions(tempWord, 0).get(0).toString());
    //}
  //  else
   //  builder.append(tempWord);
   }
   else {
    builder.append(tempWord);
   }
   builder.append(" ");
  }
  return builder.toString().trim();
 }
  
  
 public List<String> getSuggestions(String misspelledWord){
   
  @SuppressWarnings("unchecked")
  List<Word> su99esti0ns = spellChecker.getSuggestions(misspelledWord, 0);
  List<String> suggestions = new ArrayList<String>();
  for (Word suggestion : su99esti0ns){
   suggestions.add(suggestion.getWord());
  }
   
  return suggestions;
 }
 
  
 @Override
 public void spellingError(SpellCheckEvent event) {
  event.ignoreWord(true);
  misspelledWords.add(event.getInvalidWord());
 }
 
 public static void main(String[] args) {
  JazzySpellChecker jazzySpellChecker = new JazzySpellChecker();
  
  //here add all the Facebook posts to the list
  List<String> facebookPosts = new ArrayList<String>();
  facebookPosts.add("PLEASE LIKE THIS!!! Comment and SHARE!!If you want Mark Zuckerburg to dlete Donald Trump off FB!!GO! LIKE!");
  facebookPosts.add("For those who aren’t into complex video production, Vine lets you create simple, six-second looping videos to share on Twitter and Facebook.");
  
  int i=0;
  float average=0;
  
  //going trough all the facebook posts and analysing it
  for(String facebookPost : facebookPosts){
	  jazzySpellChecker = new JazzySpellChecker();
	  int typoCount = jazzySpellChecker.getMisspelledWordsCount(facebookPost);
	  int charCount = facebookPost.length();
	  float charTypoRatio = (float)typoCount/(float)charCount;
	  float metric = charTypoRatio*1000;
	  average =+ metric;
	  System.out.println("Post: "+i);
	  System.out.println("typoCount="+typoCount);
	  System.out.println("charCount="+charCount);
	  System.out.println("ratio="+charTypoRatio);
	  System.out.println("metric="+metric);
	 
	  i++;
  }
  average = average / i;
  
  //average is the value for visualization
  System.out.println("overall metric average="+average);

 }
}