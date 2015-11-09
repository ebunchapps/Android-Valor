package ul.aws;

import java.io.File;

import ul.aws.util.Util;
import android.content.Context;
import android.os.Environment;
import android.widget.ImageView;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.mobileconnectors.s3.transfermanager.PersistableTransfer;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.mobileconnectors.s3.transfermanager.internal.S3ProgressListener;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AWSManager {

	private static AWSManager mInstance = null;

	public String AWSAccountID = null;
	public String CognitoPoolID = null;
	public String CognitoRoleAuth = null;
	public String CognitoRoleUnauth = null;
	public String S3BucketName = null;

	public static AWSManager getInstance() {
		if (mInstance == null) {
			mInstance = new AWSManager();
		}
		return mInstance;
	}

	public void init(String AWSAccountID, String CognitoPoolID,
			String CognitoRoleAuth, String CognitoRoleUnauth,
			String S3BucketName) {
		this.AWSAccountID = AWSAccountID;
		this.CognitoPoolID = CognitoPoolID;
		this.CognitoRoleAuth = CognitoRoleAuth;
		this.CognitoRoleUnauth = CognitoRoleUnauth;
		this.S3BucketName = S3BucketName;
	}

	private String getFileExtension(String name) {
		int lastIndexOf = name.lastIndexOf(".");
		if (lastIndexOf == -1) {
			return ""; // empty extension
		}
		return name.substring(lastIndexOf);
	}

	/**
	 * 
	 * @param fileType
	 *            IMAGE or AUDIO
	 * @param bucket
	 *            without leading /
	 * @param filename
	 *            without leading /
	 * @param path
	 *            with loading /
	 * @return
	 */
	public String downloadImageFile(final Context context, final String bucket,
			String filenamearg, final String path, final ImageView imageView) {
		File file = new File(context.getCacheDir() + "/" + bucket + "/" + path);
		file.mkdirs();

		if (getFileExtension(filenamearg).equals("")) {
			filenamearg += ".png";
		}
		final String filename = filenamearg;
		final File fileToSave = new File(context.getCacheDir() + "/" + bucket
				+ "/" + path + "/" + filename);

		if (fileToSave.exists()) {
			ImageLoader.getInstance().displayImage(
					"file://" + fileToSave.getAbsolutePath(), imageView);
			return fileToSave.getAbsolutePath();
		}
		final AWSManager awsmanger = this;
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					AmazonS3Client s3Client = Util.getS3Client(context,
							awsmanger);
					GetObjectRequest gor = new GetObjectRequest(bucket, path
							+ filename);
					TransferManager tm = new TransferManager(s3Client);
					tm.download(gor, fileToSave, new S3ProgressListener() {

						@Override
						public void progressChanged(ProgressEvent arg0) {
							if (arg0.getEventCode() == ProgressEvent.COMPLETED_EVENT_CODE) {
								ImageLoader.getInstance().displayImage(
										"file://"
												+ fileToSave.getAbsolutePath(),
										imageView);
							}

						}

						@Override
						public void onPersistableTransfer(
								PersistableTransfer arg0) {
							// TODO Auto-generated method stub

						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();

		return file.getAbsolutePath();
	}
}
