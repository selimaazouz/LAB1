# Lab 1: HDFS & MapReduce

## "Pageview complete dumps"

Download compressed files (bzip2) from Wikimedia: <https://dumps.wikimedia.org/other/pageview_complete/readme.html>

Please download in directory `tmp/data`.

Example to download data from Jan 1st 2024.

```sh
curl -o tmp/data/pageviews-20240101-user.bz2 https://dumps.wikimedia.org/other/pageview_complete/2024/2024-01/pagevi
ews-20240101-user.bz2
```

This file can be uncompressed using `unbzip2` from the `bzip2` package of your package manager.

```sh
bunzip2 -c tmp/data/pageviews-20240101-user.bz2 | head
```

> Question 1: How many times was the page `https://aa.wikibooks.org/wiki/Main_Page` viewed on Jan 1st 2024? At what time was it viewed most?

The above question can be answered without MapReduce, however we can quickly find harder questions.
Think about another page of a Wiki project and try to find its pageview count on a given day using `bunzip2` and `grep`. Latency will be horribly large.

# HDFS

Ingest this data into HDSF.

```sh
"$HADOOP_HOME/bin/hdfs" dfs -put tmp/data/pageviews-20240101-user.bz2
```

Note: Hadoop handles `bz2` files directly.

You can verify that the file is present in your home directory in HDFS.

```sh
"$HADOOP_HOME/bin/hdfs" dfs -ls
```

You should then be able to read from HDFS like you did from disk.

```sh
"$HADOOP_HOME/bin/hdfs" dfs -cat pageviews-20240101-user.bz2 | bunzip2 -c | head
```

# MapReduce: Your First Jobs

## Exercise 1

In this exercise, we will create a MapReduce Job to compute the sum of counts of page views grouped by Wiki Code.

### Liminary: Parse records

We will need to parse each line of text into variables and types we can work with.

Make the test `parseRecordOk` in `src/test/java/fr/ensta/bigdata/PageCountMapperTest.java` succeed.

### Implement Mapper

We will need to parse each value received to write the necessary `(key, value)` pairs.

Make the test `testMapOk` in `src/test/java/fr/ensta/bigdata/PageCountMapperTest.java` succeed.

### Implement Reducer

Once `(key, value)` pairs have been computed, we need to aggregate values by key.

Make the test `testReduceOk` in `src/test/java/fr/ensta/bigdata/PageCountReducerTest.java` succeed.

### Run on the data

Implement a driver for the MapReduce Job in `fr.ensta.bigdata.PageCount`.

Build a JAR of your code.

```sh
mvn package
```

It should produce a JAR file `target/lab1-1.0-SNAPSHOT.jar`.

Check that you can use this JAR with Hadoop.

```sh
"$HADOOP_HOME/bin/hadoop" jar target/lab1-1.0-SNAPSHOT.jar fr.ensta.bigdata.PageCount
```

Then use this JAR to run the MapReduce Job on the input file.

> Question 2: How many page views were there on `wikipedia.fr` on Jan 1st 2024?

## Exercise 2

In this exercise, you will create a MapReduce Job to compute the hourly average count of page views of January 2024.

> Question 3: What were the 10 most viewed pages on average from 12:00 to 13:00 on Wikipedia in January 2024? How many page views did they have on average at this time?

Create new classes to reproduce the steps taken before in the same Java project.

1. Write tests for Mapper and implement Mapper.
2. Write tests for Reducer and implement Reducer.
3. Write a MapReduce driver for your Job and run it on Hadoop.

## Appendix

### A. Get `Path` of input split in `Mapper.map`

When the `Mapper` is working on data from files, some data (e.g. date, id, ...) may be in the file path.

You may use the helper function below to get the `Path` of the file being processed by the `Mapper`.

```java
    // all imports are in `org.apache.hadoop`
    public static Path getInputFilePath(Context context) {
        var inputSplit = context.getInputSplit();
        if (!(inputSplit instanceof FileSplit)) {
            throw new IllegalArgumentException("inputSplit is not a FileSplit");
        }
        var fileSplit = (FileSplit) inputSplit;
        var path = fileSplit.getPath();
        return path;
    }
```
