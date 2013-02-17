//cc MaxTemperature Application to find the maximum temperature in the weather dataset
// vv MaxTemperature
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

// (nprabhak@unmac ~/Projects/Java/Jafun/max-temperature-new)$ hadoop jar dist/max-temperature.jar MaxTemperature /Users/nprabhak/hadoop-book/input/ncdc/sample.txt /Users/nprabhak/hadoop-book/output/max-temperature
// (nprabhak@unmac ~)$ hadoop dfs -cat /Users/nprabhak/hadoop-book/output/max-temperature/part-r-00000

// nipra@lambda:max-temperature-new$ hadoop jar dist/max-temperature.jar MaxTemperature /user/nipra/hadoop-book/input/ncdc/sample.txt /user/nipra/hadoop-book/output/max-temperature


public class MaxTemperature {

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: MaxTemperature <input path> <output path>");
			System.exit(-1);
		}

		Job job = new Job();
		job.setJarByClass(MaxTemperature.class);
		job.setJobName("Max temperature");

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.setMapperClass(MaxTemperatureMapper.class);
		job.setReducerClass(MaxTemperatureReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
// ^^ MaxTemperature
