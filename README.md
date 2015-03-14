Demonstration of Play - New Relic problem
=========================================

To run this app with New Relic agent use the `-J-javaagent` flag, so:

````
activator "~run" -J-javaagent:/root/play-newrelic/lib/newrelic.jar
````

Then go to [http://localhost:9000/](http://localhost:9000/). Compare results with and without New Relic.

### Limit memory

To limit the amount of memory used and thus the size of the dump file, we use the Xmx flag. First we need to compile, as activator does not respect the memory limits.

````
activator dist
cd target/universal
unzip play-newrelic-1.0-SNAPSHOT.zip
cd play-newrelic-1.0-SNAPSHOT
cp ../../../lib/newrelic.yml lib
./bin/play-newrelic -J-XX:+HeapDumpOnOutOfMemoryError -J-Xmx64m -J-Xms64m -J-javaagent:/root/play-newrelic/target/universal/play-newrelic-1.0-SNAPSHOT/lib/newrelic.jar
````

`-XX:+HeapDumpOnOutOfMemoryError` makes sure the Heap file will be created automatically.

Credits:
Original zip streaming recipe from [http://blog.greweb.fr/?p=1993](http://blog.greweb.fr/?p=1993)
