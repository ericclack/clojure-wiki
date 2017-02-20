(ns clojure-wiki.models.setup
  (:require [com.ashafa.clutch :as couch]
            [clojure-wiki.models.db :as db]))


(defn create-page-views []
  (db/with-db
    (couch/save-view "pages" (couch/view-server-fns
                               :javascript
                               {:by_tag {:map
"function(doc) {
  if ('tags' in doc) {
    doc.tags.forEach( function(tag) {
      emit(tag, doc._id );
    });
  }
}"
                                         }
                                :by_word {:map
"function(doc) {
  const stopwords = ['and', 'the'];
  var freq = {};
  ((doc._id + \" \" + doc.tags + \" \" + doc.content).toLowerCase().match(/\\w+/g)).forEach(function(word) {
    if (word.length >= 3 && stopwords.indexOf(word) == -1) {
      freq[word] = (freq[word] || 0) +1;
    }
  });
  Object.keys(freq).forEach(function(word) {
    emit(word, {count: freq[word]});
  });
}"
                                          }}))))

(defn create-page-graph-views []
  (db/with-db
    (couch/save-view "page_graph" (couch/view-server-fns
                                    :javascript
                                    {:who_links_to {:map
"
function(doc) {
  regex = /\\[\\[([\\w -]+)\\]\\]/g;
  while( match = regex.exec(doc.content) ) {
    emit(match[1].toLowerCase().replace(\" \",\"-\"), doc._id);
  }
}
"
                                                    }}))))

                       
(defn setup-db []
  (create-page-views)
  (create-page-graph-views))

