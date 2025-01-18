package fr.ensta.bigdata;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class PageCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    /* START STUDENT CODE */
    /* END STUDENT CODE */
}
