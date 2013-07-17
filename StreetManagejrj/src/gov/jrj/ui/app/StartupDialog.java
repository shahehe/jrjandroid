package gov.jrj.ui.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import gov.jrj.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;


public class StartupDialog extends Dialog {

	public StartupDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public StartupDialog(Context context, int dialog) {
		// TODO Auto-generated constructor stub
		super(context,dialog);

	}

	public static class Builder {

		private Context context;
		private String title;
		private String message;
		private String image;
		private String positiveButtonText;
		private String negativeButtonText;
		private String noMoreButtonText;
		private View contentView;

		private DialogInterface.OnClickListener positiveButtonClickListener,
				negativeButtonClickListener, noMoreButtonClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}


		/**
		 * Set a custom content view for the Dialog. If a message is set, the
		 * contentView is not added to the Dialog...
		 * 
		 * @param v
		 * @return
		 */
		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @param listener
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		/**
		 * Set the positive button text and it's listener
		 * 
		 * @param positiveButtonText
		 * @param listener
		 * @return
		 */
		public Builder setPositiveButton(String positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		/**
		 * Set the negative button resource and it's listener
		 * 
		 * @param negativeButtonText
		 * @param listener
		 * @return
		 */
		public Builder setNegativeButton(int negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		/**
		 * Set the negative button text and it's listener
		 * 
		 * @param negativeButtonText
		 * @param listener
		 * @return
		 */
		public Builder setNegativeButton(String negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		/**
		 * Set the noMore button text and it's listener
		 * 
		 * @param noMoreButtonText
		 * @param listener
		 * @return
		 */
		public Builder setNoMoreButton(String noMoreButtonText,
				DialogInterface.OnClickListener listener) {
			this.noMoreButtonText = noMoreButtonText;
			this.noMoreButtonClickListener = listener;
			return this;
		}

		public Builder setNoMoreButton(int noMoreButtonText,
				DialogInterface.OnClickListener listener) {
			this.noMoreButtonText = (String) context.getText(noMoreButtonText);
			this.noMoreButtonClickListener = listener;
			return this;
		}

		public StartupDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final StartupDialog dialog = new StartupDialog(context,
					R.style.Dialog);
			View layout = inflater.inflate(R.layout.alertdialog_startup, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			((TextView) layout.findViewById(R.id.title)).setText(title);
			// set the confirm button
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.positiveButton))
						.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					((Button) layout.findViewById(R.id.positiveButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.positiveButton).setVisibility(
						View.GONE);
			}
			
			if (noMoreButtonText != null) {
				((Button) layout.findViewById(R.id.noMoreButton))
						.setText(noMoreButtonText);
				if (noMoreButtonClickListener != null) {
					((Button) layout.findViewById(R.id.noMoreButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									noMoreButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.noMoreButton).setVisibility(
						View.GONE);
			}
			// set the cancel button
			if (negativeButtonText != null) {
				((Button) layout.findViewById(R.id.negativeButton))
						.setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					((Button) layout.findViewById(R.id.negativeButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.negativeButton).setVisibility(
						View.GONE);
			}
			// set the content message
			if (message != null) {
				((TextView) layout.findViewById(R.id.message)).setText(message);
			} else if (contentView != null) {
				// if no message set
				// add the contentView to the dialog body
				((LinearLayout) layout.findViewById(R.id.content))
						.removeAllViews();
				((LinearLayout) layout.findViewById(R.id.content)).addView(
						contentView, new LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT));
			}
			
			if(image != null){
				Log.v("StartDialog",image);
				try {
					FileInputStream fis = new FileInputStream(image);
					Bitmap bitmap =BitmapFactory.decodeStream(fis);
					((ImageView) layout.findViewById(R.id.startimage)).setImageBitmap(bitmap);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				
			}
			dialog.setContentView(layout);
			return dialog;

		}

		public void setImageView(String string) {
			// TODO Auto-generated method stub
			image = string;
		}

	}
}
