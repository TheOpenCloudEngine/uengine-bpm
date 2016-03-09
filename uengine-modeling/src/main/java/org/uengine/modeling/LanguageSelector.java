package org.uengine.modeling;

import org.metaworks.annotation.Range;

/**
 * Created by jjy on 2015. 12. 29..
 */
public class LanguageSelector {

    String language;
    @Range(options = {"English", "Korean", "Deutsch", "Portuguese", "Spanish", "Chinese", "Japanese"}, values = {"en", "ko", "de", "pr", "sp", "ch", "jp"})
        public String getLanguage() {
            return language;
        }
        public void setLanguage(String language) {
            this.language = language;
        }


}
