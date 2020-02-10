# POC of getting metrics out of play cache API backed with Ehcache

To start the app:
```bash
$ sbt run
```

Then navigate to http://localhost:9000/fibonacci/<n> (where n is small, like 10)
to get an initial slow response followed by fast cached ones.

You can check metrics in http://localhost:9000/admin/metrics
