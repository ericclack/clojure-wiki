# clojure-wiki

generated using Luminus version "2.9.11.24"

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Set up

Create two databases from the Futon admin system, one for dev, one for testing, and enter their URLs in `profiles.clj`

Create the database views:

   lein setup-db

## Running

Start CouchDB run:

    couchdb

To start a web server for the application, run:

    lein run
    
Now browse app: http://127.0.0.1:3000/

You can use the CouchDB Futon admin: http://127.0.0.1:5984/_utils/

## License

Copyright © 2017 Eric Clack
