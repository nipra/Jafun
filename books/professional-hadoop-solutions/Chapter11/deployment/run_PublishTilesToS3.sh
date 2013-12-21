#!/bin/bash

#--------- DEFAULT SETTINGS ------------------------------------------------

jarFile=ec-s3publisher-*-jar-with-dependencies.jar

tilepublishClass=com.nokia.ec.s3copytool.tile.TilePublisherHadoopJob

landmarkPublishClass=com.nokia.ec.s3copytool.landmark.LandmarksPublisher

landmarkChangeDetectClass=com.nokia.ec.s3copytool.landmark.LandmarksVersioner

#number for mappers for S3 publisher
mappers=50
threads=20

# can set the log level with -logLevel TRACE|DEBUG|INFO|WARN|ERROR
logLevel=INFO
logFile=output.log

#--------------- TO BE CONFIGURED SETTINGS -------------------------------

# New log file to maintain the last run time
timeLogFile=/disk1/hadoopUsers/earthcore/landmarkpub/timelogFile.log

# S3 bucket name
s3PublicationBucket=ec-publication-test

# credentials properties file path in local
#awsCredentialsFile=my-aws-credentials.properties

# Lookup file to maintain the changed landmarks.
# Should be a new HDFS file location in an existing HDFS directory
lookupFilePath=/user/earthcoredpl/schaphal/test/testlookup.txt
lookupFile=hdfs://sachicn001.hq.navteq.com:8020/user/earthcoredpl/schaphal/test/testlookup.txt

# Path in HDFS where the feature tiles are located.
hdfsFeatureTypePath=/user/earthcoredpl/schaphal/landmarksPub/orignal

# Feature type which is being published.
featureType=building

# Optional if needed to only push a subset of tilekey
tileKey=

# Path in HDFS where the landmarks are located.
hdfsLandmarkPath="/user/earthcoredpl/schaphal/landmarkspub/landmarks"

# Local directory from where the landmarks need to be copied to HDFS for publishing
landmarksLocalDirectory="/disk1/hadoopUsers/earthcore/landmarkpub/US_CA_SANFRANCISCO_101CALIFORNIA/"


#--------------------Landmarks storage copy -------------------------
# copy all the landmarks to HDFS directory
hadoop fs -mkdir ${hdfsLandmarkPath}
hadoop fs -put ${landmarksLocalDirectory}* ${hdfsLandmarkPath}

#-------------------- Landmarks Versioner and Landmark Publisher ---------------------------------------

# Get the last time the job was run
  touch ${timeLogFile}
  lastRunDate=`cat $timeLogFile`

  current=$(date "+%Y%m%d%H%M")

  java -cp ${jarFile} ${landmarkChangeDetectClass} -lookupFilePath ${lookupFile} -lastRunDate "${lastRunDate}" -currentDate "${current}"

  hadoop jar ${jarFile} ${landmarkPublishClass} -hdfsLandmarkPath "${hdfsLandmarkPath}" -s3PublicationBucket "${s3PublicationBucket}" -mappers ${mappers} -threads ${threads} -logLevel ${logLevel} -lookupFilePath ${lookupFilePath} > ${logFile}

  rc=$? 
  if [[ $rc != 0 ]] ; then
    exit $rc
  fi


#-------------------- Feature Tile Publisher ---------------------------------------
if test -z ${tileKey}; then
  hadoop jar ${jarFile} ${tilepublishClass} -featureType ${featureType} -hdfsFeatureTypePath "${hdfsFeatureTypePath}" -s3PublicationBucket "${s3PublicationBucket}" -mappers ${mappers} -threads ${threads} -logLevel ${logLevel} -lookupFilePath ${lookupFilePath} > ${logFile}
else
  hadoop jar ${jarFile} ${tilepublishClass} -featureType ${featureType} -tile ${tileKey} -hdfsFeatureTypePath "${hdfsFeatureTypePath}" -s3PublicationBucket "${s3PublicationBucket}" -mappers ${mappers} -threads ${threads} -logLevel ${logLevel} -lookupFilePath ${lookupFilePath} > ${logFile}
fi

# Write the last run time to a file.
echo "$current" > $timeLogFile
