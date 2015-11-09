/*
 * Copyright 2010-2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package ul.aws.util;

import ul.aws.AWSManager;
import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

/* 
 * This class just handles getting the client since we don't need to have more than
 * one per application
 */
public class Util {
	private static AmazonS3Client sS3Client;
	private static CognitoCachingCredentialsProvider sCredProvider;

	public static CognitoCachingCredentialsProvider getCredProvider(
			Context context, AWSManager awsManger) {
		if (sCredProvider == null) {
			sCredProvider = new CognitoCachingCredentialsProvider(context,
					awsManger.AWSAccountID, awsManger.CognitoPoolID,
					awsManger.CognitoRoleUnauth, null, Regions.US_EAST_1);
		}
		return sCredProvider;
	}

	public static AmazonS3Client getS3Client(Context context,
			AWSManager awsManger) {
		if (sS3Client == null) {
			sS3Client = new AmazonS3Client(getCredProvider(context, awsManger));
		}
		return sS3Client;
	}

	public static String getFileName(String path) {
		return path.substring(path.lastIndexOf("/") + 1);
	}
}