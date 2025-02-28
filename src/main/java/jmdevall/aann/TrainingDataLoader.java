package jmdevall.aann;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.util.HadoopInputFile;

import com.exasol.parquetio.data.Row;
import com.exasol.parquetio.reader.RowParquetReader;

public class TrainingDataLoader {

	private String fullParquetFilePath;
			

	public TrainingDataLoader(String fullParquetFilePath) {
		super();
		this.fullParquetFilePath = fullParquetFilePath;
	}

	public List<TrainingData> readParquet() {
		int rowsreaded=0;
		final Path path = new Path(fullParquetFilePath);
		final Configuration conf = new Configuration();
		
		List<TrainingData> ret=new ArrayList<TrainingData>();
		try (final ParquetReader<Row> reader = RowParquetReader.builder(HadoopInputFile.fromPath(path, conf)).build()) {
			
			Row row = reader.read();
			while (row != null && rowsreaded<1000) {
				List<String> fieldNames=row.getFieldNames();
				
				//for debuging ...
				//printFieldNames(fieldNames,row);
				ret.add(new TrainingData(row));
				
				row = reader.read();
				rowsreaded++;
			}
		} catch (final IOException exception) {

		}
		return ret;
	}

	private void printFieldNames(List<String> fieldNames, Row row) {
		for(String f:fieldNames) {
			//System.out.println("field="+f);
			//System.out.println("value="+row.getValue(f));
		}
	}

}
