
package dane.asdra.Utils;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;

import dane.asdra.R;

public class EditNameDialog extends DialogFragment {

    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }

    private EditText editText;

    public EditNameDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_name, container);
        editText = (EditText) view.findViewById(R.id.txt_yourName);

        // Show soft keyboard automatically
        editText.requestFocus();
        getDialog().setTitle(getString(R.string.ingrese_nombre));
        getDialog().getWindow().setGravity(Gravity.FILL_HORIZONTAL);
        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // Return input text to activity
        editText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    EditNameDialogListener activity = (EditNameDialogListener) getActivity();
                    activity.onFinishEditDialog(editText.getText().toString());
                    // EditNameDialog.this.dismiss();
                    return true;
                }
                return false;
            }
        });

        return view;
    }
}
