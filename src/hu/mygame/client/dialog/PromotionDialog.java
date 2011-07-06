package hu.mygame.client.dialog;

import hu.mygame.client.chessboard.ActionHandler;
import hu.mygame.shared.Position;
import hu.mygame.shared.PromotionPiece;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class PromotionDialog extends DialogBox {

	interface PromotionDialogUiBinder extends UiBinder<Widget, PromotionDialog> {
	}
	private static final PromotionDialogUiBinder uiBinder = GWT.create(PromotionDialogUiBinder.class);
	private ActionHandler actionHandler = null;
	@UiField
	PushButton button_bishop;
	@UiField
	PushButton button_knight;

	@UiField
	PushButton button_queen;
	@UiField
	PushButton button_rook;
	@UiField
	DialogBox dialog;
	@UiField
	Label label;

	private Position selected = null;

	private Position target = null;

	public PromotionDialog(Position selected, Position target, ActionHandler actionHandler) {
		setWidget(uiBinder.createAndBindUi(this));
		this.selected = selected;
		this.target = target;
		this.actionHandler = actionHandler;
		dialog.setPopupPositionAndShow(new PositionCallback() {
			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				dialog.setPopupPosition((Window.getClientWidth() - offsetWidth) / 2,
						(Window.getClientHeight() - offsetHeight) / 2);
			}
		});
	}

	@UiHandler("button_bishop")
	void onBishop(ClickEvent e) {
		dialog.hide();
		actionHandler.makeMove(selected, target, PromotionPiece.BISHOP);
	}
	@UiHandler("button_knight")
	void onKnight(ClickEvent e) {
		dialog.hide();
		actionHandler.makeMove(selected, target, PromotionPiece.KNIGHT);
	}
	@UiHandler("button_queen")
	void onQueen(ClickEvent e) {
		dialog.hide();
		actionHandler.makeMove(selected, target, PromotionPiece.QUEEN);
	}
	@UiHandler("button_rook")
	void onRook(ClickEvent e) {
		dialog.hide();
		actionHandler.makeMove(selected, target, PromotionPiece.ROOK);
	}
}
