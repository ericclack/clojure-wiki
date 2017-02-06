(ns clojure-wiki.filters
  (:require [markdown.core :refer [md-to-html-string]]
            [clojure.string :as s]))

(defn wiki-links
  ;; Convert links in form [[About Cars]] to an HTML link
  [text state]
  [(s/replace text #"\[\[([\w -]+)\]\]"
              (fn [i]
                (let [link-text (i 1)
                      link-ref (.toLowerCase (s/replace link-text " " "-"))]
                  (str "<a href=\"" link-ref "\">" link-text "</a>"))))
   state])

(defn my-markdown
  [content]
  (md-to-html-string content :custom-transformers [wiki-links]))
