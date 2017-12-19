# clojure-wiki

A simple Clojure wiki engine with a CouchDB backend, intended as a personal wiki.
Content is written in Markdown and the engine supports tag navigation and simple word search.

## Prerequisites

Java 1.8+

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

CouchDB, tested with version 1.6.1. On a Mac you can install with `brew install couchdb`, on Debian/Ubuntu/etc `sudo apt-get install couchdb`

## Set up

Start up couchdb:

    couchdb

Then from the Futon admin system - http://127.0.0.1:5984/_utils/ - create two databases, one for dev, one for testing, and enter their URLs in `profiles.clj`. Here's an example file:

```
;; The profiles.clj file is used for local environment variables, such as database credentials.
;; This file is listed in .gitignore and will be excluded from version control by Git.

{:profiles/dev  {:env {:database-url "wiki"}}
 :profiles/test {:env {:database-url "wiki-test"}}}
```

Now create the database views:

    lein setup-db

## Running

If you've not already done so, start CouchDB:

    couchdb

To start a web server for the application, run:

    lein run
    
Now browse app: http://127.0.0.1:3000/

## Hints

Add links between pages [[Like This]]. 

## License

GNU General Public License v3.0
Copyright © 2017 Eric Clack
