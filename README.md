# clojure-wiki

generated using Luminus version "2.9.11.24"

Example code:

    (require ['com.ashafa.clutch :as 'clutch])
    (require ['clojure.data.json :as 'json])

    (clutch/get-database "wiki")
    
    (clutch/with-db "wiki"
        (clutch/get-view "pages" "by_tag"))

Or calling our app code

## To Do

Better connect emacs to Lein REPL
This sort of works: M-x cider-connect localhost 7000

Read more here:
http://www.luminusweb.net/docs/repl.md

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein run
    
Then start CouchDB run:

    couchdb

Now browse app: http://127.0.0.1:3000/

And CouchDB Futon admin: http://127.0.0.1:5984/_utils/


## License

Copyright © 2017 FIXME
