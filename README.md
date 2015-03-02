Demonstration of Play - New Relic problem
=========================================

To run this app with New Relic agent use the `-J-javaagent` flag, so:

````
activator "~run" -J-javaagent:/root/play-newrelic/lib/newrelic.jar
````

Then go to [http://localhost:9000/](http://localhost:9000/). Compare results with and without New Relic.

Credits:
Original zip streaming recipe from [http://blog.greweb.fr/?p=1993](http://blog.greweb.fr/?p=1993)