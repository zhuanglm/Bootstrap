package com.securespaces.wizard;

/**
 * Created by hfarah on 31/03/17.
 */
public interface ILoadable {
    // this interface specifies that a page has loadable elements
    // for example, a background image
    // we need to wait for the items to be loaded before the page can be displayed

    // the single current usage is in the intro fragment where the page loads its
    // background then tells the bootstrap controller to proceed when the loading is
    // completed
}
