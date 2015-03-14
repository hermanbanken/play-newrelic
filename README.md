Demonstration of Play - New Relic problem
=========================================

To run this app with New Relic agent use the `-J-javaagent` flag, so:

````
activator "~run" -J-javaagent:/root/play-newrelic/lib/newrelic.jar
````

Then go to [http://localhost:9000/](http://localhost:9000/). Compare results with and without New Relic.

To limit the amount of memory used and thus the size of the dump file, use the Xmx flag:

````
activator start -XX:+HeapDumpOnOutOfMemoryError -Xmx64m -J-javaagent:/root/play-newrelic/lib/newrelic.jar
````

`-XX:+HeapDumpOnOutOfMemoryError` makes sure the Heap file will be created automatically.

Credits:
Original zip streaming recipe from [http://blog.greweb.fr/?p=1993](http://blog.greweb.fr/?p=1993)
