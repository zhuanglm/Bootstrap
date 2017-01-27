package com.securespaces.wizard;

/**
 * Created by eric on 27/01/17.
 */

public interface BootstrapContract {
    interface View {
    }
    interface UserActionsListener {
        void onProceed();
        void onBack();
        void onDestroy();
    }
}
