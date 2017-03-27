(ns ^{:doc "Set up CouchDB for the wiki engine. Run these functions 
            once after install with `lein setup-db`"}
    clojure-wiki.models.setup
  (:require [com.ashafa.clutch :as couch]
            [clojure-wiki.models.db :as db]))

(defn create-page-views 
  "Create CouchDB views pages/by_tag and pages/by_word.
  by_tag enables tag navigation, by_word is a simple search index."
  []
  (db/with-db
    (couch/save-view "pages" (couch/view-server-fns
                              :javascript
{:by_tag
 {:map
  "
function(doc) {
  if ('tags' in doc) {
    doc.tags.forEach( function(tag) {
      emit(tag, doc._id );
    });
  }
}"}
 
 :by_word
 {:map
  "
function(doc) {
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
}"}

 :by_timestamp
 {:map
   "
function(doc) {
  if (doc.timestamp) {
    emit(doc.timestamp, doc.id);
  }
}"}
  }))))

(defn create-page-graph-views
  "Create CouchDB views page_graph/who_links_to.
  Which pages link to which page via links of form [[link]]."
  []
  (db/with-db
    (couch/save-view "page_graph" (couch/view-server-fns
                                    :javascript
{:who_links_to
 {:map
  "
function(doc) {
  regex = /\\[\\[([\\w -]+)\\]\\]/g;
  while( match = regex.exec(doc.content) ) {
    emit(match[1].toLowerCase().replace(/ /g,\"-\"), doc._id);
  }
}
"
  }}))))

                       
(defn setup-db
  "Create all the views we need."
  []
  (create-page-views)
  (create-page-graph-views))

