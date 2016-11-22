package com.dark.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;

/**
 * amazon s3 implement
 *
 */
public class AmazonS3FileResourceOp {

    private static final Logger logger = LoggerFactory.getLogger(AmazonS3FileResourceOp.class);

    private String accessKey = "";
    private String secretKey = "";
    private String bucketName = "";

    private AmazonS3 s3;

    public AmazonS3FileResourceOp() {}

    private void connectAws() {
        if (s3 != null) {
            return;
        }
        synchronized (logger) {
            logger.info("--- init Amazon S3 start ---- ");
            long d1 = System.currentTimeMillis();

            AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            s3 = new AmazonS3Client(credentials);

            logger.info("--- init Amazon S3 end, it used time : "
                    + (System.currentTimeMillis() - d1) + " ms.");
        }
    }

    public boolean copy(File file, String destFile) {
        try {
            ObjectMetadata om = new ObjectMetadata();
            connectAws();

            long start = System.currentTimeMillis();
            s3.putObject(new PutObjectRequest(bucketName, destFile, file).withMetadata(om)
                    .withCannedAcl(CannedAccessControlList.PublicRead)); // 设置权限
            logger.info("copy file :" + destFile + " to amazon s3 success !, cost {}",
                    System.currentTimeMillis() - start);
            return true;
        } catch (Exception e) {
            logger.error(
                    "copy file :" + destFile + " to amazon s3 error :" + e.getLocalizedMessage(),
                    e);
        }
        return false;
    }

    public boolean copy(InputStream inputStream, String destFile, ObjectMetadata metaData) {
        try {
            connectAws();

            long start = System.currentTimeMillis();
            s3.putObject(new PutObjectRequest(bucketName, destFile, inputStream, metaData)
                    .withCannedAcl(CannedAccessControlList.PublicRead)); // 设置权限
            logger.info("copy file :" + destFile + " to amazon s3 success !, cost {}",
                    System.currentTimeMillis() - start);
            return true;
        } catch (Exception e) {
            logger.error(
                    "copy file :" + destFile + " to amazon s3 error :" + e.getLocalizedMessage(),
                    e);
        }
        return false;
    }

}
