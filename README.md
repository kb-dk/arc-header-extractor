ARC Header Extractor tool
================================

This tool extracts the metadata for each record in an Internet Archive file (ARC). The tool uses the [Java Web Archive Toolkit](https://sbforge.org/display/JWAT/JWAT;jsessionid=4561692C2CE9F27765C9E835F7A994D9) (JWAT) and is heavily inspired by [JWAT-tools](https://sbforge.org/display/JWAT/JWAT-Tools).

## Usage

The package is build with Maven

	mvn package 

This command generates a tar ball which includes the necessary JAR files, a UNIX shell script for invoking the tool and some other files.

    → ./headerextractor.sh 
	Usage: headerextractor.sh {input} {output}
	{input} ARC file or directory of ARC files
	{output} output directory
	
Invoking the script creates a new file for each record within the ARC file. These new files each contain the ARC header information for the associated ARC record.