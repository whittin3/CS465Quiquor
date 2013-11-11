package client.transitions;

import client.ViewController;

/**
 * User: Neal Eric
 * Date: 11/11/13
 */
public interface ViewTransition {
    public void in(final String name, final ViewController viewController);
    public void out(final String name, final ViewController viewController);
}
