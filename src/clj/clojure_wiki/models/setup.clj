(ns clojure-wiki.models.db
  (:require [com.ashafa.clutch :as couch]
            [clojure-wiki.models.db :as db]))


(defn create-map-of-tags-to-docs []
  (with-db
    (couch/save-view "pages3" (couch/view-server-fns
                               :javascript
                               {:by_tag {:map
"function(doc) {
  if ('tags' in doc) {
    doc.tags.forEach( function(tag) {
      emit(tag, doc._id );
    });
  }
}"
                                         }}))))

(defn create-map-of-words-to-docs []
  (with-db
    (couch/save-view "pages3" (couch/view-server-fns
                               :javascript
                               {:by_word {:map
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


(defn setup-db []
  (create-map-of-tags-to-docs)
  (create-map-of-words-to-docs))

