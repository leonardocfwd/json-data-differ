## JSON Binary Data Differ

This is a tool to diff two JSON Base64 binary data and provide the comparison results in JSON Format.

It is composed by three HTTP endpoints:

* <host>/v1/diff/<ID>/left (receives JSON Base64 binary data)
* <host>/v1/diff/<ID>/right (receives JSON Base64 binary data)
* <host>/v1/diff/<ID> (provides the diff results)

The diff HTTP endpoint will give you three possible outputs depending upon the left and right files you upload:

* Files are equal. (if both byte arrays are the same)
* Files do not have the same size. (if both byte arrays have different sizes)
* Files have the same size. (you will also get an array with the diff offsets and the files length, when both files have the same size)

### Usage

    mvn spring-boot:run
	
An embedded tomcat container will be started. You can access a front-end tool to upload the binaries and check the results at localhost:8080.

Steps to use:

* Enter the diff id on the text input. It will be used to create a new diff instance on the database and to fetch the diff results.
* Select the left file to be uploaded.
* Select the right file to be uploaded.
* Click on View Results Button.
* A modal pop-up will be opened showing the results in the JSON format.

You can use the files within `resources/files` folders. There is a combination of right and left files for each situation (equal, not_equal_size, same_size). Feel free to test also with your own files.