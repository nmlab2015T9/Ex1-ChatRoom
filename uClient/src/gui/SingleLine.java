/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import java.util.*;

/**
 *
 * @author Eddie
 */
public class SingleLine {
    private String oriText;
    private String oriTexture;
    private Vector<String> smileys;

    //operated
    public Vector<String> texts;
    public Vector<String> textures;


    public SingleLine ( String text, String texture , Vector<String> smileys) {
        oriText = text;
        oriTexture = texture;
        this.smileys = smileys;

        texts = new Vector<String>();
        textures = new Vector<String>();
        
        generate();
    }

    private void generate() {
        Vector<Integer> indexs  = new Vector<Integer>();

        for (String regex : smileys) {
            int index = 0;
            while (index >= 0) {
                index = oriText.indexOf(regex, index);
                if (index>=0) {
                    indexs.add(index);
                    index++;
                }
            }
        }

        if ( indexs.size()!=0 ) {  //some smileys exist
            int[] indexs_array = new int[indexs.size()];
            for (int k=0; k<indexs.size(); k++) {
                indexs_array[k] = indexs.elementAt(k);
            }

            Arrays.sort(indexs_array);

            Vector<String> splited = new Vector<String>();
            splited.add(oriText.substring(0, indexs_array[0]));
            for (int j=0; j<indexs_array.length; j++) {
                splited.add(oriText.substring(indexs_array[j], indexs_array[j]+9));
                if (j==indexs_array.length-1)
                    splited.add(oriText.substring(indexs_array[j]+9));
                else
                    splited.add(oriText.substring(indexs_array[j]+9, indexs_array[j+1]));
            }
            for (int i=0; i<splited.size(); i++) {
                String s = splited.elementAt(i);
                String t = oriTexture;
                for (String smi: smileys) {
                    if (s.equals(smi)) {
                        t = smi;
                    }
                    else if (s.equals("")) {
                        s = new String(" ");
                    }
                }
                texts.add(i, s);
                textures.add(i, t);
            }            
        }
        else {
            texts.add(oriText);
            textures.add(oriTexture);
        }

        
    }
    

}
