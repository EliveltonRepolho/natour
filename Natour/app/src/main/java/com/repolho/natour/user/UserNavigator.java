package com.repolho.natour.user;

/**
 * Listener used with data binding to process user actions.
 */
public interface UserNavigator {

    void onLogout();

    /**
     * The user can check if want the welcome page again
     */
    void onRestartIntro();

    /**
     * Link for the user to answer the questions about the usage of app in tests period
     *
     * @param: url Link the web page
     */
    void answerOnlineForm(String url);

}
