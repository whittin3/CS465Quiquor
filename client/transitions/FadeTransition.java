package client.transitions;

import client.ViewController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * User: Neal Eric
 * Date: 11/11/13
 */
public class FadeTransition implements ViewTransition {

	@Override
	public void in(String name, ViewController viewController) {
		final DoubleProperty opacity = viewController.opacityProperty();
		viewController.setOpacity(0.0);
		Node parentNode = viewController.getViews().get(name);
		viewController.getChildren().add(parentNode);
		Timeline fadeIn = new Timeline(
				new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
				new KeyFrame(new Duration(500), new KeyValue(opacity, 1.0)));
		fadeIn.play();
	}

	@Override
	public void out(final String name, final ViewController viewController) {
		final DoubleProperty opacity = viewController.opacityProperty();
		if (!viewController.getChildren().isEmpty()) {
			Timeline fade = new Timeline(
					new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
					new KeyFrame(new Duration(100), new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent t) {
							viewController.getChildren().remove(0);
							viewController.getChildren().add(0, viewController.getViews().get(name));
							Timeline fadeIn = new Timeline(
									new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
									new KeyFrame(new Duration(800), new KeyValue(opacity, 1.0)));
							fadeIn.play();
						}
					}, new KeyValue(opacity, 0.0)));
			fade.play();
		}
	}
}
